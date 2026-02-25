package com.example.proyectoarboles.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private Button btRegister, btCerrarSesion;
    private PermissionManager permissionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_listar_centros, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        permissionManager = new PermissionManager(requireContext());

        recyclerViewCentros = view.findViewById(R.id.RecyclerViewCentros);
        btRegister = view.findViewById(R.id.btRegister);
        btCerrarSesion = view.findViewById(R.id.btCerrarS);

        btRegister.setVisibility(View.GONE);

        configurarRecyclerView();
        cargarCentrosDesdeAPI();
        configurarListeners();
        actualizarVisibilidadBotones();
    }

    private void configurarRecyclerView() {
        recyclerViewCentros.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new CentroEducativoAdapter(listaCentros, this, permissionManager);
        recyclerViewCentros.setAdapter(adapter);
    }

    @Override
    public void onVerArbolesClick(CentroEducativo centro) {
        ((MainActivity) requireActivity()).navigateToListarArboles(centro.getId());
    }

    @Override
    public void onEditarCentroClick(CentroEducativo centro) {
        Toast.makeText(requireContext(), "Editar centro: " + centro.getNombre(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEliminarCentroClick(CentroEducativo centro) {
        Toast.makeText(requireContext(), "Eliminar centro: " + centro.getNombre(), Toast.LENGTH_SHORT).show();
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
                    if (listaCentros.isEmpty()) {
                        Log.d(TAG, "No hay centros registrados");
                    }
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

    private void configurarListeners() {
        btCerrarSesion.setOnClickListener(v -> {
            permissionManager.clearSession();
            actualizarVisibilidadBotones();
            Toast.makeText(requireContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show();
        });
    }

    private void actualizarVisibilidadBotones() {
        boolean isLoggedIn = permissionManager.isLoggedIn();
        btRegister.setVisibility(View.GONE);
        btCerrarSesion.setVisibility(isLoggedIn ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (permissionManager != null) {
            actualizarVisibilidadBotones();
            cargarCentrosDesdeAPI();
        }
    }
}
