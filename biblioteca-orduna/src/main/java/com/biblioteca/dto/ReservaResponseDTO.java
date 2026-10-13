package com.biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * estado: PENDIENTE ("En espera"), NOTIFICADA (ejemplar apartado, recoger hasta fecha_limite_retiro),
 * ATENDIDA, CANCELADA o VENCIDA.
 */
public record ReservaResponseDTO(
        Long id,
        @JsonProperty("id_libro") Long idLibro,
        @JsonProperty("titulo_libro") String tituloLibro,
        @JsonProperty("id_usuario_lector") Long idUsuarioLector,
        @JsonProperty("nombre_lector") String nombreLector,
        @JsonProperty("fecha_reserva") LocalDateTime fechaReserva,
        String estado,
        /** Posición en la cola FIFO (1 = el siguiente). 0 si ya no está en espera. */
        @JsonProperty("posicion_cola") long posicionCola,
        @JsonProperty("codigo_ejemplar_asignado") String codigoEjemplarAsignado,
        @JsonProperty("fecha_limite_retiro") LocalDate fechaLimiteRetiro
) {}
