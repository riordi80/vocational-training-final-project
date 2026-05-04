package com.example.proyectoarboles.fragments;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArbolDetallesFragment extends Fragment {

    private static final String TAG = "ArbolDetallesFragment";
    private static final String ARG_ARBOL_ID = "arbol_id";

    private TextView tvNombre, tvEspecie, tvFecha, tvUbicacion, tvCentroEducativo, tvAbsorcionCo2, tvCantidad;
    private TextInputEditText etNombre, etEspecie, etFecha, etUbicacion, etCantidad;
    private TextInputLayout tilNombre, tilEspecie, tilFecha, tilUbicacion, tilCantidad;
    private Spinner spinnerCentroEducativo;
    private LinearLayout llNombreView, llEspecieView, llFechaView, llCentroView, llUbicacionView, llCantidadView;
    private ImageButton btnEditar, btnEliminar, btnVolver;
    private Button btnGuardar, btnCancelar;

    private Arbol arbolActual;
    private long arbolId = -1;

    private SharedPreferences sharedPreferences;
    private PermissionManager permissionManager;
    private Rol userRole;
    private Set<String> userCentrosIds;

    private List<CentroEducativo> listaCentros = new ArrayList<>();
    private ArrayAdapter<CentroEducativo> centrosAdapter;

    public static ArbolDetallesFragment newInstance(long arbolId) {
        ArbolDetallesFragment fragment = new ArbolDetallesFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ARBOL_ID, arbolId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_arbol_detalles, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cargarDatosSesion();
        inicializarVistas(view);
        configurarListeners();

        if (getArguments() != null) {
            arbolId = getArguments().getLong(ARG_ARBOL_ID, -1);
        }

        if (arbolId != -1) {
            cargarDetallesDesdeAPI(arbolId);
        } else {
            Toast.makeText(requireContext(), "Error: ID de árbol no válido", Toast.LENGTH_LONG).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        }
    }

    private void cargarDatosSesion() {
        sharedPreferences = requireContext().getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE);
        permissionManager = new PermissionManager(requireContext());
        String roleString = sharedPreferences.getString("user_role", null);
        userRole = (roleString != null) ? Rol.valueOf(roleString) : null;
        userCentrosIds = sharedPreferences.getStringSet("user_centros", Collections.emptySet());
    }

    private void inicializarVistas(View view) {
        tvNombre = view.findViewById(R.id.textViewNombreDetalle);
        tvEspecie = view.findViewById(R.id.textViewEspecieDetalle);
        tvFecha = view.findViewById(R.id.textViewFechaDetalle);
        tvUbicacion = view.findViewById(R.id.textViewUbicacion);
        tvCentroEducativo = view.findViewById(R.id.textViewCentroEducativo);
        tvAbsorcionCo2 = view.findViewById(R.id.textViewAbsorcionCo2);

        etNombre = view.findViewById(R.id.editTextNombreDetalle);
        etEspecie = view.findViewById(R.id.editTextEspecieDetalle);
        etFecha = view.findViewById(R.id.editTextFechaDetalle);
        etUbicacion = view.findViewById(R.id.editTextUbicacion);
        etCantidad = view.findViewById(R.id.editTextCantidad);
        tilNombre = view.findViewById(R.id.tilNombreDetalle);
        tilEspecie = view.findViewById(R.id.tilEspecieDetalle);
        tilFecha = view.findViewById(R.id.tilFechaDetalle);
        tilUbicacion = view.findViewById(R.id.tilUbicacion);
        tilCantidad = view.findViewById(R.id.tilCantidad);

        spinnerCentroEducativo = view.findViewById(R.id.spinnerCentroEducativo);
        centrosAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, listaCentros);
        centrosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCentroEducativo.setAdapter(centrosAdapter);

        llNombreView = view.findViewById(R.id.llNombreView);
        llEspecieView = view.findViewById(R.id.llEspecieView);
        llFechaView = view.findViewById(R.id.llFechaView);
        llCentroView = view.findViewById(R.id.llCentroView);
        llUbicacionView = view.findViewById(R.id.llUbicacionView);
        llCantidadView = view.findViewById(R.id.llCantidadView);
        tvCantidad = view.findViewById(R.id.textViewCantidad);

        btnEditar = view.findViewById(R.id.buttonEditar);
        btnGuardar = view.findViewById(R.id.buttonGuardar);
        btnCancelar = view.findViewById(R.id.buttonCancelar);
        btnEliminar = view.findViewById(R.id.buttonEliminar);
        btnVolver = view.findViewById(R.id.buttonVolver);
    }

    private void configurarListeners() {
        btnVolver.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());
        btnEditar.setOnClickListener(v -> activarModoEdicion());
        btnGuardar.setOnClickListener(v -> guardarCambios());
        btnCancelar.setOnClickListener(v -> cancelarEdicion());
        btnEliminar.setOnClickListener(v -> mostrarDialogoEliminar());
    }

    private void cargarDetallesDesdeAPI(long id) {
        Call<Arbol> call = RetrofitClient.getArbolApi().obtenerArbolPorId(id);
        call.enqueue(new Callback<Arbol>() {
            @Override
            public void onResponse(Call<Arbol> call, Response<Arbol> response) {
                if (!isAdded()) return;
                if (response.isSuccessful() && response.body() != null) {
                    arbolActual = response.body();
                    mostrarDatosDelArbol(arbolActual);
                    verificarPermisosYActualizarUI();
                } else {
                    Toast.makeText(requireContext(), "Error al cargar detalles del árbol", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Arbol> call, Throwable t) {
                if (!isAdded()) return;
                Log.e(TAG, "Error de conexión: " + t.getMessage());
                Toast.makeText(requireContext(), "Error de conexión", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void mostrarDatosDelArbol(Arbol arbol) {
        tvNombre.setText(arbol.getNombre());
        tvEspecie.setText(arbol.getEspecie());
        tvFecha.setText(formatearFechaEspanol(arbol.getFechaPlantacion()));
        tvUbicacion.setText(arbol.getUbicacion() != null ? arbol.getUbicacion() : "No disponible");
        tvCentroEducativo.setText(arbol.getCentroEducativo() != null
                ? arbol.getCentroEducativo().getNombre() : "Sin centro asignado");
        tvCantidad.setText(String.valueOf(arbol.getCantidad()));
        if (arbol.getAbsorcionCo2Anual() != null) {
            tvAbsorcionCo2.setText(String.format(Locale.getDefault(), "%.2f kg/año", arbol.getAbsorcionCo2Anual()));
        } else {
            tvAbsorcionCo2.setText("No disponible");
        }
    }

    private void verificarPermisosYActualizarUI() {
        if (arbolActual == null || arbolActual.getCentroEducativo() == null) return;
        Long centroId = arbolActual.getCentroEducativo().getId();
        btnEditar.setVisibility(permissionManager.puedeEditarArbol(centroId) ? View.VISIBLE : View.GONE);
        btnEliminar.setVisibility(permissionManager.puedeEliminarArbol(centroId) ? View.VISIBLE : View.GONE);
    }

    private void activarModoEdicion() {
        cargarCentrosEnSpinner();
        mostrarEditTexts();
    }

    private void guardarCambios() {
        String nombre = etNombre.getText().toString().trim();
        String especie = etEspecie.getText().toString().trim();
        String fecha = etFecha.getText().toString().trim();
        String ubicacion = etUbicacion.getText().toString().trim();
        String cantidadStr = etCantidad.getText() != null ? etCantidad.getText().toString().trim() : "1";

        if (nombre.isEmpty()) { etNombre.setError("El nombre es requerido"); return; }
        if (especie.isEmpty()) { etEspecie.setError("La especie es requerida"); return; }
        if (fecha.isEmpty()) { etFecha.setError("La fecha es requerida"); return; }
        if (!validarFecha(fecha)) {
            etFecha.setError("Fecha inválida. Usa formato YYYY-MM-DD y debe ser en el pasado");
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadStr);
            if (cantidad < 1) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            etCantidad.setError("La cantidad debe ser al menos 1");
            etCantidad.requestFocus();
            return;
        }

        CentroEducativo centroSeleccionado = (CentroEducativo) spinnerCentroEducativo.getSelectedItem();
        if (centroSeleccionado == null) {
            Toast.makeText(requireContext(), "Debe seleccionar un centro", Toast.LENGTH_SHORT).show();
            return;
        }

        Arbol arbolActualizado = new Arbol();
        arbolActualizado.setId(arbolActual.getId());
        arbolActualizado.setNombre(nombre);
        arbolActualizado.setEspecie(especie);
        arbolActualizado.setFechaPlantacion(fecha);
        arbolActualizado.setUbicacion(ubicacion);
        arbolActualizado.setCentroEducativo(centroSeleccionado);
        arbolActualizado.setCantidad(cantidad);

        actualizarArbolEnAPI(arbolActual.getId(), arbolActualizado);
    }

    private void cancelarEdicion() {
        mostrarTextViews();
        verificarPermisosYActualizarUI();
    }

    private boolean validarFecha(String fechaStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            sdf.setLenient(false);
            Date fecha = sdf.parse(fechaStr);
            return !fecha.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private void mostrarTextViews() {
        llNombreView.setVisibility(View.VISIBLE);
        tilNombre.setVisibility(View.GONE);
        llEspecieView.setVisibility(View.VISIBLE);
        tilEspecie.setVisibility(View.GONE);
        llFechaView.setVisibility(View.VISIBLE);
        tilFecha.setVisibility(View.GONE);
        llUbicacionView.setVisibility(View.VISIBLE);
        tilUbicacion.setVisibility(View.GONE);
        llCentroView.setVisibility(View.VISIBLE);
        spinnerCentroEducativo.setVisibility(View.GONE);
        llCantidadView.setVisibility(View.VISIBLE);
        tilCantidad.setVisibility(View.GONE);
        btnGuardar.setVisibility(View.GONE);
        btnCancelar.setVisibility(View.GONE);
        verificarPermisosYActualizarUI();
    }

    private void mostrarEditTexts() {
        llNombreView.setVisibility(View.GONE);
        tilNombre.setVisibility(View.VISIBLE);
        etNombre.setText(tvNombre.getText());
        llEspecieView.setVisibility(View.GONE);
        tilEspecie.setVisibility(View.VISIBLE);
        etEspecie.setText(tvEspecie.getText());
        llFechaView.setVisibility(View.GONE);
        tilFecha.setVisibility(View.VISIBLE);
        if (arbolActual != null) etFecha.setText(arbolActual.getFechaPlantacion());
        llUbicacionView.setVisibility(View.GONE);
        tilUbicacion.setVisibility(View.VISIBLE);
        etUbicacion.setText(tvUbicacion.getText());
        llCentroView.setVisibility(View.GONE);
        spinnerCentroEducativo.setVisibility(View.VISIBLE);
        llCantidadView.setVisibility(View.GONE);
        tilCantidad.setVisibility(View.VISIBLE);
        if (arbolActual != null) etCantidad.setText(String.valueOf(arbolActual.getCantidad()));
        btnEditar.setVisibility(View.GONE);
        btnEliminar.setVisibility(View.GONE);
        btnGuardar.setVisibility(View.VISIBLE);
        btnCancelar.setVisibility(View.VISIBLE);
    }

    private void cargarCentrosEnSpinner() {
        Call<List<CentroEducativo>> call = RetrofitClient.getCentroEducativoApi().obtenerTodosLosCentros();
        call.enqueue(new Callback<List<CentroEducativo>>() {
            @Override
            public void onResponse(Call<List<CentroEducativo>> call, Response<List<CentroEducativo>> response) {
                if (!isAdded()) return;
                if (response.isSuccessful() && response.body() != null) {
                    listaCentros.clear();
                    listaCentros.addAll(response.body());
                    centrosAdapter.notifyDataSetChanged();
                    if (arbolActual != null && arbolActual.getCentroEducativo() != null) {
                        for (int i = 0; i < listaCentros.size(); i++) {
                            if (listaCentros.get(i).getId().equals(arbolActual.getCentroEducativo().getId())) {
                                spinnerCentroEducativo.setSelection(i);
                                break;
                            }
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Error al cargar centros", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CentroEducativo>> call, Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarArbolEnAPI(Long id, Arbol arbolActualizado) {
        Call<Arbol> call = RetrofitClient.getArbolApi().actualizarArbol(id, arbolActualizado);
        call.enqueue(new Callback<Arbol>() {
            @Override
            public void onResponse(Call<Arbol> call, Response<Arbol> response) {
                if (!isAdded()) return;
                if (response.isSuccessful() && response.body() != null) {
                    arbolActual = response.body();
                    mostrarDatosDelArbol(arbolActual);
                    Toast.makeText(requireContext(), "Árbol actualizado", Toast.LENGTH_SHORT).show();
                    mostrarTextViews();
                    verificarPermisosYActualizarUI();
                } else {
                    String msg = "Error al actualizar el árbol";
                    if (response.code() == 409) msg = "Ya existe un árbol con ese nombre en este centro";
                    else if (response.code() == 400) msg = "Datos inválidos. Verifica los campos.";
                    else if (response.code() == 403) msg = "No tienes permiso para editar este árbol";
                    else if (response.code() == 404) msg = "Árbol no encontrado";
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Arbol> call, Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), "Error de conexión", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void mostrarDialogoEliminar() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Eliminar árbol")
                .setMessage("¿Eliminar '" + tvNombre.getText().toString() + "'?")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarArbolAPI())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarArbolAPI() {
        Call<Arbol> call = RetrofitClient.getArbolApi().eliminarArbol(arbolId);
        call.enqueue(new Callback<Arbol>() {
            @Override
            public void onResponse(Call<Arbol> call, Response<Arbol> response) {
                if (!isAdded()) return;
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Árbol eliminado", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(requireContext(), "Error al eliminar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Arbol> call, Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String formatearFechaEspanol(String fechaISO) {
        if (fechaISO == null || fechaISO.isEmpty()) return "Fecha no disponible";
        try {
            SimpleDateFormat entrada = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = entrada.parse(fechaISO);
            SimpleDateFormat salida = new SimpleDateFormat("d MMM yyyy", new Locale("es", "ES"));
            return salida.format(date);
        } catch (ParseException e) {
            return fechaISO;
        }
    }
}
