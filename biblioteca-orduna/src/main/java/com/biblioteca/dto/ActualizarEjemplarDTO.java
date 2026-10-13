package com.biblioteca.dto;

import com.biblioteca.entity.EstadoEjemplar;
import jakarta.validation.constraints.Size;

/**
 * RF14 - Body de PUT /api/ejemplares/{id}. Ambos campos opcionales.
 * estado permitido: DISPONIBLE, DANADO, PERDIDO o BAJA (PRESTADO y RESERVADO los pone el sistema).
 */
public record ActualizarEjemplarDTO(
        @Size(max = 100) String ubicacion,
        EstadoEjemplar estado
) {}
