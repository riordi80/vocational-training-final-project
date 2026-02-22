import { render, screen } from '@testing-library/react';
import { vi } from 'vitest';
import { MemoryRouter, Routes, Route } from 'react-router-dom';
import ProtectedRoute from './ProtectedRoute';
import { useAuth } from '../context/AuthContext';

vi.mock('../context/AuthContext', () => ({
  useAuth: vi.fn(),
}));

describe('ProtectedRoute', () => {

  afterEach(() => vi.clearAllMocks());

  // Test 15
  it('redirige a /login cuando el usuario no está autenticado', () => {
    useAuth.mockReturnValue({ user: null, loading: false });
    render(
      <MemoryRouter initialEntries={['/arboles/nuevo']}>
        <Routes>
          <Route path="/arboles/nuevo" element={
            <ProtectedRoute><p>Protected Content</p></ProtectedRoute>
          } />
          <Route path="/login" element={<p>Login Page</p>} />
        </Routes>
      </MemoryRouter>
    );
    expect(screen.getByText('Login Page')).toBeInTheDocument();
    expect(screen.queryByText('Protected Content')).not.toBeInTheDocument();
  });

});