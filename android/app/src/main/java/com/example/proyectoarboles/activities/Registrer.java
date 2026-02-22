package com.example.proyectoarboles.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.proyectoarboles.R;
import com.example.proyectoarboles.api.AuthApi;
import com.example.proyectoarboles.api.RetrofitClient;
import com.example.proyectoarboles.dto.AuthResponse;
import com.example.proyectoarboles.dto.RegisterRequest;

import java.util.Set;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registrer extends AppCompatActivity {

    private static final String TAG = "Registrer";

    private EditText inputNombre;
    private EditText inputUsuario;
    private EditText inputPassword;
    private Button volverButton;
    private Button registrerButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrer);

        // Inicializar SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        inputNombre = findViewById(R.id.editTextNombreRegistrer);
        inputUsuario = findViewById(R.id.editTextUsuarioRegistrer);
        inputPassword = findViewById(R.id.editTextPasswordRegistrer);

        volverButton = findViewById(R.id.buttonVolver);
        registrerButton = findViewById(R.id.buttonRegistrer);

        volverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registrer.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        registrerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = inputNombre.getText().toString().trim();
                String email = inputUsuario.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Validar campos
                if (nombre.isEmpty()) {
                    inputNombre.setError("Este campo no puede quedar en blanco");
                    return;
                }
                if (email.isEmpty()) {
                    inputUsuario.setError("Este campo no puede quedar en blanco");
                    return;
                }
                if (password.isEmpty()) {
                    inputPassword.setError("Este campo no puede quedar en blanco");
                    return;
                }

                // Realizar registro
                realizarRegistro(nombre, email, password);
            }
        });
    }

    private void realizarRegistro(String nombre, String email, String password) {
        AuthApi authApi = RetrofitClient.getAuthApi();
        RegisterRequest registerRequest = new RegisterRequest(nombre, email, password);

        Call<AuthResponse> call = authApi.register(registerRequest);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    Log.d(TAG, "Registro exitoso para: " + authResponse.getEmail());

                    // Guardar sesión
                    guardarSesion(authResponse);

                    Toast.makeText(Registrer.this, "Registro exitoso. Bienvenido " + authResponse.getNombre(), Toast.LENGTH_SHORT).show();

                    // Navegar a ListarCentros
                    Intent intent = new Intent(Registrer.this, ListarCentros.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                } else {
                    // Manejar errores específicos
                    String errorMsg = "Error en el registro";
                    if (response.code() == 409) {
                        errorMsg = "El email ya está registrado";
                    } else if (response.code() == 400) {
                        errorMsg = "Datos inválidos. Verifica los campos.";
                    }
                    Log.e(TAG, "Error en registro - Código: " + response.code());
                    Toast.makeText(Registrer.this, errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                // Manejar error de conexión
                Log.e(TAG, "Error de conexión: " + t.getMessage());
                Toast.makeText(Registrer.this, "Error de conexión. Inténtalo de nuevo.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void guardarSesion(AuthResponse authResponse) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("is_logged_in", true);
        editor.putLong("user_id", authResponse.getId());
        editor.putString("user_name", authResponse.getNombre());
        editor.putString("user_email", authResponse.getEmail());

        // Guardar rol (puede ser null)
        if (authResponse.getRol() != null) {
            editor.putString("user_role", authResponse.getRol());
        } else {
            editor.remove("user_role");
        }

        // Guardar los IDs de los centros asignados
        if (authResponse.getCentros() != null && !authResponse.getCentros().isEmpty()) {
            Set<String> centrosSet = authResponse.getCentros().stream()
                    .map(centro -> String.valueOf(centro.getId()))
                    .collect(Collectors.toSet());
            editor.putStringSet("user_centros", centrosSet);
        } else {
            editor.remove("user_centros");
        }

        editor.apply();
    }
}