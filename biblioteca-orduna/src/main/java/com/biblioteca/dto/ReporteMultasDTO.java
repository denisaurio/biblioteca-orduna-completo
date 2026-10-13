package com.biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/** RF10 - Reporte de multas generadas en un rango de fechas. */
public record ReporteMultasDTO(
        LocalDate desde,
        LocalDate hasta,
        @JsonProperty("total_multas") long totalMultas,
        long pagadas,
        long parciales,
        long pendientes,
        @JsonProperty("monto_total") BigDecimal montoTotal,
        @JsonProperty("monto_cobrado") BigDecimal montoCobrado,
        @JsonProperty("monto_pendiente") BigDecimal montoPendiente,
        @JsonProperty("cobrado_por_metodo") Map<String, BigDecimal> cobradoPorMetodo,
        List<FilaMultaDTO> detalle
) {}
