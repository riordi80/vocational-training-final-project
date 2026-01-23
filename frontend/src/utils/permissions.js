import { ROLES, ROLES_CENTRO } from "../constants/roles";

export function isAdminUser(user) {
  return user.rol === ROLES.ADMIN;
}

export function hasRoleInCenter(user, centroId, roles) {
  if (isAdminUser(user)) {
    return true;
  }
  const asignacion = user.centros.find(c => c.centroId === centroId);
  if (!asignacion) {
    return false;
  }
  return roles.includes(asignacion.rolEnCentro);
}

export function canCreateArbol(user, centroId) {
  return hasRoleInCenter(user, centroId, [ROLES_CENTRO.COORDINADOR, ROLES_CENTRO.PROFESOR]);
}

export function canEditArbol(user, centroId) {
  return hasRoleInCenter(user, centroId, [ROLES_CENTRO.COORDINADOR, ROLES_CENTRO.PROFESOR]);
}

export function canDeleteArbol(user, centroId) {
  return hasRoleInCenter(user, centroId, [ROLES_CENTRO.COORDINADOR]);
}

export function canManageCenter(user, centroId) {
  return hasRoleInCenter(user, centroId, [ROLES_CENTRO.COORDINADOR]);
}

export function canAssignUsersToCenter(user, centroId){
  return hasRoleInCenter(user, centroId, [ROLES_CENTRO.COORDINADOR]);
}