package com.example.gardenmonitor.dto;

import java.util.List;

public class AuthResponse {

    private Long id;
    private String nombre;
    private String email;
    private String rol;
    private List<CentroRef> centros;

    public AuthResponse() {}

    public AuthResponse(Long id, String nombre, String email, String rol, List<CentroRef> centros) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
        this.centros = centros;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public List<CentroRef> getCentros() { return centros; }
    public void setCentros(List<CentroRef> centros) { this.centros = centros; }

    public static class CentroRef {
        private Long centroId;

        public CentroRef() {}

        public CentroRef(Long centroId) {
            this.centroId = centroId;
        }

        public Long getCentroId() { return centroId; }
        public void setCentroId(Long centroId) { this.centroId = centroId; }
    }
}
