import { isAdminUser, isCoordinadorInCenter } from '../utils/permissions';
import { ROLES } from '../constants/roles';

describe('permissions', () => {

  describe('isAdminUser', () => {
    // Test 1
    it('devuelve true cuando el usuario tiene rol ADMIN', () => {
      // Arrange
      const user = { rol: ROLES.ADMIN };
      // Act
      const result = isAdminUser(user);
      // Assert
      expect(result).toBe(true);
    });

    // Test 2
    it('devuelve false cuando el usuario tiene rol COORDINADOR', () => {
      // Arrange
      const user = { rol: ROLES.COORDINADOR };
      // Act & Assert
      expect(isAdminUser(user)).toBe(false);
    });
  });

  describe('isCoordinadorInCenter', () => {
    // Test 3
    it('devuelve true para ADMIN independientemente del centroId', () => {
      // Arrange
      const admin = { rol: ROLES.ADMIN, centros: [] };
      // Act & Assert
      expect(isCoordinadorInCenter(admin, 99)).toBe(true);
    });

    // Test 4
    it('devuelve false cuando el COORDINADOR no está asignado a ese centro', () => {
      // Arrange
      const coordinador = { rol: ROLES.COORDINADOR, centros: [{ centroId: 1 }] };
      // Act & Assert
      expect(isCoordinadorInCenter(coordinador, 2)).toBe(false);
    });
  });
});