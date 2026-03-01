package com.example.proyectoarboles.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoarboles.R;
import com.example.proyectoarboles.api.RetrofitClient;
import com.example.proyectoarboles.model.Arbol;
import com.example.proyectoarboles.model.CentroEducativo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearArbol extends AppCompatActivity {

    private static final String TAG = "CrearArbol";

    private EditText inputNombre;
    private EditText inputEspecie;
    private EditText inputFechaPlantacion;
    private EditText inputUbicacion;
    private Button btnCrear;
    private Button btnCancelar;
    private ProgressBar progressBar;

    private long centroId = -1;
    private String centrNombre = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_arbol);

        // Obtener el ID del centro desde el Intent
        centroId = getIntent().getLongExtra("centro_id", -1);
        centrNombre = getIntent().getStringExtra("centro_nombre");

        // Inicializar vistas
        inputNombre = findViewById(R.id.inputNombreArbol);
        inputEspecie = findViewById(R.id.inputEspecieArbol);
        inputFechaPlantacion = findViewById(R.id.inputFechaPlantacion);
        inputUbicacion = findViewById(R.id.inputUbicacion);
        btnCrear = findViewById(R.id.btnCrearArbol);
        btnCancelar = findViewById(R.id.btnCancelarCrear);
        progressBar = findViewById(R.id.progressBarCrear);

        if (centroId == -1) {
            Toast.makeText(this, "Error: Centro no especificado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Configurar listeners
        btnCrear.setOnClickListener(v -> validarYCrearArbol());
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void validarYCrearArbol() {
        String nombre = inputNombre.getText().toString().trim();
        String especie = inputEspecie.getText().toString().trim();
        String fechaStr = inputFechaPlantacion.getText().toString().trim();
        String ubicacion = inputUbicacion.getText().toString().trim();

        // Validar campos requeridos
        if (nombre.isEmpty()) {
            inputNombre.setError("El nombre es requerido");
            inputNombre.requestFocus();
            return;
        }

        if (especie.isEmpty()) {
            inputEspecie.setError("La especie es requerida");
            inputEspecie.requestFocus();
            return;
        }

        if (fechaStr.isEmpty()) {
            inputFechaPlantacion.setError("La fecha de plantación es requerida");
            inputFechaPlantacion.requestFocus();
            return;
        }

        // Validar formato de fecha (yyyy-MM-dd) y que sea en el pasado
        if (!validarFecha(fechaStr)) {
            inputFechaPlantacion.setError("Fecha inválida. Usa formato YYYY-MM-DD y debe ser en el pasado");
            inputFechaPlantacion.requestFocus();
            return;
        }

        // Si todo es válido, crear el árbol
        crearArbol(nombre, especie, fechaStr, ubicacion);
    }

    private boolean validarFecha(String fechaStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            sdf.setLenient(false);
            Date fecha = sdf.parse(fechaStr);

            // Verificar que la fecha sea en el pasado
            if (fecha.after(new Date())) {
                return false;
            }

            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error al validar fecha: " + e.getMessage());
            return false;
        }
    }

    private void crearArbol(String nombre, String especie, String fechaPlantacion, String ubicacion) {
        // Mostrar progress bar
        progressBar.setVisibility(View.VISIBLE);
        btnCrear.setEnabled(false);

        // Crear objeto CentroEducativo con solo el ID (el backend lo validará)
        CentroEducativo centro = new CentroEducativo();
        centro.setId(centroId);
        centro.setNombre(centrNombre);

        // Crear objeto Arbol
        Arbol nuevoArbol = new Arbol();
        nuevoArbol.setNombre(nombre);
        nuevoArbol.setEspecie(especie);
        nuevoArbol.setFechaPlantacion(fechaPlantacion);
        nuevoArbol.setUbicacion(ubicacion);
        nuevoArbol.setCentroEducativo(centro);

        // Realizar la llamada POST a la API
        Call<Arbol> call = RetrofitClient.getArbolApi().crearArbol(nuevoArbol);

        call.enqueue(new Callback<Arbol>() {
            @Override
            public void onResponse(Call<Arbol> call, Response<Arbol> response) {
                progressBar.setVisibility(View.GONE);
                btnCrear.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    Arbol arbolCreado = response.body();
                    Log.d(TAG, "Árbol creado exitosamente: " + arbolCreado.getNombre());
                    Toast.makeText(CrearArbol.this, "Árbol creado: " + arbolCreado.getNombre(), Toast.LENGTH_SHORT).show();

                    // Volver a ListarArboles (onResume la recargará automáticamente)
                    finish();

                } else {
                    // Manejar errores específicos
                    String errorMsg = "Error al crear el árbol";

                    if (response.code() == 409) {
                        errorMsg = "Ya existe un árbol con ese nombre en este centro";
                    } else if (response.code() == 400) {
                        errorMsg = "Datos inválidos. Verifica los campos.";
                    } else if (response.code() == 403) {
                        errorMsg = "No tienes permiso para crear árboles en este centro";
                    } else if (response.code() == 404) {
                        errorMsg = "Centro educativo no encontrado";
                    }

                    Log.e(TAG, "Error al crear árbol - Código: " + response.code());
                    Toast.makeText(CrearArbol.this, errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Arbol> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnCrear.setEnabled(true);

                Log.e(TAG, "Error de conexión: " + t.getMessage());
                Toast.makeText(CrearArbol.this, "Error de conexión. Inténtalo de nuevo.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
