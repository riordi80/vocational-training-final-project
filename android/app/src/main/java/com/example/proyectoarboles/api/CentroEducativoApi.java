package com.example.proyectoarboles.api;

import com.example.proyectoarboles.model.CentroEducativo;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface CentroEducativoApi {

    @GET("api/centros")
    Call<List<CentroEducativo>> obtenerTodosLosCentros();

    @GET("api/centros/{id}")
    Call<CentroEducativo> obtenerCentroPorId(@Path("id") Long id);

    @POST("api/centros")
    Call<CentroEducativo> crearCentro(@Body CentroEducativo centro);

    @PUT("api/centros/{id}")
    Call<CentroEducativo> actualizarCentro(@Path("id") Long id, @Body CentroEducativo centro);

    @DELETE("api/centros/{id}")
    Call<CentroEducativo> eliminarCentro(@Path("id") Long id);
}
