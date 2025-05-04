package com.example.bibliotecaalejandra;

import com.google.gson.annotations.SerializedName;

public class UserResponse {

    @SerializedName("id")
    private int id;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("email")
    private String email;

    @SerializedName("telefono")
    private long telefono;

    @SerializedName("fecha_registro")
    private String fechaRegistro;

    @SerializedName("fecha_penalizacion")
    private String fechaPenalizacion;

    @SerializedName("admin")
    private boolean admin;

    @SerializedName("num_max_libros")
    private int numMaxLibros;

    // Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public long getTelefono() {
        return telefono;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public String getFechaPenalizacion() {
        return fechaPenalizacion;
    }

    public boolean isAdmin() {
        return admin;
    }

    public int getNumMaxLibros() {
        return numMaxLibros;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", telefono=" + telefono +
                ", fechaRegistro='" + fechaRegistro + '\'' +
                ", fechaPenalizacion='" + fechaPenalizacion + '\'' +
                ", admin=" + admin +
                ", numMaxLibros=" + numMaxLibros +
                '}';
    }
}
