import { useState, useEffect } from 'react';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import { getUsuarioById, updateUsuario, deleteUsuario } from '../../services/usuariosService';
import { getCentrosDeUsuario, asignarCentro, desasignarCentro } from '../../services/usuarioCentroService';
import { getCentros } from '../../services/centrosService';
import Button from '../../components/common/Button';
import Spinner from '../../components/common/Spinner';
import Alert from '../../components/common/Alert';

function DetalleUsuario() {
  const { id } = useParams();
  const navigate = useNavigate();
  const location = useLocation();

  const [usuario, setUsuario] = useState(null);
  const [centrosAsignados, setCentrosAsignados] = useState([]);
  const [todosCentros, setTodosCentros] = useState([]);
  const [centroSeleccionado, setCentroSeleccionado] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const [showDeleteModal, setShowDeleteModal] = useState(false);

  useEffect(() => {
    cargarUsuario();

    if (location.state?.mensaje) {
      setSuccessMessage(location.state.mensaje);
      window.history.replaceState({}, document.title);
    }
  }, [id, location]);

  const cargarUsuario = async () => {
    try {
      setLoading(true);
      setError('');
      const usuarioData = await getUsuarioById(id);
      setUsuario(usuarioData);

      // Si es coordinador, cargar centros asignados y todos los centros
      if (usuarioData.rol === 'COORDINADOR') {
        const [asignados, todos] = await Promise.all([
          getCentrosDeUsuario(id),
          getCentros()
        ]);
        setCentrosAsignados(asignados);
        setTodosCentros(todos);
      }
    } catch (err) {
      console.error('Error cargando usuario:', err);
      setError('No se pudo cargar el usuario.');
    } finally {
      setLoading(false);
    }
  };

  const handleVolver = () => {
    navigate(-1);
  };

  const handleEditar = () => {
    navigate(`/usuarios/${id}/editar`);
  };

  const handleToggleActivo = async () => {
    try {
      setError('');
      await updateUsuario(id, { ...usuario, activo: !usuario.activo });
      await cargarUsuario();
      setSuccessMessage(`Usuario ${!usuario.activo ? 'activado' : 'desactivado'} correctamente`);
    } catch (err) {
      console.error('Error actualizando estado del usuario:', err);
      setError('No se pudo actualizar el estado del usuario.');
    }
  };

  const handleEliminar = async () => {
    try {
      await deleteUsuario(id);
      navigate('/usuarios', {
        state: { mensaje: 'Usuario eliminado exitosamente' }
      });
    } catch (err) {
      console.error('Error eliminando usuario:', err);
      setError('No se pudo eliminar el usuario.');
      setShowDeleteModal(false);
    }
  };

  const handleAsignarCentro = async () => {
    if (!centroSeleccionado) return;
    try {
      setError('');
      await asignarCentro(id, parseInt(centroSeleccionado));
      setCentroSeleccionado('');
      // Recargar centros asignados
      const asignados = await getCentrosDeUsuario(id);
      setCentrosAsignados(asignados);
      setSuccessMessage('Centro asignado correctamente');
    } catch (err) {
      console.error('Error asignando centro:', err);
      setError('No se pudo asignar el centro.');
    }
  };

  const handleDesasignarCentro = async (asignacionId) => {
    try {
      setError('');
      await desasignarCentro(asignacionId);
      const asignados = await getCentrosDeUsuario(id);
      setCentrosAsignados(asignados);
      setSuccessMessage('Centro desasignado correctamente');
    } catch (err) {
      console.error('Error desasignando centro:', err);
      setError('No se pudo desasignar el centro.');
    }
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

  // Filtrar centros que no están ya asignados
  const centrosDisponibles = todosCentros.filter(
    centro => !centrosAsignados.some(asig => asig.centroEducativo?.id === centro.id)
  );

  if (loading) {
    return (
      <div className="flex justify-center items-center py-12">
        <Spinner size="lg" text="Cargando usuario..." />
      </div>
    );
  }

  if (error && !usuario) {
    return (
      <div className="container mx-auto px-4 py-8">
        <Alert type="error" message={error} />
        <Button onClick={handleVolver} variant="outline" className="mt-4">
          Volver al listado
        </Button>
      </div>
    );
  }

  if (!usuario) {
    return (
      <div className="container mx-auto px-4 py-8">
        <Alert type="error" message="Usuario no encontrado" />
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
          <h1 className="text-3xl font-bold text-gray-800 mb-2">{usuario.nombre}</h1>
          <p className="text-gray-600">Detalles del usuario</p>
        </div>
        <div className="flex gap-2 mt-4 md:mt-0">
          <Button onClick={handleVolver} variant="outline">
            Volver
          </Button>
          <Button onClick={handleEditar} variant="primary">
            Editar
          </Button>
          <Button onClick={() => setShowDeleteModal(true)} variant="danger">
            Eliminar
          </Button>
        </div>
      </div>

      {/* Información del usuario */}
      <div className="bg-white rounded-lg shadow p-6 mb-6">
        <div className="flex items-center justify-between mb-4 border-b pb-2">
          <h2 className="text-xl font-semibold text-gray-800">
            Información General
          </h2>
          <div className="flex items-center gap-2">
            <button
              onClick={handleToggleActivo}
              className={`relative inline-flex h-6 w-11 items-center rounded-full transition-colors focus:outline-none ${
                usuario.activo ? 'bg-green-500' : 'bg-gray-300'
              }`}
              title={usuario.activo ? 'Desactivar usuario' : 'Activar usuario'}
            >
              <span className={`inline-block h-4 w-4 transform rounded-full bg-white shadow transition-transform ${
                usuario.activo ? 'translate-x-6' : 'translate-x-1'
              }`} />
            </button>
            <span className={`text-sm font-medium ${
              usuario.activo ? 'text-green-700' : 'text-gray-400'
            }`}>
              {usuario.activo ? 'Activo' : 'Inactivo'}
            </span>
          </div>
        </div>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label className="text-sm font-medium text-gray-500">Nombre</label>
            <p className="text-gray-900">{usuario.nombre}</p>
          </div>
          <div>
            <label className="text-sm font-medium text-gray-500">Email</label>
            <p className="text-gray-900">{usuario.email}</p>
          </div>
          <div>
            <label className="text-sm font-medium text-gray-500">Rol</label>
            <p>
              <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
                usuario.rol === 'ADMIN'
                  ? 'bg-purple-100 text-purple-800'
                  : 'bg-blue-100 text-blue-800'
              }`}>
                {usuario.rol}
              </span>
            </p>
          </div>
          <div>
            <label className="text-sm font-medium text-gray-500">Fecha de Creación</label>
            <p className="text-gray-900">{formatearFecha(usuario.fechaCreacion)}</p>
          </div>
        </div>
      </div>

      {/* Sección de centros asignados (solo para COORDINADOR) */}
      {usuario.rol === 'COORDINADOR' && (
        <div className="bg-white rounded-lg shadow p-6">
          <h2 className="text-xl font-semibold text-gray-800 mb-4 border-b pb-2">
            Centros Asignados ({centrosAsignados.length})
          </h2>

          {/* Formulario de asignación */}
          <div className="flex flex-col sm:flex-row gap-2 mb-4">
            <select
              value={centroSeleccionado}
              onChange={(e) => setCentroSeleccionado(e.target.value)}
              className="flex-1 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
            >
              <option value="">Selecciona un centro...</option>
              {centrosDisponibles.map(centro => (
                <option key={centro.id} value={centro.id}>
                  {centro.nombre}
                </option>
              ))}
            </select>
            <Button
              variant="primary"
              onClick={handleAsignarCentro}
              disabled={!centroSeleccionado}
            >
              Asignar
            </Button>
          </div>

          {/* Lista de centros asignados */}
          {centrosAsignados.length === 0 ? (
            <p className="text-gray-500 text-center py-4">
              Este coordinador no tiene centros asignados.
            </p>
          ) : (
            <div className="overflow-x-auto">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Centro
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Acciones
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {centrosAsignados.map((asignacion) => (
                    <tr key={asignacion.id} className="hover:bg-gray-50">
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        {asignacion.centroEducativo?.nombre || `Centro #${asignacion.centroEducativo?.id}`}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm">
                        <Button
                          variant="danger"
                          size="sm"
                          onClick={() => handleDesasignarCentro(asignacion.id)}
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
      )}

      {/* Modal de confirmación para eliminar */}
      {showDeleteModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
            <h3 className="text-lg font-semibold text-gray-900 mb-2">
              Confirmar Eliminación
            </h3>
            <p className="text-gray-600 mb-4">
              ¿Estás seguro de que deseas eliminar al usuario <strong>{usuario.nombre}</strong>?
            </p>
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

export default DetalleUsuario;
