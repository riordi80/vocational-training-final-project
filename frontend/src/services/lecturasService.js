import api from "./api";

/**
 * Obtener lecturas de un dispositivo paginadas (ordenadas DESC por timestamp).
 * @param {number} dispositivoId - ID del dispositivo
 * @param {number} page - Número de página (0-indexed)
 * @param {number} size - Tamaño de página
 * @returns {Promise<Page>} Objeto Page con { content, totalElements, totalPages, number, size }
 */
export const getLecturasByDispositivo = async (dispositivoId, page = 0, size = 20) => {
  try {
    const response = await api.get(`/lecturas/dispositivo/${dispositivoId}`, {
      params: { page, size },
    });
    return response.data;
  } catch (error) {
    console.error(`Error obteniendo lecturas del dispositivo ${dispositivoId}`, error);
    throw error;
  }
};

/**
 * Obtener lecturas REALES muestreadas (stride sampling) para la gráfica.
 * El backend garantiza siempre ≤ ~400 puntos preservando picos y valles reales.
 *
 * @param {number} dispositivoId - ID del dispositivo
 * @param {'DIA'|'SEMANA'|'MES'|'SEMESTRE'|'ANIO'} periodo - Periodo predefinido
 * @returns {Promise<Array>} Lista de { id, timestamp, temperatura, humedadAmbiente, ... }
 */
export const getLecturasParaGrafica = async (dispositivoId, periodo) => {
  try {
    const response = await api.get(`/lecturas/dispositivo/${dispositivoId}/grafica`, {
      params: { periodo },
    });
    return response.data;
  } catch (error) {
    console.error(`Error obteniendo muestra de lecturas del dispositivo ${dispositivoId}`, error);
    throw error;
  }
};

/**
 * Obtener la lectura más reciente de un dispositivo.
 * @param {number} dispositivoId - ID del dispositivo
 * @returns {Promise<Object|null>} Lectura más reciente o null si no hay ninguna
 */
export const getUltimaLectura = async (dispositivoId) => {
  const data = await getLecturasByDispositivo(dispositivoId, 0, 1);
  return data.content.length > 0 ? data.content[0] : null;
};

/**
 * Obtener lecturas de un dispositivo en un rango de fechas, paginadas.
 * @param {number} dispositivoId - ID del dispositivo
 * @param {string} desde - Fecha inicio (ISO 8601)
 * @param {string} hasta - Fecha fin (ISO 8601)
 * @param {number} page - Número de página (0-indexed)
 * @param {number} size - Tamaño de página
 * @returns {Promise<Page>} Objeto Page con { content, totalElements, totalPages, number, size }
 */
export const getLecturasByDispositivoRango = async (dispositivoId, desde, hasta, page = 0, size = 20) => {
  try {
    const response = await api.get(`/lecturas/dispositivo/${dispositivoId}/rango`, {
      params: { desde, hasta, page, size },
    });
    return response.data;
  } catch (error) {
    console.error(`Error obteniendo lecturas en rango para dispositivo ${dispositivoId}`, error);
    throw error;
  }
};

// Aliases para compatibilidad con código antiguo
export const getLecturasByArbol = getLecturasByDispositivo;
export const getLecturasParaGraficaDispositivo = getLecturasParaGrafica;
export const getLecturasByArbolRango = getLecturasByDispositivoRango;
