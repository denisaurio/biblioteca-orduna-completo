package com.biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CierreDiarioResponseDTO(
        Long id,
        LocalDate fecha,
        @JsonProperty("prestamos_del_dia") long prestamosDelDia,
        @JsonProperty("devoluciones_del_dia") long devolucionesDelDia,
        @JsonProperty("multas_generadas") long multasGeneradas,
        @JsonProperty("monto_multas_generadas") BigDecimal montoMultasGeneradas,
        @JsonProperty("monto_recaudado") BigDecimal montoRecaudado,
        @JsonProperty("prestamos_pendientes") long prestamosPendientes,
        @JsonProperty("prestamos_vencidos") long prestamosVencidos,
        @JsonProperty("reservas_en_espera") long reservasEnEspera,
        @JsonProperty("fecha_generacion") LocalDateTime fechaGeneracion,
        /** null = cierre automático */
        @JsonProperty("generado_por") String generadoPor,
        String observaciones
) {}
