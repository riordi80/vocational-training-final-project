package com.example.proyectoarboles.api;

import com.example.proyectoarboles.dto.AuthResponse;
import com.example.proyectoarboles.dto.LoginRequest;
import com.example.proyectoarboles.dto.RegisterRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {

    @POST("api/auth/login")
    Call<AuthResponse> login(@Body LoginRequest loginRequest);

    @POST("api/auth/register")
    Call<AuthResponse> register(@Body RegisterRequest registerRequest);

}
