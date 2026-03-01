package com.example.proyectoarboles.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proyectoarboles.R;
import com.example.proyectoarboles.activities.MainActivity;
import com.example.proyectoarboles.activities.Registrer;
import com.example.proyectoarboles.api.AuthApi;
import com.example.proyectoarboles.api.RetrofitClient;
import com.example.proyectoarboles.dto.AuthResponse;
import com.example.proyectoarboles.dto.LoginRequest;

import java.util.Set;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";

    private EditText inputUsuario;
    private EditText inputPassword;
    private Button loginButton;
    private Button registrerButton;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);

        inputUsuario = view.findViewById(R.id.editTextUsuario);
        inputPassword = view.findViewById(R.id.editTextPassword);
        loginButton = view.findViewById(R.id.buttonLogin);
        registrerButton = view.findViewById(R.id.buttonRegistrer);

        loginButton.setOnClickListener(v -> {
            String email = inputUsuario.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Email y contraseña son requeridos", Toast.LENGTH_SHORT).show();
                return;
            }

            realizarLogin(email, password);
        });

        registrerButton.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), Registrer.class)));
    }

    private void realizarLogin(String email, String password) {
        AuthApi authApi = RetrofitClient.getAuthApi();
        LoginRequest loginRequest = new LoginRequest(email, password);

        Call<AuthResponse> call = authApi.login(loginRequest);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (!isAdded()) return;
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    Log.d(TAG, "Login exitoso para: " + authResponse.getEmail());

                    guardarSesion(authResponse);

                    Toast.makeText(requireContext(), "Bienvenido " + authResponse.getNombre(), Toast.LENGTH_SHORT).show();

                    // Actualizar menú y navegar al Dashboard
                    MainActivity activity = (MainActivity) requireActivity();
                    activity.actualizarMenuSegunPermisos();
                    activity.navigateToDashboard();

                } else {
                    Log.e(TAG, "Error en login - Código: " + response.code());
                    Toast.makeText(requireContext(), "Credenciales incorrectas", Toast.LENGTH_LONG).show();
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
