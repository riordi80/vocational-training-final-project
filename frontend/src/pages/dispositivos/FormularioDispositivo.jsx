import { useState, useEffect } from 'react';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import { getDispositivoById, createDispositivo, updateDispositivo } from '../../services/dispositivosService';
import { getCentros } from '../../services/centrosService';
import { useForm } from '../../hooks/useForm';
import Input from '../../components/common/Input';
import Button from '../../components/common/Button';
import Spinner from '../../components/common/Spinner';
import Alert from '../../components/common/Alert';
import { usePermissions } from '../../hooks/usePermissions';
import { useAuth } from '../../context/AuthContext';

const macRegex = /^([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}$/;

const validarDispositivo = (values) => {
  const errors = {};
  if (!values.macAddress.trim()) {
    errors.macAddress = 'La dirección MAC es obligatoria';
  } else if (!macRegex.test(values.macAddress.trim())) {
    errors.macAddress = 'Formato incorrecto. Usa XX:XX:XX:XX:XX:XX';
  }
  if (!values.centroEducativo?.id) {
    errors.centroEducativo = 'Debes seleccionar un centro educativo';
  }
  const freq = parseInt(values.frecuenciaLecturaSeg);
  if (isNaN(freq) || freq < 5 || freq > 3600) {
    errors.frecuenciaLecturaSeg = 'La frecuencia debe estar entre 5 y 3600 segundos';
  }
  return errors;
};

function FormularioDispositivo() {
  const { id } = useParams();
  const navigate = useNavigate();
  const location = useLocation();
  const isEditMode = Boolean(id);

  const centroIdDesdeState = location.state?.centroId;
  const initialValues = {
    macAddress: '',
    centroEducativo: { id: centroIdDesdeState || '' },
    activo: true,
    frecuenciaLecturaSeg: 30,
    umbralTempMin: '',
    umbralTempMax: '',
    umbralHumedadAmbienteMin: '',
    umbralHumedadAmbienteMax: '',
    umbralHumedadSueloMin: '',
    umbralCO2Max: '',
  };

  const {
    values: formData,
    setValues: setFormData,
    errors,
    setErrors,
    handleChange,
    handleBlur,
    validateAll,
  } = useForm(
    initialValues,
    validarDispositivo,
    { centroEducativo: (v) => ({ id: v }) }
  );

  const [centros, setCentros] = useState([]);
  const [loading, setLoading] = useState(false);
  const [loadingData, setLoadingData] = useState(isEditMode);
  const [error, setError] = useState('');
  const { isAdmin } = usePermissions();
  const { user } = useAuth();

  const centrosFiltrados = isAdmin()
    ? centros
    : centros.filter(c => user?.centros?.some(uc => uc.centroId === c.id));

  useEffect(() => {
    cargarDatosIniciales();
  }, [id]);

  const cargarDatosIniciales = async () => {
    try {
      const centrosData = await getCentros();
      setCentros(centrosData);

      if (isEditMode) {
        setLoadingData(true);
        const dispositivoData = await getDispositivoById(id);
        setFormData({
          macAddress: dispositivoData.macAddress || '',
          centroEducativo: { id: dispositivoData.centroEducativo?.id || '' },
          activo: dispositivoData.activo ?? true,
          frecuenciaLecturaSeg: dispositivoData.frecuenciaLecturaSeg ?? 30,
          umbralTempMin: dispositivoData.umbralTempMin ?? '',
          umbralTempMax: dispositivoData.umbralTempMax ?? '',
          umbralHumedadAmbienteMin: dispositivoData.umbralHumedadAmbienteMin ?? '',
          umbralHumedadAmbienteMax: dispositivoData.umbralHumedadAmbienteMax ?? '',
          umbralHumedadSueloMin: dispositivoData.umbralHumedadSueloMin ?? '',
          umbralCO2Max: dispositivoData.umbralCO2Max ?? '',
        });
      }
    } catch (err) {
      console.error('Error cargando datos iniciales:', err);
      setError('No se pudo conectar con el servidor. Si es la primera carga, puede estar iniciándose (30-60 seg). Recarga la página en unos momentos.');
    } finally {
      setLoadingData(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateAll()) {
      setError('Por favor, corrige los errores en el formulario');
      return;
    }

    try {
      setLoading(true);
      setError('');

      const dataToSend = {
        macAddress: formData.macAddress.trim().toUpperCase(),
        centroEducativo: { id: parseInt(formData.centroEducativo.id) },
        activo: formData.activo,
        frecuenciaLecturaSeg: parseInt(formData.frecuenciaLecturaSeg),
        umbralTempMin: formData.umbralTempMin !== '' ? parseFloat(formData.umbralTempMin) : null,
        umbralTempMax: formData.umbralTempMax !== '' ? parseFloat(formData.umbralTempMax) : null,
        umbralHumedadAmbienteMin: formData.umbralHumedadAmbienteMin !== '' ? parseFloat(formData.umbralHumedadAmbienteMin) : null,
        umbralHumedadAmbienteMax: formData.umbralHumedadAmbienteMax !== '' ? parseFloat(formData.umbralHumedadAmbienteMax) : null,
        umbralHumedadSueloMin: formData.umbralHumedadSueloMin !== '' ? parseFloat(formData.umbralHumedadSueloMin) : null,
        umbralCO2Max: formData.umbralCO2Max !== '' ? parseFloat(formData.umbralCO2Max) : null,
      };

      if (isEditMode) {
        await updateDispositivo(id, dataToSend);
        navigate(-1, { state: { mensaje: 'Dispositivo actualizado correctamente' } });
      } else {
        const nuevo = await createDispositivo(dataToSend);
        navigate(`/centros/${nuevo.centroEducativo.id}`, {
          state: { mensaje: 'Dispositivo registrado correctamente' },
        });
      }
    } catch (err) {
      console.error('Error guardando dispositivo:', err);
      if (err.response?.status === 409) {
        setErrors(prev => ({ ...prev, macAddress: 'Ya existe un dispositivo con esa dirección MAC' }));
      }
      setError(
        err.response?.data?.message ||
        `Error al ${isEditMode ? 'actualizar' : 'registrar'} el dispositivo. Por favor, intenta de nuevo.`
      );
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = () => navigate(-1);

  if (loadingData) {
    return (
      <div className="flex justify-center items-center py-12">
        <Spinner size="lg" text="Cargando datos..." />
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="max-w-2xl mx-auto">
        <div className="mb-6">
          <h1 className="text-3xl font-bold text-gray-800">
            {isEditMode ? 'Editar Dispositivo' : 'Nuevo Dispositivo'}
          </h1>
          <p className="text-gray-600 mt-1">
            {isEditMode
              ? 'Modifica los datos del dispositivo ESP32'
              : 'Registra un nuevo dispositivo ESP32 en el sistema'}
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

        <form onSubmit={handleSubmit} className="bg-white rounded-lg shadow-md p-6 space-y-6">

          {/* Datos básicos */}
          <fieldset className="border border-gray-200 rounded-lg p-5">
            <legend className="px-2 text-base font-semibold text-gray-700">
              Datos básicos
            </legend>

            <div className="space-y-5 mt-2">
              {/* Dirección MAC */}
              <div>
                <Input
                  id="macAddress"
                  name="macAddress"
                  label="Dirección MAC"
                  type="text"
                  value={formData.macAddress}
                  onChange={handleChange}
                  onBlur={handleBlur}
                  error={errors.macAddress}
                  required
                  placeholder="Ej: AA:BB:CC:DD:EE:FF"
                />
                <p className="text-xs text-gray-500 mt-1">
                  Dirección MAC del chip ESP32 (visible en el monitor serie)
                </p>
              </div>

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
                  onBlur={handleBlur}
                  className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-brand-primary ${
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

              {/* Frecuencia de lectura */}
              <div>
                <Input
                  id="frecuenciaLecturaSeg"
                  name="frecuenciaLecturaSeg"
                  label="Frecuencia de lectura (segundos)"
                  type="number"
                  min="5"
                  max="3600"
                  value={formData.frecuenciaLecturaSeg}
                  onChange={handleChange}
                  onBlur={handleBlur}
                  error={errors.frecuenciaLecturaSeg}
                  required
                />
                <p className="text-xs text-gray-500 mt-1">
                  Intervalo entre lecturas. Mínimo 5 s, máximo 3600 s (1 hora).
                </p>
              </div>

              {/* Estado activo */}
              <div className="flex items-center gap-3">
                <input
                  id="activo"
                  name="activo"
                  type="checkbox"
                  checked={formData.activo}
                  onChange={handleChange}
                  className="h-4 w-4 rounded border-gray-300 text-brand-primary focus:ring-brand-primary"
                />
                <label htmlFor="activo" className="text-sm font-medium text-gray-700">
                  Dispositivo activo
                </label>
              </div>
            </div>
          </fieldset>

          {/* Umbrales de Monitorización */}
          <fieldset className="border border-gray-200 rounded-lg p-5">
            <legend className="px-2 text-base font-semibold text-gray-700">
              Umbrales de Monitorización
            </legend>
            <p className="text-sm text-gray-500 mt-2 mb-4">
              El sistema generará alertas cuando los valores del sensor superen estos rangos.
            </p>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <Input
                id="umbralTempMin"
                name="umbralTempMin"
                label="Temperatura Mínima (°C)"
                type="number"
                step="0.1"
                value={formData.umbralTempMin}
                onChange={handleChange}
                onBlur={handleBlur}
                placeholder="Ej: 5"
              />
              <Input
                id="umbralTempMax"
                name="umbralTempMax"
                label="Temperatura Máxima (°C)"
                type="number"
                step="0.1"
                value={formData.umbralTempMax}
                onChange={handleChange}
                onBlur={handleBlur}
                placeholder="Ej: 40"
              />
              <Input
                id="umbralHumedadAmbienteMin"
                name="umbralHumedadAmbienteMin"
                label="Humedad Ambiente Mínima (%)"
                type="number"
                step="0.1"
                value={formData.umbralHumedadAmbienteMin}
                onChange={handleChange}
                onBlur={handleBlur}
                placeholder="Ej: 30"
              />
              <Input
                id="umbralHumedadAmbienteMax"
                name="umbralHumedadAmbienteMax"
                label="Humedad Ambiente Máxima (%)"
                type="number"
                step="0.1"
                value={formData.umbralHumedadAmbienteMax}
                onChange={handleChange}
                onBlur={handleBlur}
                placeholder="Ej: 90"
              />
              <Input
                id="umbralHumedadSueloMin"
                name="umbralHumedadSueloMin"
                label="Humedad Suelo Mínima (%)"
                type="number"
                step="0.1"
                value={formData.umbralHumedadSueloMin}
                onChange={handleChange}
                onBlur={handleBlur}
                placeholder="Ej: 30"
              />
              <Input
                id="umbralCO2Max"
                name="umbralCO2Max"
                label="CO2 Máximo (ppm)"
                type="number"
                step="1"
                value={formData.umbralCO2Max}
                onChange={handleChange}
                onBlur={handleBlur}
                placeholder="Ej: 1000"
              />
            </div>
          </fieldset>

          {/* Botones de acción */}
          <div className="flex gap-3 justify-end pt-2">
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
                ? (isEditMode ? 'Actualizando...' : 'Registrando...')
                : (isEditMode ? 'Actualizar Dispositivo' : 'Registrar Dispositivo')}
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default FormularioDispositivo;
