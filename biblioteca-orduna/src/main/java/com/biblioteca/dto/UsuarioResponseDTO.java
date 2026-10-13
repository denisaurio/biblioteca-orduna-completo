package com.biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/** Nunca incluye la contraseña. estado: ACTIVO, INACTIVO o SUSPENDIDO (calculado). */
public record UsuarioResponseDTO(
        Long id,
        String nombre,
        String apellido,
        String dni,
        String email,
        String telefono,
        String direccion,
        @JsonProperty("tipo_usuario") String tipoUsuario,
        String rol,
        String estado,
        @JsonProperty("prestamos_activos") long prestamosActivos,
        @JsonProperty("fecha_registro") LocalDateTime fechaRegistro
) {}
