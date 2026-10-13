package com.biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * RF05 - Respuesta del login. El frontend guarda "token" y lo envía en cada petición:
 * Authorization: Bearer &lt;token&gt;
 */
public record LoginResponseDTO(
        String token,
        String tipo,
        @JsonProperty("expira_en_ms") long expiraEnMs,
        @JsonProperty("id_usuario") Long idUsuario,
        String nombre,
        String email,
        String rol
) {}
