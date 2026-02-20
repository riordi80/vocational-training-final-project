import { vi } from 'vitest';
import api from './api';
import { getArboles, deleteArbol } from './arbolesService';

vi.mock('./api');

describe('arbolesService', () => {

  afterEach(() => vi.clearAllMocks());

  // Test 9
  it('getArboles calls GET /arboles and returns data', async () => {
    // Arrange
    const mockArboles = [{ id: 1, nombre: 'Pino' }, { id: 2, nombre: 'Olivo' }];
    api.get.mockResolvedValue({ data: mockArboles });
    // Act
    const result = await getArboles();
    // Assert
    expect(api.get).toHaveBeenCalledWith('/arboles');
    expect(result).toEqual(mockArboles);
  });

  // Test 10
  it('deleteArbol throws the original error on API failure', async () => {
    // Arrange
    const apiError = new Error('Network Error');
    api.delete.mockRejectedValue(apiError);
    // Act & Assert
    await expect(deleteArbol(1)).rejects.toThrow('Network Error');
  });
});