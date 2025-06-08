package com.example.bibliotecaalejandra;

public class Libro {
    private String titulo;
    private String autor;
    private String editorial;
    private String disponibilidad;
    private String estante;
    private String balda;
    private String imagen_url;

    public Libro(String titulo, String autor, String editorial, String disponibilidad, String estante, String balda, String imagen_url) {
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.disponibilidad = disponibilidad;
        this.estante = estante;
        this.balda = balda;
        this.imagen_url = imagen_url;
    }

    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getEditorial() { return editorial; }
    public String getDisponible() { return disponibilidad; }
    public boolean isDisponible() { return disponibilidad.equals("Disponible"); }
    public String getEstante() { return estante; }
    public String getBalda() { return balda; }
    public String getImagenUrl() { return imagen_url; }
}
