package com.example.proyectoarboles.api;

import com.example.proyectoarboles.model.DispositivoEsp32;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface DispositivoApi {

    @GET("api/dispositivos")
    Call<List<DispositivoEsp32>> obtenerTodosLosDispositivos();

    @GET("api/dispositivos/{id}")
    Call<DispositivoEsp32> obtenerDispositivoPorId(@Path("id") Long id);

    @GET("api/dispositivos/centro/{centroId}")
    Call<List<DispositivoEsp32>> obtenerDispositivosPorCentro(@Path("centroId") Long centroId);

    @POST("api/dispositivos")
    Call<DispositivoEsp32> crearDispositivo(@Body DispositivoEsp32 dispositivo);

    @PUT("api/dispositivos/{id}")
    Call<DispositivoEsp32> actualizarDispositivo(@Path("id") Long id, @Body DispositivoEsp32 dispositivo);

    @DELETE("api/dispositivos/{id}")
    Call<DispositivoEsp32> eliminarDispositivo(@Path("id") Long id);
}
