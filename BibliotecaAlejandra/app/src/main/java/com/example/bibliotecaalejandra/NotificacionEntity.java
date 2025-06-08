package com.example.bibliotecaalejandra;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "notificaciones")
public class NotificacionEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String titulo;

    public String fecha;

    public NotificacionEntity(String titulo, String fecha) {
        this.titulo = titulo;
        this.fecha = fecha;
    }
}
