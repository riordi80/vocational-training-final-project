import { isAdminUser, isCoordinadorInCenter } from '../utils/permissions';
import { ROLES } from '../constants/roles';

describe('permissions', () => {

  describe('isAdminUser', () => {
    // Test 1
    it('returns true when user has ADMIN role', () => {
      // Arrange
      const user = { rol: ROLES.ADMIN };
      // Act
      const result = isAdminUser(user);
      // Assert
      expect(result).toBe(true);
    });

    // Test 2
    it('returns false when user has COORDINADOR role', () => {
      // Arrange
      const user = { rol: ROLES.COORDINADOR };
      // Act & Assert
      expect(isAdminUser(user)).toBe(false);
    });
  });

  describe('isCoordinadorInCenter', () => {
    // Test 3
    it('returns true for ADMIN regardless of centroId', () => {
      // Arrange
      const admin = { rol: ROLES.ADMIN, centros: [] };
      // Act & Assert
      expect(isCoordinadorInCenter(admin, 99)).toBe(true);
    });

    // Test 4
    it('returns false when COORDINADOR is not assigned to that center', () => {
      // Arrange
      const coordinador = { rol: ROLES.COORDINADOR, centros: [{ centroId: 1 }] };
      // Act & Assert
      expect(isCoordinadorInCenter(coordinador, 2)).toBe(false);
    });
  });
});