package com.example.proyectoarboles.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proyectoarboles.R;

public class AyudaFragment extends Fragment {

    private static class Seccion {
        final String titulo;
        final String[][] items;

        Seccion(String titulo, String[][] items) {
            this.titulo = titulo;
            this.items = items;
        }
    }

    private static final Seccion[] SECCIONES = {
        new Seccion("Inicio de sesión y registro", new String[][] {
            {
                "¿Cómo inicio sesión?",
                "Introduce tu email y contraseña en la pantalla Login y pulsa «Iniciar Sesión». Si tus credenciales son correctas accederás al Inicio."
            },
            {
                "Mensajes de error habituales al iniciar sesión",
                "• «Email y contraseña son requeridos» — Rellena ambos campos.\n" +
                "• «Credenciales incorrectas» — Verifica email y contraseña.\n" +
                "• «Error de conexión. Inténtalo de nuevo.» — El servidor puede estar arrancando (cold start). Espera 30-60 segundos y vuelve a intentarlo."
            },
            {
                "¿Cómo me registro?",
                "Pulsa «Registrarse» desde la pantalla de login. Completa nombre, email y contraseña. Si el registro es correcto, la app inicia sesión automáticamente. Los nuevos usuarios tienen rol COORDINADOR por defecto."
            },
        }),
        new Seccion("Navegación", new String[][] {
            {
                "¿Cómo navego por la app?",
                "En vertical (portrait) usa la barra inferior con Inicio, Centros, Usuarios (solo ADMIN) y Login/Logout. En horizontal (landscape) la navegación aparece como rail lateral con las mismas opciones."
            },
            {
                "No veo la sección «Usuarios»",
                "La sección Usuarios solo es visible para cuentas con rol ADMIN. Si eres COORDINADOR no tienes acceso a la gestión de usuarios."
            },
        }),
        new Seccion("Pantalla Inicio (Dashboard)", new String[][] {
            {
                "¿Qué muestra el Inicio?",
                "• Número de centros educativos.\n" +
                "• Total de árboles plantados.\n" +
                "• Número de dispositivos activos.\n" +
                "• CO₂ total anual estimado.\n" +
                "• Si hay sesión activa, se muestra tu nombre y rol.\n" +
                "• Botón para ir al listado de centros."
            },
            {
                "Los datos no se cargan",
                "El servidor puede estar arrancando tras un periodo de inactividad (cold start en Render). Espera 30-60 segundos y vuelve a la pantalla."
            },
        }),
        new Seccion("Centros educativos", new String[][] {
            {
                "¿Cómo veo los centros?",
                "Pulsa «Centros» en la navegación. Verás el listado con nombre, población e isla. Pulsa un centro para ver su detalle con árboles y dispositivos."
            },
            {
                "¿Cómo creo o edito un centro?",
                "Necesitas rol ADMIN o COORDINADOR. Desde el listado pulsa el botón de nuevo centro. Para editar, entra al detalle y usa el botón de edición."
            },
        }),
        new Seccion("Árboles", new String[][] {
            {
                "¿Dónde gestiono los árboles?",
                "Los árboles se gestionan desde el detalle de cada centro educativo. Ve a Centros → selecciona un centro → sección de árboles."
            },
            {
                "¿Qué información tiene el detalle de un árbol?",
                "Nombre común y científico, especie, cantidad de ejemplares, fecha de plantación y absorción de CO₂ anual estimada."
            },
            {
                "¿Cómo creo o edito un árbol?",
                "Necesitas rol ADMIN o COORDINADOR. Desde el detalle del centro pulsa «Añadir árbol». Para editar, entra al detalle del árbol y usa el botón de edición."
            },
        }),
        new Seccion("Dispositivos IoT", new String[][] {
            {
                "¿Qué es un dispositivo IoT?",
                "Es un sensor ESP32 instalado en un centro que mide temperatura, humedad, CO₂, humedad del suelo y luz. Los datos se envían automáticamente al sistema."
            },
            {
                "¿Cómo veo las lecturas de un dispositivo?",
                "Ve al detalle del centro → sección Dispositivos → pulsa el dispositivo → «Ver histórico». Podrás filtrar por periodo y ver las gráficas de evolución."
            },
            {
                "¿Cómo registro un nuevo dispositivo?",
                "Necesitas rol ADMIN o COORDINADOR. Desde el detalle del centro pulsa «Añadir dispositivo». Introduce el nombre y la dirección MAC del ESP32."
            },
        }),
        new Seccion("Alertas", new String[][] {
            {
                "¿Qué es una alerta?",
                "Una alerta se genera automáticamente cuando una lectura de un sensor supera el umbral configurado. Puede tener estado ACTIVA o RESUELTA."
            },
            {
                "¿Dónde veo las alertas?",
                "Las alertas aparecen en el detalle del dispositivo. Cada alerta muestra el sensor afectado, el valor registrado y la fecha."
            },
        }),
        new Seccion("Preguntas frecuentes", new String[][] {
            {
                "¿Por qué la app tarda en cargar la primera vez?",
                "El servidor backend está en Render con plan gratuito. Tras un tiempo sin uso entra en reposo. La primera carga puede tardar 30-60 segundos. Espera y vuelve a intentarlo."
            },
            {
                "¿Qué rol tengo al registrarme?",
                "Los nuevos usuarios se registran como COORDINADOR. Solo un administrador puede elevar una cuenta a ADMIN."
            },
            {
                "¿Qué versión de Android necesito?",
                "Android 7.0 o superior (API 24)."
            },
        }),
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ayuda, container, false);
        LinearLayout contenedor = view.findViewById(R.id.container_secciones);

        for (Seccion seccion : SECCIONES) {
            View seccionView = inflater.inflate(R.layout.item_ayuda_seccion, contenedor, false);

            TextView tvTitulo = seccionView.findViewById(R.id.tv_titulo_seccion);
            LinearLayout header = seccionView.findViewById(R.id.header_seccion);
            LinearLayout contenedorPreguntas = seccionView.findViewById(R.id.container_preguntas);
            ImageView ivChevron = seccionView.findViewById(R.id.iv_chevron);

            tvTitulo.setText(seccion.titulo);

            for (String[] item : seccion.items) {
                View preguntaView = inflater.inflate(R.layout.item_ayuda_pregunta, contenedorPreguntas, false);
                ((TextView) preguntaView.findViewById(R.id.tv_pregunta)).setText(item[0]);
                ((TextView) preguntaView.findViewById(R.id.tv_respuesta)).setText(item[1]);
                contenedorPreguntas.addView(preguntaView);
            }

            header.setOnClickListener(v -> {
                boolean estaAbierto = contenedorPreguntas.getVisibility() == View.VISIBLE;
                contenedorPreguntas.setVisibility(estaAbierto ? View.GONE : View.VISIBLE);
                ivChevron.animate().rotation(estaAbierto ? 0f : 180f).setDuration(200).start();
            });

            contenedor.addView(seccionView);
        }

        return view;
    }
}
