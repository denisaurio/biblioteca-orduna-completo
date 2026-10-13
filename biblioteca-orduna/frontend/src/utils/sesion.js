// Guarda la sesión (token + usuario). "Recordar sesión" => localStorage; si no => sessionStorage.
const CLAVE = 'biblioteca_sesion';

export function leerSesion() {
  try {
    const txt = localStorage.getItem(CLAVE) || sessionStorage.getItem(CLAVE);
    return txt ? JSON.parse(txt) : null;
  } catch {
    return null;
  }
}

export function guardarSesion(sesion, recordar) {
  borrarSesion();
  (recordar ? localStorage : sessionStorage).setItem(CLAVE, JSON.stringify(sesion));
}

export function borrarSesion() {
  localStorage.removeItem(CLAVE);
  sessionStorage.removeItem(CLAVE);
}
