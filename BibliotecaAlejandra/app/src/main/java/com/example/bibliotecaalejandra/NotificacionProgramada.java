package com.example.bibliotecaalejandra;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notificaciones")
public class NotificacionProgramada {
    @PrimaryKey
    @NonNull
    public String id;

    public String titulo;
    public String fechaMillis;

    public NotificacionProgramada(String id, String titulo, String fechaMillis) {
        this.id = id;
        this.titulo = titulo;
        this.fechaMillis = fechaMillis;
    }
}
