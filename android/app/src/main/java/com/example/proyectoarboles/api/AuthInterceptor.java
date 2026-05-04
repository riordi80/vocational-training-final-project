package com.example.proyectoarboles.api;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private final SharedPreferences sharedPreferences;

    public AuthInterceptor(Context context) {
        this.sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = sharedPreferences.getString("token", null);
        Request original = chain.request();

        if (token != null) {
            Request request = original.newBuilder()
                    .header("Authorization", "Bearer " + token)
                    .build();
            return chain.proceed(request);
        }

        return chain.proceed(original);
    }
}
