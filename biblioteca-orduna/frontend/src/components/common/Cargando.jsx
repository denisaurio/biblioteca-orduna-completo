// Indicador de carga y mensaje de "sin datos".
export function Cargando({ texto = 'Cargando...' }) {
  return <div className="estado-vacio"><span className="spinner" /> {texto}</div>;
}

export function Vacio({ texto = 'No hay registros para mostrar.' }) {
  return <div className="estado-vacio">{texto}</div>;
}

/** Fila de tabla para "cargando" / "vacío" ocupando todas las columnas. */
export function FilaEstado({ columnas, cargando, vacio = 'No hay registros para mostrar.' }) {
  return (
    <tr>
      <td colSpan={columnas} className="celda-estado">
        {cargando ? <><span className="spinner" /> Cargando...</> : vacio}
      </td>
    </tr>
  );
}
