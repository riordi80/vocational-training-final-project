import { render, screen, fireEvent } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { vi } from 'vitest';
import { MemoryRouter } from 'react-router-dom';
import FormularioArbol from './FormularioArbol';
import { getCentros } from '../../services/centrosService';

vi.mock('../../services/arbolesService');
vi.mock('../../services/centrosService');
vi.mock('../../hooks/usePermissions', () => ({
  usePermissions: () => ({ isAdmin: () => true }),
}));
vi.mock('../../context/AuthContext', () => ({
  useAuth: () => ({ user: { id: 1, rol: 'ADMIN', centros: [] } }),
}));

const renderForm = () => render(
  <MemoryRouter>
    <FormularioArbol />
  </MemoryRouter>
);

describe('FormularioArbol', () => {

  beforeEach(() => {
    getCentros.mockResolvedValue([{ id: 1, nombre: 'IES Test' }]);
  });

  afterEach(() => vi.clearAllMocks());

  // Test 13
  it('shows validation errors when required fields are empty', async () => {
    const { container } = renderForm();
    fireEvent.submit(container.querySelector('form'));
    expect(await screen.findByText('El nombre es obligatorio')).toBeInTheDocument();
    expect(screen.getByText('La especie es obligatoria')).toBeInTheDocument();
  });

  // Test 14
  it('shows error when umbralTempMin is not less than umbralTempMax', async () => {
    const user = userEvent.setup();
    renderForm();

    // Esperar a que carguen los centros antes de interactuar
    await screen.findByRole('option', { name: 'IES Test' });

    // Rellenar campos obligatorios
    await user.type(screen.getByLabelText(/nombre del árbol/i), 'Test Árbol');
    await user.type(screen.getByLabelText(/especie/i), 'Pinus sylvestris');
    fireEvent.change(screen.getByLabelText(/fecha de plantación/i), { target: { value: '2020-01-01' } });
    await user.selectOptions(screen.getByRole('combobox'), '1');

    // Umbrales con valor inválido (mínimo >= máximo)
    await user.type(screen.getByLabelText(/temperatura mínima/i), '30');
    await user.type(screen.getByLabelText(/temperatura máxima/i), '10');

    await user.click(screen.getByRole('button', { name: /crear árbol/i }));

    expect(await screen.findByText('La temperatura mínima debe ser menor que la máxima')).toBeInTheDocument();
  });

});
