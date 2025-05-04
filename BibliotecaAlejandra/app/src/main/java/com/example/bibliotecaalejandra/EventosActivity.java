package com.example.bibliotecaalejandra;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventosActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private EventoPagerAdapter eventoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        viewPager = findViewById(R.id.viewPagerEventos);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://biblioteca-alejandria.vercel.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        // Llamada a la API
        Call<List<Evento>> call = apiService.getEventos();
        call.enqueue(new Callback<List<Evento>>() {
            @Override
            public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Evento> eventos = response.body();
                    for (Evento evento : eventos) {
                        Log.d("Evento", "Imagen URL: " + evento.getImagenUrl());
                    }
                    eventoAdapter = new EventoPagerAdapter(EventosActivity.this, eventos);
                    viewPager.setAdapter(eventoAdapter);
                } else {
                    Toast.makeText(EventosActivity.this, "Error en la respuesta de la API", Toast.LENGTH_SHORT).show();
                    Log.e("EventosActivity", "Error en la respuesta de la API");
                }
            }

            @Override
            public void onFailure(Call<List<Evento>> call, Throwable t) {
                Toast.makeText(EventosActivity.this, "Error al hacer la solicitud a la API", Toast.LENGTH_SHORT).show();
                Log.e("EventosActivity", "Error en la llamada a la API", t);
            }
        });

        //menu de navegacion
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_eventos);
        bottomNavigationView.setItemActiveIndicatorColor(ContextCompat.getColorStateList(this, R.color.chamoiseTransparent));
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                    if (item.getItemId() == R.id.nav_libros) {
                        startActivity(new Intent(EventosActivity.this, CatalogoActivity.class));
                        return true;
                    }
                    if (item.getItemId() == R.id.nav_perfil) {
                        if(User.hayUsuario()){
                            startActivity(new Intent(EventosActivity.this, PerfilActivity.class));
                        }else{
                            startActivity(new Intent(EventosActivity.this, LoginActivity.class));
                        }
                        return true;
                    }
                    return false;
            }
        });
    }
}

