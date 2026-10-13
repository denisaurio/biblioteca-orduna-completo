package com.biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/** Respuesta paginada (pantalla Usuarios, avisos del personal). pagina empieza en 0. */
public record PaginaDTO<T>(
        List<T> contenido,
        int pagina,
        int tamanio,
        @JsonProperty("total_elementos") long totalElementos,
        @JsonProperty("total_paginas") int totalPaginas
) {}
