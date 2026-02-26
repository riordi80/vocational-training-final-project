import { Link } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

const Dashboard = () => {
  const { user } = useAuth();

  return (
    <div className="max-w-4xl mx-auto">
      {/* Bienvenida */}
      <div className="bg-white rounded-lg shadow-md p-6 mb-6">
        <h1 className="text-3xl font-bold text-brand-accent mb-2">
          {user ? `¡Bienvenido, ${user.nombre}!` : '¡Bienvenido a Proyecto Árboles!'}
        </h1>
        <p className="text-brand-text">
          {user
            ? 'Gestiona los árboles y centros educativos desde tu panel de control.'
            : 'Explora los árboles y centros educativos. Inicia sesión para gestionar contenido.'}
        </p>
      </div>

      {/* Tarjetas de acceso rápido */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {/* Tarjeta Árboles */}
        <Link
          to="/arboles"
          className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition group"
        >
          <div className="flex items-center mb-4">
            <span className="text-4xl mr-4">🌳</span>
            <h2 className="text-2xl font-bold text-brand-accent group-hover:text-brand-primary transition">
              Árboles
            </h2>
          </div>
          <p className="text-brand-text">
            Consulta, añade y gestiona los árboles monitorizados en los diferentes centros educativos.
          </p>
        </Link>

        {/* Tarjeta Centros */}
        <Link
          to="/centros"
          className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition group"
        >
          <div className="flex items-center mb-4">
            <span className="text-4xl mr-4">🏫</span>
            <h2 className="text-2xl font-bold text-brand-accent group-hover:text-brand-primary transition">
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
