package com.example.proyectoarboles.model;

import com.google.gson.annotations.SerializedName;

/**
 * Modelo de Usuario correspondiente al backend.
 * Representa un usuario del sistema con roles ADMIN o COORDINADOR.
 */
public class Usuario {

    @SerializedName("id")
    private Long id;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("email")
    private String email;

    @SerializedName("passwordHash")
    private String passwordHash;

    @SerializedName("rol")
    private String rol;  // "ADMIN" o "COORDINADOR"

    @SerializedName("fechaCreacion")
    private String fechaCreacion;  // ISO: "yyyy-MM-ddTHH:mm:ss"

    @SerializedName("activo")
    private Boolean activo;

    // Constructores
    public Usuario() {
    }

    public Usuario(String nombre, String email, String rol) {
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
    }

    public Usuario(String nombre, String email, String rol, Boolean activo) {
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
        this.activo = activo;
    }

    public Usuario(Long id, String nombre, String email, String rol, String fechaCreacion, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
        this.fechaCreacion = fechaCreacion;
        this.activo = activo;
    }

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

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getRol() {
        return rol;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public Boolean getActivo() {
        return activo;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", rol='" + rol + '\'' +
                ", activo=" + activo +
                '}';
    }
}
