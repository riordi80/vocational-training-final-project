import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getUsuarios } from '../../services/usuariosService';
import Button from '../../components/common/Button';
import Spinner from '../../components/common/Spinner';
import Alert from '../../components/common/Alert';

function ListadoUsuarios() {
  const [usuarios, setUsuarios] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const navigate = useNavigate();

  useEffect(() => {
    cargarUsuarios();
  }, []);

  const cargarUsuarios = async () => {
    try {
      setLoading(true);
      setError('');
      const data = await getUsuarios();
      setUsuarios(data);
    } catch (err) {
      console.error('Error cargando usuarios:', err);
      setError('No se pudo conectar con el servidor. Si es la primera carga, puede estar iniciándose (30-60 seg). Recarga la página en unos momentos.');
    } finally {
      setLoading(false);
    }
  };

  const handleVerDetalle = (id) => {
    navigate(`/usuarios/${id}`);
  };

  const handleNuevoUsuario = () => {
    navigate('/usuarios/nuevo');
  };

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="mb-6">
        <h1 className="text-3xl font-bold text-gray-800 mb-2">Gestión de Usuarios</h1>
        <p className="text-gray-600">Listado de usuarios del sistema</p>
      </div>

      {/* Barra de acciones */}
      <div className="mb-6 flex justify-end">
        <Button variant="primary" onClick={handleNuevoUsuario}>
          + Añadir Usuario
        </Button>
      </div>

      {/* Mensaje de error */}
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

      {/* Loading spinner */}
      {loading ? (
        <div className="flex justify-center items-center py-12">
          <Spinner size="lg" text="Cargando usuarios..." />
        </div>
      ) : (
        <>
          {usuarios.length === 0 ? (
            <div className="bg-white rounded-lg shadow p-8 text-center">
              <p className="text-gray-500 text-lg">
                No hay usuarios registrados.
              </p>
            </div>
          ) : (
            <div className="bg-white rounded-lg shadow overflow-hidden">
              {/* Tabla - versión desktop */}
              <div className="hidden md:block overflow-x-auto">
                <table className="min-w-full divide-y divide-gray-200">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Nombre
                      </th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Email
                      </th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Rol
                      </th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Activo
                      </th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Acciones
                      </th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-200">
                    {usuarios.map((usuario) => (
                      <tr
                        key={usuario.id}
                        className="hover:bg-gray-50 cursor-pointer transition-colors"
                        onClick={() => handleVerDetalle(usuario.id)}
                      >
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="text-sm font-medium text-gray-900">{usuario.nombre}</div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="text-sm text-gray-500">{usuario.email}</div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
                            usuario.rol === 'ADMIN'
                              ? 'bg-purple-100 text-purple-800'
                              : 'bg-blue-100 text-blue-800'
                          }`}>
                            {usuario.rol}
                          </span>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
                            usuario.activo
                              ? 'bg-green-100 text-green-800'
                              : 'bg-red-100 text-red-800'
                          }`}>
                            {usuario.activo ? 'Sí' : 'No'}
                          </span>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm">
                          <Button
                            variant="outline"
                            size="sm"
                            onClick={(e) => {
                              e.stopPropagation();
                              handleVerDetalle(usuario.id);
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

              {/* Cards - versión mobile */}
              <div className="md:hidden">
                {usuarios.map((usuario) => (
                  <div
                    key={usuario.id}
                    onClick={() => handleVerDetalle(usuario.id)}
                    className="p-4 border-b border-gray-200 hover:bg-gray-50 cursor-pointer transition-colors"
                  >
                    <div className="flex justify-between items-start mb-2">
                      <h3 className="text-lg font-semibold text-gray-900">{usuario.nombre}</h3>
                      <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
                        usuario.rol === 'ADMIN'
                          ? 'bg-purple-100 text-purple-800'
                          : 'bg-blue-100 text-blue-800'
                      }`}>
                        {usuario.rol}
                      </span>
                    </div>
                    <p className="text-sm text-gray-600 mb-1">
                      <span className="font-medium">Email:</span> {usuario.email}
                    </p>
                    <p className="text-sm text-gray-600 mb-3">
                      <span className="font-medium">Activo:</span> {usuario.activo ? 'Sí' : 'No'}
                    </p>
                    <Button
                      variant="outline"
                      size="sm"
                      fullWidth
                      onClick={(e) => {
                        e.stopPropagation();
                        handleVerDetalle(usuario.id);
                      }}
                    >
                      Ver Detalle
                    </Button>
                  </div>
                ))}
              </div>
            </div>
          )}

          {usuarios.length > 0 && (
            <div className="mt-4 text-center text-sm text-gray-600">
              Mostrando {usuarios.length} {usuarios.length === 1 ? 'usuario' : 'usuarios'}
            </div>
          )}
        </>
      )}
    </div>
  );
}

export default ListadoUsuarios;
