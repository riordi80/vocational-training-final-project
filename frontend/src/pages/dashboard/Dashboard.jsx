import { Link } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

const Dashboard = () => {
  const { user } = useAuth();

  return (
    <div className="max-w-4xl mx-auto">
      {/* Bienvenida */}
      <div className="bg-white rounded-lg shadow-md p-6 mb-6">
        <h1 className="text-3xl font-bold text-gray-800 mb-2">
          ¡Bienvenido, {user?.nombre}!
        </h1>
        <p className="text-gray-600">
          Gestiona los árboles y centros educativos desde tu panel de control.
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
            <h2 className="text-2xl font-bold text-gray-800 group-hover:text-green-600 transition">
              Árboles
            </h2>
          </div>
          <p className="text-gray-600">
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
            <h2 className="text-2xl font-bold text-gray-800 group-hover:text-green-600 transition">
              Centros Educativos
            </h2>
          </div>
          <p className="text-gray-600">
            Administra los centros educativos y consulta los árboles asociados a cada uno.
          </p>
        </Link>
      </div>

      {/* Info del usuario */}
      <div className="bg-green-50 border border-green-200 rounded-lg p-4 mt-6">
        <p className="text-sm text-gray-700">
          <span className="font-medium">Rol:</span> {user?.rol} |
          <span className="font-medium ml-3">Email:</span> {user?.email}
        </p>
      </div>
    </div>
  );
};

export default Dashboard;
