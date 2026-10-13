package com.biblioteca.dto;

import com.biblioteca.entity.TipoUsuario;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * RF04 - Body de POST /api/usuarios/registro (público) y POST /api/usuarios (bibliotecario).
 * tipo_usuario: ESTUDIANTE (por defecto), DOCENTE o EXTERNO.
 */
public record RegistroUsuarioDTO(
        @NotBlank @Size(max = 100) String nombre,
        @NotBlank @Size(max = 100) String apellido,
        @NotBlank @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos") String dni,
        @NotBlank @Email @Size(max = 150) String email,
        @NotBlank @Size(min = 6, max = 72) String password,
        @Size(max = 20) String telefono,
        @Size(max = 200) String direccion,
        @JsonProperty("tipo_usuario") TipoUsuario tipoUsuario
) {}
