package com.example.proyectoarboles.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoarboles.R;
import com.example.proyectoarboles.model.Arbol;

import java.util.List;

public class ArbolAdapter extends RecyclerView.Adapter<ArbolAdapter.ArbolViewHolder>{
    private List<Arbol> arboles;
    private OnItemClickListener listener;

    public ArbolAdapter(List<Arbol> arboles, OnItemClickListener listener){
        this.arboles = arboles;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ArbolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }


    //El metodo onBindViewHolder es como un while, muestra todos los arboles que tengamos en el xml
    @Override
    public void onBindViewHolder(@NonNull ArbolViewHolder holder, int position) {
        Arbol arbol = arboles.get(position);

        holder.textViewNombre.setText(arbol.getNombre());
        holder.textViewEspecieFecha.setText(arbol.getEspecie() + " - " + arbol.getFechaPlantacion());

        holder.itemView.setOnClickListener(v -> listener.OnItemClick(arbol));
    }

    @Override
    public int getItemCount() {
        return arboles.size();
    }

    public static class ArbolViewHolder extends RecyclerView.ViewHolder{
        TextView textViewNombre, textViewEspecieFecha;
        public ArbolViewHolder(@NonNull View itemView){
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
            textViewEspecieFecha = itemView.findViewById(R.id.textViewEspecieFecha);
        }
    }

    public interface OnItemClickListener{
        void OnItemClick(Arbol arbol);
    }
}
