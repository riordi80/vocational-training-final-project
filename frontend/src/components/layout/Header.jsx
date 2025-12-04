import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

const Header = () => {
    const { user, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    return (
        <header className="bg-green-600 text-white shadow-md">
            <div className="container mx-auto px-4 py-4 flex items-center justify-between">
                {/* Logo y título */}
                <Link to="/dashboard" className="flex items-center space-x-2">
                    <span className="text-2xl">🌳</span>
                    <h1 className="text-xl font-bold">Garden Monitor</h1>
                </Link>

                {/* Navegación y usuario */}
                <div className="flex items-center space-x-6">
                    {user && (
                        <>
                            {/* Links de navegación */}
                            <nav className="hidden md:flex space-x-4">
                                <Link
                                    to="/dashboard"
                                    className="hover:text-green-200 transition"
                                >
                                    Dashboard
                                </Link>
                                <Link
                                    to="/arboles"
                                    className="hover:text-green-200 transition"
                                >
                                    Árboles
                                </Link>
                                <Link
                                    to="/centros"
                                    className="hover:text-green-200 transition"
                                >
                                    Centros
                                </Link>
                            </nav>

                            {/* Info de usuario */}
                            <div className="flex items-center space-x-4">
                                <div className="text-right hidden md:block">
                                    <p className="text-sm font-medium">{user.nombre}</p>
                                    <p className="text-xs text-green-200">{user.rol}</p>
                                </div>

                                {/* Botón logout */}
                                <button
                                    onClick={handleLogout}
                                    className="bg-green-700 hover:bg-green-800 px-4 py-2 rounded-md text-sm font-medium transition"
                                >
                                    Cerrar sesión
                                </button>
                            </div>
                        </>
                    )}
                </div>
            </div>
        </header>
    );
};

export default Header;
