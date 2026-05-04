package com.example.proyectoarboles.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoarboles.R;
import com.example.proyectoarboles.activities.MainActivity;
import com.example.proyectoarboles.adapter.CentroEducativoAdapter;
import com.example.proyectoarboles.api.RetrofitClient;
import com.example.proyectoarboles.model.CentroEducativo;
import com.example.proyectoarboles.util.PermissionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListarCentrosFragment extends Fragment implements CentroEducativoAdapter.OnItemClickListener {

    private static final String TAG = "ListarCentrosFragment";

    private RecyclerView recyclerViewCentros;
    private CentroEducativoAdapter adapter;
    private List<CentroEducativo> listaCentros = new ArrayList<>();
    private FloatingActionButton fabAnadirCentro;
    private android.widget.Button btnAnadir;
    private PermissionManager permissionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_listar_centros, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        permissionManager = new PermissionManager(requireContext());

        ((android.widget.ImageView) view.findViewById(R.id.imageViewHeaderIcon)).setImageResource(R.drawable.ic_school);
        ((TextView) view.findViewById(R.id.textViewHeaderTitle)).setText("Centros Educativos");
        ((TextView) view.findViewById(R.id.textViewHeaderSubtitle)).setText("Listado de centros educativos participantes en el proyecto");

        recyclerViewCentros = view.findViewById(R.id.RecyclerViewCentros);
        fabAnadirCentro = view.findViewById(R.id.fabAnadirCentro);
        btnAnadir = view.findViewById(R.id.buttonHeaderAnadir);

        configurarRecyclerView();
        cargarCentrosDesdeAPI();
        configurarFab();
    }

    private void configurarRecyclerView() {
        recyclerViewCentros.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new CentroEducativoAdapter(listaCentros, this);
        recyclerViewCentros.setAdapter(adapter);
    }

    @Override
    public void onVerDetalleClick(CentroEducativo centro) {
        ((MainActivity) requireActivity()).navigateToDetalleCentro(centro.getId());
    }

    private void cargarCentrosDesdeAPI() {
        Log.d(TAG, "Iniciando carga de centros desde API...");
        Call<List<CentroEducativo>> call = RetrofitClient.getCentroEducativoApi().obtenerTodosLosCentros();

        call.enqueue(new Callback<List<CentroEducativo>>() {
            @Override
            public void onResponse(Call<List<CentroEducativo>> call, Response<List<CentroEducativo>> response) {
                if (!isAdded()) return;
                if (response.isSuccessful() && response.body() != null) {
                    listaCentros.clear();
                    listaCentros.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "Centros cargados: " + listaCentros.size());
                } else {
                    String errorBody = "";
                    try {
                        errorBody = response.errorBody().string();
                    } catch (Exception e) {
                        Log.e(TAG, "Error al leer el cuerpo del error", e);
                    }
                    Log.e(TAG, "Error al cargar centros. Código: " + response.code() + ", Cuerpo: " + errorBody);
                    Toast.makeText(requireContext(), "Error al cargar centros: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<CentroEducativo>> call, Throwable t) {
                if (!isAdded()) return;
                Log.e(TAG, "Error de conexión", t);
                Toast.makeText(requireContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void configurarFab() {
        if (!permissionManager.puedeCrearCentro()) {
            fabAnadirCentro.setVisibility(View.GONE);
            btnAnadir.setVisibility(View.GONE);
            return;
        }
        boolean landscape = getResources().getConfiguration().orientation
                == android.content.res.Configuration.ORIENTATION_LANDSCAPE;
        if (landscape) {
            fabAnadirCentro.setVisibility(View.GONE);
            btnAnadir.setVisibility(View.VISIBLE);
            btnAnadir.setOnClickListener(v ->
                    ((MainActivity) requireActivity()).navigateToFormularioCentro());
        } else {
            btnAnadir.setVisibility(View.GONE);
            fabAnadirCentro.setVisibility(View.VISIBLE);
            fabAnadirCentro.setOnClickListener(v ->
                    ((MainActivity) requireActivity()).navigateToFormularioCentro());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarCentrosDesdeAPI();
        if (permissionManager != null) configurarFab();
    }
}
