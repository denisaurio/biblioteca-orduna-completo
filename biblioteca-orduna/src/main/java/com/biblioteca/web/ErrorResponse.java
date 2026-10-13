package com.biblioteca.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Formato único de error que recibe el frontend. Ejemplo:
 * <pre>
 * {
 *   "timestamp": "2026-10-01T18:30:00",
 *   "status": 409,
 *   "error": "Conflict",
 *   "mensaje": "Ya existe un libro con el ISBN 978843760",
 *   "path": "/api/libros",
 *   "errores": { "isbn": "no debe estar vacío" }   // solo en errores de validación
 * }
 * </pre>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String mensaje,
        String path,
        Map<String, String> errores
) {
    public static ErrorResponse de(HttpStatus status, String mensaje, String path) {
        return new ErrorResponse(LocalDateTime.now(), status.value(), status.getReasonPhrase(), mensaje, path, null);
    }

    public static ErrorResponse deValidacion(String mensaje, String path, Map<String, String> errores) {
        HttpStatus s = HttpStatus.BAD_REQUEST;
        return new ErrorResponse(LocalDateTime.now(), s.value(), s.getReasonPhrase(), mensaje, path, errores);
    }
}
