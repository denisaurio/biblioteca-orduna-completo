package com.biblioteca.exception;

/** EXTRA: regla de negocio no cumplida (préstamo ya devuelto, multa ya pagada, monto incorrecto, cerrar turno sin abrir, etc.). HTTP 400. */
public class OperacionInvalidaException extends NegocioException {
    public OperacionInvalidaException(String mensaje) {
        super(mensaje);
    }
}
