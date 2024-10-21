package com.project.tailsroute.controller;


import com.opencsv.CSVReader;
import com.project.tailsroute.service.HospitalService;
import com.project.tailsroute.vo.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Controller
public class UsrHospitalController {

    // 루트 디렉토리에 있는 .env 파일에서 API 키를 불러옴.
    @Value("${GOOGLE_MAP_API_KEY}")
    private String API_KEY;

    @Autowired
    private HospitalService hospitalService;

    @GetMapping("/usr/hospital/main")
    public String showMain(Model model) {

        model.addAttribute("GOOGLE_MAP_API_KEY", API_KEY);
        model.addAttribute("message", "24시간 병원 message 추가");
        return "usr/map/hospital";
    }

    // 테스트용 코드
    @RequestMapping("/usr/hospital/showCSVData")
    @ResponseBody
    public String showCSVData() {

        StringBuilder output = new StringBuilder(); // CSV 파일 내용을 담을 StringBuilder

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("csv/data_veterinary_care.csv");
             CSVReader reader = new CSVReader(new InputStreamReader(inputStream, "UTF-8"))) {

            if (inputStream == null) {
                throw new FileNotFoundException("CSV file not found in resources folder");
            }

            String[] nextLine;

            // CSV 파일에서 한 줄씩 읽어와 StringBuilder에 추가
            while ((nextLine = reader.readNext()) != null) {
                for (String token : nextLine) {
                    // HTML 인젝션 방지를 위해 이스케이프 처리
                    output.append(escapeHtml(token)).append(" "); // 각 CSV 값을 공백으로 구분하여 추가
                }
                output.append("<br>"); // 줄 바꿈 추가 (HTML)
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred: " + e.getMessage();
        }

        return "showCSVData 성공! <hr>" + output.toString(); // CSV 파일 내용 출력
    }

    // HTML 인젝션 방지를 위한 메소드
    private String escapeHtml(String input) {
        if (input == null) return null;
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    // 일회성 코드(DB에 .csv 파일의 내용 넣기)
    @RequestMapping("/usr/hospital/importCsvToDatabase")
    @ResponseBody
    public String importCsvToDatabase() {

        StringBuilder output = new StringBuilder(); // 로그 출력용 StringBuilder
        CSVReader reader = null;

        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("csv/data_veterinary_care.csv");

            if (inputStream == null) {
                throw new FileNotFoundException("CSV file not found in resources folder");
            }

            reader = new CSVReader(new InputStreamReader(inputStream, "UTF-8"));
            String[] nextLine;

            // CSV 파일에서 한 줄씩 읽어오기
            while ((nextLine = reader.readNext()) != null) {

                // 번호,소재지전화,소재지전체주소,도로명전체주소,사업장명 순으로 저장됨. 0~4

                String id = nextLine[0]; // 번호
                String callNumber = nextLine[1]; // 소재지 전화
                String addressLocation = nextLine[2]; // 소재지 전체 주소
                String addressStreet = nextLine[3];// 도로명 전체주소
                String name = nextLine[4]; // 사업장 명

                System.err.println(id);
                // int temp = Integer.parseInt(id);

                hospitalService.doInsertHospitalInfo(callNumber, addressLocation, addressStreet, name);

                output.append("Inserted: ").append(id).append(", ").append(callNumber).append(", ").append(addressLocation)
                        .append(", ").append(addressStreet).append(", ").append(name).append("<hr>");

            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred: " + e.getMessage();
        } finally {
            try {
                if (reader != null) {
                    reader.close(); // 리소스 해제
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "CSV to DB Insert 성공! <hr>" + output.toString();
    }

    @RequestMapping("/usr/hospital/hospitals")
    public String showHospitals(Model model) {
        List<Hospital> hospitals = hospitalService.getAllHospitals();
        model.addAttribute("hospitals", hospitals);
        return "usr/hospital/hospitals"; // hospitalList.html 또는 hospitalList.jsp
    }

}