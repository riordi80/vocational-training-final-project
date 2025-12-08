  import { useState, useEffect } from 'react';
  import { useParams, useNavigate } from 'react-router-dom';
  import { getArbolById, deleteArbol } from '../../services/arbolesService';
  import Button from '../../components/common/Button';
  import Spinner from '../../components/common/Spinner';
  import Alert from '../../components/common/Alert';

  function DetalleArbol() {
    const { id } = useParams();
    const navigate = useNavigate();

    const [arbol, setArbol] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [showDeleteConfirm, setShowDeleteConfirm] = useState(false);
    const [deleting, setDeleting] = useState(false);

    useEffect(() => {
      cargarArbol();
    }, [id]);

    const cargarArbol = async () => {
      try {
        setLoading(true);
        setError('');
        const data = await getArbolById(id);
        setArbol(data);
      } catch (err) {
        console.error('Error cargando árbol:', err);
        setError('No se pudo conectar con el servidor. Si es la primera carga, puede estar iniciándose (30-60 seg). Recarga la página en unos momentos.');
      } finally {
        setLoading(false);
      }
    };

    const handleEditar = () => {
      navigate(`/arboles/${id}/editar`);
    };

    const handleEliminar = async () => {
      try {
        setDeleting(true);
        await deleteArbol(id);
        navigate('/arboles', {
          state: { message: 'Árbol eliminado correctamente' }
        });
      } catch (err) {
        console.error('Error eliminando árbol:', err);
        setError('Error al eliminar el árbol. Por favor, intenta de nuevo.');
        setShowDeleteConfirm(false);
      } finally {
        setDeleting(false);
      }
    };

    const handleVolver = () => {
      navigate('/arboles');
    };

    const formatearFecha = (fecha) => {
      if (!fecha) return '-';
      return new Date(fecha).toLocaleDateString('es-ES', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
      });
    };

    if (loading) {
      return (
        <div className="flex justify-center items-center py-12">
          <Spinner size="lg" text="Cargando árbol..." />
        </div>
      );
    }

    if (error) {
      return (
        <div className="container mx-auto px-4 py-8">
          <Alert
            type="error"
            message={error}
            dismissible
            onClose={() => setError('')}
          />
          <div className="mt-4">
            <Button onClick={handleVolver}>Volver al listado</Button>
          </div>
        </div>
      );
    }

    if (!arbol) {
      return (
        <div className="container mx-auto px-4 py-8">
          <Alert
            type="warning"
            message="No se encontró el árbol solicitado."
          />
          <div className="mt-4">
            <Button onClick={handleVolver}>Volver al listado</Button>
          </div>
        </div>
      );
    }

    return (
      <div className="container mx-auto px-4 py-8">
        {/* Header con título y botones */}
        <div className="mb-6 flex flex-col md:flex-row md:items-center md:justify-between gap-4">
          <div>
            <h1 className="text-3xl font-bold text-gray-800">{arbol.nombre}</h1>
            <p className="text-gray-600 mt-1">{arbol.especie}</p>
          </div>
          <div className="flex gap-2">
            <Button variant="outline" onClick={handleVolver}>
              Volver
            </Button>
            <Button variant="primary" onClick={handleEditar}>
              Editar
            </Button>
            <Button 
              variant="danger" 
              onClick={() => setShowDeleteConfirm(true)}
              disabled={deleting}
            >
              Eliminar
            </Button>
          </div>
        </div>

        {/* Card con información del árbol */}
        <div className="bg-white rounded-lg shadow-md overflow-hidden">
          <div className="p-6">
            <h2 className="text-xl font-semibold text-gray-800 mb-4">Información General</h2>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              {/* Nombre */}
              <div>
                <label className="block text-sm font-medium text-gray-600 mb-1">
                  Nombre
                </label>
                <p className="text-gray-900">{arbol.nombre}</p>
              </div>

              {/* Especie */}
              <div>
                <label className="block text-sm font-medium text-gray-600 mb-1">
                  Especie
                </label>
                <p className="text-gray-900">{arbol.especie}</p>
              </div>

              {/* Centro Educativo */}
              <div>
                <label className="block text-sm font-medium text-gray-600 mb-1">
                  Centro Educativo
                </label>
                <p className="text-gray-900">
                  {arbol.centroEducativo?.nombre || 'No asignado'}
                </p>
              </div>

              {/* Fecha de Plantación */}
              <div>
                <label className="block text-sm font-medium text-gray-600 mb-1">
                  Fecha de Plantación
                </label>
                <p className="text-gray-900">{formatearFecha(arbol.fechaPlantacion)}</p>
              </div>

              {/* Ubicación Específica */}
              <div className="md:col-span-2">
                <label className="block text-sm font-medium text-gray-600 mb-1">
                  Ubicación Específica
                </label>
                <p className="text-gray-900">
                  {arbol.ubicacionEspecifica || 'No especificada'}
                </p>
              </div>
            </div>
          </div>

          {/* Sección de umbrales */}
          <div className="border-t border-gray-200 p-6 bg-gray-50">
            <h2 className="text-xl font-semibold text-gray-800 mb-4">Umbrales de Monitorización</h2>

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {/* Temperatura Mínima */}
              <div>
                <label className="block text-sm font-medium text-gray-600 mb-1">
                  Temperatura Mínima
                </label>
                <p className="text-gray-900">
                  {arbol.umbralTempMin !== null ? `${arbol.umbralTempMin}°C` : 'No configurado'}
                </p>
              </div>

              {/* Temperatura Máxima */}
              <div>
                <label className="block text-sm font-medium text-gray-600 mb-1">
                  Temperatura Máxima
                </label>
                <p className="text-gray-900">
                  {arbol.umbralTempMax !== null ? `${arbol.umbralTempMax}°C` : 'No configurado'}
                </p>
              </div>

              {/* Humedad Ambiente Mínima */}
              <div>
                <label className="block text-sm font-medium text-gray-600 mb-1">
                  Humedad Ambiente Mínima
                </label>
                <p className="text-gray-900">
                  {arbol.umbralHumedadAmbienteMin !== null
                    ? `${arbol.umbralHumedadAmbienteMin}%`
                    : 'No configurado'}
                </p>
              </div>

              {/* Humedad Ambiente Máxima */}
              <div>
                <label className="block text-sm font-medium text-gray-600 mb-1">
                  Humedad Ambiente Máxima
                </label>
                <p className="text-gray-900">
                  {arbol.umbralHumedadAmbienteMax !== null
                    ? `${arbol.umbralHumedadAmbienteMax}%`
                    : 'No configurado'}
                </p>
              </div>

              {/* Humedad Suelo Mínima */}
              <div>
                <label className="block text-sm font-medium text-gray-600 mb-1">
                  Humedad Suelo Mínima
                </label>
                <p className="text-gray-900">
                  {arbol.umbralHumedadSueloMin !== null
                    ? `${arbol.umbralHumedadSueloMin}%`
                    : 'No configurado'}
                </p>
              </div>

              {/* CO2 Máximo */}
              <div>
                <label className="block text-sm font-medium text-gray-600 mb-1">
                  CO2 Máximo
                </label>
                <p className="text-gray-900">
                  {arbol.umbralCO2Max !== null
                    ? `${arbol.umbralCO2Max} ppm`
                    : 'No configurado'}
                </p>
              </div>
            </div>
          </div>
        </div>

        {/* Modal de confirmación de eliminación */}
        {showDeleteConfirm && (
          <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
            <div className="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-2">
                ¿Eliminar árbol?
              </h3>
              <p className="text-gray-600 mb-6">
                ¿Estás seguro de que deseas eliminar "{arbol.nombre}"? Esta acción no se puede deshacer.
              </p>
              <div className="flex gap-3 justify-end">
                <Button
                  variant="outline"
                  onClick={() => setShowDeleteConfirm(false)}
                  disabled={deleting}
                >
                  Cancelar
                </Button>
                <Button
                  variant="danger"
                  onClick={handleEliminar}
                  disabled={deleting}
                >
                  {deleting ? 'Eliminando...' : 'Eliminar'}
                </Button>
              </div>
            </div>
          </div>
        )}
      </div>
    );
  }

  export default DetalleArbol;
