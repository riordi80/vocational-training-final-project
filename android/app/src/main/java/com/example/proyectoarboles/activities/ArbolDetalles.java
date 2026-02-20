package com.example.proyectoarboles.activities;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoarboles.R;
import com.example.proyectoarboles.api.RetrofitClient;
import com.example.proyectoarboles.model.Arbol;
import com.example.proyectoarboles.model.CentroEducativo;
import com.example.proyectoarboles.model.Rol;
import com.example.proyectoarboles.util.PermissionManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArbolDetalles extends AppCompatActivity {

    private static final String TAG = "ArbolDetalles";

    // Vistas
    private TextView tvNombre, tvEspecie, tvFecha, tvUbicacion, tvCentroEducativo, tvTemp, tvHumedad, tvCO2, tvHumedadSuelo;
    private EditText etNombre, etEspecie, etFecha, etUbicacion, etTemp, etHumedad, etCO2, etHumedadSuelo;
    private Spinner spinnerCentroEducativo;
    private Button btnEditar, btnGuardar, btnCancelar, btnEliminar, btnVolver;

    // Datos del árbol y estado
    private Arbol arbolActual;
    private Long arbolId;
    private boolean modoEdicion = false;
    private String fechaISO;

    // Datos de sesión
    private SharedPreferences sharedPreferences;
    private PermissionManager permissionManager;
    private Rol userRole;
    private Set<String> userCentrosIds;

    // Adapter para Spinner
    private List<CentroEducativo> listaCentros = new ArrayList<>();
    private ArrayAdapter<CentroEducativo> centrosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arbol_detalles);

        cargarDatosSesion();
        inicializarVistas();
        configurarListeners();

        arbolId = getIntent().getLongExtra("arbol_id", -1);
        if (arbolId != -1) {
            cargarDetallesDesdeAPI(arbolId);
        } else {
            Toast.makeText(this, "Error: ID de árbol no válido", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void cargarDatosSesion() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        permissionManager = new PermissionManager(this);
        String roleString = sharedPreferences.getString("user_role", null);
        userRole = (roleString != null) ? Rol.valueOf(roleString) : null;
        userCentrosIds = sharedPreferences.getStringSet("user_centros", Collections.emptySet());
    }

    private void inicializarVistas() {
        tvNombre = findViewById(R.id.textViewNombreDetalle);
        tvEspecie = findViewById(R.id.textViewEspecieDetalle);
        tvFecha = findViewById(R.id.textViewFechaDetalle);
        tvUbicacion = findViewById(R.id.textViewUbicacion);
        tvCentroEducativo = findViewById(R.id.textViewCentroEducativo);
        tvTemp = findViewById(R.id.textViewTemp);
        tvHumedad = findViewById(R.id.textViewHumedad);
        tvCO2 = findViewById(R.id.textViewCO2);
        tvHumedadSuelo = findViewById(R.id.textViewHumedadSuelo);

        etNombre = findViewById(R.id.editTextNombreDetalle);
        etEspecie = findViewById(R.id.editTextEspecieDetalle);
        etFecha = findViewById(R.id.editTextFechaDetalle);
        etUbicacion = findViewById(R.id.editTextUbicacion);
        etTemp = findViewById(R.id.editTextTemp);
        etHumedad = findViewById(R.id.editTextHumedad);
        etCO2 = findViewById(R.id.editTextCO2);
        etHumedadSuelo = findViewById(R.id.editTextHumedadSuelo);

        spinnerCentroEducativo = findViewById(R.id.spinnerCentroEducativo);
        centrosAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaCentros);
        centrosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCentroEducativo.setAdapter(centrosAdapter);

        btnEditar = findViewById(R.id.buttonEditar);
        btnGuardar = findViewById(R.id.buttonGuardar);
        btnCancelar = findViewById(R.id.buttonCancelar);
        btnEliminar = findViewById(R.id.buttonEliminar);
        btnVolver = findViewById(R.id.buttonVolver);
    }

    private void configurarListeners() {
        btnEditar.setOnClickListener(v -> activarModoEdicion());
        btnGuardar.setOnClickListener(v -> guardarCambios());
        btnCancelar.setOnClickListener(v -> cancelarEdicion());
        btnEliminar.setOnClickListener(v -> mostrarDialogoEliminar());
        btnVolver.setOnClickListener(v -> finish()); // Más eficiente que iniciar una nueva actividad
    }

    private void cargarDetallesDesdeAPI(Long id) {
        Call<Arbol> call = RetrofitClient.getArbolApi().obtenerArbolPorId(id);
        call.enqueue(new Callback<Arbol>() {
            @Override
            public void onResponse(Call<Arbol> call, Response<Arbol> response) {
                if (response.isSuccessful() && response.body() != null) {
                    arbolActual = response.body();
                    mostrarDatosDelArbol(arbolActual);
                    verificarPermisosYActualizarUI();
                } else {
                    Toast.makeText(ArbolDetalles.this, "Error al cargar detalles del árbol", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Arbol> call, Throwable t) {
                Log.e(TAG, "Error de conexión: " + t.getMessage());
                Toast.makeText(ArbolDetalles.this, "Error de conexión", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void mostrarDatosDelArbol(Arbol arbol) {
        tvNombre.setText(arbol.getNombre());
        tvEspecie.setText(arbol.getEspecie());
        fechaISO = arbol.getFechaPlantacion();
        tvFecha.setText(formatearFechaEspanol(fechaISO));
        tvUbicacion.setText(arbol.getUbicacion() != null ? arbol.getUbicacion() : "No disponible");

        if (arbol.getCentroEducativo() != null) {
            tvCentroEducativo.setText(arbol.getCentroEducativo().getNombre());
        } else {
            tvCentroEducativo.setText("Sin centro asignado");
        }

        // Sensores (con valores aleatorios de fallback)
        Random random = new Random();
        tvTemp.setText(arbol.getTemperatura() != null ? arbol.getTemperatura() + "°C" : random.nextInt(30) + "°C");
        tvHumedad.setText(arbol.getHumedad() != null ? arbol.getHumedad() + "%" : random.nextInt(100) + "%");
        tvHumedadSuelo.setText(arbol.getHumedadSuelo() != null ? arbol.getHumedadSuelo() + "%" : random.nextInt(100) + "%");
        tvCO2.setText(arbol.getCo2() != null ? arbol.getCo2() + " ppm" : (random.nextInt(500) + 1000) + " ppm");
    }

    private void verificarPermisosYActualizarUI() {
        if (arbolActual == null || arbolActual.getCentroEducativo() == null) return;

        Long centroId = arbolActual.getCentroEducativo().getId();

        // Usar PermissionManager para validar permisos
        boolean puedeEditar = permissionManager.puedeEditarArbol(centroId);
        boolean puedeEliminar = permissionManager.puedeEliminarArbol(centroId);

        btnEditar.setVisibility(puedeEditar ? View.VISIBLE : View.GONE);
        btnEliminar.setVisibility(puedeEliminar ? View.VISIBLE : View.GONE);
    }

    private void activarModoEdicion() {
        // ... (similar a tu implementación original)
        mostrarEditTexts();
    }

    private void guardarCambios() {
        // ... (similar a tu implementación original)
        mostrarTextViews();
        verificarPermisosYActualizarUI();
    }

    private void cancelarEdicion() {
        mostrarTextViews();
        verificarPermisosYActualizarUI();
    }

    private void mostrarTextViews() {
        // Ocultar EditTexts y mostrar TextViews
        etNombre.setVisibility(View.GONE);
        tvNombre.setVisibility(View.VISIBLE);
        // ... (hacer lo mismo para el resto de vistas)

        // Gestionar visibilidad de botones
        btnGuardar.setVisibility(View.GONE);
        btnCancelar.setVisibility(View.GONE);
        verificarPermisosYActualizarUI(); // Muestra Editar/Eliminar si corresponde
    }

    private void mostrarEditTexts() {
        // Ocultar TextViews y mostrar EditTexts
        tvNombre.setVisibility(View.GONE);
        etNombre.setVisibility(View.VISIBLE);
        etNombre.setText(tvNombre.getText());
        // ... (hacer lo mismo para el resto de vistas)

        // Gestionar visibilidad de botones
        btnEditar.setVisibility(View.GONE);
        btnEliminar.setVisibility(View.GONE);
        btnGuardar.setVisibility(View.VISIBLE);
        btnCancelar.setVisibility(View.VISIBLE);
    }

    private void mostrarDialogoEliminar() {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar árbol")
                .setMessage("¿Estás seguro de que deseas eliminar '" + tvNombre.getText().toString() + "'?")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarArbolAPI())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarArbolAPI() {
        Call<Arbol> call = RetrofitClient.getArbolApi().eliminarArbol(arbolId);
        call.enqueue(new Callback<Arbol>() {
            @Override
            public void onResponse(Call<Arbol> call, Response<Arbol> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ArbolDetalles.this, "Árbol eliminado", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ArbolDetalles.this, "Error al eliminar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Arbol> call, Throwable t) {
                Toast.makeText(ArbolDetalles.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String formatearFechaEspanol(String fechaISO) {
        if (fechaISO == null || fechaISO.isEmpty()) return "Fecha no disponible";
        try {
            SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = formatoEntrada.parse(fechaISO);
            SimpleDateFormat formatoSalida = new SimpleDateFormat("d MMM yyyy", new Locale("es", "ES"));
            return formatoSalida.format(date);
        } catch (ParseException e) {
            Log.e(TAG, "Error al parsear fecha: " + fechaISO, e);
            return fechaISO; // Devolver fecha original si el formato es inesperado
        }
    }
}
