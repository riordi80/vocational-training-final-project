package com.example.proyectoarboles.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoarboles.R;
import com.example.proyectoarboles.activities.MainActivity;
import com.example.proyectoarboles.adapter.ArbolAdapter;
import com.example.proyectoarboles.api.RetrofitClient;
import com.example.proyectoarboles.model.Arbol;
import com.example.proyectoarboles.util.PermissionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListarArbolesFragment extends Fragment {

    private static final String TAG = "ListarArbolesFragment";
    private static final String ARG_CENTRO_ID = "centro_id";

    private RecyclerView recyclerViewArboles;
    private ArbolAdapter adapter;
    private List<Arbol> listaArboles = new ArrayList<>();
    private PermissionManager permissionManager;
    private long centroId = -1;

    public static ListarArbolesFragment newInstance(long centroId) {
        ListarArbolesFragment fragment = new ListarArbolesFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CENTRO_ID, centroId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            centroId = getArguments().getLong(ARG_CENTRO_ID, -1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_listar_arboles, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        permissionManager = new PermissionManager(requireContext());

        recyclerViewArboles = view.findViewById(R.id.RecyclerViewArboles);

        adapter = new ArbolAdapter(listaArboles, arbol ->
                ((MainActivity) requireActivity()).navigateToArbolDetalles(arbol.getId()),
                permissionManager);

        recyclerViewArboles.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewArboles.setAdapter(adapter);

        cargarArbolesDesdeAPI();
    }

    private void cargarArbolesDesdeAPI() {
        Call<List<Arbol>> call = centroId != -1
                ? RetrofitClient.getCentroEducativoApi().obtenerArbolesPorCentro(centroId)
                : RetrofitClient.getArbolApi().obtenerTodosLosArboles();

        call.enqueue(new Callback<List<Arbol>>() {
            @Override
            public void onResponse(Call<List<Arbol>> call, Response<List<Arbol>> response) {
                if (!isAdded()) return;
                if (response.isSuccessful() && response.body() != null) {
                    listaArboles.clear();
                    listaArboles.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e(TAG, "Error en respuesta - Código: " + response.code());
                    Toast.makeText(requireContext(), "Error al cargar árboles (código: " + response.code() + ")", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Arbol>> call, Throwable t) {
                if (!isAdded()) return;
                Log.e(TAG, "Error de conexión: " + t.getMessage());
                Toast.makeText(requireContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (permissionManager != null) {
            cargarArbolesDesdeAPI();
        }
    }
}
