package com.biblioteca.dto;

import jakarta.validation.constraints.Size;

/** RF12 - Body de POST /api/cierre-diario (opcional). */
public record CierreDiarioDTO(@Size(max = 500) String observaciones) {}
