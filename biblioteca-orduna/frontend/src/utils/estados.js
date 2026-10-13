// Texto y color de cada estado que devuelve el backend, para usar con <Badge color=...>.

export const estadoLibro = {
  DISPONIBLE: ['green', 'Disponible'],
  PRESTADO: ['red', 'Préstamo'],
  SIN_EJEMPLARES: ['amber', 'Sin ejemplares'],
};

export const estadoEjemplar = {
  DISPONIBLE: ['green', 'Disponible'],
  PRESTADO: ['red', 'Prestado'],
  RESERVADO: ['amber', 'Reservado'],
  DANADO: ['amber', 'Dañado'],
  PERDIDO: ['red', 'Perdido'],
  BAJA: ['blue', 'De baja'],
};

export const estadoUsuario = {
  ACTIVO: ['green', 'Activo'],
  INACTIVO: ['amber', 'Inactivo'],
  SUSPENDIDO: ['red', 'Suspendido'],
};

export const estadoPrestamo = {
  ACTIVO: ['blue', 'En plazo'],
  VENCIDO: ['red', 'Retraso'],
  DEVUELTO: ['green', 'Devuelto'],
};

export const estadoReserva = {
  PENDIENTE: ['amber', 'En espera'],
  NOTIFICADA: ['blue', 'Por recoger'],
  ATENDIDA: ['green', 'Atendida'],
  CANCELADA: ['red', 'Cancelada'],
  VENCIDA: ['red', 'Vencida'],
};

export const estadoMulta = {
  PENDIENTE: ['red', 'Pendiente'],
  PARCIAL: ['amber', 'Pago parcial'],
  PAGADA: ['green', 'Pagado'],
};

export const estadoSancion = {
  ACTIVA: ['red', 'Activa'],
  FINALIZADA: ['green', 'Finalizada'],
};

export const tipoAviso = {
  VENCIMIENTO_PROXIMO: ['⏰', 'Vencimiento de préstamo'],
  PRESTAMO_VENCIDO: ['⚠️', 'Préstamo vencido'],
  RESERVA_DISPONIBLE: ['📘', 'Reserva disponible'],
  RESERVA_VENCIDA: ['⌛', 'Reserva vencida'],
  MULTA_PENDIENTE: ['💰', 'Multa generada'],
  NUEVO_LIBRO: ['✨', 'Nuevo libro en catálogo'],
  ADMINISTRATIVO: ['📢', 'Aviso de la biblioteca'],
};

export const metodosPago = ['EFECTIVO', 'TARJETA', 'TRANSFERENCIA', 'YAPE', 'PLIN', 'OTRO'];
export const nombreMetodo = {
  EFECTIVO: 'Efectivo', TARJETA: 'Tarjeta', TRANSFERENCIA: 'Transferencia', YAPE: 'Yape', PLIN: 'Plin', OTRO: 'Otro',
};
