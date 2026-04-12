package com.example.proyectoarboles.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoarboles.R;
import com.example.proyectoarboles.api.RetrofitClient;
import com.example.proyectoarboles.model.CentroEducativo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormularioCentroActivity extends AppCompatActivity {

    private static final String TAG = "FormularioCentro";

    private static final String[] ISLAS = {
            "-- Seleccionar isla --",
            "Gran Canaria", "Tenerife", "Lanzarote", "Fuerteventura",
            "La Palma", "La Gomera", "El Hierro"
    };
    private static final String[] ISLAS_VALUES = {
            "", "GRAN_CANARIA", "TENERIFE", "LANZAROTE", "FUERTEVENTURA",
            "LA_PALMA", "LA_GOMERA", "EL_HIERRO"
    };

    private Long centroId;
    private boolean esEdicion;

    private TextView tvTitulo;
    private EditText etNombre, etDireccion, etResponsable, etPoblacion, etCodigoPostal;
    private EditText etTelefono, etEmail, etLatitud, etLongitud;
    private Spinner spinnerIsla;
    private Button btnCancelar, btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_centro);

        centroId = getIntent().getLongExtra("centro_id", -1);
        esEdicion = centroId != -1;

        inicializarVistas();
        configurarSpinnerIsla();
        configurarListeners();

        if (esEdicion) {
            tvTitulo.setText("Editar Centro Educativo");
            cargarDatosCentro(centroId);
        } else {
            tvTitulo.setText("Nuevo Centro Educativo");
        }
    }

    private void inicializarVistas() {
        tvTitulo = findViewById(R.id.textViewTituloFormularioCentro);
        etNombre = findViewById(R.id.editTextNombreCentro);
        etDireccion = findViewById(R.id.editTextDireccionCentro);
        etResponsable = findViewById(R.id.editTextResponsableCentro);
        etPoblacion = findViewById(R.id.editTextPoblacion);
        etCodigoPostal = findViewById(R.id.editTextCodigoPostal);
        etTelefono = findViewById(R.id.editTextTelefonoCentro);
        etEmail = findViewById(R.id.editTextEmailCentro);
        etLatitud = findViewById(R.id.editTextLatitudCentro);
        etLongitud = findViewById(R.id.editTextLongitudCentro);
        spinnerIsla = findViewById(R.id.spinnerIsla);
        btnCancelar = findViewById(R.id.buttonCancelarFormularioCentro);
        btnGuardar = findViewById(R.id.buttonGuardarCentro);
    }

    private void configurarSpinnerIsla() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, ISLAS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIsla.setAdapter(adapter);
    }

    private void configurarListeners() {
        btnCancelar.setOnClickListener(v -> finish());
        btnGuardar.setOnClickListener(v -> guardarCentro());
    }

    private void cargarDatosCentro(Long id) {
        Call<CentroEducativo> call = RetrofitClient.getCentroEducativoApi().obtenerCentroPorId(id);
        call.enqueue(new Callback<CentroEducativo>() {
            @Override
            public void onResponse(Call<CentroEducativo> call, Response<CentroEducativo> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CentroEducativo c = response.body();
                    etNombre.setText(c.getNombre() != null ? c.getNombre() : "");
                    etDireccion.setText(c.getDireccion() != null ? c.getDireccion() : "");
                    etResponsable.setText(c.getResponsable() != null ? c.getResponsable() : "");
                    etPoblacion.setText(c.getPoblacion() != null ? c.getPoblacion() : "");
                    etCodigoPostal.setText(c.getCodigoPostal() != null ? c.getCodigoPostal() : "");
                    etTelefono.setText(c.getTelefono() != null ? c.getTelefono() : "");
                    etEmail.setText(c.getEmail() != null ? c.getEmail() : "");
                    etLatitud.setText(c.getLatitud() != null ? c.getLatitud().toPlainString() : "");
                    etLongitud.setText(c.getLongitud() != null ? c.getLongitud().toPlainString() : "");

                    // Seleccionar isla en el spinner
                    if (c.getIsla() != null) {
                        for (int i = 0; i < ISLAS_VALUES.length; i++) {
                            if (ISLAS_VALUES[i].equals(c.getIsla())) {
                                spinnerIsla.setSelection(i);
                                break;
                            }
                        }
                    }
                } else {
                    Toast.makeText(FormularioCentroActivity.this, "Error al cargar el centro", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error cargando centro: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<CentroEducativo> call, Throwable t) {
                Log.e(TAG, "Error de conexión: " + t.getMessage());
                Toast.makeText(FormularioCentroActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarCentro() {
        String nombre = etNombre.getText().toString().trim();
        String direccion = etDireccion.getText().toString().trim();
        String responsable = etResponsable.getText().toString().trim();
        String latitudStr = etLatitud.getText().toString().trim();
        String longitudStr = etLongitud.getText().toString().trim();

        // Validaciones básicas
        if (nombre.isEmpty()) { etNombre.setError("El nombre es obligatorio"); return; }
        if (direccion.isEmpty()) { etDireccion.setError("La dirección es obligatoria"); return; }
        if (responsable.isEmpty()) { etResponsable.setError("El responsable es obligatorio"); return; }
        if (latitudStr.isEmpty()) { etLatitud.setError("La latitud es obligatoria"); return; }
        if (longitudStr.isEmpty()) { etLongitud.setError("La longitud es obligatoria"); return; }

        BigDecimal latitud, longitud;
        try {
            latitud = new BigDecimal(latitudStr);
            if (latitud.compareTo(BigDecimal.valueOf(-90)) < 0 || latitud.compareTo(BigDecimal.valueOf(90)) > 0) {
                etLatitud.setError("La latitud debe estar entre -90 y 90");
                return;
            }
        } catch (NumberFormatException e) {
            etLatitud.setError("Latitud inválida");
            return;
        }

        try {
            longitud = new BigDecimal(longitudStr);
            if (longitud.compareTo(BigDecimal.valueOf(-180)) < 0 || longitud.compareTo(BigDecimal.valueOf(180)) > 0) {
                etLongitud.setError("La longitud debe estar entre -180 y 180");
                return;
            }
        } catch (NumberFormatException e) {
            etLongitud.setError("Longitud inválida");
            return;
        }

        CentroEducativo centro = new CentroEducativo();
        centro.setNombre(nombre);
        centro.setDireccion(direccion);
        centro.setResponsable(responsable);
        centro.setLatitud(latitud);
        centro.setLongitud(longitud);

        String poblacion = etPoblacion.getText().toString().trim();
        if (!poblacion.isEmpty()) centro.setPoblacion(poblacion);

        String cp = etCodigoPostal.getText().toString().trim();
        if (!cp.isEmpty()) centro.setCodigoPostal(cp);

        String telefono = etTelefono.getText().toString().trim();
        if (!telefono.isEmpty()) centro.setTelefono(telefono);

        String email = etEmail.getText().toString().trim();
        if (!email.isEmpty()) centro.setEmail(email);

        int islaPos = spinnerIsla.getSelectedItemPosition();
        if (islaPos > 0) centro.setIsla(ISLAS_VALUES[islaPos]);

        btnGuardar.setEnabled(false);

        if (esEdicion) {
            Call<CentroEducativo> call = RetrofitClient.getCentroEducativoApi().actualizarCentro(centroId, centro);
            call.enqueue(new Callback<CentroEducativo>() {
                @Override
                public void onResponse(Call<CentroEducativo> call, Response<CentroEducativo> response) {
                    btnGuardar.setEnabled(true);
                    if (response.isSuccessful()) {
                        Toast.makeText(FormularioCentroActivity.this, "Centro actualizado", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        manejarErrorRespuesta(response.code());
                    }
                }

                @Override
                public void onFailure(Call<CentroEducativo> call, Throwable t) {
                    btnGuardar.setEnabled(true);
                    Toast.makeText(FormularioCentroActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Call<CentroEducativo> call = RetrofitClient.getCentroEducativoApi().crearCentro(centro);
            call.enqueue(new Callback<CentroEducativo>() {
                @Override
                public void onResponse(Call<CentroEducativo> call, Response<CentroEducativo> response) {
                    btnGuardar.setEnabled(true);
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(FormularioCentroActivity.this, "Centro creado", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        manejarErrorRespuesta(response.code());
                    }
                }

                @Override
                public void onFailure(Call<CentroEducativo> call, Throwable t) {
                    btnGuardar.setEnabled(true);
                    Toast.makeText(FormularioCentroActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void manejarErrorRespuesta(int code) {
        String msg;
        if (code == 409) msg = "Ya existe un centro con ese nombre";
        else if (code == 400) msg = "Datos inválidos";
        else if (code == 403) msg = "Sin permisos para esta operación";
        else msg = "Error al guardar el centro (código " + code + ")";
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
