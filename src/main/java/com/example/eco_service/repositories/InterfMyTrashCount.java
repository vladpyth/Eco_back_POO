package com.example.eco_service.repositories;

import com.example.eco_service.entities.MyTrash;
import com.example.eco_service.entities.MyTrashCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface InterfMyTrashCount extends JpaRepository<MyTrashCount, Long>, JpaSpecificationExecutor<MyTrashCount>, RevisionRepository<MyTrashCount , Long, Integer> {
    // Найти все связи по ID типа отхода
    @Query("SELECT m FROM MyTrashCount m WHERE m.id_my_trash.id_my_trash = :myTrashId")
    List<MyTrashCount> findAllByMyTrashId(@Param("myTrashId") Long myTrashId);

    // Найти все связи по ID предприятия
    @Query("SELECT m FROM MyTrashCount m WHERE m.id_object_place_trash.id_magasin_factory = :factoryId")
    List<MyTrashCount> findAllByFactoryId(@Param("factoryId") Long factoryId);

    // Найти количество отхода для конкретного предприятия и типа отхода
    @Query("SELECT m FROM MyTrashCount m WHERE m.id_my_trash.id_my_trash = :myTrashId AND m.id_object_place_trash.id_magasin_factory = :factoryId")
    Optional<MyTrashCount> findByMyTrashAndFactory(@Param("myTrashId") Long myTrashId, @Param("factoryId") Long factoryId);
}