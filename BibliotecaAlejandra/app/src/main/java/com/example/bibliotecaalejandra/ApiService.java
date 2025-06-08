package com.example.bibliotecaalejandra;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface ApiService {
    @GET("/api/evento")
    Call<List<Evento>> getEventos();

    @POST("/api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("/api/auth/signup")
    Call<RegisterResponse> registrarUsuario(@Body RegisterRequest registerRequest);

    @GET("/api/libro")
    Call<List<Libro>> getLibros();

    @GET("/api/auth/login")
    Call<User> getUsuarioById(@Query("id") int id);

    @GET("/api/libro")
    Call<Libro> getLibroById(@Query("id") int libroId);

    @GET("/api/userdata")
    Call<MoreUserData> getMoreUserData(@Query("u") int userId);
}

