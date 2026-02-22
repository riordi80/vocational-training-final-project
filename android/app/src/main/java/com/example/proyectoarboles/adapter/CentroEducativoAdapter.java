package com.example.proyectoarboles.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoarboles.R;
import com.example.proyectoarboles.model.CentroEducativo;
import com.example.proyectoarboles.util.PermissionManager;

import java.util.List;

public class CentroEducativoAdapter extends RecyclerView.Adapter<CentroEducativoAdapter.CentroEducativoViewHolder> {

    private List<CentroEducativo> listaCentros;
    private OnItemClickListener listener;
    private PermissionManager permissionManager;

    public interface OnItemClickListener {
        void onVerArbolesClick(CentroEducativo centro);
        void onEditarCentroClick(CentroEducativo centro);
        void onEliminarCentroClick(CentroEducativo centro);
    }

    public CentroEducativoAdapter(List<CentroEducativo> listaCentros, OnItemClickListener listener) {
        this.listaCentros = listaCentros;
        this.listener = listener;
        this.permissionManager = null; // Pueden usarse sin PermissionManager si es necesario
    }

    public CentroEducativoAdapter(List<CentroEducativo> listaCentros, OnItemClickListener listener, PermissionManager permissionManager) {
        this.listaCentros = listaCentros;
        this.listener = listener;
        this.permissionManager = permissionManager;
    }

    @NonNull
    @Override
    public CentroEducativoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_centros, parent, false);
        return new CentroEducativoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CentroEducativoViewHolder holder, int position) {
        CentroEducativo centro = listaCentros.get(position);
        holder.bind(centro, listener, permissionManager);
    }

    @Override
    public int getItemCount() {
        return listaCentros.size();
    }

    public static class CentroEducativoViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewNombre;
        private TextView textViewDireccion;
        private Button buttonVerArboles;
        private Button buttonEditarCentro;
        private Button buttonEliminarCentro;

        public CentroEducativoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombreCentro);
            textViewDireccion = itemView.findViewById(R.id.textViewDireccionCentro);
            buttonVerArboles = itemView.findViewById(R.id.buttonVerArboles);
            buttonEditarCentro = itemView.findViewById(R.id.buttonEditarCentro);
            buttonEliminarCentro = itemView.findViewById(R.id.buttonEliminarCentro);
        }

        public void bind(final CentroEducativo centro, final OnItemClickListener listener, PermissionManager permissionManager) {
            if (centro != null) {
                if (centro.getNombre() != null) {
                    textViewNombre.setText(centro.getNombre());
                } else {
                    textViewNombre.setText("Nombre no disponible");
                }

                if (centro.getDireccion() != null && !centro.getDireccion().isEmpty()) {
                    textViewDireccion.setText(centro.getDireccion());
                } else {
                    textViewDireccion.setText("Dirección no disponible");
                }

                // Botón Ver Árboles - siempre visible para todos
                buttonVerArboles.setOnClickListener(v -> listener.onVerArbolesClick(centro));

                // Configurar visibilidad de botones de edición según permisos
                if (permissionManager != null) {
                    // Botón Editar - visible para COORDINADOR (de este centro) y ADMIN
                    if (permissionManager.puedeEditarCentro(centro.getId())) {
                        buttonEditarCentro.setVisibility(View.VISIBLE);
                        buttonEditarCentro.setOnClickListener(v -> listener.onEditarCentroClick(centro));
                    } else {
                        buttonEditarCentro.setVisibility(View.GONE);
                    }

                    // Botón Eliminar - visible solo para ADMIN
                    if (permissionManager.puedeEliminarCentro(centro.getId())) {
                        buttonEliminarCentro.setVisibility(View.VISIBLE);
                        buttonEliminarCentro.setOnClickListener(v -> listener.onEliminarCentroClick(centro));
                    } else {
                        buttonEliminarCentro.setVisibility(View.GONE);
                    }
                } else {
                    // Sin PermissionManager, ocultar botones de edición
                    buttonEditarCentro.setVisibility(View.GONE);
                    buttonEliminarCentro.setVisibility(View.GONE);
                }
            }
        }
    }
}
