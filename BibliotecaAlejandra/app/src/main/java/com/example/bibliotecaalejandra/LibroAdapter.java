package com.example.bibliotecaalejandra;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class LibroAdapter extends RecyclerView.Adapter<LibroAdapter.LibroViewHolder> implements Filterable {

    private Context context;
    private List<Libro> listaOriginal;
    private List<Libro> listaFiltrada;

    public LibroAdapter(Context context, List<Libro> libros) {
        this.context = context;
        this.listaOriginal = libros;
        this.listaFiltrada = new ArrayList<>(libros);
    }

    @NonNull
    @Override
    public LibroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.libro_card, parent, false);
        return new LibroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibroViewHolder holder, int position) {
        Libro libro = listaFiltrada.get(position);
        holder.titulo.setText(libro.getTitulo());
        holder.autor.setText(libro.getAutor());
        holder.editorial.setText(libro.getEditorial());
        holder.disponibilidad.setText(libro.getDisponible());
        holder.circuloDisponibilidad.setBackgroundResource(
                libro.isDisponible() ? R.drawable.circulo_verde : R.drawable.circulo_rojo);
        holder.ubicacion.setText("Estante: " + libro.getEstante() + " | Balda: " + libro.getBalda());

        Log.d("Picasso", "Cargando imagen desde: " + libro.getImagenUrl());
        Picasso.get()
                .load(libro.getImagenUrl())
                .into(holder.imagen);

    }

    @Override
    public int getItemCount() {
        return listaFiltrada.size();
    }

    static class LibroViewHolder extends RecyclerView.ViewHolder {
        ImageView imagen, circuloDisponibilidad;
        TextView titulo, autor, editorial, disponibilidad, ubicacion;

        public LibroViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.imagenLibro);
            titulo = itemView.findViewById(R.id.tituloLibro);
            autor = itemView.findViewById(R.id.autorLibro);
            editorial = itemView.findViewById(R.id.editorialLibro);
            disponibilidad = itemView.findViewById(R.id.textoDisponibilidad);
            circuloDisponibilidad = itemView.findViewById(R.id.circuloDisponibilidad);
            ubicacion = itemView.findViewById(R.id.ubicacionLibro);
        }
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Libro> filtrado = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filtrado.addAll(listaOriginal);
                } else {
                    String filtro = constraint.toString().toLowerCase().trim();
                    for (Libro libro : listaOriginal) {
                        if (libro.getTitulo().toLowerCase().contains(filtro)) {
                            filtrado.add(libro);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filtrado;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listaFiltrada.clear();
                listaFiltrada.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }
}

