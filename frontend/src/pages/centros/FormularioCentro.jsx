import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { getCentroById, createCentro, updateCentro } from '../../services/centrosService';
import Button from '../../components/common/Button';
import Input from '../../components/common/Input';
import Alert from '../../components/common/Alert';
import Spinner from '../../components/common/Spinner';
import { ISLAS } from '../../constants/islas';

function FormularioCentro() {
  const { id } = useParams();
  const navigate = useNavigate();
  const esEdicion = Boolean(id);

  // Estados del formulario
  const [formData, setFormData] = useState({
    nombre: '',
    direccion: '',
    latitud: '',
    longitud: '',
    responsable: '',
    isla: '',
    poblacion: '',
    provincia: '',
    codigoPostal: '',
    telefono: '',
    email: ''
  });

  const [loading, setLoading] = useState(false);
  const [loadingCentro, setLoadingCentro] = useState(esEdicion);
  const [error, setError] = useState('');
  const [errors, setErrors] = useState({});

  // Cargar datos del centro si es edición
  useEffect(() => {
    if (esEdicion) {
      cargarCentro();
    }
  }, [id]);

  const cargarCentro = async () => {
    try {
      setLoadingCentro(true);
      const centro = await getCentroById(id);
      setFormData({
        nombre: centro.nombre || '',
        direccion: centro.direccion || '',
        latitud: centro.latitud?.toString() || '',
        longitud: centro.longitud?.toString() || '',
        responsable: centro.responsable || '',
        isla: centro.isla || '',
        poblacion: centro.poblacion || '',
        provincia: centro.provincia || '',
        codigoPostal: centro.codigoPostal || '',
        telefono: centro.telefono || '',
        email: centro.email || ''
      });
    } catch (err) {
      console.error('Error cargando centro:', err);
      setError('No se pudo cargar el centro educativo.');
    } finally {
      setLoadingCentro(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    const updates = { [name]: value };

    // Auto-rellenar provincia al seleccionar isla
    if (name === 'isla') {
      const islaSeleccionada = ISLAS.find((i) => i.value === value);
      updates.provincia = islaSeleccionada ? islaSeleccionada.provincia : '';
    }

    setFormData(prev => ({ ...prev, ...updates }));
    // Limpiar error del campo al escribir
    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
  };

  const validarFormulario = () => {
    const nuevosErrores = {};

    // Nombre (requerido, max 200)
    if (!formData.nombre.trim()) {
      nuevosErrores.nombre = 'El nombre es obligatorio';
    } else if (formData.nombre.length > 200) {
      nuevosErrores.nombre = 'El nombre no puede exceder 200 caracteres';
    }

    // Dirección (requerida, max 300)
    if (!formData.direccion.trim()) {
      nuevosErrores.direccion = 'La dirección es obligatoria';
    } else if (formData.direccion.length > 300) {
      nuevosErrores.direccion = 'La dirección no puede exceder 300 caracteres';
    }

    // Latitud (requerida, rango -90 a 90)
    if (!formData.latitud.trim()) {
      nuevosErrores.latitud = 'La latitud es obligatoria';
    } else {
      const lat = parseFloat(formData.latitud);
      if (isNaN(lat)) {
        nuevosErrores.latitud = 'La latitud debe ser un número válido';
      } else if (lat < -90 || lat > 90) {
        nuevosErrores.latitud = 'La latitud debe estar entre -90 y 90';
      }
    }

    // Longitud (requerida, rango -180 a 180)
    if (!formData.longitud.trim()) {
      nuevosErrores.longitud = 'La longitud es obligatoria';
    } else {
      const lng = parseFloat(formData.longitud);
      if (isNaN(lng)) {
        nuevosErrores.longitud = 'La longitud debe ser un número válido';
      } else if (lng < -180 || lng > 180) {
        nuevosErrores.longitud = 'La longitud debe estar entre -180 y 180';
      }
    }

    // Responsable (requerido, max 100)
    if (!formData.responsable.trim()) {
      nuevosErrores.responsable = 'El responsable es obligatorio';
    } else if (formData.responsable.length > 100) {
      nuevosErrores.responsable = 'El responsable no puede exceder 100 caracteres';
    }

    setErrors(nuevosErrores);
    return Object.keys(nuevosErrores).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (!validarFormulario()) {
      setError('Por favor, corrige los errores en el formulario.');
      return;
    }

    try {
      setLoading(true);

      // Preparar datos para enviar (convertir lat/lng a números)
      const centroData = {
        nombre: formData.nombre.trim(),
        direccion: formData.direccion.trim(),
        latitud: parseFloat(formData.latitud),
        longitud: parseFloat(formData.longitud),
        responsable: formData.responsable.trim(),
        isla: formData.isla || null,
        poblacion: formData.poblacion.trim() || null,
        provincia: formData.provincia.trim() || null,
        codigoPostal: formData.codigoPostal.trim() || null,
        telefono: formData.telefono.trim() || null,
        email: formData.email.trim() || null
      };

      if (esEdicion) {
        await updateCentro(id, centroData);
        navigate(`/centros/${id}`, {
          state: { mensaje: 'Centro educativo actualizado exitosamente' }
        });
      } else {
        const centroCreado = await createCentro(centroData);
        navigate(`/centros/${centroCreado.id}`, {
          state: { mensaje: 'Centro educativo creado exitosamente' }
        });
      }
    } catch (err) {
      console.error('Error guardando centro:', err);
      if (err.response?.status === 409) {
        setError('Ya existe un centro educativo con ese nombre.');
      } else if (err.response?.status === 400) {
        setError('Datos inválidos. Verifica los campos del formulario.');
      } else {
        setError('No se pudo guardar el centro educativo. Intenta de nuevo.');
      }
    } finally {
      setLoading(false);
    }
  };

  const handleCancelar = () => {
    navigate(-1);
  };

  if (loadingCentro) {
    return (
      <div className="flex justify-center items-center py-12">
        <Spinner size="lg" text="Cargando centro..." />
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-8 max-w-4xl">
      {/* Encabezado */}
      <div className="mb-6">
        <h1 className="text-3xl font-bold text-gray-800 mb-2">
          {esEdicion ? 'Editar Centro Educativo' : 'Nuevo Centro Educativo'}
        </h1>
        <p className="text-gray-600">
          {esEdicion
            ? 'Actualiza la información del centro educativo'
            : 'Completa el formulario para registrar un nuevo centro'}
        </p>
      </div>

      {/* Mensaje de error general */}
      {error && (
        <div className="mb-4">
          <Alert
            type="error"
            message={error}
            dismissible
            onClose={() => setError('')}
          />
        </div>
      )}

      {/* Formulario */}
      <form onSubmit={handleSubmit} className="bg-white rounded-lg shadow p-6">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {/* Nombre */}
          <div className="md:col-span-2">
            <Input
              id="nombre"
              name="nombre"
              label="Nombre del Centro"
              type="text"
              value={formData.nombre}
              onChange={handleChange}
              placeholder="Ej: IES El Rincón"
              error={errors.nombre}
              required
            />
            <p className="text-xs text-gray-500 mt-1">
              Máximo 200 caracteres
            </p>
          </div>

          {/* Dirección */}
          <div className="md:col-span-2">
            <Input
              id="direccion"
              name="direccion"
              label="Dirección"
              type="text"
              value={formData.direccion}
              onChange={handleChange}
              placeholder="Ej: Calle Principal 123, Ciudad"
              error={errors.direccion}
              required
            />
            <p className="text-xs text-gray-500 mt-1">
              Máximo 300 caracteres
            </p>
          </div>

          {/* Responsable */}
          <div className="md:col-span-2">
            <Input
              id="responsable"
              name="responsable"
              label="Responsable"
              type="text"
              value={formData.responsable}
              onChange={handleChange}
              placeholder="Ej: Juan Pérez García"
              error={errors.responsable}
              required
            />
            <p className="text-xs text-gray-500 mt-1">
              Máximo 100 caracteres
            </p>
          </div>

          {/* Isla */}
          <div>
            <label htmlFor="isla" className="block text-sm font-medium text-gray-700 mb-1">
              Isla
            </label>
            <select
              id="isla"
              name="isla"
              value={formData.isla}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-green-500"
            >
              <option value="">-- Seleccionar isla --</option>
              {ISLAS.map((isla) => (
                <option key={isla.value} value={isla.value}>
                  {isla.label}
                </option>
              ))}
            </select>
          </div>

          {/* Población */}
          <div>
            <Input
              id="poblacion"
              name="poblacion"
              label="Población"
              type="text"
              value={formData.poblacion}
              onChange={handleChange}
              placeholder="Ej: Las Palmas de Gran Canaria"
            />
          </div>

          {/* Provincia (auto-rellenada por isla) */}
          <div>
            <Input
              id="provincia"
              name="provincia"
              label="Provincia"
              type="text"
              value={formData.provincia}
              readOnly
              disabled
              placeholder="Se rellena al seleccionar isla"
            />
          </div>

          {/* Código Postal */}
          <div>
            <Input
              id="codigoPostal"
              name="codigoPostal"
              label="Código Postal"
              type="text"
              value={formData.codigoPostal}
              onChange={handleChange}
              placeholder="Ej: 35001"
            />
          </div>

          {/* Teléfono */}
          <div>
            <Input
              id="telefono"
              name="telefono"
              label="Teléfono"
              type="tel"
              value={formData.telefono}
              onChange={handleChange}
              placeholder="Ej: 928 123 456"
            />
          </div>

          {/* Email */}
          <div>
            <Input
              id="email"
              name="email"
              label="Correo Electrónico"
              type="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="Ej: centro@ejemplo.es"
            />
          </div>

          {/* Latitud */}
          <div>
            <Input
              id="latitud"
              name="latitud"
              label="Latitud"
              type="number"
              step="0.00000001"
              value={formData.latitud}
              onChange={handleChange}
              placeholder="Ej: 28.4636296"
              error={errors.latitud}
              required
            />
            <p className="text-xs text-gray-500 mt-1">
              Rango: -90 a 90
            </p>
          </div>

          {/* Longitud */}
          <div>
            <Input
              id="longitud"
              name="longitud"
              label="Longitud"
              type="number"
              step="0.00000001"
              value={formData.longitud}
              onChange={handleChange}
              placeholder="Ej: -16.2518467"
              error={errors.longitud}
              required
            />
            <p className="text-xs text-gray-500 mt-1">
              Rango: -180 a 180
            </p>
          </div>
        </div>

        {/* Información adicional */}
        <div className="mt-6 p-4 bg-blue-50 border border-blue-200 rounded">
          <p className="text-sm text-blue-800">
            <span className="font-medium">💡 Consejo:</span> Puedes obtener las coordenadas exactas desde{' '}
            <a
              href="https://www.google.com/maps"
              target="_blank"
              rel="noopener noreferrer"
              className="underline hover:text-blue-900"
            >
              Google Maps
            </a>{' '}
            haciendo clic derecho en la ubicación del centro.
          </p>
        </div>

        {/* Botones */}
        <div className="mt-6 flex gap-3 justify-end">
          <Button
            type="button"
            onClick={handleCancelar}
            variant="outline"
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
              ? (esEdicion ? 'Actualizando...' : 'Creando...')
              : (esEdicion ? 'Actualizar Centro' : 'Crear Centro')}
          </Button>
        </div>
      </form>
    </div>
  );
}

export default FormularioCentro;
