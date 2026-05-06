import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getUsuarioById, createUsuario, updateUsuario } from '../../services/usuariosService';
import { getCentros } from '../../services/centrosService';
import { asignarCentro, desasignarCentro, getCentrosDeUsuario } from '../../services/usuarioCentroService';
import Input from '../../components/common/Input';
import Button from '../../components/common/Button';
import Spinner from '../../components/common/Spinner';
import Alert from '../../components/common/Alert';

function FormularioUsuario() {
  const { id } = useParams();
  const navigate = useNavigate();
  const isEditMode = Boolean(id);

  const [formData, setFormData] = useState({
    nombre: '',
    email: '',
    password: '',
    rol: 'COORDINADOR'
  });

  // Centros para creación (estado local, se aplican al guardar)
  const [centros, setCentros] = useState([]);
  const [centrosSeleccionados, setCentrosSeleccionados] = useState([]);
  const [centroSeleccionado, setCentroSeleccionado] = useState('');

  // Centros para edición (estado API, se aplican inmediatamente)
  const [centrosAsignados, setCentrosAsignados] = useState([]);

  const [loading, setLoading] = useState(false);
  const [loadingData, setLoadingData] = useState(isEditMode);
  const [error, setError] = useState('');
  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (isEditMode) {
      cargarUsuario();
    } else {
      getCentros().then(setCentros).catch(() => {});
    }
  }, [id]);

  const cargarUsuario = async () => {
    try {
      setLoadingData(true);
      const usuarioData = await getUsuarioById(id);
      setFormData({
        nombre: usuarioData.nombre || '',
        email: usuarioData.email || '',
        password: '',
        rol: usuarioData.rol || 'COORDINADOR'
      });
      if (usuarioData.rol === 'COORDINADOR') {
        const [asignados, todos] = await Promise.all([
          getCentrosDeUsuario(id),
          getCentros()
        ]);
        setCentrosAsignados(asignados);
        setCentros(todos);
      }
    } catch (err) {
      console.error('Error cargando usuario:', err);
      setError('No se pudo cargar el usuario.');
    } finally {
      setLoadingData(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
    if (name === 'rol' && value === 'ADMIN') {
      setCentrosSeleccionados([]);
      setCentroSeleccionado('');
    }
    if (name === 'rol' && value === 'COORDINADOR' && !isEditMode && centros.length === 0) {
      getCentros().then(setCentros).catch(() => {});
    }
    if (errors[name]) {
      setErrors(prev => ({ ...prev, [name]: '' }));
    }
  };

  // --- Handlers modo CREACIÓN (estado local) ---
  const handleAgregarCentroLocal = () => {
    if (!centroSeleccionado) return;
    const centro = centros.find(c => c.id === parseInt(centroSeleccionado));
    if (centro) {
      setCentrosSeleccionados(prev => [...prev, centro]);
      setCentroSeleccionado('');
      if (errors.centros) setErrors(prev => ({ ...prev, centros: '' }));
    }
  };

  const handleQuitarCentroLocal = (centroId) => {
    setCentrosSeleccionados(prev => prev.filter(c => c.id !== centroId));
  };

  // --- Handlers modo EDICIÓN (llamadas API inmediatas) ---
  const handleAgregarCentroEdicion = async () => {
    if (!centroSeleccionado) return;
    try {
      await asignarCentro(id, parseInt(centroSeleccionado));
      const asignados = await getCentrosDeUsuario(id);
      setCentrosAsignados(asignados);
      setCentroSeleccionado('');
    } catch (err) {
      setError('No se pudo asignar el centro.');
    }
  };

  const handleQuitarCentroEdicion = async (asignacionId) => {
    try {
      await desasignarCentro(asignacionId);
      const asignados = await getCentrosDeUsuario(id);
      setCentrosAsignados(asignados);
    } catch (err) {
      setError('No se pudo desasignar el centro.');
    }
  };

  // Centros disponibles en el selector (excluye los ya asignados)
  const centrosDisponibles = isEditMode
    ? centros.filter(c => !centrosAsignados.some(a => a.centroEducativo?.id === c.id))
    : centros.filter(c => !centrosSeleccionados.some(s => s.id === c.id));

  const validarFormulario = () => {
    const nuevosErrores = {};

    if (!formData.nombre.trim()) {
      nuevosErrores.nombre = 'El nombre es obligatorio';
    }

    if (!formData.email.trim()) {
      nuevosErrores.email = 'El email es obligatorio';
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
      nuevosErrores.email = 'El email no es válido';
    }

    if (!isEditMode && !formData.password.trim()) {
      nuevosErrores.password = 'La contraseña es obligatoria';
    }

    if (formData.password && formData.password.length < 6) {
      nuevosErrores.password = 'La contraseña debe tener al menos 6 caracteres';
    }

    if (!formData.rol) {
      nuevosErrores.rol = 'El rol es obligatorio';
    }

    if (!isEditMode && formData.rol === 'COORDINADOR' && centrosSeleccionados.length === 0) {
      nuevosErrores.centros = 'Debes asignar al menos un centro al coordinador';
    }

    setErrors(nuevosErrores);
    return Object.keys(nuevosErrores).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validarFormulario()) {
      setError('Por favor, corrige los errores en el formulario');
      return;
    }

    try {
      setLoading(true);
      setError('');

      const dataToSend = {
        nombre: formData.nombre.trim(),
        email: formData.email.trim(),
        rol: formData.rol
      };

      if (formData.password) {
        dataToSend.password = formData.password;
      }

      if (isEditMode) {
        await updateUsuario(id, dataToSend);
        navigate(`/usuarios/${id}`, {
          state: { mensaje: 'Usuario actualizado correctamente' }
        });
      } else {
        const nuevoUsuario = await createUsuario(dataToSend);
        if (formData.rol === 'COORDINADOR' && centrosSeleccionados.length > 0) {
          await Promise.all(centrosSeleccionados.map(c => asignarCentro(nuevoUsuario.id, c.id)));
        }
        navigate(`/usuarios/${nuevoUsuario.id}`, {
          state: { mensaje: 'Usuario creado correctamente' }
        });
      }
    } catch (err) {
      console.error('Error guardando usuario:', err);
      setError(
        err.response?.data?.message ||
        `Error al ${isEditMode ? 'actualizar' : 'crear'} el usuario. Por favor, intenta de nuevo.`
      );
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = () => {
    navigate(-1);
  };

  // Bloque JSX compartido para la sección de centros
  const renderCentrosSection = () => {
    const filas = isEditMode
      ? centrosAsignados.map(asig => ({
          key: asig.id,
          nombre: asig.centroEducativo?.nombre || `Centro #${asig.centroEducativo?.id}`,
          onQuitar: () => handleQuitarCentroEdicion(asig.id)
        }))
      : centrosSeleccionados.map(centro => ({
          key: centro.id,
          nombre: centro.nombre,
          onQuitar: () => handleQuitarCentroLocal(centro.id)
        }));

    const onAsignar = isEditMode ? handleAgregarCentroEdicion : handleAgregarCentroLocal;
    const isEmpty = isEditMode ? centrosAsignados.length === 0 : centrosSeleccionados.length === 0;

    return (
      <div className="mt-6 pt-6 border-t border-gray-200">
        <h2 className="text-xl font-semibold text-gray-800 mb-4 border-b pb-2">
          Centros Asignados {!isEditMode && <span className="text-red-500 text-base">*</span>}
        </h2>

        <div className="flex flex-col sm:flex-row gap-2 mb-4">
          <select
            value={centroSeleccionado}
            onChange={(e) => setCentroSeleccionado(e.target.value)}
            className="flex-1 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-brand-primary"
          >
            <option value="">Selecciona un centro...</option>
            {centrosDisponibles.map(centro => (
              <option key={centro.id} value={centro.id}>
                {centro.nombre}
              </option>
            ))}
          </select>
          <Button
            type="button"
            variant="primary"
            onClick={onAsignar}
            disabled={!centroSeleccionado}
          >
            Asignar
          </Button>
        </div>

        {errors.centros && (
          <p className="mb-3 text-sm text-red-500">{errors.centros}</p>
        )}

        {isEmpty ? (
          <p className="text-gray-500 text-center py-4">
            Este coordinador no tiene centros asignados.
          </p>
        ) : (
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-brand-bg-green">
              <thead className="bg-brand-primary">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-white uppercase tracking-wider">
                    Centro
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-white uppercase tracking-wider">
                    Acciones
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-brand-bg-green">
                {filas.map(fila => (
                  <tr key={fila.key} className="hover:bg-brand-primary/5">
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                      {fila.nombre}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm">
                      <Button
                        type="button"
                        variant="danger"
                        size="sm"
                        onClick={fila.onQuitar}
                      >
                        Desasignar
                      </Button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    );
  };

  if (loadingData) {
    return (
      <div className="flex justify-center items-center py-12">
        <Spinner size="lg" text="Cargando datos..." />
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="max-w-3xl mx-auto">
        <div className="mb-6">
          <h1 className="text-3xl font-bold text-gray-800">
            {isEditMode ? 'Editar Usuario' : 'Nuevo Usuario'}
          </h1>
          <p className="text-gray-600 mt-1">
            {isEditMode
              ? 'Modifica los datos del usuario'
              : 'Completa el formulario para añadir un nuevo usuario'}
          </p>
        </div>

        {error && (
          <div className="mb-4">
            <Alert
              type="error"
              message={error}
              onClose={() => setError('')}
              dismissible
            />
          </div>
        )}

        <form onSubmit={handleSubmit} className="bg-white rounded-lg shadow-md p-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <Input
              id="nombre"
              name="nombre"
              label="Nombre"
              type="text"
              value={formData.nombre}
              onChange={handleChange}
              error={errors.nombre}
              required
              placeholder="Nombre del usuario"
            />

            <Input
              id="email"
              name="email"
              label="Email"
              type="email"
              value={formData.email}
              onChange={handleChange}
              error={errors.email}
              required
              placeholder="correo@ejemplo.com"
            />

            <Input
              id="password"
              name="password"
              label={isEditMode ? 'Contraseña (dejar vacío para no cambiar)' : 'Contraseña'}
              type="password"
              value={formData.password}
              onChange={handleChange}
              error={errors.password}
              required={!isEditMode}
              placeholder={isEditMode ? 'Nueva contraseña (opcional)' : 'Contraseña'}
            />

            <div>
              <label htmlFor="rol" className="block text-sm font-medium text-gray-700 mb-1">
                Rol <span className="text-red-500">*</span>
              </label>
              <select
                id="rol"
                name="rol"
                value={formData.rol}
                onChange={handleChange}
                className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-brand-primary ${
                  errors.rol ? 'border-red-500' : 'border-gray-300'
                }`}
                required
              >
                <option value="COORDINADOR">COORDINADOR</option>
                <option value="ADMIN">ADMIN</option>
              </select>
              {errors.rol && (
                <p className="mt-1 text-sm text-red-500">{errors.rol}</p>
              )}
            </div>
          </div>

          {formData.rol === 'COORDINADOR' && renderCentrosSection()}

          {/* Botones de acción */}
          <div className="flex gap-3 justify-end pt-6 border-t border-gray-200 mt-6">
            <Button
              type="button"
              variant="outline"
              onClick={handleCancel}
              disabled={loading}
            >
              Cancelar
            </Button>
            <Button
              type="submit"
              variant="primary"
              disabled={loading}
            >
              {loading
                ? (isEditMode ? 'Actualizando...' : 'Creando...')
                : (isEditMode ? 'Actualizar Usuario' : 'Crear Usuario')}
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default FormularioUsuario;
