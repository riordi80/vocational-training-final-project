package com.example.proyectoarboles.api;

import com.example.proyectoarboles.dto.LecturaMuestraProjection;
import com.example.proyectoarboles.model.Lectura;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import java.util.List;

/**
 * Interface Retrofit para obtener lecturas de sensores de árboles.
 * Base URL: /api/lecturas
 *
 * Las lecturas contienen datos en tiempo real de sensores IoT (temperatura, humedad, CO2, etc.)
 */
public interface LecturaApi {

    /**
     * Obtiene todas las lecturas de un árbol con paginación.
     * Los datos se ordenen DESC por timestamp (más recientes primero).
     *
     * GET /api/lecturas/arbol/{arbolId}?page=0&size=20
     *
     * @param arbolId ID del árbol
     * @param page Número de página (default: 0)
     * @param size Registros por página (default: 20)
     */
    @GET("api/lecturas/arbol/{arbolId}")
    Call<List<Lectura>> obtenerLecturasPorArbol(
            @Path("arbolId") Long arbolId,
            @Query("page") int page,
            @Query("size") int size
    );

    /**
     * Obtiene lecturas de un árbol en un rango de fechas con paginación.
     * Útil para obtener histórico en rango específico.
     *
     * GET /api/lecturas/arbol/{arbolId}/rango?desde=2025-02-01T00:00:00&hasta=2025-02-20T23:59:59&page=0&size=20
     *
     * @param arbolId ID del árbol
     * @param desde Inicio del rango (ISO 8601 format: yyyy-MM-ddTHH:mm:ss)
     * @param hasta Fin del rango (ISO 8601 format: yyyy-MM-ddTHH:mm:ss)
     * @param page Número de página (default: 0)
     * @param size Registros por página (default: 20)
     */
    @GET("api/lecturas/arbol/{arbolId}/rango")
    Call<List<Lectura>> obtenerLecturasEnRango(
            @Path("arbolId") Long arbolId,
            @Query("desde") String desde,
            @Query("hasta") String hasta,
            @Query("page") int page,
            @Query("size") int size
    );

    /**
     * Obtiene lecturas muestreadas para gráficas con stride sampling automático.
     * Garantiza máximo ~400 puntos de datos independientemente del volumen.
     *
     * Períodos soportados:
     * - DIA: Últimas 24 horas
     * - SEMANA: Últimos 7 días
     * - MES: Últimos 30 días
     * - SEMESTRE: Últimos 180 días
     * - ANIO: Últimos 365 días
     *
     * GET /api/lecturas/arbol/{arbolId}/grafica?periodo=SEMANA
     *
     * @param arbolId ID del árbol
     * @param periodo Periodo de tiempo: DIA, SEMANA, MES, SEMESTRE, ANIO
     */
    @GET("api/lecturas/arbol/{arbolId}/grafica")
    Call<List<LecturaMuestraProjection>> obtenerLecturasParaGrafica(
            @Path("arbolId") Long arbolId,
            @Query("periodo") String periodo
    );

    /**
     * Obtiene la última lectura registrada de un árbol.
     * Útil para mostrar datos en tiempo real en ArbolDetalles.
     *
     * Nota: Este endpoint no está explícitamente documentado en el backend,
     * pero puede ser simulado usando obtenerLecturasPorArbol(arbolId, 0, 1)
     * que devuelve la lectura más reciente.
     */
    @GET("api/lecturas/arbol/{arbolId}")
    Call<List<Lectura>> obtenerUltimaLectura(
            @Path("arbolId") Long arbolId,
            @Query("page") int page,
            @Query("size") int size
    );
}
