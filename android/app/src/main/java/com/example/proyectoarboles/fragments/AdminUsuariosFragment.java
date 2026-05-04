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
import com.example.proyectoarboles.adapter.UsuarioAdapter;
import com.example.proyectoarboles.api.RetrofitClient;
import com.example.proyectoarboles.api.UsuarioApi;
import com.example.proyectoarboles.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminUsuariosFragment extends Fragment implements UsuarioAdapter.OnItemClickListener {

    private static final String TAG = "AdminUsuariosFragment";

    private RecyclerView recyclerViewUsuarios;
    private UsuarioAdapter adapter;
    private final List<Usuario> listaUsuarios = new ArrayList<>();
    private FloatingActionButton fabAnadirUsuario;
    private android.widget.Button btnAnadir;
    private UsuarioApi usuarioApi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_usuarios, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usuarioApi = RetrofitClient.getUsuarioApi();

        ((android.widget.ImageView) view.findViewById(R.id.imageViewHeaderIcon)).setImageResource(R.drawable.ic_nav_usuarios);
        ((TextView) view.findViewById(R.id.textViewHeaderTitle)).setText("Usuarios");
        ((TextView) view.findViewById(R.id.textViewHeaderSubtitle)).setText("Gestiona los usuarios del sistema");

        recyclerViewUsuarios = view.findViewById(R.id.recyclerViewUsuarios);
        fabAnadirUsuario = view.findViewById(R.id.fabAnadirUsuario);
        btnAnadir = view.findViewById(R.id.buttonHeaderAnadir);

        configurarRecyclerView();
        configurarFab();
        cargarUsuariosDesdeAPI();
    }

    private void configurarRecyclerView() {
        recyclerViewUsuarios.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new UsuarioAdapter(listaUsuarios, this);
        recyclerViewUsuarios.setAdapter(adapter);
    }

    private void configurarFab() {
        boolean landscape = getResources().getConfiguration().orientation
                == android.content.res.Configuration.ORIENTATION_LANDSCAPE;
        if (landscape) {
            fabAnadirUsuario.setVisibility(View.GONE);
            btnAnadir.setVisibility(View.VISIBLE);
            btnAnadir.setOnClickListener(v ->
                    ((MainActivity) requireActivity()).navigateToFormularioUsuario());
        } else {
            btnAnadir.setVisibility(View.GONE);
            fabAnadirUsuario.setVisibility(View.VISIBLE);
            fabAnadirUsuario.setOnClickListener(v ->
                    ((MainActivity) requireActivity()).navigateToFormularioUsuario());
        }
    }

    private void cargarUsuariosDesdeAPI() {
        Log.d(TAG, "Cargando usuarios desde API...");

        Call<List<Usuario>> call = usuarioApi.obtenerTodos();
        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (!isAdded()) {
                    return;
                }

                if (response.isSuccessful() && response.body() != null) {
                    listaUsuarios.clear();
                    listaUsuarios.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "Usuarios cargados: " + listaUsuarios.size());
                } else {
                    Log.e(TAG, "Error al cargar usuarios. Codigo: " + response.code());
                    Toast.makeText(requireContext(), "Error al cargar usuarios", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                if (!isAdded()) {
                    return;
                }
                Log.e(TAG, "Error de conexion: " + t.getMessage());
                Toast.makeText(requireContext(), "Error de conexion", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onEditarClick(Usuario usuario) {
        ((MainActivity) requireActivity()).navigateToDetalleUsuario(usuario);
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarUsuariosDesdeAPI();
    }
}
