package com.biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/** Panel lateral "Cola de reservas" de un libro, en orden de atención. */
public record ColaReservasDTO(
        @JsonProperty("id_libro") Long idLibro,
        @JsonProperty("titulo_libro") String tituloLibro,
        List<ReservaResponseDTO> cola
) {}
