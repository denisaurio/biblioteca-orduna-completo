// Registrar / editar libro (RF01). Autores y géneros obligatorios (tablas libros_autores y libros_generos).
import { useState } from 'react';
import { crearLibro, actualizarLibro, getGeneros, crearGenero } from '../../api/librosApi.js';
import { getAutores } from '../../api/autoresApi.js';
import { useApi, useAccion } from '../../hooks.js';
import Modal from '../../components/common/Modal.jsx';
import Campo from '../../components/common/Campo.jsx';

function FormLibro({ libro, onClose, onGuardado }) {
  const [f, setF] = useState({
    titulo: libro?.titulo || '',
    isbn: libro?.isbn || '',
    editorial: libro?.editorial || '',
    anio_publicacion: libro?.anio_publicacion || '',
    num_paginas: libro?.num_paginas || '',
    ids_autores: libro?.autores.map((a) => a.id) || [],
    ids_generos: libro?.generos.map((g) => g.id) || [],
  });
  const [nuevoGenero, setNuevoGenero] = useState('');
  const { datos: autores } = useApi(() => getAutores(), [], []);
  const { datos: generos, recargar: recargarGeneros } = useApi(() => getGeneros(), [], []);
  const { ejecutar, enviando } = useAccion();

  const set = (k) => (e) => setF({ ...f, [k]: e.target.value });
  const alternar = (k, id) => setF({ ...f, [k]: f[k].includes(id) ? f[k].filter((x) => x !== id) : [...f[k], id] });

  const agregarGenero = async () => {
    if (!nuevoGenero.trim()) return;
    const g = await ejecutar(() => crearGenero(nuevoGenero.trim()), 'Género creado');
    if (g) {
      setNuevoGenero('');
      await recargarGeneros();
      setF((x) => ({ ...x, ids_generos: [...x.ids_generos, g.id] }));
    }
  };

  const guardar = async (e) => {
    e.preventDefault();
    const datos = {
      ...f,
      anio_publicacion: f.anio_publicacion ? Number(f.anio_publicacion) : null,
      num_paginas: f.num_paginas ? Number(f.num_paginas) : null,
    };
    const ok = await ejecutar(
      () => (libro ? actualizarLibro(libro.id, datos) : crearLibro(datos)),
      libro ? 'Libro actualizado' : 'Libro registrado'
    );
    if (ok) onGuardado();
  };

  return (
    <Modal
      titulo={libro ? 'Editar libro' : 'Registrar libro'}
      subtitulo="Título, ISBN, autor y género son obligatorios"
      onClose={onClose}
      ancho={640}
      pie={
        <>
          <button className="btn-ghost" onClick={onClose}>Cancelar</button>
          <button className="btn-primary" form="form-libro" disabled={enviando}>{enviando ? 'Guardando...' : 'Guardar'}</button>
        </>
      }
    >
      <form id="form-libro" className="form-grid" onSubmit={guardar}>
        <Campo label="Título" ancho={2}><input value={f.titulo} onChange={set('titulo')} required /></Campo>
        <Campo label="ISBN"><input value={f.isbn} onChange={set('isbn')} required placeholder="978-..." /></Campo>
        <Campo label="Editorial"><input value={f.editorial} onChange={set('editorial')} /></Campo>
        <Campo label="Año de publicación"><input type="number" value={f.anio_publicacion} onChange={set('anio_publicacion')} min="1000" max="2100" /></Campo>
        <Campo label="Número de páginas"><input type="number" value={f.num_paginas} onChange={set('num_paginas')} min="1" /></Campo>
        <Campo label={`Autores (${f.ids_autores.length} seleccionados)`} ancho={2} ayuda={autores.length === 0 ? 'Primero registra autores en el módulo Autores.' : undefined}>
          <div className="chips">
            {autores.map((a) => (
              <span key={a.id} className={'chip' + (f.ids_autores.includes(a.id) ? ' on' : '')} onClick={() => alternar('ids_autores', a.id)}>{a.nombre}</span>
            ))}
          </div>
        </Campo>
        <Campo label={`Géneros (${f.ids_generos.length} seleccionados)`} ancho={2}>
          <div className="chips">
            {generos.map((g) => (
              <span key={g.id} className={'chip' + (f.ids_generos.includes(g.id) ? ' on' : '')} onClick={() => alternar('ids_generos', g.id)}>{g.nombre}</span>
            ))}
          </div>
          <div style={{ display: 'flex', gap: 8, marginTop: 6 }}>
            <input className="input" placeholder="Nuevo género..." value={nuevoGenero} onChange={(e) => setNuevoGenero(e.target.value)}
                   onKeyDown={(e) => { if (e.key === 'Enter') { e.preventDefault(); agregarGenero(); } }} />
            <button type="button" className="btn-ghost" onClick={agregarGenero}>Agregar</button>
          </div>
        </Campo>
      </form>
    </Modal>
  );
}

export default FormLibro;
