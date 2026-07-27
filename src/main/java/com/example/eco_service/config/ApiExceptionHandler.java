package com.example.eco_service.config;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining("; "));
        if (message.isBlank()) {
            message = "Ошибка валидации запроса";
        }
        return ResponseEntity.badRequest().body(errorBody(400, message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleUnreadable(HttpMessageNotReadableException ex) {
        String detail = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        log.warn("Не удалось разобрать JSON-запрос: {}", detail);
        // Пустые строки в JSON часто прилетают на даты/числа — не пугаем общим «формат данных»
        if (detail != null) {
            String d = detail.toLowerCase();
            if (d.contains("date") || d.contains("localdate")) {
                return ResponseEntity.badRequest().body(errorBody(400, "Некорректный формат даты (ожидается ГГГГ-ММ-ДД)"));
            }
            if (d.contains("empty string") || d.contains("from string \"\"")) {
                return ResponseEntity.badRequest().body(errorBody(400, "Пустое значение в числовом или дата-поле"));
            }
        }
        return ResponseEntity.badRequest().body(errorBody(
                400,
                detail != null && !detail.isBlank() ? "Ошибка JSON: " + detail : "Не удалось разобрать запрос"
        ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleIntegrity(DataIntegrityViolationException ex) {
        String detail = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        String message = "Нарушение ограничений БД";
        if (detail != null) {
            String d = detail.toLowerCase();
            if (d.contains("id_registration")) {
                message = "Регистрационный номер уже существует у другой записи";
            } else if (d.contains("class_danger")) {
                message = "Такой класс опасности уже есть";
            } else if (d.contains("numberphone") || d.contains("number_phone") || (d.contains("number") && d.contains("unique"))) {
                message = "Такой номер телефона уже есть в справочнике";
            } else if (d.contains("foreign key") || d.contains("violates foreign key")) {
                message = "Нельзя удалить/изменить: есть связанные записи";
            } else if (d.contains("unique") || d.contains("duplicate")) {
                message = "Значение уже используется в другой записи";
            }
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorBody(409, message));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {
        String message = ex.getMessage() != null && !ex.getMessage().isBlank()
                ? ex.getMessage()
                : "Ошибка сервера";
        HttpStatus status = message.toLowerCase().contains("не найден") || message.toLowerCase().contains("not found")
                ? HttpStatus.NOT_FOUND
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(errorBody(status.value(), message));
    }

    private String formatFieldError(FieldError e) {
        String field = e.getField();
        String msg = e.getDefaultMessage() != null ? e.getDefaultMessage() : "некорректно";
        return field + ": " + msg;
    }

    private Map<String, Object> errorBody(int status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", status);
        body.put("error", status == 400 ? "Bad Request" : status == 409 ? "Conflict" : "Error");
        body.put("message", message);
        return body;
    }
}
