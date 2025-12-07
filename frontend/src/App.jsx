import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from './context/AuthContext';
import ProtectedRoute from './components/common/ProtectedRoute';
import MainLayout from './components/layout/MainLayout';
import Login from './pages/login/Login';
import Register from './pages/register/Register';
import Dashboard from './pages/dashboard/Dashboard';
import ComponentLibrary from './pages/component-library/ComponentLibrary';
import ListadoArboles from './pages/arboles/ListadoArboles';
import DetalleArbol from './pages/arboles/DetalleArbol';
import FormularioArbol from './pages/arboles/FormularioArbol';
import './App.css';

function App() {
  const { user } = useAuth();

  return (
    <Router>
      <Routes>
        {/* Redirección raíz */}
        <Route
          path="/"
          element={user ? <Navigate to="/dashboard" replace /> : <Navigate to="/login" replace />}
        />

        {/* Rutas públicas */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        {/* Biblioteca de componentes - Documentación */}
        <Route path="/component-library" element={<ComponentLibrary />} />

        {/* Rutas protegidas con layout */}
        <Route
          element={
            <ProtectedRoute>
              <MainLayout />
            </ProtectedRoute>
          }
        >
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/arboles" element={<ListadoArboles />} />
          <Route path="/arboles/nuevo" element={<FormularioArbol />} />
          <Route path="/arboles/:id/editar" element={<FormularioArbol />} />
          <Route path="/arboles/:id" element={<DetalleArbol />} />
          <Route path="/centros" element={<div className="text-center"><h1 className="text-2xl font-bold">Listado de Centros (próximamente)</h1></div>} />
        </Route>

        {/* 404 */}
        <Route path="*" element={<div className="p-8 text-center"><h1 className="text-2xl font-bold text-red-500">404 - Página no encontrada</h1></div>} />
      </Routes>
    </Router>
  );
}

export default App;
