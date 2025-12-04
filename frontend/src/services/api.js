import axios from 'axios';

// Instancia de axios para las llamadas a la API
const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor para añadir el token de autenticación si existe
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

  // Interceptor para responses (manejo de errores global)
  api.interceptors.response.use(
    (response) => {
      return response;
    },
    (error) => {
      // Manejo de errores global
      if (error.response) {
        // El servidor respondió con un código de error
        console.error('Error response:', error.response.status);
      } else if (error.request) {
        // La petición fue hecha pero no hubo respuesta
        console.error('Error request:', error.request);
      } else {
        // Algo pasó al configurar la petición
        console.error('Error:', error.message);
      }
      return Promise.reject(error);
    }
  );


export default api;