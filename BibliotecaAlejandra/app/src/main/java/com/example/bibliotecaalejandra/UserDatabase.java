package com.example.bibliotecaalejandra;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "usuario")
public class UserDatabase {

    @PrimaryKey
    private int id;

    private String nombre;
    private String email;
    private String telefono;
    private String fecha_registro;
    private String fecha_penalizacion;
    private boolean admin;
    private int num_max_libros;

    public UserDatabase() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setFecha_registro(String fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public void setFecha_penalizacion(String fecha_penalizacion) {
        this.fecha_penalizacion = fecha_penalizacion;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public void setNum_max_libros(int num_max_libros) {
        this.num_max_libros = num_max_libros;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getFecha_registro() {
        return fecha_registro;
    }

    public String getFecha_penalizacion() {
        return fecha_penalizacion;
    }

    public boolean isAdmin() {
        return admin;
    }

    public int getNum_max_libros() {
        return num_max_libros;
    }
}
