package com.biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/** Fila de la Bitácora de turnos. tipo: GERENTE o BIBLIOTECARIO (rol de quien abrió el turno). */
public record TurnoResponseDTO(
        Long id,
        @JsonProperty("id_bibliotecario") Long idBibliotecario,
        @JsonProperty("nombre_bibliotecario") String nombreBibliotecario,
        String tipo,
        @JsonProperty("fecha_apertura") LocalDateTime fechaApertura,
        @JsonProperty("fecha_cierre") LocalDateTime fechaCierre,
        String observaciones,
        String estado
) {}
