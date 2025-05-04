package com.example.bibliotecaalejandra;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupActivity extends AppCompatActivity {

    private TextInputEditText nombre, email, telefono, contraseña, contraseñaRepetida;
    private CheckBox terminosCheckbox;
    private MaterialButton submitButton, loginButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        nombre = findViewById(R.id.nombre);
        email = findViewById(R.id.email);
        telefono = findViewById(R.id.telefono);
        contraseña = findViewById(R.id.contraseña);
        contraseñaRepetida = findViewById(R.id.contraseña_repetida);
        terminosCheckbox = findViewById(R.id.terminos_checkbox);
        submitButton = findViewById(R.id.submit_button);
        loginButton = findViewById(R.id.login_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificación de que el checkbox esté marcado
                if (!terminosCheckbox.isChecked()) {
                    Toast.makeText(SignupActivity.this, "Debes aceptar los términos y condiciones", Toast.LENGTH_SHORT).show();
                    return;
                }

                String nombreText = nombre.getText().toString();
                String emailText = email.getText().toString();
                String telefonoText = telefono.getText().toString();
                String contraseñaText = contraseña.getText().toString();
                String contraseñaRepetidaText = contraseñaRepetida.getText().toString();

                // Validar los datos
                ValidacionResponse validacionResponse = validarDatosRegistro(nombreText, emailText, telefonoText, contraseñaText, contraseñaRepetidaText);
                if (!validacionResponse.isValid()) {
                    Toast.makeText(SignupActivity.this, validacionResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                // Si la validación es exitosa, llamar al API para registrar
                registrarUsuario(nombreText, emailText, telefonoText, contraseñaText);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private ValidacionResponse validarDatosRegistro(String nombre, String email, String telefono, String password, String passwordRepetida) {
        if (nombre.isEmpty()) {
            return new ValidacionResponse(false, "Introduce tu nombre para completar el registro.");
        }
        if (email.isEmpty()) {
            return new ValidacionResponse(false, "Introduce tu email para completar el registro.");
        }
        if (telefono.isEmpty()) {
            return new ValidacionResponse(false, "Introduce tu telefono para completar el registro.");
        }
        if (password.isEmpty()) {
            return new ValidacionResponse(false, "Introduce una contraseña segura para completar el registro.");
        }
        if (!password.equals(passwordRepetida)) {
            return new ValidacionResponse(false, "Las contraseñas no coinciden.");
        }

        // Validación del email
        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            return new ValidacionResponse(false, "Introduce un email válido para completar el registro.");
        }

        // Validación del teléfono
        if (!telefono.matches("^\\d{9}$")) {
            return new ValidacionResponse(false, "Introduce un telefono válido para completar el registro.");
        }

        // Validación de la contraseña
        if (!password.matches("^(?=.*\\d)(?=.*[a-zA-Z]).{6,}$")) {
            return new ValidacionResponse(false, "La contraseña debe tener mínimo 6 caracteres, al menos un número y una letra.");
        }

        return new ValidacionResponse(true, "");
    }

    // Función para hacer el registro del usuario
    private void registrarUsuario(String nombre, String email, String telefono, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://biblioteca-alejandria.vercel.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        RegisterRequest registerRequest = new RegisterRequest(nombre, email, telefono, password);

        Call<RegisterResponse> call = apiService.registrarUsuario(registerRequest);

        // Realizar la llamada a la API
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.code() == 201) {
                    User.setUsuarioRegistro(nombre, email, telefono);

                    Toast.makeText(SignupActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignupActivity.this, PerfilActivity.class);
                    startActivity(intent);
                } else {
                    String errorMessage = "Error en el registro";

                    if (response.body() != null && response.body().getMessage() != null) {
                        errorMessage = response.body().getMessage();
                    }

                    Toast.makeText(SignupActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(SignupActivity.this, "Error al hacer la solicitud", Toast.LENGTH_SHORT).show();
                Log.e("RegisterActivity", "Error en la llamada a la API", t);
            }
        });
    }

    // Clase para almacenar la respuesta de la validación
    private static class ValidacionResponse {
        private boolean valid;
        private String message;

        public ValidacionResponse(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        public boolean isValid() {
            return valid;
        }

        public String getMessage() {
            return message;
        }
    }
}
