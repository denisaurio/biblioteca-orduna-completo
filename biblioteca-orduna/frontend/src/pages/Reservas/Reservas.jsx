// Réplica del prototipo Figma "RESERVAS" (RF09): tabla de reservas + panel lateral "Cola de reservas"
// (FIFO por libro). Al devolverse un ejemplar, el backend lo aparta para el primero de la cola.
import { useEffect, useState } from 'react';
import { getReservas, getColaLibro, cancelarReserva } from '../../api/reservasApi.js';
import { useApi, useAccion } from '../../hooks.js';
import SearchInput from '../../components/common/SearchInput.jsx';
import Estado from '../../components/common/Estado.jsx';
import { FilaEstado, Cargando, Vacio } from '../../components/common/Cargando.jsx';
import { estadoReserva } from '../../utils/estados.js';
import { codigo, fmtFecha } from '../../utils/formato.js';
import '../Prestamos/Prestamos.css';

const ACTIVAS = ['PENDIENTE', 'NOTIFICADA'];

function Reservas() {
  const [busqueda, setBusqueda] = useState('');
  const [vista, setVista] = useState('activas');
  const [libroCola, setLibroCola] = useState(null);
  const { datos: reservas, cargando, recargar } = useApi(() => getReservas(), [], []);
  const { ejecutar } = useAccion();

  const filtradas = reservas
    .filter((r) => vista === 'todas' || (vista === 'activas' ? ACTIVAS.includes(r.estado) : !ACTIVAS.includes(r.estado)))
    .filter((r) => `${r.titulo_libro} ${r.nombre_lector}`.toLowerCase().includes(busqueda.toLowerCase()));

  // Panel lateral: por defecto la cola del primer libro con reservas activas.
  useEffect(() => {
    if (!libroCola) {
      const primera = reservas.find((r) => ACTIVAS.includes(r.estado));
      if (primera) setLibroCola(primera.id_libro);
    }
  }, [reservas, libroCola]);

  const cancelar = async (r) => {
    if (!window.confirm(`¿Cancelar la reserva de ${r.nombre_lector}?`)) return;
    const ok = await ejecutar(() => cancelarReserva(r.id), 'Reserva cancelada');
    if (ok) recargar();
  };

  return (
    <div>
      <div className="page-head">
        <div><h1>Reservas</h1><p>Reservas en línea (RF09) — se atienden por orden de llegada (FIFO).</p></div>
      </div>

      <div className="wizard-grid" style={{ gridTemplateColumns: '1.7fr 1fr' }}>
        <div>
          <div className="toolbar">
            <SearchInput placeholder="Buscar reserva por libro o usuario..." value={busqueda} onChange={(e) => setBusqueda(e.target.value)} />
            <div className="tabs">
              {[['activas', 'Activas'], ['cerradas', 'Cerradas'], ['todas', 'Todas']].map(([v, t]) => (
                <button key={v} className={vista === v ? 'active' : ''} onClick={() => setVista(v)}>{t}</button>
              ))}
            </div>
          </div>
          <div className="card table-card">
            <table>
              <thead><tr><th>Identificación</th><th>Libro</th><th>Usuario</th><th>Fecha</th><th>Estado</th><th></th></tr></thead>
              <tbody>
                {(cargando || filtradas.length === 0) && <FilaEstado columnas={6} cargando={cargando} vacio="No hay reservas en esta vista." />}
                {!cargando && filtradas.map((r) => (
                  <tr key={r.id} onClick={() => setLibroCola(r.id_libro)} style={{ cursor: 'pointer' }}
                      className={libroCola === r.id_libro ? 'fila-sel' : ''}>
                    <td className="mono">{codigo('R', r.id)}</td>
                    <td>{r.titulo_libro}</td>
                    <td>{r.nombre_lector}</td>
                    <td className="mono">{fmtFecha(r.fecha_reserva)}</td>
                    <td>
                      <Estado valor={r.estado} mapa={estadoReserva} />
                      {r.estado === 'NOTIFICADA' && <div className="pick-sub">Hasta {fmtFecha(r.fecha_limite_retiro)} · {r.codigo_ejemplar_asignado}</div>}
                    </td>
                    <td onClick={(e) => e.stopPropagation()}>
                      {ACTIVAS.includes(r.estado) && <button className="btn-link" onClick={() => cancelar(r)}>Cancelar</button>}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>

        <ColaReservas idLibro={libroCola} version={reservas} />
      </div>
    </div>
  );
}

function ColaReservas({ idLibro, version }) {
  const { datos: cola, cargando } = useApi(() => (idLibro ? getColaLibro(idLibro) : Promise.resolve({ data: null })), [idLibro, version]);

  return (
    <div className="card" style={{ padding: 18 }}>
      <h3 className="seccion-titulo">Cola de reservas</h3>
      {!idLibro ? <Vacio texto="Selecciona una reserva para ver la cola del libro." /> : cargando ? <Cargando /> : cola && (
        <>
          <p className="pick-sub" style={{ marginTop: -6, marginBottom: 12 }}>{cola.titulo_libro}</p>
          {cola.cola.length === 0 ? <Vacio texto="Nadie en espera para este libro." /> : cola.cola.map((r, i) => (
            <div key={r.id} className="cola-item">
              <span className="cola-num">{i + 1}</span>
              <div style={{ flex: 1 }}>
                <div className="pick-title">{r.nombre_lector}</div>
                <div className="pick-sub">
                  {r.estado === 'NOTIFICADA' ? `Por recoger hasta ${fmtFecha(r.fecha_limite_retiro)}` : `En espera desde ${fmtFecha(r.fecha_reserva)}`}
                </div>
              </div>
              <Estado valor={r.estado} mapa={estadoReserva} />
            </div>
          ))}
        </>
      )}
    </div>
  );
}

export default Reservas;
