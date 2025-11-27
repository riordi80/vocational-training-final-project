package com.example.proyectoarboles.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.proyectoarboles.R;
import com.example.proyectoarboles.model.Arbol;

import java.util.ArrayList;
import java.util.List;

public class ListarArbolesActivity extends AppCompatActivity {

    String[] nombres = getResources().getStringArray(R.array.nombres_arboles);
    String[] especies = getResources().getStringArray(R.array.especies_arboles);
    String[] fechas = getResources().getStringArray(R.array.fecha_plantacion);

    List<Arbol> listaArboles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listar_arboles);
        for(int i = 0; i < nombres.length; i++){

        }
    }
}