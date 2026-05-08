package com.example.proyectoarboles.api;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.proyectoarboles.activities.MainActivity;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private final Context context;
    private final SharedPreferences sharedPreferences;

    public AuthInterceptor(Context context) {
        this.context = context.getApplicationContext();
        this.sharedPreferences = this.context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = sharedPreferences.getString("token", null);
        Request original = chain.request();

        if (token != null) {
            original = original.newBuilder()
                    .header("Authorization", "Bearer " + token)
                    .build();
        }

        Response response = chain.proceed(original);

        if (response.code() == 401) {
            sharedPreferences.edit()
                    .remove("token")
                    .remove("user")
                    .apply();
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }

        return response;
    }
}
