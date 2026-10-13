package com.biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/** INICIO del bibliotecario/gerente: tarjetas KPI + libros destacados. */
public record DashboardDTO(
        @JsonProperty("total_libros") long totalLibros,
        @JsonProperty("total_ejemplares") long totalEjemplares,
        @JsonProperty("ejemplares_disponibles") long ejemplaresDisponibles,
        @JsonProperty("prestamos_activos") long prestamosActivos,
        @JsonProperty("prestamos_vencidos") long prestamosVencidos,
        @JsonProperty("reservas_en_espera") long reservasEnEspera,
        @JsonProperty("usuarios_registrados") long usuariosRegistrados,
        @JsonProperty("multas_pendientes") long multasPendientes,
        @JsonProperty("libros_destacados") List<LibroResponseDTO> librosDestacados
) {}
