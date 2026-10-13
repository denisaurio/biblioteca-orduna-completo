// Paginación del prototipo ("Mostrando 1-5 de 20" + botones). pagina empieza en 0.
function Paginacion({ pagina, totalPaginas, totalElementos, tamanio, onCambiar }) {
  if (!totalElementos) return null;
  const desde = pagina * tamanio + 1;
  const hasta = Math.min(totalElementos, (pagina + 1) * tamanio);
  const inicio = Math.max(0, Math.min(pagina - 2, totalPaginas - 5));
  const paginas = Array.from({ length: Math.min(5, totalPaginas) }, (_, i) => inicio + i);

  return (
    <div className="pagination">
      <span>Mostrando {desde}-{hasta} de {totalElementos}</span>
      <div className="pages">
        <button disabled={pagina === 0} onClick={() => onCambiar(pagina - 1)} aria-label="Anterior">‹</button>
        {paginas.map((p) => (
          <button key={p} className={p === pagina ? 'active' : ''} onClick={() => onCambiar(p)}>{p + 1}</button>
        ))}
        <button disabled={pagina >= totalPaginas - 1} onClick={() => onCambiar(pagina + 1)} aria-label="Siguiente">›</button>
      </div>
    </div>
  );
}

export default Paginacion;
