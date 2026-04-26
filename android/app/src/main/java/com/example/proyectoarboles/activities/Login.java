package com.example.proyectoarboles.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoarboles.R;
import com.example.proyectoarboles.api.AuthApi;
import com.example.proyectoarboles.api.RetrofitClient;
import com.example.proyectoarboles.dto.AuthResponse;
import com.example.proyectoarboles.dto.LoginRequest;
import com.example.proyectoarboles.util.PermissionManager;

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
    private PermissionManager permissionManager;
    
    // Vistas para modo logout
    private LinearLayout layoutLoginForm;
    private LinearLayout layoutLogout;
    private TextView textViewUsuarioLogueado;
    private Button buttonCerrarSesion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar SharedPreferences y PermissionManager
        sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        permissionManager = new PermissionManager(this);

        // Inicializar vistas del formulario de login
        inputUsuario = findViewById(R.id.editTextUsuario);
        inputPassword = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
        registrerButton = findViewById(R.id.buttonRegistrer);
        
        // Inicializar vistas del modo logout
        layoutLoginForm = findViewById(R.id.layoutLoginForm);
        layoutLogout = findViewById(R.id.layoutLogout);
        textViewUsuarioLogueado = findViewById(R.id.textViewUsuarioLogueado);
        buttonCerrarSesion = findViewById(R.id.buttonCerrarSesion);

        // Verificar estado de sesión y mostrar UI apropiada
        verificarEstadoSesion();

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

    /**
     * Verifica si hay una sesión activa y muestra la UI apropiada.
     * Si hay sesión y el usuario NO es admin, muestra botón de cerrar sesión.
     * Si no hay sesión, muestra formulario de login.
     */
    private void verificarEstadoSesion() {
        boolean isLoggedIn = permissionManager.isLoggedIn();
        boolean isAdmin = permissionManager.isAdmin();
        
        if (isLoggedIn && !isAdmin) {
            // Usuario logueado pero NO es admin: mostrar opción de cerrar sesión
            layoutLoginForm.setVisibility(View.GONE);
            layoutLogout.setVisibility(View.VISIBLE);
            
            String nombreUsuario = sharedPreferences.getString("user_name", "Usuario");
            textViewUsuarioLogueado.setText("Sesión activa como: " + nombreUsuario);
            
            buttonCerrarSesion.setOnClickListener(v -> cerrarSesion());
        } else {
            // No hay sesión o es admin: mostrar formulario de login
            layoutLoginForm.setVisibility(View.VISIBLE);
            layoutLogout.setVisibility(View.GONE);
        }
    }

    /**
     * Cierra la sesión del usuario y redirige al login
     */
    private void cerrarSesion() {
        permissionManager.clearSession();
        Toast.makeText(Login.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
        
        // Ir al Dashboard (MainActivity) en lugar de a la actividad Login
        Intent intent = new Intent(Login.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
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
                    Intent intent = new Intent(Login.this, MainActivity.class);
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
