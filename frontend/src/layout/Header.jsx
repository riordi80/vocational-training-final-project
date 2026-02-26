import { useState } from "react";
import { Link, NavLink, useNavigate } from "react-router-dom";
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

    const navLinkClass = ({ isActive }) =>
        isActive
            ? "text-brand-secondary uppercase tracking-wide font-semibold transition"
            : "text-brand-accent uppercase tracking-wide font-semibold hover:text-brand-secondary transition";

    return (
        <header className="bg-brand-primary text-white shadow-md">
            <div className="container mx-auto px-4 py-4">
                <div className="flex items-center justify-between gap-8">
                    {/* Logos */}
                    <div className="flex items-center gap-4">
                        <Link to="/dashboard">
                            <img
                                src="/Logotipo-Proy-arboles_color_v2.png"
                                alt="Proyecto Árboles"
                                className="h-12 w-auto shrink-0"
                            />
                        </Link>
                        <div className="hidden xl:flex items-center gap-4 border-l border-white/30 pl-4">
                            <img src="/pa-fsa-blanco.png"      alt="Fundación Sergio Alonso" className="h-5" />
                            <img src="/pa-foresta-blanco.png"  alt="Foresta"                 className="h-5" />
                            <img src="/pa-acuorum-blanco.png"  alt="Acuorum"                 className="h-5" />
                        </div>
                    </div>

                    {/* Navegación desktop */}
                    <nav className="hidden md:flex items-center space-x-4">
                        <NavLink to="/dashboard" className={navLinkClass}>
                            Dashboard
                        </NavLink>
                        <NavLink to="/arboles" className={navLinkClass}>
                            Árboles
                        </NavLink>
                        <NavLink to="/centros" className={navLinkClass}>
                            Centros
                        </NavLink>
                        {user?.rol === 'ADMIN' && (
                            <NavLink to="/usuarios" className={navLinkClass}>
                                Usuarios
                            </NavLink>
                        )}

                        {user ? (
                            <>
                                {/* Info de usuario — solo en xl */}
                                <div className="hidden xl:block text-right">
                                    <p className="text-sm font-medium">{user.nombre}</p>
                                    <p className="text-xs text-white/70">{user.rol}</p>
                                </div>

                                {/* Logout: icono en md, texto en xl */}
                                <button
                                    onClick={handleLogout}
                                    className="xl:hidden bg-brand-accent hover:bg-brand-primary text-white p-1.5 rounded-md transition focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-brand-accent"
                                    aria-label="Cerrar sesión"
                                >
                                    <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15.75 9V5.25A2.25 2.25 0 0 0 13.5 3h-6a2.25 2.25 0 0 0-2.25 2.25v13.5A2.25 2.25 0 0 0 7.5 21h6a2.25 2.25 0 0 0 2.25-2.25V15M12 9l-3 3m0 0 3 3m-3-3h12.75" />
                                    </svg>
                                </button>
                                <div className="hidden xl:block">
                                    <Button onClick={handleLogout} variant="secondary" size="sm">
                                        Cerrar sesión
                                    </Button>
                                </div>
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
                        className="md:hidden p-2 rounded-md hover:bg-white/10 transition focus:outline-none focus:ring-2 focus:ring-white"
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
                    <div className="md:hidden mt-4 pb-4 border-t border-white/20 pt-4">
                        <nav className="flex flex-col space-y-3">
                            {/* Info de usuario */}
                            {user && (
                                <div className="pb-3 border-b border-white/20">
                                    <p className="text-sm font-medium">{user.nombre}</p>
                                    <p className="text-xs text-white/70">{user.rol}</p>
                                </div>
                            )}

                            {/* Links de navegación */}
                            <NavLink
                                to="/dashboard"
                                onClick={closeMobileMenu}
                                className={({ isActive }) => `${navLinkClass({ isActive })} py-2`}
                            >
                                Dashboard
                            </NavLink>
                            <NavLink
                                to="/arboles"
                                onClick={closeMobileMenu}
                                className={({ isActive }) => `${navLinkClass({ isActive })} py-2`}
                            >
                                Árboles
                            </NavLink>
                            <NavLink
                                to="/centros"
                                onClick={closeMobileMenu}
                                className={({ isActive }) => `${navLinkClass({ isActive })} py-2`}
                            >
                                Centros
                            </NavLink>
                            {user?.rol === 'ADMIN' && (
                                <NavLink
                                    to="/usuarios"
                                    onClick={closeMobileMenu}
                                    className={({ isActive }) => `${navLinkClass({ isActive })} py-2`}
                                >
                                    Usuarios
                                </NavLink>
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
