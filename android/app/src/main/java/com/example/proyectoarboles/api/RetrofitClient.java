package com.example.proyectoarboles.api;

import com.example.proyectoarboles.adapter.BigDecimalStringAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // ⚠️ CAMBIA ESTA URL POR LA IP DE TU BACKEND
    // Si usas emulador de Android: http://10.0.2.2:8080/
    // Si usas dispositivo físico: http://TU_IP_LOCAL:8080/
    private static final String BASE_URL = "http://10.0.2.2:8080/";

    private static Retrofit retrofit = null;
    private static ArbolApi arbolApi = null;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // Configurar Gson con el adaptador para BigDecimal
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(String.class, new BigDecimalStringAdapter())
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static ArbolApi getArbolApi() {
        if (arbolApi == null) {
            arbolApi = getRetrofitInstance().create(ArbolApi.class);
        }
        return arbolApi;
    }
}