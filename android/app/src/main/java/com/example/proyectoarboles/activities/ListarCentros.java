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
    private Button btCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_centros);

        recyclerViewCentros = findViewById(R.id.RecyclerViewCentros);
        btCerrarSesion = findViewById(R.id.btCerrarS);

        // Configurar RecyclerView
        recyclerViewCentros.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CentroEducativoAdapter(listaCentros, centro -> {
            // Al hacer clic en un centro, ir a ListarArboles con el ID del centro
            Intent intent = new Intent(ListarCentros.this, ListarArboles.class);
            intent.putExtra("centro_id", centro.getId());
            startActivity(intent);
        });
        recyclerViewCentros.setAdapter(adapter);

        // Cargar los centros desde la API
        cargarCentrosDesdeAPI();

        // Configurar botón de cerrar sesión
        btCerrarSesion.setOnClickListener(v -> {
            Intent intent = new Intent(ListarCentros.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
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

                    if(listaCentros.isEmpty()){
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
}
