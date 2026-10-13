// Badge a partir de un estado del backend y su tabla de colores (ver utils/estados.js).
import Badge from './Badge.jsx';

function Estado({ valor, mapa }) {
  const [color, texto] = mapa[valor] || ['blue', valor || '—'];
  return <Badge color={color}>{texto}</Badge>;
}

export default Estado;
