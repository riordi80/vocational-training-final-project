import { useState, useEffect } from 'react';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import { getCentroById, deleteCentro, getArbolesByCentro } from '../../services/centrosService';
import Button from '../../components/common/Button';
import Spinner from '../../components/common/Spinner';
import Alert from '../../components/common/Alert';
import { usePermissions } from '../../hooks/usePermissions';
import { ISLAS } from '../../constants/islas';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L from 'leaflet';

// Fix para el icono por defecto de Leaflet en builds con bundlers (Vite/Webpack)
delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png',
  iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
  shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
});

function DetalleCentro() {
  const { id } = useParams();
  const navigate = useNavigate();
  const location = useLocation();

  const [centro, setCentro] = useState(null);
  const [arboles, setArboles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const { isAdmin, canManageCenter } = usePermissions();

  useEffect(() => {
    cargarCentro();
    cargarArboles();

    // Mostrar mensaje de éxito si viene del formulario
    if (location.state?.mensaje) {
      setSuccessMessage(location.state.mensaje);
      // Limpiar el state después de mostrarlo
      window.history.replaceState({}, document.title);
    }
  }, [id, location]);

  const cargarCentro = async () => {
    try {
      setLoading(true);
      setError('');
      const centroData = await getCentroById(id);
      setCentro(centroData);
    } catch (err) {
      console.error('Error cargando centro:', err);
      setError('No se pudo cargar el centro educativo.');
    } finally {
      setLoading(false);
    }
  };

  const cargarArboles = async () => {
    try {
      const arbolesData = await getArbolesByCentro(id);
      setArboles(arbolesData);
    } catch (err) {
      console.error('Error cargando árboles:', err);
      // No mostramos error aquí, puede ser que simplemente no tenga árboles
    }
  };

  const handleVolver = () => {
    navigate('/centros');
  };

  const handleEditar = () => {
    navigate(`/centros/${id}/editar`);
  };

  const handleEliminar = async () => {
    try {
      await deleteCentro(id);
      navigate('/centros', {
        state: { mensaje: 'Centro educativo eliminado exitosamente' }
      });
    } catch (err) {
      console.error('Error eliminando centro:', err);
      setError('No se pudo eliminar el centro educativo.');
      setShowDeleteModal(false);
    }
  };

  const handleVerArbol = (arbolId) => {
    navigate(`/arboles/${arbolId}`);
  };

  const formatearFecha = (fecha) => {
    if (!fecha) return '-';
    const date = new Date(fecha);
    return date.toLocaleDateString('es-ES', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  };

  const formatearIsla = (isla) => {
    if (!isla) return null;
    const found = ISLAS.find((i) => i.value === isla);
    return found ? found.label : isla;
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center py-12">
        <Spinner size="lg" text="Cargando centro..." />
      </div>
    );
  }

  if (error && !centro) {
    return (
      <div className="container mx-auto px-4 py-8">
        <Alert type="error" message={error} />
        <Button onClick={handleVolver} variant="outline" className="mt-4">
          Volver al listado
        </Button>
      </div>
    );
  }

  if (!centro) {
    return (
      <div className="container mx-auto px-4 py-8">
        <Alert type="error" message="Centro educativo no encontrado" />
        <Button onClick={handleVolver} variant="outline" className="mt-4">
          Volver al listado
        </Button>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-8">
      {/* Mensaje de éxito */}
      {successMessage && (
        <div className="mb-4">
          <Alert
            type="success"
            message={successMessage}
            dismissible
            onClose={() => setSuccessMessage('')}
          />
        </div>
      )}

      {/* Mensaje de error */}
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

      {/* Encabezado */}
      <div className="mb-6 flex flex-col md:flex-row md:items-center md:justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-800 mb-2">
            {centro.nombre}
            {centro.isla && (
              <span className="text-xl font-normal text-gray-500"> · {formatearIsla(centro.isla)}</span>
            )}
          </h1>
        </div>
        <div className="flex gap-2 mt-4 md:mt-0">
          <Button onClick={handleVolver} variant="outline">
            Volver
          </Button>
          {canManageCenter(centro.id) && (
            <Button onClick={handleEditar} variant="primary">
              Editar
            </Button>
          )}
          {isAdmin() && (
            <Button onClick={() => setShowDeleteModal(true)} variant="danger">
              Eliminar
            </Button>
          )}
        </div>
      </div>

      {/* Contenido principal */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Información General */}
        <div className="bg-white rounded-lg shadow p-6">
          <h2 className="text-xl font-semibold text-gray-800 mb-4 border-b pb-2">
            Información General
          </h2>
          <div className="space-y-3">
            <div>
              <label className="text-sm font-medium text-gray-500">Responsable</label>
              <p className="text-gray-900">{centro.responsable}</p>
            </div>
            {centro.telefono && (
              <div>
                <label className="text-sm font-medium text-gray-500">Teléfono</label>
                <p className="text-gray-900">{centro.telefono}</p>
              </div>
            )}
            {centro.email && (
              <div>
                <label className="text-sm font-medium text-gray-500">Correo Electrónico</label>
                <p className="text-gray-900">{centro.email}</p>
              </div>
            )}
            <div>
              <label className="text-sm font-medium text-gray-500">Fecha de Creación</label>
              <p className="text-gray-900">{formatearFecha(centro.fechaCreacion)}</p>
            </div>
          </div>
        </div>

        {/* Ubicación Geográfica */}
        <div className="bg-white rounded-lg shadow p-6">
          <h2 className="text-xl font-semibold text-gray-800 mb-4 border-b pb-2">
            Ubicación Geográfica
          </h2>
          <div className="grid grid-cols-2 gap-3 mb-4">
            <div>
              <label className="text-sm font-medium text-gray-500">Dirección</label>
              <p className="text-gray-900">{centro.direccion}</p>
            </div>
            {centro.codigoPostal && (
              <div>
                <label className="text-sm font-medium text-gray-500">Código Postal</label>
                <p className="text-gray-900">{centro.codigoPostal}</p>
              </div>
            )}
            {centro.poblacion && (
              <div>
                <label className="text-sm font-medium text-gray-500">Población</label>
                <p className="text-gray-900">{centro.poblacion}</p>
              </div>
            )}
            {centro.provincia && (
              <div>
                <label className="text-sm font-medium text-gray-500">Provincia</label>
                <p className="text-gray-900">{centro.provincia}</p>
              </div>
            )}
          </div>
          {centro.latitud && centro.longitud && (
            <div className="rounded overflow-hidden border border-gray-200" style={{ height: '200px' }}>
              <MapContainer
                center={[parseFloat(centro.latitud), parseFloat(centro.longitud)]}
                zoom={15}
                style={{ height: '100%', width: '100%' }}
                scrollWheelZoom={false}
              >
                <TileLayer
                  attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
                  url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                />
                <Marker position={[parseFloat(centro.latitud), parseFloat(centro.longitud)]}>
                  <Popup>{centro.nombre}</Popup>
                </Marker>
              </MapContainer>
            </div>
          )}
        </div>
      </div>

      {/* Árboles del Centro */}
      <div className="mt-6 bg-white rounded-lg shadow p-6">
        <h2 className="text-xl font-semibold text-gray-800 mb-4 border-b pb-2">
          Árboles del Centro ({arboles.length})
        </h2>
        {arboles.length === 0 ? (
          <p className="text-gray-500 text-center py-4">
            Este centro aún no tiene árboles registrados.
          </p>
        ) : (
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Nombre
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Especie
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Ubicación
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Acciones
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {arboles.map((arbol) => (
                  <tr
                    key={arbol.id}
                    className="hover:bg-gray-50 cursor-pointer"
                    onClick={() => handleVerArbol(arbol.id)}
                  >
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                      {arbol.nombre}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {arbol.especie}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {arbol.ubicacionEspecifica || '-'}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm">
                      <Button
                        variant="outline"
                        size="sm"
                        onClick={(e) => {
                          e.stopPropagation();
                          handleVerArbol(arbol.id);
                        }}
                      >
                        Ver Detalle
                      </Button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {/* Modal de confirmación para eliminar */}
      {showDeleteModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
            <h3 className="text-lg font-semibold text-gray-900 mb-2">
              Confirmar Eliminación
            </h3>
            <p className="text-gray-600 mb-4">
              ¿Estás seguro de que deseas eliminar el centro educativo <strong>{centro.nombre}</strong>?
            </p>
            {arboles.length > 0 && (
              <div className="bg-red-50 border border-red-200 rounded p-3 mb-4">
                <p className="text-sm text-red-800 font-medium">
                  ⚠️ Advertencia: Este centro tiene {arboles.length} {arboles.length === 1 ? 'árbol' : 'árboles'} asociado(s).
                </p>
                <p className="text-sm text-red-700 mt-1">
                  Al eliminar el centro, se eliminarán también todos sus árboles.
                </p>
              </div>
            )}
            <p className="text-sm text-gray-500 mb-6">
              Esta acción no se puede deshacer.
            </p>
            <div className="flex gap-3 justify-end">
              <Button
                onClick={() => setShowDeleteModal(false)}
                variant="outline"
              >
                Cancelar
              </Button>
              <Button
                onClick={handleEliminar}
                variant="danger"
              >
                Eliminar
              </Button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default DetalleCentro;
