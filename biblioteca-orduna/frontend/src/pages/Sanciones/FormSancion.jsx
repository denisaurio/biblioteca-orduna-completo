// RF17 - Aplicar sanción a un lector (POST /api/usuarios/{id}/suspender).
// Si no se pasa "lector", permite buscarlo. El responsable sale del JWT.
import { useState } from 'react';
import { aplicarSancion } from '../../api/sancionesApi.js';
import { getUsuarios } from '../../api/usuariosApi.js';
import { useApi, useAccion, useDebounce } from '../../hooks.js';
import Modal from '../../components/common/Modal.jsx';
import Campo from '../../components/common/Campo.jsx';
import { hoyISO } from '../../utils/formato.js';

const MOTIVOS = ['Retraso en devolución', 'Multa pendiente', 'Uso indebido', 'Daño a material', 'Otro'];

function FormSancion({ lector: inicial, onClose, onGuardado }) {
  const [lector, setLector] = useState(inicial || null);
  const [busqueda, setBusqueda] = useState('');
  const [motivo, setMotivo] = useState(MOTIVOS[0]);
  const [detalle, setDetalle] = useState('');
  const [fechaFin, setFechaFin] = useState(hoyISO(7));
  const q = useDebounce(busqueda);
  const { datos: encontrados } = useApi(
    () => (inicial ? Promise.resolve({ data: { contenido: [] } }) : getUsuarios({ q, rol: 'LECTOR', tamanio: 6 })),
    [q], { contenido: [] });
  const { ejecutar, enviando } = useAccion();

  const guardar = async (e) => {
    e.preventDefault();
    const texto = detalle.trim() ? `${motivo}: ${detalle.trim()}` : motivo;
    const r = await ejecutar(() => aplicarSancion(lector.id, { motivo: texto, fecha_fin: fechaFin }), 'Sanción aplicada');
    if (r) onGuardado();
  };

  return (
    <Modal titulo="Aplicar sanción" subtitulo="Mientras esté activa, el lector no podrá iniciar sesión, pedir préstamos ni reservar."
           onClose={onClose}
           pie={<><button className="btn-ghost" onClick={onClose}>Cancelar</button>
             <button className="btn-danger" form="form-sancion" disabled={enviando || !lector}>Aplicar sanción</button></>}>
      <form id="form-sancion" className="form-grid" onSubmit={guardar}>
        {inicial ? (
          <Campo label="Lector" ancho={2}><input value={`${inicial.nombre} ${inicial.apellido} · DNI ${inicial.dni}`} disabled /></Campo>
        ) : (
          <Campo label="Lector" ancho={2}>
            {lector ? (
              <div style={{ display: 'flex', gap: 8 }}>
                <input value={`${lector.nombre} ${lector.apellido} · DNI ${lector.dni}`} disabled />
                <button type="button" className="btn-ghost" onClick={() => setLector(null)}>Cambiar</button>
              </div>
            ) : (
              <>
                <input placeholder="Buscar por nombre o DNI..." value={busqueda} onChange={(e) => setBusqueda(e.target.value)} autoFocus />
                <div className="chips" style={{ marginTop: 6 }}>
                  {encontrados.contenido.map((u) => (
                    <span key={u.id} className="chip" onClick={() => setLector(u)}>{u.nombre} {u.apellido} · {u.dni}</span>
                  ))}
                  {encontrados.contenido.length === 0 && <small>Sin resultados</small>}
                </div>
              </>
            )}
          </Campo>
        )}
        <Campo label="Motivo">
          <select value={motivo} onChange={(e) => setMotivo(e.target.value)}>
            {MOTIVOS.map((m) => <option key={m}>{m}</option>)}
          </select>
        </Campo>
        <Campo label="Fecha fin (inclusive)">
          <input type="date" value={fechaFin} min={hoyISO()} onChange={(e) => setFechaFin(e.target.value)} required />
        </Campo>
        <Campo label="Detalle (opcional)" ancho={2}><input value={detalle} onChange={(e) => setDetalle(e.target.value)} maxLength={200} /></Campo>
      </form>
    </Modal>
  );
}

export default FormSancion;
