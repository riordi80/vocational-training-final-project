import api from "./api";

/**
 * Obtener los centros asignados a un usuario
 * @param {number} usuarioId - ID del usuario
 * @returns {Promise<Array>} Lista de asignaciones usuario-centro
 */
export const getCentrosDeUsuario = async (usuarioId) => {
  try {
    const response = await api.get(`/usuario-centro/usuario/${usuarioId}`);
    return response.data;
  } catch (error) {
    console.error(`Error obteniendo centros del usuario ${usuarioId}`, error);
    throw error;
  }
};

/**
 * Asignar un centro a un usuario
 * @param {number} usuarioId - ID del usuario
 * @param {number} centroId - ID del centro
 * @returns {Promise<Object>} Asignación creada
 */
export const asignarCentro = async (usuarioId, centroId) => {
  try {
    const response = await api.post('/usuario-centro', { usuarioId, centroId });
    return response.data;
  } catch (error) {
    console.error(`Error asignando centro ${centroId} al usuario ${usuarioId}`, error);
    throw error;
  }
};

/**
 * Desasignar un centro de un usuario
 * @param {number} id - ID de la asignación usuario-centro
 * @returns {Promise<Object>} Resultado de la eliminación
 */
export const desasignarCentro = async (id) => {
  try {
    const response = await api.delete(`/usuario-centro/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error desasignando usuario-centro ${id}`, error);
    throw error;
  }
};
