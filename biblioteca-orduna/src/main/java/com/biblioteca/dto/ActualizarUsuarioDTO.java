package com.biblioteca.dto;

import com.biblioteca.entity.TipoUsuario;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Body de PUT /api/usuarios/me (el propio usuario) y PUT /api/usuarios/{id} (personal).
 * "activo" solo lo puede cambiar el personal; si un lector lo envía, se ignora.
 * "password" es opcional: si viene, se cambia la contraseña.
 */
public record ActualizarUsuarioDTO(
        @NotBlank @Size(max = 100) String nombre,
        @NotBlank @Size(max = 100) String apellido,
        @Size(max = 20) String telefono,
        @Size(max = 200) String direccion,
        @JsonProperty("tipo_usuario") TipoUsuario tipoUsuario,
        Boolean activo,
        @Size(min = 6, max = 72) String password
) {}
