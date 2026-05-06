import { useState, useEffect } from 'react';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import { getArbolById, createArbol, updateArbol } from '../../services/arbolesService';
import { getCentros } from '../../services/centrosService';
import { useForm } from '../../hooks/useForm';
import Input from '../../components/common/Input';
import Button from '../../components/common/Button';
import Spinner from '../../components/common/Spinner';
import Alert from '../../components/common/Alert';
import { usePermissions } from '../../hooks/usePermissions';
import { useAuth } from '../../context/AuthContext';

const validarArbol = (values) => {
  const errors = {};
  if (!values.nombre.trim()) errors.nombre = 'El nombre es obligatorio';
  if (!values.especie.trim()) errors.especie = 'La especie es obligatoria';
  if (!values.fechaPlantacion) {
    errors.fechaPlantacion = 'La fecha de plantación es obligatoria';
  } else {
    const hoy = new Date();
    hoy.setHours(0, 0, 0, 0);
    if (new Date(values.fechaPlantacion) > hoy) {
      errors.fechaPlantacion = 'La fecha de plantación no puede ser futura';
    }
  }
  if (!values.centroEducativo?.id) errors.centroEducativo = 'Debes seleccionar un centro educativo';
  const cantidadNum = parseInt(values.cantidad);
  if (!cantidadNum || cantidadNum < 1) errors.cantidad = 'La cantidad debe ser al menos 1';
  return errors;
};

function FormularioArbol() {
  const { id } = useParams();
  const navigate = useNavigate();
  const location = useLocation();
  const isEditMode = Boolean(id);

  const centroIdDesdeState = location.state?.centroId;
  const initialValues = {
    nombre: '',
    especie: '',
    fechaPlantacion: '',
    ubicacionEspecifica: '',
    centroEducativo: { id: centroIdDesdeState || '' },
    absorcionCo2Anual: '',
    cantidad: 1,
  };

  const {
    values: formData,
    setValues: setFormData,
    errors,
    handleChange,
    handleBlur,
    validateAll,
  } = useForm(
    initialValues,
    validarArbol,
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
        const arbolData = await getArbolById(id);
        const fechaFormateada = arbolData.fechaPlantacion
          ? new Date(arbolData.fechaPlantacion).toISOString().split('T')[0]
          : '';
        setFormData({
          nombre: arbolData.nombre || '',
          especie: arbolData.especie || '',
          fechaPlantacion: fechaFormateada,
          ubicacionEspecifica: arbolData.ubicacionEspecifica || '',
          centroEducativo: { id: arbolData.centroEducativo?.id || '' },
          absorcionCo2Anual: arbolData.absorcionCo2Anual || '',
          cantidad: arbolData.cantidad ?? 1,
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
        nombre: formData.nombre.trim(),
        especie: formData.especie.trim(),
        fechaPlantacion: formData.fechaPlantacion,
        ubicacionEspecifica: formData.ubicacionEspecifica.trim() || null,
        centroEducativo: { id: parseInt(formData.centroEducativo.id) },
        absorcionCo2Anual: formData.absorcionCo2Anual !== '' ? parseFloat(formData.absorcionCo2Anual) : null,
        cantidad: parseInt(formData.cantidad) || 1,
      };

      if (isEditMode) {
        await updateArbol(id, dataToSend);
        navigate(`/arboles/${id}`, {
          state: { message: 'Árbol actualizado correctamente' },
        });
      } else {
        const nuevoArbol = await createArbol(dataToSend);
        navigate(`/arboles/${nuevoArbol.id}`, {
          state: { message: 'Árbol creado correctamente' },
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
                onBlur={handleBlur}
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
                onBlur={handleBlur}
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

              {/* Fecha de Plantación */}
              <Input
                id="fechaPlantacion"
                name="fechaPlantacion"
                label="Fecha de Plantación"
                type="date"
                value={formData.fechaPlantacion}
                onChange={handleChange}
                onBlur={handleBlur}
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
                  onBlur={handleBlur}
                  placeholder="Ej: Patio principal, junto a la entrada"
                />
              </div>

              {/* Cantidad */}
              <div>
                <Input
                  id="cantidad"
                  name="cantidad"
                  label="Cantidad de árboles"
                  type="number"
                  min="1"
                  value={formData.cantidad}
                  onChange={handleChange}
                  onBlur={handleBlur}
                  error={errors.cantidad}
                  required
                  placeholder="1"
                />
                <p className="text-xs text-gray-500 mt-1">
                  Número de árboles de esta familia en el centro
                </p>
              </div>

              {/* Absorción CO2 Anual */}
              <div>
                <Input
                  id="absorcionCo2Anual"
                  name="absorcionCo2Anual"
                  label="Absorción CO2 Anual (kg/año)"
                  type="number"
                  step="0.01"
                  min="0"
                  value={formData.absorcionCo2Anual}
                  onChange={handleChange}
                  onBlur={handleBlur}
                  placeholder="Ej: 22.50"
                />
                <p className="text-xs text-gray-500 mt-1">
                  Estimación de kg de CO2 absorbidos al año
                </p>
              </div>
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
