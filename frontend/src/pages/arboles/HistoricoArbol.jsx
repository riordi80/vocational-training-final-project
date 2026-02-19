import { useState, useEffect } from 'react';
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
import { getLecturasParaGrafica, getLecturasByArbolRango } from '../../services/lecturasService';
import Spinner from '../../components/common/Spinner';
import Alert from '../../components/common/Alert';
import Button from '../../components/common/Button';

const PAGE_SIZE = 20;

/**
 * Periodos predefinidos. El backend calcula el bucket adecuado para cada uno,
 * garantizando siempre < 400 puntos en la gráfica.
 * El campo `dias` se usa para calcular el rango de la tabla (datos raw).
 */
const PERIODOS = [
  { key: 'DIA',      label: 'Hoy',     info: 'cada 5 min', dias: 1   },
  { key: 'SEMANA',   label: '7 días',  info: 'cada hora',  dias: 7   },
  { key: 'MES',      label: '30 días', info: 'cada 6 h',   dias: 30  },
  { key: 'SEMESTRE', label: '6 meses', info: 'diario',     dias: 180 },
  { key: 'ANIO',     label: '1 año',   info: 'diario',     dias: 365 },
];

/** Devuelve desde/hasta en formato ISO-8601 sin zona para pasar al backend */
function calcularRango(dias) {
  const hasta = new Date();
  const desde = new Date(hasta.getTime() - dias * 24 * 60 * 60 * 1000);
  return {
    desdeISO: desde.toISOString().slice(0, 19),
    hastaISO: hasta.toISOString().slice(0, 19),
  };
}

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
  const [error, setError] = useState('');

  // Periodo activo (controla tanto la gráfica como la tabla)
  const [periodoActivo, setPeriodoActivo] = useState('SEMANA');

  // Gráfica (datos agregados)
  const [datosGrafica, setDatosGrafica] = useState([]);
  const [loadingGrafica, setLoadingGrafica] = useState(false);

  // Tabla (datos raw paginados)
  const [lecturas, setLecturas] = useState([]);
  const [loadingTabla, setLoadingTabla] = useState(false);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  // --- Carga del árbol (solo una vez) ---
  useEffect(() => {
    getArbolById(id)
      .then(setArbol)
      .catch(() => setError('No se pudo cargar la información del árbol.'));
  }, [id]);

  // --- Gráfica: recarga cuando cambia el árbol o el periodo ---
  useEffect(() => {
    let cancelled = false;
    const fetch = async () => {
      setLoadingGrafica(true);
      try {
        const data = await getLecturasParaGrafica(id, periodoActivo);
        if (!cancelled) {
          setDatosGrafica(
            data.map((item) => ({
              tiempo: formatTimestampShort(item.timestamp),
              temperatura: item.temperatura != null ? parseFloat(item.temperatura) : null,
              humedadAmbiente: item.humedadAmbiente != null ? parseFloat(item.humedadAmbiente) : null,
            }))
          );
        }
      } catch {
        if (!cancelled) setDatosGrafica([]);
      } finally {
        if (!cancelled) setLoadingGrafica(false);
      }
    };
    fetch();
    return () => { cancelled = true; };
  }, [id, periodoActivo]);

  // --- Tabla: recarga cuando cambia el árbol, el periodo o la página ---
  useEffect(() => {
    let cancelled = false;
    const fetch = async () => {
      const config = PERIODOS.find((p) => p.key === periodoActivo);
      const { desdeISO, hastaISO } = calcularRango(config.dias);
      setLoadingTabla(true);
      try {
        const data = await getLecturasByArbolRango(id, desdeISO, hastaISO, currentPage, PAGE_SIZE);
        if (!cancelled) {
          setLecturas(data.content);
          setTotalPages(data.totalPages);
          setTotalElements(data.totalElements);
        }
      } catch {
        if (!cancelled) setError('No se pudieron cargar las lecturas. Inténtalo de nuevo.');
      } finally {
        if (!cancelled) setLoadingTabla(false);
      }
    };
    fetch();
    return () => { cancelled = true; };
  }, [id, periodoActivo, currentPage]);

  // Cuando el usuario cambia de periodo, reseteamos a página 0 en el mismo batch
  const handlePeriodoChange = (key) => {
    setPeriodoActivo(key);
    setCurrentPage(0);
  };

  const periodoConfig = PERIODOS.find((p) => p.key === periodoActivo);

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

      {/* Selector de periodo */}
      <div className="bg-white rounded-lg shadow-md p-4 mb-6">
        <p className="text-xs font-semibold text-gray-500 uppercase tracking-wider mb-3">
          Periodo
        </p>
        <div className="flex flex-wrap gap-2">
          {PERIODOS.map((p) => (
            <button
              key={p.key}
              onClick={() => handlePeriodoChange(p.key)}
              className={`px-4 py-2 rounded-full text-sm font-medium transition-colors ${
                periodoActivo === p.key
                  ? 'bg-green-600 text-white shadow-sm'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
              }`}
            >
              {p.label}
            </button>
          ))}
        </div>
      </div>

      {/* Gráfica — todos los datos del periodo, agregados */}
      <div className="bg-white rounded-lg shadow-md p-6 mb-6">
        <div className="flex items-baseline gap-2 mb-1">
          <h2 className="text-lg font-semibold text-gray-800">
            Temperatura y Humedad Ambiente
          </h2>
        </div>
        <p className="text-xs text-gray-500 mb-4">
          {periodoConfig.label} · {datosGrafica.length} lecturas reales
          {datosGrafica.length === 400 && ' (muestra representativa)'}
        </p>

        {loadingGrafica ? (
          <div className="flex justify-center py-10">
            <Spinner size="md" text="Cargando gráfica..." />
          </div>
        ) : datosGrafica.length === 0 ? (
          <p className="text-center text-gray-400 py-10 text-sm">
            Sin datos en este periodo.
          </p>
        ) : (
          <ResponsiveContainer width="100%" height={300}>
            <LineChart data={datosGrafica} margin={{ top: 5, right: 20, left: 0, bottom: 5 }}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis
                dataKey="tiempo"
                tick={{ fontSize: 11 }}
                interval="preserveStartEnd"
              />
              <YAxis tick={{ fontSize: 11 }} />
              <Tooltip
                formatter={(value, name) =>
                  value != null ? [`${value}`, name] : ['-', name]
                }
              />
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
        )}
      </div>

      {/* Tabla de lecturas raw paginadas */}
      <div className="bg-white rounded-lg shadow-md overflow-hidden">
        <div className="p-6 pb-4 flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3">
          <h2 className="text-lg font-semibold text-gray-800">
            Lecturas individuales
            <span className="ml-2 text-sm font-normal text-gray-500">
              ({totalElements} en este periodo)
            </span>
          </h2>
          <Paginacion
            currentPage={currentPage}
            totalPages={totalPages}
            loading={loadingTabla}
            onPageChange={setCurrentPage}
          />
        </div>

        {loadingTabla ? (
          <div className="flex justify-center py-8">
            <Spinner size="md" text="Cargando..." />
          </div>
        ) : lecturas.length === 0 ? (
          <p className="text-center text-gray-500 py-8 text-sm italic">
            No hay lecturas registradas en este periodo.
          </p>
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

        {totalPages > 1 && (
          <div className="p-4 border-t border-gray-100 flex justify-end">
            <Paginacion
              currentPage={currentPage}
              totalPages={totalPages}
              loading={loadingTabla}
              onPageChange={setCurrentPage}
            />
          </div>
        )}
      </div>
    </div>
  );
}

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
