package com.biblioteca.dto;

import com.biblioteca.entity.TipoPrestamo;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

/**
 * RF06 - Body de POST /api/prestamos (paso "Confirmar" del asistente).
 * Enviar id_ejemplar O id_libro: con id_libro el sistema elige el ejemplar disponible.
 * tipo_prestamo: FECHA_FIJA (por defecto) o ABIERTO (sin fecha, nunca genera multa).
 */
public record NuevoPrestamoDTO(
        @JsonProperty("id_ejemplar") Long idEjemplar,
        @JsonProperty("id_libro") Long idLibro,
        @JsonProperty("id_usuario_lector") @NotNull Long idUsuarioLector,
        @JsonProperty("tipo_prestamo") TipoPrestamo tipoPrestamo
) {}
