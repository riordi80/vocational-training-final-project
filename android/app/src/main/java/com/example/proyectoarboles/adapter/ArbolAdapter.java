package com.example.proyectoarboles.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoarboles.R;
import com.example.proyectoarboles.model.Arbol;

import java.util.List;

public class ArbolAdapter extends RecyclerView.Adapter<ArbolAdapter.ArbolViewHolder>{
    private List<Arbol> listaArboles;
    private OnItemClickListener listener;

    private onArbolDeleteListener deleteListener;

    public ArbolAdapter(List<Arbol> listaArboles, onArbolDeleteListener deleteListener,OnItemClickListener listener){
        this.listaArboles = listaArboles;
        this.deleteListener = deleteListener;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ArbolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_arbol, parent, false);
        return new ArbolViewHolder(view);
    }


    //El metodo onBindViewHolder es como un while, muestra todos los arboles que tengamos en el xml
    @Override
    public void onBindViewHolder(@NonNull ArbolViewHolder holder, int position) {
        Arbol arbol = listaArboles.get(position);

        holder.textViewNombre.setText(arbol.getNombre());
        holder.textViewEspecieFecha.setText(arbol.getEspecie() + " - " + arbol.getFechaPlantacion());

        holder.buttonDelete.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            if (pos != RecyclerView.NO_POSITION && deleteListener != null){
                deleteListener.onArbolDelete(pos);
            }
        });

        holder.itemView.setOnClickListener(v -> listener.OnItemClick(arbol));

    }

    @Override
    public int getItemCount() {
        return listaArboles.size();
    }

    public static class ArbolViewHolder extends RecyclerView.ViewHolder{
        TextView textViewNombre, textViewEspecieFecha;
        Button buttonDelete;
        public ArbolViewHolder(@NonNull View itemView){
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
            textViewEspecieFecha = itemView.findViewById(R.id.textViewEspecieFecha);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }

    public interface OnItemClickListener{
        void OnItemClick(Arbol arbol);
    }

    public interface onArbolDeleteListener{
        void onArbolDelete(int position);
    }

    public void eliminarArbol(int position) {
        listaArboles.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listaArboles.size());
    }
}
