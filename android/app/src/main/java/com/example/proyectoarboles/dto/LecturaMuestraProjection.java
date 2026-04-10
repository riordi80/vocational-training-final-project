package com.example.proyectoarboles.dto;

import com.google.gson.annotations.SerializedName;

/**
 * DTO que representa una lectura muestreada para gráficas.
 * Se usa en el endpoint GET /api/lecturas/arbol/{arbolId}/grafica
 * que devuelve máximo ~400 puntos mediante stride sampling.
 */
public class LecturaMuestraProjection {

    @SerializedName("id")
    private Long id;

    @SerializedName("timestamp")
    private String timestamp;  // ISO 8601 format

    @SerializedName("temperatura")
    private Double temperatura;  // °C

    @SerializedName("humedadAmbiente")
    private Double humedadAmbiente;  // %

    @SerializedName("humedadSuelo")
    private Double humedadSuelo;  // %

    @SerializedName("co2")
    private Double co2;  // ppm (puede ser null)

    @SerializedName("luz1")
    private Double luz1;  // % (puede ser null)

    @SerializedName("luz2")
    private Double luz2;  // % (puede ser null)

    // Constructores
    public LecturaMuestraProjection() {
    }

    public LecturaMuestraProjection(Long id, String timestamp, Double temperatura,
                                     Double humedadAmbiente, Double humedadSuelo, Double co2) {
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

    public Double getLuz1() {
        return luz1;
    }

    public Double getLuz2() {
        return luz2;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
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

    public void setLuz1(Double luz1) {
        this.luz1 = luz1;
    }

    public void setLuz2(Double luz2) {
        this.luz2 = luz2;
    }

    @Override
    public String toString() {
        return "LecturaMuestraProjection{" +
                "id=" + id +
                ", timestamp='" + timestamp + '\'' +
                ", temperatura=" + temperatura +
                ", humedadAmbiente=" + humedadAmbiente +
                ", humedadSuelo=" + humedadSuelo +
                ", co2=" + co2 +
                '}';
    }
}
