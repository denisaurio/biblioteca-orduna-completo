package com.biblioteca.exception;

/** RF06: el ejemplar no está disponible para préstamo. HTTP 400. */
public class EjemplarNoDisponibleException extends NegocioException {
    public EjemplarNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
