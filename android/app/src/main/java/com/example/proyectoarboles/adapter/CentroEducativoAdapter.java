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
        void onItemClick(CentroEducativo centro);
    }

    public CentroEducativoAdapter(List<CentroEducativo> listaCentros, OnItemClickListener listener) {
        this.listaCentros = listaCentros;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CentroEducativoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new CentroEducativoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CentroEducativoViewHolder holder, int position) {
        CentroEducativo centro = listaCentros.get(position);
        holder.bind(centro, listener);
    }

    @Override
    public int getItemCount() {
        return listaCentros.size();
    }

    public static class CentroEducativoViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewNombre;

        public CentroEducativoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
        }

        public void bind(final CentroEducativo centro, final OnItemClickListener listener) {
            textViewNombre.setText(centro.getNombre());
            itemView.setOnClickListener(v -> listener.onItemClick(centro));
        }
    }
}
