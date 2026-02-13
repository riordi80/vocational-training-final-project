import { ROLES } from "../constants/roles";

export function isAdminUser(user) {
  if (!user) return false;
  return user.rol === ROLES.ADMIN;
}

export function isCoordinadorInCenter(user, centroId) {
  if (!user) return false;
  if (isAdminUser(user)) return true;
  return user.centros?.some(c => c.centroId === centroId);
}

export function canCreateArbol(user, centroId) {
  return isCoordinadorInCenter(user, centroId);
}

export function canEditArbol(user, centroId) {
  return isCoordinadorInCenter(user, centroId);
}

export function canDeleteArbol(user, centroId) {
  return isCoordinadorInCenter(user, centroId);
}

export function canManageCenter(user, centroId) {
  return isCoordinadorInCenter(user, centroId);
}
