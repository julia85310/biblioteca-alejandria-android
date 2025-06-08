package com.example.bibliotecaalejandra;

public class LibroUsuario {
    private int id;
    private String fecha_adquisicion;
    private String fecha_devolucion;
    private int libro;      // id del libro
    private int usuario;    // id del usuario
    private String condicion;
    private Integer dias_penalizacion; // puede ser null

    // Getters y setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getFecha_adquisicion() {
        return fecha_adquisicion;
    }
    public void setFecha_adquisicion(String fecha_adquisicion) {
        this.fecha_adquisicion = fecha_adquisicion;
    }
    public String getFecha_devolucion() {
        return fecha_devolucion;
    }
    public void setFecha_devolucion(String fecha_devolucion) {
        this.fecha_devolucion = fecha_devolucion;
    }
    public int getLibro() {
        return libro;
    }
    public void setLibro(int libro) {
        this.libro = libro;
    }
    public int getUsuario() {
        return usuario;
    }
    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }
    public String getCondicion() {
        return condicion;
    }
    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }
    public Integer getDias_penalizacion() {
        return dias_penalizacion;
    }
    public void setDias_penalizacion(Integer dias_penalizacion) {
        this.dias_penalizacion = dias_penalizacion;
    }
}
