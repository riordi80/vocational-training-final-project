import { useState, useEffect, useRef } from 'react';

const SERVER_ERROR =
  'No se pudo conectar con el servidor. Si es la primera carga, puede estar iniciándose (30-60 seg). Recarga la página en unos momentos.';

export function useFetch(fetchFn, initialData = null) {
  const [data, setData] = useState(initialData);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const fetchRef = useRef(fetchFn);
  fetchRef.current = fetchFn;

  const execute = async () => {
    try {
      setLoading(true);
      setError('');
      const result = await fetchRef.current();
      setData(result);
    } catch (err) {
      console.error('useFetch error:', err);
      setError(SERVER_ERROR);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    execute();
  }, []);

  return { data, setData, loading, setLoading, error, setError, refetch: execute };
}
