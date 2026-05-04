package com.example.proyectoarboles.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proyectoarboles.R;
import com.example.proyectoarboles.activities.MainActivity;
import com.example.proyectoarboles.util.IslaUtils;
import com.example.proyectoarboles.api.RetrofitClient;
import com.example.proyectoarboles.model.Arbol;
import com.example.proyectoarboles.model.CentroEducativo;
import com.example.proyectoarboles.model.DispositivoEsp32;
import com.example.proyectoarboles.util.PermissionManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleCentroFragment extends Fragment {

    private static final String TAG = "DetalleCentroFragment";
    private static final String ARG_CENTRO_ID = "centro_id";

    private long centroId = -1;
    private CentroEducativo centroActual;

    private TextView tvTitulo, tvResponsable, tvTelefono, tvEmail, tvFechaCreacion;
    private TextView tvDireccion, tvPoblacion, tvIsla;
    private TextView tvTituloDispositivos, tvNoDispositivos;
    private TextView tvTituloArboles, tvNoArboles;
    private LinearLayout layoutDispositivos, layoutArboles;
    private ImageButton btnVolver, btnEditarCentro, btnEliminarCentro;
    private Button btnAnadirDispositivo, btnAnadirArbol;

    private PermissionManager permissionManager;

    public static DetalleCentroFragment newInstance(long centroId) {
        DetalleCentroFragment fragment = new DetalleCentroFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CENTRO_ID, centroId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detalle_centro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        permissionManager = new PermissionManager(requireContext());

        inicializarVistas(view);

        if (getArguments() != null) {
            centroId = getArguments().getLong(ARG_CENTRO_ID, -1);
        }

        if (centroId != -1) {
            configurarListeners();
            cargarCentro(centroId);
            cargarDispositivos(centroId);
            cargarArboles(centroId);
        } else {
            Toast.makeText(requireContext(), "Error: ID de centro no válido", Toast.LENGTH_LONG).show();
            ((MainActivity) requireActivity()).navigateToListarCentros();
        }
    }

    private void inicializarVistas(View view) {
        tvTitulo = view.findViewById(R.id.textViewTituloCentro);
        tvResponsable = view.findViewById(R.id.textViewResponsable);
        tvTelefono = view.findViewById(R.id.textViewTelefono);
        tvEmail = view.findViewById(R.id.textViewEmail);
        tvFechaCreacion = view.findViewById(R.id.textViewFechaCreacion);
        tvDireccion = view.findViewById(R.id.textViewDireccion);
        tvPoblacion = view.findViewById(R.id.textViewPoblacion);
        tvIsla = view.findViewById(R.id.textViewIsla);
        tvTituloDispositivos = view.findViewById(R.id.textViewTituloDispositivos);
        tvNoDispositivos = view.findViewById(R.id.textViewNoDispositivos);
        tvTituloArboles = view.findViewById(R.id.textViewTituloArboles);
        tvNoArboles = view.findViewById(R.id.textViewNoArboles);
        layoutDispositivos = view.findViewById(R.id.layoutListaDispositivos);
        layoutArboles = view.findViewById(R.id.layoutListaArboles);
        btnVolver = view.findViewById(R.id.buttonVolverCentro);
        btnEditarCentro = view.findViewById(R.id.buttonEditarCentro);
        btnEliminarCentro = view.findViewById(R.id.buttonEliminarCentro);
        btnAnadirDispositivo = view.findViewById(R.id.buttonAnadirDispositivo);
        btnAnadirArbol = view.findViewById(R.id.buttonAnadirArbol);
    }

    private void configurarListeners() {
        btnVolver.setOnClickListener(v ->
                ((MainActivity) requireActivity()).navigateToListarCentros());

        btnEditarCentro.setOnClickListener(v ->
                ((MainActivity) requireActivity()).navigateToFormularioCentro(centroId));

        btnEliminarCentro.setOnClickListener(v -> mostrarDialogoEliminarCentro());

        btnAnadirDispositivo.setOnClickListener(v ->
                ((MainActivity) requireActivity()).navigateToFormularioDispositivo(-1, centroId));

        btnAnadirArbol.setOnClickListener(v ->
                ((MainActivity) requireActivity()).navigateToCrearArbol(
                        centroId, centroActual != null ? centroActual.getNombre() : ""));
    }

    private void cargarCentro(long id) {
        Call<CentroEducativo> call = RetrofitClient.getCentroEducativoApi().obtenerCentroPorId(id);
        call.enqueue(new Callback<CentroEducativo>() {
            @Override
            public void onResponse(Call<CentroEducativo> call, Response<CentroEducativo> response) {
                if (!isAdded()) return;
                if (response.isSuccessful() && response.body() != null) {
                    centroActual = response.body();
                    mostrarDatosCentro(centroActual);
                    actualizarBotonesPermisos();
                } else {
                    Toast.makeText(requireContext(), "Error al cargar el centro", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error cargando centro: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<CentroEducativo> call, Throwable t) {
                if (!isAdded()) return;
                Log.e(TAG, "Error de conexión: " + t.getMessage());
                Toast.makeText(requireContext(), "Error de conexión", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void mostrarDatosCentro(CentroEducativo centro) {
        String titulo = centro.getNombre() != null ? centro.getNombre() : "Centro";
        String islaLabel = IslaUtils.formatear(centro.getIsla());
        if (!islaLabel.equals("-")) {
            titulo += " · " + islaLabel;
        }
        tvTitulo.setText(titulo);
        tvResponsable.setText(centro.getResponsable() != null ? centro.getResponsable() : "-");
        tvTelefono.setText(centro.getTelefono() != null ? centro.getTelefono() : "-");
        tvEmail.setText(centro.getEmail() != null ? centro.getEmail() : "-");
        tvFechaCreacion.setText(formatearFecha(centro.getFechaCreacion()));
        tvDireccion.setText(centro.getDireccion() != null ? centro.getDireccion() : "-");
        tvPoblacion.setText(centro.getPoblacion() != null ? centro.getPoblacion() : "-");
        tvIsla.setText(islaLabel);
    }

    private void actualizarBotonesPermisos() {
        if (centroActual == null) return;
        boolean puedeEditar = permissionManager.puedeEditarCentro(centroActual.getId());
        boolean puedeEliminar = permissionManager.puedeEliminarCentro(centroActual.getId());
        btnEditarCentro.setVisibility(puedeEditar ? View.VISIBLE : View.GONE);
        btnEliminarCentro.setVisibility(puedeEliminar ? View.VISIBLE : View.GONE);
        btnAnadirDispositivo.setVisibility(puedeEditar ? View.VISIBLE : View.GONE);
        btnAnadirArbol.setVisibility(puedeEditar ? View.VISIBLE : View.GONE);
    }

    private void cargarDispositivos(long idCentro) {
        Call<List<DispositivoEsp32>> call = RetrofitClient.getDispositivoApi().obtenerDispositivosPorCentro(idCentro);
        call.enqueue(new Callback<List<DispositivoEsp32>>() {
            @Override
            public void onResponse(Call<List<DispositivoEsp32>> call, Response<List<DispositivoEsp32>> response) {
                if (!isAdded()) return;
                if (response.isSuccessful() && response.body() != null) {
                    List<DispositivoEsp32> dispositivos = response.body();
                    tvTituloDispositivos.setText("Dispositivos ESP32 (" + dispositivos.size() + ")");
                    layoutDispositivos.removeAllViews();
                    if (dispositivos.isEmpty()) {
                        tvNoDispositivos.setVisibility(View.VISIBLE);
                    } else {
                        tvNoDispositivos.setVisibility(View.GONE);
                        for (DispositivoEsp32 d : dispositivos) {
                            agregarFilaDispositivo(d);
                        }
                    }
                } else {
                    Log.e(TAG, "Error cargando dispositivos: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<DispositivoEsp32>> call, Throwable t) {
                if (!isAdded()) return;
                Log.e(TAG, "Error de conexión cargando dispositivos: " + t.getMessage());
            }
        });
    }

    private void agregarFilaDispositivo(DispositivoEsp32 dispositivo) {
        View itemView = LayoutInflater.from(requireContext()).inflate(R.layout.item_dispositivo, layoutDispositivos, false);

        TextView tvMac = itemView.findViewById(R.id.textViewMacDispositivo);
        TextView tvEstado = itemView.findViewById(R.id.textViewEstadoDispositivo);
        TextView tvFrecuencia = itemView.findViewById(R.id.textViewFrecuenciaDispositivo);
        TextView tvUltimaConexion = itemView.findViewById(R.id.textViewUltimaConexion);
        Button btnHistorico = itemView.findViewById(R.id.buttonHistoricoDispositivo);
        ImageButton btnEditar = itemView.findViewById(R.id.buttonEditarDispositivo);
        ImageButton btnEliminar = itemView.findViewById(R.id.buttonEliminarDispositivo);

        tvMac.setText(dispositivo.getMacAddress());
        boolean activo = Boolean.TRUE.equals(dispositivo.getActivo());
        tvEstado.setText(activo ? "Activo" : "Inactivo");
        tvEstado.setTextColor(activo ? 0xFF22c55e : 0xFF9ca3af);

        Integer freq = dispositivo.getFrecuenciaLecturaSeg();
        tvFrecuencia.setText("Frecuencia: " + (freq != null ? freq + " s" : "-"));
        tvUltimaConexion.setText(formatearFechaHora(dispositivo.getUltimaConexion()));

        btnHistorico.setOnClickListener(v ->
                ((MainActivity) requireActivity()).navigateToHistoricoDispositivo(
                        dispositivo.getId(), dispositivo.getMacAddress()));

        boolean puedeEditar = centroActual != null && permissionManager.puedeEditarCentro(centroActual.getId());
        boolean puedeEliminar = centroActual != null && permissionManager.puedeEliminarCentro(centroActual.getId());

        if (puedeEditar) {
            btnEditar.setVisibility(View.VISIBLE);
            btnEditar.setOnClickListener(v ->
                    ((MainActivity) requireActivity()).navigateToFormularioDispositivo(dispositivo.getId(), -1));
        }
        if (puedeEliminar) {
            btnEliminar.setVisibility(View.VISIBLE);
            btnEliminar.setOnClickListener(v -> mostrarDialogoEliminarDispositivo(dispositivo));
        }

        layoutDispositivos.addView(itemView);
    }

    private void cargarArboles(long idCentro) {
        Call<List<Arbol>> call = RetrofitClient.getCentroEducativoApi().obtenerArbolesPorCentro(idCentro);
        call.enqueue(new Callback<List<Arbol>>() {
            @Override
            public void onResponse(Call<List<Arbol>> call, Response<List<Arbol>> response) {
                if (!isAdded()) return;
                if (response.isSuccessful() && response.body() != null) {
                    List<Arbol> arboles = response.body();
                    tvTituloArboles.setText("Árboles del Centro (" + arboles.size() + ")");
                    layoutArboles.removeAllViews();
                    if (arboles.isEmpty()) {
                        tvNoArboles.setVisibility(View.VISIBLE);
                    } else {
                        tvNoArboles.setVisibility(View.GONE);
                        for (Arbol arbol : arboles) {
                            agregarFilaArbol(arbol);
                        }
                    }
                } else {
                    Log.e(TAG, "Error cargando árboles: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Arbol>> call, Throwable t) {
                if (!isAdded()) return;
                Log.e(TAG, "Error de conexión cargando árboles: " + t.getMessage());
            }
        });
    }

    private void agregarFilaArbol(Arbol arbol) {
        View itemView = LayoutInflater.from(requireContext()).inflate(R.layout.item_arbol_fila, layoutArboles, false);

        TextView tvNombre = itemView.findViewById(R.id.textViewNombreArbolFila);
        TextView tvEspecie = itemView.findViewById(R.id.textViewEspecieArbolFila);
        TextView tvCantidad = itemView.findViewById(R.id.textViewCantidadArbolFila);
        Button btnVer = itemView.findViewById(R.id.buttonVerArbol);

        tvNombre.setText(arbol.getNombre());
        tvEspecie.setText(arbol.getEspecie());
        tvCantidad.setText("Cantidad: " + arbol.getCantidad());
        btnVer.setOnClickListener(v ->
                ((MainActivity) requireActivity()).navigateToArbolDetalles(arbol.getId()));

        View separator = new View(requireContext());
        separator.setBackgroundColor(0xFFe5e7eb);
        LinearLayout.LayoutParams sepParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 1);
        separator.setLayoutParams(sepParams);

        layoutArboles.addView(itemView);
        layoutArboles.addView(separator);
    }

    private void mostrarDialogoEliminarDispositivo(DispositivoEsp32 dispositivo) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Eliminar dispositivo")
                .setMessage("¿Eliminar el dispositivo " + dispositivo.getMacAddress() + "?\n\nSe eliminarán también todas sus lecturas y alertas.")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarDispositivo(dispositivo))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarDispositivo(DispositivoEsp32 dispositivo) {
        Call<DispositivoEsp32> call = RetrofitClient.getDispositivoApi().eliminarDispositivo(dispositivo.getId());
        call.enqueue(new Callback<DispositivoEsp32>() {
            @Override
            public void onResponse(Call<DispositivoEsp32> call, Response<DispositivoEsp32> response) {
                if (!isAdded()) return;
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Dispositivo eliminado", Toast.LENGTH_SHORT).show();
                    cargarDispositivos(centroId);
                } else {
                    Toast.makeText(requireContext(), "Error al eliminar dispositivo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DispositivoEsp32> call, Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDialogoEliminarCentro() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Eliminar centro")
                .setMessage("¿Eliminar el centro " + (centroActual != null ? centroActual.getNombre() : "") + "?\n\nEsta acción no se puede deshacer.")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarCentro())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarCentro() {
        Call<Void> call = RetrofitClient.getCentroEducativoApi().eliminarCentro(centroId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!isAdded()) return;
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Centro eliminado", Toast.LENGTH_SHORT).show();
                    ((MainActivity) requireActivity()).navigateToListarCentros();
                } else {
                    Toast.makeText(requireContext(), "Error al eliminar el centro", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String formatearFecha(String fechaISO) {
        if (fechaISO == null || fechaISO.isEmpty()) return "-";
        try {
            SimpleDateFormat entrada = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = entrada.parse(fechaISO.substring(0, 10));
            SimpleDateFormat salida = new SimpleDateFormat("d MMM yyyy", new Locale("es", "ES"));
            return salida.format(date);
        } catch (ParseException e) {
            return fechaISO;
        }
    }

    private String formatearFechaHora(String fechaISO) {
        if (fechaISO == null || fechaISO.isEmpty()) return "Nunca";
        try {
            SimpleDateFormat entrada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date date = entrada.parse(fechaISO.substring(0, 19));
            SimpleDateFormat salida = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());
            return salida.format(date);
        } catch (ParseException e) {
            return fechaISO;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (centroId != -1) {
            cargarCentro(centroId);
            cargarDispositivos(centroId);
            cargarArboles(centroId);
        }
    }
}
