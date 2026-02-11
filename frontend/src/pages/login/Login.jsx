import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import Button from "../../components/common/Button";
import Input from "../../components/common/Input";
import Alert from "../../components/common/Alert";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    // Validaciones básicas
    if (!email.trim()) {
      setError("El email es requerido");
      setLoading(false);
      return;
    }

    if (!password.trim()) {
      setError("La contraseña es requerida");
      setLoading(false);
      return;
    }

    try {
      login(email, password);
      navigate("/dashboard");
    } catch (err) {
      setError(err.message || "Error al iniciar sesión");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center px-4">
      <div className="max-w-md w-full bg-white rounded-lg shadow-md p-8">
        {/* Logo y título */}
        <div className="text-center mb-8">
          <span className="text-5xl">🌳</span>
          <h1 className="text-2xl font-bold text-gray-800 mt-2">Proyecto Árboles</h1>
          <p className="text-gray-600 mt-1">Inicia sesión en tu cuenta</p>
        </div>

        {/* Mensaje de error */}
        {error && (
          <div className="mb-4">
            <Alert
              type="error"
              message={error}
              dismissible
              onClose={() => setError("")}
            />
          </div>
        )}

        {/* Formulario */}
        <form onSubmit={handleSubmit} className="space-y-6">
          <Input
            id="email"
            label="Email"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="tu@email.com"
          />

          <Input
            id="password"
            label="Contraseña"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="••••••••"
          />

          <Button
            type="submit"
            variant="primary"
            fullWidth
            disabled={loading}
          >
            {loading ? "Iniciando sesión..." : "Iniciar sesión"}
          </Button>
        </form>

        {/* Link a Register */}
        <p className="text-center text-sm text-gray-600 mt-6">
          ¿No tienes cuenta?{" "}
          <Link to="/register" className="text-green-600 hover:text-green-700 font-medium">
            Regístrate aquí
          </Link>
        </p>
      </div>
    </div>
  );
};

export default Login;
