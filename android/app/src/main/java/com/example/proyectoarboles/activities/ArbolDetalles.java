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

import java.util.Random;

public class ArbolDetalles extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_arbol_detalles);

        Random random = new Random();

        TextView tvNombre = findViewById(R.id.textViewNombreDetalle);
        TextView tvEspecie = findViewById(R.id.textViewEspecieDetalle);
        TextView tvFecha = findViewById(R.id.textViewFechaDetalle);
        TextView tvUbicacion = findViewById(R.id.textViewUbicacion);
        TextView tvTemp = findViewById(R.id.textViewTemp);
        TextView tvHumedad = findViewById(R.id.textViewHumedad);
        TextView tvCO2 = findViewById(R.id.textViewCO2);
        TextView tvHumedadSuelo = findViewById(R.id.textViewHumedadSuelo);

        tvTemp.setText(String.valueOf(random.nextInt(30 )));
        tvHumedad.setText(String.valueOf(random.nextInt(100)));
        tvHumedadSuelo.setText(String.valueOf(random.nextInt(100)));
        tvCO2.setText(String.valueOf(random.nextInt(500) + 1000));
    }
}