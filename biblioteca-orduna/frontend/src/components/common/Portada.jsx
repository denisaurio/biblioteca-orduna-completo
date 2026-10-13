// Portada generada (color + título), al estilo de "Libros destacados" del prototipo.
import { colorPortada } from '../../utils/formato.js';

function Portada({ libro, alto = 140 }) {
  return (
    <div className="portada" style={{ background: colorPortada(libro.id), height: alto }}>
      <span className="portada-titulo">{libro.titulo}</span>
      {libro.anio_publicacion && <span className="portada-anio">{libro.anio_publicacion}</span>}
    </div>
  );
}

export default Portada;
