package com.example.eco_service.repositories;

import com.example.eco_service.entities.Technology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface InterfTechnology extends JpaRepository<Technology, Long>, JpaSpecificationExecutor<Technology>, RevisionRepository<Technology, Long, Integer> {

    @Query("SELECT t FROM Technology t WHERE t.id_magasin_factory.id_magasin_factory = :factoryId")
    List<Technology> findAllByFactoryId(@Param("factoryId") Long factoryId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("DELETE FROM Technology t WHERE t.id_magasin_factory.id_magasin_factory = :factoryId")
    void deleteAllByFactoryId(@Param("factoryId") Long factoryId);
}
