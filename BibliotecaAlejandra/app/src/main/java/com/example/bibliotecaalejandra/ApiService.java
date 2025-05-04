package com.example.bibliotecaalejandra;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;

public interface ApiService {
    @GET("/api/evento")
    Call<List<Evento>> getEventos();

    @POST("/api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("/api/auth/signup")
    Call<RegisterResponse> registrarUsuario(@Body RegisterRequest registerRequest);
}

