package com.project.tailsroute.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class UsrMapController_가나다순 {

    // 시/도와 군/구 데이터를 하드코딩
    private Map<String, List<String>> cityCountyMap = new TreeMap<>(); // TreeMap으로 시/도 정렬

    public UsrMapController_가나다순() {

        cityCountyMap.put("서울특별시", sortList(Arrays.asList("종로구", "중구", "용산구", "성동구", "광진구", "동대문구", "중랑구", "성북구", "강북구", "도봉구", "노원구", "은평구", "서대문구", "마포구", "양천구", "강서구", "구로구", "금천구", "영등포구", "동작구", "관악구", "서초구", "강남구", "송파구", "강동구")));
        cityCountyMap.put("부산광역시", sortList(Arrays.asList("중구", "서구", "동구", "영도구", "부산진구", "동래구", "남구", "북구", "해운대구", "사하구", "금정구", "강서구", "연제구", "수영구", "사상구", "기장군")));
        cityCountyMap.put("대구광역시", sortList(Arrays.asList("중구", "동구", "서구", "남구", "북구", "수성구", "달서구", "달성군", "군위군")));
        cityCountyMap.put("인천광역시", sortList(Arrays.asList("중구", "동구", "미추홀구", "연수구", "남동구", "부평구", "계양구", "서구", "강화군", "옹진군")));
        cityCountyMap.put("광주광역시", sortList(Arrays.asList("동구", "서구", "남구", "북구", "광산구")));
        cityCountyMap.put("대전광역시", sortList(Arrays.asList("동구", "중구", "서구", "유성구", "대덕구")));
        cityCountyMap.put("울산광역시", sortList(Arrays.asList("중구", "남구", "동구", "북구", "울주군")));
        // 세종특별자치시: 군/구 없음
        cityCountyMap.put("세종특별자치시", Collections.emptyList());

        cityCountyMap.put("경기도", sortList(Arrays.asList("수원시", "용인시", "고양시", "화성시", "성남시", "부천시", "남양주시", "안산시", "평택시", "안양시", "시흥시", "파주시", "김포시", "의정부시", "광주시", "하남시", "광명시", "군포시", "양주시", "오산시", "이천시", "안성시", "구리시", "의왕시", "포천시", "양평군", "여주시", "동두천시", "과천시", "가평군", "연천군")));

        cityCountyMap.put("강원특별자치도", sortList(Arrays.asList("춘천시", "원주시", "강릉시", "동해시", "태백시", "속초시", "삼척시", "홍천군", "횡성군", "영월군", "평창군", "정선군", "철원군", "화천군", "양구군", "인제군", "고성군", "양양군")));

        cityCountyMap.put("충청북도", sortList(Arrays.asList("청주시", "충주시", "제천시", "보은군", "옥천군", "영동군", "증평군", "진천군", "괴산군", "음성군", "단양군")));

        cityCountyMap.put("충청남도", sortList(Arrays.asList("천안시", "공주시", "보령시", "아산시", "서산시", "논산시", "계룡시", "당진시", "금산군", "부여군", "서천군", "청양군", "홍성군", "예산군", "태안군")));

        cityCountyMap.put("전북특별자치도", sortList(Arrays.asList("전주시", "군산시", "익산시", "정읍시", "남원시", "김제시", "완주군", "진안군", "무주군", "장수군", "임실군", "순창군", "고창군", "부안군")));

        cityCountyMap.put("전라남도", sortList(Arrays.asList("목포시", "여수시", "순천시", "나주시", "광양시", "담양군", "곡성군", "구례군", "고흥군", "보성군", "화순군", "장흥군", "강진군", "해남군", "영암군", "무안군", "함평군", "영광군", "장성군", "완도군", "진도군", "신안군")));

        cityCountyMap.put("경상북도", sortList(Arrays.asList("포항시", "경주시", "김천시", "안동시", "구미시", "영주시", "영천시", "상주시", "문경시", "경산시", "의성군", "청송군", "영양군", "영덕군", "청도군", "고령군", "성주군", "칠곡군", "예천군", "봉화군", "울진군", "울릉군")));

        cityCountyMap.put("경상남도", sortList(Arrays.asList("창원시", "진주시", "통영시", "사천시", "김해시", "밀양시", "거제시", "양산시", "의령군", "함안군", "창녕군", "고성군", "남해군", "하동군", "산청군", "함양군", "거창군", "합천군")));

        cityCountyMap.put("제주특별자치도", sortList(Arrays.asList("제주시", "서귀포시")));

    }

    // 리스트 정렬 메서드
    private List<String> sortList(List<String> list) {
        Collections.sort(list); // 사전순 정렬
        return list;
    }

    // 시 리스트를 반환
    //@GetMapping("/cities")
    @ResponseBody
    public List<String> getCities() {
        return new ArrayList<>(cityCountyMap.keySet());
    }


    // 선택한 시에 따라 군/구 리스트를 반환
    //@GetMapping("/counties")
    @ResponseBody
    public List<String> getCounties(@RequestParam("city") String city) {
        return cityCountyMap.getOrDefault(city, Collections.emptyList());
    }
}
