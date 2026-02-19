import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import Button from "../../components/common/Button";
import Input from "../../components/common/Input";
import Alert from "../../components/common/Alert";

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
            await register(nombre, email, password);
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
                    <h1 className="text-2xl font-bold text-gray-800 mt-2">Proyecto Árboles</h1>
                    <p className="text-gray-600 mt-1">Crea tu cuenta</p>
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
                        id="nombre"
                        label="Nombre completo"
                        type="text"
                        value={nombre}
                        onChange={(e) => setNombre(e.target.value)}
                        placeholder="Tu nombre"
                    />

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
                        placeholder="Mínimo 6 caracteres"
                    />

                    <Input
                        id="confirmPassword"
                        label="Confirmar contraseña"
                        type="password"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        placeholder="Repite tu contraseña"
                    />

                    <Button
                        type="submit"
                        variant="primary"
                        fullWidth
                        disabled={loading}
                    >
                        {loading ? "Registrando..." : "Registrarse"}
                    </Button>
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