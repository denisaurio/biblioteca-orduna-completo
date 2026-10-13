package com.biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

/** RF10 - Reporte de inventario. */
public record ReporteInventarioDTO(
        @JsonProperty("fecha_generacion") LocalDateTime fechaGeneracion,
        @JsonProperty("total_libros") long totalLibros,
        @JsonProperty("total_ejemplares") long totalEjemplares,
        long disponibles,
        long prestados,
        long reservados,
        long danados,
        long perdidos,
        @JsonProperty("de_baja") long deBaja,
        /** % de ejemplares en circulación (no dañados, perdidos ni de baja) que están prestados. */
        @JsonProperty("porcentaje_en_uso") double porcentajeEnUso,
        List<FilaInventarioDTO> detalle
) {}
