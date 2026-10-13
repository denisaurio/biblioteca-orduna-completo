package com.biblioteca.exception;

/** El usuario intenta ver o modificar algo que no le pertenece (ej. el perfil de otro). HTTP 403. */
public class AccesoDenegadoException extends NegocioException {
    public AccesoDenegadoException(String mensaje) {
        super(mensaje);
    }
}
