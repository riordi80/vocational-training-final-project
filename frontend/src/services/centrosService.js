import api from "./api";

/**                                                                                                                                                                                        │
* Servicio para gestionar operaciones CRUD de centros                                                                                                                                     │
*/

/**                                                                                                                                                                                        │
* Obtener todos los centros                                                                                                                                                               │
* @returns {Promise<Array>} Lista de centros                                                                                                                                              │
*/
export const getCentros = async () => {
  try {
    const response = await api.get('/centros');
    return response.data;
  } catch (error) {
    console.error('Error obteniendo centros', error);
    throw error;
  }
};

/**                                                                                                                                                                                        │
* Obtener un centro por su ID                                                                                                                                                              │
* @param {number} id - ID del centro
* @returns {Promise<Object>} Datos del centro                                                                                              │
*/
export const getCentroById = async (id) => {
  try {
    const response = await api.get(`/centros/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error obteniendo centro ${id}`, error);
    throw error;
  }
};

/**
 * Crear centro
 * @param {Object} centroData - Dato del centro a crear
 * @return {Promise<Object>} Centro creado
 */
export const createCentro = async (centroData) => {
  try {
    const response = await api.post('/centros', centroData);
    return response.data;
  } catch (error) {
    console.error('Error creando centro', error);
    throw error;
  }
};

/**
 * Actualizar un centro existente
 * @param {number} id - ID del centro
 * @param {Object}  centroData - Nuevos datos del centro
 * @return {Promise<Object>} Centro actualizado
 */
export const updateCentro = async (id, centroData) => {
  try {
    const response = await api.put(`/centros/${id}`, centroData);
    return response.data;
  } catch (error) {
    console.error(`Error actualizando centro ${id}`, error);
    throw error;
  }
};

/**
 * Eliminar un centro
 * @param {number} id - ID del centro
 * @return {Promise<Object>} Centro eliminado
 */
export const deleteCentro = async (id) => {
  try {
    const response = await api.delete(`/centros/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error eliminando centro ${id}`, error);
    throw error;
  }
};

/**
 * Obtener los árboles de un centro específico
 * @param {number} id - ID del centro
 * @return {Promise<Array>} Lista de arboles del centro
 */
export const getArbolesByCentro = async (id) => {
  try {
    const response = await api.get(`/centros/${id}/arboles`);
    return response.data;
  } catch (error) {
    console.error(`Error obteniendo árboles del centro ${id}`, error);
    throw error;
  }
};
