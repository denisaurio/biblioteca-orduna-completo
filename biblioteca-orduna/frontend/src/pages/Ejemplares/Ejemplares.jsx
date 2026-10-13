// Réplica del prototipo Figma "EJEMPLARES" (RF14): identificación, libro, estado, ubicación,
// "Ver detalle" y "+ Registrar ejemplar". PRESTADO y RESERVADO los pone el sistema;
// a mano solo: Disponible, Dañado, Perdido o De baja.
import { useState } from 'react';
import { getEjemplares, crearEjemplar, actualizarEjemplar } from '../../api/ejemplaresApi.js';
import { getLibros } from '../../api/librosApi.js';
import { useApi, useAccion, useDebounce } from '../../hooks.js';
import SearchInput from '../../components/common/SearchInput.jsx';
import Estado from '../../components/common/Estado.jsx';
import Modal from '../../components/common/Modal.jsx';
import Campo from '../../components/common/Campo.jsx';
import { FilaEstado } from '../../components/common/Cargando.jsx';
import { estadoEjemplar } from '../../utils/estados.js';
import { codigo } from '../../utils/formato.js';

function Ejemplares() {
  const [busqueda, setBusqueda] = useState('');
  const [estado, setEstado] = useState('');
  const [nuevo, setNuevo] = useState(false);
  const [detalle, setDetalle] = useState(null);
  const q = useDebounce(busqueda);
  const { datos: ejemplares, cargando, recargar } = useApi(() => getEjemplares({ q, estado: estado || undefined }), [q, estado], []);

  return (
    <div>
      <div className="page-head">
        <div><h1>Ejemplares</h1><p>Copias físicas de cada libro (RF14).</p></div>
        <button className="btn-primary" onClick={() => setNuevo(true)}>+ Registrar ejemplar</button>
      </div>

      <div className="toolbar">
        <SearchInput placeholder="Buscar por código, ubicación o título..." value={busqueda} onChange={(e) => setBusqueda(e.target.value)} />
        <select className="select-inline" value={estado} onChange={(e) => setEstado(e.target.value)}>
          <option value="">Todos los estados</option>
          {Object.entries(estadoEjemplar).map(([v, [, t]]) => <option key={v} value={v}>{t}</option>)}
        </select>
      </div>

      <div className="card table-card">
        <table>
          <thead><tr><th>Identificación</th><th>Código</th><th>Libro</th><th>Estado</th><th>Ubicación</th><th>Acciones</th></tr></thead>
          <tbody>
            {(cargando || ejemplares.length === 0) && <FilaEstado columnas={6} cargando={cargando} vacio="No hay ejemplares." />}
            {!cargando && ejemplares.map((e) => (
              <tr key={e.id}>
                <td className="mono">{codigo('E', e.id)}</td>
                <td className="mono">{e.codigo_inventario}</td>
                <td>{e.titulo_libro}</td>
                <td><Estado valor={e.estado} mapa={estadoEjemplar} /></td>
                <td>{e.ubicacion || '—'}</td>
                <td><button className="btn-link" onClick={() => setDetalle(e)}>👁 Ver detalle</button></td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {nuevo && <NuevoEjemplar onClose={() => setNuevo(false)} onHecho={() => { setNuevo(false); recargar(); }} />}
      {detalle && <DetalleEjemplar ejemplar={detalle} onClose={() => setDetalle(null)} onHecho={() => { setDetalle(null); recargar(); }} />}
    </div>
  );
}

function NuevoEjemplar({ onClose, onHecho }) {
  const [f, setF] = useState({ id_libro: '', codigo_inventario: '', ubicacion: '' });
  const { datos: libros } = useApi(() => getLibros(), [], []);
  const { ejecutar, enviando } = useAccion();
  const set = (k) => (e) => setF({ ...f, [k]: e.target.value });

  const guardar = async (e) => {
    e.preventDefault();
    const r = await ejecutar(
      () => crearEjemplar(f.id_libro, { codigo_inventario: f.codigo_inventario, ubicacion: f.ubicacion }),
      'Ejemplar registrado'
    );
    if (r) onHecho();
  };

  return (
    <Modal titulo="Registrar ejemplar" subtitulo="Si hay lectores esperando este libro, se le apartará al primero de la cola."
           onClose={onClose}
           pie={<><button className="btn-ghost" onClick={onClose}>Cancelar</button><button className="btn-primary" form="form-ej" disabled={enviando}>Guardar</button></>}>
      <form id="form-ej" className="form-grid" onSubmit={guardar}>
        <Campo label="Libro" ancho={2}>
          <select value={f.id_libro} onChange={set('id_libro')} required>
            <option value="">Seleccione un libro...</option>
            {libros.map((l) => <option key={l.id} value={l.id}>{l.titulo} ({l.ejemplares_total} ejemplares)</option>)}
          </select>
        </Campo>
        <Campo label="Código de inventario"><input value={f.codigo_inventario} onChange={set('codigo_inventario')} placeholder="INV-001" required /></Campo>
        <Campo label="Ubicación"><input value={f.ubicacion} onChange={set('ubicacion')} placeholder="Estante A1" /></Campo>
      </form>
    </Modal>
  );
}

function DetalleEjemplar({ ejemplar, onClose, onHecho }) {
  const [f, setF] = useState({ ubicacion: ejemplar.ubicacion || '', estado: ejemplar.estado });
  const { ejecutar, enviando } = useAccion();
  const bloqueado = ['PRESTADO', 'RESERVADO'].includes(ejemplar.estado);

  const guardar = async (e) => {
    e.preventDefault();
    const datos = { ubicacion: f.ubicacion, estado: f.estado !== ejemplar.estado ? f.estado : null };
    const r = await ejecutar(() => actualizarEjemplar(ejemplar.id, datos), 'Ejemplar actualizado');
    if (r) onHecho();
  };

  return (
    <Modal titulo={`Ejemplar ${ejemplar.codigo_inventario}`} subtitulo={ejemplar.titulo_libro} onClose={onClose}
           pie={<><button className="btn-ghost" onClick={onClose}>Cerrar</button><button className="btn-primary" form="form-ej2" disabled={enviando}>Guardar cambios</button></>}>
      <dl className="detalle" style={{ marginBottom: 18 }}>
        <dt>Identificación</dt><dd className="mono">{codigo('E', ejemplar.id)}</dd>
        <dt>Estado actual</dt><dd><Estado valor={ejemplar.estado} mapa={estadoEjemplar} /></dd>
      </dl>
      <form id="form-ej2" className="form-grid" onSubmit={guardar}>
        <Campo label="Ubicación"><input value={f.ubicacion} onChange={(e) => setF({ ...f, ubicacion: e.target.value })} /></Campo>
        <Campo label="Estado" ayuda={bloqueado ? 'Cambia solo con la devolución o la reserva.' : 'Para dar de baja elija "De baja".'}>
          <select value={f.estado} onChange={(e) => setF({ ...f, estado: e.target.value })} disabled={bloqueado}>
            {bloqueado && <option value={ejemplar.estado}>{estadoEjemplar[ejemplar.estado][1]}</option>}
            <option value="DISPONIBLE">Disponible</option>
            <option value="DANADO">Dañado</option>
            <option value="PERDIDO">Perdido</option>
            <option value="BAJA">De baja</option>
          </select>
        </Campo>
      </form>
    </Modal>
  );
}

export default Ejemplares;
