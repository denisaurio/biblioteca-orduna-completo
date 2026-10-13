// RF11 - Tabla notificaciones.
import axiosClient from './axiosClient.js';

export const getMisNotificaciones = (soloNoLeidas = false) =>
  axiosClient.get('/notificaciones', { params: { no_leidas: soloNoLeidas } });
export const contarNoLeidas = () => axiosClient.get('/notificaciones/no-leidas/total');
export const marcarLeida = (id) => axiosClient.patch(`/notificaciones/${id}/leida`);
export const marcarTodasLeidas = () => axiosClient.patch('/notificaciones/leidas');
/** datos: { id_usuario (null = todos los lectores), mensaje } */
export const crearAviso = (datos) => axiosClient.post('/notificaciones', datos);
export const getTodosLosAvisos = (params) => axiosClient.get('/notificaciones/todas', { params });
export const generarAvisos = () => axiosClient.post('/notificaciones/generar');
