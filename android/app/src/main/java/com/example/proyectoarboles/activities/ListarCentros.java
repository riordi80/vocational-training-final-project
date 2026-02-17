package com.example.proyectoarboles.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoarboles.R;
import com.example.proyectoarboles.adapter.CentroEducativoAdapter;
import com.example.proyectoarboles.api.RetrofitClient;
import com.example.proyectoarboles.model.CentroEducativo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListarCentros extends AppCompatActivity {

    private static final String TAG = "ListarCentros";

    private RecyclerView recyclerViewCentros;
    private CentroEducativoAdapter adapter;
    private List<CentroEducativo> listaCentros = new ArrayList<>();
    private Button btLogin, btRegister, btCerrarSesion;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_centros);

        // Inicializar SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Inicializar vistas
        recyclerViewCentros = findViewById(R.id.RecyclerViewCentros);
        btLogin = findViewById(R.id.btLogin);
        btRegister = findViewById(R.id.btRegister);
        btCerrarSesion = findViewById(R.id.btCerrarS);

        // Configurar RecyclerView
        configurarRecyclerView();

        // Cargar centros desde la API
        cargarCentrosDesdeAPI();

        // Configurar listeners de botones
        configurarListeners();

        // Actualizar la visibilidad de los botones según el estado de la sesión
        actualizarVisibilidadBotones();
    }

    private void configurarRecyclerView() {
        recyclerViewCentros.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CentroEducativoAdapter(listaCentros, centro -> {
            Intent intent = new Intent(ListarCentros.this, ListarArboles.class);
            intent.putExtra("centro_id", centro.getId());
            startActivity(intent);
        });
        recyclerViewCentros.setAdapter(adapter);
    }

    private void cargarCentrosDesdeAPI() {
        Log.d(TAG, "Iniciando carga de centros desde API...");
        Call<List<CentroEducativo>> call = RetrofitClient.getCentroEducativoApi().obtenerTodosLosCentros();

        call.enqueue(new Callback<List<CentroEducativo>>() {
            @Override
            public void onResponse(Call<List<CentroEducativo>> call, Response<List<CentroEducativo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Centros recibidos: " + response.body().size());
                    listaCentros.clear();
                    listaCentros.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    if (listaCentros.isEmpty()) {
                        Toast.makeText(ListarCentros.this, "No hay centros registrados", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "Error en respuesta - Código: " + response.code());
                    Toast.makeText(ListarCentros.this, "Error al cargar centros", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CentroEducativo>> call, Throwable t) {
                Log.e(TAG, "Error de conexión: " + t.getMessage());
                Toast.makeText(ListarCentros.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configurarListeners() {
        btLogin.setOnClickListener(v -> {
            Intent intent = new Intent(ListarCentros.this, Login.class);
            startActivity(intent);
        });

        btRegister.setOnClickListener(v -> {
            Intent intent = new Intent(ListarCentros.this, Registrer.class);
            startActivity(intent);
        });

        btCerrarSesion.setOnClickListener(v -> {
            // Limpiar SharedPreferences para cerrar sesión
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("is_logged_in", false);
            editor.apply();

            // Actualizar visibilidad de botones
            actualizarVisibilidadBotones();
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
        });
    }

    private void actualizarVisibilidadBotones() {
        boolean isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false);

        if (isLoggedIn) {
            btLogin.setVisibility(View.GONE);
            btRegister.setVisibility(View.GONE);
            btCerrarSesion.setVisibility(View.VISIBLE);
        } else {
            btLogin.setVisibility(View.VISIBLE);
            btRegister.setVisibility(View.VISIBLE);
            btCerrarSesion.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Actualizar la visibilidad de los botones cada vez que la actividad se reanuda
        actualizarVisibilidadBotones();
    }
}
