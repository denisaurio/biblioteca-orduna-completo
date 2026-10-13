package com.biblioteca.exception;

/** ISBN, DNI, email o código de inventario ya existen. HTTP 409 (Conflict). */
public class RecursoDuplicadoException extends NegocioException {
    public RecursoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
