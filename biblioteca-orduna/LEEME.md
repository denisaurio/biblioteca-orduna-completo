# Sistema de Biblioteca Orduña – Backend (Spring Boot + JWT)

Backend completo alineado con el documento *Seguimiento 2*: roles **LECTOR / BIBLIOTECARIO / GERENTE**,
reglas de negocio, 14 pantallas de Figma y tablas con los nombres del modelo (`id_usuario`,
`contrasena_hash`, `libros_autores`, `libros_generos`, `bitacora_turnos`, `configuracion_multas`, …).

## Cómo arrancar

1. En MySQL: `CREATE DATABASE biblioteca_orduna;`
   **Si ya tenías la base de la versión anterior, bórrala y créala de nuevo**
   (`DROP DATABASE biblioteca_orduna; CREATE DATABASE biblioteca_orduna;`): cambiaron roles,
   columnas y nombres de tablas, y `ddl-auto=update` no modifica columnas existentes.
2. Usuario/clave en `src/main/resources/application.properties` (root / sebas123).
3. `mvn spring-boot:run` (o Run en `BibliotecaApplication`). Hibernate crea las tablas y
   `data.sql` carga usuarios, configuración y géneros.
4. API en `http://localhost:8080`.

| Rol | Email | Contraseña |
|---|---|---|
| GERENTE | admin@biblioteca.pe | admin123 |
| BIBLIOTECARIO | biblio@biblioteca.pe | biblio123 |
| LECTOR | lector@biblioteca.pe | lector123 |

## Login (RF05)

```http
POST /api/auth/login
{"email":"biblio@biblioteca.pe","password":"biblio123"}
→ 200 {"token":"eyJ...","tipo":"Bearer","expira_en_ms":28800000,
        "id_usuario":2,"nombre":"Bibliotecario Principal","email":"biblio@biblioteca.pe","rol":"BIBLIOTECARIO"}
```

- El frontend guarda `token` y lo envía en cada petición: `Authorization: Bearer <token>` (dura 8 h).
- Según `rol`, redirige al Inicio que corresponde (lector: `GET /api/mi/panel`; personal: `GET /api/dashboard`).
- Lector con sanción activa → **403** con la fecha de fin (el documento dice que el acceso queda restringido).
- Los ids de bibliotecario/lector/gerente **salen del token**, nunca del body.

## Endpoints por pantalla

Leyenda: 🌐 público · 👤 cualquier usuario logueado · 📚 lector · 🧑‍💼 bibliotecario o gerente · 👑 gerente

### Login / Inicio
| Método | Ruta | Quién | Para qué |
|---|---|---|---|
| POST | `/api/auth/login` | 🌐 | Iniciar sesión |
| POST | `/api/usuarios/registro` | 🌐 | Auto-registro de lector |
| GET | `/api/dashboard` | 🧑‍💼 | KPIs (libros, préstamos activos/vencidos, reservas en espera, usuarios, multas) + destacados |
| GET | `/api/mi/panel` | 👤 | Inicio reducido del lector: sus préstamos, reservas, deuda y avisos sin leer |
| GET | `/api/configuracion` | 🌐 | Nombre de la biblioteca, horario, etc. |

### Libros (RF01, RF02)
| Método | Ruta | Quién |
|---|---|---|
| GET | `/api/libros` · `/api/libros/buscar?q=` · `/api/libros/{id}` · `/api/libros/destacados` | 🌐 |
| POST | `/api/libros` | 🧑‍💼 |
| PUT | `/api/libros/{id}` | 🧑‍💼 |

`estado` del libro (derivado): `DISPONIBLE`, `PRESTADO` o `SIN_EJEMPLARES`.

### Autores (RF03) y géneros
| Método | Ruta | Quién |
|---|---|---|
| GET | `/api/autores` · `/api/autores/{id}` · `/api/autores/{id}/libros` ("Ver libros") | 🌐 |
| POST / PUT / DELETE | `/api/autores` · `/api/autores/{id}` | 🧑‍💼 (no se elimina si tiene libros) |
| GET / POST | `/api/generos` | 🌐 / 🧑‍💼 |

### Ejemplares (RF14)
| Método | Ruta | Quién |
|---|---|---|
| POST | `/api/libros/{idLibro}/ejemplares` | 🧑‍💼 |
| GET | `/api/ejemplares?id_libro=&estado=&q=` · `/api/ejemplares/{id}` | 🧑‍💼 |
| PUT | `/api/ejemplares/{id}` `{"ubicacion":"B2","estado":"DANADO"}` | 🧑‍💼 |

Estados: `DISPONIBLE, PRESTADO, RESERVADO, DANADO, PERDIDO, BAJA`. A mano solo DISPONIBLE/DANADO/PERDIDO/BAJA.

