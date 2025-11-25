package com.example.proyectoarboles.model;

public class Arbol {
    private Long id;
    private String nombre;
    private String especie;
    private String fechaPlantacion;

    public Long getId() {return id;}
    public String getNombre() {return nombre;}
    public String getEspecie() {return especie;}
    public String getFechaPlantacion() {return fechaPlantacion;}

    public void setId(Long id) {this.id = id;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public void setEspecie(String especie) {this.especie = especie;}
    public void setFechaPlantacion(String fechaPlantacion) {this.fechaPlantacion = fechaPlantacion;}

}
