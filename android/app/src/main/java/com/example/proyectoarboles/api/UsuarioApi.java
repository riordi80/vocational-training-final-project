package com.example.proyectoarboles.api;

import com.example.proyectoarboles.model.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import java.util.List;

/**
 * Interface Retrofit para operaciones CRUD de Usuarios.
 * Base URL: /api/usuarios
 */
public interface UsuarioApi {

    // GET endpoints

    /**
     * Obtiene la lista de todos los usuarios
     * GET /api/usuarios
     */
    @GET("api/usuarios")
    Call<List<Usuario>> obtenerTodos();

    /**
     * Obtiene un usuario por su ID
     * GET /api/usuarios/{id}
     */
    @GET("api/usuarios/{id}")
    Call<Usuario> obtenerPorId(@Path("id") Long id);

    /**
     * Obtiene un usuario por su email
     * GET /api/usuarios/email/{email}
     */
    @GET("api/usuarios/email/{email}")
    Call<Usuario> obtenerPorEmail(@Path("email") String email);

    /**
     * Obtiene la lista de usuarios activos
     * GET /api/usuarios/activos
     */
    @GET("api/usuarios/activos")
    Call<List<Usuario>> obtenerActivos();

    /**
     * Obtiene la lista de usuarios inactivos
     * GET /api/usuarios/inactivos
     */
    @GET("api/usuarios/inactivos")
    Call<List<Usuario>> obtenerInactivos();

    /**
     * Obtiene usuarios filtrados por rol
     * GET /api/usuarios/rol/{rol}
     * @param rol Nombre del rol (ADMIN o COORDINADOR)
     */
    @GET("api/usuarios/rol/{rol}")
    Call<List<Usuario>> obtenerPorRol(@Path("rol") String rol);

    // POST endpoint

    /**
     * Crea un nuevo usuario
     * POST /api/usuarios
     */
    @POST("api/usuarios")
    Call<Usuario> crear(@Body Usuario usuario);

    // PUT endpoint

    /**
     * Actualiza un usuario existente
     * PUT /api/usuarios/{id}
     */
    @PUT("api/usuarios/{id}")
    Call<Usuario> actualizar(@Path("id") Long id, @Body Usuario usuario);

    // DELETE endpoint

    /**
     * Elimina un usuario
     * DELETE /api/usuarios/{id}
     */
    @DELETE("api/usuarios/{id}")
    Call<Void> eliminar(@Path("id") Long id);

    // PATCH endpoints para cambios de estado

    /**
     * Desactiva un usuario
     * PATCH /api/usuarios/{id}/desactivar
     */
    @PATCH("api/usuarios/{id}/desactivar")
    Call<Usuario> desactivar(@Path("id") Long id);

    /**
     * Activa un usuario
     * PATCH /api/usuarios/{id}/activar
     */
    @PATCH("api/usuarios/{id}/activar")
    Call<Usuario> activar(@Path("id") Long id);
}
