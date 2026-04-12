package com.example.proyectoarboles.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoarboles.R;
import com.example.proyectoarboles.model.Arbol;
import com.example.proyectoarboles.util.PermissionManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ArbolAdapter extends RecyclerView.Adapter<ArbolAdapter.ArbolViewHolder> {

    private List<Arbol> listaArboles;
    private OnItemClickListener listener;
    private PermissionManager permissionManager;

    public interface OnItemClickListener {
        void OnItemClick(Arbol arbol);
    }

    public ArbolAdapter(List<Arbol> listaArboles, OnItemClickListener listener) {
        this.listaArboles = listaArboles;
        this.listener = listener;
        this.permissionManager = null;
    }

    public ArbolAdapter(List<Arbol> listaArboles, OnItemClickListener listener, PermissionManager permissionManager) {
        this.listaArboles = listaArboles;
        this.listener = listener;
        this.permissionManager = permissionManager;
    }

    @NonNull
    @Override
    public ArbolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_arbol, parent, false);
        return new ArbolViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArbolViewHolder holder, int position) {
        Arbol arbol = listaArboles.get(position);

        holder.textViewNombre.setText(arbol.getNombre());
        holder.textViewEspecieFecha.setText(arbol.getEspecie() + " - " + formatearFechaEspanol(arbol.getFechaPlantacion()));

        if (arbol.getCentroEducativo() != null) {
            holder.textViewCentro.setText(arbol.getCentroEducativo().getNombre());
        } else {
            holder.textViewCentro.setText("Sin centro asignado");
        }

        holder.itemView.setOnClickListener(v -> listener.OnItemClick(arbol));
    }

    @Override
    public int getItemCount() {
        return listaArboles.size();
    }

    public static class ArbolViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre, textViewEspecieFecha, textViewCentro;

        public ArbolViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
            textViewEspecieFecha = itemView.findViewById(R.id.textViewEspecieFecha);
            textViewCentro = itemView.findViewById(R.id.textViewCentro);
        }
    }

    private String formatearFechaEspanol(String fecha) {
        if (fecha == null || fecha.isEmpty()) return "Fecha no disponible";
        try {
            SimpleDateFormat entrada = new SimpleDateFormat("yyyy-MM-dd", new Locale("es", "ES"));
            Date date = entrada.parse(fecha);
            return new SimpleDateFormat("d MMM yyyy", new Locale("es", "ES")).format(date);
        } catch (ParseException e) {
            try {
                SimpleDateFormat entrada2 = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "ES"));
                Date date = entrada2.parse(fecha);
                return new SimpleDateFormat("d MMM yyyy", new Locale("es", "ES")).format(date);
            } catch (ParseException e2) {
                return fecha;
            }
        }
    }
}
