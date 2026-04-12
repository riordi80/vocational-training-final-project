package com.example.proyectoarboles.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoarboles.R;
import com.example.proyectoarboles.model.CentroEducativo;

import java.util.List;

public class CentroEducativoAdapter extends RecyclerView.Adapter<CentroEducativoAdapter.CentroEducativoViewHolder> {

    private List<CentroEducativo> listaCentros;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onVerDetalleClick(CentroEducativo centro);
    }

    public CentroEducativoAdapter(List<CentroEducativo> listaCentros, OnItemClickListener listener) {
        this.listaCentros = listaCentros;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CentroEducativoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_centros, parent, false);
        return new CentroEducativoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CentroEducativoViewHolder holder, int position) {
        holder.bind(listaCentros.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return listaCentros.size();
    }

    public static class CentroEducativoViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewNombre;
        private TextView textViewDireccion;

        public CentroEducativoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombreCentro);
            textViewDireccion = itemView.findViewById(R.id.textViewDireccionCentro);
        }

        public void bind(CentroEducativo centro, OnItemClickListener listener) {
            textViewNombre.setText(centro.getNombre() != null ? centro.getNombre() : "Nombre no disponible");
            textViewDireccion.setText(
                    centro.getDireccion() != null && !centro.getDireccion().isEmpty()
                            ? centro.getDireccion()
                            : "Dirección no disponible"
            );
            itemView.setOnClickListener(v -> listener.onVerDetalleClick(centro));
        }
    }
}
