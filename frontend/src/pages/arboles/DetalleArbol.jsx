  import { useState, useEffect } from 'react';
  import { useParams, useNavigate } from 'react-router-dom';
  import { getArbolById, deleteArbol } from '../../services/arbolesService';
  import { getUltimaLectura } from '../../services/lecturasService';
  import Button from '../../components/common/Button';
  import Spinner from '../../components/common/Spinner';
  import Alert from '../../components/common/Alert';
  import { usePermissions } from '../../hooks/usePermissions';

  function DetalleArbol() {
    const { id } = useParams();
    const navigate = useNavigate();

    const [arbol, setArbol] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [showDeleteConfirm, setShowDeleteConfirm] = useState(false);
    const [deleting, setDeleting] = useState(false);
    const [ultimaLectura, setUltimaLectura] = useState(null);
    const [loadingLectura, setLoadingLectura] = useState(false);
    const { canEditArbol, canDeleteArbol } = usePermissions();

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
      // Cargamos la última lectura en paralelo (sin bloquear la carga principal)
      try {
        setLoadingLectura(true);
        const lectura = await getUltimaLectura(id);
        setUltimaLectura(lectura);
      } catch {
        // La sección de lecturas simplemente no se muestra si falla
        setUltimaLectura(null);
      } finally {
        setLoadingLectura(false);
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
      navigate(-1);
    };

    const formatearFecha = (fecha) => {
      if (!fecha) return '-';
      return new Date(fecha).toLocaleDateString('es-ES', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
      });
    };

    const formatearTimestamp = (ts) => {
      if (!ts) return '-';
      return new Date(ts).toLocaleString('es-ES', {
        day: '2-digit', month: '2-digit', year: 'numeric',
        hour: '2-digit', minute: '2-digit',
      });
    };

    const tiempoRelativo = (ts) => {
      if (!ts) return null;
      const diffMs = Date.now() - new Date(ts).getTime();
      const diffMin = Math.round(diffMs / 60000);
      if (diffMin < 1) return 'hace menos de 1 min';
      if (diffMin < 60) return `hace ${diffMin} min`;
      const diffH = Math.round(diffMin / 60);
      if (diffH < 24) return `hace ${diffH} h`;
      return formatearTimestamp(ts);
    };

    const fueraDeRango = (valor, min, max) => {
      if (valor == null) return false;
      const v = parseFloat(valor);
      if (min != null && v < parseFloat(min)) return true;
      if (max != null && v > parseFloat(max)) return true;
      return false;
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
            <div className="flex items-center gap-3">
              <h1 className="text-3xl font-bold text-gray-800">{arbol.nombre}</h1>
              <span className="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-green-100 text-green-800">
                #{arbol.id}
              </span>
            </div>
            <p className="text-gray-600 mt-1">{arbol.especie}</p>
          </div>
          <div className="flex gap-2">
            <Button variant="outline" onClick={handleVolver}>
              Volver
            </Button>
            {canEditArbol(arbol.centroEducativo?.id) && (
              <Button variant="primary" onClick={handleEditar}>
                Editar
              </Button>
            )}
            {canDeleteArbol(arbol.centroEducativo?.id) && (
              <Button
                variant="danger"
                onClick={() => setShowDeleteConfirm(true)}
                disabled={deleting}
              >
                Eliminar
              </Button>
            )}
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

              {/* Absorción CO2 Anual */}
              <div>
                <label className="block text-sm font-medium text-gray-600 mb-1">
                  Absorción CO2 Anual
                </label>
                <p className="text-gray-900">
                  {arbol.absorcionCo2Anual != null
                    ? `${arbol.absorcionCo2Anual} kg CO2/año`
                    : 'No estimado'}
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

        {/* Sección Última Lectura */}
        <div className="border-t border-gray-200 p-6">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-xl font-semibold text-gray-800">Última Lectura</h2>
            <Button
              variant="outline"
              onClick={() => navigate(`/arboles/${id}/lecturas`)}
            >
              Ver histórico completo →
            </Button>
          </div>

          {loadingLectura ? (
            <div className="flex items-center gap-2 text-gray-500 text-sm">
              <Spinner size="sm" /> Cargando lectura...
            </div>
          ) : ultimaLectura === null ? (
            <p className="text-gray-500 text-sm italic">Sin lecturas registradas aún.</p>
          ) : (
            <>
              <p className="text-xs text-gray-500 mb-4">
                Última lectura: {tiempoRelativo(ultimaLectura.timestamp)}
              </p>
              <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                {/* Temperatura */}
                {(() => {
                  const alerta = fueraDeRango(ultimaLectura.temperatura, arbol.umbralTempMin, arbol.umbralTempMax);
                  return (
                    <div className={`rounded-lg p-4 flex flex-col gap-1 border ${alerta ? 'border-red-400 bg-red-50' : 'border-gray-200 bg-gray-50'}`}>
                      <span className="text-2xl">🌡</span>
                      <span className="text-xs font-medium text-gray-500">Temperatura</span>
                      <span className="text-xl font-bold text-gray-800">
                        {ultimaLectura.temperatura != null ? `${parseFloat(ultimaLectura.temperatura).toFixed(1)} °C` : '-'}
                      </span>
                      {alerta && (
                        <span className="text-xs font-semibold text-red-600 bg-red-100 rounded px-2 py-0.5 self-start">
                          Fuera de umbral
                        </span>
                      )}
                    </div>
                  );
                })()}

                {/* Humedad Ambiente */}
                {(() => {
                  const alerta = fueraDeRango(ultimaLectura.humedadAmbiente, arbol.umbralHumedadAmbienteMin, arbol.umbralHumedadAmbienteMax);
                  return (
                    <div className={`rounded-lg p-4 flex flex-col gap-1 border ${alerta ? 'border-red-400 bg-red-50' : 'border-gray-200 bg-gray-50'}`}>
                      <span className="text-2xl">💧</span>
                      <span className="text-xs font-medium text-gray-500">Humedad Ambiente</span>
                      <span className="text-xl font-bold text-gray-800">
                        {ultimaLectura.humedadAmbiente != null ? `${parseFloat(ultimaLectura.humedadAmbiente).toFixed(1)} %` : '-'}
                      </span>
                      {alerta && (
                        <span className="text-xs font-semibold text-red-600 bg-red-100 rounded px-2 py-0.5 self-start">
                          Fuera de umbral
                        </span>
                      )}
                    </div>
                  );
                })()}

                {/* Humedad Suelo */}
                <div className="rounded-lg p-4 flex flex-col gap-1 border border-gray-200 bg-gray-50">
                  <span className="text-2xl">🌱</span>
                  <span className="text-xs font-medium text-gray-500">Humedad Suelo</span>
                  <span className="text-xl font-bold text-gray-800">
                    {ultimaLectura.humedadSuelo != null ? `${parseFloat(ultimaLectura.humedadSuelo).toFixed(1)} %` : '-'}
                  </span>
                </div>

                {/* CO2 (solo si viene el valor) */}
                {ultimaLectura.co2 != null && (
                  <div className="rounded-lg p-4 flex flex-col gap-1 border border-gray-200 bg-gray-50">
                    <span className="text-2xl">💨</span>
                    <span className="text-xs font-medium text-gray-500">CO2</span>
                    <span className="text-xl font-bold text-gray-800">
                      {parseFloat(ultimaLectura.co2).toFixed(0)} ppm
                    </span>
                  </div>
                )}
              </div>
            </>
          )}
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