### Usuarios (RF04)
| Método | Ruta | Quién |
|---|---|---|
| GET | `/api/usuarios?q=&rol=LECTOR&pagina=0&tamanio=10` (paginado) | 🧑‍💼 |
| POST | `/api/usuarios` ("+ Registrar usuario", crea lector) | 🧑‍💼 |
| GET / PUT | `/api/usuarios/{id}` | 🧑‍💼 (bibliotecario edita lectores; gerente a todos) |
| GET / PUT | `/api/usuarios/me` | 👤 |

Respuesta paginada: `{"contenido":[...],"pagina":0,"tamanio":10,"total_elementos":57,"total_paginas":6}`.
`estado` del usuario (calculado): `ACTIVO`, `INACTIVO` o `SUSPENDIDO`.

### Préstamos (RF06) – asistente Usuario → Libro → Confirmar
| Método | Ruta | Quién |
|---|---|---|
| GET | `/api/usuarios?q=` (paso 1) · `/api/libros/buscar?q=` (paso 2) | 🧑‍💼 |
| POST | `/api/prestamos` `{"id_libro":1,"id_usuario_lector":3,"tipo_prestamo":"FECHA_FIJA"}` | 🧑‍💼 |
| GET | `/api/prestamos?estado=ACTIVOS\|VENCIDOS\|DEVUELTOS\|TODOS&q=` | 🧑‍💼 |

Con `id_libro` el sistema elige el ejemplar (el apartado por reserva o el primero disponible).
`tipo_prestamo`: `FECHA_FIJA` (vence en `dias_max`) o `ABIERTO` (sin fecha, **nunca multa**).

### Devoluciones (RF07, RF08)
| Método | Ruta | Quién |
|---|---|---|
| GET | `/api/prestamos?estado=ACTIVOS` (tabla con `en_plazo` y `dias_retraso`) | 🧑‍💼 |
| POST | `/api/devoluciones` `{"id_prestamo":1,"observaciones":"...","estado_ejemplar":"DISPONIBLE"}` | 🧑‍💼 |

### Reservas (RF09)
| Método | Ruta | Quién |
|---|---|---|
| POST | `/api/reservas` `{"id_libro":1}` | 📚 |
| GET | `/api/mi/reservas` | 👤 |
| PATCH | `/api/reservas/{id}/cancelar` | 👤 (el lector solo las suyas) |
| GET | `/api/reservas?estado=PENDIENTE` · `/api/reservas/cola/{idLibro}` | 🧑‍💼 |

### Pagos de multa (RF15)
| Método | Ruta | Quién |
|---|---|---|
| GET | `/api/multas?estado=PENDIENTES\|PAGADAS\|TODAS` | 🧑‍💼 |
| POST | `/api/multas/{id}/pagar` `{"monto_pagado":1.00,"metodo_pago":"EFECTIVO"}` | 🧑‍💼 |
| GET | `/api/multas/pagos?desde=&hasta=` (auditoría) | 🧑‍💼 |
| GET | `/api/mi/multas` · `/api/mi/prestamos` | 👤 |

Métodos: `EFECTIVO, TARJETA, TRANSFERENCIA, YAPE, PLIN, OTRO`. Acepta **pago parcial** (estado `PARCIAL`).

### Sanciones y bloqueos (RF17)
| Método | Ruta | Quién |
|---|---|---|
| POST | `/api/usuarios/{id}/suspender` `{"motivo":"...","fecha_fin":"2026-10-15"}` | 🧑‍💼 |
| GET | `/api/sanciones?activas=true` · `/api/usuarios/{id}/sanciones` | 🧑‍💼 |
| PATCH | `/api/sanciones/{id}/levantar` | 🧑‍💼 |

### Avisos y notificaciones (RF11)
| Método | Ruta | Quién |
|---|---|---|
| GET | `/api/notificaciones?no_leidas=true` · `/api/notificaciones/no-leidas/total` | 👤 |
| PATCH | `/api/notificaciones/{id}/leida` · `/api/notificaciones/leidas` | 👤 |
| POST | `/api/notificaciones` ("+ Crear aviso") `{"id_usuario":5,"mensaje":"..."}` (sin id = a todos los lectores) | 🧑‍💼 |
| GET | `/api/notificaciones/todas?tipo=&pagina=0&tamanio=20` | 🧑‍💼 |
| POST | `/api/notificaciones/generar` | 👑 |

Tipos: `VENCIMIENTO_PROXIMO, PRESTAMO_VENCIDO, RESERVA_DISPONIBLE, RESERVA_VENCIDA, MULTA_PENDIENTE, NUEVO_LIBRO, ADMINISTRATIVO`.
Los interruptores de "Configuración de avisos" están en `PUT /api/configuracion` (`aviso_vencimiento`, `aviso_reserva`, `aviso_multa`, `aviso_nuevo_libro`).

