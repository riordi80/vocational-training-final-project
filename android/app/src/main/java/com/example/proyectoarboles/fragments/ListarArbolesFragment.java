package com.example.proyectoarboles.fragments;

import android.content.Intent;
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
import com.example.proyectoarboles.activities.ArbolDetalles;
import com.example.proyectoarboles.activities.CrearArbol;
import com.example.proyectoarboles.activities.MainActivity;
import com.example.proyectoarboles.adapter.ArbolAdapter;
import com.example.proyectoarboles.api.RetrofitClient;
import com.example.proyectoarboles.model.Arbol;
import com.example.proyectoarboles.util.PermissionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    private Button btCerrarSesion;
    private FloatingActionButton fabAnadirArbol;
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
        btCerrarSesion = view.findViewById(R.id.btCerrarS);
        fabAnadirArbol = view.findViewById(R.id.fabAnadirArbol);

        adapter = new ArbolAdapter(listaArboles, arbol -> {
            Intent intent = new Intent(requireContext(), ArbolDetalles.class);
            intent.putExtra("arbol_id", arbol.getId());
            intent.putExtra("centro_id", centroId);
            startActivity(intent);
        }, permissionManager);

        recyclerViewArboles.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewArboles.setAdapter(adapter);

        cargarArbolesDesdeAPI();
        configurarListeners();
        actualizarVisibilidadBotones();
        configurarBotonesDinamicos();
    }

    private void cargarArbolesDesdeAPI() {
        Log.d(TAG, "Iniciando carga de árboles desde API...");

        Call<List<Arbol>> call;

        if (centroId != -1) {
            Log.d(TAG, "Cargando árboles para el centro ID: " + centroId);
            call = RetrofitClient.getCentroEducativoApi().obtenerArbolesPorCentro(centroId);
        } else {
            Log.d(TAG, "Cargando todos los árboles");
            call = RetrofitClient.getArbolApi().obtenerTodosLosArboles();
        }

        call.enqueue(new Callback<List<Arbol>>() {
            @Override
            public void onResponse(Call<List<Arbol>> call, Response<List<Arbol>> response) {
                if (!isAdded()) return;
                if (response.isSuccessful() && response.body() != null) {
                    listaArboles.clear();
                    listaArboles.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    if (listaArboles.isEmpty()) {
                        Log.d(TAG, "No hay árboles para este centro");
                    }
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

    private void configurarListeners() {
        btCerrarSesion.setOnClickListener(v -> {
            permissionManager.clearSession();
            actualizarVisibilidadBotones();
            configurarBotonesDinamicos();
            Toast.makeText(requireContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show();
        });
    }

    private void actualizarVisibilidadBotones() {
        boolean isLoggedIn = permissionManager.isLoggedIn();
        btCerrarSesion.setVisibility(isLoggedIn ? View.VISIBLE : View.GONE);
    }

    private void configurarBotonesDinamicos() {
        if (permissionManager.puedeCrearArbol(centroId)) {
            fabAnadirArbol.setVisibility(View.VISIBLE);
            Log.d(TAG, "Usuario puede crear árbol en este centro - FAB visible");
            fabAnadirArbol.setOnClickListener(v -> {
                Intent intent = new Intent(requireContext(), CrearArbol.class);
                intent.putExtra("centro_id", centroId);
                startActivity(intent);
            });
        } else {
            fabAnadirArbol.setVisibility(View.GONE);
            Log.d(TAG, "Usuario NO puede crear árbol en este centro - FAB oculto");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (permissionManager != null) {
            cargarArbolesDesdeAPI();
            actualizarVisibilidadBotones();
            configurarBotonesDinamicos();
        }
    }
}
