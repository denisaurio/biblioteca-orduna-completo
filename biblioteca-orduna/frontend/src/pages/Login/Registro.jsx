// RF04 - Auto-registro de lector (público): POST /api/usuarios/registro.
import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { registrarse } from '../../api/authApi.js';
import { mensajeError } from '../../api/axiosClient.js';
import { useAuth } from '../../context/AuthContext.jsx';
import Campo from '../../components/common/Campo.jsx';
import './Login.css';

const VACIO = { nombre: '', apellido: '', dni: '', email: '', telefono: '', direccion: '', tipo_usuario: 'ESTUDIANTE', password: '', confirmar: '' };

function Registro() {
  const [f, setF] = useState(VACIO);
  const [error, setError] = useState('');
  const [enviando, setEnviando] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();
  const set = (k) => (e) => setF({ ...f, [k]: e.target.value });

  const enviar = async (e) => {
    e.preventDefault();
    setError('');
    if (f.password !== f.confirmar) return setError('Las contraseñas no coinciden');
    setEnviando(true);
    try {
      const { confirmar, ...datos } = f;
      await registrarse(datos);
      await login(f.email, f.password, true);
      navigate('/inicio');
    } catch (err) {
      setError(mensajeError(err));
    } finally {
      setEnviando(false);
    }
  };

  return (
    <div className="login-screen">
      <div className="login-visual">
        <div className="login-visual-overlay" />
        <div className="login-visual-content">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" className="login-book-icon">
            <path d="M4 5.5A2.5 2.5 0 0 1 6.5 3H20v16H6.5A2.5 2.5 0 0 0 4 21.5V5.5Z" />
          </svg>
          <h1>Biblioteca Orduña</h1>
          <p>Crea tu cuenta para consultar el catálogo, reservar libros y ver tus préstamos.</p>
        </div>
      </div>
      <div className="login-form-side">
        <form className="login-form ancho" onSubmit={enviar}>
          <h2>Crear cuenta</h2>
          <p className="sub">Registro de lector</p>
          {error && <div className="error-form">{error}</div>}
          <div className="form-grid">
            <Campo label="Nombre"><input value={f.nombre} onChange={set('nombre')} required /></Campo>
            <Campo label="Apellido"><input value={f.apellido} onChange={set('apellido')} required /></Campo>
            <Campo label="DNI"><input value={f.dni} onChange={set('dni')} pattern="\d{8}" title="8 dígitos" maxLength={8} required /></Campo>
            <Campo label="Tipo de usuario">
              <select value={f.tipo_usuario} onChange={set('tipo_usuario')}>
                <option value="ESTUDIANTE">Estudiante</option>
                <option value="DOCENTE">Docente</option>
                <option value="EXTERNO">Externo</option>
              </select>
            </Campo>
            <Campo label="Correo" ancho={2}><input type="email" value={f.email} onChange={set('email')} required /></Campo>
            <Campo label="Teléfono"><input value={f.telefono} onChange={set('telefono')} /></Campo>
            <Campo label="Dirección"><input value={f.direccion} onChange={set('direccion')} /></Campo>
            <Campo label="Contraseña"><input type="password" value={f.password} onChange={set('password')} minLength={6} required /></Campo>
            <Campo label="Repetir contraseña"><input type="password" value={f.confirmar} onChange={set('confirmar')} minLength={6} required /></Campo>
          </div>
          <button type="submit" className="btn-login" disabled={enviando}>{enviando ? 'Creando cuenta...' : 'Crear cuenta'}</button>
          <p className="registro-link">¿Ya tienes cuenta? <Link to="/login">Inicia sesión</Link></p>
        </form>
      </div>
    </div>
  );
}

export default Registro;
