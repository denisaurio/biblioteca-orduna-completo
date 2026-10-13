package com.biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * RF13 - Body de PUT /api/configuracion y respuesta de GET /api/configuracion.
 * En el PUT todos los campos son opcionales: lo que no se envía (null) se mantiene.
 * Horas en formato "08:00".
 */
public record ConfiguracionDTO(
        // ---- Pestaña "General" ----
        @JsonProperty("nombre_biblioteca") @Size(max = 150) String nombreBiblioteca,
        @JsonProperty("correo_contacto") @Email @Size(max = 150) String correoContacto,
        @JsonProperty("telefono_contacto") @Size(max = 20) String telefonoContacto,
        @JsonProperty("hora_apertura") LocalTime horaApertura,
        @JsonProperty("hora_cierre") LocalTime horaCierre,
        // ---- Pestaña "Multas y Reservas" (versionada) ----
        @JsonProperty("tarifa_multa_diaria") @DecimalMin("0.00") BigDecimal tarifaMultaDiaria,
        @JsonProperty("dias_max") @Min(1) Integer diasMax,
        @JsonProperty("dias_retiro_reserva") @Min(1) Integer diasRetiroReserva,
        // ---- Configuración de avisos (RF11) ----
        @JsonProperty("aviso_vencimiento") Boolean avisoVencimiento,
        @JsonProperty("aviso_reserva") Boolean avisoReserva,
        @JsonProperty("aviso_multa") Boolean avisoMulta,
        @JsonProperty("aviso_nuevo_libro") Boolean avisoNuevoLibro
) {}
