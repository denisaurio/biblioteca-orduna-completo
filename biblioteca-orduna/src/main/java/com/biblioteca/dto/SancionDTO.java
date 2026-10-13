package com.biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/** RF17 - Body de POST /api/usuarios/{idUsuario}/suspender. fecha_fin en formato "2026-10-15" */
public record SancionDTO(
        @NotBlank @Size(max = 300) String motivo,
        @JsonProperty("fecha_fin") @NotNull @FutureOrPresent LocalDate fechaFin
) {}
