package com.project.tailsroute.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class PlaceApiService {

    // 루트 디렉토리에 있는 .env 파일에서 API 키를 불러옴.
    @Value("${GOOGLE_MAP_API_KEY}")
    private String API_KEY; // 실제 API 키를 사용

    public String getPlaceId(String hospitalName, String latitude, String longitude) {
        String apiUrl = "https://maps.googleapis.com/maps/api/place/autocomplete/json";
        String requestUrl = apiUrl + "?input=" + hospitalName + "&location=" + latitude + "," + longitude + "&radius=1&type=veterinary_care&key=" + API_KEY;

        System.out.println("requestUrl : " + requestUrl);

        try {
            URL url = new URL(requestUrl);
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

            JSONObject jsonResponse = new JSONObject(content.toString());
            JSONArray predictions = jsonResponse.getJSONArray("predictions");

            if (predictions.length() > 0) {
                return predictions.getJSONObject(0).getString("place_id");
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}