package com.biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AutorResponseDTO(
        Long id,
        String nombre,
        String nacionalidad,
        String biografia,
        @JsonProperty("cantidad_libros") long cantidadLibros
) {}
