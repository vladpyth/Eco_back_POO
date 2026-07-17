package com.example.eco_service.repositories;


import com.example.eco_service.entities.Cities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterfCities extends JpaRepository<Cities, Long>, JpaSpecificationExecutor<Cities>, RevisionRepository<Cities, Long, Integer> {

}
