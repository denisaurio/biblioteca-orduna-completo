package com.biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

/** RF09 - Body de POST /api/reservas (el lector sale del JWT). */
public record NuevaReservaDTO(
        @JsonProperty("id_libro") @NotNull Long idLibro
) {}
