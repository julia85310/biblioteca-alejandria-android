package com.example.bibliotecaalejandra;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PerfilActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil);

        TextView bienvenida = findViewById(R.id.bienvenida);
        String texto = bienvenida.getText().toString();
        bienvenida.setText(texto + " " + User.getNombre());

        //menu de navegacion
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_perfil);
        bottomNavigationView.setItemActiveIndicatorColor(ContextCompat.getColorStateList(this, R.color.chamoiseTransparent));
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.nav_libros) {
                    startActivity(new Intent(PerfilActivity.this, CatalogoActivity.class));
                    return true;
                }
                if (item.getItemId() == R.id.nav_eventos) {
                    startActivity(new Intent(PerfilActivity.this, EventosActivity.class));
                }
                return false;
            }
        });
    }
}
