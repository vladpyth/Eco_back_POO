package com.example.eco_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос для сущности MagazinTrash")
public class MagazinTrashRequest {

    @Schema(description = "ID класса опасности (необязателен)", example = "2")
    private Long id_class_danger;

    @Schema(description = "Код отхода (8 цифр)", example = "31401103")
    @NotNull(message = "Код отхода обязателен")
    @Min(value = 10000000, message = "Код должен содержать 8 цифр")
    @Max(value = 99999999, message = "Код должен содержать 8 цифр")
    private Integer code_trash;

    @Schema(description = "Название отхода", example = "Лом черных металлов")
    @NotEmpty(message = "Название отхода обязательно")
    @Size(max = 50, message = "Максимум 50 символов")
    private String name_trash;
}
