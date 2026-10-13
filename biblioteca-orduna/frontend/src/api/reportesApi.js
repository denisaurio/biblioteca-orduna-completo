// RF10 - Reportes y RF12 - Cierre diario.
import axiosClient from './axiosClient.js';

export const getReporte = (tipo, params) => axiosClient.get(`/reportes/${tipo}`, { params });

/** Descarga el CSV (tipo: inventario | prestamos | multas) usando el nombre que manda el backend. */
export async function descargarCsv(tipo, params) {
  const res = await axiosClient.get(`/reportes/${tipo}/csv`, { params, responseType: 'blob' });
  const dispo = res.headers['content-disposition'] || '';
  const nombre = (dispo.match(/filename="?([^";]+)"?/) || [])[1] || `reporte_${tipo}.csv`;
  const url = URL.createObjectURL(res.data);
  const a = document.createElement('a');
  a.href = url;
  a.download = nombre;
  document.body.appendChild(a);
  a.click();
  a.remove();
  URL.revokeObjectURL(url);
}

export const generarCierre = (observaciones) => axiosClient.post('/cierre-diario', { observaciones });
export const getCierres = (desde, hasta) => axiosClient.get('/cierre-diario', { params: { desde, hasta } });
