package com.example.proyectoarboles.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoarboles.R;
import com.example.proyectoarboles.api.RetrofitClient;
import com.example.proyectoarboles.model.Arbol;
import com.example.proyectoarboles.model.CentroEducativo;
import com.example.proyectoarboles.util.PermissionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends AppCompatActivity {

    private static final String TAG = "Dashboard";

    private TextView tvNumeroCentros, tvNumeroArboles, tvNombreUsuario, tvRolUsuario;
    private Button btVerCentros, btVerArboles, btLogin, btRegister, btCerrarSesion;
    private LinearLayout llEstadoUsuario;
    private SharedPreferences sharedPreferences;
    private PermissionManager permissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        permissionManager = new PermissionManager(this);

        // Inicializar vistas
        tvNumeroCentros = findViewById(R.id.tvNumeroCentros);
        tvNumeroArboles = findViewById(R.id.tvNumeroArboles);
        tvNombreUsuario = findViewById(R.id.tvNombreUsuario);
        tvRolUsuario = findViewById(R.id.tvRolUsuario);
        llEstadoUsuario = findViewById(R.id.llEstadoUsuario);

        btVerCentros = findViewById(R.id.btVerCentros);
        btVerArboles = findViewById(R.id.btVerArboles);
        btLogin = findViewById(R.id.btLogin);
        btRegister = findViewById(R.id.btRegister);
        btCerrarSesion = findViewById(R.id.btCerrarSesion);

        // Cargar datos
        cargarEstadisticas();
        configurarListeners();
        actualizarVisibilidadBotones();
    }

    private void cargarEstadisticas() {
        cargarNumeroCentros();
        cargarNumeroArboles();
    }

    private void cargarNumeroCentros() {
        Log.d(TAG, "Cargando número de centros...");
        Call<List<CentroEducativo>> call = RetrofitClient.getCentroEducativoApi().obtenerTodosLosCentros();

        call.enqueue(new Callback<List<CentroEducativo>>() {
            @Override
            public void onResponse(Call<List<CentroEducativo>> call, Response<List<CentroEducativo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int numeroCentros = response.body().size();
                    tvNumeroCentros.setText(String.valueOf(numeroCentros));
                    Log.d(TAG, "Centros cargados: " + numeroCentros);
                } else {
                    Log.e(TAG, "Error al cargar centros: " + response.code());
                    tvNumeroCentros.setText("Error");
                }
            }

            @Override
            public void onFailure(Call<List<CentroEducativo>> call, Throwable t) {
                Log.e(TAG, "Error de conexión al cargar centros", t);
                tvNumeroCentros.setText("Error");
            }
        });
    }

    private void cargarNumeroArboles() {
        Log.d(TAG, "Cargando número de árboles...");
        Call<List<Arbol>> call = RetrofitClient.getArbolApi().obtenerTodosLosArboles();

        call.enqueue(new Callback<List<Arbol>>() {
            @Override
            public void onResponse(Call<List<Arbol>> call, Response<List<Arbol>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int numeroArboles = response.body().size();
                    tvNumeroArboles.setText(String.valueOf(numeroArboles));
                    Log.d(TAG, "Árboles cargados: " + numeroArboles);
                } else {
                    Log.e(TAG, "Error al cargar árboles: " + response.code());
                    tvNumeroArboles.setText("Error");
                }
            }

            @Override
            public void onFailure(Call<List<Arbol>> call, Throwable t) {
                Log.e(TAG, "Error de conexión al cargar árboles", t);
                tvNumeroArboles.setText("Error");
            }
        });
    }

    private void configurarListeners() {
        btVerCentros.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, ListarCentros.class);
            startActivity(intent);
        });

        btVerArboles.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, ListarArboles.class);
            // Pasar centro_id = -1 para indicar que se muestren todos los árboles
            intent.putExtra("centro_id", -1L);
            startActivity(intent);
        });

        btLogin.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, Login.class);
            startActivity(intent);
        });

        btCerrarSesion.setOnClickListener(v -> {
            permissionManager.clearSession();
            actualizarVisibilidadBotones();
            actualizarInfoUsuario();
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
        });
    }

    private void actualizarVisibilidadBotones() {
        boolean isLoggedIn = permissionManager.isLoggedIn();
        btLogin.setVisibility(isLoggedIn ? View.GONE : View.VISIBLE);
        btRegister.setVisibility(View.GONE);
        btCerrarSesion.setVisibility(isLoggedIn ? View.VISIBLE : View.GONE);
        actualizarInfoUsuario();
    }

    private void actualizarInfoUsuario() {
        boolean isLoggedIn = permissionManager.isLoggedIn();

        if (isLoggedIn) {
            String nombreUsuario = sharedPreferences.getString("user_name", "");
            String rolUsuario = sharedPreferences.getString("user_role", "PUBLICO");

            tvNombreUsuario.setText(nombreUsuario);
            tvRolUsuario.setText(rolUsuario != null ? rolUsuario : "PUBLICO");
            llEstadoUsuario.setVisibility(View.VISIBLE);
        } else {
            llEstadoUsuario.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarVisibilidadBotones();
        cargarEstadisticas();
    }
}
