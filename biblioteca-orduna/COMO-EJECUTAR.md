# Sistema de Biblioteca Orduña — Cómo ejecutar

```
biblioteca-orduna/
├── pom.xml, src/      ← BACKEND (Spring Boot, puerto 8080)
└── frontend/          ← FRONTEND (React + Vite, puerto 5173)
```

## 1. Base de datos (una sola vez)
```sql
CREATE DATABASE biblioteca_orduna;
```
Usuario/clave en `src/main/resources/application.properties` (root / sebas123).

## 2. Backend (terminal 1, en la carpeta biblioteca-orduna)
```
mvn spring-boot:run
```
o Run en `BibliotecaApplication` desde IntelliJ / VS Code.
Prueba: http://localhost:8080/ debe mostrar `"estado": "funcionando"`.

## 3. Frontend (terminal 2, en la carpeta biblioteca-orduna/frontend)
```
npm install      (solo la primera vez)
npm run dev
```
Abre http://localhost:5173

## Usuarios de prueba
| Rol | Correo | Contraseña |
|---|---|---|
| Gerente | admin@biblioteca.pe | admin123 |
| Bibliotecario | biblio@biblioteca.pe | biblio123 |
| Lector | lector@biblioteca.pe | lector123 |

Detalle del backend: `LEEME.md` · Detalle del frontend: `frontend/README.md`
