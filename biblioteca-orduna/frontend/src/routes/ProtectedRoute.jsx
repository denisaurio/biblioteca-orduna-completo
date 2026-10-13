// Protege rutas: exige sesión y, si se indica, un rol.
// soloAdmin = bibliotecario o gerente · soloGerente = gerente.
import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';

function ProtectedRoute({ children, soloAdmin = false, soloGerente = false }) {
  const { usuario, esAdmin, esGerente } = useAuth();

  if (!usuario) return <Navigate to="/login" replace />;
  if (soloAdmin && !esAdmin) return <Navigate to="/inicio" replace />;
  if (soloGerente && !esGerente) return <Navigate to="/inicio" replace />;

  return children;
}

export default ProtectedRoute;
