package com.biblioteca.exception;

/** RF09: el usuario ya está en la cola FIFO de ese libro. HTTP 400. */
public class ReservaActivaDuplicadaException extends NegocioException {
    public ReservaActivaDuplicadaException(String mensaje) {
        super(mensaje);
    }
}
