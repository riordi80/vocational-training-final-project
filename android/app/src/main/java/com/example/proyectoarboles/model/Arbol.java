package com.example.proyectoarboles.model;

import com.google.gson.annotations.SerializedName;

public class Arbol {

    @SerializedName("id")
    private Long id;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("especie")
    private String especie;

    @SerializedName("fechaPlantacion")
    private String fechaPlantacion;

    @SerializedName("ubicacionEspecifica")
    private String ubicacion;

    @SerializedName("centroEducativo")
    private CentroEducativo centroEducativo;

    @SerializedName("absorcionCo2Anual")
    private Double absorcionCo2Anual;

    // Constructor básico (para XML/fallback)
    public Arbol(Long id, String nombre, String especie, String fechaPlantacion){
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.fechaPlantacion = fechaPlantacion;
    }

    // Constructor vacío (necesario para Gson)
    public Arbol() {}

    // Getters
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre != null ? nombre : "Sin nombre";
    }

    public String getEspecie() {
        return especie != null ? especie : "Desconocida";
    }

    public String getFechaPlantacion() {
        return fechaPlantacion != null ? fechaPlantacion : "No disponible";
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public CentroEducativo getCentroEducativo() {
        return centroEducativo;
    }

    public Double getAbsorcionCo2Anual() {
        return absorcionCo2Anual;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public void setFechaPlantacion(String fechaPlantacion) {
        this.fechaPlantacion = fechaPlantacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setCentroEducativo(CentroEducativo centroEducativo) {
        this.centroEducativo = centroEducativo;
    }

    public void setAbsorcionCo2Anual(Double absorcionCo2Anual) {
        this.absorcionCo2Anual = absorcionCo2Anual;
    }

    @Override
    public String toString() {
        return "Arbol{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", especie='" + especie + '\'' +
                ", fechaPlantacion='" + fechaPlantacion + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                '}';
    }
}