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

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoarboles.R;
import com.example.proyectoarboles.api.AuthApi;
import com.example.proyectoarboles.api.RetrofitClient;
import com.example.proyectoarboles.dto.AuthResponse;
import com.example.proyectoarboles.dto.LoginRequest;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private static final String TAG = "Login";

    private EditText inputUsuario;
    private EditText inputPassword;
    private Button loginButton;
    private SharedPreferences sharedPreferences;
    private Button registrerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Inicializar vistas
        inputUsuario = findViewById(R.id.editTextUsuario);
        inputPassword = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
        registrerButton = findViewById(R.id.buttonRegistrer);

        loginButton.setOnClickListener(v -> {
            String email = inputUsuario.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(Login.this, "Email y contraseña son requeridos", Toast.LENGTH_SHORT).show();
                return;
            }

            realizarLogin(email, password);
        });

        registrerButton.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Registrer.class);
            startActivity(intent);
        });
    }

    private void realizarLogin(String email, String password) {
        AuthApi authApi = RetrofitClient.getAuthApi();
        LoginRequest loginRequest = new LoginRequest(email, password);

        Call<AuthResponse> call = authApi.login(loginRequest);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    Log.d(TAG, "Login exitoso para: " + authResponse.getEmail());

                    // Guardar datos de sesión en SharedPreferences
                    guardarSesion(authResponse);

                    Toast.makeText(Login.this, "Bienvenido " + authResponse.getNombre(), Toast.LENGTH_SHORT).show();

                    // Navegar a la pantalla principal
                    Intent intent = new Intent(Login.this, ListarCentros.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                } else {
                    // Manejar error de login (ej. credenciales incorrectas)
                    Log.e(TAG, "Error en login - Código: " + response.code());
                    Toast.makeText(Login.this, "Credenciales incorrectas", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                // Manejar error de conexión
                Log.e(TAG, "Error de conexión: " + t.getMessage());
                Toast.makeText(Login.this, "Error de conexión. Inténtalo de nuevo.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void guardarSesion(AuthResponse authResponse) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("is_logged_in", true);
        editor.putLong("user_id", authResponse.getId());
        editor.putString("user_name", authResponse.getNombre());
        editor.putString("user_email", authResponse.getEmail());

        // Guardar rol (puede ser null si el backend lo permite)
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
