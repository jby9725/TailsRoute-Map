package com.project.tailsroute.service;

import com.project.tailsroute.repository.DogRepository;
import com.project.tailsroute.repository.MissingRepository;
import com.project.tailsroute.vo.Missing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MissingService {
    @Autowired
    private MissingRepository missingRepository;

    public int lastNumber() {
        Integer number = missingRepository.lastNumber();
        return (number != null) ? number : 0; // null일 경우 0을 반환
    }

    public void write(int loginedMemberId, String name, String reportDate, String missingLocation, String breed, String color, String gender, String age, String rfid, String photoPath, String trait) {
        missingRepository.write(loginedMemberId, name, reportDate, missingLocation, breed, color, gender, age, rfid, photoPath, trait);
    }

    public int totalCnt(String str) {
        return missingRepository.totalCnt(str);
    }

    public List<Missing> list(int limitFrom, int itemsInAPage, String str) {
        return missingRepository.list(limitFrom, itemsInAPage, str);
    }

    public Missing missingArticle(int missingId) {
        return missingRepository.missingArticle(missingId);
    }

    public void missingDelete(int missingId) {
        missingRepository.missingDelete(missingId);
    }

    public void modify(int id, String name, String reportDate, String missingLocation, String breed, String color, String gender, String age, String rfid, String photoPath, String trait) {
        missingRepository.modify(id, name, reportDate, missingLocation, breed, color, gender, age, rfid, photoPath, trait);
    }
}
