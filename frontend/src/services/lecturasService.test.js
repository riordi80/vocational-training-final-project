import { vi } from 'vitest';
import api from './api';
import { getUltimaLectura, getLecturasParaGrafica } from './/lecturasService';

vi.mock('./api');

describe('lecturasService', () => {

  afterEach(() => vi.clearAllMocks());

  describe('getUltimaLectura', () => {
    // Test 11
    it('returns null when content array is empty', async () => {
      // Arrange
      api.get.mockResolvedValue({ data: { content: [], totalElements: 0 } });
      // Act
      const result = await getUltimaLectura(1);
      // Assert
      expect(result).toBeNull();
    });

    // Test 12
    it('returns first element when readings exist', async () => {
      // Arrange
      const lectura = { id: 1, temperatura: 22.5, timestamp: '2026-02-19T10:00:00' };
      api.get.mockResolvedValue({ data: { content: [lectura], totalElements: 1 } });
      // Act
      const result = await getUltimaLectura(1);
      // Assert
      expect(result).toEqual(lectura);
    });
  });
});