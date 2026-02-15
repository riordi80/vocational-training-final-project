package com.example.proyectoarboles.model;

import com.google.gson.annotations.SerializedName;

public class CentroEducativo {
    private Long id;
    private String nombre;
    private String isla;
    private String poblacion;
    private String provincia;

    @SerializedName("codigoPostal")
    private String codigoPostal;

    private String telefono;
    private String email;

    public Long getId() {return id;}
    public String getNombre() {return nombre;}
    public String getIsla() {return isla;}
    public String getPoblacion() {return poblacion;}
    public String getProvincia() {return provincia;}
    public String getCodigoPostal() {return codigoPostal;}
    public String getTelefono() {return telefono;}
    public String getEmail() {return email;}

    public void setId(Long id) {this.id = id;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public void setIsla(String isla) {this.isla = isla;}
    public void setPoblacion(String poblacion) {this.poblacion = poblacion;}
    public void setProvincia(String provincia) {this.provincia = provincia;}
    public void setCodigoPostal(String codigoPostal) {this.codigoPostal = codigoPostal;}
    public void setTelefono(String telefono) {this.telefono = telefono;}
    public void setEmail(String email) {this.email = email;}

    @Override
    public String toString() {return nombre;}
}
