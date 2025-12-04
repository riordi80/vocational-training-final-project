import { createContext, useContext, useState, useEffect } from "react";

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

    // Login mock - guarda usuario en localStorage
    const login = (email, password) => {
        // Validación básica mock
        if (!email || !password) {
            throw new Error('Email y contraseña son requeridos');
        }

        // Simular usuario (en producción esto vendría del backend)
        const mockUser = {
            id: 1,
            nombre: email.split('@')[0],
            email: email,
            rol: 'ADMIN', // Mock: siempre admin
        };

        localStorage.setItem('user', JSON.stringify(mockUser));
        setUser(mockUser);
        return mockUser;
    };

    // Logout - elimina usuario del localStorage
    const logout = () => {
        localStorage.removeItem('user');
        setUser(null);
    };

    // Register mock - guarda usuario en localStorage
    const register = (nombre, email, password) => {
        if (!nombre || !email || !password) {
            throw new Error('Todos los campos son requeridos');
        }

        // Simular registro exitoso
        const mockUser = {
            id: Date.now(),
            nombre: nombre,
            email: email,
            rol: 'PROFESOR', // Mock: nuevos usuarios son profesores
        };

        localStorage.setItem('user', JSON.stringify(mockUser));
        setUser(mockUser);
        return mockUser;
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

