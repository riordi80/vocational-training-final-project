import api from "./api";

/**
 * Servicio para gestionar operaciones CRUD de dispositivos ESP32
 */

/**
 * Obtener todos los dispositivos ESP32
 * @returns {Promise<Array>} Lista de dispositivos
 */
export const getDispositivos = async () => {
  try {
    const response = await api.get('/dispositivos');
    return response.data;
  } catch (error) {
    console.error('Error obteniendo dispositivos', error);
    throw error;
  }
};

/**
 * Obtener un dispositivo ESP32 por su ID
 * @param {number} id - ID del dispositivo
 * @returns {Promise<Object>} Datos del dispositivo
 */
export const getDispositivoById = async (id) => {
  try {
    const response = await api.get(`/dispositivos/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error obteniendo dispositivo ${id}`, error);
    throw error;
  }
};

/**
 * Obtener los dispositivos de un centro educativo
 * @param {number} centroId - ID del centro educativo
 * @returns {Promise<Array>} Lista de dispositivos del centro
 */
export const getDispositivosByCentro = async (centroId) => {
  try {
    const response = await api.get(`/dispositivos/centro/${centroId}`);
    return response.data;
  } catch (error) {
    console.error(`Error obteniendo dispositivos del centro ${centroId}`, error);
    throw error;
  }
};

/**
 * Crear un nuevo dispositivo ESP32
 * @param {Object} dispositivoData - Datos del dispositivo a crear
 * @returns {Promise<Object>} Dispositivo creado
 */
export const createDispositivo = async (dispositivoData) => {
  try {
    const response = await api.post('/dispositivos', dispositivoData);
    return response.data;
  } catch (error) {
    console.error('Error creando dispositivo', error);
    throw error;
  }
};

/**
 * Actualizar un dispositivo ESP32 existente
 * @param {number} id - ID del dispositivo a actualizar
 * @param {Object} dispositivoData - Nuevos datos del dispositivo
 * @returns {Promise<Object>} Dispositivo actualizado
 */
export const updateDispositivo = async (id, dispositivoData) => {
  try {
    const response = await api.put(`/dispositivos/${id}`, dispositivoData);
    return response.data;
  } catch (error) {
    console.error(`Error actualizando dispositivo ${id}`, error);
    throw error;
  }
};

/**
 * Eliminar un dispositivo ESP32
 * @param {number} id - ID del dispositivo a eliminar
 * @returns {Promise<Object>} Dispositivo eliminado
 */
export const deleteDispositivo = async (id) => {
  try {
    const response = await api.delete(`/dispositivos/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error eliminando dispositivo ${id}`, error);
    throw error;
  }
};
