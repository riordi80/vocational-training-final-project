package com.example.proyectoarboles.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.proyectoarboles.util.IslaUtils;

import java.math.BigDecimal;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormularioCentroFragment extends Fragment {

    private static final String TAG = "FormularioCentro";
    private static final String ARG_CENTRO_ID = "centro_id";


    private Long centroId;
    private boolean esEdicion;

    private TextView tvTitulo;
    private EditText etNombre;
    private EditText etDireccion;
    private EditText etResponsable;
    private EditText etPoblacion;
    private EditText etCodigoPostal;
    private EditText etTelefono;
    private EditText etEmail;
    private EditText etLatitud;
    private EditText etLongitud;
    private Spinner spinnerIsla;
    private ImageButton btnVolver;
    private Button btnCancelar;
    private Button btnGuardar;

    public static FormularioCentroFragment newInstance(long centroId) {
        FormularioCentroFragment fragment = new FormularioCentroFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CENTRO_ID, centroId);
        fragment.setArguments(args);
        return fragment;
    }

    public static FormularioCentroFragment newInstance() {
        return new FormularioCentroFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_formulario_centro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        centroId = getArguments() != null ? getArguments().getLong(ARG_CENTRO_ID, -1) : -1;
        esEdicion = centroId != -1;

        inicializarVistas(view);
        configurarSpinnerIsla();
        configurarListeners();

        if (esEdicion) {
            tvTitulo.setText("Editar Centro Educativo");
            cargarDatosCentro(centroId);
        } else {
            tvTitulo.setText("Nuevo Centro Educativo");
        }
    }

    private void inicializarVistas(View view) {
        tvTitulo = view.findViewById(R.id.textViewTituloFormularioCentro);
        etNombre = view.findViewById(R.id.editTextNombreCentro);
        etDireccion = view.findViewById(R.id.editTextDireccionCentro);
        etResponsable = view.findViewById(R.id.editTextResponsableCentro);
        etPoblacion = view.findViewById(R.id.editTextPoblacion);
        etCodigoPostal = view.findViewById(R.id.editTextCodigoPostal);
        etTelefono = view.findViewById(R.id.editTextTelefonoCentro);
        etEmail = view.findViewById(R.id.editTextEmailCentro);
        etLatitud = view.findViewById(R.id.editTextLatitudCentro);
        etLongitud = view.findViewById(R.id.editTextLongitudCentro);
        spinnerIsla = view.findViewById(R.id.spinnerIsla);
        btnVolver = view.findViewById(R.id.buttonVolverFormularioCentro);
        btnCancelar = view.findViewById(R.id.buttonCancelarFormularioCentro);
        btnGuardar = view.findViewById(R.id.buttonGuardarCentro);
    }

    private void configurarSpinnerIsla() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, IslaUtils.ETIQUETAS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIsla.setAdapter(adapter);
    }

    private void configurarListeners() {
        btnVolver.setOnClickListener(v -> volverAtras());
        btnCancelar.setOnClickListener(v -> volverAtras());
        btnGuardar.setOnClickListener(v -> guardarCentro());
    }

    private void volverAtras() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void cargarDatosCentro(Long id) {
        Call<CentroEducativo> call = RetrofitClient.getCentroEducativoApi().obtenerCentroPorId(id);
        call.enqueue(new Callback<CentroEducativo>() {
            @Override
            public void onResponse(Call<CentroEducativo> call, Response<CentroEducativo> response) {
                if (!isAdded()) {
                    return;
                }
                if (response.isSuccessful() && response.body() != null) {
                    CentroEducativo centro = response.body();
                    etNombre.setText(centro.getNombre() != null ? centro.getNombre() : "");
                    etDireccion.setText(centro.getDireccion() != null ? centro.getDireccion() : "");
                    etResponsable.setText(centro.getResponsable() != null ? centro.getResponsable() : "");
                    etPoblacion.setText(centro.getPoblacion() != null ? centro.getPoblacion() : "");
                    etCodigoPostal.setText(centro.getCodigoPostal() != null ? centro.getCodigoPostal() : "");
                    etTelefono.setText(centro.getTelefono() != null ? centro.getTelefono() : "");
                    etEmail.setText(centro.getEmail() != null ? centro.getEmail() : "");
                    etLatitud.setText(centro.getLatitud() != null ? centro.getLatitud().toPlainString() : "");
                    etLongitud.setText(centro.getLongitud() != null ? centro.getLongitud().toPlainString() : "");

                    if (centro.getIsla() != null) {
                        for (int index = 0; index < IslaUtils.VALORES.length; index++) {
                            if (IslaUtils.VALORES[index].equals(centro.getIsla())) {
                                spinnerIsla.setSelection(index);
                                break;
                            }
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Error al cargar el centro", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error cargando centro: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<CentroEducativo> call, Throwable t) {
                if (!isAdded()) {
                    return;
                }
                Log.e(TAG, "Error de conexion: " + t.getMessage());
                Toast.makeText(requireContext(), "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarCentro() {
        String nombre = etNombre.getText().toString().trim();
        String direccion = etDireccion.getText().toString().trim();
        String responsable = etResponsable.getText().toString().trim();
        String latitudStr = etLatitud.getText().toString().trim();
        String longitudStr = etLongitud.getText().toString().trim();

        if (nombre.isEmpty()) {
            etNombre.setError("El nombre es obligatorio");
            return;
        }
        if (direccion.isEmpty()) {
            etDireccion.setError("La direccion es obligatoria");
            return;
        }
        if (responsable.isEmpty()) {
            etResponsable.setError("El responsable es obligatorio");
            return;
        }
        if (latitudStr.isEmpty()) {
            etLatitud.setError("La latitud es obligatoria");
            return;
        }
        if (longitudStr.isEmpty()) {
            etLongitud.setError("La longitud es obligatoria");
            return;
        }

        BigDecimal latitud;
        BigDecimal longitud;
        try {
            latitud = new BigDecimal(latitudStr);
            if (latitud.compareTo(BigDecimal.valueOf(-90)) < 0 || latitud.compareTo(BigDecimal.valueOf(90)) > 0) {
                etLatitud.setError("La latitud debe estar entre -90 y 90");
                return;
            }
        } catch (NumberFormatException e) {
            etLatitud.setError("Latitud invalida");
            return;
        }

        try {
            longitud = new BigDecimal(longitudStr);
            if (longitud.compareTo(BigDecimal.valueOf(-180)) < 0 || longitud.compareTo(BigDecimal.valueOf(180)) > 0) {
                etLongitud.setError("La longitud debe estar entre -180 y 180");
                return;
            }
        } catch (NumberFormatException e) {
            etLongitud.setError("Longitud invalida");
            return;
        }

        CentroEducativo centro = new CentroEducativo();
        centro.setNombre(nombre);
        centro.setDireccion(direccion);
        centro.setResponsable(responsable);
        centro.setLatitud(latitud);
        centro.setLongitud(longitud);

        String poblacion = etPoblacion.getText().toString().trim();
        if (!poblacion.isEmpty()) {
            centro.setPoblacion(poblacion);
        }
        String codigoPostal = etCodigoPostal.getText().toString().trim();
        if (!codigoPostal.isEmpty()) {
            centro.setCodigoPostal(codigoPostal);
        }
        String telefono = etTelefono.getText().toString().trim();
        if (!telefono.isEmpty()) {
            centro.setTelefono(telefono);
        }
        String email = etEmail.getText().toString().trim();
        if (!email.isEmpty()) {
            centro.setEmail(email);
        }
        int islaPosicion = spinnerIsla.getSelectedItemPosition();
        if (islaPosicion > 0) {
            centro.setIsla(IslaUtils.VALORES[islaPosicion]);
        }

        btnGuardar.setEnabled(false);

        if (esEdicion) {
            Call<CentroEducativo> call = RetrofitClient.getCentroEducativoApi().actualizarCentro(centroId, centro);
            call.enqueue(new Callback<CentroEducativo>() {
                @Override
                public void onResponse(Call<CentroEducativo> call, Response<CentroEducativo> response) {
                    if (!isAdded()) {
                        return;
                    }
                    btnGuardar.setEnabled(true);
                    if (response.isSuccessful()) {
                        Toast.makeText(requireContext(), "Centro actualizado", Toast.LENGTH_SHORT).show();
                        volverAtras();
                    } else {
                        manejarErrorRespuesta(response.code());
                    }
                }

                @Override
                public void onFailure(Call<CentroEducativo> call, Throwable t) {
                    if (!isAdded()) {
                        return;
                    }
                    btnGuardar.setEnabled(true);
                    Toast.makeText(requireContext(), "Error de conexion", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Call<CentroEducativo> call = RetrofitClient.getCentroEducativoApi().crearCentro(centro);
            call.enqueue(new Callback<CentroEducativo>() {
                @Override
                public void onResponse(Call<CentroEducativo> call, Response<CentroEducativo> response) {
                    if (!isAdded()) {
                        return;
                    }
                    btnGuardar.setEnabled(true);
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(requireContext(), "Centro creado", Toast.LENGTH_SHORT).show();
                        volverAtras();
                    } else {
                        manejarErrorRespuesta(response.code());
                    }
                }

                @Override
                public void onFailure(Call<CentroEducativo> call, Throwable t) {
                    if (!isAdded()) {
                        return;
                    }
                    btnGuardar.setEnabled(true);
                    Toast.makeText(requireContext(), "Error de conexion", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void manejarErrorRespuesta(int code) {
        String mensaje;
        if (code == 409) {
            mensaje = "Ya existe un centro con ese nombre";
        } else if (code == 400) {
            mensaje = "Datos invalidos";
        } else if (code == 403) {
            mensaje = "Sin permisos para esta operacion";
        } else {
            mensaje = "Error al guardar el centro (codigo " + code + ")";
        }
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show();
    }
}
