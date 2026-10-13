package com.biblioteca.exception;

/** El usuario tiene una sanción activa (RF17). HTTP 403 (Forbidden). */
public class UsuarioSancionadoException extends NegocioException {
    public UsuarioSancionadoException(String mensaje) {
        super(mensaje);
    }
}
