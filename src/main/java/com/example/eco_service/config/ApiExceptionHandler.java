package com.example.eco_service.config;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
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
        String message = "Некорректный формат данных";
        if (detail != null && detail.toLowerCase().contains("date")) {
            message = "Некорректный формат даты (ожидается ГГГГ-ММ-ДД)";
        }
        return ResponseEntity.badRequest().body(errorBody(400, message));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleIntegrity(DataIntegrityViolationException ex) {
        String detail = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        String message = "Нарушение ограничений БД";
        if (detail != null) {
            if (detail.contains("id_registration") || detail.toLowerCase().contains("unique")) {
                message = "Регистрационный номер уже существует";
            } else if (detail.contains("foreign key") || detail.contains("violates foreign key")) {
                message = "Нельзя удалить/изменить: есть связанные записи";
            }
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorBody(409, message));
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
