package com.example.proyectoarboles.model;

import com.google.gson.annotations.SerializedName;

/**
 * Modelo que representa la relación N:M entre Usuario y CentroEducativo.
 * Un usuario puede estar asignado a múltiples centros.
 */
public class UsuarioCentro {

    @SerializedName("id")
    private Long id;

    @SerializedName("usuario")
    private Usuario usuario;

    @SerializedName("centroEducativo")
    private CentroEducativo centroEducativo;

    @SerializedName("fechaAsignacion")
    private String fechaAsignacion;  // ISO: "yyyy-MM-ddTHH:mm:ss"

    @SerializedName("activo")
    private Boolean activo;

    // Constructores
    public UsuarioCentro() {
    }

    public UsuarioCentro(Usuario usuario, CentroEducativo centroEducativo) {
        this.usuario = usuario;
        this.centroEducativo = centroEducativo;
        this.activo = true;
    }

    public UsuarioCentro(Long id, Usuario usuario, CentroEducativo centroEducativo, String fechaAsignacion, Boolean activo) {
        this.id = id;
        this.usuario = usuario;
        this.centroEducativo = centroEducativo;
        this.fechaAsignacion = fechaAsignacion;
        this.activo = activo;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public CentroEducativo getCentroEducativo() {
        return centroEducativo;
    }

    public String getFechaAsignacion() {
        return fechaAsignacion;
    }

    public Boolean getActivo() {
        return activo;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setCentroEducativo(CentroEducativo centroEducativo) {
        this.centroEducativo = centroEducativo;
    }

    public void setFechaAsignacion(String fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "UsuarioCentro{" +
                "id=" + id +
                ", usuario=" + usuario +
                ", centroEducativo=" + centroEducativo +
                ", activo=" + activo +
                '}';
    }
}
