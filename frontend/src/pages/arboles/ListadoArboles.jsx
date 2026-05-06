import { useState } from 'react';
import { TreePine, Plus } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { getArboles, getArbolesByCentro } from '../../services/arbolesService';
import { getCentros } from '../../services/centrosService';
import { useFetch } from '../../hooks/useFetch';
import Button from '../../components/common/Button';
import Spinner from '../../components/common/Spinner';
import Alert from '../../components/common/Alert';
import { usePermissions } from '../../hooks/usePermissions';
import { useAuth } from '../../context/AuthContext';

function ListadoArboles() {
  const { data, setData, loading, setLoading, error, setError } = useFetch(
    () => Promise.all([getArboles(), getCentros()])
  );

  const arboles = data?.[0] ?? [];
  const centros = data?.[1] ?? [];

  const [centroFiltro, setCentroFiltro] = useState('');

  const navigate = useNavigate();
  const { isAdmin, canCreateArbol } = usePermissions();
  const { user } = useAuth();

  const handleFiltroChange = async (e) => {
    const centroId = e.target.value;
    setCentroFiltro(centroId);

    try {
      setLoading(true);
      setError('');
      const arbolesData = centroId
        ? await getArbolesByCentro(centroId)
        : await getArboles();
      setData(prev => [arbolesData, prev?.[1] ?? []]);
    } catch (err) {
      console.error('Error filtrando árboles:', err);
      setError('Error al filtrar los árboles.');
    } finally {
      setLoading(false);
    }
  };

  const handleVerDetalle = (id) => navigate(`/arboles/${id}`);
  const handleNuevoArbol = () => navigate('/arboles/nuevo');

  const formatearFecha = (fecha) => {
    if (!fecha) return '-';
    return new Date(fecha).toLocaleDateString('es-ES');
  };

  const mostrarBotonAnadir = user && (
    isAdmin() || user.centros?.some(c => canCreateArbol(c.centroId))
  );

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="mb-6">
        <h1 className="text-3xl font-bold text-brand-primary mb-2 flex items-center gap-3">
            <TreePine className="w-8 h-8 text-brand-secondary shrink-0" />
            Gestión de Árboles
          </h1>
        <p className="text-gray-600">Listado de árboles monitorizados en centros educativos</p>
      </div>

      {/* Barra de acciones y filtros */}
      <div className="mb-6 flex flex-col md:flex-row md:items-center md:justify-between gap-4">
        {/* Filtro por centro */}
        <div className="flex items-center gap-2">
          <label htmlFor="filtro-centro" className="text-sm font-medium text-gray-700">
            Filtrar por centro:
          </label>
          <select
            id="filtro-centro"
            value={centroFiltro}
            onChange={handleFiltroChange}
            className="border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-brand-primary"
          >
            <option value="">Todos los centros</option>
            {centros.map((centro) => (
              <option key={centro.id} value={centro.id}>
                {centro.nombre}
              </option>
            ))}
          </select>
        </div>

        {/* Botón añadir árbol */}
        {mostrarBotonAnadir && (
        <Button
          variant="primary"
          onClick={handleNuevoArbol}
        >
          <Plus className="w-4 h-4 mr-1 inline" /> Añadir Árbol
        </Button> )}
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
          <Spinner size="lg" text="Cargando árboles..." />
        </div>
      ) : (
        <>
          {/* Tabla de árboles */}
          {arboles.length === 0 ? (
            <div className="bg-white rounded-lg shadow p-8 text-center">
              <p className="text-gray-500 text-lg">
                {centroFiltro
                  ? 'No hay árboles en este centro educativo.'
                  : 'No hay árboles registrados. Añade el primero.'}
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
                        ID
                      </th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-white uppercase tracking-wider">
                        Nombre
                      </th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-white uppercase tracking-wider">
                        Especie
                      </th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-white uppercase tracking-wider">
                        Centro Educativo
                      </th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-white uppercase tracking-wider">
                        Fecha Plantación
                      </th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-white uppercase tracking-wider">
                        Acciones
                      </th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-brand-bg-green">
                    {arboles.map((arbol) => (
                      <tr
                        key={arbol.id}
                        className="hover:bg-brand-primary/5 cursor-pointer transition-colors"
                        onClick={() => handleVerDetalle(arbol.id)}
                      >
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="text-sm font-medium text-gray-600">#{arbol.id}</div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="text-sm font-medium text-gray-900">{arbol.nombre}</div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="text-sm text-gray-500">{arbol.especie}</div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="text-sm text-gray-500">
                            {arbol.centroEducativo?.nombre || '-'}
                          </div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="text-sm text-gray-500">
                            {formatearFecha(arbol.fechaPlantacion)}
                          </div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm">
                          <Button
                            variant="outline"
                            size="sm"
                            onClick={(e) => {
                              e.stopPropagation();
                              handleVerDetalle(arbol.id);
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
                {arboles.map((arbol) => (
                  <div
                    key={arbol.id}
                    onClick={() => handleVerDetalle(arbol.id)}
                    className="p-4 border-b border-gray-200 hover:bg-brand-primary/5 cursor-pointer transition-colors"
                  >
                    <div className="flex justify-between items-start mb-2">
                      <div className="flex items-center gap-2">
                        <h3 className="text-lg font-semibold text-gray-900">{arbol.nombre}</h3>
                        <span className="inline-flex items-center px-2 py-0.5 rounded-full text-xs font-medium bg-brand-bg-green text-brand-primary">
                          #{arbol.id}
                        </span>
                      </div>
                    </div>
                    <p className="text-sm text-gray-600 mb-1">
                      <span className="font-medium">Especie:</span> {arbol.especie}
                    </p>
                    <p className="text-sm text-gray-600 mb-1">
                      <span className="font-medium">Centro:</span>{' '}
                      {arbol.centroEducativo?.nombre || '-'}
                    </p>
                    <p className="text-sm text-gray-600 mb-3">
                      <span className="font-medium">Plantación:</span>{' '}
                      {formatearFecha(arbol.fechaPlantacion)}
                    </p>
                    <Button
                      variant="outline"
                      size="sm"
                      fullWidth
                      onClick={(e) => {
                        e.stopPropagation();
                        handleVerDetalle(arbol.id);
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
          {arboles.length > 0 && (
            <div className="mt-4 text-center text-sm text-gray-600">
              Mostrando {arboles.length} {arboles.length === 1 ? 'árbol' : 'árboles'}
            </div>
          )}
        </>
      )}
    </div>
  );
}

export default ListadoArboles;
