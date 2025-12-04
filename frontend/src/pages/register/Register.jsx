  import { useState } from "react";
  import { useNavigate, Link } from "react-router-dom";
  import { useAuth } from "../../context/AuthContext";

  const Register = () => {
      const [nombre, setNombre] = useState("");
      const [email, setEmail] = useState("");
      const [password, setPassword] = useState("");
      const [confirmPassword, setConfirmPassword] = useState("");
      const [error, setError] = useState("");
      const [loading, setLoading] = useState(false);

      const { register } = useAuth();
      const navigate = useNavigate();

      const handleSubmit = async (e) => {
          e.preventDefault();
          setError("");
          setLoading(true);

          // Validaciones
          if (!nombre.trim()) {
              setError("El nombre es requerido");
              setLoading(false);
              return;
          }

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

          if (password.length < 6) {
              setError("La contraseña debe tener al menos 6 caracteres");
              setLoading(false);
              return;
          }

          if (password !== confirmPassword) {
              setError("Las contraseñas no coinciden");
              setLoading(false);
              return;
          }

          try {
              register(nombre, email, password);
              navigate("/dashboard");
          } catch (err) {
              setError(err.message || "Error al registrarse");
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
                      <h1 className="text-2xl font-bold text-gray-800 mt-2">Garden Monitor</h1>
                      <p className="text-gray-600 mt-1">Crea tu cuenta</p>
                  </div>

                  {/* Mensaje de error */}
                  {error && (
                      <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
                          {error}
                      </div>
                  )}

                  {/* Formulario */}
                  <form onSubmit={handleSubmit} className="space-y-6">
                      <div>
                          <label htmlFor="nombre" className="block text-sm font-medium text-gray-700 mb-1">
                              Nombre completo
                          </label>
                          <input
                              id="nombre"
                              type="text"
                              value={nombre}
                              onChange={(e) => setNombre(e.target.value)}
                              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                              placeholder="Tu nombre"
                          />
                      </div>

                      <div>
                          <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-1">
                              Email
                          </label>
                          <input
                              id="email"
                              type="email"
                              value={email}
                              onChange={(e) => setEmail(e.target.value)}
                              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                              placeholder="tu@email.com"
                          />
                      </div>

                      <div>
                          <label htmlFor="password" className="block text-sm font-medium text-gray-700 mb-1">
                              Contraseña
                          </label>
                          <input
                              id="password"
                              type="password"
                              value={password}
                              onChange={(e) => setPassword(e.target.value)}
                              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                              placeholder="Mínimo 6 caracteres"
                          />
                      </div>

                      <div>
                          <label htmlFor="confirmPassword" className="block text-sm font-medium text-gray-700 mb-1">
                              Confirmar contraseña
                          </label>
                          <input
                              id="confirmPassword"
                              type="password"
                              value={confirmPassword}
                              onChange={(e) => setConfirmPassword(e.target.value)}
                              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                              placeholder="Repite tu contraseña"
                          />
                      </div>

                      <button
                          type="submit"
                          disabled={loading}
                          className="w-full bg-green-600 hover:bg-green-700 text-white font-medium py-2 px-4 rounded-md transition disabled:bg-gray-400 disabled:cursor-not-allowed"
                      >
                          {loading ? "Registrando..." : "Registrarse"}
                      </button>
                  </form>

                  {/* Link a Login */}
                  <p className="text-center text-sm text-gray-600 mt-6">
                      ¿Ya tienes cuenta?{" "}
                      <Link to="/login" className="text-green-600 hover:text-green-700 font-medium">
                          Inicia sesión aquí
                      </Link>
                  </p>
              </div>
          </div>
      );
  };

  export default Register;