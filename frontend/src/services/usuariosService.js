import api from "./api";

/**
 * Obtener todos los usuarios
 * @returns {Promise<Array>} Lista de usuarios
 */
export const getUsuarios = async () => {
  try {
    const response = await api.get('/usuarios');
    return response.data;
  } catch (error) {
    console.error('Error obteniendo usuarios', error);
    throw error;
  }
};

/**
 * Obtener un usuario por su ID
 * @param {number} id - ID del usuario
 * @returns {Promise<Object>} Datos del usuario
 */
export const getUsuarioById = async (id) => {
  try {
    const response = await api.get(`/usuarios/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error obteniendo usuario ${id}`, error);
    throw error;
  }
};

/**
 * Crear usuario
 * @param {Object} usuarioData - Datos del usuario a crear
 * @returns {Promise<Object>} Usuario creado
 */
export const createUsuario = async (usuarioData) => {
  try {
    const response = await api.post('/usuarios', usuarioData);
    return response.data;
  } catch (error) {
    console.error('Error creando usuario', error);
    throw error;
  }
};

/**
 * Actualizar un usuario existente
 * @param {number} id - ID del usuario
 * @param {Object} usuarioData - Nuevos datos del usuario
 * @returns {Promise<Object>} Usuario actualizado
 */
export const updateUsuario = async (id, usuarioData) => {
  try {
    const response = await api.put(`/usuarios/${id}`, usuarioData);
    return response.data;
  } catch (error) {
    console.error(`Error actualizando usuario ${id}`, error);
    throw error;
  }
};

/**
 * Eliminar un usuario
 * @param {number} id - ID del usuario
 * @returns {Promise<Object>} Usuario eliminado
 */
export const deleteUsuario = async (id) => {
  try {
    const response = await api.delete(`/usuarios/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error eliminando usuario ${id}`, error);
    throw error;
  }
};
