import { useAuth } from "../../context/AuthContext";

const RoleGate = ({ children, roles, fallback }) => {
  const { user } = useAuth();

  if (!user || !roles) return fallback;

  const hasPermission = user.rol === 'ADMIN' || roles.includes(user.rol);

  if (hasPermission) {
    return children;
  }

  return fallback;
};

export default RoleGate;
