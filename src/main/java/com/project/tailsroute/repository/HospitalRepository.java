package com.project.tailsroute.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HospitalRepository {

    @Insert("""
        INSERT INTO `hospital` (name, callNumber, jibunAddress, roadAddress)
        VALUES (#{name}, #{callNumber}, #{addressLocation}, #{addressStreet})
    """)
    void doInsertHospitalInfo(String callNumber, String addressLocation, String addressStreet, String name);
}
