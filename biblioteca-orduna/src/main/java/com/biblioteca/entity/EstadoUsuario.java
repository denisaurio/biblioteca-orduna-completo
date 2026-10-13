package com.biblioteca.entity;

/**
 * Estado que se muestra en pantalla. No se guarda: se calcula.
 * INACTIVO si la cuenta está desactivada; SUSPENDIDO si tiene una sanción activa; si no, ACTIVO.
 */
public enum EstadoUsuario { ACTIVO, INACTIVO, SUSPENDIDO }
