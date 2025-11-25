package com.example.proyectoarboles.api;

import com.example.proyectoarboles.model.Arbol;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("arboles")
    Call<List<Arbol>> getArboles();

    @GET("arboles/{id}")
    Call<Arbol> getArbolById(@Path("id") long id);

    @POST("centros/{centroId}/arboles")
    Call<Arbol> createArbol(@Path("centroId") long centroId, @Body Arbol arbol);

    @PUT("arboles/{id}")
    Call<Arbol> updateArbol(@Path("id") long id, @Body Arbol arbol);

    @DELETE("arboles/{id}")
    Call<Void> deleteArbol(@Path("id") long id);

    @GET("arboles/{id}/lecturas")
    Call<List<Lectura>> getLecturas(@Path("id") long id);

    @GET("alertas/activas")
    Call<List<Alerta>> getAlertasActivas();

    @GET("arboles/centro/{centroId}")
    Call<List<Arbol>> getArbolesPorCentro(@Path("centroId") long centroId);
}
