import api from "./api";

/**
 * Obtener lecturas de un árbol paginadas (ordenadas DESC por timestamp).
 * @param {number} arbolId - ID del árbol
 * @param {number} page - Número de página (0-indexed)
 * @param {number} size - Tamaño de página
 * @returns {Promise<Page>} Objeto Page con { content, totalElements, totalPages, number, size }
 */
export const getLecturasByArbol = async (arbolId, page = 0, size = 20) => {
  try {
    const response = await api.get(`/lecturas/arbol/${arbolId}`, {
      params: { page, size },
    });
    return response.data;
  } catch (error) {
    console.error(`Error obteniendo lecturas del árbol ${arbolId}`, error);
    throw error;
  }
};

/**
 * Obtener lecturas REALES muestreadas (stride sampling) para la gráfica.
 * El backend garantiza siempre ≤ ~400 puntos preservando picos y valles reales.
 *
 * @param {number} arbolId - ID del árbol
 * @param {'DIA'|'SEMANA'|'MES'|'SEMESTRE'|'ANIO'} periodo - Periodo predefinido
 * @returns {Promise<Array>} Lista de { id, timestamp, temperatura, humedadAmbiente, ... }
 */
export const getLecturasParaGrafica = async (arbolId, periodo) => {
  try {
    const response = await api.get(`/lecturas/arbol/${arbolId}/grafica`, {
      params: { periodo },
    });
    return response.data;
  } catch (error) {
    console.error(`Error obteniendo muestra de lecturas del árbol ${arbolId}`, error);
    throw error;
  }
};

/**
 * Obtener la lectura más reciente de un árbol.
 * @param {number} arbolId - ID del árbol
 * @returns {Promise<Object|null>} Lectura más reciente o null si no hay ninguna
 */
export const getUltimaLectura = async (arbolId) => {
  const data = await getLecturasByArbol(arbolId, 0, 1);
  return data.content.length > 0 ? data.content[0] : null;
};

/**
 * Obtener lecturas de un árbol en un rango de fechas, paginadas.
 * @param {number} arbolId - ID del árbol
 * @param {string} desde - Fecha inicio (ISO 8601)
 * @param {string} hasta - Fecha fin (ISO 8601)
 * @param {number} page - Número de página (0-indexed)
 * @param {number} size - Tamaño de página
 * @returns {Promise<Page>} Objeto Page con { content, totalElements, totalPages, number, size }
 */
export const getLecturasByArbolRango = async (arbolId, desde, hasta, page = 0, size = 20) => {
  try {
    const response = await api.get(`/lecturas/arbol/${arbolId}/rango`, {
      params: { desde, hasta, page, size },
    });
    return response.data;
  } catch (error) {
    console.error(`Error obteniendo lecturas en rango para árbol ${arbolId}`, error);
    throw error;
  }
};
