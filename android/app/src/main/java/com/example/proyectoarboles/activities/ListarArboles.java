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
import com.example.proyectoarboles.adapter.ArbolAdapter;
import com.example.proyectoarboles.api.RetrofitClient;
import com.example.proyectoarboles.model.Arbol;
import com.example.proyectoarboles.util.PermissionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    private Button btVolver, btLogin, btCerrarSesion;
    private FloatingActionButton fabAnadirArbol;
    private SharedPreferences sharedPreferences;
    private PermissionManager permissionManager;
    private long centroId = -1; // Variable para almacenar el ID del centro

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_arboles);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        permissionManager = new PermissionManager(this);
        centroId = getIntent().getLongExtra("centro_id", -1);

        recyclerViewArboles = findViewById(R.id.RecyclerViewArboles);
        btVolver = findViewById(R.id.btVolver);
        btLogin = findViewById(R.id.btLogin);
        btCerrarSesion = findViewById(R.id.btCerrarS);
        fabAnadirArbol = findViewById(R.id.fabAnadirArbol);

        adapter = new ArbolAdapter(listaArboles, arbol -> {
            Intent intent = new Intent(ListarArboles.this, ArbolDetalles.class);
            intent.putExtra("arbol_id", arbol.getId());
            intent.putExtra("centro_id", centroId);
            startActivity(intent);
        }, permissionManager);

        recyclerViewArboles.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewArboles.setAdapter(adapter);

        cargarArbolesDesdeAPI();
        configurarListeners();
        actualizarVisibilidadBotones();
        configurarBotonesDinamicos();
    }

    private void cargarArbolesDesdeAPI() {
        Log.d(TAG, "Iniciando carga de árboles desde API...");

        Call<List<Arbol>> call;

        if (centroId != -1) {
            Log.d(TAG, "Cargando árboles para el centro ID: " + centroId);
            call = RetrofitClient.getCentroEducativoApi().obtenerArbolesPorCentro(centroId);
        } else {
            Log.d(TAG, "Cargando todos los árboles");
            call = RetrofitClient.getArbolApi().obtenerTodosLosArboles();
        }

        call.enqueue(new Callback<List<Arbol>>() {
            @Override
            public void onResponse(Call<List<Arbol>> call, Response<List<Arbol>> response) {
                if (response.isSuccessful() && response.body() != null) {
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
                    Toast.makeText(ListarArboles.this, "Error al cargar árboles (código: " + response.code() + ")", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Arbol>> call, Throwable t) {
                Log.e(TAG, "Error de conexión: " + t.getMessage());
                Toast.makeText(ListarArboles.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configurarListeners() {
        btVolver.setOnClickListener(v -> finish()); // Cierra la actividad actual y vuelve a la anterior

        btLogin.setOnClickListener(v -> {
            Intent intent = new Intent(ListarArboles.this, Login.class);
            startActivity(intent);
        });

        btCerrarSesion.setOnClickListener(v -> {
            permissionManager.clearSession();
            actualizarVisibilidadBotones();
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
        });
    }

    private void actualizarVisibilidadBotones() {
        boolean isLoggedIn = permissionManager.isLoggedIn();
        btLogin.setVisibility(isLoggedIn ? View.GONE : View.VISIBLE);
        btCerrarSesion.setVisibility(isLoggedIn ? View.VISIBLE : View.GONE);
    }

    /**
     * Configura los botones dinámicos según los permisos del usuario.
     * Muestra/oculta el FAB según si puede crear árboles en este centro.
     */
    private void configurarBotonesDinamicos() {
        // Si el usuario es COORDINADOR, mostrar FAB solo si es de este centro
        // Si el usuario es ADMIN, siempre mostrar FAB
        // Si es PUBLICO, no mostrar FAB

        if (permissionManager.puedeCrearArbol(centroId)) {
            fabAnadirArbol.setVisibility(View.VISIBLE);
            Log.d(TAG, "Usuario puede crear árbol en este centro - FAB visible");

            // Configurar listener para el FAB
            fabAnadirArbol.setOnClickListener(v -> {
                Intent intent = new Intent(ListarArboles.this, CrearArbol.class);
                intent.putExtra("centro_id", centroId);
                startActivity(intent);
            });
        } else {
            fabAnadirArbol.setVisibility(View.GONE);
            Log.d(TAG, "Usuario NO puede crear árbol en este centro - FAB oculto");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarArbolesDesdeAPI();
        actualizarVisibilidadBotones();
        configurarBotonesDinamicos();
    }
}
