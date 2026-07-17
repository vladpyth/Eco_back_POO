package com.example.eco_service.repositories;


import com.example.eco_service.entities.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterfRegion extends JpaRepository<Region, Long>, JpaSpecificationExecutor<Region>, RevisionRepository<Region, Long, Integer> {

}
