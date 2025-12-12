package com.example.proyectoarboles.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoarboles.api.RetrofitClient;
import com.example.proyectoarboles.R;
import com.example.proyectoarboles.adapter.ArbolAdapter;
import com.example.proyectoarboles.model.Arbol;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListarArboles extends AppCompatActivity {

    private static final String TAG = "ListarArboles";

    RecyclerView recyclerViewArboles;
    ArbolAdapter adapter;
    ProgressBar progressBar;
    TextView tvEstado;
    private List<Arbol> listaArboles = new ArrayList<>();
    Button btCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listar_arboles);

        // Inicializar vistas
        recyclerViewArboles = findViewById(R.id.RecyclerViewArboles);


        // Configurar adapter
        adapter = new ArbolAdapter(listaArboles, arbol -> {
            Intent intent = new Intent(ListarArboles.this, ArbolDetalles.class);
            intent.putExtra("arbol_id", arbol.getId());
            intent.putExtra("arbol_nombre", arbol.getNombre());
            intent.putExtra("arbol_especie", arbol.getEspecie());
            intent.putExtra("arbol_fecha", arbol.getFechaPlantacion());
            intent.putExtra("arbol_ubicacion", arbol.getUbicacion());
            startActivity(intent);
        });

        recyclerViewArboles.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewArboles.setAdapter(adapter);

        // Cargar datos
        cargarArbolesDesdeAPI();

        btCerrarSesion = findViewById(R.id.btCerrarS);

        btCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sIntent = new Intent(ListarArboles.this, Login.class);
                startActivity(sIntent);
            }
        });
    }

    private void cargarArbolesDesdeAPI() {
        Log.d(TAG, "Iniciando carga de árboles desde API...");

        // Mostrar indicador de carga (si existe)
        // if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
        // if (tvEstado != null) tvEstado.setText("Cargando árboles...");

        Call<List<Arbol>> call = RetrofitClient.getArbolApi().obtenerTodosLosArboles();

        call.enqueue(new Callback<List<Arbol>>() {
            @Override
            public void onResponse(Call<List<Arbol>> call, Response<List<Arbol>> response) {
                // Ocultar indicador de carga
                // if (progressBar != null) progressBar.setVisibility(View.GONE);

                Log.d(TAG, "Respuesta recibida - Código: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    List<Arbol> arbolesRecibidos = response.body();
                    Log.d(TAG, "Árboles recibidos: " + arbolesRecibidos.size());

                    // Limpiar lista y agregar nuevos datos
                    listaArboles.clear();
                    listaArboles.addAll(arbolesRecibidos);

                    // Notificar al adapter
                    adapter.notifyDataSetChanged();

                    // Verificar si la lista está vacía
                    if (listaArboles.isEmpty()) {
                        Toast.makeText(ListarArboles.this,
                                "No hay árboles registrados",
                                Toast.LENGTH_SHORT).show();
                        // if (tvEstado != null) tvEstado.setText("No hay árboles registrados");
                    } else {
                        Toast.makeText(ListarArboles.this,
                                "Árboles cargados: " + listaArboles.size(),
                                Toast.LENGTH_SHORT).show();
                        // if (tvEstado != null) tvEstado.setText("");
                    }

                    // Log detallado de cada árbol
                    for (Arbol arbol : listaArboles) {
                        Log.d(TAG, "Árbol: " + arbol.getNombre() +
                                " | Especie: " + arbol.getEspecie() +
                                " | Fecha: " + arbol.getFechaPlantacion());
                    }

                } else {
                    // Error en la respuesta
                    Log.e(TAG, "Error en respuesta - Código: " + response.code());
                    Log.e(TAG, "Mensaje: " + response.message());

                    Toast.makeText(ListarArboles.this,
                            "Error al cargar árboles (código " + response.code() + ")",
                            Toast.LENGTH_LONG).show();

                    // if (tvEstado != null) tvEstado.setText("Error al cargar datos");

                    // Cargar datos de fallback
                    cargarDatosFallback();
                }
            }

            @Override
            public void onFailure(Call<List<Arbol>> call, Throwable t) {
                // Ocultar indicador de carga
                // if (progressBar != null) progressBar.setVisibility(View.GONE);

                Log.e(TAG, "Error de conexión: " + t.getClass().getSimpleName());
                Log.e(TAG, "Mensaje: " + t.getMessage());
                t.printStackTrace();

                Toast.makeText(ListarArboles.this,
                        "Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();

                // if (tvEstado != null) tvEstado.setText("Error de conexión");

                // Cargar datos de fallback
                cargarDatosFallback();
            }
        });
    }

    private void cargarDatosFallback() {
        Log.d(TAG, "Cargando datos de fallback (XML)...");

        List<Arbol> arbolesXML = getArbolesXML();

        if (!arbolesXML.isEmpty()) {
            listaArboles.clear();
            listaArboles.addAll(arbolesXML);
            adapter.notifyDataSetChanged();

            Toast.makeText(this,
                    "Mostrando datos de ejemplo (" + arbolesXML.size() + " árboles)",
                    Toast.LENGTH_SHORT).show();

            Log.d(TAG, "Datos de fallback cargados: " + arbolesXML.size() + " árboles");
        } else {
            Log.w(TAG, "No hay datos de fallback disponibles");
            Toast.makeText(this,
                    "No hay datos disponibles",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public List<Arbol> getArbolesXML(){
        try {
            String[] nombres = getResources().getStringArray(R.array.nombres_arboles);
            String[] especies = getResources().getStringArray(R.array.especies_arboles);
            String[] fechas = getResources().getStringArray(R.array.fecha_plantacion);

            List<Arbol> listaArboles = new ArrayList<>();

            for(int i = 0; i < nombres.length; i++){
                Long id = Long.valueOf(i + 1);
                listaArboles.add(new Arbol(id, nombres[i], especies[i], fechas[i]));
            }

            return listaArboles;
        } catch (Exception e) {
            Log.e(TAG, "Error al cargar datos XML: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Opcional: Recargar datos al volver a la actividad
        // cargarArbolesDesdeAPI();
    }
}