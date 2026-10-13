package com.biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

/** RF10 - Reporte de préstamos en un rango de fechas. */
public record ReportePrestamosDTO(
        LocalDate desde,
        LocalDate hasta,
        @JsonProperty("total_prestamos") long totalPrestamos,
        long activos,
        @JsonProperty("vencidos_sin_devolver") long vencidosSinDevolver,
        long devueltos,
        @JsonProperty("devueltos_con_retraso") long devueltosConRetraso,
        /** % de devoluciones que llegaron tarde. */
        @JsonProperty("porcentaje_retraso") double porcentajeRetraso,
        @JsonProperty("libros_mas_prestados") List<RankingDTO> librosMasPrestados,
        @JsonProperty("lectores_mas_activos") List<RankingDTO> lectoresMasActivos,
        List<FilaPrestamoDTO> detalle
) {}
