package com.example.proyectoarboles.adapter;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoarboles.R;
import com.example.proyectoarboles.model.Usuario;

import java.util.List;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {

    private List<Usuario> listaUsuarios;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditarClick(Usuario usuario);
    }

    public UsuarioAdapter(List<Usuario> listaUsuarios, OnItemClickListener listener) {
        this.listaUsuarios = listaUsuarios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuario, parent, false);
        return new UsuarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        holder.bind(listaUsuarios.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.listaUsuarios = usuarios;
        notifyDataSetChanged();
    }

    public static class UsuarioViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewNombre;
        private TextView textViewEmail;
        private TextView textViewRol;
        private View viewActivo;
        private ImageView imageViewAvatar;

        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombreUsuario);
            textViewEmail = itemView.findViewById(R.id.textViewEmailUsuario);
            textViewRol = itemView.findViewById(R.id.textViewRolUsuario);
            viewActivo = itemView.findViewById(R.id.viewActivo);
            imageViewAvatar = itemView.findViewById(R.id.imageViewAvatar);
        }

        public void bind(Usuario usuario, OnItemClickListener listener) {
            textViewNombre.setText(usuario.getNombre() != null ? usuario.getNombre() : "Nombre no disponible");
            textViewEmail.setText(usuario.getEmail() != null ? usuario.getEmail() : "Email no disponible");
            
            // Configurar rol con color
            String rol = usuario.getRol();
            textViewRol.setText(rol != null ? rol : "SIN ROL");
            
            int colorRol;
            if ("ADMIN".equals(rol)) {
                colorRol = ContextCompat.getColor(itemView.getContext(), R.color.accent);
            } else if ("COORDINADOR".equals(rol)) {
                colorRol = ContextCompat.getColor(itemView.getContext(), R.color.green_secondary);
            } else {
                colorRol = ContextCompat.getColor(itemView.getContext(), R.color.gray_600);
            }
            textViewRol.setTextColor(colorRol);

            // Configurar indicador de activo
            Boolean activo = usuario.getActivo();
            int colorActivo;
            if (activo != null && activo) {
                colorActivo = ContextCompat.getColor(itemView.getContext(), R.color.green_secondary);
            } else {
                colorActivo = ContextCompat.getColor(itemView.getContext(), R.color.red_primary);
            }

            GradientDrawable drawable = (GradientDrawable) viewActivo.getBackground();
            if (drawable != null) {
                drawable.setColor(colorActivo);
            }

            // Click para editar (el botón eliminar está en el diálogo de edición)
            itemView.setOnClickListener(v -> listener.onEditarClick(usuario));
        }
    }
}