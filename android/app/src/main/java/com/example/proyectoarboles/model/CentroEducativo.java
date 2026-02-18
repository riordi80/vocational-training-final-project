package com.example.proyectoarboles.model;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;

public class CentroEducativo {

    @SerializedName("id")
    private Long id;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("direccion")
    private String direccion;

    @SerializedName("latitud")
    private BigDecimal latitud;

    @SerializedName("longitud")
    private BigDecimal longitud;

    @SerializedName("responsable")
    private String responsable;

    @SerializedName("isla")
    private String isla;

    @SerializedName("poblacion")
    private String poblacion;

    @SerializedName("provincia")
    private String provincia;

    @SerializedName("codigoPostal")
    private String codigoPostal;

    @SerializedName("telefono")
    private String telefono;

    @SerializedName("email")
    private String email;

    // Getters
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public BigDecimal getLatitud() { return latitud; }
    public BigDecimal getLongitud() { return longitud; }
    public String getResponsable() { return responsable; }
    public String getIsla() { return isla; }
    public String getPoblacion() { return poblacion; }
    public String getProvincia() { return provincia; }
    public String getCodigoPostal() { return codigoPostal; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setLatitud(BigDecimal latitud) { this.latitud = latitud; }
    public void setLongitud(BigDecimal longitud) { this.longitud = longitud; }
    public void setResponsable(String responsable) { this.responsable = responsable; }
    public void setIsla(String isla) { this.isla = isla; }
    public void setPoblacion(String poblacion) { this.poblacion = poblacion; }
    public void setProvincia(String provincia) { this.provincia = provincia; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return nombre; // Para que funcione bien en los Spinners
    }
}
