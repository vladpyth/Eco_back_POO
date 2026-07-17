package com.example.eco_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос для сущности MagasinFactory")
public class MagasinFactoryRequest {

    @Schema(description = "Регистрационный номер", example = "REG1234567")
    @NotEmpty(message = "Регистрационный номер обязателен")
    @Size(max = 10, message = "Максимум 10 символов")
    private String id_registration;

    @Schema(description = "Дата регистрации", example = "2020-05-15")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date_register;

    @Schema(description = "ID города", example = "3")
    private Long id_cities;

    @Schema(description = "ID краткого описания технологии", example = "4")
    private Long id_short_discribe_technology;

    @Schema(description = "ID технологии", example = "6")
    private Long id_technology;

    @Schema(description = "Название объекта", example = "Завод Металлург")
    @NotEmpty(message = "Название объекта обязательно")
    private String name_obj;

    @Schema(description = "ФИО владельца", example = "Иванов Иван Иванович")
    @NotEmpty(message = "ФИО владельца обязательно")
    private String name_own;

    @Schema(description = "Адрес владельца", example = "г. Минск, ул. Ленина 1, кв 10")
    @NotEmpty(message = "Адрес владельца обязателен")
    private String address_own;

    @Schema(description = "Адрес объекта", example = "г. Минск, ул. Промышленная 15")
    @NotEmpty(message = "Адрес объекта обязателен")
    private String address_obj;

    @Schema(description = "Разрабатывающая организация", example = "Гипрострой")
    private String develop_organization;

    @Schema(description = "Утвержденный проект", example = "Проект-2024-001")
    private String confirmed_project;

    @Schema(description = "Дата утверждения", example = "2024-01-20")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date_approve;

    @Schema(description = "Наличие заключения документации", example = "true")
    private Boolean conclusion_documentation;

    @Schema(description = "Акт использования", example = "Акт №45")
    private String act_use;

    @Schema(description = "Требования актов", example = "Соблюдать ПДК")
    private String requirements_acts;

    @Schema(description = "Использование отходов", example = "true")
    private Boolean obj_use_trash;

    @Schema(description = "Прием отходов", example = "false")
    private Boolean obj_accept_trash;

    @Schema(description = "Характер производства", example = "Металлургия")
    private String character_prod;

    @Schema(description = "Проектная мощность (год)", example = "5000")
    private String project_power_yer;

    @Schema(description = "Проектная мощность (час)", example = "2.5")
    private String project_power_hr;

    @Schema(description = "Фактическая мощность", example = "4800")
    private String facticheskay_power;

    @Schema(description = "УНП (Учетный номер плательщика)", example = "123456789")
    @Size(max = 12, message = "УНП максимум 12 символов")
    @JsonProperty("YNP")
    private String YNP;

    @Schema(description = "Значение", example = "100")
    private Integer value;
}
