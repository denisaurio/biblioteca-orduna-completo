// Pill de estado reutilizado en todos los módulos (Disponible, Retraso, Activo, etc.)
// color: 'green' | 'red' | 'amber' | 'blue'
function Badge({ color = 'green', children }) {
  return (
    <span className={`badge ${color}`}>
      <span className="dot"></span>
      {children}
    </span>
  );
}

export default Badge;
