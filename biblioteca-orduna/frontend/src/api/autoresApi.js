// RF03 - Tabla autores.
import axiosClient from './axiosClient.js';

export const getAutores = () => axiosClient.get('/autores');
export const getLibrosDeAutor = (id) => axiosClient.get(`/autores/${id}/libros`);
/** datos: { nombre, nacionalidad, biografia } */
export const crearAutor = (datos) => axiosClient.post('/autores', datos);
export const actualizarAutor = (id, datos) => axiosClient.put(`/autores/${id}`, datos);
export const eliminarAutor = (id) => axiosClient.delete(`/autores/${id}`);
