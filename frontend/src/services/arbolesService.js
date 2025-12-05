import api from "./api";

/**                                                                                                                                                                                        │
* Servicio para gestionar operaciones CRUD de árboles                                                                                                                                     │
*/

/**                                                                                                                                                                                        │
* Obtener todos los árboles                                                                                                                                                               │
* @returns {Promise<Array>} Lista de árboles                                                                                                                                              │
*/
export const getArboles = async () => {
  try {
    const response = await api.get('/arboles');
    return response.data;
  } catch (error) {
    console.error('Error obteniendo árboles', error);
    throw error;
  }
};

/**                                                                                                                                                                                        │
* Obtener un árbol por su ID                                                                                                                                                              │
* @param {number} id - ID del árbol
* @returns {Promise<Object>} Datos del árbol                                                                                              │
*/
export const getArbolById = async (id) => {
  try {
    const response = await api.get(`/arboles/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error obteniendo árbol ${id}`, error);
    throw error;
  }
};

/**                                                                                                                                                                                        │
* Crea un nuevo árbol                                                                                                                                                                     │
* @param {Object} arbolData - Datos del árbol a crear                                                                                                                                     │
* @returns {Promise<Object>} Árbol creado                                                                                                                                                 │
*/
export const createArbol = async (arbolData) => {
  try {
    const response = await api.post('/arboles', arbolData);
    return response.data;
  } catch (error) {
    console.error('Error creando árbol', error);
    throw error;
  }
};

/**
 * Actualizar un árbol existente
 * @param {number} id - ID del árbol a actualizar
 * @param {Object} arbolData - Nuevos datos del árbol
 * @returns {Promise<Object>} Árbol actualizado
 */
export const updateArbol = async (id, arbolData) => {
  try {
    const response = await api.put(`/arboles/${id}`, arbolData);
    return response.data;
  } catch (error) {
    console.error(`Error actualizando árbol ${id}`, error);
    throw error;
  }
};

/**
 * Eliminar un árbol
 * @param {number} id - ID del árbol a eliminar
 * @returns {Promise<Object>} Árbol eliminado
 */
export const deleteArbol = async (id) => {
  try {
    const response = await api.delete(`/arboles/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error eliminando árbol ${id}`, error);
    throw error;
  }
};

/**
 * Obtener los árboles de un centro educativo específico
 * @param {number} centroId - ID del centro educativo
 * @returns {Promise<Array>} Lista de árboles del centro
 */
export const getArbolesByCentro = async (centroId) => {
  try {
    const response = await api.get(`/arboles/centro/${centroId}`);
    return response.data;
  } catch (error) {
    console.error(`Error obteniendo árboles del centro ${centroId}`, error);
    throw error;
  }
};

/**
 * Buscar árboles por nombre (búsqueda parcial, case-insensitive)
 * @param {string} nombre - Texto a buscar en el nombre
 * @returns {Promise<Array>} Lista de árboles que coinciden
 */
export const buscarArbolesPorNombre = async (nombre) => {
  try {
    const response = await api.get(`/arboles/buscar/${nombre}`);
    return response.data;
  } catch (error) {
    console.error(`Error buscando árboles con el nombre ${nombre}`, error);
    throw error;
  }
};