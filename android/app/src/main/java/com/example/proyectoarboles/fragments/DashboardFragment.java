package com.example.proyectoarboles.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.google.android.material.card.MaterialCardView;
import android.widget.TextView;

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

    private TextView tvNumeroCentros, tvNumeroArboles, tvNombreUsuario, tvRolUsuario;
    private MaterialCardView btVerCentros;
    private LinearLayout llEstadoUsuario;
    private SharedPreferences sharedPreferences;
    private PermissionManager permissionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
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
        btVerCentros = (MaterialCardView) view.findViewById(R.id.btVerCentros);

        configurarListeners();
        actualizarInfoUsuario();
        cargarNumeroCentros();
        cargarNumeroArboles();
    }

    private void cargarNumeroCentros() {
        Call<List<CentroEducativo>> call = RetrofitClient.getCentroEducativoApi().obtenerTodosLosCentros();
        call.enqueue(new Callback<List<CentroEducativo>>() {
            @Override
            public void onResponse(Call<List<CentroEducativo>> call, Response<List<CentroEducativo>> response) {
                if (!isAdded()) return;
                if (response.isSuccessful() && response.body() != null) {
                    tvNumeroCentros.setText(String.valueOf(response.body().size()));
                } else {
                    tvNumeroCentros.setText("Error");
                }
            }

            @Override
            public void onFailure(Call<List<CentroEducativo>> call, Throwable t) {
                if (!isAdded()) return;
                tvNumeroCentros.setText("Error");
            }
        });
    }

    private void cargarNumeroArboles() {
        Call<List<Arbol>> call = RetrofitClient.getArbolApi().obtenerTodosLosArboles();
        call.enqueue(new Callback<List<Arbol>>() {
            @Override
            public void onResponse(Call<List<Arbol>> call, Response<List<Arbol>> response) {
                if (!isAdded()) return;
                if (response.isSuccessful() && response.body() != null) {
                    tvNumeroArboles.setText(String.valueOf(response.body().size()));
                } else {
                    tvNumeroArboles.setText("Error");
                }
            }

            @Override
            public void onFailure(Call<List<Arbol>> call, Throwable t) {
                if (!isAdded()) return;
                tvNumeroArboles.setText("Error");
            }
        });
    }

    private void configurarListeners() {
        btVerCentros.setOnClickListener(v ->
                ((MainActivity) requireActivity()).navigateToListarCentros());
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
            actualizarInfoUsuario();
            cargarNumeroCentros();
            cargarNumeroArboles();
        }
    }
}
