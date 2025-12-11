import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getCentros } from '../../services/centrosService';
import Button from '../../components/common/Button';
import Spinner from '../../components/common/Spinner';
import Alert from '../../components/common/Alert';

function ListadoCentros() {
  // Estados
  const [centros, setCentros] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const navigate = useNavigate();

  // Cargar centros al montar el componente
  useEffect(() => {
    cargarCentros();
  }, []);

  // Cargar datos
  const cargarCentros = async () => {
    try {
      setLoading(true);
      setError('');

      const centrosData = await getCentros();
      setCentros(centrosData);
    } catch (err) {
      console.error('Error cargando centros:', err);
      setError('No se pudo conectar con el servidor. Si es la primera carga, puede estar iniciándose (30-60 seg). Recarga la página en unos momentos.');
    } finally {
      setLoading(false);
    }
  };

  // Navegar al detalle de un centro
  const handleVerDetalle = (id) => {
    navigate(`/centros/${id}`);
  };

  // Navegar al formulario de crear centro
  const handleNuevoCentro = () => {
    navigate('/centros/nuevo');
  };

  // Formatear fecha para mostrar
  const formatearFecha = (fecha) => {
    if (!fecha) return '-';
    return new Date(fecha).toLocaleDateString('es-ES');
  };

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="mb-6">
        <h1 className="text-3xl font-bold text-gray-800 mb-2">Gestión de Centros Educativos</h1>
        <p className="text-gray-600">Listado de centros educativos participantes en el proyecto</p>
      </div>

      {/* Barra de acciones */}
      <div className="mb-6 flex justify-end">
        {/* Botón añadir centro */}
        <Button
          variant="primary"
          onClick={handleNuevoCentro}
        >
          + Añadir Centro
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
          <Spinner size="lg" text="Cargando centros..." />
        </div>
      ) : (
        <>
          {/* Tabla de centros */}
          {centros.length === 0 ? (
            <div className="bg-white rounded-lg shadow p-8 text-center">
              <p className="text-gray-500 text-lg">
                No hay centros educativos registrados. Añade el primero.
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
                        Dirección
                      </th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Responsable
                      </th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Fecha Creación
                      </th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Acciones
                      </th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-200">
                    {centros.map((centro) => (
                      <tr
                        key={centro.id}
                        className="hover:bg-gray-50 cursor-pointer transition-colors"
                        onClick={() => handleVerDetalle(centro.id)}
                      >
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="text-sm font-medium text-gray-900">{centro.nombre}</div>
                        </td>
                        <td className="px-6 py-4">
                          <div className="text-sm text-gray-500">{centro.direccion}</div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="text-sm text-gray-500">{centro.responsable}</div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="text-sm text-gray-500">
                            {formatearFecha(centro.fechaCreacion)}
                          </div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm">
                          <Button
                            variant="outline"
                            size="sm"
                            onClick={(e) => {
                              e.stopPropagation();
                              handleVerDetalle(centro.id);
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
                {centros.map((centro) => (
                  <div
                    key={centro.id}
                    onClick={() => handleVerDetalle(centro.id)}
                    className="p-4 border-b border-gray-200 hover:bg-gray-50 cursor-pointer transition-colors"
                  >
                    <div className="flex justify-between items-start mb-2">
                      <h3 className="text-lg font-semibold text-gray-900">{centro.nombre}</h3>
                    </div>
                    <p className="text-sm text-gray-600 mb-1">
                      <span className="font-medium">Dirección:</span> {centro.direccion}
                    </p>
                    <p className="text-sm text-gray-600 mb-1">
                      <span className="font-medium">Responsable:</span> {centro.responsable}
                    </p>
                    <p className="text-sm text-gray-600 mb-3">
                      <span className="font-medium">Creación:</span>{' '}
                      {formatearFecha(centro.fechaCreacion)}
                    </p>
                    <Button
                      variant="outline"
                      size="sm"
                      fullWidth
                      onClick={(e) => {
                        e.stopPropagation();
                        handleVerDetalle(centro.id);
                      }}
                    >
                      Ver Detalle
                    </Button>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* Contador de resultados */}
          {centros.length > 0 && (
            <div className="mt-4 text-center text-sm text-gray-600">
              Mostrando {centros.length} {centros.length === 1 ? 'centro' : 'centros'}
            </div>
          )}
        </>
      )}
    </div>
  );
}

export default ListadoCentros;
