// Réplica del prototipo Figma "LOGIN", conectada a POST /api/auth/login (RF05).
// El backend devuelve el JWT y el rol; según el rol se muestra el Inicio correspondiente.
import { useState } from 'react';
import { Link, Navigate, useNavigate, useSearchParams } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext.jsx';
import { mensajeError } from '../../api/axiosClient.js';
import './Login.css';

function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [verPassword, setVerPassword] = useState(false);
  const [recordar, setRecordar] = useState(true);
  const [error, setError] = useState('');
  const [enviando, setEnviando] = useState(false);
  const [ayuda, setAyuda] = useState(false);
  const { login, usuario } = useAuth();
  const navigate = useNavigate();
  const [params] = useSearchParams();

  if (usuario) return <Navigate to="/inicio" replace />;

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setEnviando(true);
    try {
      await login(email.trim(), password, recordar);
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
          <p>Un espacio para descubrir, aprender y compartir</p>
        </div>
      </div>

      <div className="login-form-side">
        <form className="login-form" onSubmit={handleSubmit}>
          <h2>Iniciar sesión</h2>
          <p className="sub">Acceda a su cuenta para gestionar la biblioteca</p>

          {params.get('expirada') && !error && (
            <div className="info-box" style={{ marginBottom: 6 }}>Su sesión expiró. Vuelva a iniciar sesión.</div>
          )}
          {error && <div className="error-form">{error}</div>}

          <label>Usuario</label>
          <input
            type="email"
            placeholder="Ingresa tu correo"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            autoComplete="username"
            required
          />

          <label>Contraseña</label>
          <div className="password-wrap">
            <input
              type={verPassword ? 'text' : 'password'}
              placeholder="Ingresa tu contraseña"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              autoComplete="current-password"
              required
            />
            <button type="button" className="toggle-pass" onClick={() => setVerPassword(!verPassword)}
                    aria-label={verPassword ? 'Ocultar contraseña' : 'Mostrar contraseña'}>
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.6">
                <path d="M2 12s3.5-7 10-7 10 7 10 7-3.5 7-10 7-10-7-10-7Z" />
                <circle cx="12" cy="12" r="3" />
                {verPassword && <path d="M3 3l18 18" />}
              </svg>
            </button>
          </div>

          <label className="checkbox-row">
            <input type="checkbox" checked={recordar} onChange={(e) => setRecordar(e.target.checked)} /> Recordar sesión
          </label>

          <button type="submit" className="btn-login" disabled={enviando}>
            {enviando ? 'Ingresando...' : 'Iniciar sesión'}
          </button>

          <a href="#" className="forgot-link" onClick={(e) => { e.preventDefault(); setAyuda(!ayuda); }}>
            ¿Olvidaste tu contraseña?
          </a>
          {ayuda && (
            <div className="info-box" style={{ marginTop: 12 }}>
              Acércate al mostrador de la biblioteca con tu DNI: el bibliotecario puede asignarte una nueva contraseña.
            </div>
          )}
          <p className="registro-link">¿Eres nuevo? <Link to="/registro">Crea tu cuenta de lector</Link></p>
        </form>
      </div>
    </div>
  );
}

export default Login;
