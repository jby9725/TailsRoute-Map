package com.project.tailsroute.service;

import com.project.tailsroute.repository.HospitalRepository;
import com.project.tailsroute.vo.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    public HospitalService(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    public void doInsertHospitalInfo(String callNumber, String addressLocation, String addressStreet, String name) {
        hospitalRepository.doInsertHospitalInfo(callNumber, addressLocation, addressStreet, name);
    }

    public List<Hospital> getAllHospitals() {
        return hospitalRepository.findAllHospitals();
    }
}
