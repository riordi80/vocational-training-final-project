package com.example.proyectoarboles.api;

import com.example.proyectoarboles.dto.LecturaMuestraProjection;
import com.example.proyectoarboles.model.Lectura;
import com.example.proyectoarboles.model.PageResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import java.util.List;

public interface LecturaApi {

    /**
     * Obtiene lecturas de un dispositivo paginadas (más recientes primero).
     * GET /api/lecturas/dispositivo/{dispositivoId}?page=0&size=20
     */
    @GET("api/lecturas/dispositivo/{dispositivoId}")
    Call<PageResponse<Lectura>> obtenerLecturasPorDispositivo(
            @Path("dispositivoId") Long dispositivoId,
            @Query("page") int page,
            @Query("size") int size
    );

    /**
     * Obtiene lecturas en un rango de fechas con paginación.
     * GET /api/lecturas/dispositivo/{dispositivoId}/rango?desde=...&hasta=...&page=0&size=20
     */
    @GET("api/lecturas/dispositivo/{dispositivoId}/rango")
    Call<PageResponse<Lectura>> obtenerLecturasEnRango(
            @Path("dispositivoId") Long dispositivoId,
            @Query("desde") String desde,
            @Query("hasta") String hasta,
            @Query("page") int page,
            @Query("size") int size
    );

    /**
     * Obtiene lecturas muestreadas para gráficas (stride sampling, máximo ~400 puntos).
     * Períodos: DIA, SEMANA, MES, SEMESTRE, ANIO
     * GET /api/lecturas/dispositivo/{dispositivoId}/grafica?periodo=SEMANA
     */
    @GET("api/lecturas/dispositivo/{dispositivoId}/grafica")
    Call<List<LecturaMuestraProjection>> obtenerLecturasParaGrafica(
            @Path("dispositivoId") Long dispositivoId,
            @Query("periodo") String periodo
    );
}
