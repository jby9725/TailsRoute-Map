package com.project.tailsroute.repository;

import com.project.tailsroute.vo.Hospital;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HospitalRepository {

    @Insert("""
        INSERT INTO `hospital` (name, callNumber, jibunAddress, roadAddress)
        VALUES (#{name}, #{callNumber}, #{addressLocation}, #{addressStreet})
    """)
    void doInsertHospitalInfo(String callNumber, String addressLocation, String addressStreet, String name);

    @Select("SELECT * FROM hospital")
    List<Hospital> findAllHospitals();
}
