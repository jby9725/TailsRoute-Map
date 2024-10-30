package com.project.tailsroute.service;

import com.project.tailsroute.repository.HospitalRepository;
import com.project.tailsroute.vo.Hospital;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;

@Service
public class HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private PlaceApiService placeApiService;

    // 루트 디렉토리에 있는 .env 파일에서 API 키를 불러옴.
    @Value("${GOOGLE_MAP_API_KEY}")
    private String API_KEY;

    public void updatePlaceIdsForHospitals() {
        List<Hospital> hospitals = hospitalRepository.findAllHospitals();
        for (Hospital hospital : hospitals) {
            try {
                String placeId = placeApiService.getPlaceId(hospital.getName(), hospital.getLatitude(), hospital.getLongitude());
                if (placeId != null) {
                    hospitalRepository.updatePlaceId(hospital.getId(), placeId);
                }
            } catch (Exception e) {
                System.out.println("Error updating place_id for hospital " + hospital.getName() + ": " + e.getMessage());
            }
        }
    }

    // Google Place Autocomplete API를 호출하여 place_id를 얻는 메소드
    public String getPlaceIdByNameAndLocation(String hospitalName, double latitude, double longitude) {
        try {
            String urlString = String.format("https://maps.googleapis.com/maps/api/place/autocomplete/json?" +
                            "input=%s&location=%f,%f&radius=1&key=%s",
                    hospitalName, latitude, longitude, API_KEY);

            URL url = new URL(urlString);

            System.err.println("URL: " + urlString);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();

            JSONObject json = new JSONObject(content.toString());
            JSONArray predictions = json.getJSONArray("predictions");

            if (predictions.length() > 0) {
                return predictions.getJSONObject(0).getString("place_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 병원 이름과 좌표로 place_id를 업데이트하는 메소드
    public void updateHospitalWithPlaceId(String hospitalName, double latitude, double longitude) {
        Optional<Hospital> hospitalOptional = hospitalRepository.findByName(hospitalName);

        if (hospitalOptional.isPresent()) {
            String placeId = getPlaceIdByNameAndLocation(hospitalName, latitude, longitude);
            if (placeId != null) {
                Hospital hospital = hospitalOptional.get();
                hospitalRepository.updatePlaceId(hospital.getId(), placeId);
            }
        }
    }

    public HospitalService(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    public void doInsertHospitalInfo(String callNumber, String addressLocation, String addressStreet, String name) {
        hospitalRepository.doInsertHospitalInfo(callNumber, addressLocation, addressStreet, name);
    }

    public List<Hospital> getAllHospitals() {
        return hospitalRepository.findAllHospitals();
    }

    public List<Hospital> getHospitalsWithoutCoordinates() {
        return hospitalRepository.getHospitalsWithoutCoordinates();
    }

    public void updateHospitalCoordinates(int id, String latitude, String longitude) {
        hospitalRepository.updateHospitalCoordinates(id, latitude, longitude);
    }
}
