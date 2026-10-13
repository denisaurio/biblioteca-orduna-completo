package com.biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * RF01 - Body de POST /api/libros y PUT /api/libros/{id}.
 * Según el documento: título, autor, ISBN y género son obligatorios.
 */
public record RegistroLibroDTO(
        @NotBlank @Size(max = 200) String titulo,
        @NotBlank @Size(max = 20) String isbn,
        @Size(max = 120) String editorial,
        @JsonProperty("anio_publicacion") Integer anioPublicacion,
        @JsonProperty("num_paginas") @Min(1) Integer numPaginas,
        /** ids de autores (tabla libros_autores). Al menos uno. */
        @JsonProperty("ids_autores") @NotEmpty(message = "Debe indicar al menos un autor") List<Long> idsAutores,
        /** ids de géneros (tabla libros_generos). Al menos uno. */
        @JsonProperty("ids_generos") @NotEmpty(message = "Debe indicar al menos un género") List<Long> idsGeneros
) {}
