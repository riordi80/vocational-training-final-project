package com.example.proyectoarboles.activities;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.proyectoarboles.R;
import com.example.proyectoarboles.api.RetrofitClient;
import com.example.proyectoarboles.dto.LecturaMuestraProjection;
import com.example.proyectoarboles.model.Arbol;
import com.example.proyectoarboles.model.CentroEducativo;
import com.example.proyectoarboles.model.Lectura;
import com.example.proyectoarboles.model.Rol;
import com.example.proyectoarboles.util.PermissionManager;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArbolDetalles extends AppCompatActivity {

    private static final String TAG = "ArbolDetalles";

    // Vistas
    private TextView tvNombre, tvEspecie, tvFecha, tvUbicacion, tvCentroEducativo, tvTemp, tvHumedad, tvCO2, tvHumedadSuelo, tvUltimaActualizacion;
    private EditText etNombre, etEspecie, etFecha, etUbicacion;
    private Spinner spinnerCentroEducativo;
    private Button btnEditar, btnGuardar, btnCancelar, btnEliminar, btnVolver;
    private LineChart lineChart;
    private RadioGroup radioGroupPeriodo;

    // Datos del árbol y estado
    private Arbol arbolActual;
    private Long arbolId;

    // Datos de sesión
    private SharedPreferences sharedPreferences;
    private PermissionManager permissionManager;
    private Rol userRole;
    private Set<String> userCentrosIds;

    // Adapter para Spinner
    private List<CentroEducativo> listaCentros = new ArrayList<>();
    private ArrayAdapter<CentroEducativo> centrosAdapter;

    // Handler para actualización en tiempo real cada 30 segundos
    private Handler handlerActualizacion;
    private final long INTERVALO_ACTUALIZACION = 30000; // 30 segundos en milisegundos
    private Runnable runnableActualizacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arbol_detalles);

        // Inicializar Handler para actualización periódica
        handlerActualizacion = new Handler(Looper.getMainLooper());

        cargarDatosSesion();
        inicializarVistas();
        configurarListeners();
        inicializarLineChart();

        arbolId = getIntent().getLongExtra("arbol_id", -1);
        if (arbolId != -1) {
            cargarDetallesDesdeAPI(arbolId);
            iniciarActualizacionTiempoReal(arbolId);
            configurarSelectorPeriodo(arbolId);
            // Cargar gráfica inicial (DÍA por defecto)
            cargarGraficaHistorica(arbolId, "DIA");
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
        tvUltimaActualizacion = findViewById(R.id.textViewUltimaActualizacion);

        etNombre = findViewById(R.id.editTextNombreDetalle);
        etEspecie = findViewById(R.id.editTextEspecieDetalle);
        etFecha = findViewById(R.id.editTextFechaDetalle);
        etUbicacion = findViewById(R.id.editTextUbicacion);

        spinnerCentroEducativo = findViewById(R.id.spinnerCentroEducativo);
        centrosAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaCentros);
        centrosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCentroEducativo.setAdapter(centrosAdapter);

        btnEditar = findViewById(R.id.buttonEditar);
        btnGuardar = findViewById(R.id.buttonGuardar);
        btnCancelar = findViewById(R.id.buttonCancelar);
        btnEliminar = findViewById(R.id.buttonEliminar);
        btnVolver = findViewById(R.id.buttonVolver);

        // Vistas para gráfica
        lineChart = findViewById(R.id.lineChartHistorico);
        radioGroupPeriodo = findViewById(R.id.radioGroupPeriodo);
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
        String fechaISO = arbol.getFechaPlantacion();
        tvFecha.setText(formatearFechaEspanol(fechaISO));
        tvUbicacion.setText(arbol.getUbicacion() != null ? arbol.getUbicacion() : "No disponible");

        if (arbol.getCentroEducativo() != null) {
            tvCentroEducativo.setText(arbol.getCentroEducativo().getNombre());
        } else {
            tvCentroEducativo.setText("Sin centro asignado");
        }

        // Los datos de sensores se cargan desde la API en cargarDatosEnTiempoReal()
        // Inicializar con valores por defecto--
        tvTemp.setText("--");
        tvHumedad.setText("--");
        tvHumedadSuelo.setText("--");
        tvCO2.setText("--");
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
        // Ocultar EditTexts y mostrar TextViews para datos editables
        etNombre.setVisibility(View.GONE);
        tvNombre.setVisibility(View.VISIBLE);

        etEspecie.setVisibility(View.GONE);
        tvEspecie.setVisibility(View.VISIBLE);

        etFecha.setVisibility(View.GONE);
        tvFecha.setVisibility(View.VISIBLE);

        etUbicacion.setVisibility(View.GONE);
        tvUbicacion.setVisibility(View.VISIBLE);

        spinnerCentroEducativo.setVisibility(View.GONE);
        tvCentroEducativo.setVisibility(View.VISIBLE);

        // Gestionar visibilidad de botones
        btnGuardar.setVisibility(View.GONE);
        btnCancelar.setVisibility(View.GONE);
        verificarPermisosYActualizarUI(); // Muestra Editar/Eliminar si corresponde
    }

    private void mostrarEditTexts() {
        // Ocultar TextViews y mostrar EditTexts para datos editables
        tvNombre.setVisibility(View.GONE);
        etNombre.setVisibility(View.VISIBLE);
        etNombre.setText(tvNombre.getText());

        tvEspecie.setVisibility(View.GONE);
        etEspecie.setVisibility(View.VISIBLE);
        etEspecie.setText(tvEspecie.getText());

        tvFecha.setVisibility(View.GONE);
        etFecha.setVisibility(View.VISIBLE);
        etFecha.setText(tvFecha.getText());

        tvUbicacion.setVisibility(View.GONE);
        etUbicacion.setVisibility(View.VISIBLE);
        etUbicacion.setText(tvUbicacion.getText());

        tvCentroEducativo.setVisibility(View.GONE);
        spinnerCentroEducativo.setVisibility(View.VISIBLE);

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

    private void inicializarLineChart() {
        lineChart.setNoDataText("Cargando datos...");
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);

        // Configurar eje X
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));

        // Configurar leyenda
        lineChart.getLegend().setEnabled(true);
        lineChart.getLegend().setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));
    }

    private void cargarDatosEnTiempoReal(Long arbolId) {
        Call<List<Lectura>> call = RetrofitClient.getLecturaApi().obtenerUltimaLectura(arbolId, 0, 1);
        call.enqueue(new Callback<List<Lectura>>() {
            @Override
            public void onResponse(Call<List<Lectura>> call, Response<List<Lectura>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Lectura lectura = response.body().get(0);

                    // Mostrar datos con unidades
                    tvTemp.setText(lectura.getTemperatura() != null ?
                            String.format(Locale.getDefault(), "%.1f °C", lectura.getTemperatura()) : "--");
                    tvHumedad.setText(lectura.getHumedadAmbiente() != null ?
                            String.format(Locale.getDefault(), "%.1f %%", lectura.getHumedadAmbiente()) : "--");
                    tvHumedadSuelo.setText(lectura.getHumedadSuelo() != null ?
                            String.format(Locale.getDefault(), "%.1f %%", lectura.getHumedadSuelo()) : "--");
                    tvCO2.setText(lectura.getCo2() != null ?
                            String.format(Locale.getDefault(), "%.0f ppm", lectura.getCo2()) : "--");

                    // Mostrar timestamp de última actualización
                    tvUltimaActualizacion.setText("Última actualización: " + lectura.getTimestamp());
                } else {
                    Log.e(TAG, "Error al obtener lecturas");
                }
            }

            @Override
            public void onFailure(Call<List<Lectura>> call, Throwable t) {
                Log.e(TAG, "Error de conexión al cargar datos en tiempo real: " + t.getMessage());
            }
        });
    }

    /**
     * Inicia la actualización automática de datos en tiempo real cada 30 segundos
     */
    private void iniciarActualizacionTiempoReal(Long arbolId) {
        // Crear el Runnable que se ejecutará periódicamente
        runnableActualizacion = new Runnable() {
            @Override
            public void run() {
                // Cargar datos en tiempo real
                cargarDatosEnTiempoReal(arbolId);

                // Programar la siguiente ejecución en 30 segundos
                handlerActualizacion.postDelayed(this, INTERVALO_ACTUALIZACION);
            }
        };

        // Ejecutar la primera carga inmediatamente
        cargarDatosEnTiempoReal(arbolId);

        // Programar las siguientes actualizaciones cada 30 segundos
        handlerActualizacion.postDelayed(runnableActualizacion, INTERVALO_ACTUALIZACION);

        Log.d(TAG, "Actualización automática iniciada - Intervalo: " + INTERVALO_ACTUALIZACION + "ms");
    }

    /**
     * Detiene la actualización automática de datos (llamar en onDestroy)
     */
    private void detenerActualizacionTiempoReal() {
        if (handlerActualizacion != null && runnableActualizacion != null) {
            handlerActualizacion.removeCallbacks(runnableActualizacion);
            Log.d(TAG, "Actualización automática detenida");
        }
    }

    private void configurarSelectorPeriodo(Long arbolId) {
        radioGroupPeriodo.setOnCheckedChangeListener((group, checkedId) -> {
            String periodo;
            if (checkedId == R.id.radioBtnDia) {
                periodo = "DIA";
            } else if (checkedId == R.id.radioBtnSemana) {
                periodo = "SEMANA";
            } else if (checkedId == R.id.radioBtnMes) {
                periodo = "MES";
            } else {
                periodo = "DIA";
            }
            cargarGraficaHistorica(arbolId, periodo);
        });
    }

    private void cargarGraficaHistorica(Long arbolId, String periodo) {
        Call<List<LecturaMuestraProjection>> call = RetrofitClient.getLecturaApi()
                .obtenerLecturasParaGrafica(arbolId, periodo);
        call.enqueue(new Callback<List<LecturaMuestraProjection>>() {
            @Override
            public void onResponse(Call<List<LecturaMuestraProjection>> call, Response<List<LecturaMuestraProjection>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<LecturaMuestraProjection> lecturas = response.body();
                    if (!lecturas.isEmpty()) {
                        crearGrafica(lecturas);
                    } else {
                        Log.w(TAG, "No hay datos para el período: " + periodo);
                        lineChart.setNoDataText("No hay datos disponibles");
                        lineChart.invalidate(); // Refrescar para mostrar el texto
                    }
                } else {
                    Log.e(TAG, "Error al obtener lecturas para gráfica");
                    lineChart.setNoDataText("Error al cargar datos");
                    lineChart.invalidate();
                }
            }

            @Override
            public void onFailure(Call<List<LecturaMuestraProjection>> call, Throwable t) {
                Log.e(TAG, "Error de conexión al cargar gráfica: " + t.getMessage());
                lineChart.setNoDataText("Error de conexión");
                lineChart.invalidate();
            }
        });
    }

    private void crearGrafica(List<LecturaMuestraProjection> lecturas) {
        // Crear datasets para cada sensor
        ArrayList<Entry> temperaturasEntries = new ArrayList<>();
        ArrayList<Entry> humedadAmbienteEntries = new ArrayList<>();
        ArrayList<Entry> humedadSueloEntries = new ArrayList<>();

        // Procesar cada lectura
        for (int i = 0; i < lecturas.size(); i++) {
            LecturaMuestraProjection lectura = lecturas.get(i);

            if (lectura.getTemperatura() != null) {
                temperaturasEntries.add(new Entry(i, lectura.getTemperatura().floatValue()));
            }
            if (lectura.getHumedadAmbiente() != null) {
                humedadAmbienteEntries.add(new Entry(i, lectura.getHumedadAmbiente().floatValue()));
            }
            if (lectura.getHumedadSuelo() != null) {
                humedadSueloEntries.add(new Entry(i, lectura.getHumedadSuelo().floatValue()));
            }
        }

        // Crear LineDataSet para cada sensor
        LineDataSet temperaturasDataSet = new LineDataSet(temperaturasEntries, "Temperatura (°C)");
        temperaturasDataSet.setColor(ContextCompat.getColor(this, android.R.color.holo_red_light));
        temperaturasDataSet.setCircleColor(ContextCompat.getColor(this, android.R.color.holo_red_light));
        temperaturasDataSet.setLineWidth(2);
        temperaturasDataSet.setCircleRadius(3);
        temperaturasDataSet.setDrawValues(false);

        LineDataSet humedadAmbienteDataSet = new LineDataSet(humedadAmbienteEntries, "Humedad Ambiente (%)");
        humedadAmbienteDataSet.setColor(ContextCompat.getColor(this, android.R.color.holo_blue_light));
        humedadAmbienteDataSet.setCircleColor(ContextCompat.getColor(this, android.R.color.holo_blue_light));
        humedadAmbienteDataSet.setLineWidth(2);
        humedadAmbienteDataSet.setCircleRadius(3);
        humedadAmbienteDataSet.setDrawValues(false);

        LineDataSet humedadSueloDataSet = new LineDataSet(humedadSueloEntries, "Humedad Suelo (%)");
        humedadSueloDataSet.setColor(ContextCompat.getColor(this, android.R.color.holo_green_light));
        humedadSueloDataSet.setCircleColor(ContextCompat.getColor(this, android.R.color.holo_green_light));
        humedadSueloDataSet.setLineWidth(2);
        humedadSueloDataSet.setCircleRadius(3);
        humedadSueloDataSet.setDrawValues(false);

        // Combinar todos los datasets
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(temperaturasDataSet);
        dataSets.add(humedadAmbienteDataSet);
        dataSets.add(humedadSueloDataSet);

        LineData lineData = new LineData(dataSets);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Detener la actualización automática para evitar memory leaks
        detenerActualizacionTiempoReal();
    }
}
