package com.example.eco_service.repositories;


import com.example.eco_service.entities.PhysStateTrash;
import com.example.eco_service.entities.Technology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterfPhysStateTrash extends JpaRepository<PhysStateTrash, Long>, JpaSpecificationExecutor<PhysStateTrash>, RevisionRepository<PhysStateTrash, Long, Integer> {

}
