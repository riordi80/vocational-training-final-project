import { School, Plus } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { getCentros } from '../../services/centrosService';
import { useFetch } from '../../hooks/useFetch';
import Button from '../../components/common/Button';
import Spinner from '../../components/common/Spinner';
import Alert from '../../components/common/Alert';
import { usePermissions } from '../../hooks/usePermissions';
import { ISLAS } from '../../constants/islas';

function ListadoCentros() {
  const { data, loading, error, setError } = useFetch(getCentros);
  const centros = data ?? [];

  const navigate = useNavigate();
  const { isAdmin } = usePermissions();

  const handleVerDetalle = (id) => navigate(`/centros/${id}`);
  const handleNuevoCentro = () => navigate('/centros/nuevo');

  const formatearFecha = (fecha) => {
    if (!fecha) return '-';
    return new Date(fecha).toLocaleDateString('es-ES');
  };

  const formatearIsla = (isla) => {
    if (!isla) return '-';
    const found = ISLAS.find((i) => i.value === isla);
    return found ? found.label : isla;
  };

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="mb-6">
        <h1 className="text-3xl font-bold text-brand-primary mb-2 flex items-center gap-3">
            <School className="w-8 h-8 text-brand-secondary shrink-0" />
            Gestión de Centros Educativos
          </h1>
        <p className="text-gray-600">Listado de centros educativos participantes en el proyecto</p>
      </div>

      {/* Barra de acciones */}
      {isAdmin() && (
        <div className="mb-6 flex justify-end">
          <Button
            variant="primary"
            onClick={handleNuevoCentro}
          >
            <Plus className="w-4 h-4 mr-1 inline" /> Añadir Centro
          </Button>
        </div>
      )}

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
                <table className="min-w-full divide-y divide-brand-bg-green">
                  <thead className="bg-brand-primary">
                    <tr>
                      <th className="px-6 py-3 text-left text-xs font-medium text-white uppercase tracking-wider">
                        Nombre
                      </th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-white uppercase tracking-wider">
                        Isla
                      </th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-white uppercase tracking-wider">
                        Dirección
                      </th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-white uppercase tracking-wider">
                        Responsable
                      </th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-white uppercase tracking-wider">
                        Fecha Creación
                      </th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-white uppercase tracking-wider">
                        Acciones
                      </th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-brand-bg-green">
                    {centros.map((centro) => (
                      <tr
                        key={centro.id}
                        className="hover:bg-brand-primary/5 cursor-pointer transition-colors"
                        onClick={() => handleVerDetalle(centro.id)}
                      >
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="text-sm font-medium text-gray-900">{centro.nombre}</div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="text-sm text-gray-500">{formatearIsla(centro.isla)}</div>
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
                    className="p-4 border-b border-gray-200 hover:bg-brand-primary/5 cursor-pointer transition-colors"
                  >
                    <div className="flex justify-between items-start mb-2">
                      <h3 className="text-lg font-semibold text-gray-900">{centro.nombre}</h3>
                    </div>
                    {centro.isla && (
                      <p className="text-sm text-gray-600 mb-1">
                        <span className="font-medium">Isla:</span> {formatearIsla(centro.isla)}
                      </p>
                    )}
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
