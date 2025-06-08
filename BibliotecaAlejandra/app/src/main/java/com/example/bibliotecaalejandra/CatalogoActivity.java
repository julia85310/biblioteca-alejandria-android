package com.example.bibliotecaalejandra;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CatalogoActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LibroAdapter libroAdapter;
    private List<Libro> libroList = new ArrayList<>();
    private TextInputEditText buscador;

    private ApiService apiService;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_catalogo);

        //menu de navegacion
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_libros);
        bottomNavigationView.setItemActiveIndicatorColor(ContextCompat.getColorStateList(this, R.color.chamoiseTransparent));
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.nav_eventos) {
                    startActivity(new Intent(CatalogoActivity.this, EventosActivity.class));
                    return true;
                }
                if (item.getItemId() == R.id.nav_perfil) {
                    if(User.hayUsuario()){
                        startActivity(new Intent(CatalogoActivity.this, PerfilActivity.class));
                    }else{
                        startActivity(new Intent(CatalogoActivity.this, LoginActivity.class));
                    }
                    return true;
                }
                return false;
            }
        });

        recyclerView = findViewById(R.id.recyclerViewLibros);
        buscador = findViewById(R.id.buscadorLibros);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        libroAdapter = new LibroAdapter(CatalogoActivity.this, libroList);
        recyclerView.setAdapter(libroAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://biblioteca-alejandria.vercel.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        cargarLibros();

        // Filtrado en tiempo real del buscador
        buscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s,int start,int count,int after) {}

            @Override
            public void onTextChanged(CharSequence s,int start,int before,int count) {
                filtrarLibros(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void cargarLibros() {
        Call<List<Libro>> call = apiService.getLibros();
        call.enqueue(new Callback<List<Libro>>() {
            @Override
            public void onResponse(Call<List<Libro>> call, Response<List<Libro>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    libroList.clear();
                    libroList.addAll(response.body());
                    libroAdapter = new LibroAdapter(CatalogoActivity.this, libroList);
                    recyclerView.setAdapter(libroAdapter);
                    libroAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(CatalogoActivity.this, "Error al cargar libros", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Libro>> call, Throwable t) {
                Toast.makeText(CatalogoActivity.this, "Fallo en la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filtrarLibros(String texto) {
        List<Libro> filtrados = new ArrayList<>();
        for (Libro libro : libroList) {
            if (libro.getTitulo().toLowerCase().contains(texto.toLowerCase())) {
                filtrados.add(libro);
            }
        }
        libroAdapter.getFilter().filter(texto);
    }
}
