package com.example.proyectoarboles.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proyectoarboles.R;
import com.example.proyectoarboles.activities.MainActivity;
import com.example.proyectoarboles.api.AuthApi;
import com.example.proyectoarboles.api.RetrofitClient;
import com.example.proyectoarboles.dto.AuthResponse;
import com.example.proyectoarboles.dto.RegisterRequest;

import java.util.Set;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrerFragment extends Fragment {

    private static final String TAG = "RegistrerFragment";

    private TextInputEditText inputNombre;
    private TextInputEditText inputUsuario;
    private TextInputEditText inputPassword;
    private Button volverButton;
    private Button registrerButton;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registrer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);

        inputNombre = view.findViewById(R.id.editTextNombreRegistrer);
        inputUsuario = view.findViewById(R.id.editTextUsuarioRegistrer);
        inputPassword = view.findViewById(R.id.editTextPasswordRegistrer);
        volverButton = view.findViewById(R.id.buttonVolver);
        registrerButton = view.findViewById(R.id.buttonRegistrer);

        volverButton.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());

        registrerButton.setOnClickListener(v -> {
            String nombre = inputNombre.getText().toString().trim();
            String email = inputUsuario.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            if (nombre.isEmpty()) {
                inputNombre.setError("Este campo no puede quedar en blanco");
                return;
            }
            if (email.isEmpty()) {
                inputUsuario.setError("Este campo no puede quedar en blanco");
                return;
            }
            if (password.isEmpty()) {
                inputPassword.setError("Este campo no puede quedar en blanco");
                return;
            }

            realizarRegistro(nombre, email, password);
        });
    }

    private void realizarRegistro(String nombre, String email, String password) {
        AuthApi authApi = RetrofitClient.getAuthApi();
        RegisterRequest registerRequest = new RegisterRequest(nombre, email, password);

        Call<AuthResponse> call = authApi.register(registerRequest);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (!isAdded()) return;
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    Log.d(TAG, "Registro exitoso para: " + authResponse.getEmail());

                    guardarSesion(authResponse);
                    Toast.makeText(requireContext(), "Registro exitoso. Bienvenido " + authResponse.getNombre(), Toast.LENGTH_SHORT).show();

                    MainActivity activity = (MainActivity) requireActivity();
                    activity.actualizarMenuSegunPermisos();
                    activity.navigateToDashboard();
                } else {
                    String errorMsg = "Error en el registro";
                    if (response.code() == 409) {
                        errorMsg = "El email ya está registrado";
                    } else if (response.code() == 400) {
                        errorMsg = "Datos inválidos. Verifica los campos.";
                    }
                    Log.e(TAG, "Error en registro - Código: " + response.code());
                    Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                if (!isAdded()) return;
                Log.e(TAG, "Error de conexión: " + t.getMessage());
                Toast.makeText(requireContext(), "Error de conexión. Inténtalo de nuevo.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void guardarSesion(AuthResponse authResponse) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("is_logged_in", true);
        editor.putLong("user_id", authResponse.getId());
        editor.putString("user_name", authResponse.getNombre());
        editor.putString("user_email", authResponse.getEmail());
        editor.putString("token", authResponse.getToken());

        if (authResponse.getRol() != null) {
            editor.putString("user_role", authResponse.getRol());
        } else {
            editor.remove("user_role");
        }

        if (authResponse.getCentros() != null && !authResponse.getCentros().isEmpty()) {
            Set<String> centrosSet = authResponse.getCentros().stream()
                    .map(centro -> String.valueOf(centro.getId()))
                    .collect(Collectors.toSet());
            editor.putStringSet("user_centros", centrosSet);
        } else {
            editor.remove("user_centros");
        }

        editor.apply();
    }
}
