import { Link } from "react-router-dom";
import { useState, useEffect } from "react";
import { useAuth } from "../../context/AuthContext";
import { School, TreePine } from "lucide-react";
import { getCentros } from "../../services/centrosService";
import { getArboles } from "../../services/arbolesService";

const Dashboard = () => {
  const { user } = useAuth();
  const [numCentros, setNumCentros] = useState(null);
  const [numArboles, setNumArboles] = useState(null);

  useEffect(() => {
    getCentros().then(data => setNumCentros(data.length)).catch(() => setNumCentros('—'));
    getArboles().then(data => setNumArboles(data.length)).catch(() => setNumArboles('—'));
  }, []);

  return (
    <div className="max-w-4xl mx-auto">
      {/* Bienvenida */}
      <div className="bg-white rounded-lg shadow-md p-6 mb-6">
        <h1 className="text-3xl font-bold text-brand-primary mb-2">
          {user ? `¡Bienvenido, ${user.nombre}!` : '¡Bienvenido a Proyecto Árboles!'}
        </h1>
        <p className="text-brand-text">
          {user
            ? 'Gestiona los árboles y centros educativos desde tu panel de control.'
            : 'Explora los árboles y centros educativos. Inicia sesión para gestionar contenido.'}
        </p>
      </div>

      {/* Métricas */}
      <div className="bg-brand-primary rounded-lg shadow-md px-8 py-5 mb-6 flex divide-x divide-white/20">
        <div className="flex items-center gap-3 pr-8">
          <School className="w-7 h-7 text-brand-bg-green shrink-0" />
          <div>
            <p className="text-2xl font-bold text-white leading-none">
              {numCentros ?? '…'}
            </p>
            <p className="text-xs text-white/60 mt-1">Centros educativos</p>
          </div>
        </div>
        <div className="flex items-center gap-3 pl-8">
          <TreePine className="w-7 h-7 text-brand-bg-green shrink-0" />
          <div>
            <p className="text-2xl font-bold text-white leading-none">
              {numArboles ?? '…'}
            </p>
            <p className="text-xs text-white/60 mt-1">Árboles plantados</p>
          </div>
        </div>
      </div>

      {/* Tarjetas de acceso rápido */}
      <div className="grid grid-cols-1 gap-6">
        {/* Tarjeta Centros */}
        <Link
          to="/centros"
          className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition group"
        >
          <div className="flex items-center mb-4">
            <School className="w-10 h-10 mr-4 text-brand-secondary shrink-0" />
            <h2 className="text-2xl font-bold text-brand-primary transition">
              Centros Educativos
            </h2>
          </div>
          <p className="text-brand-text">
            Administra los centros educativos y consulta los árboles asociados a cada uno.
          </p>
        </Link>
      </div>

      {/* Info del usuario */}
      {user && (
        <div className="bg-white rounded-lg shadow-md p-4 mt-6 text-center">
          <p className="text-sm text-gray-700">
            <span className="font-medium">Rol:</span> {user.rol} |
            <span className="font-medium ml-3">Email:</span> {user.email}
          </p>
        </div>
      )}
    </div>
  );
};

export default Dashboard;
