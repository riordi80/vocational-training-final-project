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

    private String temperatura;
    private String humedad;
    private String humedadSuelo;
    private String co2;

    public Arbol(Long id, String nombre, String especie, String fechaPlantacion){
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.fechaPlantacion = fechaPlantacion;
    }
    public Arbol(Long id, String nombre, String especie, String fechaPlantacion, String ubicacion, String temperatura, String humedad, String humedadSuelo,String co2){
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

    public Long getId() {return id;}
    public String getNombre() {return nombre;}
    public String getEspecie() {return especie;}
    public String getFechaPlantacion() {return fechaPlantacion;}

    public String getUbicacion() {return ubicacion;}

    public String getTemperatura() {return temperatura;}
    public String getHumedad() {return humedad;}

    public String getHumedadSuelo() {return humedadSuelo;}

    public String getCo2() {return co2;}

    public void setId(Long id) {this.id = id;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public void setEspecie(String especie) {this.especie = especie;}
    public void setFechaPlantacion(String fechaPlantacion) {this.fechaPlantacion = fechaPlantacion;}

    public void setUbicacion(String ubicacion) {this.ubicacion = ubicacion;}
    public void setTemperatura(String temperatura) {this.temperatura = temperatura;}
    public void setHumedad(String humedad) {this.humedad = humedad;}
    public void setHumedadSuelo(String humedadSuelo) {this.humedadSuelo = humedadSuelo;}
    public void setCo2(String co2) {this.co2 = co2;}
}
