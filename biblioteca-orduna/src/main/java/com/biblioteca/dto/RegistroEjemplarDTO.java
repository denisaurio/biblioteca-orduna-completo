package com.biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** RF14 - Body de POST /api/libros/{idLibro}/ejemplares */
public record RegistroEjemplarDTO(
        @JsonProperty("codigo_inventario") @NotBlank @Size(max = 50) String codigoInventario,
        @Size(max = 100) String ubicacion
) {}
