// Tarjeta KPI del Inicio (ícono + etiqueta + valor), como en el prototipo.
function StatCard({ label, value, icono, color = 'blue' }) {
  return (
    <div className="stat-card">
      {icono && <div className={`stat-icon ${color}`}>{icono}</div>}
      <div>
        <div className="label">{label}</div>
        <div className="value">{value ?? '—'}</div>
      </div>
    </div>
  );
}

export default StatCard;
