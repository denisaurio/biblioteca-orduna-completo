// Réplica del prototipo Figma "CONFIGURACION" (RF13), solo gerente:
// pestañas General / Multas y Reservas, vista previa y "Guardar cambios".
// Los cambios de tarifa y días quedan versionados (configuracion_multas): no son retroactivos.
import { useEffect, useState } from 'react';
import { getConfiguracion, actualizarConfiguracion, getHistorialConfiguracion } from '../../api/configuracionApi.js';
import { useApi, useAccion } from '../../hooks.js';
import { Cargando } from '../../components/common/Cargando.jsx';
import { fmtFechaHora, fmtSoles } from '../../utils/formato.js';
import './Configuracion.css';

function Configuracion() {
  const [tab, setTab] = useState('general');
  const [form, setForm] = useState(null);
  const { datos, cargando } = useApi(() => getConfiguracion(), []);
  const { datos: historial, recargar: recargarHistorial } = useApi(() => getHistorialConfiguracion(), [], []);
  const { ejecutar, enviando } = useAccion();

  useEffect(() => {
    if (datos) setForm({ ...datos, hora_apertura: datos.hora_apertura?.slice(0, 5) || '', hora_cierre: datos.hora_cierre?.slice(0, 5) || '' });
  }, [datos]);

  if (cargando || !form) return <Cargando />;

  const set = (campo) => (e) => setForm((f) => ({ ...f, [campo]: e.target.value }));

  const guardar = async () => {
    const datosEnviar = {
      nombre_biblioteca: form.nombre_biblioteca,
      correo_contacto: form.correo_contacto || null,
      telefono_contacto: form.telefono_contacto,
      hora_apertura: form.hora_apertura || null,
      hora_cierre: form.hora_cierre || null,
      dias_max: Number(form.dias_max),
      tarifa_multa_diaria: Number(form.tarifa_multa_diaria),
      dias_retiro_reserva: Number(form.dias_retiro_reserva),
    };
    const r = await ejecutar(() => actualizarConfiguracion(datosEnviar), 'Configuración guardada');
    if (r) recargarHistorial();
  };

  return (
    <div>
      <div className="page-head">
        <div><h1>Configuración</h1><p>Parámetros generales del sistema (RF13).</p></div>
      </div>

      <div className="config-tabs">
        <button className={tab === 'general' ? 'active' : ''} onClick={() => setTab('general')}>General</button>
        <button className={tab === 'multas' ? 'active' : ''} onClick={() => setTab('multas')}>Multas y Reservas</button>
      </div>

      <div className="config-grid">
        <div className="card config-form">
          {tab === 'general' ? (
            <>
              <h3>Configuración general</h3>
              <div className="form-row">
                <div><label>Nombre de la biblioteca</label><input value={form.nombre_biblioteca || ''} onChange={set('nombre_biblioteca')} /></div>
                <div><label>Email de contacto</label><input type="email" value={form.correo_contacto || ''} onChange={set('correo_contacto')} /></div>
              </div>
              <div className="form-row">
                <div><label>Teléfono</label><input value={form.telefono_contacto || ''} onChange={set('telefono_contacto')} /></div>
                <div><label>Días máximo de préstamo</label><input type="number" min="1" value={form.dias_max} onChange={set('dias_max')} /></div>
              </div>
              <label className="horario-label">Horario de apertura</label>
              <div className="form-row">
                <div><label className="sub-label">Apertura</label><input type="time" value={form.hora_apertura} onChange={set('hora_apertura')} /></div>
                <div><label className="sub-label">Cierre</label><input type="time" value={form.hora_cierre} onChange={set('hora_cierre')} /></div>
              </div>
            </>
          ) : (
            <>
              <h3>Multas y reservas</h3>
              <div className="form-row">
                <div><label>Tarifa diaria de multa (S/)</label><input type="number" step="0.10" min="0" value={form.tarifa_multa_diaria} onChange={set('tarifa_multa_diaria')} /></div>
                <div><label>Días máximo de préstamo</label><input type="number" min="1" value={form.dias_max} onChange={set('dias_max')} /></div>
              </div>
              <div className="form-row">
                <div><label>Días para retirar una reserva</label><input type="number" min="1" value={form.dias_retiro_reserva} onChange={set('dias_retiro_reserva')} /></div>
                <div />
              </div>
              <p className="hint">Los cambios quedan versionados con fecha de vigencia y no afectan préstamos o multas ya calculados.</p>
            </>
          )}
          <button className="btn-primary" onClick={guardar} disabled={enviando}>{enviando ? 'Guardando...' : 'Guardar cambios'}</button>
        </div>

        <div className="card config-preview">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.6">
            <path d="M4 5.5A2.5 2.5 0 0 1 6.5 3H20v16H6.5A2.5 2.5 0 0 0 4 21.5V5.5Z" />
          </svg>
          <div>{form.nombre_biblioteca}</div>
          <small style={{ color: 'var(--muted)' }}>
            {form.hora_apertura && form.hora_cierre ? `Abierto de ${form.hora_apertura} a ${form.hora_cierre}` : ''}
          </small>
        </div>
      </div>

      {tab === 'multas' && (
        <>
          <h3 className="seccion-titulo" style={{ marginTop: 26 }}>Historial de vigencias</h3>
          <div className="card table-card">
            <table>
              <thead><tr><th>Vigente desde</th><th>Hasta</th><th>Tarifa diaria</th><th>Días de préstamo</th><th>Días para retirar</th><th>Modificado por</th></tr></thead>
              <tbody>
                {historial.map((h) => (
                  <tr key={h.id}>
                    <td className="mono">{fmtFechaHora(h.fecha_vigencia_desde)}</td>
                    <td className="mono">{h.fecha_vigencia_hasta ? fmtFechaHora(h.fecha_vigencia_hasta) : <b style={{ color: 'var(--green-text)' }}>Vigente</b>}</td>
                    <td className="mono">{fmtSoles(h.tarifa_multa_diaria)}</td>
                    <td className="mono">{h.dias_max}</td>
                    <td className="mono">{h.dias_retiro_reserva}</td>
                    <td>{h.modificado_por || 'Configuración inicial'}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </>
      )}
    </div>
  );
}

export default Configuracion;
