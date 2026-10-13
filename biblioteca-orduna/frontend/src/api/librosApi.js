// RF01, RF02 - Tabla libros + libros_autores + libros_generos.
import axiosClient from './axiosClient.js';

export const getLibros = () => axiosClient.get('/libros');
export const buscarLibros = (q) => axiosClient.get('/libros/buscar', { params: { q } });
export const getLibroPorId = (id) => axiosClient.get(`/libros/${id}`);
export const getDestacados = () => axiosClient.get('/libros/destacados');
/** datos: { titulo, isbn, editorial, anio_publicacion, num_paginas, ids_autores: [], ids_generos: [] } */
export const crearLibro = (datos) => axiosClient.post('/libros', datos);
export const actualizarLibro = (id, datos) => axiosClient.put(`/libros/${id}`, datos);

export const getGeneros = () => axiosClient.get('/generos');
export const crearGenero = (nombre) => axiosClient.post('/generos', { nombre });
