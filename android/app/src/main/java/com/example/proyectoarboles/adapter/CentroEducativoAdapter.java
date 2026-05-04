package com.example.proyectoarboles.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoarboles.R;
import com.example.proyectoarboles.model.CentroEducativo;
import com.example.proyectoarboles.util.IslaUtils;

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
        private TextView textViewPoblacion;
        private TextView textViewIsla;

        public CentroEducativoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombreCentro);
            textViewPoblacion = itemView.findViewById(R.id.textViewPoblacion);
            textViewIsla = itemView.findViewById(R.id.textViewIsla);
        }

        public void bind(CentroEducativo centro, OnItemClickListener listener) {
            textViewNombre.setText(centro.getNombre() != null ? centro.getNombre() : "Nombre no disponible");
            String poblacion = centro.getPoblacion() != null ? centro.getPoblacion() : "";
            String isla = IslaUtils.formatear(centro.getIsla());
            textViewPoblacion.setText(poblacion);
            textViewIsla.setText(isla.equals("-") ? "" : isla);
            itemView.setOnClickListener(v -> listener.onVerDetalleClick(centro));
        }
    }
}
