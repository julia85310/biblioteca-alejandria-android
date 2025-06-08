package com.example.bibliotecaalejandra;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UsuarioDao {
    @Insert
    void insertarUsuario(UserDatabase usuario);

    @Update
    void actualizarUsuario(UserDatabase usuario);

    @Query("SELECT * FROM usuario LIMIT 1")
    UserDatabase obtenerUsuario();

    @Query("DELETE FROM usuario")
    void borrarUsuario();
}
