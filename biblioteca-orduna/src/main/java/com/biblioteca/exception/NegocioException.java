package com.biblioteca.exception;

/** Base de todas las excepciones de negocio que lanzan los servicios. */
public abstract class NegocioException extends RuntimeException {
    protected NegocioException(String mensaje) {
        super(mensaje);
    }
}
