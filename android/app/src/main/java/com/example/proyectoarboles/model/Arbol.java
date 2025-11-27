package com.example.proyectoarboles.model;

public class Arbol {
    private int id;
    private String nombre;
    private String especie;
    private String fechaPlantacion;

    public Arbol(int id, String nombre, String especie, String fechaPlantacion){
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.fechaPlantacion = fechaPlantacion;
    }

    public int getId() {return id;}
    public String getNombre() {return nombre;}
    public String getEspecie() {return especie;}
    public String getFechaPlantacion() {return fechaPlantacion;}

    public void setId(int id) {this.id = id;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public void setEspecie(String especie) {this.especie = especie;}
    public void setFechaPlantacion(String fechaPlantacion) {this.fechaPlantacion = fechaPlantacion;}

}
