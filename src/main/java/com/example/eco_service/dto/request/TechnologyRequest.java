package com.example.eco_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос для сущности Technology")
public class TechnologyRequest {

    @Schema(description = "ID класса опасности (необязателен)", example = "2")
    private Long id_class_danger;

    @Schema(description = "ID отхода из справочника", example = "8")
    @NotNull(message = "ID отхода обязателен")
    private Long id_magazin_trash;

    @Schema(description = "ID физического состояния отхода", example = "3")
    @NotNull(message = "ID физического состояния обязателен")
    private Long id_phys_trash;

    @Schema(description = "ID предприятия", example = "7")
    private Long id_magasin_factory;
}
