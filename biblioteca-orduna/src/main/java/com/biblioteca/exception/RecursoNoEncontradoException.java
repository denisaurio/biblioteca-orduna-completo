package com.biblioteca.exception;

/** EXTRA: el id enviado (libro, usuario, préstamo, multa...) no existe. HTTP 404 (Not Found). */
public class RecursoNoEncontradoException extends NegocioException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
