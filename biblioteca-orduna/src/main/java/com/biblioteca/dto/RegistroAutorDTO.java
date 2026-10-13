package com.biblioteca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** RF03 - Body de POST /api/autores y PUT /api/autores/{id} */
public record RegistroAutorDTO(
        @NotBlank @Size(max = 150) String nombre,
        @Size(max = 80) String nacionalidad,
        String biografia
) {}
