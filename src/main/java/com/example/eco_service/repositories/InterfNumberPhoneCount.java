package com.example.eco_service.repositories;

import com.example.eco_service.entities.NumberPhoneCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterfNumberPhoneCount extends JpaRepository<NumberPhoneCount, Long>, JpaSpecificationExecutor<NumberPhoneCount>, RevisionRepository<NumberPhoneCount, Long, Integer> {

    @Query("SELECT c FROM NumberPhoneCount c WHERE c.id_object_place_trash.id_magasin_factory = :objectPlaceId")
    List<NumberPhoneCount> findAllByObjectPlaceId(@Param("objectPlaceId") Long objectPlaceId);

    @Query("""
            SELECT c FROM NumberPhoneCount c
            WHERE c.id_object_place_trash.id_magasin_factory = :objectPlaceId
              AND c.id_phone_number.id_phone_number = :phoneId
            """)
    Optional<NumberPhoneCount> findLink(
            @Param("objectPlaceId") Long objectPlaceId,
            @Param("phoneId") Long phoneId);

    @Query("""
            SELECT COUNT(c) > 0 FROM NumberPhoneCount c
            WHERE c.id_object_place_trash.id_magasin_factory = :objectPlaceId
              AND c.id_phone_number.id_phone_number = :phoneId
            """)
    boolean existsLink(@Param("objectPlaceId") Long objectPlaceId, @Param("phoneId") Long phoneId);

    @Modifying
    @Transactional
    @Query("""
            DELETE FROM NumberPhoneCount c
            WHERE c.id_object_place_trash.id_magasin_factory = :objectPlaceId
              AND c.id_phone_number.id_phone_number = :phoneId
            """)
    void deleteLink(@Param("objectPlaceId") Long objectPlaceId, @Param("phoneId") Long phoneId);

    @Modifying
    @Transactional
    @Query("DELETE FROM NumberPhoneCount c WHERE c.id_phone_number.id_phone_number = :phoneId")
    void deleteAllByPhoneId(@Param("phoneId") Long phoneId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("DELETE FROM NumberPhoneCount c WHERE c.id_object_place_trash.id_magasin_factory = :objectPlaceId")
    void deleteAllByObjectPlaceId(@Param("objectPlaceId") Long objectPlaceId);
}

