import { renderHook, act } from '@testing-library/react';
import { vi } from 'vitest';
import api from '../services/api';
import { AuthProvider, useAuth } from '../context/AuthContext';

vi.mock('../services/api');

const wrapper = ({ children }) => <AuthProvider>{children}</AuthProvider>;

describe('AuthContext', () => {

  afterEach(() => {
    vi.clearAllMocks();
    localStorage.clear();
  });

  describe('login', () => {
    // Test 5
    it('lanza error cuando el email está vacío', async () => {
      // Arrange
      const { result } = renderHook(() => useAuth(), { wrapper });
      // Act & Assert
      await expect(result.current.login('', 'password'))
        .rejects.toThrow('Email y contraseña son requeridos');
    });

    // Test 6
    it('guarda el usuario en localStorage al iniciar sesión correctamente', async () => {
      // Arrange
      const mockUser = { id: 1, email: 'test@test.com', rol: 'ADMIN' };
      api.post.mockResolvedValue({ data: mockUser });
      const { result } = renderHook(() => useAuth(), { wrapper });
      // Act
      await act(async () => { await result.current.login('test@test.com', '123456'); });
      // Assert
      expect(localStorage.getItem('user')).toBe(JSON.stringify(mockUser));
    });

    // Test 7
    it('lanza el mensaje correcto ante una respuesta 401', async () => {
      // Arrange
      api.post.mockRejectedValue({ response: { status: 401 } });
      const { result } = renderHook(() => useAuth(), { wrapper });
      // Act & Assert
      await expect(result.current.login('wrong@test.com', 'wrong'))
        .rejects.toThrow('Usuario o contraseña incorrecta');
    });
  });

  describe('logout', () => {
    // Test 8
    it('elimina el usuario de localStorage', async () => {
      // Arrange
      localStorage.setItem('user', JSON.stringify({ id: 1 }));
      const { result } = renderHook(() => useAuth(), { wrapper });
      // Act
      act(() => { result.current.logout(); });
      // Assert
      expect(localStorage.getItem('user')).toBeNull();
    });
  });
});