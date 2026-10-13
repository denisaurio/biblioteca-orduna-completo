package com.biblioteca.exception;

/** RF05: email o contraseña incorrectos, o cuenta desactivada. HTTP 401 (Unauthorized). */
public class CredencialesInvalidasException extends NegocioException {
    public CredencialesInvalidasException(String mensaje) {
        super(mensaje);
    }
}
