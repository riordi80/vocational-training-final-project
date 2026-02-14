import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getUsuarioById, createUsuario, updateUsuario } from '../../services/usuariosService';
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

  const [loading, setLoading] = useState(false);
  const [loadingData, setLoadingData] = useState(isEditMode);
  const [error, setError] = useState('');
  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (isEditMode) {
      cargarUsuario();
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
    } catch (err) {
      console.error('Error cargando usuario:', err);
      setError('No se pudo cargar el usuario.');
    } finally {
      setLoadingData(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));

    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
  };

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

      // Solo enviar password si tiene valor
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
    if (isEditMode) {
      navigate(`/usuarios/${id}`);
    } else {
      navigate('/usuarios');
    }
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
                className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-green-500 ${
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
