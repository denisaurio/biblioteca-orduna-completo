package com.biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * estado (derivado de los ejemplares, no se edita):
 * DISPONIBLE (hay al menos uno), PRESTADO (todos prestados/reservados) o SIN_EJEMPLARES.
 */
public record LibroResponseDTO(
        Long id,
        String titulo,
        String isbn,
        String editorial,
        @JsonProperty("anio_publicacion") Integer anioPublicacion,
        @JsonProperty("num_paginas") Integer numPaginas,
        List<AutorResponseDTO> autores,
        List<GeneroResponseDTO> generos,
        @JsonProperty("ejemplares_total") long ejemplaresTotal,
        @JsonProperty("ejemplares_disponibles") long ejemplaresDisponibles,
        String estado
) {}
