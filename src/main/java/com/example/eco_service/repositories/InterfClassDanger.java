package com.example.eco_service.repositories;


import com.example.eco_service.entities.Cities;
import com.example.eco_service.entities.ClassDanger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterfClassDanger extends JpaRepository<ClassDanger, Long>, JpaSpecificationExecutor<ClassDanger>, RevisionRepository<ClassDanger, Long, Integer> {

}
