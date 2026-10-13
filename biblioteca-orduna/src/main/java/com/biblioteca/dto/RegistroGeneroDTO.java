package com.biblioteca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Body de POST /api/generos */
public record RegistroGeneroDTO(@NotBlank @Size(max = 80) String nombre) {}
