import { useAuth } from "../../context/AuthContext";
import { hasRoleInCenter } from "../../utils/permissions";

const RoleGate = ({
  children,
  roles,
  centroId,
  fallback
}) => {
  const { user } = useAuth();

  let hasPermission = false;

  if (centroId) {
    // Verificar rol en el centro    
    hasPermission = hasRoleInCenter(user, centroId, roles);
  } else if (roles) {
    // Verificar rol global
    hasPermission = user?.rol === 'ADMIN' || roles.includes(user?.rol);
  }

  if (hasPermission) {
    return children;
  }

  return fallback;
  };

export default RoleGate;