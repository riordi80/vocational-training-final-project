package com.example.proyectoarboles.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        // Obtener token de SharedPreferences
        // (Necesita contexto, se puede pasar en constructor)
        String token = getTokenFromPreferences();

        if (token != null) {
            Request request = original.newBuilder()
                .header("Authorization", "Bearer " + token)
                .build();
            return chain.proceed(request);
        }

        return chain.proceed(original);
    }

    private String getTokenFromPreferences() {
        // Implementar acceso a SharedPreferences
        return null; // Placeholder
    }
}
