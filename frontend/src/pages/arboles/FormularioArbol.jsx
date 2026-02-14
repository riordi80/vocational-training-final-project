  import { useState, useEffect } from 'react';
  import { useParams, useNavigate } from 'react-router-dom';
  import { getArbolById, createArbol, updateArbol } from '../../services/arbolesService';
  import { getCentros } from '../../services/centrosService';
  import Input from '../../components/common/Input';
  import Button from '../../components/common/Button';
  import Spinner from '../../components/common/Spinner';
  import Alert from '../../components/common/Alert';
  import { usePermissions } from '../../hooks/usePermissions';
  import { useAuth } from '../../context/AuthContext';

  function FormularioArbol() {
    const { id } = useParams();
    const navigate = useNavigate();
    const isEditMode = Boolean(id);

    // Estados del formulario
    const [formData, setFormData] = useState({
      nombre: '',
      especie: '',
      fechaPlantacion: '',
      ubicacionEspecifica: '',
      centroEducativo: { id: '' },
      umbralTempMin: '',
      umbralTempMax: '',
      umbralHumedadAmbienteMin: '',
      umbralHumedadAmbienteMax: '',
      umbralHumedadSueloMin: '',
      umbralCO2Max: ''
    });

    const [centros, setCentros] = useState([]);
    const [loading, setLoading] = useState(false);
    const [loadingData, setLoadingData] = useState(isEditMode);
    const [error, setError] = useState('');
    const [errors, setErrors] = useState({});
    const { isAdmin } = usePermissions();
    const { user } = useAuth();

    // ADMIN ve todos los centros, COORDINADOR solo los suyos
    const centrosFiltrados = isAdmin()
      ? centros
      : centros.filter(c => user?.centros?.some(uc => uc.centroId === c.id));

    // Cargar centros y datos del árbol (si es edición)
    useEffect(() => {
      cargarDatosIniciales();
    }, [id]);

    const cargarDatosIniciales = async () => {
      try {
        const centrosData = await getCentros();
        setCentros(centrosData);

        if (isEditMode) {
          setLoadingData(true);
          const arbolData = await getArbolById(id);

          // Formatear fecha para el input (YYYY-MM-DD)
          const fechaFormateada = arbolData.fechaPlantacion
            ? new Date(arbolData.fechaPlantacion).toISOString().split('T')[0]
            : '';

          setFormData({
            nombre: arbolData.nombre || '',
            especie: arbolData.especie || '',
            fechaPlantacion: fechaFormateada,
            ubicacionEspecifica: arbolData.ubicacionEspecifica || '',
            centroEducativo: { id: arbolData.centroEducativo?.id || '' },
            umbralTempMin: arbolData.umbralTempMin || '',
            umbralTempMax: arbolData.umbralTempMax || '',
            umbralHumedadAmbienteMin: arbolData.umbralHumedadAmbienteMin || '',
            umbralHumedadAmbienteMax: arbolData.umbralHumedadAmbienteMax || '',
            umbralHumedadSueloMin: arbolData.umbralHumedadSueloMin || '',
            umbralCO2Max: arbolData.umbralCO2Max || ''
          });
        }
      } catch (err) {
        console.error('Error cargando datos iniciales:', err);
        setError('No se pudo conectar con el servidor. Si es la primera carga, puede estar iniciándose (30-60 seg). Recarga la página en unos momentos.');
      } finally {
        setLoadingData(false);
      }
    };

    const handleChange = (e) => {
      const { name, value } = e.target;

      if (name === 'centroEducativo') {
        setFormData(prev => ({
          ...prev,
          centroEducativo: { id: value }
        }));
      } else {
        setFormData(prev => ({
          ...prev,
          [name]: value
        }));
      }

      // Limpiar error del campo cuando el usuario empieza a escribir
      if (errors[name]) {
        setErrors(prev => ({
          ...prev,
          [name]: ''
        }));
      }
    };

    const validarFormulario = () => {
      const nuevosErrores = {};

      // Validaciones de campos obligatorios
      if (!formData.nombre.trim()) {
        nuevosErrores.nombre = 'El nombre es obligatorio';
      }

      if (!formData.especie.trim()) {
        nuevosErrores.especie = 'La especie es obligatoria';
      }

      if (!formData.fechaPlantacion) {
        nuevosErrores.fechaPlantacion = 'La fecha de plantación es obligatoria';
      } else {
        // Validar que la fecha no sea futura
        const fechaSeleccionada = new Date(formData.fechaPlantacion);
        const hoy = new Date();
        hoy.setHours(0, 0, 0, 0);

        if (fechaSeleccionada > hoy) {
          nuevosErrores.fechaPlantacion = 'La fecha de plantación no puede ser futura';
        }
      }

      if (!formData.centroEducativo.id) {
        nuevosErrores.centroEducativo = 'Debes seleccionar un centro educativo';
      }

      // Validaciones de umbrales (si están presentes)
      if (formData.umbralTempMin !== '' && formData.umbralTempMax !== '') {
        const tempMin = parseFloat(formData.umbralTempMin);
        const tempMax = parseFloat(formData.umbralTempMax);

        if (tempMin >= tempMax) {
          nuevosErrores.umbralTempMin = 'La temperatura mínima debe ser menor que la máxima';
        }
      }

      if (formData.umbralHumedadAmbienteMin !== '' && formData.umbralHumedadAmbienteMax !== '') {
        const humMin = parseFloat(formData.umbralHumedadAmbienteMin);
        const humMax = parseFloat(formData.umbralHumedadAmbienteMax);

        if (humMin >= humMax) {
          nuevosErrores.umbralHumedadAmbienteMin = 'La humedad mínima debe ser menor que la máxima';
        }
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

        // Preparar datos para enviar
        const dataToSend = {
          nombre: formData.nombre.trim(),
          especie: formData.especie.trim(),
          fechaPlantacion: formData.fechaPlantacion,
          ubicacionEspecifica: formData.ubicacionEspecifica.trim() || null,
          centroEducativo: { id: parseInt(formData.centroEducativo.id) },
          umbralTempMin: formData.umbralTempMin !== '' ? parseFloat(formData.umbralTempMin) : null,
          umbralTempMax: formData.umbralTempMax !== '' ? parseFloat(formData.umbralTempMax) : null,
          umbralHumedadAmbienteMin: formData.umbralHumedadAmbienteMin !== '' ? parseFloat(formData.umbralHumedadAmbienteMin) : null,
          umbralHumedadAmbienteMax: formData.umbralHumedadAmbienteMax !== '' ? parseFloat(formData.umbralHumedadAmbienteMax) : null,
          umbralHumedadSueloMin: formData.umbralHumedadSueloMin !== '' ? parseFloat(formData.umbralHumedadSueloMin) : null,
          umbralCO2Max: formData.umbralCO2Max !== '' ? parseFloat(formData.umbralCO2Max) : null
        };

        if (isEditMode) {
          await updateArbol(id, dataToSend);
          navigate(`/arboles/${id}`, {
            state: { message: 'Árbol actualizado correctamente' }
          });
        } else {
          const nuevoArbol = await createArbol(dataToSend);
          navigate(`/arboles/${nuevoArbol.id}`, {
            state: { message: 'Árbol creado correctamente' }
          });
        }
      } catch (err) {
        console.error('Error guardando árbol:', err);
        setError(
          err.response?.data?.message ||
          `Error al ${isEditMode ? 'actualizar' : 'crear'} el árbol. Por favor, intenta de nuevo.`
        );
      } finally {
        setLoading(false);
      }
    };

    const handleCancel = () => {
      if (isEditMode) {
        navigate(`/arboles/${id}`);
      } else {
        navigate('/arboles');
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
              {isEditMode ? 'Editar Árbol' : 'Nuevo Árbol'}
            </h1>
            <p className="text-gray-600 mt-1">
              {isEditMode
                ? 'Modifica los datos del árbol'
                : 'Completa el formulario para añadir un nuevo árbol'}
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
            {/* Sección: Información General */}
            <div className="mb-6">
              <h2 className="text-xl font-semibold text-gray-800 mb-4">Información General</h2>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                {/* Nombre */}
                <Input
                  id="nombre"
                  name="nombre"
                  label="Nombre del Árbol"
                  type="text"
                  value={formData.nombre}
                  onChange={handleChange}
                  error={errors.nombre}
                  required
                  placeholder="Ej: Pino Canario del Patio"
                />

                {/* Especie */}
                <Input
                  id="especie"
                  name="especie"
                  label="Especie"
                  type="text"
                  value={formData.especie}
                  onChange={handleChange}
                  error={errors.especie}
                  required
                  placeholder="Ej: Pinus canariensis"
                />

                {/* Centro Educativo */}
                <div>
                  <label htmlFor="centroEducativo" className="block text-sm font-medium text-gray-700 mb-1">
                    Centro Educativo <span className="text-red-500">*</span>
                  </label>
                  <select
                    id="centroEducativo"
                    name="centroEducativo"
                    value={formData.centroEducativo.id}
                    onChange={handleChange}
                    className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-green-500 ${
                      errors.centroEducativo ? 'border-red-500' : 'border-gray-300'
                    }`}
                    required
                  >
                    <option value="">Selecciona un centro</option>
                    {centrosFiltrados.map(centro => (
                      <option key={centro.id} value={centro.id}>
                        {centro.nombre}
                      </option>
                    ))}
                  </select>
                  {errors.centroEducativo && (
                    <p className="mt-1 text-sm text-red-500">{errors.centroEducativo}</p>
                  )}
                </div>

                {/* Fecha de Plantación */}
                <Input
                  id="fechaPlantacion"
                  name="fechaPlantacion"
                  label="Fecha de Plantación"
                  type="date"
                  value={formData.fechaPlantacion}
                  onChange={handleChange}
                  error={errors.fechaPlantacion}
                  required
                />

                {/* Ubicación Específica */}
                <div className="md:col-span-2">
                  <Input
                    id="ubicacionEspecifica"
                    name="ubicacionEspecifica"
                    label="Ubicación Específica"
                    type="text"
                    value={formData.ubicacionEspecifica}
                    onChange={handleChange}
                    placeholder="Ej: Patio principal, junto a la entrada"
                  />
                </div>
              </div>
            </div>

            {/* Sección: Umbrales de Monitorización */}
            <div className="mb-6 pt-6 border-t border-gray-200">
              <h2 className="text-xl font-semibold text-gray-800 mb-4">
                Umbrales de Monitorización (Opcional)
              </h2>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                {/* Temperatura Mínima */}
                <Input
                  id="umbralTempMin"
                  name="umbralTempMin"
                  label="Temperatura Mínima (°C)"
                  type="number"
                  step="0.1"
                  value={formData.umbralTempMin}
                  onChange={handleChange}
                  error={errors.umbralTempMin}
                  placeholder="Ej: 10"
                />

                {/* Temperatura Máxima */}
                <Input
                  id="umbralTempMax"
                  name="umbralTempMax"
                  label="Temperatura Máxima (°C)"
                  type="number"
                  step="0.1"
                  value={formData.umbralTempMax}
                  onChange={handleChange}
                  placeholder="Ej: 35"
                />

                {/* Humedad Ambiente Mínima */}
                <Input
                  id="umbralHumedadAmbienteMin"
                  name="umbralHumedadAmbienteMin"
                  label="Humedad Ambiente Mínima (%)"
                  type="number"
                  step="0.1"
                  value={formData.umbralHumedadAmbienteMin}
                  onChange={handleChange}
                  error={errors.umbralHumedadAmbienteMin}
                  placeholder="Ej: 30"
                />

                {/* Humedad Ambiente Máxima */}
                <Input
                  id="umbralHumedadAmbienteMax"
                  name="umbralHumedadAmbienteMax"
                  label="Humedad Ambiente Máxima (%)"
                  type="number"
                  step="0.1"
                  value={formData.umbralHumedadAmbienteMax}
                  onChange={handleChange}
                  placeholder="Ej: 90"
                />

                {/* Humedad Suelo Mínima */}
                <Input
                  id="umbralHumedadSueloMin"
                  name="umbralHumedadSueloMin"
                  label="Humedad Suelo Mínima (%)"
                  type="number"
                  step="0.1"
                  value={formData.umbralHumedadSueloMin}
                  onChange={handleChange}
                  placeholder="Ej: 20"
                />

                {/* CO2 Máximo */}
                <Input
                  id="umbralCO2Max"
                  name="umbralCO2Max"
                  label="CO2 Máximo (ppm)"
                  type="number"
                  step="0.01"
                  value={formData.umbralCO2Max}
                  onChange={handleChange}
                  placeholder="Ej: 1000"
                />
              </div>
            </div>

            {/* Botones de acción */}
            <div className="flex gap-3 justify-end pt-6 border-t border-gray-200">
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
                  : (isEditMode ? 'Actualizar Árbol' : 'Crear Árbol')}
              </Button>
            </div>
          </form>
        </div>
      </div>
    );
  }

  export default FormularioArbol;
