package com.example.eco_service.services;


import com.example.eco_service.dto.main_dto.WasteTypeReportDto;
import com.example.eco_service.entities.*;
import com.example.eco_service.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
