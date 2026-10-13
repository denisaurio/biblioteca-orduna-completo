package com.biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Historial de configuracion_multas (GET /api/configuracion/historial). */
public record VigenciaMultaDTO(
        Long id,
        @JsonProperty("tarifa_multa_diaria") BigDecimal tarifaMultaDiaria,
        @JsonProperty("dias_max") Integer diasMax,
        @JsonProperty("dias_retiro_reserva") Integer diasRetiroReserva,
        @JsonProperty("fecha_vigencia_desde") LocalDateTime fechaVigenciaDesde,
        @JsonProperty("fecha_vigencia_hasta") LocalDateTime fechaVigenciaHasta,
        @JsonProperty("modificado_por") String modificadoPor
) {}
