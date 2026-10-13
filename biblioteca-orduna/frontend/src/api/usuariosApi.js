// RF04 - Tabla usuarios.
import axiosClient from './axiosClient.js';

/** params: { q, rol, pagina, tamanio } -> { contenido, pagina, tamanio, total_elementos, total_paginas } */
export const getUsuarios = (params) => axiosClient.get('/usuarios', { params });
export const getUsuario = (id) => axiosClient.get(`/usuarios/${id}`);
export const crearUsuario = (datos) => axiosClient.post('/usuarios', datos);
export const actualizarUsuario = (id, datos) => axiosClient.put(`/usuarios/${id}`, datos);
export const getMiPerfil = () => axiosClient.get('/usuarios/me');
export const actualizarMiPerfil = (datos) => axiosClient.put('/usuarios/me', datos);
