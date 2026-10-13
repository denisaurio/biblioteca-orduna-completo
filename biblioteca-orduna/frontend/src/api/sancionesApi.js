// RF17 - Tabla sanciones.
import axiosClient from './axiosClient.js';

export const getSanciones = (activas = false) => axiosClient.get('/sanciones', { params: { activas } });
/** datos: { motivo, fecha_fin: 'yyyy-mm-dd' } */
export const aplicarSancion = (idUsuario, datos) => axiosClient.post(`/usuarios/${idUsuario}/suspender`, datos);
export const levantarSancion = (id) => axiosClient.patch(`/sanciones/${id}/levantar`);
export const getSancionesDeUsuario = (idUsuario) => axiosClient.get(`/usuarios/${idUsuario}/sanciones`);
