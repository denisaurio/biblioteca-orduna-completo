// Portal del lector: Inicio reducido, "Mis préstamos y multas", "Mis reservas".
import axiosClient from './axiosClient.js';

export const getPanel = () => axiosClient.get('/mi/panel');
export const getMisPrestamos = () => axiosClient.get('/mi/prestamos');
export const getMisMultas = () => axiosClient.get('/mi/multas');
export const getMisReservas = () => axiosClient.get('/mi/reservas');
export const getMisSanciones = () => axiosClient.get('/mi/sanciones');
export const getDashboard = () => axiosClient.get('/dashboard');
