package com.example.eco_service.repositories;

import com.example.eco_service.entities.Region;
import com.example.eco_service.entities.ShortDiscribeTechnology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InterfShortDiscribeTechnology extends JpaRepository<ShortDiscribeTechnology, Long>, JpaSpecificationExecutor<ShortDiscribeTechnology>, RevisionRepository<ShortDiscribeTechnology, Long, Integer> {

}
