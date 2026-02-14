import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import ProtectedRoute from './routes/ProtectedRoute';
import MainLayout from './layout/MainLayout';
import Login from './pages/login/Login';
import Register from './pages/register/Register';
import Dashboard from './pages/dashboard/Dashboard';
import ComponentLibrary from './pages/component-library/ComponentLibrary';
import ListadoArboles from './pages/arboles/ListadoArboles';
import DetalleArbol from './pages/arboles/DetalleArbol';
import FormularioArbol from './pages/arboles/FormularioArbol';
import ListadoCentros from './pages/centros/ListadoCentros';
import DetalleCentro from './pages/centros/DetalleCentro';
import FormularioCentro from './pages/centros/FormularioCentro';
import ListadoUsuarios from './pages/usuarios/ListadoUsuarios';
import DetalleUsuario from './pages/usuarios/DetalleUsuario';
import FormularioUsuario from './pages/usuarios/FormularioUsuario';
import AccessDenied from './pages/access-denied/AccessDenied';
import './App.css';

function App() {
  return (
    <Router>
      <Routes>
        {/* Redirección raíz - siempre a dashboard */}
        <Route path="/" element={<Navigate to="/dashboard" replace />} />

        {/* Rutas de autenticación */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        {/* Biblioteca de componentes - Documentación */}
        <Route path="/component-library" element={<ComponentLibrary />} />

        {/* Rutas públicas de lectura con layout */}
        <Route element={<MainLayout />}>
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/arboles" element={<ListadoArboles />} />
          <Route path="/arboles/:id" element={<DetalleArbol />} />
          <Route path="/centros" element={<ListadoCentros />} />
          <Route path="/centros/:id" element={<DetalleCentro />} />
          <Route path="/access-denied" element={<AccessDenied />} />

          {/* Rutas protegidas de escritura */}
          <Route path="/arboles/nuevo" element={
            <ProtectedRoute requiredRoles={['ADMIN', 'COORDINADOR']}>
              <FormularioArbol />
            </ProtectedRoute>
          } />
          <Route path="/arboles/:id/editar" element={
            <ProtectedRoute requiredRoles={['ADMIN', 'COORDINADOR']}>
              <FormularioArbol />
            </ProtectedRoute>
          } />
          <Route path="/centros/nuevo" element={
            <ProtectedRoute requiredRoles={['ADMIN']}>
              <FormularioCentro />
            </ProtectedRoute>
          } />
          <Route path="/centros/:id/editar" element={
            <ProtectedRoute requiredRoles={['ADMIN', 'COORDINADOR']}>
              <FormularioCentro />
            </ProtectedRoute>
          } />

          {/* Rutas protegidas de usuarios (solo ADMIN) */}
          <Route path="/usuarios" element={
            <ProtectedRoute requiredRoles={['ADMIN']}>
              <ListadoUsuarios />
            </ProtectedRoute>
          } />
          <Route path="/usuarios/nuevo" element={
            <ProtectedRoute requiredRoles={['ADMIN']}>
              <FormularioUsuario />
            </ProtectedRoute>
          } />
          <Route path="/usuarios/:id" element={
            <ProtectedRoute requiredRoles={['ADMIN']}>
              <DetalleUsuario />
            </ProtectedRoute>
          } />
          <Route path="/usuarios/:id/editar" element={
            <ProtectedRoute requiredRoles={['ADMIN']}>
              <FormularioUsuario />
            </ProtectedRoute>
          } />
        </Route>

        {/* 404 */}
        <Route path="*" element={<div className="p-8 text-center"><h1 className="text-2xl font-bold text-red-500">404 - Página no encontrada</h1></div>} />
      </Routes>
    </Router>
  );
}

export default App;
