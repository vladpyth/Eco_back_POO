package com.example.eco_service.repositories;

import com.example.eco_service.entities.MagasinFactory;
import com.example.eco_service.entities.MagazinTrash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterfMagazinTrash extends JpaRepository<MagazinTrash, Long>, JpaSpecificationExecutor<MagazinTrash>, RevisionRepository<MagazinTrash , Long, Integer> {

}