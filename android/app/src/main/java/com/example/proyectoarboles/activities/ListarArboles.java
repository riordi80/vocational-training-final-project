package com.example.proyectoarboles.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    RecyclerView recyclerViewArboles;
    ArbolAdapter adapter;
    private List<Arbol> listaArboles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listar_arboles);

        recyclerViewArboles = findViewById(R.id.RecyclerViewArboles);

         adapter = new ArbolAdapter(listaArboles,
                 new ArbolAdapter.onArbolDeleteListener() {
                    @Override
                    public void onArbolDelete(int position) {
                        mostrarDialogoConfirmacion(position);
                    }
                }, arbol -> {
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

        cargarArbolesDesdeAPI();
    }

    // Este metodo se encarga de recoger los datos desde la api
    private void cargarArbolesDesdeAPI() {
        Call<List<Arbol>> call = RetrofitClient.getArbolApi().obtenerTodosLosArboles();

        call.enqueue(new Callback<List<Arbol>>() {
            @Override
            public void onResponse(Call<List<Arbol>> call, Response<List<Arbol>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaArboles.clear();
                    listaArboles.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    Toast.makeText(ListarArboles.this,
                            "Árboles cargados: " + listaArboles.size(),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ListarArboles.this,
                            "Error al cargar árboles",
                            Toast.LENGTH_SHORT).show();
                    // ⚠️ OPCIONAL: Si falla, cargar datos de XML como fallback
                    listaArboles.addAll(getArbolesXML());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Arbol>> call, Throwable t) {
                Log.e("ListarArboles", "Error: " + t.getMessage());
                Toast.makeText(ListarArboles.this,
                        "Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
                // Cargar datos de XML como fallback
                listaArboles.addAll(getArbolesXML());
                adapter.notifyDataSetChanged();
            }
        });
    }


    //Metodo que sirve de fallback (respaldo por si no se consiguen cargar los datos desde la api)
    public List<Arbol> getArbolesXML(){
        String[] nombres = getResources().getStringArray(R.array.nombres_arboles);
        String[] especies = getResources().getStringArray(R.array.especies_arboles);
        String[] fechas = getResources().getStringArray(R.array.fecha_plantacion);

        List<Arbol> listaArboles = new ArrayList<>();

        for(int i = 0; i < nombres.length; i++){
            Long c = Long.valueOf(i);
            listaArboles.add(new Arbol(c+1, nombres[i], especies[i], fechas[i]));
        }

        return listaArboles;
    }

    private void mostrarDialogoConfirmacion(int position){
        Arbol arbol = listaArboles.get(position);

        new AlertDialog.Builder(this)
                .setTitle("Eliminar árbol")
                .setMessage("¿Seguro que quieres eliminar este arbol?")
                .setPositiveButton("Eliminar", (dialog,which) ->{
                    eliminarArbolAPI(arbol.getId(), position);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
    private void eliminarArbolAPI(Long id, int position) {
        Call<Arbol> call = RetrofitClient.getArbolApi().eliminarArbol(id);

        call.enqueue(new Callback<Arbol>() {
            @Override
            public void onResponse(Call<Arbol> call, Response<Arbol> response) {
                if (response.isSuccessful()) {
                    adapter.eliminarArbol(position);
                    Toast.makeText(ListarArboles.this,
                            "Árbol eliminado",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ListarArboles.this,
                            "Error al eliminar árbol",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Arbol> call, Throwable t) {
                Toast.makeText(ListarArboles.this,
                        "Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}