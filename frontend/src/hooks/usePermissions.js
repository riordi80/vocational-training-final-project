import { useAuth } from '../context/AuthContext';
import { isAdminUser, canCreateArbol, canEditArbol, canDeleteArbol, canManageCenter, canAssignUsersToCenter } from '../utils/permissions';

export function usePermissions() {
  const { user } = useAuth();

  return {
    isAdmin: () => isAdminUser(user),
    canCreateArbol: (centroId) => canCreateArbol(user, centroId),
    canEditArbol: (centroId) => canEditArbol(user, centroId),
    canDeleteArbol: (centroId) => canDeleteArbol(user, centroId),
    canManageCenter: (centroId) => canManageCenter(user, centroId),
    canAssignUsersToCenter: (centroId) => canAssignUsersToCenter(user, centroId),
  };
}