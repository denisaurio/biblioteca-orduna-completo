package com.biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** estado: ACTIVA o FINALIZADA (por fecha cumplida o levantada antes). */
public record SancionResponseDTO(
        Long id,
        @JsonProperty("id_usuario") Long idUsuario,
        @JsonProperty("nombre_usuario") String nombreUsuario,
        @JsonProperty("dni_usuario") String dniUsuario,
        String motivo,
        @JsonProperty("fecha_inicio") LocalDateTime fechaInicio,
        @JsonProperty("fecha_fin") LocalDate fechaFin,
        @JsonProperty("id_bibliotecario") Long idBibliotecario,
        @JsonProperty("nombre_bibliotecario") String nombreBibliotecario,
        String estado,
        @JsonProperty("fecha_levantamiento") LocalDateTime fechaLevantamiento
) {}
