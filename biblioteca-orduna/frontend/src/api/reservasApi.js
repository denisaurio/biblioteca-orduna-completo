// RF09 - Tabla reservas (cola FIFO).
import axiosClient from './axiosClient.js';

export const crearReserva = (idLibro) => axiosClient.post('/reservas', { id_libro: idLibro });
/** estado: PENDIENTE | NOTIFICADA | ATENDIDA | CANCELADA | VENCIDA | (vacío = todas) */
export const getReservas = (estado) => axiosClient.get('/reservas', { params: { estado: estado || undefined } });
export const getColaLibro = (idLibro) => axiosClient.get(`/reservas/cola/${idLibro}`);
export const cancelarReserva = (id) => axiosClient.patch(`/reservas/${id}/cancelar`);
