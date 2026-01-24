import { createContext, useContext, useState, useEffect } from "react";

const AuthContext = createContext();

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};

// EN DESARROLLO...
const MOCK_USERS = {
    'admin@test.com': {
        id: 1,
        nombre: 'Admin',
        email: 'admin@test.com',
        password: 'admin123',
        rol: 'ADMIN',
        centros: []
    },
    'coordinador@test.com': {
        id: 2,
        nombre: 'Coordinador',
        email: 'coordinador@test.com',
        password: 'coord123',
        rol: 'USUARIO',
        centros: [
            { centroId: 1, rolEnCentro: 'COORDINADOR' }
        ]
    },
    'profesor@test.com': {
        id: 3,
        nombre: 'Profesor',
        email: 'profesor@test.com',
        password: 'prof123',
        rol: 'USUARIO',
        centros: [
            { centroId: 1, rolEnCentro: 'PROFESOR' },
            { centroId: 2, rolEnCentro: 'PROFESOR' }
        ]
    },
    'estudiante@test.com': {
        id: 4,
        nombre: 'Estudiante',
        email: 'estudiante@test.com',
        password: 'est123',
        rol: 'USUARIO',
        centros: [
            { centroId: 1, rolEnCentro: 'ESTUDIANTE' }
        ]
    },
    'observador@test.com': {
        id: 5,
        nombre: 'Observador',
        email: 'observador@test.com',
        password: 'obs123',
        rol: 'USUARIO',
        centros: [
            { centroId: 1, rolEnCentro: 'OBSERVADOR' }
        ]
    },
    'nuevo@test.com': {
        id: 6,
        nombre: 'Nuevo',
        email: 'nuevo@test.com',
        password: 'nuevo123',
        rol: 'USUARIO',
        centros: []
    }
}

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

    // Login mock - guarda usuario (sin contraseña) en localStorage
    const login = (email, password) => {
        if (!email || !password) {
            throw new Error('Email y contraseña son requeridos');
        }
        const mockUser = MOCK_USERS[email];
        if (!mockUser) {
            throw new Error('Usuario o contraseña incorrecta');
        }
        if (mockUser.password !== password) {
            throw new Error('Usuario o contraseña incorrecta');
        }

        // Crear objeto sin password y guardar (seguridad)
        const userToSave = {
            id: mockUser.id,
            nombre: mockUser.nombre,
            email: mockUser.email,
            rol: mockUser.rol,
            centros: mockUser.centros
        };
        localStorage.setItem('user', JSON.stringify(userToSave));
        setUser(userToSave);
        return userToSave;
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
            rol: 'USUARIO',
            centros: []
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

