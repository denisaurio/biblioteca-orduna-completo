package com.biblioteca.dto;

import com.biblioteca.entity.MetodoPago;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/** RF15 - Body de POST /api/multas/{idMulta}/pagar. metodo_pago: EFECTIVO, TARJETA, YAPE, PLIN o TRANSFERENCIA */
public record PagoMultaDTO(
        @JsonProperty("monto_pagado") @NotNull @DecimalMin("0.01") BigDecimal montoPagado,
        @JsonProperty("metodo_pago") @NotNull MetodoPago metodoPago
) {}
