// RF16 - Tabla bitacora_turnos.
import axiosClient from './axiosClient.js';

/** params: { desde, hasta, id_bibliotecario, tipo } */
export const getTurnos = (params) => axiosClient.get('/turnos', { params });
export const getTurnoActual = () => axiosClient.get('/turnos/actual');
export const abrirTurno = () => axiosClient.post('/turnos/abrir');
export const cerrarTurno = (observaciones) => axiosClient.post('/turnos/cerrar', { observaciones });
