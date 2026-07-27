package com.example.eco_service.repositories;

import com.example.eco_service.entities.MagasinFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InterfMagasinFactory extends JpaRepository<MagasinFactory, Long>, JpaSpecificationExecutor<MagasinFactory>, RevisionRepository<MagasinFactory, Long, Integer> {

    @Query("select (count(m) > 0) from MagasinFactory m where m.id_registration = :reg")
    boolean existsByRegistrationNumber(@Param("reg") String reg);

    @Query("select (count(m) > 0) from MagasinFactory m where m.id_registration = :reg and m.id_magasin_factory <> :id")
    boolean existsByRegistrationNumberAndIdNot(@Param("reg") String reg, @Param("id") Long id);
}
