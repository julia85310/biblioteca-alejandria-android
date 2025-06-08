package com.example.bibliotecaalejandra;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;

@Dao
public interface NotificacionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertarNotificacion(NotificacionEntity notificacion);

    @Query("SELECT * FROM notificaciones WHERE titulo = :titulo AND fecha = :fecha LIMIT 1")
    NotificacionEntity obtenerNotificacion(String titulo, String fecha);
}
