package com.biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record NotificacionResponseDTO(
        Long id,
        String tipo,
        String mensaje,
        @JsonProperty("id_referencia") Long idReferencia,
        @JsonProperty("fecha_creacion") LocalDateTime fechaCreacion,
        boolean leida,
        /** Destinatario (solo se llena en la vista del personal). */
        @JsonProperty("nombre_destinatario") String nombreDestinatario,
        /** null = generado por el sistema. */
        @JsonProperty("creado_por") String creadoPor
) {}
