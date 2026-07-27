package com.example.eco_service.services;

import com.example.eco_service.dto.request.*;
import com.example.eco_service.dto.response.PageResponse;
import com.example.eco_service.entities.*;
import com.example.eco_service.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CRUDServices {

    // ==================== РЕПОЗИТОРИИ ====================
    private final InterfRegion regionRepository;
    private final InterfDistrict districtRepository;
    private final InterfCities citiesRepository;
    private final InterfClassDanger classDangerRepository;
    private final InterfMagazinTrash magazinTrashRepository;
    private final InterfNameDropAirTrash nameDropAirTrashRepository;
    private final InterfPhysStateTrash physStateTrashRepository;
    private final InterfShortDiscribeTechnology shortDiscribeTechnologyRepository;
    private final InterfTechnology technologyRepository;
    private final InterfMagasinFactory magasinFactoryRepository;
    private final InterfMyTrash myTrashRepository;
    private final InterfMyTrashCount myTrashCountRepository;
    private final InterfDropAir dropAirRepository;
    private final InterfNumberPhone numberPhoneRepository;
    private final InterfNumberPhoneCount numberPhoneCountRepository;




    // ==================== REGION CRUD ====================

    public Region createRegion(RegionRequest request) {
        log.info("Creating Region with name: {}", request.getNameRegion());

        Region entity = Region.builder()
                .name_region(request.getNameRegion())
                .build();

        return regionRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<Region> findAllRegions() {
        log.info("Fetching all Regions");
        return regionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Region findByIdRegion(Long id) {
        log.info("Fetching Region by id: {}", id);
        return regionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Region not found with id: " + id));
    }

    public Region updateRegion(Long id, RegionRequest request) {
        log.info("Updating Region with id: {}", id);

        Region entity = regionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Region not found with id: " + id));

        if (request.getNameRegion() != null) {
            entity.setName_region(request.getNameRegion());
        }

        return regionRepository.save(entity);
    }

    public void deleteRegion(Long id) {
        log.info("Deleting Region with id: {}", id);

        if (!regionRepository.existsById(id)) {
            throw new RuntimeException("Region not found with id: " + id);
        }

        regionRepository.deleteById(id);
    }

    // ==================== DISTRICT CRUD ====================

    public District createDistrict(DistrictRequest request) {
        log.info("Creating District with name: {}", request.getName_district());

        District entity = District.builder()
                .name_district(request.getName_district())
                .build();

        return districtRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<District> findAllDistricts() {
        log.info("Fetching all Districts");
        return districtRepository.findAll();
    }

    @Transactional(readOnly = true)
    public District findByIdDistrict(Long id) {
        log.info("Fetching District by id: {}", id);
        return districtRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("District not found with id: " + id));
    }

    public District updateDistrict(Long id, DistrictRequest request) {
        log.info("Updating District with id: {}", id);

        District entity = districtRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("District not found with id: " + id));

        if (request.getName_district() != null) {
            entity.setName_district(request.getName_district());
        }

        return districtRepository.save(entity);
    }

    public void deleteDistrict(Long id) {
        log.info("Deleting District with id: {}", id);

        if (!districtRepository.existsById(id)) {
            throw new RuntimeException("District not found with id: " + id);
        }

        districtRepository.deleteById(id);
    }

    // ==================== CITIES CRUD ====================

    public Cities createCities(CitiesRequest request) {
        log.info("Creating Cities with index: {}", request.getIndex());

        Region region = regionRepository.findById(request.getIdRegion())
                .orElseThrow(() -> new RuntimeException("Region not found with id: " + request.getIdRegion()));

        District district = districtRepository.findById(request.getIdDistrict())
                .orElseThrow(() -> new RuntimeException("District not found with id: " + request.getIdDistrict()));

        Cities entity = Cities.builder()
                .id_region(region)
                .index(request.getIndex())
                .id_district(district)
                .name_cities(request.getName_cities())
                .build();

        return citiesRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<Cities> findAllCities() {
        log.info("Fetching all Cities");
        return citiesRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Cities findByIdCities(Long id) {
        log.info("Fetching Cities by id: {}", id);
        return citiesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cities not found with id: " + id));
    }

    public Cities updateCities(Long id, CitiesRequest request) {
        log.info("Updating Cities with id: {}", id);

        Cities entity = citiesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cities not found with id: " + id));

        if (request.getName_cities() != null) {
            entity.setName_cities(request.getName_cities());
        }

        if (request.getIndex() != null) {
            entity.setIndex(request.getIndex());
        }

        if (request.getIdRegion() != null) {
            Region region = regionRepository.findById(request.getIdRegion())
                    .orElseThrow(() -> new RuntimeException("Region not found with id: " + request.getIdRegion()));
            entity.setId_region(region);
        }

        if (request.getIdDistrict() != null) {
            District district = districtRepository.findById(request.getIdDistrict())
                    .orElseThrow(() -> new RuntimeException("District not found with id: " + request.getIdDistrict()));
            entity.setId_district(district);
        }

        return citiesRepository.save(entity);
    }

    public void deleteCities(Long id) {
        log.info("Deleting Cities with id: {}", id);

        if (!citiesRepository.existsById(id)) {
            throw new RuntimeException("Cities not found with id: " + id);
        }

        citiesRepository.deleteById(id);
    }

    // ==================== CLASS DANGER CRUD ====================

    public ClassDanger createClassDanger(ClassDangerRequest request) {
        log.info("Creating ClassDanger with class: {}", request.getClassDanger());
        Integer v = request.getClassDanger();
        if (v == null) {
            throw new RuntimeException("classDanger обязателен");
        }
        return classDangerRepository.findByClassDangerValue(v)
                .orElseGet(() -> classDangerRepository.save(ClassDanger.builder()
                        .class_danger(v)
                        .build()));
    }

    @Transactional(readOnly = true)
    public List<ClassDanger> findAllClassDangers() {
        log.info("Fetching all ClassDangers");
        return classDangerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ClassDanger findByIdClassDanger(Long id) {
        log.info("Fetching ClassDanger by id: {}", id);
        return classDangerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ClassDanger not found with id: " + id));
    }

    public ClassDanger updateClassDanger(Long id, ClassDangerRequest request) {
        log.info("Updating ClassDanger with id: {}", id);

        ClassDanger entity = classDangerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ClassDanger not found with id: " + id));

        if (request.getClassDanger() != null) {
            entity.setClass_danger(request.getClassDanger());
        }

        return classDangerRepository.save(entity);
    }

    public void deleteClassDanger(Long id) {
        log.info("Deleting ClassDanger with id: {}", id);

        if (!classDangerRepository.existsById(id)) {
            throw new RuntimeException("ClassDanger not found with id: " + id);
        }

        classDangerRepository.deleteById(id);
    }

    // ==================== MAGAZIN TRASH CRUD ====================

    public MagazinTrash createMagazinTrash(MagazinTrashRequest request) {
        log.info("Creating MagazinTrash with code: {}", request.getCode_trash());

        ClassDanger classDanger = null;
        if (request.getId_class_danger() != null && request.getId_class_danger() > 0) {
            classDanger = classDangerRepository.findById(request.getId_class_danger())
                    .orElseThrow(() -> new RuntimeException("ClassDanger not found with id: " + request.getId_class_danger()));
        }

        MagazinTrash entity = MagazinTrash.builder()
                .id_class_danger(classDanger)
                .code_trash(request.getCode_trash())
                .name_trash(request.getName_trash())
                .build();

        return magazinTrashRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<MagazinTrash> findAllMagazinTrashes() {
        log.info("Fetching all MagazinTrashes");
        return magazinTrashRepository.findAll();
    }

    @Transactional(readOnly = true)
    public MagazinTrash findByIdMagazinTrash(Long id) {
        log.info("Fetching MagazinTrash by id: {}", id);
        return magazinTrashRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MagazinTrash not found with id: " + id));
    }

    public MagazinTrash updateMagazinTrash(Long id, MagazinTrashRequest request) {
        log.info("Updating MagazinTrash with id: {}", id);

        MagazinTrash entity = magazinTrashRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MagazinTrash not found with id: " + id));

        if (request.getId_class_danger() != null) {
            entity.setId_class_danger(resolveOptionalClassDanger(request.getId_class_danger()));
        }

        if (request.getCode_trash() != null) {
            entity.setCode_trash(request.getCode_trash());
        }

        if (request.getName_trash() != null) {
            entity.setName_trash(request.getName_trash());
        }

        return magazinTrashRepository.save(entity);
    }

    public void deleteMagazinTrash(Long id) {
        log.info("Deleting MagazinTrash with id: {}", id);

        if (!magazinTrashRepository.existsById(id)) {
            throw new RuntimeException("MagazinTrash not found with id: " + id);
        }

        magazinTrashRepository.deleteById(id);
    }

    // ==================== NAME DROP AIR TRASH CRUD ====================

    public NameDropAirTrash createNameDropAirTrash(NameDropAirTrashRequest request) {
        log.info("Creating NameDropAirTrash with name: {}", request.getName_drop_air_trash());

        NameDropAirTrash entity = NameDropAirTrash.builder()
                .name_drop_air_trash(request.getName_drop_air_trash())
                .build();

        return nameDropAirTrashRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<NameDropAirTrash> findAllNameDropAirTrashes() {
        log.info("Fetching all NameDropAirTrashes");
        return nameDropAirTrashRepository.findAll();
    }

    @Transactional(readOnly = true)
    public NameDropAirTrash findByIdNameDropAirTrash(Long id) {
        log.info("Fetching NameDropAirTrash by id: {}", id);
        return nameDropAirTrashRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("NameDropAirTrash not found with id: " + id));
    }

    public NameDropAirTrash updateNameDropAirTrash(Long id, NameDropAirTrashRequest request) {
        log.info("Updating NameDropAirTrash with id: {}", id);

        NameDropAirTrash entity = nameDropAirTrashRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("NameDropAirTrash not found with id: " + id));

        if (request.getName_drop_air_trash() != null) {
            entity.setName_drop_air_trash(request.getName_drop_air_trash());
        }

        return nameDropAirTrashRepository.save(entity);
    }

    public void deleteNameDropAirTrash(Long id) {
        log.info("Deleting NameDropAirTrash with id: {}", id);

        if (!nameDropAirTrashRepository.existsById(id)) {
            throw new RuntimeException("NameDropAirTrash not found with id: " + id);
        }

        nameDropAirTrashRepository.deleteById(id);
    }

    // ==================== PHYS STATE TRASH CRUD ====================

    public PhysStateTrash createPhysStateTrash(PhysStateTrashRequest request) {
        log.info("Creating PhysStateTrash with name: {}", request.getName_group());

        PhysStateTrash entity = PhysStateTrash.builder()
                .name_group(request.getName_group())
                .build();

        return physStateTrashRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<PhysStateTrash> findAllPhysStateTrashes() {
        log.info("Fetching all PhysStateTrashes");
        return physStateTrashRepository.findAll();
    }

    @Transactional(readOnly = true)
    public PhysStateTrash findByIdPhysStateTrash(Long id) {
        log.info("Fetching PhysStateTrash by id: {}", id);
        return physStateTrashRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PhysStateTrash not found with id: " + id));
    }

    public PhysStateTrash updatePhysStateTrash(Long id, PhysStateTrashRequest request) {
        log.info("Updating PhysStateTrash with id: {}", id);

        PhysStateTrash entity = physStateTrashRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PhysStateTrash not found with id: " + id));

        if (request.getName_group() != null) {
            entity.setName_group(request.getName_group());
        }

        return physStateTrashRepository.save(entity);
    }

    public void deletePhysStateTrash(Long id) {
        log.info("Deleting PhysStateTrash with id: {}", id);

        if (!physStateTrashRepository.existsById(id)) {
            throw new RuntimeException("PhysStateTrash not found with id: " + id);
        }

        physStateTrashRepository.deleteById(id);
    }

    // ==================== SHORT DISCRIBE TECHNOLOGY CRUD ====================

    public ShortDiscribeTechnology createShortDiscribeTechnology(ShortDiscribeTechnologyRequest request) {
        log.info("Creating ShortDiscribeTechnology with technology: {}", request.getTechnology());

        ShortDiscribeTechnology entity = ShortDiscribeTechnology.builder()
                .technology(request.getTechnology())
                .build();

        return shortDiscribeTechnologyRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<ShortDiscribeTechnology> findAllShortDiscribeTechnologies() {
        log.info("Fetching all ShortDiscribeTechnologies");
        return shortDiscribeTechnologyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ShortDiscribeTechnology findByIdShortDiscribeTechnology(Long id) {
        log.info("Fetching ShortDiscribeTechnology by id: {}", id);
        return shortDiscribeTechnologyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ShortDiscribeTechnology not found with id: " + id));
    }

    public ShortDiscribeTechnology updateShortDiscribeTechnology(Long id, ShortDiscribeTechnologyRequest request) {
        log.info("Updating ShortDiscribeTechnology with id: {}", id);

        ShortDiscribeTechnology entity = shortDiscribeTechnologyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ShortDiscribeTechnology not found with id: " + id));

        if (request.getTechnology() != null) {
            entity.setTechnology(request.getTechnology());
        }

        return shortDiscribeTechnologyRepository.save(entity);
    }

    public void deleteShortDiscribeTechnology(Long id) {
        log.info("Deleting ShortDiscribeTechnology with id: {}", id);

        if (!shortDiscribeTechnologyRepository.existsById(id)) {
            throw new RuntimeException("ShortDiscribeTechnology not found with id: " + id);
        }

        shortDiscribeTechnologyRepository.deleteById(id);
    }

    // ==================== TECHNOLOGY CRUD ====================

    private MagasinFactory resolveTechnologyFactory(Long factoryId) {
        if (factoryId == null || factoryId <= 0) {
            return null;
        }
        return magasinFactoryRepository.findById(factoryId)
                .orElseThrow(() -> new RuntimeException("MagasinFactory not found with id: " + factoryId));
    }

    /** null или ≤0 (в т.ч. −1 с фронта) — класс опасности не задан */
    private ClassDanger resolveOptionalClassDanger(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        return classDangerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ClassDanger not found with id: " + id));
    }

    public Technology createTechnology(TechnologyRequest request) {
        log.info("Creating Technology");

        ClassDanger classDanger = resolveOptionalClassDanger(request.getId_class_danger());

        MagazinTrash magazinTrash = magazinTrashRepository.findById(request.getId_magazin_trash())
                .orElseThrow(() -> new RuntimeException("MagazinTrash not found with id: " + request.getId_magazin_trash()));

        PhysStateTrash physStateTrash = physStateTrashRepository.findById(request.getId_phys_trash())
                .orElseThrow(() -> new RuntimeException("PhysStateTrash not found with id: " + request.getId_phys_trash()));

        Technology entity = Technology.builder()
                .id_class_danger(classDanger)
                .id_magazin_trash(magazinTrash)
                .id_phys_trash(physStateTrash)
                .id_magasin_factory(resolveTechnologyFactory(request.getId_magasin_factory()))
                .build();

        return technologyRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<Technology> findAllTechnologies() {
        log.info("Fetching all Technologies");
        return technologyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Technology findByIdTechnology(Long id) {
        log.info("Fetching Technology by id: {}", id);
        return technologyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Technology not found with id: " + id));
    }

    public Technology updateTechnology(Long id, TechnologyRequest request) {
        log.info("Updating Technology with id: {}", id);

        Technology entity = technologyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Technology not found with id: " + id));

        if (request.getId_class_danger() != null) {
            entity.setId_class_danger(resolveOptionalClassDanger(request.getId_class_danger()));
        }

        if (request.getId_magazin_trash() != null) {
            MagazinTrash magazinTrash = magazinTrashRepository.findById(request.getId_magazin_trash())
                    .orElseThrow(() -> new RuntimeException("MagazinTrash not found with id: " + request.getId_magazin_trash()));
            entity.setId_magazin_trash(magazinTrash);
        }

        if (request.getId_phys_trash() != null) {
            PhysStateTrash physStateTrash = physStateTrashRepository.findById(request.getId_phys_trash())
                    .orElseThrow(() -> new RuntimeException("PhysStateTrash not found with id: " + request.getId_phys_trash()));
            entity.setId_phys_trash(physStateTrash);
        }

        entity.setId_magasin_factory(resolveTechnologyFactory(request.getId_magasin_factory()));

        return technologyRepository.save(entity);
    }

    public void deleteTechnology(Long id) {
        log.info("Deleting Technology with id: {}", id);

        if (!technologyRepository.existsById(id)) {
            throw new RuntimeException("Technology not found with id: " + id);
        }

        technologyRepository.deleteById(id);
    }

    // ==================== MAGASIN FACTORY CRUD ====================

    public MagasinFactory createMagasinFactory(MagasinFactoryRequest request) {
        log.info("Creating MagasinFactory with registration: {}", request.getId_registration());

        if (request.getId_registration() != null
                && magasinFactoryRepository.existsByRegistrationNumber(request.getId_registration())) {
            throw new RuntimeException("Регистрационный номер уже существует у другой записи");
        }

        Cities city = null;
        if (request.getId_cities() != null) {
            city = citiesRepository.findById(request.getId_cities())
                    .orElseThrow(() -> new RuntimeException("Cities not found with id: " + request.getId_cities()));
        }

        ShortDiscribeTechnology shortDiscribeTechnology = null;
        if (request.getId_short_discribe_technology() != null) {
            shortDiscribeTechnology = shortDiscribeTechnologyRepository.findById(request.getId_short_discribe_technology())
                    .orElseThrow(() -> new RuntimeException("ShortDiscribeTechnology not found with id: " + request.getId_short_discribe_technology()));
        }

        Technology technology = null;
        if (request.getId_technology() != null) {
            technology = technologyRepository.findById(request.getId_technology())
                    .orElseThrow(() -> new RuntimeException("Technology not found with id: " + request.getId_technology()));
        }

        MagasinFactory entity = MagasinFactory.builder()
                .id_registration(request.getId_registration())
                .date_register(request.getDate_register())
                .id_cities(city)
                .id_short_discribe_technology(shortDiscribeTechnology)
                .id_technology(technology)
                .name_obj(request.getName_obj())
                .name_own(request.getName_own())
                .address_own(request.getAddress_own())
                .address_obj(request.getAddress_obj())
                .develop_organization(request.getDevelop_organization())
                .confirmed_project(request.getConfirmed_project())
                .date_approve(request.getDate_approve())
                .conclusion_documentation(request.getConclusion_documentation())
                .act_use(request.getAct_use())
                .requirements_acts(request.getRequirements_acts())
                .obj_use_trash(request.getObj_use_trash())
                .obj_accept_trash(request.getObj_accept_trash())
                .character_prod(request.getCharacter_prod())
                .project_power_yer(request.getProject_power_yer())
                .project_power_hr(request.getProject_power_hr())
                .facticheskay_power(request.getFacticheskay_power())
                .YNP(request.getYNP())
                .value(request.getValue() != null ? request.getValue() : 0)
                .build();

        return magasinFactoryRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<MagasinFactory> findAllMagasinFactories() {
        log.info("Fetching all MagasinFactories");
        return magasinFactoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public MagasinFactory findByIdMagasinFactory(Long id) {
        log.info("Fetching MagasinFactory by id: {}", id);
        return magasinFactoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MagasinFactory not found with id: " + id));
    }

    public MagasinFactory updateMagasinFactory(Long id, MagasinFactoryRequest request) {
        log.info("Updating MagasinFactory with id: {}", id);

        MagasinFactory entity = magasinFactoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MagasinFactory not found with id: " + id));

        if (request.getId_registration() != null) {
            String reg = request.getId_registration();
            if (!reg.equals(entity.getId_registration())
                    && magasinFactoryRepository.existsByRegistrationNumberAndIdNot(reg, id)) {
                throw new RuntimeException("Регистрационный номер уже существует у другой записи");
            }
            entity.setId_registration(reg);
        }

        if (request.getDate_register() != null) {
            entity.setDate_register(request.getDate_register());
        }

        if (request.getId_cities() != null) {
            Cities city = citiesRepository.findById(request.getId_cities())
                    .orElseThrow(() -> new RuntimeException("Cities not found with id: " + request.getId_cities()));
            entity.setId_cities(city);
        }

        if (request.getId_short_discribe_technology() != null) {
            ShortDiscribeTechnology shortDiscribeTechnology = shortDiscribeTechnologyRepository.findById(request.getId_short_discribe_technology())
                    .orElseThrow(() -> new RuntimeException("ShortDiscribeTechnology not found with id: " + request.getId_short_discribe_technology()));
            entity.setId_short_discribe_technology(shortDiscribeTechnology);
        }

        if (request.getId_technology() != null) {
            Technology technology = technologyRepository.findById(request.getId_technology())
                    .orElseThrow(() -> new RuntimeException("Technology not found with id: " + request.getId_technology()));
            entity.setId_technology(technology);
        }

        if (request.getName_obj() != null) {
            entity.setName_obj(request.getName_obj());
        }

        if (request.getName_own() != null) {
            entity.setName_own(request.getName_own());
        }

        if (request.getAddress_own() != null) {
            entity.setAddress_own(request.getAddress_own());
        }

        if (request.getAddress_obj() != null) {
            entity.setAddress_obj(request.getAddress_obj());
        }

        if (request.getDevelop_organization() != null) {
            entity.setDevelop_organization(request.getDevelop_organization());
        }

        if (request.getConfirmed_project() != null) {
            entity.setConfirmed_project(request.getConfirmed_project());
        }

        if (request.getDate_approve() != null) {
            entity.setDate_approve(request.getDate_approve());
        }

        if (request.getConclusion_documentation() != null) {
            entity.setConclusion_documentation(request.getConclusion_documentation());
        }

        if (request.getAct_use() != null) {
            entity.setAct_use(request.getAct_use());
        }

        if (request.getRequirements_acts() != null) {
            entity.setRequirements_acts(request.getRequirements_acts());
        }

        if (request.getObj_use_trash() != null) {
            entity.setObj_use_trash(request.getObj_use_trash());
        }

        if (request.getObj_accept_trash() != null) {
            entity.setObj_accept_trash(request.getObj_accept_trash());
        }

        if (request.getCharacter_prod() != null) {
            entity.setCharacter_prod(request.getCharacter_prod());
        }

        if (request.getProject_power_yer() != null) {
            entity.setProject_power_yer(request.getProject_power_yer());
        }

        if (request.getProject_power_hr() != null) {
            entity.setProject_power_hr(request.getProject_power_hr());
        }

        if (request.getFacticheskay_power() != null) {
            entity.setFacticheskay_power(request.getFacticheskay_power());
        }

        if (request.getYNP() != null) {
            entity.setYNP(request.getYNP());
        }

        if (request.getValue() != null) {
            entity.setValue(request.getValue());
        }

        return magasinFactoryRepository.save(entity);
    }

    public void deleteMagasinFactory(Long id) {
        log.info("Deleting MagasinFactory with id: {} (cascade related)", id);

        MagasinFactory factory = magasinFactoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MagasinFactory not found with id: " + id));

        // Снять FK предприятия → technology, иначе нельзя удалить связанные Technology
        if (factory.getId_technology() != null) {
            factory.setId_technology(null);
            magasinFactoryRepository.saveAndFlush(factory);
        }

        numberPhoneCountRepository.deleteAllByObjectPlaceId(id);

        List<MyTrashCount> trashLinks = myTrashCountRepository.findAllByFactoryId(id);
        if (!trashLinks.isEmpty()) {
            List<Long> myTrashIds = trashLinks.stream()
                    .filter(l -> l.getId_my_trash() != null && l.getId_my_trash().getId_my_trash() != null)
                    .map(l -> l.getId_my_trash().getId_my_trash())
                    .distinct()
                    .toList();
            myTrashCountRepository.deleteAll(trashLinks);
            for (Long myTrashId : myTrashIds) {
                List<MyTrashCount> leftover = myTrashCountRepository.findAllByMyTrashId(myTrashId);
                if (leftover.isEmpty() && myTrashRepository.existsById(myTrashId)) {
                    myTrashRepository.deleteById(myTrashId);
                }
            }
        }

        dropAirRepository.deleteAllByFactoryId(id);
        technologyRepository.deleteAllByFactoryId(id);

        magasinFactoryRepository.deleteById(id);
    }

    // ==================== MY TRASH CRUD ====================

    private void enrichMyTrashWithFactory(MyTrash entity) {
        if (entity == null || entity.getId_my_trash() == null) {
            return;
        }
        myTrashCountRepository.findAllByMyTrashId(entity.getId_my_trash()).stream()
                .findFirst()
                .ifPresent(link -> entity.setId_magasin_factory(link.getId_object_place_trash()));
    }

    private void syncMyTrashFactoryLink(MyTrash myTrash, Long factoryId) {
        if (myTrash.getId_my_trash() == null) {
            return;
        }

        List<MyTrashCount> existing = myTrashCountRepository.findAllByMyTrashId(myTrash.getId_my_trash());
        if (factoryId == null) {
            if (!existing.isEmpty()) {
                myTrashCountRepository.deleteAll(existing);
            }
            return;
        }

        MagasinFactory factory = magasinFactoryRepository.findById(factoryId)
                .orElseThrow(() -> new RuntimeException("MagasinFactory not found with id: " + factoryId));

        for (MyTrashCount link : existing) {
            if (link.getId_object_place_trash() != null
                    && factoryId.equals(link.getId_object_place_trash().getId_magasin_factory())) {
                return;
            }
        }

        if (!existing.isEmpty()) {
            MyTrashCount link = existing.get(0);
            link.setId_object_place_trash(factory);
            myTrashCountRepository.save(link);
            if (existing.size() > 1) {
                myTrashCountRepository.deleteAll(existing.subList(1, existing.size()));
            }
            return;
        }

        myTrashCountRepository.save(MyTrashCount.builder()
                .id_my_trash(myTrash)
                .id_object_place_trash(factory)
                .build());
    }

    public MyTrash createMyTrash(MyTrashRequest request) {
        log.info("Creating MyTrash");

        ClassDanger classDanger = resolveOptionalClassDanger(request.getId_class_danger());

        MagazinTrash magazinTrash = magazinTrashRepository.findById(request.getId_magazin_trash())
                .orElseThrow(() -> new RuntimeException("MagazinTrash not found with id: " + request.getId_magazin_trash()));

        magasinFactoryRepository.findById(request.getId_magasin_factory())
                .orElseThrow(() -> new RuntimeException("MagasinFactory not found with id: " + request.getId_magasin_factory()));

        MyTrash entity = MyTrash.builder()
                .id_class_danger(classDanger)
                .id_magazin_trash(magazinTrash)
                .value_trash(request.getValue_trash())
                .build();

        MyTrash saved = myTrashRepository.save(entity);
        syncMyTrashFactoryLink(saved, request.getId_magasin_factory());
        enrichMyTrashWithFactory(saved);
        return saved;
    }

    @Transactional(readOnly = true)
    public List<MyTrash> findAllMyTrashes() {
        log.info("Fetching all MyTrashes");
        List<MyTrash> list = myTrashRepository.findAll();
        list.forEach(this::enrichMyTrashWithFactory);
        return list;
    }

    @Transactional(readOnly = true)
    public MyTrash findByIdMyTrash(Long id) {
        log.info("Fetching MyTrash by id: {}", id);
        MyTrash entity = myTrashRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MyTrash not found with id: " + id));
        enrichMyTrashWithFactory(entity);
        return entity;
    }

    public MyTrash updateMyTrash(Long id, MyTrashRequest request) {
        log.info("Updating MyTrash with id: {}", id);

        MyTrash entity = myTrashRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MyTrash not found with id: " + id));

        if (request.getId_class_danger() != null) {
            entity.setId_class_danger(resolveOptionalClassDanger(request.getId_class_danger()));
        }

        if (request.getId_magazin_trash() != null) {
            MagazinTrash magazinTrash = magazinTrashRepository.findById(request.getId_magazin_trash())
                    .orElseThrow(() -> new RuntimeException("MagazinTrash not found with id: " + request.getId_magazin_trash()));
            entity.setId_magazin_trash(magazinTrash);
        }

        if (request.getId_magasin_factory() != null) {
            syncMyTrashFactoryLink(entity, request.getId_magasin_factory());
        }

        if (request.getValue_trash() != null) {
            entity.setValue_trash(request.getValue_trash());
        }

        MyTrash saved = myTrashRepository.save(entity);
        enrichMyTrashWithFactory(saved);
        return saved;
    }

    public void deleteMyTrash(Long id) {
        log.info("Deleting MyTrash with id: {}", id);

        if (!myTrashRepository.existsById(id)) {
            throw new RuntimeException("MyTrash not found with id: " + id);
        }

        List<MyTrashCount> links = myTrashCountRepository.findAllByMyTrashId(id);
        if (!links.isEmpty()) {
            myTrashCountRepository.deleteAll(links);
        }

        myTrashRepository.deleteById(id);
    }

    // ==================== DROP AIR CRUD ====================

    public DropAir createDropAir(DropAirRequest request) {
        log.info("Creating DropAir");

        ClassDanger classDanger = resolveOptionalClassDanger(request.getId_class_danger());

        NameDropAirTrash nameDropAirTrash = nameDropAirTrashRepository.findById(request.getId_name_grope_air())
                .orElseThrow(() -> new RuntimeException("NameDropAirTrash not found with id: " + request.getId_name_grope_air()));

        MagasinFactory magasinFactory = magasinFactoryRepository.findById(request.getId_magasin_factory())
                .orElseThrow(() -> new RuntimeException("MagasinFactory not found with id: " + request.getId_magasin_factory()));

        DropAir entity = DropAir.builder()
                .id_class_danger(classDanger)
                .id_name_grope_air(nameDropAirTrash)
                .id_magasin_factory(magasinFactory)
                .value_drop_trash(request.getValue_drop_trash())
                .build();

        return dropAirRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<DropAir> findAllDropAirs() {
        log.info("Fetching all DropAirs");
        return dropAirRepository.findAll();
    }

    @Transactional(readOnly = true)
    public DropAir findByIdDropAir(Long id) {
        log.info("Fetching DropAir by id: {}", id);
        return dropAirRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DropAir not found with id: " + id));
    }

    public DropAir updateDropAir(Long id, DropAirRequest request) {
        log.info("Updating DropAir with id: {}", id);

        DropAir entity = dropAirRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DropAir not found with id: " + id));

        if (request.getId_class_danger() != null) {
            entity.setId_class_danger(resolveOptionalClassDanger(request.getId_class_danger()));
        }

        if (request.getId_name_grope_air() != null) {
            NameDropAirTrash nameDropAirTrash = nameDropAirTrashRepository.findById(request.getId_name_grope_air())
                    .orElseThrow(() -> new RuntimeException("NameDropAirTrash not found with id: " + request.getId_name_grope_air()));
            entity.setId_name_grope_air(nameDropAirTrash);
        }

        if (request.getId_magasin_factory() != null) {
            MagasinFactory magasinFactory = magasinFactoryRepository.findById(request.getId_magasin_factory())
                    .orElseThrow(() -> new RuntimeException("MagasinFactory not found with id: " + request.getId_magasin_factory()));
            entity.setId_magasin_factory(magasinFactory);
        }

        if (request.getValue_drop_trash() != null) {
            entity.setValue_drop_trash(request.getValue_drop_trash());
        }

        return dropAirRepository.save(entity);
    }

    public void deleteDropAir(Long id) {
        log.info("Deleting DropAir with id: {}", id);

        if (!dropAirRepository.existsById(id)) {
            throw new RuntimeException("DropAir not found with id: " + id);
        }

        dropAirRepository.deleteById(id);
    }

    // ==================== NUMBER PHONE CRUD ====================

    public NumberPhone createNumberPhone(NumberPhoneRequest request) {
        log.info("Creating NumberPhone with number: {}", request.getNumber());

        // Если указан idObjectPlaceTrash, создаем связь
        if (request.getIdObjectPlaceTrash() != null) {
            NumberPhone phone = numberPhoneRepository.findByNumber(request.getNumber())
                    .orElseGet(() -> {
                        NumberPhone newPhone = NumberPhone.builder()
                                .number(request.getNumber())
                                .build();
                        return numberPhoneRepository.save(newPhone);
                    });

            MagasinFactory magasinFactory = magasinFactoryRepository.findById(request.getIdObjectPlaceTrash())
                    .orElseThrow(() -> new RuntimeException("MagasinFactory not found with id: " + request.getIdObjectPlaceTrash()));

            // Проверяем, существует ли уже связь
            if (!numberPhoneCountRepository.existsLink(request.getIdObjectPlaceTrash(), phone.getId_phone_number())) {
                NumberPhoneCount link = NumberPhoneCount.builder()
                        .id_phone_number(phone)
                        .id_object_place_trash(magasinFactory)
                        .ur_ob(request.getUr_ob() != null ? request.getUr_ob() : 0)
                        .build();
                numberPhoneCountRepository.save(link);
            }

            return phone;
        }

        // Иначе просто создаем номер
        NumberPhone entity = NumberPhone.builder()
                .number(request.getNumber())
                .build();

        return numberPhoneRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<NumberPhone> findAllNumberPhones() {
        log.info("Fetching all NumberPhones");
        return numberPhoneRepository.findAll();
    }

    @Transactional(readOnly = true)
    public NumberPhone findByIdNumberPhone(Long id) {
        log.info("Fetching NumberPhone by id: {}", id);
        return numberPhoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("NumberPhone not found with id: " + id));
    }

    public NumberPhone updateNumberPhone(Long id, NumberPhoneRequest request) {
        log.info("Updating NumberPhone with id: {}", id);

        NumberPhone entity = numberPhoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("NumberPhone not found with id: " + id));

        if (request.getNumber() != null) {
            String num = request.getNumber();
            numberPhoneRepository.findByNumber(num).ifPresent(existing -> {
                if (!existing.getId_phone_number().equals(id)) {
                    throw new RuntimeException("Такой номер телефона уже есть в справочнике");
                }
            });
            entity.setNumber(num);
        }

        return numberPhoneRepository.save(entity);
    }

    public void deleteNumberPhone(Long id) {
        log.info("Deleting NumberPhone with id: {}", id);

        if (!numberPhoneRepository.existsById(id)) {
            throw new RuntimeException("NumberPhone not found with id: " + id);
        }

        // Сначала удаляем все связи
        numberPhoneCountRepository.deleteAllByPhoneId(id);
        numberPhoneRepository.deleteById(id);
    }

    // ==================== NUMBER PHONE COUNT CRUD ====================

    public NumberPhoneCount createNumberPhoneCount(Long objectPlaceId, Long phoneId, int urOb) {
        log.info("Creating NumberPhoneCount link between object {} and phone {}", objectPlaceId, phoneId);

        NumberPhone numberPhone = numberPhoneRepository.findById(phoneId)
                .orElseThrow(() -> new RuntimeException("NumberPhone not found with id: " + phoneId));

        MagasinFactory magasinFactory = magasinFactoryRepository.findById(objectPlaceId)
                .orElseThrow(() -> new RuntimeException("MagasinFactory not found with id: " + objectPlaceId));

        // Обновляем ur_ob, если связь уже есть; иначе создаём
        if (numberPhoneCountRepository.existsLink(objectPlaceId, phoneId)) {
            return numberPhoneCountRepository.findLink(objectPlaceId, phoneId)
                    .map(link -> {
                        link.setUr_ob(urOb);
                        return numberPhoneCountRepository.save(link);
                    })
                    .orElseThrow(() -> new RuntimeException("Link already exists between phone and object"));
        }

        NumberPhoneCount entity = NumberPhoneCount.builder()
                .id_phone_number(numberPhone)
                .id_object_place_trash(magasinFactory)
                .ur_ob(urOb)
                .build();

        return numberPhoneCountRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<NumberPhoneCount> findAllNumberPhoneCounts() {
        log.info("Fetching all NumberPhoneCounts");
        return numberPhoneCountRepository.findAll();
    }

    @Transactional(readOnly = true)
    public NumberPhoneCount findByIdNumberPhoneCount(Long id) {
        log.info("Fetching NumberPhoneCount by id: {}", id);
        return numberPhoneCountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("NumberPhoneCount not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<NumberPhoneCount> findNumberPhoneCountsByObjectPlace(Long objectPlaceId) {
        log.info("Fetching NumberPhoneCounts for object: {}", objectPlaceId);
        return numberPhoneCountRepository.findAllByObjectPlaceId(objectPlaceId);
    }

    public NumberPhoneCount updateNumberPhoneCount(Long id, int urOb) {
        log.info("Updating NumberPhoneCount with id: {}", id);

        NumberPhoneCount entity = numberPhoneCountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("NumberPhoneCount not found with id: " + id));

        entity.setUr_ob(urOb);

        return numberPhoneCountRepository.save(entity);
    }

    public void deleteNumberPhoneCount(Long id) {
        log.info("Deleting NumberPhoneCount with id: {}", id);

        if (!numberPhoneCountRepository.existsById(id)) {
            throw new RuntimeException("NumberPhoneCount not found with id: " + id);
        }

        numberPhoneCountRepository.deleteById(id);
    }

    public void unlinkNumberPhoneFromObject(Long objectPlaceId, Long phoneId) {
        log.info("Unlinking phone {} from object {}", phoneId, objectPlaceId);

        if (!numberPhoneCountRepository.existsLink(objectPlaceId, phoneId)) {
            throw new RuntimeException("Link not found between phone " + phoneId + " and object " + objectPlaceId);
        }

        numberPhoneCountRepository.deleteLink(objectPlaceId, phoneId);
    }

    // ==================== MY TRASH COUNT CRUD (если используется) ====================

    public MyTrashCount createMyTrashCount(Long myTrashId, Long objectPlaceId) {
        log.info("Creating MyTrashCount link");

        MyTrash myTrash = myTrashRepository.findById(myTrashId)
                .orElseThrow(() -> new RuntimeException("MyTrash not found with id: " + myTrashId));

        MagasinFactory magasinFactory = magasinFactoryRepository.findById(objectPlaceId)
                .orElseThrow(() -> new RuntimeException("MagasinFactory not found with id: " + objectPlaceId));

        MyTrashCount entity = MyTrashCount.builder()
                .id_my_trash(myTrash)
                .id_object_place_trash(magasinFactory)
                .build();

        return myTrashCountRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<MyTrashCount> findAllMyTrashCounts() {
        log.info("Fetching all MyTrashCounts");
        return myTrashCountRepository.findAll();
    }

    @Transactional(readOnly = true)
    public MyTrashCount findByIdMyTrashCount(Long id) {
        log.info("Fetching MyTrashCount by id: {}", id);
        return myTrashCountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MyTrashCount not found with id: " + id));
    }

    public void deleteMyTrashCount(Long id) {
        log.info("Deleting MyTrashCount with id: {}", id);

        if (!myTrashCountRepository.existsById(id)) {
            throw new RuntimeException("MyTrashCount not found with id: " + id);
        }

        myTrashCountRepository.deleteById(id);
    }

    // ==================== PAGED LIST (page/size/q/sort/dir) ====================

    @Transactional(readOnly = true)
    public PageResponse<Region> findAllRegionsPaged(Integer page, Integer size, String q, String sort, String dir) {
        Pageable pageable = PageSupport.pageable(page, size, "id_region");
        return PageResponse.from(regionRepository.findAll(
                PageSupport.textSearch(q, "id_region", sort, dir, "id_region", false), pageable));
    }

    @Transactional(readOnly = true)
    public PageResponse<District> findAllDistrictsPaged(Integer page, Integer size, String q, String sort, String dir) {
        Pageable pageable = PageSupport.pageable(page, size, "id_district");
        return PageResponse.from(districtRepository.findAll(
                PageSupport.districtSpec(q, sort, dir), pageable));
    }

    @Transactional(readOnly = true)
    public PageResponse<Cities> findAllCitiesPaged(Integer page, Integer size, String q, String sort, String dir) {
        Pageable pageable = PageSupport.pageable(page, size, "id_cities");
        return PageResponse.from(citiesRepository.findAll(
                PageSupport.citiesSpec(q, sort, dir), pageable));
    }

    @Transactional(readOnly = true)
    public PageResponse<ClassDanger> findAllClassDangersPaged(Integer page, Integer size, String q, String sort, String dir) {
        Pageable pageable = PageSupport.pageable(page, size, "id_class_danger");
        return PageResponse.from(classDangerRepository.findAll(
                PageSupport.textSearch(q, "id_class_danger", sort, dir, "id_class_danger", false), pageable));
    }

    @Transactional(readOnly = true)
    public PageResponse<MagazinTrash> findAllMagazinTrashesPaged(Integer page, Integer size, String q, String sort, String dir) {
        Pageable pageable = PageSupport.pageable(page, size, "id_magazin_trash");
        return PageResponse.from(magazinTrashRepository.findAll(
                PageSupport.magazinTrashSpec(q, sort, dir), pageable));
    }

    @Transactional(readOnly = true)
    public PageResponse<PhysStateTrash> findAllPhysStateTrashesPaged(Integer page, Integer size, String q, String sort, String dir) {
        Pageable pageable = PageSupport.pageable(page, size, "id_mame_group");
        return PageResponse.from(physStateTrashRepository.findAll(
                PageSupport.physStateTrashSpec(q, sort, dir), pageable));
    }

    @Transactional(readOnly = true)
    public PageResponse<ShortDiscribeTechnology> findAllShortDiscribeTechnologiesPaged(
            Integer page, Integer size, String q, String sort, String dir) {
        Pageable pageable = PageSupport.pageable(page, size, "id_short_discribe_technology");
        return PageResponse.from(shortDiscribeTechnologyRepository.findAll(
                PageSupport.shortDiscribeTechnologySpec(q, sort, dir), pageable));
    }

    @Transactional(readOnly = true)
    public PageResponse<NameDropAirTrash> findAllNameDropAirTrashesPaged(
            Integer page, Integer size, String q, String sort, String dir) {
        Pageable pageable = PageSupport.pageable(page, size, "id_name_grope_air");
        return PageResponse.from(nameDropAirTrashRepository.findAll(
                PageSupport.nameDropAirTrashSpec(q, sort, dir), pageable));
    }

    @Transactional(readOnly = true)
    public List<Technology> findTechnologiesByFactory(Long factoryId) {
        return technologyRepository.findAllByFactoryId(factoryId);
    }

    @Transactional(readOnly = true)
    public List<DropAir> findDropAirsByFactory(Long factoryId) {
        return dropAirRepository.findAllByFactoryId(factoryId);
    }

    @Transactional(readOnly = true)
    public List<MyTrash> findMyTrashesByFactory(Long factoryId) {
        return myTrashCountRepository.findAllByFactoryId(factoryId).stream()
                .map(MyTrashCount::getId_my_trash)
                .filter(t -> t != null && t.getId_my_trash() != null)
                .peek(this::enrichMyTrashWithFactory)
                .toList();
    }

    @Transactional(readOnly = true)
    public PageResponse<Technology> findAllTechnologiesPaged(Integer page, Integer size, String q, String sort, String dir) {
        Pageable pageable = PageSupport.pageable(page, size, "id_technology");
        return PageResponse.from(technologyRepository.findAll(
                PageSupport.technologySpec(q, sort, dir), pageable));
    }

    @Transactional(readOnly = true)
    public PageResponse<MagasinFactory> findAllMagasinFactoriesPaged(
            Integer page, Integer size, String q, String sort, String dir) {
        Pageable pageable = PageSupport.pageable(page, size, "id_magasin_factory");
        // По умолчанию — рег. номер (то, что видно в первой колонке), а не PK
        return PageResponse.from(magasinFactoryRepository.findAll(
                PageSupport.textSearch(q, "id_magasin_factory", sort, dir, "id_registration", true), pageable));
    }

    @Transactional(readOnly = true)
    public PageResponse<MyTrash> findAllMyTrashesPaged(Integer page, Integer size, String q, String sort, String dir) {
        Pageable pageable = PageSupport.pageable(page, size, "id_my_trash");
        Page<MyTrash> result = myTrashRepository.findAll(
                PageSupport.myTrashSpec(q, sort, dir), pageable);
        result.getContent().forEach(this::enrichMyTrashWithFactory);
        return PageResponse.from(result);
    }

    @Transactional(readOnly = true)
    public PageResponse<DropAir> findAllDropAirsPaged(Integer page, Integer size, String q, String sort, String dir) {
        Pageable pageable = PageSupport.pageable(page, size, "id_drop_air");
        return PageResponse.from(dropAirRepository.findAll(
                PageSupport.dropAirSpec(q, sort, dir), pageable));
    }

    @Transactional(readOnly = true)
    public PageResponse<NumberPhone> findAllNumberPhonesPaged(Integer page, Integer size, String q, String sort, String dir) {
        Pageable pageable = PageSupport.pageable(page, size, "id_phone_number");
        return PageResponse.from(numberPhoneRepository.findAll(
                PageSupport.numberPhoneSpec(q, sort, dir), pageable));
    }
}