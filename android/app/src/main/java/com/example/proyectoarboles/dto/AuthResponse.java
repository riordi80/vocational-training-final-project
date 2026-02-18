package com.example.proyectoarboles.dto;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AuthResponse {

    @SerializedName("id")
    private Long id;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("email")
    private String email;

    @SerializedName("rol")
    private String rol;

    @SerializedName("centros")
    private List<CentroRef> centros;

    // Getters
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getRol() {
        return rol;
    }

    public List<CentroRef> getCentros() {
        return centros;
    }

    /**
     * Clase anidada para la referencia de los centros
     */
    public static class CentroRef {
        @SerializedName("id")
        private Long id;

        public Long getId() {
            return id;
        }
    }
}
