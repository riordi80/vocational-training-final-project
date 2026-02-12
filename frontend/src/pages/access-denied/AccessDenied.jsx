import { useNavigate } from "react-router-dom";
import Button from "../../components/common/Button";

const AccessDenied = () => {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center px-4">
      <div className="max-w-md w-full bg-white rounded-lg shadow-md p-8">
        {/* Icono y título */}
        <div className="text-center mb-8">
          <span className="text-5xl">🚫</span>
          <h1 className="text-2xl font-bold text-gray-800 mt-2">Acceso Denegado</h1>
          <p className="text-gray-600 mt-1">No tiene suficientes permisos para acceder a este contenido.</p>
        </div>

        {/* Botón volver */}
        <Button
          variant="primary"
          fullWidth
          onClick={() => navigate("/dashboard")}
        >
          Volver al Dashboard
        </Button>
      </div>
    </div>
  );
};

export default AccessDenied
