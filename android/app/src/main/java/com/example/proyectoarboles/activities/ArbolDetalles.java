package com.example.proyectoarboles.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.proyectoarboles.R;

public class ArbolDetalles extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_arbol_detalles);

        TextView tvNombre = findViewById(R.id.textViewNombreDetalle);
        TextView tvEspecie = findViewById(R.id.textViewEspecieDetalle);
        TextView tvFecha = findViewById(R.id.textViewFechaDetalle);
        TextView tvUbicacion = findViewById(R.id.textViewUbicacion);
        TextView tvTemp = findViewById(R.id.textViewTemp);
        TextView tvHumedad = findViewById(R.id.textViewHumedad);
        TextView tvCO2 = findViewById(R.id.textViewCO2);
        TextView tvHumedadSuelo = findViewById(R.id.textViewHumedadSuelo);

        Intent intent = getIntent();

        if (intent != null){
        }
    }
}