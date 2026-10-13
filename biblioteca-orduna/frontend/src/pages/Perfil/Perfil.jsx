// Mi perfil (cualquier rol): GET y PUT /api/usuarios/me.
import { useState } from 'react';
import { getMiPerfil, actualizarMiPerfil } from '../../api/usuariosApi.js';
import { useAuth } from '../../context/AuthContext.jsx';
import { useApi } from '../../hooks.js';
import Estado from '../../components/common/Estado.jsx';
import { Cargando } from '../../components/common/Cargando.jsx';
import { FormUsuario } from '../Usuarios/Usuarios.jsx';
import { estadoUsuario } from '../../utils/estados.js';
import { codigo, fmtFecha, iniciales, nombreRol, nombreTipoUsuario } from '../../utils/formato.js';

function Perfil() {
  const { actualizarNombre } = useAuth();
  const [editar, setEditar] = useState(false);
  const { datos: u, cargando, recargar } = useApi(() => getMiPerfil(), []);

  if (cargando || !u) return <Cargando />;

  return (
    <div>
      <div className="page-head">
        <div><h1>Mi perfil</h1><p>Tus datos de usuario</p></div>
        <button className="btn-primary" onClick={() => setEditar(true)}>Editar mis datos</button>
      </div>
      <div className="card" style={{ padding: 24, maxWidth: 640 }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 14, marginBottom: 20 }}>
          <span className="pick-avatar" style={{ width: 52, height: 52, fontSize: 16 }}>{iniciales(`${u.nombre} ${u.apellido}`)}</span>
          <div>
            <div style={{ fontSize: 18, fontWeight: 700 }}>{u.nombre} {u.apellido}</div>
            <div className="pick-sub">{codigo('U', u.id)} · {nombreRol[u.rol]}</div>
          </div>
          <div style={{ marginLeft: 'auto' }}><Estado valor={u.estado} mapa={estadoUsuario} /></div>
        </div>
        <dl className="detalle">
          <dt>DNI</dt><dd className="mono">{u.dni}</dd>
          <dt>Correo</dt><dd>{u.email}</dd>
          <dt>Teléfono</dt><dd>{u.telefono || '—'}</dd>
          <dt>Dirección</dt><dd>{u.direccion || '—'}</dd>
          <dt>Tipo</dt><dd>{nombreTipoUsuario[u.tipo_usuario]}</dd>
          <dt>Registrado</dt><dd>{fmtFecha(u.fecha_registro)}</dd>
        </dl>
      </div>
      {editar && (
        <FormUsuario
          usuario={u}
          guardar={actualizarMiPerfil}
          onClose={() => setEditar(false)}
          onGuardado={(nuevo) => { setEditar(false); actualizarNombre(`${nuevo.nombre} ${nuevo.apellido}`); recargar(); }}
        />
      )}
    </div>
  );
}

export default Perfil;
