package com.example.proyectoarboles.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class FormularioDispositivoFragment extends Fragment {

    private static final String TAG = "FormularioDispositivo";
    private static final Pattern MAC_REGEX = Pattern.compile("^([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}$");
    private static final String ARG_DISPOSITIVO_ID = "dispositivo_id";
    private static final String ARG_CENTRO_ID = "centro_id";

    private Long dispositivoId;
    private Long centroIdPreseleccionado;
    private boolean esEdicion;

    private TextView tvTitulo;
    private EditText etMacAddress;
    private EditText etFrecuencia;
    private EditText etUmbralTempMin;
    private EditText etUmbralTempMax;
    private EditText etUmbralHumAmbMin;
    private EditText etUmbralHumAmbMax;
    private EditText etUmbralHumSueloMin;
    private EditText etUmbralCO2Max;
    private CheckBox checkBoxActivo;
    private Spinner spinnerCentro;
    private ImageButton btnVolver;
    private Button btnCancelar;
    private Button btnGuardar;

    private List<CentroEducativo> listaCentros = new ArrayList<>();
    private ArrayAdapter<CentroEducativo> centrosAdapter;

    public static FormularioDispositivoFragment newInstance(long dispositivoId, long centroId) {
        FormularioDispositivoFragment fragment = new FormularioDispositivoFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_DISPOSITIVO_ID, dispositivoId);
        args.putLong(ARG_CENTRO_ID, centroId);
        fragment.setArguments(args);
        return fragment;
    }

    public static FormularioDispositivoFragment newInstance() {
        return new FormularioDispositivoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_formulario_dispositivo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dispositivoId = getArguments() != null ? getArguments().getLong(ARG_DISPOSITIVO_ID, -1) : -1;
        centroIdPreseleccionado = getArguments() != null ? getArguments().getLong(ARG_CENTRO_ID, -1) : -1;
        esEdicion = dispositivoId != -1;

        inicializarVistas(view);
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

    private void inicializarVistas(View view) {
        tvTitulo = view.findViewById(R.id.textViewTituloFormularioDispositivo);
        etMacAddress = view.findViewById(R.id.editTextMacAddress);
        etFrecuencia = view.findViewById(R.id.editTextFrecuencia);
        etFrecuencia.setText("30");
        etUmbralTempMin = view.findViewById(R.id.editTextUmbralTempMin);
        etUmbralTempMax = view.findViewById(R.id.editTextUmbralTempMax);
        etUmbralHumAmbMin = view.findViewById(R.id.editTextUmbralHumedadAmbienteMin);
        etUmbralHumAmbMax = view.findViewById(R.id.editTextUmbralHumedadAmbienteMax);
        etUmbralHumSueloMin = view.findViewById(R.id.editTextUmbralHumedadSueloMin);
        etUmbralCO2Max = view.findViewById(R.id.editTextUmbralCO2Max);
        checkBoxActivo = view.findViewById(R.id.checkBoxActivo);
        spinnerCentro = view.findViewById(R.id.spinnerCentroDispositivo);
        btnVolver = view.findViewById(R.id.buttonVolverFormularioDispositivo);
        btnCancelar = view.findViewById(R.id.buttonCancelarFormularioDispositivo);
        btnGuardar = view.findViewById(R.id.buttonGuardarDispositivo);
    }

    private void configurarSpinnerCentros() {
        centrosAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, listaCentros);
        centrosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCentro.setAdapter(centrosAdapter);
    }

    private void configurarListeners() {
        btnVolver.setOnClickListener(v -> volverAtras());
        btnCancelar.setOnClickListener(v -> volverAtras());
        btnGuardar.setOnClickListener(v -> guardarDispositivo());
    }

    private void volverAtras() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void cargarCentros() {
        Call<List<CentroEducativo>> call = RetrofitClient.getCentroEducativoApi().obtenerTodosLosCentros();
        call.enqueue(new Callback<List<CentroEducativo>>() {
            @Override
            public void onResponse(Call<List<CentroEducativo>> call, Response<List<CentroEducativo>> response) {
                if (!isAdded()) {
                    return;
                }
                if (response.isSuccessful() && response.body() != null) {
                    listaCentros.clear();
                    listaCentros.addAll(response.body());
                    centrosAdapter.notifyDataSetChanged();

                    if (centroIdPreseleccionado != -1) {
                        for (int index = 0; index < listaCentros.size(); index++) {
                            if (listaCentros.get(index).getId().equals(centroIdPreseleccionado)) {
                                spinnerCentro.setSelection(index);
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CentroEducativo>> call, Throwable t) {
                if (!isAdded()) {
                    return;
                }
                Log.e(TAG, "Error cargando centros: " + t.getMessage());
                Toast.makeText(requireContext(), "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarDatosDispositivo(Long id) {
        Call<List<CentroEducativo>> callCentros = RetrofitClient.getCentroEducativoApi().obtenerTodosLosCentros();
        callCentros.enqueue(new Callback<List<CentroEducativo>>() {
            @Override
            public void onResponse(Call<List<CentroEducativo>> call, Response<List<CentroEducativo>> response) {
                if (!isAdded()) {
                    return;
                }
                if (response.isSuccessful() && response.body() != null) {
                    listaCentros.clear();
                    listaCentros.addAll(response.body());
                    centrosAdapter.notifyDataSetChanged();
                    cargarDispositivo(id);
                }
            }

            @Override
            public void onFailure(Call<List<CentroEducativo>> call, Throwable t) {
                if (!isAdded()) {
                    return;
                }
                Log.e(TAG, "Error cargando centros: " + t.getMessage());
                Toast.makeText(requireContext(), "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarDispositivo(Long id) {
        Call<DispositivoEsp32> call = RetrofitClient.getDispositivoApi().obtenerDispositivoPorId(id);
        call.enqueue(new Callback<DispositivoEsp32>() {
            @Override
            public void onResponse(Call<DispositivoEsp32> call, Response<DispositivoEsp32> response) {
                if (!isAdded()) {
                    return;
                }
                if (response.isSuccessful() && response.body() != null) {
                    DispositivoEsp32 dispositivo = response.body();
                    etMacAddress.setText(dispositivo.getMacAddress());
                    etFrecuencia.setText(dispositivo.getFrecuenciaLecturaSeg() != null
                            ? String.valueOf(dispositivo.getFrecuenciaLecturaSeg()) : "30");
                    checkBoxActivo.setChecked(Boolean.TRUE.equals(dispositivo.getActivo()));

                    if (dispositivo.getUmbralTempMin() != null) {
                        etUmbralTempMin.setText(String.valueOf(dispositivo.getUmbralTempMin()));
                    }
                    if (dispositivo.getUmbralTempMax() != null) {
                        etUmbralTempMax.setText(String.valueOf(dispositivo.getUmbralTempMax()));
                    }
                    if (dispositivo.getUmbralHumedadAmbienteMin() != null) {
                        etUmbralHumAmbMin.setText(String.valueOf(dispositivo.getUmbralHumedadAmbienteMin()));
                    }
                    if (dispositivo.getUmbralHumedadAmbienteMax() != null) {
                        etUmbralHumAmbMax.setText(String.valueOf(dispositivo.getUmbralHumedadAmbienteMax()));
                    }
                    if (dispositivo.getUmbralHumedadSueloMin() != null) {
                        etUmbralHumSueloMin.setText(String.valueOf(dispositivo.getUmbralHumedadSueloMin()));
                    }
                    if (dispositivo.getUmbralCO2Max() != null) {
                        etUmbralCO2Max.setText(String.valueOf(dispositivo.getUmbralCO2Max()));
                    }

                    if (dispositivo.getCentroEducativo() != null) {
                        for (int index = 0; index < listaCentros.size(); index++) {
                            if (listaCentros.get(index).getId().equals(dispositivo.getCentroEducativo().getId())) {
                                spinnerCentro.setSelection(index);
                                break;
                            }
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Error al cargar dispositivo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DispositivoEsp32> call, Throwable t) {
                if (!isAdded()) {
                    return;
                }
                Toast.makeText(requireContext(), "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarDispositivo() {
        String mac = etMacAddress.getText().toString().trim().toUpperCase();
        String frecuenciaTexto = etFrecuencia.getText().toString().trim();

        if (mac.isEmpty()) {
            etMacAddress.setError("La direccion MAC es obligatoria");
            return;
        }
        if (!MAC_REGEX.matcher(mac).matches()) {
            etMacAddress.setError("Formato incorrecto. Usa XX:XX:XX:XX:XX:XX");
            return;
        }
        if (frecuenciaTexto.isEmpty()) {
            etFrecuencia.setError("La frecuencia es obligatoria");
            return;
        }

        int frecuencia;
        try {
            frecuencia = Integer.parseInt(frecuenciaTexto);
            if (frecuencia < 5 || frecuencia > 3600) {
                etFrecuencia.setError("La frecuencia debe estar entre 5 y 3600");
                return;
            }
        } catch (NumberFormatException e) {
            etFrecuencia.setError("Frecuencia invalida");
            return;
        }

        CentroEducativo centroSeleccionado = (CentroEducativo) spinnerCentro.getSelectedItem();
        if (centroSeleccionado == null) {
            Toast.makeText(requireContext(), "Debes seleccionar un centro educativo", Toast.LENGTH_SHORT).show();
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
            Call<DispositivoEsp32> call = RetrofitClient.getDispositivoApi()
                    .actualizarDispositivo(dispositivoId, dispositivo);
            call.enqueue(new Callback<DispositivoEsp32>() {
                @Override
                public void onResponse(Call<DispositivoEsp32> call, Response<DispositivoEsp32> response) {
                    if (!isAdded()) {
                        return;
                    }
                    btnGuardar.setEnabled(true);
                    if (response.isSuccessful()) {
                        Toast.makeText(requireContext(), "Dispositivo actualizado", Toast.LENGTH_SHORT).show();
                        volverAtras();
                    } else {
                        manejarErrorRespuesta(response.code());
                    }
                }

                @Override
                public void onFailure(Call<DispositivoEsp32> call, Throwable t) {
                    if (!isAdded()) {
                        return;
                    }
                    btnGuardar.setEnabled(true);
                    Toast.makeText(requireContext(), "Error de conexion", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Call<DispositivoEsp32> call = RetrofitClient.getDispositivoApi().crearDispositivo(dispositivo);
            call.enqueue(new Callback<DispositivoEsp32>() {
                @Override
                public void onResponse(Call<DispositivoEsp32> call, Response<DispositivoEsp32> response) {
                    if (!isAdded()) {
                        return;
                    }
                    btnGuardar.setEnabled(true);
                    if (response.isSuccessful()) {
                        Toast.makeText(requireContext(), "Dispositivo registrado", Toast.LENGTH_SHORT).show();
                        volverAtras();
                    } else {
                        manejarErrorRespuesta(response.code());
                    }
                }

                @Override
                public void onFailure(Call<DispositivoEsp32> call, Throwable t) {
                    if (!isAdded()) {
                        return;
                    }
                    btnGuardar.setEnabled(true);
                    Toast.makeText(requireContext(), "Error de conexion", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private Double parsarDouble(String valor) {
        if (valor == null || valor.isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(valor);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void manejarErrorRespuesta(int code) {
        String mensaje;
        if (code == 409) {
            mensaje = "Ya existe un dispositivo con esa direccion MAC";
        } else if (code == 400) {
            mensaje = "Datos invalidos";
        } else if (code == 403) {
            mensaje = "Sin permisos para esta operacion";
        } else {
            mensaje = "Error al guardar el dispositivo (codigo " + code + ")";
        }
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show();
    }
}
