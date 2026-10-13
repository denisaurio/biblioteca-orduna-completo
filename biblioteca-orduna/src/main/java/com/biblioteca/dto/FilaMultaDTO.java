package com.biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FilaMultaDTO(
        Long id,
        @JsonProperty("fecha_generacion") LocalDateTime fechaGeneracion,
        String lector,
        String dni,
        @JsonProperty("titulo_libro") String tituloLibro,
        @JsonProperty("dias_retraso") int diasRetraso,
        BigDecimal monto,
        @JsonProperty("monto_pagado") BigDecimal montoPagado,
        String estado,
        @JsonProperty("fecha_ultimo_pago") LocalDateTime fechaUltimoPago,
        @JsonProperty("metodos_pago") String metodosPago
) {}
