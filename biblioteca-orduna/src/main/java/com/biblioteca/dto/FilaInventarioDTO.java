package com.biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FilaInventarioDTO(
        @JsonProperty("id_libro") Long idLibro,
        String titulo,
        String isbn,
        String autores,
        String generos,
        long total,
        long disponibles,
        long prestados,
        long reservados,
        long danados,
        long perdidos,
        @JsonProperty("de_baja") long deBaja
) {}
