package com.biblioteca.dto;

import jakarta.validation.constraints.Size;

/** RF16 - Body de POST /api/turnos/cerrar */
public record CierreTurnoDTO(
        @Size(max = 500) String observaciones
) {}
