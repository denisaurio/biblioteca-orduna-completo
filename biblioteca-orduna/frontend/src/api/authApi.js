// RF05 - Inicio de sesión (tabla usuarios). Devuelve { token, rol, id_usuario, nombre, email, ... }
import axiosClient from './axiosClient.js';

export const login = (email, password) => axiosClient.post('/auth/login', { email, password });

/** RF04 - Auto-registro de lector (público). */
export const registrarse = (datos) => axiosClient.post('/usuarios/registro', datos);
