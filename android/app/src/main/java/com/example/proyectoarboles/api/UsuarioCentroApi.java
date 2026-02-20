package com.example.proyectoarboles.api;

import com.example.proyectoarboles.model.CentroEducativo;
import com.example.proyectoarboles.model.UsuarioCentro;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import java.util.List;

/**
 * Interface Retrofit para operaciones de relación Usuario-Centro.
 * Base URL: /api/usuario-centro
 *
 * Permite gestionar la relación N:M entre usuarios (coordinadores) y centros educativos.
 */
public interface UsuarioCentroApi {

    /**
     * Obtiene todos los centros asignados a un usuario
     * GET /api/usuario-centro/usuario/{usuarioId}
     *
     * @param usuarioId ID del usuario
     */
    @GET("api/usuario-centro/usuario/{usuarioId}")
    Call<List<CentroEducativo>> obtenerCentrosPorUsuario(@Path("usuarioId") Long usuarioId);

    /**
     * Obtiene todos los coordinadores asignados a un centro
     * GET /api/usuario-centro/centro/{centroId}
     *
     * @param centroId ID del centro educativo
     */
    @GET("api/usuario-centro/centro/{centroId}")
    Call<List<UsuarioCentro>> obtenerCoordinadoresPorCentro(@Path("centroId") Long centroId);

    /**
     * Asigna un usuario (coordinador) a un centro educativo
     * POST /api/usuario-centro
     *
     * Ejemplo de body:
     * {
     *   "usuarioId": 1,
     *   "centroId": 5
     * }
     */
    @POST("api/usuario-centro")
    Call<UsuarioCentro> asignarUsuarioACentro(@Body AsignacionUsuarioCentro asignacion);

    /**
     * Desasigna un usuario de un centro (elimina la relación)
     * DELETE /api/usuario-centro/{id}
     *
     * @param id ID de la relación usuario-centro
     */
    @DELETE("api/usuario-centro/{id}")
    Call<Void> desasigniar(@Path("id") Long id);

    /**
     * DTO para solicitud de asignación de usuario a centro
     */
    class AsignacionUsuarioCentro {
        private Long usuarioId;
        private Long centroId;

        public AsignacionUsuarioCentro(Long usuarioId, Long centroId) {
            this.usuarioId = usuarioId;
            this.centroId = centroId;
        }

        public Long getUsuarioId() {
            return usuarioId;
        }

        public void setUsuarioId(Long usuarioId) {
            this.usuarioId = usuarioId;
        }

        public Long getCentroId() {
            return centroId;
        }

        public void setCentroId(Long centroId) {
            this.centroId = centroId;
        }
    }
}
