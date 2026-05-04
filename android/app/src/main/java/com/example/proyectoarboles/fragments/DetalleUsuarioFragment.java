package com.example.proyectoarboles.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.proyectoarboles.R;
import com.example.proyectoarboles.activities.MainActivity;
import com.example.proyectoarboles.api.RetrofitClient;
import com.example.proyectoarboles.model.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleUsuarioFragment extends Fragment {

    private static final String ARG_USUARIO_ID = "usuario_id";
    private static final String ARG_NOMBRE = "nombre";
    private static final String ARG_EMAIL = "email";
    private static final String ARG_ROL = "rol";
    private static final String ARG_ACTIVO = "activo";

    private long usuarioId = -1;

    private TextView tvNombre, tvEmail, tvRol, tvActivo;
    private ImageButton btnVolver, btnEditar, btnEliminar;

    public static DetalleUsuarioFragment newInstance(Usuario usuario) {
        DetalleUsuarioFragment fragment = new DetalleUsuarioFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_USUARIO_ID, usuario.getId() != null ? usuario.getId() : -1);
        args.putString(ARG_NOMBRE, usuario.getNombre());
        args.putString(ARG_EMAIL, usuario.getEmail());
        args.putString(ARG_ROL, usuario.getRol());
        args.putBoolean(ARG_ACTIVO, Boolean.TRUE.equals(usuario.getActivo()));
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detalle_usuario, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usuarioId = getArguments() != null ? getArguments().getLong(ARG_USUARIO_ID, -1) : -1;

        tvNombre   = view.findViewById(R.id.textViewNombreDetalleUsuario);
        tvEmail    = view.findViewById(R.id.textViewEmailDetalleUsuario);
        tvRol      = view.findViewById(R.id.textViewRolDetalleUsuario);
        tvActivo   = view.findViewById(R.id.textViewActivoDetalleUsuario);
        btnVolver  = view.findViewById(R.id.buttonVolverDetalleUsuario);
        btnEditar  = view.findViewById(R.id.buttonEditarDetalleUsuario);
        btnEliminar = view.findViewById(R.id.buttonEliminarDetalleUsuario);

        mostrarDatos();
        configurarListeners();
    }

    private void mostrarDatos() {
        if (getArguments() == null) return;

        tvNombre.setText(getArguments().getString(ARG_NOMBRE, "-"));
        tvEmail.setText(getArguments().getString(ARG_EMAIL, "-"));

        String rol = getArguments().getString(ARG_ROL, "-");
        tvRol.setText(rol);
        int colorRol;
        if ("ADMIN".equals(rol)) {
            colorRol = ContextCompat.getColor(requireContext(), R.color.accent);
        } else if ("COORDINADOR".equals(rol)) {
            colorRol = ContextCompat.getColor(requireContext(), R.color.green_secondary);
        } else {
            colorRol = ContextCompat.getColor(requireContext(), R.color.gray_600);
        }
        tvRol.setTextColor(colorRol);

        boolean activo = getArguments().getBoolean(ARG_ACTIVO, true);
        tvActivo.setText(activo ? "Activo" : "Inactivo");
        tvActivo.setTextColor(ContextCompat.getColor(requireContext(),
                activo ? R.color.green_secondary : R.color.red_primary));
    }

    private void configurarListeners() {
        btnVolver.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());

        btnEditar.setOnClickListener(v -> {
            Usuario usuario = new Usuario();
            usuario.setId(usuarioId);
            if (getArguments() != null) {
                usuario.setNombre(getArguments().getString(ARG_NOMBRE));
                usuario.setEmail(getArguments().getString(ARG_EMAIL));
                usuario.setRol(getArguments().getString(ARG_ROL));
                usuario.setActivo(getArguments().getBoolean(ARG_ACTIVO, true));
            }
            ((MainActivity) requireActivity()).navigateToFormularioUsuario(usuario);
        });

        btnEliminar.setOnClickListener(v -> mostrarConfirmacionEliminar());
    }

    private void mostrarConfirmacionEliminar() {
        String nombre = getArguments() != null ? getArguments().getString(ARG_NOMBRE, "") : "";
        new AlertDialog.Builder(requireContext())
                .setTitle("Eliminar Usuario")
                .setMessage("¿Estás seguro de eliminar a " + nombre + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarUsuario())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarUsuario() {
        if (usuarioId == -1) return;
        Call<Void> call = RetrofitClient.getUsuarioApi().eliminar(usuarioId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!isAdded()) return;
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Usuario eliminado correctamente", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(requireContext(), "Error al eliminar usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
