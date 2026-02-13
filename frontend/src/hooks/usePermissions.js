import { useAuth } from '../context/AuthContext';
import { isAdminUser, canCreateArbol, canEditArbol, canDeleteArbol, canManageCenter } from '../utils/permissions';

export function usePermissions() {
  const { user } = useAuth();

  return {
    isAdmin: () => user ? isAdminUser(user) : false,
    canCreateArbol: (centroId) => user ? canCreateArbol(user, centroId) : false,
    canEditArbol: (centroId) => user ? canEditArbol(user, centroId) : false,
    canDeleteArbol: (centroId) => user ? canDeleteArbol(user, centroId) : false,
    canManageCenter: (centroId) => user ? canManageCenter(user, centroId) : false,
  };
}
