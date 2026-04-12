package com.example.proyectoarboles.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoarboles.R;
import com.example.proyectoarboles.api.RetrofitClient;
import com.example.proyectoarboles.model.CentroEducativo;
import com.example.proyectoarboles.model.DispositivoEsp32;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormularioDispositivoActivity extends AppCompatActivity {

    private static final String TAG = "FormularioDispositivo";
    private static final Pattern MAC_REGEX = Pattern.compile("^([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}$");

    private Long dispositivoId;
    private Long centroIdPreseleccionado;
    private boolean esEdicion;

    private TextView tvTitulo;
    private EditText etMacAddress, etFrecuencia;
    private EditText etUmbralTempMin, etUmbralTempMax;
    private EditText etUmbralHumAmbMin, etUmbralHumAmbMax;
    private EditText etUmbralHumSueloMin, etUmbralCO2Max;
    private CheckBox checkBoxActivo;
    private Spinner spinnerCentro;
    private Button btnCancelar, btnGuardar;

    private List<CentroEducativo> listaCentros = new ArrayList<>();
    private ArrayAdapter<CentroEducativo> centrosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_dispositivo);

        dispositivoId = getIntent().getLongExtra("dispositivo_id", -1);
        centroIdPreseleccionado = getIntent().getLongExtra("centro_id", -1);
        esEdicion = dispositivoId != -1;

        inicializarVistas();
        configurarSpinnerCentros();
        configurarListeners();

        if (esEdicion) {
            tvTitulo.setText("Editar Dispositivo");
            cargarDatosDispositivo(dispositivoId);
        } else {
            tvTitulo.setText("Nuevo Dispositivo");
            cargarCentros();
        }
    }

    private void inicializarVistas() {
        tvTitulo = findViewById(R.id.textViewTituloFormularioDispositivo);
        etMacAddress = findViewById(R.id.editTextMacAddress);
        etFrecuencia = findViewById(R.id.editTextFrecuencia);
        etFrecuencia.setText("30");
        etUmbralTempMin = findViewById(R.id.editTextUmbralTempMin);
        etUmbralTempMax = findViewById(R.id.editTextUmbralTempMax);
        etUmbralHumAmbMin = findViewById(R.id.editTextUmbralHumedadAmbienteMin);
        etUmbralHumAmbMax = findViewById(R.id.editTextUmbralHumedadAmbienteMax);
        etUmbralHumSueloMin = findViewById(R.id.editTextUmbralHumedadSueloMin);
        etUmbralCO2Max = findViewById(R.id.editTextUmbralCO2Max);
        checkBoxActivo = findViewById(R.id.checkBoxActivo);
        spinnerCentro = findViewById(R.id.spinnerCentroDispositivo);
        btnCancelar = findViewById(R.id.buttonCancelarFormularioDispositivo);
        btnGuardar = findViewById(R.id.buttonGuardarDispositivo);
    }

    private void configurarSpinnerCentros() {
        centrosAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaCentros);
        centrosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCentro.setAdapter(centrosAdapter);
    }

    private void configurarListeners() {
        btnCancelar.setOnClickListener(v -> finish());
        btnGuardar.setOnClickListener(v -> guardarDispositivo());
    }

    private void cargarCentros() {
        Call<List<CentroEducativo>> call = RetrofitClient.getCentroEducativoApi().obtenerTodosLosCentros();
        call.enqueue(new Callback<List<CentroEducativo>>() {
            @Override
            public void onResponse(Call<List<CentroEducativo>> call, Response<List<CentroEducativo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaCentros.clear();
                    listaCentros.addAll(response.body());
                    centrosAdapter.notifyDataSetChanged();

                    // Preseleccionar centro si se pasó como extra
                    if (centroIdPreseleccionado != -1) {
                        for (int i = 0; i < listaCentros.size(); i++) {
                            if (listaCentros.get(i).getId().equals(centroIdPreseleccionado)) {
                                spinnerCentro.setSelection(i);
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CentroEducativo>> call, Throwable t) {
                Log.e(TAG, "Error cargando centros: " + t.getMessage());
            }
        });
    }

    private void cargarDatosDispositivo(Long id) {
        // Primero cargar centros, luego datos del dispositivo
        Call<List<CentroEducativo>> callCentros = RetrofitClient.getCentroEducativoApi().obtenerTodosLosCentros();
        callCentros.enqueue(new Callback<List<CentroEducativo>>() {
            @Override
            public void onResponse(Call<List<CentroEducativo>> call, Response<List<CentroEducativo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaCentros.clear();
                    listaCentros.addAll(response.body());
                    centrosAdapter.notifyDataSetChanged();
                    cargarDispositivo(id);
                }
            }

            @Override
            public void onFailure(Call<List<CentroEducativo>> call, Throwable t) {
                Log.e(TAG, "Error cargando centros: " + t.getMessage());
            }
        });
    }

    private void cargarDispositivo(Long id) {
        Call<DispositivoEsp32> call = RetrofitClient.getDispositivoApi().obtenerDispositivoPorId(id);
        call.enqueue(new Callback<DispositivoEsp32>() {
            @Override
            public void onResponse(Call<DispositivoEsp32> call, Response<DispositivoEsp32> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DispositivoEsp32 d = response.body();
                    etMacAddress.setText(d.getMacAddress());
                    etFrecuencia.setText(d.getFrecuenciaLecturaSeg() != null ? String.valueOf(d.getFrecuenciaLecturaSeg()) : "30");
                    checkBoxActivo.setChecked(Boolean.TRUE.equals(d.getActivo()));

                    if (d.getUmbralTempMin() != null) etUmbralTempMin.setText(String.valueOf(d.getUmbralTempMin()));
                    if (d.getUmbralTempMax() != null) etUmbralTempMax.setText(String.valueOf(d.getUmbralTempMax()));
                    if (d.getUmbralHumedadAmbienteMin() != null) etUmbralHumAmbMin.setText(String.valueOf(d.getUmbralHumedadAmbienteMin()));
                    if (d.getUmbralHumedadAmbienteMax() != null) etUmbralHumAmbMax.setText(String.valueOf(d.getUmbralHumedadAmbienteMax()));
                    if (d.getUmbralHumedadSueloMin() != null) etUmbralHumSueloMin.setText(String.valueOf(d.getUmbralHumedadSueloMin()));
                    if (d.getUmbralCO2Max() != null) etUmbralCO2Max.setText(String.valueOf(d.getUmbralCO2Max()));

                    // Seleccionar el centro del dispositivo
                    if (d.getCentroEducativo() != null) {
                        for (int i = 0; i < listaCentros.size(); i++) {
                            if (listaCentros.get(i).getId().equals(d.getCentroEducativo().getId())) {
                                spinnerCentro.setSelection(i);
                                break;
                            }
                        }
                    }
                } else {
                    Toast.makeText(FormularioDispositivoActivity.this, "Error al cargar dispositivo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DispositivoEsp32> call, Throwable t) {
                Toast.makeText(FormularioDispositivoActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarDispositivo() {
        String mac = etMacAddress.getText().toString().trim().toUpperCase();
        String frecStr = etFrecuencia.getText().toString().trim();

        if (mac.isEmpty()) { etMacAddress.setError("La dirección MAC es obligatoria"); return; }
        if (!MAC_REGEX.matcher(mac).matches()) {
            etMacAddress.setError("Formato incorrecto. Usa XX:XX:XX:XX:XX:XX");
            return;
        }
        if (frecStr.isEmpty()) { etFrecuencia.setError("La frecuencia es obligatoria"); return; }

        int frecuencia;
        try {
            frecuencia = Integer.parseInt(frecStr);
            if (frecuencia < 5 || frecuencia > 3600) {
                etFrecuencia.setError("La frecuencia debe estar entre 5 y 3600");
                return;
            }
        } catch (NumberFormatException e) {
            etFrecuencia.setError("Frecuencia inválida");
            return;
        }

        CentroEducativo centroSeleccionado = (CentroEducativo) spinnerCentro.getSelectedItem();
        if (centroSeleccionado == null) {
            Toast.makeText(this, "Debes seleccionar un centro educativo", Toast.LENGTH_SHORT).show();
            return;
        }

        DispositivoEsp32 dispositivo = new DispositivoEsp32();
        dispositivo.setMacAddress(mac);
        dispositivo.setFrecuenciaLecturaSeg(frecuencia);
        dispositivo.setActivo(checkBoxActivo.isChecked());
        dispositivo.setCentroEducativo(centroSeleccionado);

        dispositivo.setUmbralTempMin(parsarDouble(etUmbralTempMin.getText().toString().trim()));
        dispositivo.setUmbralTempMax(parsarDouble(etUmbralTempMax.getText().toString().trim()));
        dispositivo.setUmbralHumedadAmbienteMin(parsarDouble(etUmbralHumAmbMin.getText().toString().trim()));
        dispositivo.setUmbralHumedadAmbienteMax(parsarDouble(etUmbralHumAmbMax.getText().toString().trim()));
        dispositivo.setUmbralHumedadSueloMin(parsarDouble(etUmbralHumSueloMin.getText().toString().trim()));
        dispositivo.setUmbralCO2Max(parsarDouble(etUmbralCO2Max.getText().toString().trim()));

        btnGuardar.setEnabled(false);

        if (esEdicion) {
            Call<DispositivoEsp32> call = RetrofitClient.getDispositivoApi().actualizarDispositivo(dispositivoId, dispositivo);
            call.enqueue(new Callback<DispositivoEsp32>() {
                @Override
                public void onResponse(Call<DispositivoEsp32> call, Response<DispositivoEsp32> response) {
                    btnGuardar.setEnabled(true);
                    if (response.isSuccessful()) {
                        Toast.makeText(FormularioDispositivoActivity.this, "Dispositivo actualizado", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        manejarErrorRespuesta(response.code());
                    }
                }

                @Override
                public void onFailure(Call<DispositivoEsp32> call, Throwable t) {
                    btnGuardar.setEnabled(true);
                    Toast.makeText(FormularioDispositivoActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Call<DispositivoEsp32> call = RetrofitClient.getDispositivoApi().crearDispositivo(dispositivo);
            call.enqueue(new Callback<DispositivoEsp32>() {
                @Override
                public void onResponse(Call<DispositivoEsp32> call, Response<DispositivoEsp32> response) {
                    btnGuardar.setEnabled(true);
                    if (response.isSuccessful()) {
                        Toast.makeText(FormularioDispositivoActivity.this, "Dispositivo registrado", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        manejarErrorRespuesta(response.code());
                    }
                }

                @Override
                public void onFailure(Call<DispositivoEsp32> call, Throwable t) {
                    btnGuardar.setEnabled(true);
                    Toast.makeText(FormularioDispositivoActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private Double parsarDouble(String valor) {
        if (valor == null || valor.isEmpty()) return null;
        try {
            return Double.parseDouble(valor);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void manejarErrorRespuesta(int code) {
        String msg;
        if (code == 409) msg = "Ya existe un dispositivo con esa dirección MAC";
        else if (code == 400) msg = "Datos inválidos";
        else if (code == 403) msg = "Sin permisos para esta operación";
        else msg = "Error al guardar el dispositivo (código " + code + ")";
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
