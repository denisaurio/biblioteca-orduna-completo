# Frontend — Biblioteca Orduña

React 18 + Vite + React Router + Axios. Réplica de las 14 pantallas del prototipo Figma
(documento *Seguimiento 2*), **conectada al backend Spring Boot** (`/api`, JWT).

## Cómo correrlo

1. Arranca el backend (Spring Boot en `http://localhost:8080`).
2. En esta carpeta:
   ```bash
   npm install
   npm run dev
   ```
3. Abre http://localhost:5173

| Rol | Correo | Contraseña |
|---|---|---|
| Gerente | admin@biblioteca.pe | admin123 |
| Bibliotecario | biblio@biblioteca.pe | biblio123 |
| Lector | lector@biblioteca.pe | lector123 |

Si el backend corre en otra dirección, cambia `VITE_API_URL` en el archivo `.env`
(y reinicia `npm run dev`). El backend ya permite CORS para `http://localhost:5173`.

## Cómo funciona la conexión

- `src/api/axiosClient.js` — cliente único: agrega `Authorization: Bearer <token>` a cada
  petición; si el backend responde 401 (token vencido) cierra la sesión y vuelve al login.
  `mensajeError()` muestra el `mensaje` / `errores` que manda el `GlobalExceptionHandler`.
- `src/context/AuthContext.jsx` — login real con `POST /api/auth/login`; guarda token y rol
  ("Recordar sesión" = `localStorage`, si no `sessionStorage`).
- `src/api/*.js` — una función por endpoint del backend.
- `src/hooks.js` — `useApi` (cargar datos) y `useAccion` (guardar con mensaje de éxito/error).

## Pantallas y endpoints

| Pantalla | Ruta | Rol | Endpoints |
|---|---|---|---|
| Login / Registro | `/login`, `/registro` | público | `POST /auth/login`, `POST /usuarios/registro` |
| Inicio | `/inicio` | todos | `GET /dashboard` (personal) · `GET /mi/panel` (lector) |
| Libros | `/libros` | todos | `GET /libros`, `/libros/buscar`, `POST/PUT /libros`, `GET/POST /generos`, `POST /reservas` (lector) |
| Autores | `/autores` | todos | `GET/POST/PUT/DELETE /autores`, `GET /autores/{id}/libros` |
| Usuarios | `/usuarios` | personal | `GET /usuarios` (paginado), `POST/PUT /usuarios`, `POST /usuarios/{id}/suspender` |
| Préstamos | `/prestamos` | personal | asistente: `GET /usuarios`, `GET /libros`, `POST /prestamos` |
| Mis préstamos y multas | `/prestamos` | lector | `GET /mi/prestamos`, `GET /mi/multas` |
| Devoluciones | `/devoluciones` | personal | `GET /prestamos?estado=`, `POST /devoluciones` |
| Reservas | `/reservas` | personal | `GET /reservas`, `GET /reservas/cola/{id}`, `PATCH /reservas/{id}/cancelar` |
| Mis reservas | `/reservas` | lector | `GET /mi/reservas`, `POST /reservas`, `PATCH /reservas/{id}/cancelar` |
| Ejemplares | `/ejemplares` | personal | `GET /ejemplares`, `POST /libros/{id}/ejemplares`, `PUT /ejemplares/{id}` |
| Pagos de multas | `/pagos-multa` | personal | `GET /multas`, `POST /multas/{id}/pagar`, `GET /multas/pagos` |
| Sanciones y bloqueos | `/sanciones` | personal | `GET /sanciones`, `POST /usuarios/{id}/suspender`, `PATCH /sanciones/{id}/levantar` |
| Avisos | `/avisos` | todos | `GET /notificaciones`, `PATCH .../leida`, `POST /notificaciones`, `GET /notificaciones/todas`, `PUT /configuracion` (interruptores) |
| Bitácora de turnos | `/bitacora` | personal | `GET /turnos`, `GET /turnos/actual`, `POST /turnos/abrir`, `POST /turnos/cerrar` |
| Reportes y cierre | `/reportes` | personal | `GET /reportes/*`, descarga `GET /reportes/*/csv`, `GET/POST /cierre-diario` |
| Configuración | `/configuracion` | gerente | `GET/PUT /configuracion`, `GET /configuracion/historial` |
| Mi perfil | `/perfil` | todos | `GET/PUT /usuarios/me` |

*Personal* = bibliotecario o gerente (bloque "Administración" del menú). El lector ve un menú
reducido: Inicio, Libros, Autores, Mis préstamos y multas, Mis reservas y Avisos.

## Estructura

```
src/
├── api/            una función por endpoint (axios)
├── components/
│   ├── common/     Badge, Estado, Modal, Campo, Paginacion, Portada, StatCard, SearchInput, Cargando
│   └── layout/     AppLayout, Sidebar (menú por rol), Topbar (buscador, campanita, usuario)
├── context/        AuthContext (sesión JWT), ToastContext (mensajes)
├── pages/          una carpeta por pantalla
├── routes/         rutas y protección por rol
├── styles/         global.css (colores y tipografía del Figma)
├── utils/          formatos (fechas, soles), estados → colores, sesión
└── hooks.js        useApi, useAccion, useDebounce
```

## Producción

```bash
npm run build     # genera dist/
```