### Bitácora de turnos (RF16)
| Método | Ruta | Quién |
|---|---|---|
| POST | `/api/turnos/abrir` · `/api/turnos/cerrar` | 🧑‍💼 |
| GET | `/api/turnos/actual` (204 si no hay) | 🧑‍💼 |
| GET | `/api/turnos?desde=&hasta=&id_bibliotecario=&tipo=GERENTE` | 🧑‍💼 (el bibliotecario solo ve los suyos) |

### Configuración (RF13)
| Método | Ruta | Quién |
|---|---|---|
| GET | `/api/configuracion` | 🌐 |
| PUT | `/api/configuracion` (solo los campos enviados) | 👑 |
| GET | `/api/configuracion/historial` | 👑 |

```json
{"nombre_biblioteca":"Biblioteca Orduña","correo_contacto":"contacto@biblioteca.pe","telefono_contacto":"064...",
 "hora_apertura":"08:00","hora_cierre":"20:00",
 "tarifa_multa_diaria":1.00,"dias_max":7,"dias_retiro_reserva":2,
 "aviso_vencimiento":true,"aviso_reserva":true,"aviso_multa":true,"aviso_nuevo_libro":true}
```

### Reportes (RF10) y cierre diario (RF12)
| Método | Ruta | Quién |
|---|---|---|
| GET | `/api/reportes/inventario` · `/prestamos?desde=&hasta=` · `/multas?desde=&hasta=` | 🧑‍💼 |
| GET | `/api/reportes/{inventario\|prestamos\|multas}/csv` (descarga) | 🧑‍💼 |
| POST | `/api/cierre-diario` · GET `/api/cierre-diario?desde=&hasta=` · `/api/cierre-diario/{fecha}` | 🧑‍💼 |

## Reglas de negocio del documento (implementadas)

| Regla | Dónde |
|---|---|
| Préstamo con fecha fija o abierto; el abierto nunca genera multa | `PrestamoService`, `DevolucionService` |
| Multa = días de retraso × tarifa; la tarifa se guarda en el préstamo, así un cambio posterior no es retroactivo | `Prestamo.tarifaMultaDiaria`, `configuracion_multas` versionada |
| Solo se presta si hay ejemplar disponible; si no, se reserva | `PrestamoService`, `ReservaService` |
| Solo se reserva un libro sin ejemplares disponibles | `ReservaService.crearReserva` |
| Reservas FIFO: al liberarse un ejemplar se aparta para el primero y se le avisa | `ReservaService.asignarEjemplarLiberado` |
| La reserva vence si no se recoge a tiempo (`dias_retiro_reserva`) y pasa al siguiente | tarea diaria 00:10 |
| Avisos automáticos de vencimiento próximo / vencido | tarea diaria 8:00 |
| Cierre diario con préstamos activos y vencidos | `CierreDiarioService` + tarea 23:55 |
| Contraseñas cifradas (BCrypt) | `SecurityConfig.passwordEncoder` |
| Acceso por rol (lector, bibliotecario, gerente) | `SecurityConfig` |
| Sancionado: no inicia sesión, no presta, no reserva; estado SUSPENDIDO; finaliza sola o al levantarla | `SancionService`, `AuthController` |
| Lector con multa pendiente o parcial no puede pedir préstamos | `PrestamoService` |
| No se elimina un autor con libros asociados | `AutorService.eliminarAutor` |
| El estado de los ejemplares lo cambian préstamos, devoluciones y reservas | `EjemplarService` |

## Errores (GlobalExceptionHandler)

```json
{"timestamp":"2026-10-01T18:30:00","status":400,"error":"Bad Request",
 "mensaje":"Hay datos inválidos en la petición","path":"/api/libros",
 "errores":{"ids_autores":"Debe indicar al menos un autor"}}
```

| HTTP | Cuándo |
|---|---|
| 400 | Validación `@Valid`, regla de negocio (`OperacionInvalida`, `EjemplarNoDisponible`, `ReservaActivaDuplicada`, `TurnoPendiente`), JSON o parámetro mal formado |
| 401 | Sin token / token vencido / credenciales incorrectas |
| 403 | Rol sin permiso, usuario sancionado, editar/cancelar algo ajeno (`AccesoDenegado`) |
| 404 | Id inexistente |
| 409 | ISBN, DNI, email, código de inventario o género repetido; cierre del día ya hecho |

## Diferencias con el documento (decisiones)

- `libros_generos` usa una tabla `generos` con id (`id_libro`, `id_genero`) en vez de un texto libre: sigue en 4FN y evita géneros mal escritos.
- El estado ACTIVO/INACTIVO/SUSPENDIDO del usuario se **calcula** (cuenta activa + sanción vigente) en vez de guardarse, para que nunca quede desincronizado.
- "¿Olvidaste tu contraseña?" y "Recordar sesión" son del frontend; el backend no envía correos.
- Al pagar una multa no se levanta una sanción automáticamente: la deuda ya bloquea préstamos por sí sola, y la sanción se levanta con `PATCH /api/sanciones/{id}/levantar`.
