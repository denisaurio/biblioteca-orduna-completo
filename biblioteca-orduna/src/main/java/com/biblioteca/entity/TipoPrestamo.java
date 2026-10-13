package com.biblioteca.entity;

/**
 * Regla de negocio: un préstamo puede ser con fecha fija de devolución o abierto (sin fecha).
 * Un préstamo ABIERTO nunca genera multa por retraso.
 */
public enum TipoPrestamo { FECHA_FIJA, ABIERTO }
