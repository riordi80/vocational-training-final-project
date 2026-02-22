import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { vi } from 'vitest';
import { MemoryRouter, Routes, Route } from 'react-router-dom';
import Login from './Login';
import { useAuth } from '../../context/AuthContext';

vi.mock('../../context/AuthContext');

const login = vi.fn();

describe('Login', () => {

  beforeEach(() => {
    useAuth.mockReturnValue({ login });
  });

  afterEach(() => vi.clearAllMocks());

  // Test 16
  it('renderiza los campos de email y contraseña', () => {
    render(<MemoryRouter><Login /></MemoryRouter>);
    expect(screen.getByLabelText('Email')).toBeInTheDocument();
    expect(screen.getByLabelText('Contraseña')).toBeInTheDocument();
  });

  // Test 17
  it('muestra error cuando el campo email está vacío', async () => {
    const user = userEvent.setup();
    render(<MemoryRouter><Login /></MemoryRouter>);
    await user.click(screen.getByRole('button', { name: /iniciar sesión/i }));
    expect(await screen.findByText('El email es requerido')).toBeInTheDocument();
  });

  // Test 18
  it('muestra error cuando el campo contraseña está vacío', async () => {
    const user = userEvent.setup();
    render(<MemoryRouter><Login /></MemoryRouter>);
    await user.type(screen.getByLabelText('Email'), 'test@test.com');
    await user.click(screen.getByRole('button', { name: /iniciar sesión/i }));
    expect(await screen.findByText('La contraseña es requerida')).toBeInTheDocument();
  });

  // Test 19
  it('muestra el mensaje de error cuando el login falla', async () => {
    const user = userEvent.setup();
    login.mockRejectedValue(new Error('Usuario o contraseña incorrecta'));
    render(<MemoryRouter><Login /></MemoryRouter>);
    await user.type(screen.getByLabelText('Email'), 'wrong@test.com');
    await user.type(screen.getByLabelText('Contraseña'), 'wrongpass');
    await user.click(screen.getByRole('button', { name: /iniciar sesión/i }));
    expect(await screen.findByText('Usuario o contraseña incorrecta')).toBeInTheDocument();
  });

  // Test 20
  it('navega a /dashboard al iniciar sesión correctamente', async () => {
    const user = userEvent.setup();
    login.mockResolvedValue();
    render(
      <MemoryRouter initialEntries={['/login']}>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/dashboard" element={<p>Dashboard</p>} />
        </Routes>
      </MemoryRouter>
    );
    await user.type(screen.getByLabelText('Email'), 'admin@test.com');
    await user.type(screen.getByLabelText('Contraseña'), '123456');
    await user.click(screen.getByRole('button', { name: /iniciar sesión/i }));
    expect(await screen.findByText('Dashboard')).toBeInTheDocument();
  });

  // Test 21
  it('deshabilita el botón de envío mientras el login está en curso', async () => {
    const user = userEvent.setup();
    let resolveLogin;
    login.mockImplementation(() => new Promise(resolve => { resolveLogin = resolve; }));
    render(<MemoryRouter><Login /></MemoryRouter>);
    await user.type(screen.getByLabelText('Email'), 'admin@test.com');
    await user.type(screen.getByLabelText('Contraseña'), '123456');
    await user.click(screen.getByRole('button', { name: /iniciar sesión/i }));
    expect(await screen.findByRole('button', { name: /iniciando sesión/i })).toBeDisabled();
    resolveLogin();
  });

});
