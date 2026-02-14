import { createContext, useContext, useState, useEffect } from "react";
import api from "../services/api";

const AuthContext = createContext();

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    // Cargar usuario del localStorage al iniciar
    useEffect(() => {
        const storedUser = localStorage.getItem('user');
        if (storedUser) {
            try {
                setUser(JSON.parse(storedUser));
            } catch (error) {
                console.error('Error parsing user from localStorage:', error);
                localStorage.removeItem('user');
            }
        }
        setLoading(false);
    }, []);

    const login = async (email, password) => {
        if (!email || !password) {
            throw new Error('Email y contraseña son requeridos');
        }

        try {
            const response = await api.post('/auth/login', { email, password });
            const userData = response.data;
            localStorage.setItem('user', JSON.stringify(userData));
            setUser(userData);
            return userData;
        } catch (error) {
            if (error.response?.status === 401) {
                throw new Error('Usuario o contraseña incorrecta');
            }
            if (error.response?.status === 403) {
                throw new Error('Usuario no activo');
            }
            throw new Error(error.response?.data?.message || 'Error al iniciar sesión');
        }
    };

    // Logout - elimina usuario del localStorage
    const logout = () => {
        localStorage.removeItem('user');
        setUser(null);
    };

    const register = async (nombre, email, password) => {
        if (!nombre || !email || !password) {
            throw new Error('Todos los campos son requeridos');
        }

        try {
            const response = await api.post('/auth/register', { nombre, email, password });
            const userData = response.data;
            localStorage.setItem('user', JSON.stringify(userData));
            setUser(userData);
            return userData;
        } catch (error) {
            if (error.response?.status === 409) {
                throw new Error('El email ya está registrado');
            }
            throw new Error(error.response?.data?.message || 'Error al registrarse');
        }
    };

    const value = {
        user,
        loading,
        login,
        logout,
        register,
        isAuthenticated: !!user,
    };

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
