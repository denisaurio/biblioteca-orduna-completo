// RF13 - Tablas configuracion y configuracion_multas.
import axiosClient from './axiosClient.js';

export const getConfiguracion = () => axiosClient.get('/configuracion');
/** Solo se envían los campos a cambiar. */
export const actualizarConfiguracion = (datos) => axiosClient.put('/configuracion', datos);
export const getHistorialConfiguracion = () => axiosClient.get('/configuracion/historial');
