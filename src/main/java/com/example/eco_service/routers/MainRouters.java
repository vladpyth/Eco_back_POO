package com.example.eco_service.routers;

import com.example.eco_service.dto.request.*;
import com.example.eco_service.entities.*;
import com.example.eco_service.services.CRUDServices;
import com.example.eco_service.services.PageSupport;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Главный контроллер", description = "API для web")
public class MainRouters {

    private final CRUDServices service;

    // ==================== REGION ENDPOINTS ====================

    @PostMapping("/region")
    @Operation(summary = "Создать регион")
    public ResponseEntity<Region> createRegion(@Valid @RequestBody RegionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createRegion(request));
    }

    @GetMapping("/region")
    @Operation(summary = "Список регионов (page+size — страница; без них — весь список)")
    public ResponseEntity<?> findAllRegions(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String dir) {
        if (PageSupport.wantsPage(page, size)) {
            return ResponseEntity.ok(service.findAllRegionsPaged(page, size, q, sort, dir));
        }
        return ResponseEntity.ok(service.findAllRegions());
    }

    @GetMapping("/region/{id}")
    @Operation(summary = "Получить регион по ID")
    public ResponseEntity<Region> findRegionById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdRegion(id));
    }

    @PutMapping("/region/{id}")
    @Operation(summary = "Обновить регион")
    public ResponseEntity<Region> updateRegion(@PathVariable Long id, @Valid @RequestBody RegionRequest request) {
        return ResponseEntity.ok(service.updateRegion(id, request));
    }

    @DeleteMapping("/region/{id}")
    @Operation(summary = "Удалить регион")
    public ResponseEntity<Void> deleteRegion(@PathVariable Long id) {
        service.deleteRegion(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== DISTRICT ENDPOINTS ====================

    @PostMapping("/district")
    @Operation(summary = "Создать район")
    public ResponseEntity<District> createDistrict(@Valid @RequestBody DistrictRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createDistrict(request));
    }

    @GetMapping("/district")
    @Operation(summary = "Список районов (page+size — страница; без них — весь список)")
    public ResponseEntity<?> findAllDistricts(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String dir) {
        if (PageSupport.wantsPage(page, size)) {
            return ResponseEntity.ok(service.findAllDistrictsPaged(page, size, q, sort, dir));
        }
        return ResponseEntity.ok(service.findAllDistricts());
    }

    @GetMapping("/district/{id}")
    @Operation(summary = "Получить район по ID")
    public ResponseEntity<District> findDistrictById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdDistrict(id));
    }

    @PutMapping("/district/{id}")
    @Operation(summary = "Обновить район")
    public ResponseEntity<District> updateDistrict(@PathVariable Long id, @Valid @RequestBody DistrictRequest request) {
        return ResponseEntity.ok(service.updateDistrict(id, request));
    }

    @DeleteMapping("/district/{id}")
    @Operation(summary = "Удалить район")
    public ResponseEntity<Void> deleteDistrict(@PathVariable Long id) {
        service.deleteDistrict(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== CITIES ENDPOINTS ====================

    @PostMapping("/cities")
    @Operation(summary = "Создать город")
    public ResponseEntity<Cities> createCities(@Valid @RequestBody CitiesRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createCities(request));
    }

    @GetMapping("/cities")
    @Operation(summary = "Список городов (page+size — страница; без них — весь список)")
    public ResponseEntity<?> findAllCities(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String dir) {
        if (PageSupport.wantsPage(page, size)) {
            return ResponseEntity.ok(service.findAllCitiesPaged(page, size, q, sort, dir));
        }
        return ResponseEntity.ok(service.findAllCities());
    }

    @GetMapping("/cities/{id}")
    @Operation(summary = "Получить город по ID")
    public ResponseEntity<Cities> findCitiesById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdCities(id));
    }

    @PutMapping("/cities/{id}")
    @Operation(summary = "Обновить город")
    public ResponseEntity<Cities> updateCities(@PathVariable Long id, @Valid @RequestBody CitiesRequest request) {
        return ResponseEntity.ok(service.updateCities(id, request));
    }

    @DeleteMapping("/cities/{id}")
    @Operation(summary = "Удалить город")
    public ResponseEntity<Void> deleteCities(@PathVariable Long id) {
        service.deleteCities(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== CLASS DANGER ENDPOINTS ====================

    @PostMapping("/class-danger")
    @Operation(summary = "Создать класс опасности")
    public ResponseEntity<ClassDanger> createClassDanger(@Valid @RequestBody ClassDangerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createClassDanger(request));
    }

    @GetMapping("/class-danger")
    @Operation(summary = "Список классов опасности (page+size — страница; без них — весь список)")
    public ResponseEntity<?> findAllClassDangers(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String dir) {
        if (PageSupport.wantsPage(page, size)) {
            return ResponseEntity.ok(service.findAllClassDangersPaged(page, size, q, sort, dir));
        }
        return ResponseEntity.ok(service.findAllClassDangers());
    }

    @GetMapping("/class-danger/{id}")
    @Operation(summary = "Получить класс опасности по ID")
    public ResponseEntity<ClassDanger> findClassDangerById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdClassDanger(id));
    }

    @PutMapping("/class-danger/{id}")
    @Operation(summary = "Обновить класс опасности")
    public ResponseEntity<ClassDanger> updateClassDanger(@PathVariable Long id, @Valid @RequestBody ClassDangerRequest request) {
        return ResponseEntity.ok(service.updateClassDanger(id, request));
    }

    @DeleteMapping("/class-danger/{id}")
    @Operation(summary = "Удалить класс опасности")
    public ResponseEntity<Void> deleteClassDanger(@PathVariable Long id) {
        service.deleteClassDanger(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== MAGAZIN TRASH ENDPOINTS ====================

    @PostMapping("/magazin-trash")
    @Operation(summary = "Создать справочник отходов")
    public ResponseEntity<MagazinTrash> createMagazinTrash(@Valid @RequestBody MagazinTrashRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createMagazinTrash(request));
    }

    @GetMapping("/magazin-trash")
    @Operation(summary = "Список отходов (page+size — страница; без них — весь список)")
    public ResponseEntity<?> findAllMagazinTrashes(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String dir) {
        if (PageSupport.wantsPage(page, size)) {
            return ResponseEntity.ok(service.findAllMagazinTrashesPaged(page, size, q, sort, dir));
        }
        return ResponseEntity.ok(service.findAllMagazinTrashes());
    }

    @GetMapping("/magazin-trash/{id}")
    @Operation(summary = "Получить отход по ID")
    public ResponseEntity<MagazinTrash> findMagazinTrashById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdMagazinTrash(id));
    }

    @PutMapping("/magazin-trash/{id}")
    @Operation(summary = "Обновить отход")
    public ResponseEntity<MagazinTrash> updateMagazinTrash(@PathVariable Long id, @Valid @RequestBody MagazinTrashRequest request) {
        return ResponseEntity.ok(service.updateMagazinTrash(id, request));
    }

    @DeleteMapping("/magazin-trash/{id}")
    @Operation(summary = "Удалить отход")
    public ResponseEntity<Void> deleteMagazinTrash(@PathVariable Long id) {
        service.deleteMagazinTrash(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== PHYS STATE TRASH ENDPOINTS ====================

    @PostMapping("/phys-state-trash")
    @Operation(summary = "Создать физическое состояние отхода")
    public ResponseEntity<PhysStateTrash> createPhysStateTrash(@Valid @RequestBody PhysStateTrashRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createPhysStateTrash(request));
    }

    @GetMapping("/phys-state-trash")
    @Operation(summary = "Список физ. состояний (page+size — страница; без них — весь список)")
    public ResponseEntity<?> findAllPhysStateTrashes(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String dir) {
        if (PageSupport.wantsPage(page, size)) {
            return ResponseEntity.ok(service.findAllPhysStateTrashesPaged(page, size, q, sort, dir));
        }
        return ResponseEntity.ok(service.findAllPhysStateTrashes());
    }

    @GetMapping("/phys-state-trash/{id}")
    @Operation(summary = "Получить физическое состояние по ID")
    public ResponseEntity<PhysStateTrash> findPhysStateTrashById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdPhysStateTrash(id));
    }

    @PutMapping("/phys-state-trash/{id}")
    @Operation(summary = "Обновить физическое состояние")
    public ResponseEntity<PhysStateTrash> updatePhysStateTrash(@PathVariable Long id, @Valid @RequestBody PhysStateTrashRequest request) {
        return ResponseEntity.ok(service.updatePhysStateTrash(id, request));
    }

    @DeleteMapping("/phys-state-trash/{id}")
    @Operation(summary = "Удалить физическое состояние")
    public ResponseEntity<Void> deletePhysStateTrash(@PathVariable Long id) {
        service.deletePhysStateTrash(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== TECHNOLOGY ENDPOINTS ====================

    @PostMapping("/technology")
    @Operation(summary = "Создать технологию")
    public ResponseEntity<Technology> createTechnology(@Valid @RequestBody TechnologyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createTechnology(request));
    }

    @GetMapping("/technology")
    @Operation(summary = "Список технологий (page+size — страница; factoryId — по предприятию; без них — весь список)")
    public ResponseEntity<?> findAllTechnologies(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String dir,
            @RequestParam(required = false) Long factoryId) {
        if (factoryId != null) {
            return ResponseEntity.ok(service.findTechnologiesByFactory(factoryId));
        }
        if (PageSupport.wantsPage(page, size)) {
            return ResponseEntity.ok(service.findAllTechnologiesPaged(page, size, q, sort, dir));
        }
        return ResponseEntity.ok(service.findAllTechnologies());
    }

    @GetMapping("/technology/{id}")
    @Operation(summary = "Получить технологию по ID")
    public ResponseEntity<Technology> findTechnologyById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdTechnology(id));
    }

    @PutMapping("/technology/{id}")
    @Operation(summary = "Обновить технологию")
    public ResponseEntity<Technology> updateTechnology(@PathVariable Long id, @Valid @RequestBody TechnologyRequest request) {
        return ResponseEntity.ok(service.updateTechnology(id, request));
    }

    @DeleteMapping("/technology/{id}")
    @Operation(summary = "Удалить технологию")
    public ResponseEntity<Void> deleteTechnology(@PathVariable Long id) {
        service.deleteTechnology(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== MAGASIN FACTORY ENDPOINTS ====================

    @PostMapping("/magasin-factory")
    @Operation(summary = "Создать предприятие")
    public ResponseEntity<MagasinFactory> createMagasinFactory(@Valid @RequestBody MagasinFactoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createMagasinFactory(request));
    }

    @GetMapping("/magasin-factory")
    @Operation(summary = "Список предприятий (page+size — страница; без них — весь список)")
    public ResponseEntity<?> findAllMagasinFactories(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String dir) {
        if (PageSupport.wantsPage(page, size)) {
            return ResponseEntity.ok(service.findAllMagasinFactoriesPaged(page, size, q, sort, dir));
        }
        return ResponseEntity.ok(service.findAllMagasinFactories());
    }

    @GetMapping("/magasin-factory/{id}")
    @Operation(summary = "Получить предприятие по ID")
    public ResponseEntity<MagasinFactory> findMagasinFactoryById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdMagasinFactory(id));
    }

    @PutMapping("/magasin-factory/{id}")
    @Operation(summary = "Обновить предприятие")
    public ResponseEntity<MagasinFactory> updateMagasinFactory(@PathVariable Long id, @Valid @RequestBody MagasinFactoryRequest request) {
        return ResponseEntity.ok(service.updateMagasinFactory(id, request));
    }

    @DeleteMapping("/magasin-factory/{id}")
    @Operation(summary = "Удалить предприятие")
    public ResponseEntity<Void> deleteMagasinFactory(@PathVariable Long id) {
        service.deleteMagasinFactory(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== MY TRASH ENDPOINTS ====================

    @PostMapping("/my-trash")
    @Operation(summary = "Создать отход предприятия")
    public ResponseEntity<MyTrash> createMyTrash(@Valid @RequestBody MyTrashRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createMyTrash(request));
    }

    @GetMapping("/my-trash")
    @Operation(summary = "Список отходов предприятий (page+size — страница; factoryId — по предприятию; без них — весь список)")
    public ResponseEntity<?> findAllMyTrashes(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String dir,
            @RequestParam(required = false) Long factoryId) {
        if (factoryId != null) {
            return ResponseEntity.ok(service.findMyTrashesByFactory(factoryId));
        }
        if (PageSupport.wantsPage(page, size)) {
            return ResponseEntity.ok(service.findAllMyTrashesPaged(page, size, q, sort, dir));
        }
        return ResponseEntity.ok(service.findAllMyTrashes());
    }

    @GetMapping("/my-trash/{id}")
    @Operation(summary = "Получить отход предприятия по ID")
    public ResponseEntity<MyTrash> findMyTrashById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdMyTrash(id));
    }

    @PutMapping("/my-trash/{id}")
    @Operation(summary = "Обновить отход предприятия")
    public ResponseEntity<MyTrash> updateMyTrash(@PathVariable Long id, @Valid @RequestBody MyTrashRequest request) {
        return ResponseEntity.ok(service.updateMyTrash(id, request));
    }

    @DeleteMapping("/my-trash/{id}")
    @Operation(summary = "Удалить отход предприятия")
    public ResponseEntity<Void> deleteMyTrash(@PathVariable Long id) {
        service.deleteMyTrash(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== DROP AIR ENDPOINTS ====================

    @PostMapping("/drop-air")
    @Operation(summary = "Создать выброс в атмосферу")
    public ResponseEntity<DropAir> createDropAir(@Valid @RequestBody DropAirRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createDropAir(request));
    }

    @GetMapping("/drop-air")
    @Operation(summary = "Список выбросов (page+size — страница; factoryId — по предприятию; без них — весь список)")
    public ResponseEntity<?> findAllDropAirs(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String dir,
            @RequestParam(required = false) Long factoryId) {
        if (factoryId != null) {
            return ResponseEntity.ok(service.findDropAirsByFactory(factoryId));
        }
        if (PageSupport.wantsPage(page, size)) {
            return ResponseEntity.ok(service.findAllDropAirsPaged(page, size, q, sort, dir));
        }
        return ResponseEntity.ok(service.findAllDropAirs());
    }

    @GetMapping("/drop-air/{id}")
    @Operation(summary = "Получить выброс по ID")
    public ResponseEntity<DropAir> findDropAirById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdDropAir(id));
    }

    @PutMapping("/drop-air/{id}")
    @Operation(summary = "Обновить выброс")
    public ResponseEntity<DropAir> updateDropAir(@PathVariable Long id, @Valid @RequestBody DropAirRequest request) {
        return ResponseEntity.ok(service.updateDropAir(id, request));
    }

    @DeleteMapping("/drop-air/{id}")
    @Operation(summary = "Удалить выброс")
    public ResponseEntity<Void> deleteDropAir(@PathVariable Long id) {
        service.deleteDropAir(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== NUMBER PHONE ENDPOINTS ====================

    @PostMapping("/number-phone")
    @Operation(summary = "Создать номер телефона")
    public ResponseEntity<NumberPhone> createNumberPhone(@Valid @RequestBody NumberPhoneRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createNumberPhone(request));
    }

    @GetMapping("/number-phone")
    @Operation(summary = "Список телефонов (page+size — страница; без них — весь список)")
    public ResponseEntity<?> findAllNumberPhones(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String dir) {
        if (PageSupport.wantsPage(page, size)) {
            return ResponseEntity.ok(service.findAllNumberPhonesPaged(page, size, q, sort, dir));
        }
        return ResponseEntity.ok(service.findAllNumberPhones());
    }

    @GetMapping("/number-phone/{id}")
    @Operation(summary = "Получить номер телефона по ID")
    public ResponseEntity<NumberPhone> findNumberPhoneById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdNumberPhone(id));
    }

    @PutMapping("/number-phone/{id}")
    @Operation(summary = "Обновить номер телефона")
    public ResponseEntity<NumberPhone> updateNumberPhone(@PathVariable Long id, @Valid @RequestBody NumberPhoneRequest request) {
        return ResponseEntity.ok(service.updateNumberPhone(id, request));
    }

    @DeleteMapping("/number-phone/{id}")
    @Operation(summary = "Удалить номер телефона")
    public ResponseEntity<Void> deleteNumberPhone(@PathVariable Long id) {
        service.deleteNumberPhone(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== NUMBER PHONE COUNT ENDPOINTS ====================

    @PostMapping("/number-phone-count")
    @Operation(summary = "Связать номер телефона с предприятием")
    public ResponseEntity<NumberPhoneCount> createNumberPhoneCount(
            @RequestParam Long objectPlaceId,
            @RequestParam Long phoneId,
            @RequestParam int urOb) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createNumberPhoneCount(objectPlaceId, phoneId, urOb));
    }

    @GetMapping("/number-phone-count")
    @Operation(summary = "Получить все связи номеров с предприятиями")
    public ResponseEntity<List<NumberPhoneCount>> findAllNumberPhoneCounts() {
        return ResponseEntity.ok(service.findAllNumberPhoneCounts());
    }

    @GetMapping("/number-phone-count/object/{objectPlaceId}")
    @Operation(summary = "Получить все номера предприятия")
    public ResponseEntity<List<NumberPhoneCount>> findNumberPhoneCountsByObject(@PathVariable Long objectPlaceId) {
        return ResponseEntity.ok(service.findNumberPhoneCountsByObjectPlace(objectPlaceId));
    }

    @DeleteMapping("/number-phone-count/unlink")
    @Operation(summary = "Удалить связь номера с предприятием")
    public ResponseEntity<Void> unlinkNumberPhoneFromObject(
            @RequestParam Long objectPlaceId,
            @RequestParam Long phoneId) {
        service.unlinkNumberPhoneFromObject(objectPlaceId, phoneId);
        return ResponseEntity.noContent().build();
    }

    // ==================== SHORT DISCRIBE TECHNOLOGY ENDPOINTS ====================

    @PostMapping("/short-discribe-technology")
    @Operation(summary = "Создать описание технологии")
    public ResponseEntity<ShortDiscribeTechnology> createShortDiscribeTechnology(@Valid @RequestBody ShortDiscribeTechnologyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createShortDiscribeTechnology(request));
    }

    @GetMapping("/short-discribe-technology")
    @Operation(summary = "Список описаний технологий (page+size — страница; без них — весь список)")
    public ResponseEntity<?> findAllShortDiscribeTechnologies(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String dir) {
        if (PageSupport.wantsPage(page, size)) {
            return ResponseEntity.ok(service.findAllShortDiscribeTechnologiesPaged(page, size, q, sort, dir));
        }
        return ResponseEntity.ok(service.findAllShortDiscribeTechnologies());
    }

    @GetMapping("/short-discribe-technology/{id}")
    @Operation(summary = "Получить описание технологии по ID")
    public ResponseEntity<ShortDiscribeTechnology> findShortDiscribeTechnologyById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdShortDiscribeTechnology(id));
    }

    @PutMapping("/short-discribe-technology/{id}")
    @Operation(summary = "Обновить описание технологии")
    public ResponseEntity<ShortDiscribeTechnology> updateShortDiscribeTechnology(@PathVariable Long id, @Valid @RequestBody ShortDiscribeTechnologyRequest request) {
        return ResponseEntity.ok(service.updateShortDiscribeTechnology(id, request));
    }

    @DeleteMapping("/short-discribe-technology/{id}")
    @Operation(summary = "Удалить описание технологии")
    public ResponseEntity<Void> deleteShortDiscribeTechnology(@PathVariable Long id) {
        service.deleteShortDiscribeTechnology(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== NAME DROP AIR TRASH ENDPOINTS ====================

    @PostMapping("/name-drop-air-trash")
    @Operation(summary = "Создать наименование выброса")
    public ResponseEntity<NameDropAirTrash> createNameDropAirTrash(@Valid @RequestBody NameDropAirTrashRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createNameDropAirTrash(request));
    }

    @GetMapping("/name-drop-air-trash")
    @Operation(summary = "Список наименований выбросов (page+size — страница; без них — весь список)")
    public ResponseEntity<?> findAllNameDropAirTrashes(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String dir) {
        if (PageSupport.wantsPage(page, size)) {
            return ResponseEntity.ok(service.findAllNameDropAirTrashesPaged(page, size, q, sort, dir));
        }
        return ResponseEntity.ok(service.findAllNameDropAirTrashes());
    }

    @GetMapping("/name-drop-air-trash/{id}")
    @Operation(summary = "Получить наименование выброса по ID")
    public ResponseEntity<NameDropAirTrash> findNameDropAirTrashById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdNameDropAirTrash(id));
    }

    @PutMapping("/name-drop-air-trash/{id}")
    @Operation(summary = "Обновить наименование выброса")
    public ResponseEntity<NameDropAirTrash> updateNameDropAirTrash(@PathVariable Long id, @Valid @RequestBody NameDropAirTrashRequest request) {
        return ResponseEntity.ok(service.updateNameDropAirTrash(id, request));
    }

    @DeleteMapping("/name-drop-air-trash/{id}")
    @Operation(summary = "Удалить наименование выброса")
    public ResponseEntity<Void> deleteNameDropAirTrash(@PathVariable Long id) {
        service.deleteNameDropAirTrash(id);
        return ResponseEntity.noContent().build();
    }
}
