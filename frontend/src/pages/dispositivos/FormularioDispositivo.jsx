import { useState, useEffect } from 'react';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import { getDispositivoById, createDispositivo, updateDispositivo } from '../../services/dispositivosService';
import { getCentros } from '../../services/centrosService';
import Input from '../../components/common/Input';
import Button from '../../components/common/Button';
import Spinner from '../../components/common/Spinner';
import Alert from '../../components/common/Alert';
import { usePermissions } from '../../hooks/usePermissions';
import { useAuth } from '../../context/AuthContext';

function FormularioDispositivo() {
  const { id } = useParams();
  const navigate = useNavigate();
  const location = useLocation();
  const isEditMode = Boolean(id);

  const centroIdDesdeState = location.state?.centroId;
  const [formData, setFormData] = useState({
    macAddress: '',
    centroEducativo: { id: centroIdDesdeState || '' },
    activo: true,
    frecuenciaLecturaSeg: 30,
  });

  const [centros, setCentros] = useState([]);
  const [loading, setLoading] = useState(false);
  const [loadingData, setLoadingData] = useState(isEditMode);
  const [error, setError] = useState('');
  const [errors, setErrors] = useState({});
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
    const { name, value, type, checked } = e.target;

    if (name === 'centroEducativo') {
      setFormData(prev => ({ ...prev, centroEducativo: { id: value } }));
    } else if (type === 'checkbox') {
      setFormData(prev => ({ ...prev, [name]: checked }));
    } else {
      setFormData(prev => ({ ...prev, [name]: value }));
    }

    if (errors[name]) {
      setErrors(prev => ({ ...prev, [name]: '' }));
    }
  };

  const validarFormulario = () => {
    const nuevosErrores = {};

    if (!formData.macAddress.trim()) {
      nuevosErrores.macAddress = 'La dirección MAC es obligatoria';
    } else {
      // Formato MAC: XX:XX:XX:XX:XX:XX
      const macRegex = /^([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}$/;
      if (!macRegex.test(formData.macAddress.trim())) {
        nuevosErrores.macAddress = 'Formato incorrecto. Usa XX:XX:XX:XX:XX:XX';
      }
    }

    if (!formData.centroEducativo.id) {
      nuevosErrores.centroEducativo = 'Debes seleccionar un centro educativo';
    }

    const freq = parseInt(formData.frecuenciaLecturaSeg);
    if (isNaN(freq) || freq < 5 || freq > 3600) {
      nuevosErrores.frecuenciaLecturaSeg = 'La frecuencia debe estar entre 5 y 3600 segundos';
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
        macAddress: formData.macAddress.trim().toUpperCase(),
        centroEducativo: { id: parseInt(formData.centroEducativo.id) },
        activo: formData.activo,
        frecuenciaLecturaSeg: parseInt(formData.frecuenciaLecturaSeg),
      };

      if (isEditMode) {
        await updateDispositivo(id, dataToSend);
        navigate(-1, { state: { mensaje: 'Dispositivo actualizado correctamente' } });
      } else {
        const nuevo = await createDispositivo(dataToSend);
        navigate(`/centros/${nuevo.centroEducativo.id}`, {
          state: { mensaje: 'Dispositivo registrado correctamente' }
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

  const handleCancel = () => {
    navigate(-1);
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

        <form onSubmit={handleSubmit} className="bg-white rounded-lg shadow-md p-6">
          <div className="space-y-5">
            {/* Dirección MAC */}
            <Input
              id="macAddress"
              name="macAddress"
              label="Dirección MAC"
              type="text"
              value={formData.macAddress}
              onChange={handleChange}
              error={errors.macAddress}
              required
              placeholder="Ej: AA:BB:CC:DD:EE:FF"
            />
            <p className="text-xs text-gray-500 -mt-3">
              Dirección MAC del chip ESP32 (visible en el monitor serie)
            </p>

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

          {/* Botones de acción */}
          <div className="flex gap-3 justify-end pt-6 mt-6 border-t border-gray-200">
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
