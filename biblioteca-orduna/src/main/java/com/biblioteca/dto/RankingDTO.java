package com.biblioteca.dto;

/** Fila de un "top": libro más prestado, lector más activo, etc. */
public record RankingDTO(String nombre, long cantidad) {}
