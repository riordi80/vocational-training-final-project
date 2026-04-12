package com.example.proyectoarboles.model;

import com.google.gson.annotations.SerializedName;

public class DispositivoEsp32 {

    @SerializedName("id")
    private Long id;

    @SerializedName("macAddress")
    private String macAddress;

    @SerializedName("activo")
    private Boolean activo;

    @SerializedName("frecuenciaLecturaSeg")
    private Integer frecuenciaLecturaSeg;

    @SerializedName("ultimaConexion")
    private String ultimaConexion;

    @SerializedName("centroEducativo")
    private CentroEducativo centroEducativo;

    @SerializedName("umbralTempMin")
    private Double umbralTempMin;

    @SerializedName("umbralTempMax")
    private Double umbralTempMax;

    @SerializedName("umbralHumedadAmbienteMin")
    private Double umbralHumedadAmbienteMin;

    @SerializedName("umbralHumedadAmbienteMax")
    private Double umbralHumedadAmbienteMax;

    @SerializedName("umbralHumedadSueloMin")
    private Double umbralHumedadSueloMin;

    @SerializedName("umbralCO2Max")
    private Double umbralCO2Max;

    public DispositivoEsp32() {}

    // Getters
    public Long getId() { return id; }
    public String getMacAddress() { return macAddress != null ? macAddress : "Sin MAC"; }
    public Boolean getActivo() { return activo; }
    public Integer getFrecuenciaLecturaSeg() { return frecuenciaLecturaSeg; }
    public String getUltimaConexion() { return ultimaConexion; }
    public CentroEducativo getCentroEducativo() { return centroEducativo; }
    public Double getUmbralTempMin() { return umbralTempMin; }
    public Double getUmbralTempMax() { return umbralTempMax; }
    public Double getUmbralHumedadAmbienteMin() { return umbralHumedadAmbienteMin; }
    public Double getUmbralHumedadAmbienteMax() { return umbralHumedadAmbienteMax; }
    public Double getUmbralHumedadSueloMin() { return umbralHumedadSueloMin; }
    public Double getUmbralCO2Max() { return umbralCO2Max; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setMacAddress(String macAddress) { this.macAddress = macAddress; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public void setFrecuenciaLecturaSeg(Integer frecuenciaLecturaSeg) { this.frecuenciaLecturaSeg = frecuenciaLecturaSeg; }
    public void setUltimaConexion(String ultimaConexion) { this.ultimaConexion = ultimaConexion; }
    public void setCentroEducativo(CentroEducativo centroEducativo) { this.centroEducativo = centroEducativo; }
    public void setUmbralTempMin(Double umbralTempMin) { this.umbralTempMin = umbralTempMin; }
    public void setUmbralTempMax(Double umbralTempMax) { this.umbralTempMax = umbralTempMax; }
    public void setUmbralHumedadAmbienteMin(Double umbralHumedadAmbienteMin) { this.umbralHumedadAmbienteMin = umbralHumedadAmbienteMin; }
    public void setUmbralHumedadAmbienteMax(Double umbralHumedadAmbienteMax) { this.umbralHumedadAmbienteMax = umbralHumedadAmbienteMax; }
    public void setUmbralHumedadSueloMin(Double umbralHumedadSueloMin) { this.umbralHumedadSueloMin = umbralHumedadSueloMin; }
    public void setUmbralCO2Max(Double umbralCO2Max) { this.umbralCO2Max = umbralCO2Max; }

    @Override
    public String toString() {
        return macAddress != null ? macAddress : "Dispositivo";
    }
}
