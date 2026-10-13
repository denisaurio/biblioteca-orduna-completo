package com.biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

/** INICIO del lector (versión reducida) e interfaz "Mis préstamos y multas". */
public record PanelLectorDTO(
        String estado,
        @JsonProperty("prestamos_activos") List<PrestamoResponseDTO> prestamosActivos,
        @JsonProperty("reservas_activas") List<ReservaResponseDTO> reservasActivas,
        @JsonProperty("deuda_multas") BigDecimal deudaMultas,
        @JsonProperty("notificaciones_no_leidas") long notificacionesNoLeidas,
        @JsonProperty("libros_destacados") List<LibroResponseDTO> librosDestacados
) {}
