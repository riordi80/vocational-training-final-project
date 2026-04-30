package com.example.proyectoarboles.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.proyectoarboles.R;
import com.example.proyectoarboles.api.RetrofitClient;
import com.example.proyectoarboles.api.UsuarioApi;
import com.example.proyectoarboles.model.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormularioUsuarioFragment extends Fragment {

    private static final String ARG_USUARIO_ID = "usuario_id";
    private static final String ARG_NOMBRE = "nombre";
    private static final String ARG_EMAIL = "email";
    private static final String ARG_ROL = "rol";
    private static final String ARG_ACTIVO = "activo";

    private static final String[] ROLES = {"ADMIN", "COORDINADOR"};

    private UsuarioApi usuarioApi;

    private Long usuarioId;
    private boolean esEdicion;

    private TextView tvTitulo;
    private EditText editNombre;
    private EditText editEmail;
    private EditText editPassword;
    private TextView labelPassword;
    private Spinner spinnerRol;
    private SwitchCompat switchActivo;
    private ImageButton btnVolver;
    private ImageButton btnEliminar;
    private Button btnGuardar;
    private Button btnCancelar;

    public static FormularioUsuarioFragment newInstance() {
        return new FormularioUsuarioFragment();
    }

    public static FormularioUsuarioFragment newInstance(Usuario usuario) {
        FormularioUsuarioFragment fragment = new FormularioUsuarioFragment();
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
        return inflater.inflate(R.layout.fragment_formulario_usuario, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usuarioApi = RetrofitClient.getUsuarioApi();
        usuarioId = getArguments() != null ? getArguments().getLong(ARG_USUARIO_ID, -1) : -1;
        esEdicion = usuarioId != -1;

        inicializarVistas(view);
        configurarSpinner();
        configurarModo();
        configurarListeners();
    }

    private void inicializarVistas(View view) {
        tvTitulo = view.findViewById(R.id.textViewTituloFormularioUsuario);
        editNombre = view.findViewById(R.id.editTextNombre);
        editEmail = view.findViewById(R.id.editTextEmail);
        editPassword = view.findViewById(R.id.editTextPassword);
        labelPassword = view.findViewById(R.id.labelPassword);
        spinnerRol = view.findViewById(R.id.spinnerRol);
        switchActivo = view.findViewById(R.id.switchActivo);
        btnVolver = view.findViewById(R.id.buttonVolverFormularioUsuario);
        btnEliminar = view.findViewById(R.id.buttonEliminarUsuario);
        btnGuardar = view.findViewById(R.id.buttonGuardarUsuario);
        btnCancelar = view.findViewById(R.id.buttonCancelarFormularioUsuario);
    }

    private void configurarSpinner() {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, ROLES);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRol.setAdapter(spinnerAdapter);
    }

    private void configurarModo() {
        if (esEdicion) {
            tvTitulo.setText("Editar Usuario");
            btnEliminar.setVisibility(View.VISIBLE);

            editNombre.setText(getArguments() != null ? getArguments().getString(ARG_NOMBRE, "") : "");
            editEmail.setText(getArguments() != null ? getArguments().getString(ARG_EMAIL, "") : "");
            editPassword.setVisibility(View.GONE);
            labelPassword.setVisibility(View.GONE);

            if (switchActivo != null) {
                switchActivo.setChecked(getArguments() != null && getArguments().getBoolean(ARG_ACTIVO, true));
            }

            String rol = getArguments() != null ? getArguments().getString(ARG_ROL, ROLES[0]) : ROLES[0];
            for (int index = 0; index < ROLES.length; index++) {
                if (ROLES[index].equals(rol)) {
                    spinnerRol.setSelection(index);
                    break;
                }
            }
        } else {
            tvTitulo.setText("Crear Usuario");
            btnEliminar.setVisibility(View.GONE);
            if (switchActivo != null) {
                switchActivo.setChecked(true);
            }
        }
    }

    private void configurarListeners() {
        btnVolver.setOnClickListener(v -> volverAtras());
        btnCancelar.setOnClickListener(v -> volverAtras());
        btnGuardar.setOnClickListener(v -> guardarUsuario());
        btnEliminar.setOnClickListener(v -> mostrarConfirmacionEliminar());
    }

    private void volverAtras() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void guardarUsuario() {
        String nombre = editNombre.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String rol = spinnerRol.getSelectedItem().toString();
        boolean activo = switchActivo != null && switchActivo.isChecked();

        if (nombre.isEmpty() || email.isEmpty()) {
            Toast.makeText(requireContext(), "Nombre y email son requeridos", Toast.LENGTH_SHORT).show();
            return;
        }

        btnGuardar.setEnabled(false);

        if (esEdicion) {
            actualizarUsuario(usuarioId, nombre, email, rol, activo);
        } else {
            String password = editPassword.getText().toString().trim();
            if (password.isEmpty()) {
                btnGuardar.setEnabled(true);
                Toast.makeText(requireContext(), "Todos los campos son requeridos", Toast.LENGTH_SHORT).show();
                return;
            }
            crearUsuario(nombre, email, password, rol, activo);
        }
    }

    private void crearUsuario(String nombre, String email, String password, String rol, boolean activo) {
        Usuario nuevoUsuario = new Usuario(nombre, email, rol);
        nuevoUsuario.setPasswordHash(password);
        nuevoUsuario.setActivo(activo);

        Call<Usuario> call = usuarioApi.crear(nuevoUsuario);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (!isAdded()) {
                    return;
                }
                btnGuardar.setEnabled(true);
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Usuario creado correctamente", Toast.LENGTH_SHORT).show();
                    volverAtras();
                } else {
                    Toast.makeText(requireContext(), "Error al crear usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                if (!isAdded()) {
                    return;
                }
                btnGuardar.setEnabled(true);
                Toast.makeText(requireContext(), "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarUsuario(Long id, String nombre, String email, String rol, boolean activo) {
        Usuario usuarioActualizado = new Usuario(nombre, email, rol);
        usuarioActualizado.setId(id);
        usuarioActualizado.setActivo(activo);

        Call<Usuario> call = usuarioApi.actualizar(id, usuarioActualizado);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (!isAdded()) {
                    return;
                }
                btnGuardar.setEnabled(true);
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show();
                    volverAtras();
                } else {
                    Toast.makeText(requireContext(), "Error al actualizar usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                if (!isAdded()) {
                    return;
                }
                btnGuardar.setEnabled(true);
                Toast.makeText(requireContext(), "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarConfirmacionEliminar() {
        String nombre = editNombre.getText().toString().trim();
        new AlertDialog.Builder(requireContext())
                .setTitle("Eliminar Usuario")
                .setMessage("¿Estas seguro de eliminar a " + nombre + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarUsuario())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarUsuario() {
        if (usuarioId == null || usuarioId == -1) {
            return;
        }

        Call<Void> call = usuarioApi.eliminar(usuarioId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!isAdded()) {
                    return;
                }
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Usuario eliminado correctamente", Toast.LENGTH_SHORT).show();
                    volverAtras();
                } else {
                    Toast.makeText(requireContext(), "Error al eliminar usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (!isAdded()) {
                    return;
                }
                Toast.makeText(requireContext(), "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
