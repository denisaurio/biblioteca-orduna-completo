package com.biblioteca.exception;

/** RF16: el bibliotecario tiene un turno abierto sin cerrar. HTTP 400. */
public class TurnoPendienteException extends NegocioException {
    public TurnoPendienteException(String mensaje) {
        super(mensaje);
    }
}
