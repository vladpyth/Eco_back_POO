package com.example.eco_service.services;


import com.example.eco_service.dto.main_dto.WasteTypeReportDto;
import com.example.eco_service.dto.main_dto.PortalWasteRowDto;
import com.example.eco_service.dto.response.PageResponse;
import com.example.eco_service.entities.*;
import com.example.eco_service.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final InterfMagazinTrash magazinTrashRepository;
    private final InterfTechnology technologyRepository;

    /**
     * Получить все данные для отчёта по типам отходов
     * (предприятия — через Technology).
     */
    public List<WasteTypeReportDto> getAllForReport() {
        log.info("Fetching all waste types for report (via Technology)");

        Map<Long, List<Technology>> techsByWaste = loadTechsGroupedByWaste(null);

        List<WasteTypeReportDto> reportData = new ArrayList<>();
        for (MagazinTrash wasteType : magazinTrashRepository.findAll()) {
            reportData.add(convertToReportDto(
                    wasteType,
                    techsByWaste.getOrDefault(wasteType.getId_magazin_trash(), List.of())));
        }

        reportData.sort(Comparator.comparing(WasteTypeReportDto::getCodeTrash));
        log.info("Generated report data for {} waste types", reportData.size());
        return reportData;
    }

    /**
     * Плоский публичный реестр (waste × factory) с серверной пагинацией.
     * Одна Technology-строка ≈ одна строка таблицы (связь отход–предприятие).
     */
    public PageResponse<PortalWasteRowDto> getPortalTablePaged(
            Integer page, Integer size, String q, String sort, String dir) {
        log.info("Portal waste table page={} size={} q={}", page, size, q);

        Specification<Technology> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isNotNull(root.get("id_magazin_trash")));
            predicates.add(cb.isNotNull(root.get("id_magasin_factory")));

            Join<Technology, MagazinTrash> waste = root.join("id_magazin_trash", JoinType.INNER);
            Join<Technology, MagasinFactory> factory = root.join("id_magasin_factory", JoinType.INNER);

            boolean isCount = query != null
                    && (query.getResultType() == Long.class || query.getResultType() == long.class);
            if (!isCount && query != null) {
                applyPortalSort(query, cb, waste, factory, sort, dir, root);
            }

            if (q != null && !q.isBlank()) {
                String raw = q.trim();
                String like = "%" + raw.toLowerCase() + "%";
                List<Predicate> textOr = new ArrayList<>();
                textOr.add(cb.like(cb.lower(waste.get("name_trash")), like));
                textOr.add(cb.like(cb.lower(factory.get("name_obj")), like));
                textOr.add(cb.like(cb.lower(factory.get("name_own")), like));
                textOr.add(cb.like(cb.lower(factory.get("address_obj")), like));
                textOr.add(cb.like(cb.lower(factory.get("address_own")), like));
                textOr.add(cb.like(cb.lower(factory.get("id_registration")), like));
                textOr.add(cb.like(cb.lower(factory.get("YNP")), like));
                try {
                    textOr.add(cb.equal(waste.get("code_trash"), Integer.parseInt(raw)));
                } catch (NumberFormatException ignored) {
                    /* not a code */
                }
                predicates.add(cb.or(textOr.toArray(new Predicate[0])));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Technology> result = technologyRepository.findAll(
                spec, PageSupport.pageable(page, size, "id_technology"));
        return PageResponse.from(result.map(this::toPortalRow));
    }

    private void applyPortalSort(
            jakarta.persistence.criteria.CriteriaQuery<?> query,
            jakarta.persistence.criteria.CriteriaBuilder cb,
            Join<Technology, MagazinTrash> waste,
            Join<Technology, MagasinFactory> factory,
            String sort,
            String dir,
            jakarta.persistence.criteria.Root<Technology> root) {
        boolean asc = dir == null || !"desc".equalsIgnoreCase(dir.trim());
        String key = sort == null ? "" : sort.trim();
        jakarta.persistence.criteria.Order order = switch (key) {
            case "wasteName" -> asc ? cb.asc(waste.get("name_trash")) : cb.desc(waste.get("name_trash"));
            case "wasteCode" -> asc ? cb.asc(waste.get("code_trash")) : cb.desc(waste.get("code_trash"));
            case "objectName" -> asc ? cb.asc(factory.get("name_obj")) : cb.desc(factory.get("name_obj"));
            case "objectLocation" -> asc ? cb.asc(factory.get("address_obj")) : cb.desc(factory.get("address_obj"));
            case "ownerName" -> asc ? cb.asc(factory.get("name_own")) : cb.desc(factory.get("name_own"));
            case "ownerLocation" -> asc ? cb.asc(factory.get("address_own")) : cb.desc(factory.get("address_own"));
            case "registrationNumber" -> asc ? cb.asc(factory.get("id_registration")) : cb.desc(factory.get("id_registration"));
            case "unp" -> asc ? cb.asc(factory.get("YNP")) : cb.desc(factory.get("YNP"));
            default -> asc ? cb.asc(waste.get("code_trash")) : cb.desc(waste.get("code_trash"));
        };
        query.orderBy(order, cb.asc(root.get("id_technology")));
    }

    private PortalWasteRowDto toPortalRow(Technology t) {
        MagazinTrash waste = t.getId_magazin_trash();
        MagasinFactory factory = t.getId_magasin_factory();
        return PortalWasteRowDto.builder()
                .wasteCode(waste != null ? String.valueOf(waste.getCode_trash()) : "")
                .wasteName(waste != null ? waste.getName_trash() : "")
                .registrationNumber(factory != null ? factory.getId_registration() : "")
                .objectName(factory != null ? factory.getName_obj() : "")
                .objectLocation(factory != null ? factory.getAddress_obj() : "")
                .objectPhone(factory != null ? getPhoneByUrRole(factory, true) : "")
                .ownerName(factory != null ? factory.getName_own() : "")
                .ownerLocation(factory != null ? factory.getAddress_own() : "")
                .ownerPhone(factory != null ? getPhoneByUrRole(factory, false) : "")
                .treatsOwn(boolLabel(factory != null ? factory.getObj_use_trash() : null))
                .acceptsFromOthers(boolLabel(factory != null ? factory.getObj_accept_trash() : null))
                .unp(factory != null ? factory.getYNP() : "")
                .build();
    }

    private static String boolLabel(Boolean v) {
        if (v == null) return "";
        return v ? "Да" : "Нет";
    }

    /**
     * Получить данные для отчёта по конкретному типу отхода
     */
    public WasteTypeReportDto getForReportById(Long id) {
        log.info("Fetching waste type report data for id: {}", id);

        MagazinTrash wasteType = magazinTrashRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MagazinTrash not found with id: " + id));

        List<Technology> techs = loadTechsGroupedByWaste(null)
                .getOrDefault(wasteType.getId_magazin_trash(), List.of());
        return convertToReportDto(wasteType, techs);
    }

    /**
     * Получить данные для отчёта по коду отхода
     */
    public WasteTypeReportDto getForReportByCode(Integer codeTrash) {
        log.info("Fetching waste type report data for code: {}", codeTrash);

        if (codeTrash == null) {
            throw new RuntimeException("Code trash cannot be null");
        }

        MagazinTrash wasteType = magazinTrashRepository.findAll().stream()
                .filter(w -> w.getCode_trash() == codeTrash)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("MagazinTrash not found with code: " + codeTrash));

        List<Technology> techs = loadTechsGroupedByWaste(null)
                .getOrDefault(wasteType.getId_magazin_trash(), List.of());
        return convertToReportDto(wasteType, techs);
    }

    /**
     * Получить данные для отчёта по классу опасности (из Technology)
     */
    public List<WasteTypeReportDto> getForReportByClassDanger(Integer classDanger) {
        log.info("Fetching waste type report data for class danger: {} (via Technology)", classDanger);

        if (classDanger == null) {
            throw new RuntimeException("Class danger cannot be null");
        }

        Map<Long, List<Technology>> techsByWaste = loadTechsGroupedByWaste(classDanger);

        return techsByWaste.entrySet().stream()
                .map(e -> {
                    MagazinTrash waste = e.getValue().get(0).getId_magazin_trash();
                    return convertToReportDto(waste, e.getValue());
                })
                .sorted(Comparator.comparing(WasteTypeReportDto::getCodeTrash))
                .collect(Collectors.toList());
    }

    // ==================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ====================

    /**
     * Technology с отходом и предприятием, сгруппированные по id MagazinTrash.
     * @param classDangerFilter если не null — только строки с этим классом в Technology
     */
    private Map<Long, List<Technology>> loadTechsGroupedByWaste(Integer classDangerFilter) {
        return technologyRepository.findAll().stream()
                .filter(t -> t.getId_magazin_trash() != null)
                .filter(t -> t.getId_magasin_factory() != null)
                .filter(t -> classDangerFilter == null
                        || (t.getId_class_danger() != null
                        && t.getId_class_danger().getClass_danger() == classDangerFilter))
                .collect(Collectors.groupingBy(
                        t -> t.getId_magazin_trash().getId_magazin_trash(),
                        LinkedHashMap::new,
                        Collectors.toList()));
    }

    private String getPhoneByUrRole(MagasinFactory factory, boolean isObject) {
        if (factory.getNumberPhoneCounts() == null || factory.getNumberPhoneCounts().isEmpty()) {
            return "";
        }

        return factory.getNumberPhoneCounts().stream()
                .filter(npc -> npc.getId_phone_number() != null)
                .filter(npc -> {
                    int urOb = npc.getUr_ob();
                    if (isObject) {
                        return urOb == 1 || urOb == 3;
                    } else {
                        return urOb == 0 || urOb == 3;
                    }
                })
                .map(npc -> npc.getId_phone_number().getNumber())
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .collect(Collectors.joining("\n"));
    }

    /**
     * Уникальные предприятия из строк Technology по отходу.
     */
    private List<MagasinFactory> factoriesFromTechs(List<Technology> techs) {
        Map<Long, MagasinFactory> unique = new LinkedHashMap<>();
        for (Technology t : techs) {
            MagasinFactory f = t.getId_magasin_factory();
            if (f != null && f.getId_magasin_factory() != null) {
                unique.putIfAbsent(f.getId_magasin_factory(), f);
            }
        }
        return new ArrayList<>(unique.values());
    }

    /**
     * Класс опасности — из Technology (первая ненулевая запись).
     */
    private Integer classDangerFromTechs(List<Technology> techs) {
        return techs.stream()
                .map(Technology::getId_class_danger)
                .filter(Objects::nonNull)
                .map(ClassDanger::getClass_danger)
                .findFirst()
                .orElse(null);
    }

    private WasteTypeReportDto convertToReportDto(MagazinTrash wasteType, List<Technology> techs) {
        Integer classDanger = classDangerFromTechs(techs);

        List<WasteTypeReportDto.FactoryForWasteReportDto> factoryDtos = new ArrayList<>();
        for (MagasinFactory factory : factoriesFromTechs(techs)) {
            factoryDtos.add(WasteTypeReportDto.FactoryForWasteReportDto.builder()
                    .id(factory.getId_magasin_factory())
                    .nameObj(factory.getName_obj())
                    .addressObj(factory.getAddress_obj())
                    .phoneObj(getPhoneByUrRole(factory, true))
                    .nameOwn(factory.getName_own())
                    .addressOwn(factory.getAddress_own())
                    .phoneOwn(getPhoneByUrRole(factory, false))
                    .objUseTrash(factory.getObj_use_trash())
                    .objAcceptTrash(factory.getObj_accept_trash())
                    .valueTrash(null)
                    .registrationNumber(factory.getId_registration())
                    .ynp(factory.getYNP())
                    .build());
        }

        factoryDtos.sort(Comparator.comparing(WasteTypeReportDto.FactoryForWasteReportDto::getNameObj,
                Comparator.nullsLast(String::compareTo)));

        return WasteTypeReportDto.builder()
                .id(wasteType.getId_magazin_trash())
                .codeTrash(wasteType.getCode_trash())
                .nameTrash(wasteType.getName_trash())
                .classDanger(classDanger)
                .factories(factoryDtos)
                .build();
    }
}
