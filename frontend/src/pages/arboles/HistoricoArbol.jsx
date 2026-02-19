import { useState, useEffect, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from 'recharts';
import { getArbolById } from '../../services/arbolesService';
import { getLecturasByArbol, getLecturasByArbolRango } from '../../services/lecturasService';
import Spinner from '../../components/common/Spinner';
import Alert from '../../components/common/Alert';
import Button from '../../components/common/Button';

const PAGE_SIZE = 20;

function formatTimestamp(ts) {
  if (!ts) return '-';
  return new Date(ts).toLocaleString('es-ES', {
    day: '2-digit', month: '2-digit', year: 'numeric',
    hour: '2-digit', minute: '2-digit',
  });
}

function formatTimestampShort(ts) {
  if (!ts) return '-';
  return new Date(ts).toLocaleString('es-ES', {
    day: '2-digit', month: '2-digit',
    hour: '2-digit', minute: '2-digit',
  });
}

function HistoricoArbol() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [arbol, setArbol] = useState(null);
  const [lecturas, setLecturas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  // Paginación
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  // Filtro fechas
  const [desde, setDesde] = useState('');
  const [hasta, setHasta] = useState('');
  const [modoFiltro, setModoFiltro] = useState(false); // true cuando el filtro está activo
  const [filtrando, setFiltrando] = useState(false);

  // Carga inicial del árbol (solo una vez)
  useEffect(() => {
    getArbolById(id)
      .then(setArbol)
      .catch(() => setError('No se pudo cargar la información del árbol.'));
  }, [id]);

  // Carga de lecturas cuando cambia la página o el id
  const cargarPagina = useCallback(async (page) => {
    try {
      setLoading(true);
      setError('');
      const data = modoFiltro
        ? await getLecturasByArbolRango(id, `${desde}T00:00:00`, `${hasta}T23:59:59`, page, PAGE_SIZE)
        : await getLecturasByArbol(id, page, PAGE_SIZE);
      setLecturas(data.content);
      setTotalPages(data.totalPages);
      setTotalElements(data.totalElements);
      setCurrentPage(data.number);
    } catch (err) {
      console.error('Error cargando lecturas:', err);
      setError('No se pudieron cargar las lecturas. Inténtalo de nuevo.');
    } finally {
      setLoading(false);
    }
  }, [id, modoFiltro, desde, hasta]);

  useEffect(() => {
    cargarPagina(currentPage);
  }, [cargarPagina, currentPage]);

  const handleFiltrar = async () => {
    if (!desde || !hasta) return;
    try {
      setFiltrando(true);
      setError('');
      const data = await getLecturasByArbolRango(id, `${desde}T00:00:00`, `${hasta}T23:59:59`, 0, PAGE_SIZE);
      setLecturas(data.content);
      setTotalPages(data.totalPages);
      setTotalElements(data.totalElements);
      setCurrentPage(0);
      setModoFiltro(true);
    } catch (err) {
      console.error('Error filtrando lecturas:', err);
      setError('Error al filtrar por fechas. Inténtalo de nuevo.');
    } finally {
      setFiltrando(false);
    }
  };

  const handleLimpiar = () => {
    setDesde('');
    setHasta('');
    setModoFiltro(false);
    setCurrentPage(0);
    // El useEffect se encargará de recargar gracias al cambio de modoFiltro
  };

  // La gráfica muestra solo la página actual (ya viene en orden DESC, invertimos para cronológico)
  const datosGrafica = [...lecturas].reverse().map((l) => ({
    tiempo: formatTimestampShort(l.timestamp),
    temperatura: l.temperatura != null ? parseFloat(l.temperatura) : null,
    humedadAmbiente: l.humedadAmbiente != null ? parseFloat(l.humedadAmbiente) : null,
  }));

  if (loading && lecturas.length === 0) {
    return (
      <div className="flex justify-center items-center py-12">
        <Spinner size="lg" text="Cargando histórico..." />
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-8">
      {/* Cabecera */}
      <div className="mb-6 flex flex-col md:flex-row md:items-center md:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-gray-800">Histórico de lecturas</h1>
          {arbol && (
            <p className="text-gray-600 mt-1">
              {arbol.nombre} — {arbol.especie}
            </p>
          )}
        </div>
        <Button variant="outline" onClick={() => navigate(`/arboles/${id}`)}>
          ← Volver al árbol
        </Button>
      </div>

      {error && (
        <Alert type="error" message={error} dismissible onClose={() => setError('')} />
      )}

      {/* Filtro por fechas */}
      <div className="bg-white rounded-lg shadow-md p-4 mb-6">
        <h2 className="text-sm font-semibold text-gray-700 mb-3">Filtrar por rango de fechas</h2>
        <div className="flex flex-col sm:flex-row gap-3 items-end">
          <div className="flex-1">
            <label className="block text-xs text-gray-600 mb-1">Desde</label>
            <input
              type="date"
              value={desde}
              onChange={(e) => setDesde(e.target.value)}
              className="w-full border border-gray-300 rounded-md px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
            />
          </div>
          <div className="flex-1">
            <label className="block text-xs text-gray-600 mb-1">Hasta</label>
            <input
              type="date"
              value={hasta}
              onChange={(e) => setHasta(e.target.value)}
              className="w-full border border-gray-300 rounded-md px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
            />
          </div>
          <div className="flex gap-2">
            <Button
              variant="primary"
              onClick={handleFiltrar}
              disabled={!desde || !hasta || filtrando}
            >
              {filtrando ? 'Filtrando...' : 'Filtrar'}
            </Button>
            <Button variant="outline" onClick={handleLimpiar} disabled={filtrando}>
              Limpiar
            </Button>
          </div>
        </div>
        {modoFiltro && (
          <p className="text-xs text-green-700 mt-2 font-medium">
            Filtro activo: {desde} → {hasta}
          </p>
        )}
      </div>

      {totalElements === 0 && !loading ? (
        <div className="bg-white rounded-lg shadow-md p-8 text-center text-gray-500">
          {modoFiltro
            ? 'No hay lecturas en el rango de fechas seleccionado.'
            : 'Aún no hay lecturas registradas para este árbol.'}
        </div>
      ) : (
        <>
          {/* Gráfica de la página actual */}
          {datosGrafica.length > 0 && (
            <div className="bg-white rounded-lg shadow-md p-6 mb-6">
              <h2 className="text-lg font-semibold text-gray-800 mb-1">
                Temperatura y Humedad Ambiente
              </h2>
              <p className="text-xs text-gray-500 mb-4">
                Mostrando página {currentPage + 1} de {totalPages}
              </p>
              <ResponsiveContainer width="100%" height={300}>
                <LineChart data={datosGrafica} margin={{ top: 5, right: 20, left: 0, bottom: 5 }}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="tiempo" tick={{ fontSize: 11 }} interval="preserveStartEnd" />
                  <YAxis tick={{ fontSize: 11 }} />
                  <Tooltip />
                  <Legend />
                  <Line
                    type="monotone"
                    dataKey="temperatura"
                    name="Temperatura (°C)"
                    stroke="#f97316"
                    dot={false}
                    connectNulls
                  />
                  <Line
                    type="monotone"
                    dataKey="humedadAmbiente"
                    name="Humedad Amb. (%)"
                    stroke="#3b82f6"
                    dot={false}
                    connectNulls
                  />
                </LineChart>
              </ResponsiveContainer>
            </div>
          )}

          {/* Tabla */}
          <div className="bg-white rounded-lg shadow-md overflow-hidden">
            {/* Cabecera tabla con info y paginación */}
            <div className="p-6 pb-4 flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3">
              <h2 className="text-lg font-semibold text-gray-800">
                Lecturas
                <span className="ml-2 text-sm font-normal text-gray-500">
                  ({totalElements} en total)
                </span>
              </h2>
              <Paginacion
                currentPage={currentPage}
                totalPages={totalPages}
                loading={loading}
                onPageChange={(p) => setCurrentPage(p)}
              />
            </div>

            {loading ? (
              <div className="flex justify-center py-8">
                <Spinner size="md" text="Cargando..." />
              </div>
            ) : (
              <div className="overflow-x-auto">
                <table className="min-w-full divide-y divide-gray-200 text-sm">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Fecha/Hora
                      </th>
                      <th className="px-4 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Temp. (°C)
                      </th>
                      <th className="px-4 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Hum. Ambiente (%)
                      </th>
                      <th className="px-4 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Hum. Suelo (%)
                      </th>
                      <th className="px-4 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                        CO2 (ppm)
                      </th>
                      <th className="px-4 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Diám. Tronco (mm)
                      </th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-100">
                    {lecturas.map((lectura) => (
                      <tr key={lectura.id} className="hover:bg-gray-50">
                        <td className="px-4 py-3 text-gray-700 whitespace-nowrap">
                          {formatTimestamp(lectura.timestamp)}
                        </td>
                        <td className="px-4 py-3 text-right text-gray-700">
                          {lectura.temperatura != null
                            ? parseFloat(lectura.temperatura).toFixed(1)
                            : '-'}
                        </td>
                        <td className="px-4 py-3 text-right text-gray-700">
                          {lectura.humedadAmbiente != null
                            ? parseFloat(lectura.humedadAmbiente).toFixed(1)
                            : '-'}
                        </td>
                        <td className="px-4 py-3 text-right text-gray-700">
                          {lectura.humedadSuelo != null
                            ? parseFloat(lectura.humedadSuelo).toFixed(1)
                            : '-'}
                        </td>
                        <td className="px-4 py-3 text-right text-gray-700">
                          {lectura.co2 != null
                            ? parseFloat(lectura.co2).toFixed(0)
                            : '-'}
                        </td>
                        <td className="px-4 py-3 text-right text-gray-700">
                          {lectura.diametroTronco != null
                            ? parseFloat(lectura.diametroTronco).toFixed(1)
                            : '-'}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}

            {/* Paginación inferior */}
            {totalPages > 1 && (
              <div className="p-4 border-t border-gray-100 flex justify-end">
                <Paginacion
                  currentPage={currentPage}
                  totalPages={totalPages}
                  loading={loading}
                  onPageChange={(p) => setCurrentPage(p)}
                />
              </div>
            )}
          </div>
        </>
      )}
    </div>
  );
}

/** Componente de paginación reutilizable dentro de este fichero */
function Paginacion({ currentPage, totalPages, loading, onPageChange }) {
  if (totalPages <= 1) return null;
  return (
    <div className="flex items-center gap-2">
      <button
        onClick={() => onPageChange(currentPage - 1)}
        disabled={currentPage === 0 || loading}
        className="px-3 py-1.5 text-sm rounded border border-gray-300 disabled:opacity-40 hover:bg-gray-100 transition-colors"
      >
        ← Anterior
      </button>
      <span className="text-sm text-gray-600 whitespace-nowrap">
        Página {currentPage + 1} de {totalPages}
      </span>
      <button
        onClick={() => onPageChange(currentPage + 1)}
        disabled={currentPage >= totalPages - 1 || loading}
        className="px-3 py-1.5 text-sm rounded border border-gray-300 disabled:opacity-40 hover:bg-gray-100 transition-colors"
      >
        Siguiente →
      </button>
    </div>
  );
}

export default HistoricoArbol;
