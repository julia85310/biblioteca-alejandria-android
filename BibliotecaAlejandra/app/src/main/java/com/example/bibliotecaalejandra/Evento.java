package com.example.bibliotecaalejandra;

import com.google.gson.annotations.SerializedName;

public class Evento {
    private int id;
    private String titulo;
    private String descripcion;
    @SerializedName("imagen_url")
    private String imagenUrl;
    private String fechaFormateada;

    public Evento(int id, String titulo, String descripcion, String imagenUrl, String fechaFormateada) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
        this.fechaFormateada = fechaFormateada;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public String getFechaFormateada() {
        return fechaFormateada;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}
