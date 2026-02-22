import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import Button from "../components/common/Button";

const Header = () => {
    const { user, logout } = useAuth();
    const navigate = useNavigate();
    const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

    const handleLogout = () => {
        logout();
        navigate('/dashboard');
        setMobileMenuOpen(false);
    };

    const closeMobileMenu = () => {
        setMobileMenuOpen(false);
    };

    return (
        <header className="bg-green-600 text-white shadow-md">
            <div className="container mx-auto px-4 py-4">
                <div className="flex items-center justify-between">
                    {/* Logo y título */}
                    <Link to="/dashboard" className="flex items-center space-x-2">
                        <span className="text-2xl">🌳</span>
                        <h1 className="text-xl font-bold">Proyecto Árboles</h1>
                    </Link>

                    {/* Navegación desktop */}
                    <nav className="hidden md:flex items-center space-x-6">
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
                        {user?.rol === 'ADMIN' && (
                            <Link
                                to="/usuarios"
                                className="hover:text-green-200 transition"
                            >
                                Usuarios
                            </Link>
                        )}

                        {user ? (
                            <>
                                {/* Info de usuario */}
                                <div className="text-right">
                                    <p className="text-sm font-medium">{user.nombre}</p>
                                    <p className="text-xs text-green-200">{user.rol}</p>
                                </div>

                                {/* Botón logout */}
                                <Button
                                    onClick={handleLogout}
                                    variant="secondary"
                                    size="sm"
                                >
                                    Cerrar sesión
                                </Button>
                            </>
                        ) : (
                            <Button
                                onClick={() => navigate('/login')}
                                variant="secondary"
                                size="sm"
                            >
                                Iniciar sesión
                            </Button>
                        )}
                    </nav>

                    {/* Botón hamburguesa móvil */}
                    <button
                        onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
                        className="md:hidden p-2 rounded-md hover:bg-green-700 transition focus:outline-none focus:ring-2 focus:ring-white"
                        aria-label="Toggle menu"
                    >
                        {mobileMenuOpen ? (
                            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                            </svg>
                        ) : (
                            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
                            </svg>
                        )}
                    </button>
                </div>

                {/* Menú móvil desplegable */}
                {mobileMenuOpen && (
                    <div className="md:hidden mt-4 pb-4 border-t border-green-500 pt-4">
                        <nav className="flex flex-col space-y-3">
                            {/* Info de usuario */}
                            {user && (
                                <div className="pb-3 border-b border-green-500">
                                    <p className="text-sm font-medium">{user.nombre}</p>
                                    <p className="text-xs text-green-200">{user.rol}</p>
                                </div>
                            )}

                            {/* Links de navegación */}
                            <Link
                                to="/dashboard"
                                onClick={closeMobileMenu}
                                className="hover:text-green-200 transition py-2"
                            >
                                Dashboard
                            </Link>
                            <Link
                                to="/arboles"
                                onClick={closeMobileMenu}
                                className="hover:text-green-200 transition py-2"
                            >
                                Árboles
                            </Link>
                            <Link
                                to="/centros"
                                onClick={closeMobileMenu}
                                className="hover:text-green-200 transition py-2"
                            >
                                Centros
                            </Link>
                            {user?.rol === 'ADMIN' && (
                                <Link
                                    to="/usuarios"
                                    onClick={closeMobileMenu}
                                    className="hover:text-green-200 transition py-2"
                                >
                                    Usuarios
                                </Link>
                            )}

                            {user ? (
                                <Button
                                    onClick={handleLogout}
                                    variant="secondary"
                                    size="sm"
                                    fullWidth
                                >
                                    Cerrar sesión
                                </Button>
                            ) : (
                                <Button
                                    onClick={() => { navigate('/login'); closeMobileMenu(); }}
                                    variant="secondary"
                                    size="sm"
                                    fullWidth
                                >
                                    Iniciar sesión
                                </Button>
                            )}
                        </nav>
                    </div>
                )}
            </div>
        </header>
    );
};

export default Header;
