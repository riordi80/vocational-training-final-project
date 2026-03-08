import { vi } from 'vitest';
import api from '../services/api';
import { getArboles, deleteArbol } from '../services/arbolesService';

vi.mock('../services/api');

describe('arbolesService', () => {

  afterEach(() => vi.clearAllMocks());

  // Test 9
  it('getArboles llama a GET /arboles y devuelve los datos', async () => {
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
  it('deleteArbol relanza el error original al fallar la API', async () => {
    // Arrange
    const apiError = new Error('Network Error');
    api.delete.mockRejectedValue(apiError);
    // Act & Assert
    await expect(deleteArbol(1)).rejects.toThrow('Network Error');
  });
});
