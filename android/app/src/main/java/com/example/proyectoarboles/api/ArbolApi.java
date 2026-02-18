package com.example.proyectoarboles.api;

import com.example.proyectoarboles.model.Arbol;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface ArbolApi {

    @GET("api/arboles")
    Call<List<Arbol>> obtenerTodosLosArboles();

    @GET("api/arboles/{id}")
    Call<Arbol> obtenerArbolPorId(@Path("id") Long id);

    @GET("api/arboles/especie/{especie}")
    Call<List<Arbol>> obtenerArbolesPorEspecie(@Path("especie") String especie);

    @GET("api/arboles/dispositivo/{dispositivoId}")
    Call<Arbol> obtenerArbolPorDispositivo(@Path("dispositivoId") Long dispositivoId);

    @GET("api/arboles/buscar/{nombre}")
    Call<List<Arbol>> buscarArbolesPorNombre(@Path("nombre") String nombre);

    @GET("api/arboles/ordenados")
    Call<List<Arbol>> obtenerArbolesOrdenados();

    @POST("api/arboles")
    Call<Arbol> crearArbol(@Body Arbol arbol);

    @PUT("api/arboles/{id}")
    Call<Arbol> actualizarArbol(@Path("id") Long id, @Body Arbol arbol);

    @DELETE("api/arboles/{id}")
    Call<Arbol> eliminarArbol(@Path("id") Long id);

    @PATCH("api/arboles/{id}/umbrales")
    Call<Arbol> actualizarUmbrales(@Path("id") Long id, @Body Arbol umbrales);
}
