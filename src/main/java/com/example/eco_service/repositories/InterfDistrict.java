package com.example.eco_service.repositories;


import com.example.eco_service.entities.Cities;
import com.example.eco_service.entities.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterfDistrict extends JpaRepository<District, Long>, JpaSpecificationExecutor<District>, RevisionRepository<District, Long, Integer> {

}
