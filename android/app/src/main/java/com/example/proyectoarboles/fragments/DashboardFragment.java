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
import com.example.proyectoarboles.model.DispositivoEsp32;
import com.example.proyectoarboles.util.PermissionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    private TextView tvNumeroCentros, tvNumeroArboles, tvDispositivosActivos, tvCo2Total, tvNombreUsuario, tvRolUsuario;
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
        tvDispositivosActivos = view.findViewById(R.id.tvDispositivosActivos);
        tvCo2Total = view.findViewById(R.id.tvCo2Total);
        tvNombreUsuario = view.findViewById(R.id.tvNombreUsuario);
        tvRolUsuario = view.findViewById(R.id.tvRolUsuario);
        llEstadoUsuario = view.findViewById(R.id.llEstadoUsuario);
        btVerCentros = (MaterialCardView) view.findViewById(R.id.btVerCentros);

        configurarListeners();
        actualizarInfoUsuario();
        cargarNumeroCentros();
        cargarNumeroArboles();
        cargarDispositivosActivos();
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
                    int totalArboles = 0;
                    double totalCo2 = 0;
                    for (Arbol a : response.body()) {
                        int cantidad = a.getCantidad();
                        totalArboles += cantidad;
                        if (a.getAbsorcionCo2Anual() != null) {
                            totalCo2 += a.getAbsorcionCo2Anual() * cantidad;
                        }
                    }
                    tvNumeroArboles.setText(String.valueOf(totalArboles));
                    tvCo2Total.setText(String.format("%.1f", totalCo2));
                } else {
                    tvNumeroArboles.setText("—");
                    tvCo2Total.setText("—");
                }
            }

            @Override
            public void onFailure(Call<List<Arbol>> call, Throwable t) {
                if (!isAdded()) return;
                tvNumeroArboles.setText("—");
                tvCo2Total.setText("—");
            }
        });
    }

    private void cargarDispositivosActivos() {
        Call<List<DispositivoEsp32>> call = RetrofitClient.getDispositivoApi().obtenerTodosLosDispositivos();
        call.enqueue(new Callback<List<DispositivoEsp32>>() {
            @Override
            public void onResponse(Call<List<DispositivoEsp32>> call, Response<List<DispositivoEsp32>> response) {
                if (!isAdded()) return;
                if (response.isSuccessful() && response.body() != null) {
                    int activos = 0;
                    for (DispositivoEsp32 d : response.body()) {
                        if (Boolean.TRUE.equals(d.getActivo())) activos++;
                    }
                    tvDispositivosActivos.setText(String.valueOf(activos));
                } else {
                    tvDispositivosActivos.setText("—");
                }
            }

            @Override
            public void onFailure(Call<List<DispositivoEsp32>> call, Throwable t) {
                if (!isAdded()) return;
                tvDispositivosActivos.setText("—");
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
            cargarDispositivosActivos();
        }
    }
}
