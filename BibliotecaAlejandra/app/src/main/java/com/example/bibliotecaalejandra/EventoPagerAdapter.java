package com.example.bibliotecaalejandra;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class EventoPagerAdapter extends RecyclerView.Adapter<EventoPagerAdapter.EventoViewHolder> {
    private Context context;
    private List<Evento> eventoList;

    public EventoPagerAdapter(Context context, List<Evento> eventoList) {
        this.context = context;
        this.eventoList = eventoList;
    }

    @NonNull
    @Override
    public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.evento_card, parent, false);
        return new EventoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventoViewHolder holder, int position) {
        Evento evento = eventoList.get(position);

        holder.titulo.setText(evento.getTitulo());
        holder.fecha.setText(evento.getFechaFormateada());
        holder.descripcion.setText(evento.getDescripcion());

        Log.d("Picasso", "Cargando imagen desde: " + evento.getImagenUrl());
        Picasso.get()
                .load(evento.getImagenUrl())
                .into(holder.imagen);
    }

    @Override
    public int getItemCount() {
        return eventoList.size();
    }

    public static class EventoViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, fecha, descripcion;
        ImageView imagen;

        public EventoViewHolder(@NonNull View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.tituloEvento);
            fecha = itemView.findViewById(R.id.fechaEvento);
            descripcion = itemView.findViewById(R.id.descripcionEvento);
            imagen = itemView.findViewById(R.id.imagenEvento);
        }
    }
}


