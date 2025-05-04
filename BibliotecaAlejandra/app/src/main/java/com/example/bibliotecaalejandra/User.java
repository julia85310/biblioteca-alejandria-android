package com.example.bibliotecaalejandra;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class User {
    private static int id;
    private static String nombre;
    private static String email;
    private static String telefono;
    private static String fecha_registro;
    private static String fecha_penalizacion;
    private static boolean admin;
    private static int num_max_libros;

    private User() {}

    public static void setUsuario(int id, String nombre, String email,
                                  String telefono, String fecha_registro,
                                  String fecha_penalizacion, boolean admin,
                                  int num_max_libros) {
        User.id = id;
        User.nombre = nombre;
        User.email = email;
        User.telefono = telefono;
        User.fecha_registro = fecha_registro;
        User.fecha_penalizacion = fecha_penalizacion;
        User.admin = admin;
        User.num_max_libros = num_max_libros;
    }

    public static void setUsuarioRegistro(String nombre, String email,
                                  String telefono) {
        String hoy = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        User.nombre = nombre;
        User.email = email;
        User.telefono = telefono;
        User.fecha_registro = hoy;
        User.fecha_penalizacion = hoy;
        User.admin = false;
    }

    public static boolean isAdmin() {
        return admin;
    }

    public static boolean hayUsuario() {
        return email != null && !email.isEmpty();
    }

    public static void logout() {
        User.id = 0;
        User.nombre = "";
        User.email = "";
        User.telefono = "";
        User.fecha_registro = "";
        User.fecha_penalizacion = "";
        User.admin = false;
        User.num_max_libros = 0;
    }

    public static String getNombre() {
        return nombre;
    }
}
