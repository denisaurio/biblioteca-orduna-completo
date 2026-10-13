package com.biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EjemplarResponseDTO(
        Long id,
        @JsonProperty("id_libro") Long idLibro,
        @JsonProperty("titulo_libro") String tituloLibro,
        @JsonProperty("codigo_inventario") String codigoInventario,
        String ubicacion,
        String estado
) {}
