package com.example.bibliotecaalejandra;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private TextView signupLink;
    private EditText emailEditText, passwordEditText;
    private Button loginButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Iniciar vistas
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        signupLink = findViewById(R.id.sign_up_link);
        signupLink.setPaintFlags(signupLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        loginButton.setOnClickListener(v -> loginUser());

        signupLink.setOnClickListener((e) -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        //menu de navegacion
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_perfil);
        bottomNavigationView.setItemActiveIndicatorColor(ContextCompat.getColorStateList(this, R.color.chamoiseTransparent));
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.nav_libros) {
                    startActivity(new Intent(LoginActivity.this, CatalogoActivity.class));
                    return true;
                }
                if (item.getItemId() == R.id.nav_eventos) {
                    startActivity(new Intent(LoginActivity.this, EventosActivity.class));
                }
                return false;
            }
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Por favor ingresa tus credenciales", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest loginRequest = new LoginRequest(email, password);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://biblioteca-alejandria.vercel.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<LoginResponse> call = apiService.login(loginRequest);

        // Realizar la llamada a la API
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Guardar los datos del usuario en el singleton
                    LoginResponse loginResponse = response.body();
                    Log.e("LoginActivity", loginResponse.toString());
                    UserResponse userResponse = loginResponse.getUser();
                    Log.e("LoginActivity", userResponse.toString());
                    User.setUsuario(userResponse.getId(),
                            userResponse.getNombre(),
                            userResponse.getEmail(),
                            String.valueOf(userResponse.getTelefono()),
                            userResponse.getFechaRegistro(),
                            userResponse.getFechaPenalizacion(),
                            userResponse.isAdmin(),
                            userResponse.getNumMaxLibros()
                            );

                    if(User.isAdmin()){
                        Toast.makeText(LoginActivity.this, "Los admin no pueden logearse en la versión móvil.", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent = new Intent(LoginActivity.this, PerfilActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error al hacer la solicitud", Toast.LENGTH_SHORT).show();
                Log.e("LoginActivity", "Error en la llamada a la API", t);
            }
        });
    }
}
