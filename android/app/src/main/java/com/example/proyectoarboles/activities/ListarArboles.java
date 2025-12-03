package com.example.proyectoarboles.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoarboles.R;
import com.example.proyectoarboles.adapter.ArbolAdapter;
import com.example.proyectoarboles.model.Arbol;

import java.util.ArrayList;
import java.util.List;

public class ListarArboles extends AppCompatActivity {

    RecyclerView recyclerViewArboles;
    ArbolAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listar_arboles);

        recyclerViewArboles = findViewById(R.id.RecyclerViewArboles);

        List<Arbol> listaArboles = getArbolesXML();

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
            startActivity(intent);
        });

        recyclerViewArboles.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewArboles.setAdapter(adapter);
    }

    public List<Arbol> getArbolesXML(){
        String[] nombres = getResources().getStringArray(R.array.nombres_arboles);
        String[] especies = getResources().getStringArray(R.array.especies_arboles);
        String[] fechas = getResources().getStringArray(R.array.fecha_plantacion);

        List<Arbol> listaArboles = new ArrayList<>();

        for(int i = 0; i < nombres.length; i++){
            int c = i;
            listaArboles.add(new Arbol(c+1, nombres[i], especies[i], fechas[i]));
        }

        return listaArboles;
    }

    private void mostrarDialogoConfirmacion(int position){
        new AlertDialog.Builder(this)
                .setTitle("Eliminar árbol")
                .setMessage("¿Seguro que quieres eliminar este arbol?")
                .setPositiveButton("Eliminar", (dialog,which) ->{
                    adapter.eliminarArbol(position);
                    Toast.makeText(this, "Arbol eliminado", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}