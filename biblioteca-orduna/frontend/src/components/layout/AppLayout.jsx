// Shell compartido por todas las páginas autenticadas: Sidebar + Topbar + contenido.
import Sidebar from './Sidebar.jsx';
import Topbar from './Topbar.jsx';

function AppLayout({ children }) {
  return (
    <div className="app-shell">
      <Sidebar />
      <div className="app-main">
        <Topbar />
        <div className="app-content">{children}</div>
      </div>
    </div>
  );
}

export default AppLayout;
