// Etiqueta + control de formulario con el estilo del prototipo.
function Campo({ label, children, ayuda, ancho }) {
  return (
    <div className="campo" style={ancho ? { gridColumn: `span ${ancho}` } : undefined}>
      {label && <label>{label}</label>}
      {children}
      {ayuda && <small>{ayuda}</small>}
    </div>
  );
}

export default Campo;
