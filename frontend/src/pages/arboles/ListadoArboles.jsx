  import { useState, useEffect } from 'react';
  import { useNavigate } from 'react-router-dom';
  import { getArboles, getArbolesByCentro } from '../../services/arbolesService';
  import { getCentros } from '../../services/centrosService';
  import Button from '../../components/common/Button';
  import Spinner from '../../components/common/Spinner';
  import Alert from '../../components/common/Alert';

  function ListadoArboles() {
    // Estados
    const [arboles, setArboles] = useState([]);
    const [centros, setCentros] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [centroFiltro, setCentroFiltro] = useState('');

    const navigate = useNavigate();

    // Cargar árboles y centros al montar el componente
    useEffect(() => {
      cargarDatos();
    }, []);

    // Cargar datos iniciales
    const cargarDatos = async () => {
      try {
        setLoading(true);
        setError('');

        // Cargar árboles y centros en paralelo
        const [arbolesData, centrosData] = await Promise.all([
          getArboles(),
          getCentros()
        ]);

        setArboles(arbolesData);
        setCentros(centrosData);
      } catch (err) {
        console.error('Error cargando datos:', err);
        setError('No se pudo conectar con el servidor. Si es la primera carga, puede estar iniciándose (30-60 seg). Recarga la página en unos momentos.');
      } finally {
        setLoading(false);
      }
    };

    // Manejar cambio de filtro por centro
    const handleFiltroChange = async (e) => {
      const centroId = e.target.value;
      setCentroFiltro(centroId);

      try {
        setLoading(true);
        setError('');

        let arbolesData;
        if (centroId === '') {
          // Si no hay filtro, cargar todos
          arbolesData = await getArboles();
        } else {
          // Si hay filtro, cargar por centro
          arbolesData = await getArbolesByCentro(centroId);
        }

        setArboles(arbolesData);
      } catch (err) {
        console.error('Error filtrando árboles:', err);
        setError('Error al filtrar los árboles.');
      } finally {
        setLoading(false);
      }
    };

    // Navegar al detalle de un árbol
    const handleVerDetalle = (id) => {
      navigate(`/arboles/${id}`);
    };

    // Navegar al formulario de crear árbol
    const handleNuevoArbol = () => {
      navigate('/arboles/nuevo');
    };

    // Formatear fecha para mostrar
    const formatearFecha = (fecha) => {
      if (!fecha) return '-';
      return new Date(fecha).toLocaleDateString('es-ES');
    };

    return (
      <div className="container mx-auto px-4 py-8">
        <div className="mb-6">
          <h1 className="text-3xl font-bold text-gray-800 mb-2">Gestión de Árboles</h1>
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
              className="border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-green-500"
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
          <Button
            variant="primary"
            onClick={handleNuevoArbol}
          >
            + Añadir Árbol
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
                          Centro Educativo
                        </th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                          Fecha Plantación
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
                          className="hover:bg-gray-50 cursor-pointer transition-colors"
                          onClick={() => handleVerDetalle(arbol.id)}
                        >
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
                      className="p-4 border-b border-gray-200 hover:bg-gray-50 cursor-pointer transition-colors"
                    >
                      <div className="flex justify-between items-start mb-2">
                        <h3 className="text-lg font-semibold text-gray-900">{arbol.nombre}</h3>
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
