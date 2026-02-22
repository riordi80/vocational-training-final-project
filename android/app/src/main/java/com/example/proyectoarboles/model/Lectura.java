package com.example.proyectoarboles.model;

import com.google.gson.annotations.SerializedName;

/**
 * Modelo de Lectura correspondiente al backend.
 * Representa una lectura de sensores de un árbol en un momento específico.
 * Los datos vienen de sensores IoT ESP32 asociados al árbol.
 */
public class Lectura {

    @SerializedName("id")
    private Long id;

    @SerializedName("timestamp")
    private String timestamp;  // ISO 8601 format: "2025-02-20T14:30:45"

    @SerializedName("arbol")
    private Arbol arbol;

    @SerializedName("temperatura")
    private Double temperatura;  // En °C

    @SerializedName("humedadAmbiente")
    private Double humedadAmbiente;  // En %

    @SerializedName("humedadSuelo")
    private Double humedadSuelo;  // En %

    @SerializedName("co2")
    private Double co2;  // En ppm (puede ser null)

    @SerializedName("diametroTronco")
    private Double diametroTronco;  // En mm (puede ser null)

    // Constructores
    public Lectura() {
    }

    public Lectura(Long id, String timestamp, Double temperatura, Double humedadAmbiente,
                   Double humedadSuelo, Double co2) {
        this.id = id;
        this.timestamp = timestamp;
        this.temperatura = temperatura;
        this.humedadAmbiente = humedadAmbiente;
        this.humedadSuelo = humedadSuelo;
        this.co2 = co2;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Arbol getArbol() {
        return arbol;
    }

    public Double getTemperatura() {
        return temperatura;
    }

    public Double getHumedadAmbiente() {
        return humedadAmbiente;
    }

    public Double getHumedadSuelo() {
        return humedadSuelo;
    }

    public Double getCo2() {
        return co2;
    }

    public Double getDiametroTronco() {
        return diametroTronco;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setArbol(Arbol arbol) {
        this.arbol = arbol;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    public void setHumedadAmbiente(Double humedadAmbiente) {
        this.humedadAmbiente = humedadAmbiente;
    }

    public void setHumedadSuelo(Double humedadSuelo) {
        this.humedadSuelo = humedadSuelo;
    }

    public void setCo2(Double co2) {
        this.co2 = co2;
    }

    public void setDiametroTronco(Double diametroTronco) {
        this.diametroTronco = diametroTronco;
    }

    @Override
    public String toString() {
        return "Lectura{" +
                "id=" + id +
                ", timestamp='" + timestamp + '\'' +
                ", temperatura=" + temperatura +
                ", humedadAmbiente=" + humedadAmbiente +
                ", humedadSuelo=" + humedadSuelo +
                ", co2=" + co2 +
                '}';
    }
}
