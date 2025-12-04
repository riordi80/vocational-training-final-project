package com.example.proyectoarboles.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoarboles.R;
import com.example.proyectoarboles.api.RetrofitClient;  // ⚠️ AÑADIR import
import com.example.proyectoarboles.model.Arbol;

import java.util.Random;

import retrofit2.Call;  // ⚠️ AÑADIR imports de Retrofit
import retrofit2.Callback;
import retrofit2.Response;

public class ArbolDetalles extends AppCompatActivity {

    private TextView tvNombre, tvEspecie, tvFecha, tvUbicacion, tvTemp, tvHumedad, tvCO2, tvHumedadSuelo;
    private EditText etNombre, etEspecie, etFecha, etUbicacion, etTemp, etHumedad, etCO2, etHumedadSuelo;
    private Button btnEditar, btnGuardar, btnCancelar, btnVolver;
    private boolean modoEdicion = false;

    // Variables para guardar los valores originales
    private String nombreOriginal, especieOriginal, fechaOriginal, ubicacionOriginal;
    private String tempOriginal, humedadOriginal, co2Original, humedadSueloOriginal;
    private Long arbolId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_arbol_detalles);

        // Obtener los datos del Intent
        Intent intent = getIntent();
        arbolId = intent.getLongExtra("arbol_id", -1);
        String nombre = intent.getStringExtra("arbol_nombre");
        String especie = intent.getStringExtra("arbol_especie");
        String fecha = intent.getStringExtra("arbol_fecha");
        String ubicacion = intent.getStringExtra("arbol_ubicacion");

        // Inicializar TextViews
        tvNombre = findViewById(R.id.textViewNombreDetalle);
        tvEspecie = findViewById(R.id.textViewEspecieDetalle);
        tvFecha = findViewById(R.id.textViewFechaDetalle);
        tvUbicacion = findViewById(R.id.textViewUbicacion);
        tvTemp = findViewById(R.id.textViewTemp);
        tvHumedad = findViewById(R.id.textViewHumedad);
        tvCO2 = findViewById(R.id.textViewCO2);
        tvHumedadSuelo = findViewById(R.id.textViewHumedadSuelo);

        // Inicializar EditTexts
        etNombre = findViewById(R.id.editTextNombreDetalle);
        etEspecie = findViewById(R.id.editTextEspecieDetalle);
        etFecha = findViewById(R.id.editTextFechaDetalle);
        etUbicacion = findViewById(R.id.editTextUbicacion);
        etTemp = findViewById(R.id.editTextTemp);
        etHumedad = findViewById(R.id.editTextHumedad);
        etCO2 = findViewById(R.id.editTextCO2);
        etHumedadSuelo = findViewById(R.id.editTextHumedadSuelo);

        // Inicializar botones
        btnEditar = findViewById(R.id.buttonEditar);
        btnGuardar = findViewById(R.id.buttonGuardar);
        btnCancelar = findViewById(R.id.buttonCancelar);

        // Cargar datos desde la API si hay un ID válido
        if (arbolId != -1) {
            cargarDetallesDesdeAPI(arbolId);
        } else {
            // Fallback: mostrar datos del Intent
            mostrarDatosDelIntent(nombre, especie, fecha, ubicacion);
        }

        // Configurar listeners de botones
        btnEditar.setOnClickListener(v -> activarModoEdicion());
        btnGuardar.setOnClickListener(v -> guardarCambios());
        btnCancelar.setOnClickListener(v -> cancelarEdicion());

        // Inicialmente mostrar solo TextViews
        mostrarTextViews();
    }

    // ️ Metodo para cargar los detalles del arbol desde la API
    private void cargarDetallesDesdeAPI(Long id) {
        Call<Arbol> call = RetrofitClient.getArbolApi().obtenerArbolPorId(id);

        call.enqueue(new Callback<Arbol>() {
            @Override
            public void onResponse(Call<Arbol> call, Response<Arbol> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Arbol arbol = response.body();
                    mostrarDatosDelArbol(arbol);
                } else {
                    Toast.makeText(ArbolDetalles.this,
                            "Error al cargar detalles",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Arbol> call, Throwable t) {
                Log.e("ArbolDetalles", "Error: " + t.getMessage());
                Toast.makeText(ArbolDetalles.this,
                        "Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    // Metodo para mostrar datos del árbol obtenido de la API
    private void mostrarDatosDelArbol(Arbol arbol) {
        tvNombre.setText(arbol.getNombre());
        tvEspecie.setText(arbol.getEspecie());
        tvFecha.setText(arbol.getFechaPlantacion());

        if (arbol.getUbicacion() != null && !arbol.getUbicacion().isEmpty()) {
            tvUbicacion.setText(arbol.getUbicacion());
        } else {
            tvUbicacion.setText("Ubicación no disponible");
        }

        // Mostrar datos de sensores si existen, sino generar aleatorios
        if (arbol.getTemperatura() != null) {
            tvTemp.setText(arbol.getTemperatura() + "°C");
        } else {
            tvTemp.setText(new Random().nextInt(30) + "°C");
        }

        if (arbol.getHumedad() != null) {
            tvHumedad.setText(arbol.getHumedad() + "%");
        } else {
            tvHumedad.setText(new Random().nextInt(100) + "%");
        }

        if (arbol.getHumedadSuelo() != null) {
            tvHumedadSuelo.setText(arbol.getHumedadSuelo() + "%");
        } else {
            tvHumedadSuelo.setText(new Random().nextInt(100) + "%");
        }

        if (arbol.getCo2() != null) {
            tvCO2.setText(arbol.getCo2() + " ppm");
        } else {
            tvCO2.setText((new Random().nextInt(500) + 1000) + " ppm");
        }

        guardarValoresOriginales();
    }

    // Metodo que sirve para mostrar datos del Intent en caso de que la coneccion con la api falle
    private void mostrarDatosDelIntent(String nombre, String especie, String fecha, String ubicacion) {
        tvNombre.setText(nombre);
        tvEspecie.setText(especie);
        tvFecha.setText(fecha);

        if (ubicacion != null && !ubicacion.isEmpty()) {
            tvUbicacion.setText(ubicacion);
        } else {
            tvUbicacion.setText("Ubicación no disponible");
        }

        // Generar valores aleatorios para los sensores
        Random random = new Random();
        String temp = random.nextInt(30) + "°C";
        String humedad = random.nextInt(100) + "%";
        String humedadSuelo = random.nextInt(100) + "%";
        String co2 = (random.nextInt(500) + 1000) + " ppm";

        tvTemp.setText(temp);
        tvHumedad.setText(humedad);
        tvHumedadSuelo.setText(humedadSuelo);
        tvCO2.setText(co2);

        guardarValoresOriginales();
    }

    private void guardarValoresOriginales() {
        nombreOriginal = tvNombre.getText().toString();
        especieOriginal = tvEspecie.getText().toString();
        fechaOriginal = tvFecha.getText().toString();
        ubicacionOriginal = tvUbicacion.getText().toString();
        tempOriginal = tvTemp.getText().toString();
        humedadOriginal = tvHumedad.getText().toString();
        co2Original = tvCO2.getText().toString();
        humedadSueloOriginal = tvHumedadSuelo.getText().toString();
    }

    private void activarModoEdicion() {
        modoEdicion = true;

        // Copiar valores de TextViews a EditTexts
        etNombre.setText(tvNombre.getText());
        etEspecie.setText(tvEspecie.getText());
        etFecha.setText(tvFecha.getText());
        etUbicacion.setText(tvUbicacion.getText());
        etTemp.setText(tvTemp.getText());
        etHumedad.setText(tvHumedad.getText());
        etCO2.setText(tvCO2.getText());
        etHumedadSuelo.setText(tvHumedadSuelo.getText());

        // Mostrar EditTexts y ocultar TextViews
        mostrarEditTexts();
    }

    // Metodo para guardar los cambios en la API
    private void guardarCambios() {
        // Crear objeto Arbol con los nuevos valores
        Arbol arbolActualizado = new Arbol(
                arbolId,
                etNombre.getText().toString(),
                etEspecie.getText().toString(),
                etFecha.getText().toString(),
                etUbicacion.getText().toString(),
                etTemp.getText().toString().replace("°C", "").trim(),
                etHumedad.getText().toString().replace("%", "").trim(),
                etHumedadSuelo.getText().toString().replace("%", "").trim(),
                etCO2.getText().toString().replace(" ppm", "").trim()
        );

        // ️ Si hay un ID válido, actualizar en la API
        if (arbolId != -1) {
            actualizarArbolAPI(arbolActualizado);
        } else {
            // Fallback: solo actualizar localmente
            actualizarLocalmente();
        }
    }

    // ️ Metodo que sirve para actualizar árbol en la API
    private void actualizarArbolAPI(Arbol arbol) {
        Call<Arbol> call = RetrofitClient.getArbolApi().actualizarArbol(arbolId, arbol);

        call.enqueue(new Callback<Arbol>() {
            @Override
            public void onResponse(Call<Arbol> call, Response<Arbol> response) {
                if (response.isSuccessful()) {
                    actualizarLocalmente();
                    Toast.makeText(ArbolDetalles.this,
                            "Cambios guardados en el servidor",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ArbolDetalles.this,
                            "Error al guardar en el servidor",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Arbol> call, Throwable t) {
                Toast.makeText(ArbolDetalles.this,
                        "Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    // Metodo que sirve para actualizar los datos que muestra localmente
    private void actualizarLocalmente() {
        // Copiar valores de EditTexts a TextViews
        tvNombre.setText(etNombre.getText().toString());
        tvEspecie.setText(etEspecie.getText().toString());
        tvFecha.setText(etFecha.getText().toString());
        tvUbicacion.setText(etUbicacion.getText().toString());
        tvTemp.setText(etTemp.getText().toString());
        tvHumedad.setText(etHumedad.getText().toString());
        tvCO2.setText(etCO2.getText().toString());
        tvHumedadSuelo.setText(etHumedadSuelo.getText().toString());

        // Actualizar valores originales
        guardarValoresOriginales();

        modoEdicion = false;
        mostrarTextViews();

        Toast.makeText(this, "Cambios guardados", Toast.LENGTH_SHORT).show();
    }

    private void cancelarEdicion() {
        // Restaurar valores originales
        tvNombre.setText(nombreOriginal);
        tvEspecie.setText(especieOriginal);
        tvFecha.setText(fechaOriginal);
        tvUbicacion.setText(ubicacionOriginal);
        tvTemp.setText(tempOriginal);
        tvHumedad.setText(humedadOriginal);
        tvCO2.setText(co2Original);
        tvHumedadSuelo.setText(humedadSueloOriginal);

        modoEdicion = false;
        mostrarTextViews();

        Toast.makeText(this, "Edición cancelada", Toast.LENGTH_SHORT).show();
    }

    private void mostrarTextViews() {
        // Mostrar TextViews
        tvNombre.setVisibility(View.VISIBLE);
        tvEspecie.setVisibility(View.VISIBLE);
        tvFecha.setVisibility(View.VISIBLE);
        tvUbicacion.setVisibility(View.VISIBLE);
        tvTemp.setVisibility(View.VISIBLE);
        tvHumedad.setVisibility(View.VISIBLE);
        tvCO2.setVisibility(View.VISIBLE);
        tvHumedadSuelo.setVisibility(View.VISIBLE);

        // Ocultar EditTexts
        etNombre.setVisibility(View.GONE);
        etEspecie.setVisibility(View.GONE);
        etFecha.setVisibility(View.GONE);
        etUbicacion.setVisibility(View.GONE);
        etTemp.setVisibility(View.GONE);
        etHumedad.setVisibility(View.GONE);
        etCO2.setVisibility(View.GONE);
        etHumedadSuelo.setVisibility(View.GONE);

        // Mostrar solo botón Editar
        btnEditar.setVisibility(View.VISIBLE);
        btnGuardar.setVisibility(View.GONE);
        btnCancelar.setVisibility(View.GONE);
    }

    private void mostrarEditTexts() {
        // Ocultar TextViews
        tvNombre.setVisibility(View.GONE);
        tvEspecie.setVisibility(View.GONE);
        tvFecha.setVisibility(View.GONE);
        tvUbicacion.setVisibility(View.GONE);
        tvTemp.setVisibility(View.GONE);
        tvHumedad.setVisibility(View.GONE);
        tvCO2.setVisibility(View.GONE);
        tvHumedadSuelo.setVisibility(View.GONE);

        // Mostrar EditTexts
        etNombre.setVisibility(View.VISIBLE);
        etEspecie.setVisibility(View.VISIBLE);
        etFecha.setVisibility(View.VISIBLE);
        etUbicacion.setVisibility(View.VISIBLE);
        etUbicacion.setVisibility(View.VISIBLE);
        etTemp.setVisibility(View.VISIBLE);
        etHumedad.setVisibility(View.VISIBLE);
        etCO2.setVisibility(View.VISIBLE);
        etHumedadSuelo.setVisibility(View.VISIBLE);

        // Mostrar botones Guardar y Cancelar, ocultar Editar
        btnEditar.setVisibility(View.GONE);
        btnGuardar.setVisibility(View.VISIBLE);
        btnCancelar.setVisibility(View.VISIBLE);
    }
}