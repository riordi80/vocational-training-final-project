package com.example.proyectoarboles.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proyectoarboles.R;
import com.example.proyectoarboles.activities.MainActivity;
import com.example.proyectoarboles.api.RetrofitClient;
import com.example.proyectoarboles.model.Arbol;
import com.example.proyectoarboles.model.CentroEducativo;
import com.example.proyectoarboles.util.PermissionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    private static final String TAG = "DashboardFragment";

    private TextView tvNumeroCentros, tvNumeroArboles, tvNombreUsuario, tvRolUsuario;
    private Button btVerCentros, btVerArboles, btRegister, btCerrarSesion;
    private LinearLayout llEstadoUsuario;
    private SharedPreferences sharedPreferences;
    private PermissionManager permissionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        permissionManager = new PermissionManager(requireContext());

        tvNumeroCentros = view.findViewById(R.id.tvNumeroCentros);
        tvNumeroArboles = view.findViewById(R.id.tvNumeroArboles);
        tvNombreUsuario = view.findViewById(R.id.tvNombreUsuario);
        tvRolUsuario = view.findViewById(R.id.tvRolUsuario);
        llEstadoUsuario = view.findViewById(R.id.llEstadoUsuario);

        btVerCentros = view.findViewById(R.id.btVerCentros);
        btVerArboles = view.findViewById(R.id.btVerArboles);
        btRegister = view.findViewById(R.id.btRegister);
        btCerrarSesion = view.findViewById(R.id.btCerrarSesion);

        configurarListeners();
        actualizarVisibilidadBotones();
        cargarEstadisticas();
    }

    private void cargarEstadisticas() {
        cargarNumeroCentros();
        cargarNumeroArboles();
    }

    private void cargarNumeroCentros() {
        Log.d(TAG, "Cargando número de centros...");
        Call<List<CentroEducativo>> call = RetrofitClient.getCentroEducativoApi().obtenerTodosLosCentros();

        call.enqueue(new Callback<List<CentroEducativo>>() {
            @Override
            public void onResponse(Call<List<CentroEducativo>> call, Response<List<CentroEducativo>> response) {
                if (!isAdded()) return;
                if (response.isSuccessful() && response.body() != null) {
                    tvNumeroCentros.setText(String.valueOf(response.body().size()));
                    Log.d(TAG, "Centros cargados: " + response.body().size());
                } else {
                    Log.e(TAG, "Error al cargar centros: " + response.code());
                    tvNumeroCentros.setText("Error");
                }
            }

            @Override
            public void onFailure(Call<List<CentroEducativo>> call, Throwable t) {
                if (!isAdded()) return;
                Log.e(TAG, "Error de conexión al cargar centros", t);
                tvNumeroCentros.setText("Error");
            }
        });
    }

    private void cargarNumeroArboles() {
        Log.d(TAG, "Cargando número de árboles...");
        Call<List<Arbol>> call = RetrofitClient.getArbolApi().obtenerTodosLosArboles();

        call.enqueue(new Callback<List<Arbol>>() {
            @Override
            public void onResponse(Call<List<Arbol>> call, Response<List<Arbol>> response) {
                if (!isAdded()) return;
                if (response.isSuccessful() && response.body() != null) {
                    tvNumeroArboles.setText(String.valueOf(response.body().size()));
                    Log.d(TAG, "Árboles cargados: " + response.body().size());
                } else {
                    Log.e(TAG, "Error al cargar árboles: " + response.code());
                    tvNumeroArboles.setText("Error");
                }
            }

            @Override
            public void onFailure(Call<List<Arbol>> call, Throwable t) {
                if (!isAdded()) return;
                Log.e(TAG, "Error de conexión al cargar árboles", t);
                tvNumeroArboles.setText("Error");
            }
        });
    }

    private void configurarListeners() {
        btVerCentros.setOnClickListener(v ->
                ((MainActivity) requireActivity()).navigateToListarCentros());

        btVerArboles.setOnClickListener(v ->
                ((MainActivity) requireActivity()).navigateToListarArboles(-1L));

        btCerrarSesion.setOnClickListener(v -> {
            permissionManager.clearSession();
            actualizarVisibilidadBotones();
            actualizarInfoUsuario();
            Toast.makeText(requireContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show();
        });
    }

    private void actualizarVisibilidadBotones() {
        boolean isLoggedIn = permissionManager.isLoggedIn();
        btRegister.setVisibility(View.GONE);
        btCerrarSesion.setVisibility(isLoggedIn ? View.VISIBLE : View.GONE);
        actualizarInfoUsuario();
    }

    private void actualizarInfoUsuario() {
        boolean isLoggedIn = permissionManager.isLoggedIn();
        if (isLoggedIn) {
            String nombreUsuario = sharedPreferences.getString("user_name", "");
            String rolUsuario = sharedPreferences.getString("user_role", "PUBLICO");
            tvNombreUsuario.setText(nombreUsuario);
            tvRolUsuario.setText(rolUsuario != null ? rolUsuario : "PUBLICO");
            llEstadoUsuario.setVisibility(View.VISIBLE);
        } else {
            llEstadoUsuario.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (permissionManager != null) {
            actualizarVisibilidadBotones();
            cargarEstadisticas();
        }
    }
}
