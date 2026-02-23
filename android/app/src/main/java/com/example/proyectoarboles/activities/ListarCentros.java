package com.example.proyectoarboles.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoarboles.R;
import com.example.proyectoarboles.adapter.CentroEducativoAdapter;
import com.example.proyectoarboles.api.RetrofitClient;
import com.example.proyectoarboles.model.CentroEducativo;
import com.example.proyectoarboles.util.PermissionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListarCentros extends AppCompatActivity implements CentroEducativoAdapter.OnItemClickListener {

    private static final String TAG = "ListarCentros";

    private RecyclerView recyclerViewCentros;
    private CentroEducativoAdapter adapter;
    private List<CentroEducativo> listaCentros = new ArrayList<>();
    private Button btLogin, btRegister, btCerrarSesion;
    private SharedPreferences sharedPreferences;
    private PermissionManager permissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_centros);

        sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        permissionManager = new PermissionManager(this);

        recyclerViewCentros = findViewById(R.id.RecyclerViewCentros);
        btLogin = findViewById(R.id.btLogin);
        btRegister = findViewById(R.id.btRegister);
        btCerrarSesion = findViewById(R.id.btCerrarS);

        btRegister.setVisibility(View.GONE);

        configurarRecyclerView();
        cargarCentrosDesdeAPI();
        configurarListeners();
        actualizarVisibilidadBotones();
    }

    private void configurarRecyclerView() {
        recyclerViewCentros.setLayoutManager(new LinearLayoutManager(this));
        // Pasamos permissionManager para que el adaptador pueda validar permisos
        adapter = new CentroEducativoAdapter(listaCentros, this, permissionManager);
        recyclerViewCentros.setAdapter(adapter);
    }

    @Override
    public void onVerArbolesClick(CentroEducativo centro) {
        Intent intent = new Intent(ListarCentros.this, ListarArboles.class);
        intent.putExtra("centro_id", centro.getId());
        startActivity(intent);
    }

    @Override
    public void onEditarCentroClick(CentroEducativo centro) {
        Toast.makeText(this, "Editar centro: " + centro.getNombre(), Toast.LENGTH_SHORT).show();
        // TODO: Aquí iría la lógica para editar el centro
        // Por ejemplo, abrir un diálogo o llamar a API para actualizar
    }

    @Override
    public void onEliminarCentroClick(CentroEducativo centro) {
        Toast.makeText(this, "Eliminar centro: " + centro.getNombre(), Toast.LENGTH_SHORT).show();
        // TODO: Aquí iría la lógica para eliminar el centro
        // Por ejemplo, mostrar confirmation dialog y llamar a API
    }

    private void cargarCentrosDesdeAPI() {
        Log.d(TAG, "Iniciando carga de centros desde API...");
        Call<List<CentroEducativo>> call = RetrofitClient.getCentroEducativoApi().obtenerTodosLosCentros();

        call.enqueue(new Callback<List<CentroEducativo>>() {
            @Override
            public void onResponse(Call<List<CentroEducativo>> call, Response<List<CentroEducativo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaCentros.clear();
                    listaCentros.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "Centros cargados: " + listaCentros.size());
                    if (listaCentros.isEmpty()) {
                        Toast.makeText(ListarCentros.this, "No hay centros registrados", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorBody = "";
                    try {
                        errorBody = response.errorBody().string();
                    } catch (Exception e) {
                        Log.e(TAG, "Error al leer el cuerpo del error", e);
                    }
                    Log.e(TAG, "Error al cargar centros. Código: " + response.code() + ", Mensaje: " + response.message() + ", Cuerpo: " + errorBody);
                    Toast.makeText(ListarCentros.this, "Error al cargar centros: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<CentroEducativo>> call, Throwable t) {
                Log.e(TAG, "Error de conexión", t);
                Toast.makeText(ListarCentros.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void configurarListeners() {
        btLogin.setOnClickListener(v -> {
            Intent intent = new Intent(ListarCentros.this, Login.class);
            startActivity(intent);
        });

        btCerrarSesion.setOnClickListener(v -> {
            permissionManager.clearSession();
            actualizarVisibilidadBotones();
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
        });
    }

    private void actualizarVisibilidadBotones() {
        boolean isLoggedIn = permissionManager.isLoggedIn();
        btLogin.setVisibility(isLoggedIn ? View.GONE : View.VISIBLE);
        btRegister.setVisibility(View.GONE); // Siempre oculto por ahora
        btCerrarSesion.setVisibility(isLoggedIn ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarVisibilidadBotones();
    }
}
