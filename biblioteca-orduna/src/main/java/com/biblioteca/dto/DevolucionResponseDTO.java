package com.biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Si no hubo retraso, id_multa y monto_multa vienen en null. */
public record DevolucionResponseDTO(
        Long id,
        @JsonProperty("id_prestamo") Long idPrestamo,
        @JsonProperty("fecha_devolucion") LocalDateTime fechaDevolucion,
        @JsonProperty("dias_retraso") int diasRetraso,
        String observaciones,
        @JsonProperty("id_multa") Long idMulta,
        @JsonProperty("monto_multa") BigDecimal montoMulta,
        /** Si el ejemplar quedó apartado para la siguiente reserva de la cola. */
        @JsonProperty("id_reserva_notificada") Long idReservaNotificada
) {}
