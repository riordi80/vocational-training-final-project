package com.example.proyectoarboles.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoarboles.R;
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
    private List<Usuario> listaUsuarios = new ArrayList<>();
    private FloatingActionButton fabAnadirUsuario;
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
        recyclerViewUsuarios = view.findViewById(R.id.recyclerViewUsuarios);
        fabAnadirUsuario = view.findViewById(R.id.fabAnadirUsuario);

        configurarRecyclerView();
        cargarUsuariosDesdeAPI();
        configurarFab();
    }

    private void configurarRecyclerView() {
        recyclerViewUsuarios.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new UsuarioAdapter(listaUsuarios, this);
        recyclerViewUsuarios.setAdapter(adapter);
    }

    private void cargarUsuariosDesdeAPI() {
        Log.d(TAG, "Cargando usuarios desde API...");
        
        Call<List<Usuario>> call = usuarioApi.obtenerTodos();

        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (!isAdded()) return;
                
                if (response.isSuccessful() && response.body() != null) {
                    listaUsuarios.clear();
                    listaUsuarios.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "Usuarios cargados: " + listaUsuarios.size());
                } else {
                    Log.e(TAG, "Error al cargar usuarios. Código: " + response.code());
                    Toast.makeText(requireContext(), "Error al cargar usuarios", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                if (!isAdded()) return;
                Log.e(TAG, "Error de conexión: " + t.getMessage());
                Toast.makeText(requireContext(), "Error de conexión", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void configurarFab() {
        fabAnadirUsuario.setOnClickListener(v -> mostrarDialogoCrearUsuario());
    }

    @Override
    public void onEditarClick(Usuario usuario) {
        mostrarDialogoEditarUsuario(usuario);
    }

    @Override
    public void onEliminarClick(Usuario usuario) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Eliminar Usuario")
                .setMessage("¿Estás seguro de eliminar a " + usuario.getNombre() + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarUsuario(usuario))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void mostrarDialogoCrearUsuario() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_usuario, null);

        EditText editNombre = dialogView.findViewById(R.id.editTextNombre);
        EditText editEmail = dialogView.findViewById(R.id.editTextEmail);
        EditText editPassword = dialogView.findViewById(R.id.editTextPassword);
        Spinner spinnerRol = dialogView.findViewById(R.id.spinnerRol);
        androidx.appcompat.widget.SwitchCompat switchActivo = dialogView.findViewById(R.id.switchActivo);

        // Configurar spinner de roles
        String[] roles = {"ADMIN", "COORDINADOR"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, roles);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRol.setAdapter(spinnerAdapter);

        // Ocultar campo password para edición
        editPassword.setVisibility(View.VISIBLE);

        // Por defecto, nuevos usuarios están activos
        if (switchActivo != null) {
            switchActivo.setChecked(true);
        }

        new AlertDialog.Builder(requireContext())
                .setTitle("Crear Usuario")
                .setView(dialogView)
                .setPositiveButton("Crear", (dialog, which) -> {
                    String nombre = editNombre.getText().toString().trim();
                    String email = editEmail.getText().toString().trim();
                    String password = editPassword.getText().toString().trim();
                    String rol = spinnerRol.getSelectedItem().toString();
                    boolean activo = true;
                    if (switchActivo != null) {
                        activo = switchActivo.isChecked();
                    }

                    if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(requireContext(), "Todos los campos son requeridos", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    crearUsuario(nombre, email, password, rol, activo);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void mostrarDialogoEditarUsuario(Usuario usuario) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_usuario, null);

        EditText editNombre = dialogView.findViewById(R.id.editTextNombre);
        EditText editEmail = dialogView.findViewById(R.id.editTextEmail);
        EditText editPassword = dialogView.findViewById(R.id.editTextPassword);
        Spinner spinnerRol = dialogView.findViewById(R.id.spinnerRol);
        View labelPassword = dialogView.findViewById(R.id.labelPassword);
        androidx.appcompat.widget.SwitchCompat switchActivo = dialogView.findViewById(R.id.switchActivo);

        // Preencher campos
        editNombre.setText(usuario.getNombre());
        editEmail.setText(usuario.getEmail());
        editPassword.setVisibility(View.GONE);
        if (labelPassword != null) {
            labelPassword.setVisibility(View.GONE);
        }
        if (switchActivo != null) {
            switchActivo.setChecked(usuario.getActivo());
        }

        // Configurar spinner de roles
        String[] roles = {"ADMIN", "COORDINADOR"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, roles);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRol.setAdapter(spinnerAdapter);

        // Seleccionar rol actual
        if (usuario.getRol() != null) {
            for (int i = 0; i < roles.length; i++) {
                if (roles[i].equals(usuario.getRol())) {
                    spinnerRol.setSelection(i);
                    break;
                }
            }
        }

        new AlertDialog.Builder(requireContext())
                .setTitle("Editar Usuario")
                .setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nombre = editNombre.getText().toString().trim();
                    String email = editEmail.getText().toString().trim();
                    String rol = spinnerRol.getSelectedItem().toString();
                    boolean activo = false;

                    if (switchActivo != null) {
                        activo = switchActivo.isChecked();
                    } else {
                        activo = usuario.getActivo();
                    }

                    if (nombre.isEmpty() || email.isEmpty()) {
                        Toast.makeText(requireContext(), "Nombre y email son requeridos", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    actualizarUsuario(usuario.getId(), nombre, email, rol, activo);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void crearUsuario(String nombre, String email, String password, String rol, boolean activo) {
        Usuario nuevoUsuario = new Usuario(nombre, email, rol);
        nuevoUsuario.setPasswordHash(password);
        nuevoUsuario.setActivo(activo);

        Call<Usuario> call = usuarioApi.crear(nuevoUsuario);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (!isAdded()) return;
                
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Usuario creado correctamente", Toast.LENGTH_SHORT).show();
                    cargarUsuariosDesdeAPI();
                } else {
                    Toast.makeText(requireContext(), "Error al crear usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
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
                if (!isAdded()) return;
                
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show();
                    cargarUsuariosDesdeAPI();
                } else {
                    Toast.makeText(requireContext(), "Error al actualizar usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eliminarUsuario(Usuario usuario) {
        Call<Void> call = usuarioApi.eliminar(usuario.getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!isAdded()) return;
                
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Usuario eliminado correctamente", Toast.LENGTH_SHORT).show();
                    cargarUsuariosDesdeAPI();
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