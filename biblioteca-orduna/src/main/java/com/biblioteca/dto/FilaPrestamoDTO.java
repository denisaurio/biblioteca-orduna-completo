package com.biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record FilaPrestamoDTO(
        Long id,
        @JsonProperty("tipo_prestamo") String tipoPrestamo,
        @JsonProperty("fecha_prestamo") LocalDateTime fechaPrestamo,
        @JsonProperty("fecha_vencimiento") LocalDate fechaVencimiento,
        @JsonProperty("fecha_devolucion") LocalDateTime fechaDevolucion,
        @JsonProperty("dias_retraso") int diasRetraso,
        @JsonProperty("titulo_libro") String tituloLibro,
        @JsonProperty("codigo_inventario") String codigoInventario,
        String lector,
        String dni,
        /** ACTIVO, VENCIDO (activo y pasado de fecha) o DEVUELTO */
        String estado
) {}
