package com.example.eco_service.repositories;

import com.example.eco_service.entities.ClassDanger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InterfClassDanger extends JpaRepository<ClassDanger, Long>, JpaSpecificationExecutor<ClassDanger>, RevisionRepository<ClassDanger, Long, Integer> {

    @Query("select c from ClassDanger c where c.class_danger = :v")
    Optional<ClassDanger> findByClassDangerValue(@Param("v") int v);
}
