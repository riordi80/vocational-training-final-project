package com.example.proyectoarboles.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proyectoarboles.R;
import com.example.proyectoarboles.api.RetrofitClient;
import com.example.proyectoarboles.model.Arbol;
import com.example.proyectoarboles.model.CentroEducativo;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearArbolFragment extends Fragment {

    private static final String TAG = "CrearArbolFragment";
    private static final String ARG_CENTRO_ID = "centro_id";
    private static final String ARG_CENTRO_NOMBRE = "centro_nombre";

    private TextInputEditText inputNombre;
    private TextInputEditText inputEspecie;
    private TextInputEditText inputFechaPlantacion;
    private TextInputEditText inputUbicacion;
    private Button btnCrear;
    private ImageButton btnVolver;
    private ProgressBar progressBar;

    private long centroId = -1;
    private String centroNombre = "";

    public static CrearArbolFragment newInstance(long centroId, String centroNombre) {
        CrearArbolFragment fragment = new CrearArbolFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CENTRO_ID, centroId);
        args.putString(ARG_CENTRO_NOMBRE, centroNombre != null ? centroNombre : "");
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_crear_arbol, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        centroId = getArguments() != null ? getArguments().getLong(ARG_CENTRO_ID, -1) : -1;
        centroNombre = getArguments() != null ? getArguments().getString(ARG_CENTRO_NOMBRE, "") : "";

        inputNombre = view.findViewById(R.id.inputNombreArbol);
        inputEspecie = view.findViewById(R.id.inputEspecieArbol);
        inputFechaPlantacion = view.findViewById(R.id.inputFechaPlantacion);
        inputUbicacion = view.findViewById(R.id.inputUbicacion);
        btnCrear = view.findViewById(R.id.btnCrearArbol);
        btnVolver = view.findViewById(R.id.buttonVolverCrearArbol);
        progressBar = view.findViewById(R.id.progressBarCrear);

        if (centroId == -1) {
            Toast.makeText(requireContext(), "Error: Centro no especificado", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
            return;
        }

        btnCrear.setOnClickListener(v -> validarYCrearArbol());
        btnVolver.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());
    }

    private void validarYCrearArbol() {
        String nombre = inputNombre.getText() != null ? inputNombre.getText().toString().trim() : "";
        String especie = inputEspecie.getText() != null ? inputEspecie.getText().toString().trim() : "";
        String fechaStr = inputFechaPlantacion.getText() != null ? inputFechaPlantacion.getText().toString().trim() : "";
        String ubicacion = inputUbicacion.getText() != null ? inputUbicacion.getText().toString().trim() : "";

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

        if (!validarFecha(fechaStr)) {
            inputFechaPlantacion.setError("Fecha inválida. Usa formato YYYY-MM-DD y debe ser en el pasado");
            inputFechaPlantacion.requestFocus();
            return;
        }

        crearArbol(nombre, especie, fechaStr, ubicacion);
    }

    private boolean validarFecha(String fechaStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            sdf.setLenient(false);
            Date fecha = sdf.parse(fechaStr);
            return !fecha.after(new Date());
        } catch (Exception e) {
            Log.e(TAG, "Error al validar fecha: " + e.getMessage());
            return false;
        }
    }

    private void crearArbol(String nombre, String especie, String fechaPlantacion, String ubicacion) {
        progressBar.setVisibility(View.VISIBLE);
        btnCrear.setEnabled(false);

        CentroEducativo centro = new CentroEducativo();
        centro.setId(centroId);
        centro.setNombre(centroNombre);

        Arbol nuevoArbol = new Arbol();
        nuevoArbol.setNombre(nombre);
        nuevoArbol.setEspecie(especie);
        nuevoArbol.setFechaPlantacion(fechaPlantacion);
        nuevoArbol.setUbicacion(ubicacion);
        nuevoArbol.setCentroEducativo(centro);

        Call<Arbol> call = RetrofitClient.getArbolApi().crearArbol(nuevoArbol);
        call.enqueue(new Callback<Arbol>() {
            @Override
            public void onResponse(Call<Arbol> call, Response<Arbol> response) {
                if (!isAdded()) return;
                progressBar.setVisibility(View.GONE);
                btnCrear.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    Arbol arbolCreado = response.body();
                    Log.d(TAG, "Árbol creado exitosamente: " + arbolCreado.getNombre());
                    Toast.makeText(requireContext(), "Árbol creado: " + arbolCreado.getNombre(), Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
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
                    Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Arbol> call, Throwable t) {
                if (!isAdded()) return;
                progressBar.setVisibility(View.GONE);
                btnCrear.setEnabled(true);
                Log.e(TAG, "Error de conexión: " + t.getMessage());
                Toast.makeText(requireContext(), "Error de conexión. Inténtalo de nuevo.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
