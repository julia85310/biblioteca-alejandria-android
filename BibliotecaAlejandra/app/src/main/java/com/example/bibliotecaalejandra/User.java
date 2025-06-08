package com.example.bibliotecaalejandra;


import android.content.Context;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class User {
    private static int id;
    private static String nombre;
    private static String email;
    private static String telefono;
    private static String fecha_registro;
    private static String fecha_penalizacion;
    private static boolean admin;
    private static int num_max_libros;

    private static ExecutorService executor = Executors.newSingleThreadExecutor();
    private static UsuarioDao usuarioDao;

    public User() {
    }

    public static synchronized void init(Context context, ApiService apiService) {
        if (usuarioDao == null) {
            usuarioDao = AppDatabase.getInstance(context).usuarioDao();
        }

        executor.execute(() -> {
            UserDatabase usuarioBD = usuarioDao.obtenerUsuario();
            if (usuarioBD != null) {
                id = usuarioBD.getId();
                nombre = usuarioBD.getNombre();
                email = usuarioBD.getEmail();
                telefono = usuarioBD.getTelefono();
                fecha_registro = usuarioBD.getFecha_registro();
                fecha_penalizacion = usuarioBD.getFecha_penalizacion();
                admin = usuarioBD.isAdmin();
                num_max_libros = usuarioBD.getNum_max_libros();

                if (id > 0) {
                    recargarUsuarioDesdeAPI(id, apiService);
                }
            }
        });
    }

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

        guardarEnBD();
    }

    public static void setUsuarioRegistro(String nombre, String email,
                                          String telefono) {
        String hoy = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        User.id = 0; // o asigna el id que corresponda si lo tienes
        User.nombre = nombre;
        User.email = email;
        User.telefono = telefono;
        User.fecha_registro = hoy;
        User.fecha_penalizacion = hoy;
        User.admin = false;
        User.num_max_libros = 0;

        guardarEnBD();
    }

    private static UserDatabase getUsuarioObjeto() {
        UserDatabase u = new UserDatabase();
        u.setId(id);
        u.setNombre(nombre);
        u.setEmail(email);
        u.setTelefono(telefono);
        u.setFecha_registro(fecha_registro);
        u.setFecha_penalizacion(fecha_penalizacion);
        u.setAdmin(admin);
        u.setNum_max_libros(num_max_libros);
        return u;
    }

    private static void guardarEnBD() {
        if (usuarioDao == null) return;

        executor.execute(() -> {
            usuarioDao.borrarUsuario();

            usuarioDao.insertarUsuario(getUsuarioObjeto());
        });
    }


    public static boolean isAdmin() {
        return admin;
    }

    public static boolean hayUsuario() {
        return email != null && !email.isEmpty();
    }

    public static void logout() {
        id = 0;
        nombre = "";
        email = "";
        telefono = "";
        fecha_registro = "";
        fecha_penalizacion = "";
        admin = false;
        num_max_libros = 0;

        if (usuarioDao == null) return;

        executor.execute(() -> {
            usuarioDao.borrarUsuario();
        });
    }

    public static void recargarUsuarioDesdeAPI(int userId, ApiService apiService) {
        Call<User> call = apiService.getUsuarioById(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User usuarioAPI = response.body();

                    setUsuario(
                            usuarioAPI.getId(),
                            usuarioAPI.getNombre(),
                            usuarioAPI.getEmail(),
                            usuarioAPI.getTelefono(),
                            usuarioAPI.getFechaRegistro(),
                            usuarioAPI.getFechaPenalizacion(),
                            usuarioAPI.isAdmin(),
                            usuarioAPI.getNumMaxLibros()
                    );
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Aquí puedes loguear o mostrar error, si quieres
            }
        });
    }

    public static String getNombre() {
        return nombre;
    }

    public static int getId() {
        return id;
    }

    public static String getEmail() {
        return email;
    }

    public static String getTelefono() {
        return telefono;
    }

    public static String getFechaRegistro() {
        return fecha_registro;
    }

    public static String getFechaPenalizacion() {
        return fecha_penalizacion;
    }

    public static int getNumMaxLibros() {
        return num_max_libros;
    }

    public static void setId(int id) {
        User.id = id;
    }

    public static void setNombre(String nombre) {
        User.nombre = nombre;
    }

    public static void setEmail(String email) {
        User.email = email;
    }

    public static void setTelefono(String telefono) {
        User.telefono = telefono;
    }

    public static void setFecha_registro(String fecha_registro) {
        User.fecha_registro = fecha_registro;
    }

    public static void setFecha_penalizacion(String fecha_penalizacion) {
        User.fecha_penalizacion = fecha_penalizacion;
    }

    public static void setAdmin(boolean admin) {
        User.admin = admin;
    }

    public static void setNum_max_libros(int num_max_libros) {
        User.num_max_libros = num_max_libros;
    }
}