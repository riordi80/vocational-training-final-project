package com.example.proyectoarboles.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoarboles.R;
import com.example.proyectoarboles.adapter.ArbolAdapter;
import com.example.proyectoarboles.api.RetrofitClient;
import com.example.proyectoarboles.model.Arbol;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListarArboles extends AppCompatActivity {

    private static final String TAG = "ListarArboles";

    private RecyclerView recyclerViewArboles;
    private ArbolAdapter adapter;
    private List<Arbol> listaArboles = new ArrayList<>();
    private Button btCerrarSesion;
    private long centroId = -1; // Variable para almacenar el ID del centro

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_arboles);

        // Recoger el ID del centro del Intent
        centroId = getIntent().getLongExtra("centro_id", -1);

        // Inicializar vistas
        recyclerViewArboles = findViewById(R.id.RecyclerViewArboles);
        btCerrarSesion = findViewById(R.id.btCerrarS);

        // Configurar adapter
        adapter = new ArbolAdapter(listaArboles, arbol -> {
            Intent intent = new Intent(ListarArboles.this, ArbolDetalles.class);
            intent.putExtra("arbol_id", arbol.getId());
            startActivity(intent);
        });

        recyclerViewArboles.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewArboles.setAdapter(adapter);

        // Cargar datos según si se ha pasado un ID de centro o no
        cargarArbolesDesdeAPI();

        // Configurar botón de cerrar sesión
        btCerrarSesion.setOnClickListener(v -> {
            Intent intent = new Intent(ListarArboles.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void cargarArbolesDesdeAPI() {
        Log.d(TAG, "Iniciando carga de árboles desde API...");

        Call<List<Arbol>> call;

        if (centroId != -1) {
            // Si hay un ID de centro, cargar árboles de ese centro
            Log.d(TAG, "Cargando árboles para el centro ID: " + centroId);
            call = RetrofitClient.getArbolApi().obtenerArbolesPorCentro(centroId);
        } else {
            // Si no, cargar todos los árboles
            Log.d(TAG, "Cargando todos los árboles");
            call = RetrofitClient.getArbolApi().obtenerTodosLosArboles();
        }

        call.enqueue(new Callback<List<Arbol>>() {
            @Override
            public void onResponse(Call<List<Arbol>> call, Response<List<Arbol>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Árboles recibidos: " + response.body().size());
                    listaArboles.clear();
                    listaArboles.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    if (listaArboles.isEmpty()) {
                        Toast.makeText(ListarArboles.this, "No hay árboles para este centro", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ListarArboles.this, "Árboles cargados: " + listaArboles.size(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "Error en respuesta - Código: " + response.code());
                    Toast.makeText(ListarArboles.this, "Error al cargar árboles", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Arbol>> call, Throwable t) {
                Log.e(TAG, "Error de conexión: " + t.getMessage());
                Toast.makeText(ListarArboles.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
