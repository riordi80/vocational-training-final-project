import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import './App.css';

function App() {
  return (
    <Router>
      <div className="min-h-screen bg-gray-100">
        <Routes>
          {/* Ruta por defecto - redirige a /login */}
          <Route path="/" element={<Navigate to="/login" replace />} />

          {/* Ruta de las páginas */}
          <Route path="/login" element={<div className="p-8 text-center"><h1 className="text-2xl font-bold">Login Page</h1></div>} />
          <Route path="/dashboard" element={<div className="p-8 text-center"><h1 className="text-2xl font-bold">Dashboard</h1></div>} />

          {/* Ruta 404 */}
          <Route path="*" element={<div className="p-8 text-center"><h1 className="text-2xl font-bold text-red-500">404 - Página no encontrada</h1></div>} />
          
        </Routes>
      </div>
    </Router>
  );
}

export default App;
