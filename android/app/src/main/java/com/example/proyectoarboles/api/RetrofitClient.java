package com.example.proyectoarboles.api;

import android.content.Context;

import com.example.proyectoarboles.BuildConfig;
import com.example.proyectoarboles.adapter.BigDecimalStringAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // URL configurada automáticamente según el Build Flavor seleccionado
    private static final String BASE_URL = BuildConfig.BASE_URL;

    private static Retrofit retrofit = null;
    private static ArbolApi arbolApi = null;
    private static CentroEducativoApi centroEducativoApi = null;
    private static AuthApi authApi = null;
    private static UsuarioApi usuarioApi = null;
    private static UsuarioCentroApi usuarioCentroApi = null;
    private static LecturaApi lecturaApi = null;
    private static DispositivoApi dispositivoApi = null;

    public static void init(Context context) {
        AuthInterceptor authInterceptor = new AuthInterceptor(context.getApplicationContext());

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(String.class, new BigDecimalStringAdapter())
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        arbolApi = null;
        centroEducativoApi = null;
        authApi = null;
        usuarioApi = null;
        usuarioCentroApi = null;
        lecturaApi = null;
        dispositivoApi = null;
    }

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {

            // OKHTTP CLIENT CON TIMEOUT (sin AuthInterceptor — usar init(context) en su lugar)
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
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

    // Método para obtener la instancia de AuthApi
    public static AuthApi getAuthApi() {
        if (authApi == null) {
            authApi = getRetrofitInstance().create(AuthApi.class);
        }
        return authApi;
    }

    // Método para obtener la instancia de UsuarioApi
    public static UsuarioApi getUsuarioApi() {
        if (usuarioApi == null) {
            usuarioApi = getRetrofitInstance().create(UsuarioApi.class);
        }
        return usuarioApi;
    }

    // Método para obtener la instancia de UsuarioCentroApi
    public static UsuarioCentroApi getUsuarioCentroApi() {
        if (usuarioCentroApi == null) {
            usuarioCentroApi = getRetrofitInstance().create(UsuarioCentroApi.class);
        }
        return usuarioCentroApi;
    }

    // Método para obtener la instancia de LecturaApi
    public static LecturaApi getLecturaApi() {
        if (lecturaApi == null) {
            lecturaApi = getRetrofitInstance().create(LecturaApi.class);
        }
        return lecturaApi;
    }

    // Método para obtener la instancia de DispositivoApi
    public static DispositivoApi getDispositivoApi() {
        if (dispositivoApi == null) {
            dispositivoApi = getRetrofitInstance().create(DispositivoApi.class);
        }
        return dispositivoApi;
    }
}
