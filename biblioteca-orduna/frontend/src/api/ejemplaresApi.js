// RF14 - Tabla ejemplares.
import axiosClient from './axiosClient.js';

/** params: { id_libro, estado, q } */
export const getEjemplares = (params) => axiosClient.get('/ejemplares', { params });
/** datos: { codigo_inventario, ubicacion } */
export const crearEjemplar = (idLibro, datos) => axiosClient.post(`/libros/${idLibro}/ejemplares`, datos);
/** datos: { ubicacion, estado } */
export const actualizarEjemplar = (id, datos) => axiosClient.put(`/ejemplares/${id}`, datos);
