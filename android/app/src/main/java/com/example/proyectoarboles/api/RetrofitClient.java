package com.example.proyectoarboles.api;

import com.example.proyectoarboles.adapter.BigDecimalStringAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://proyecto-arboles-backend.onrender.com/";

    private static Retrofit retrofit = null;
    private static ArbolApi arbolApi = null;
    private static CentroEducativoApi centroEducativoApi = null;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {

            // OKHTTP CLIENT CON TIMEOUT
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)  // tiempo para conectar
                    .readTimeout(60, TimeUnit.SECONDS)     // tiempo para recibir datos
                    .writeTimeout(60, TimeUnit.SECONDS)    // tiempo para enviar datos
                    .build();

            // CONFIGURAR GSON
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(String.class, new BigDecimalStringAdapter())
                    .setLenient()
                    .create();

            // CREAR RETROFIT USANDO OKHTTP CLIENTE
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)                     // ← IMPORTANTE
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

    public static CentroEducativoApi getCentroEducativoApi() {
        if (centroEducativoApi == null) {
            centroEducativoApi = getRetrofitInstance().create(CentroEducativoApi.class);
        }
        return centroEducativoApi;
    }
}
