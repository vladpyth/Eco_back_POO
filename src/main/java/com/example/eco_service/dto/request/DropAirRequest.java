package com.example.eco_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос для сущности DropAir")
public class DropAirRequest {

    @Schema(description = "ID класса опасности (необязателен)", example = "2")
    private Long id_class_danger;

    @Schema(description = "ID наименования выброса", example = "10")
    @NotNull(message = "ID наименования выброса обязателен")
    private Long id_name_grope_air;

    @Schema(description = "ID предприятия", example = "7")
    @NotNull(message = "ID предприятия обязателен")
    private Long id_magasin_factory;

    @Schema(description = "Значение выброса (тонн/год)", example = "12.5")
    @NotNull(message = "Значение выброса обязательно")
    @DecimalMin(value = "0.0", inclusive = false, message = "Значение должно быть больше 0")
    private Float value_drop_trash;
}
