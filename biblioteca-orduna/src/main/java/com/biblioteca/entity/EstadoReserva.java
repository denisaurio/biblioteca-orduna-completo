package com.biblioteca.entity;

/**
 * PENDIENTE: en cola FIFO ("En espera"). NOTIFICADA: hay un ejemplar apartado para él.
 * ATENDIDA: se convirtió en préstamo. CANCELADA: la anuló el lector o el bibliotecario.
 * VENCIDA: no recogió el ejemplar dentro del plazo de retiro.
 */
public enum EstadoReserva { PENDIENTE, NOTIFICADA, ATENDIDA, CANCELADA, VENCIDA }
