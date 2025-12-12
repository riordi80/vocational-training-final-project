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

    @SerializedName("temperatura")
    private String temperatura;

    @SerializedName("humedad")
    private String humedad;

    @SerializedName("humedadSuelo")
    private String humedadSuelo;

    @SerializedName("co2")
    private String co2;

    // Constructor básico (para XML/fallback)
    public Arbol(Long id, String nombre, String especie, String fechaPlantacion){
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.fechaPlantacion = fechaPlantacion;
    }

    // Constructor completo
    public Arbol(Long id, String nombre, String especie, String fechaPlantacion,
                 String ubicacion, String temperatura, String humedad,
                 String humedadSuelo, String co2){
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.fechaPlantacion = fechaPlantacion;
        this.ubicacion = ubicacion;
        this.temperatura = temperatura;
        this.humedad = humedad;
        this.humedadSuelo = humedadSuelo;
        this.co2 = co2;
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

    public String getTemperatura() {
        return temperatura;
    }

    public String getHumedad() {
        return humedad;
    }

    public String getHumedadSuelo() {
        return humedadSuelo;
    }

    public String getCo2() {
        return co2;
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

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public void setHumedad(String humedad) {
        this.humedad = humedad;
    }

    public void setHumedadSuelo(String humedadSuelo) {
        this.humedadSuelo = humedadSuelo;
    }

    public void setCo2(String co2) {
        this.co2 = co2;
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