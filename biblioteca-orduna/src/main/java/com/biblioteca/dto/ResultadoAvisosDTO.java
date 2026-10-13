package com.biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Resumen de la generación automática de avisos (RF11). */
public record ResultadoAvisosDTO(
        @JsonProperty("vencimientos_proximos") int vencimientosProximos,
        @JsonProperty("prestamos_vencidos") int prestamosVencidos,
        @JsonProperty("multas_pendientes") int multasPendientes,
        int total
) {}
