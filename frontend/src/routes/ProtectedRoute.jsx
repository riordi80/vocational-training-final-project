import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { hasRoleInCenter } from "../utils/permissions";

const ProtectedRoute = ({
    children,
    requiredRoles,
    centroId,
    requiredCenterRoles

 }) => {
    const { user, loading } = useAuth();

    if (loading) {
        return (
            <div className="flex items-center justify-center min-h-screen">
                <div className="text-center">
                    <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-green-600 mx-auto"></div>
                    <p className="mt-4 text-gray-600">Cargando...</p>
                </div>
            </div>
        );
    }

    if (!user) {
        return <Navigate to="/login" replace />;
    }

    if (requiredRoles && requiredRoles.length > 0) {
        const hasRole = user.rol === 'ADMIN' || requiredRoles.includes(user.rol);
        if (!hasRole) {
            return <Navigate to="/access-denied" replace />;
        }
    }

    if (centroId && requiredCenterRoles) {
        const hasPermission = hasRoleInCenter(user, centroId, requiredCenterRoles);
        if (!hasPermission) {
            return <Navigate to="/access-denied" replace />;
        }
    }

    return children;
};

export default ProtectedRoute;
