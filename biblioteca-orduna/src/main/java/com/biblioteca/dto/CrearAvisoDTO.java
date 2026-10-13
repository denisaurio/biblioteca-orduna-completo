package com.biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * RF11 - "+ Crear aviso" (bibliotecario/gerente). Body de POST /api/notificaciones.
 * id_usuario: destinatario. Si es null, el aviso se envía a todos los lectores activos.
 */
public record CrearAvisoDTO(
        @JsonProperty("id_usuario") Long idUsuario,
        @NotBlank @Size(max = 500) String mensaje
) {}
