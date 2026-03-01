package com.example.proyectoarboles.util;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Collections;
import java.util.Set;

/**
 * Gestor centralizado de permisos y roles del usuario.
 * Proporciona métodos para validar si un usuario puede realizar acciones específicas
 * basándose en su rol (PUBLICO, COORDINADOR, ADMIN) y los centros asignados.
 *
 * Roles:
 * - PUBLICO: Sin cuenta, solo lectura
 * - COORDINADOR: Puede gestionar solo sus centros/árboles asignados
 * - ADMIN: Control total sobre todo
 */
public class PermissionManager {

    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_ROLE = "user_role";
    private static final String KEY_USER_CENTROS = "user_centros";

    private static final String ROL_PUBLICO = "PUBLICO";
    private static final String ROL_COORDINADOR = "COORDINADOR";
    private static final String ROL_ADMIN = "ADMIN";

    private final SharedPreferences sharedPreferences;

    public PermissionManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
    }

    // ========== MÉTODOS DE SESIÓN ==========

    /**
     * Verifica si hay una sesión activa
     */
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    /**
     * Obtiene el ID del usuario actual
     */
    public Long getUserId() {
        long userId = sharedPreferences.getLong(KEY_USER_ID, -1);
        return userId != -1 ? userId : null;
    }

    /**
     * Obtiene el rol actual del usuario
     * Retorna "PUBLICO" si no hay sesión activa
     */
    public String getCurrentRole() {
        if (!isLoggedIn()) {
            return ROL_PUBLICO;
        }
        String role = sharedPreferences.getString(KEY_USER_ROLE, ROL_PUBLICO);
        return role != null ? role : ROL_PUBLICO;
    }

    /**
     * Obtiene la lista de IDs de centros asignados al usuario (para coordinadores)
     */
    public Set<String> getUserCentrosIds() {
        return sharedPreferences.getStringSet(KEY_USER_CENTROS, Collections.emptySet());
    }

    /**
     * Verifica si el usuario es ADMIN
     */
    public boolean isAdmin() {
        return ROL_ADMIN.equals(getCurrentRole());
    }

    /**
     * Verifica si el usuario es COORDINADOR
     */
    public boolean isCoordinador() {
        return ROL_COORDINADOR.equals(getCurrentRole());
    }

    /**
     * Verifica si el usuario es PUBLICO (sin cuenta)
     */
    public boolean isPublico() {
        return ROL_PUBLICO.equals(getCurrentRole());
    }

    // ========== PERMISOS DE LECTURA ==========

    /**
     * Todos los roles pueden leer árboles (aunque públicos solo ven información básica)
     */
    public boolean puedeLeerArboles() {
        return true;
    }

    /**
     * Todos los roles pueden leer centros
     */
    public boolean puedeLeerCentros() {
        return true;
    }

    // ========== PERMISOS PARA ÁRBOLES ==========

    /**
     * Verifica si puede crear un árbol en un centro específico
     * - PUBLICO: No
     * - COORDINADOR: Solo en sus centros asignados
     * - ADMIN: Sí
     */
    public boolean puedeCrearArbol(Long centroId) {
        if (ROL_ADMIN.equals(getCurrentRole())) {
            return true;
        }
        if (ROL_COORDINADOR.equals(getCurrentRole())) {
            return esCentroAsignado(centroId);
        }
        return false;
    }

    /**
     * Verifica si puede crear un árbol (sin restricción de centro)
     * Útil para saber si mostrar botón FAB sin conocer el centro aún
     */
    public boolean puedeCrearArbol() {
        return (ROL_ADMIN.equals(getCurrentRole()) || ROL_COORDINADOR.equals(getCurrentRole()))
                && isLoggedIn();
    }

    /**
     * Verifica si puede editar un árbol en un centro específico
     * - PUBLICO: No
     * - COORDINADOR: Solo en sus centros asignados
     * - ADMIN: Sí
     */
    public boolean puedeEditarArbol(Long centroId) {
        if (ROL_ADMIN.equals(getCurrentRole())) {
            return true;
        }
        if (ROL_COORDINADOR.equals(getCurrentRole())) {
            return esCentroAsignado(centroId);
        }
        return false;
    }

    /**
     * Verifica si puede eliminar un árbol en un centro específico
     * - PUBLICO: No
     * - COORDINADOR: Solo en sus centros asignados
     * - ADMIN: Sí
     */
    public boolean puedeEliminarArbol(Long centroId) {
        if (ROL_ADMIN.equals(getCurrentRole())) {
            return true;
        }
        if (ROL_COORDINADOR.equals(getCurrentRole())) {
            return esCentroAsignado(centroId);
        }
        return false;
    }

    // ========== PERMISOS PARA CENTROS ==========

    /**
     * Verifica si puede crear un centro
     * - PUBLICO: No
     * - COORDINADOR: No
     * - ADMIN: Sí
     */
    public boolean puedeCrearCentro() {
        return ROL_ADMIN.equals(getCurrentRole());
    }

    /**
     * Verifica si puede editar un centro específico
     * - PUBLICO: No
     * - COORDINADOR: Solo si es uno de sus centros asignados
     * - ADMIN: Sí
     */
    public boolean puedeEditarCentro(Long centroId) {
        if (ROL_ADMIN.equals(getCurrentRole())) {
            return true;
        }
        if (ROL_COORDINADOR.equals(getCurrentRole())) {
            return esCentroAsignado(centroId);
        }
        return false;
    }

    /**
     * Verifica si puede eliminar un centro
     * - PUBLICO: No
     * - COORDINADOR: No
     * - ADMIN: Sí
     */
    public boolean puedeEliminarCentro(Long centroId) {
        // Solo ADMIN puede eliminar centros
        return ROL_ADMIN.equals(getCurrentRole());
    }

    // ========== MÉTODOS AUXILIARES ==========

    /**
     * Verifica si un centro está asignado al usuario coordinador
     */
    private boolean esCentroAsignado(Long centroId) {
        if (centroId == null) {
            return false;
        }
        Set<String> centrosAsignados = getUserCentrosIds();
        return centrosAsignados.contains(String.valueOf(centroId));
    }

    /**
     * Limpia la sesión del usuario
     */
    public void clearSession() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * Obtiene una descripción legible del rol actual
     */
    public String getRoleDisplayName() {
        String role = getCurrentRole();
        switch (role) {
            case ROL_ADMIN:
                return "Administrador";
            case ROL_COORDINADOR:
                return "Coordinador";
            case ROL_PUBLICO:
            default:
                return "Usuario Público";
        }
    }
}
