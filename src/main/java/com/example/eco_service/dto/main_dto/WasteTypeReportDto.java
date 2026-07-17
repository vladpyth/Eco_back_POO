package com.example.eco_service.dto.main_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WasteTypeReportDto {
    // Информация о типе отхода
    private Long id;
    private Integer codeTrash;           // Код отхода (code_trash)
    private String nameTrash;            // Наименование отхода (name_trash)
    private Integer classDanger;         // Класс опасности

    // Список организаций, работающих с этим отходом
    private List<FactoryForWasteReportDto> factories;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FactoryForWasteReportDto {
        private Long id;
        private String nameObj;           // Наименование объекта
        private String addressObj;        // Место нахождения объекта
        private String phoneObj;          // Телефон объекта
        private String nameOwn;           // Собственник
        private String addressOwn;        // Место нахождения собственника
        private String phoneOwn;          // Телефон собственника
        private Boolean objUseTrash;      // Обезвреживает собственные
        private Boolean objAcceptTrash;   // Принимает от других
        private Float valueTrash;         // Количество отхода (в PDF по Technology не заполняется)
        private String registrationNumber; // Регистрационный номер
        private String ynp;               // УНП предприятия (MagasinFactory.YNP)
    }
}
