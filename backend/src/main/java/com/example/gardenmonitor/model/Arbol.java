package com.example.gardenmonitor.model;

import java.math.BigDecimal;
import java.sql.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "arbol", indexes = {
    @Index(name = "idx_arvol_centro_educativo", columnList = "centro_educativo_id", unique = true),
    @Index(name = "idx_arbol_dispositivo_esp32", columnList = "dispositivo_esp32_id"),
    @Index(name = "idx_arbol_especie", columnList = "especie" )
})
public class Arbol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "centro_id")
    private CentroEducativo centroEducativo;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "especie", nullable = false, length = 150)
    private String especie;

    @Column(name = "fecha_plantacion", nullable = false)
    private Date fechaPlantacion;

    @Column(name = "ubicacion_especifica", length = 200)
    private String ubicacionEspecifica;

    @OneToOne
    @JoinColumn(name = "dispositivo_esp32_id", 
        foreignKey = @ForeignKey(
        name = "fk_dispositivo_esp32", 
        foreignKeyDefinition = "FOREIGN KEY (dispositivo_esp32_id) REFERENCES dispositivo_esp32(id) ON DELETE SET NULL"
        ))
    private DispositivoEsp32 dispositivoEsp32;

    @Column(name = "umbral_temp_min", columnDefinition = "DECIMAL DEFAULT 5.00")
    private BigDecimal umbralTempMin;

    @Column(name = "umbral_temp_max", columnDefinition = "DECIMAL DEFAULT 40.00")
    private BigDecimal umbralTempMax;

    @Column(name = "umbral_humedad_ambiente_min", columnDefinition = "DECIMAL DEFAULT 30.00")
    private BigDecimal umbralHumedadAmbienteMin;

    @Column(name = "umbral_humedad_ambiente_max", columnDefinition = "DECIMAL DEFAULT 90.00")
    private BigDecimal umbralHumedadAmbienteMax;

    @Column(name = "umbral_humedad_ambiente_suelo_min", columnDefinition = "DECIMAL DEFAULT 30.00")
    private BigDecimal umbralHumedadSueloMin;

    @Column(name = "umbral_co2_max", columnDefinition = "DECIMAL DEFAULT 1000.00")
    private BigDecimal umbralCO2Max;

    public Arbol(){}

    public Arbol(
        CentroEducativo centroEducativo, 
        String nombre, 
        String especie, 
        Date fechaPlantacion, 
        String ubicacionEspecifica, 
        DispositivoEsp32 dispositivoEsp32, 
        BigDecimal umbralTempMin, 
        BigDecimal umbralTempMax, 
        BigDecimal umbralHumedadAmbienteMin, 
        BigDecimal umbralHumedadAmbienteMax, 
        BigDecimal umbralHumedadSueloMin, 
        BigDecimal umbralCO2Max){

        this.centroEducativo = centroEducativo;
        this.nombre = nombre;
        this.especie = especie;
        this.fechaPlantacion = fechaPlantacion;
        this.ubicacionEspecifica = ubicacionEspecifica;
        this.dispositivoEsp32 = dispositivoEsp32;
        this.umbralTempMin = umbralTempMin;
        this.umbralTempMax = umbralTempMax;
        this.umbralHumedadAmbienteMin = umbralHumedadAmbienteMin;
        this.umbralHumedadAmbienteMax = umbralHumedadAmbienteMax;
        this.umbralHumedadSueloMin = umbralHumedadSueloMin;
        this.umbralCO2Max = umbralCO2Max;
    }

    public long getId() {return id;}
    public CentroEducativo getCentroEducativo() {return centroEducativo;}
    public String getNombre() {return nombre;}
    public String getEspecie() {return especie;}
    public Date getFechaPlantacion() {return fechaPlantacion;}
    public String getUbicacionEspecifica() {return ubicacionEspecifica;}
    public DispositivoEsp32 getDispositivoEsp32() {return dispositivoEsp32;}
    public BigDecimal getUmbralTempMin() {return umbralTempMin;}
    public BigDecimal getUmbralTempMax() {return umbralTempMax;}
    public BigDecimal getUmbralHumedadAmbienteMin() {return umbralHumedadAmbienteMin;}
    public BigDecimal getUmbralHumedadAmbienteMax() {return umbralHumedadAmbienteMax;}
    public BigDecimal getUmbralHumedadSueloMin() {return umbralHumedadSueloMin;}
    public BigDecimal getUmbralCO2Max() {return umbralCO2Max;}

    public void setCentroEducativo(CentroEducativo centroEducativo) {this.centroEducativo = centroEducativo;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public void setEspecie(String especie) {this.especie = especie;}
    public void setFechaPlantacion(Date fechaPlantacion) {this.fechaPlantacion = fechaPlantacion;}
    public void setUbicacionEspecifica(String ubicacionEspecifica) {this.ubicacionEspecifica = ubicacionEspecifica;}
    public void setDispositivoEsp32(DispositivoEsp32 dispositivoEsp32) {this.dispositivoEsp32 = dispositivoEsp32;}
    public void setUmbralTempMin(BigDecimal umbralTempMin) {this.umbralTempMin = umbralTempMin;}
    public void setUmbralTempMax(BigDecimal umbralTempMax) {this.umbralTempMax = umbralTempMax;}
    public void setUmbralHumedadAmbienteMin(BigDecimal umbralHumedadAmbienteMin) {this.umbralHumedadAmbienteMin = umbralHumedadAmbienteMin;}
    public void setUmbralHumedadAmbienteMax(BigDecimal umbralHumedadAmbienteMax) {this.umbralHumedadAmbienteMax = umbralHumedadAmbienteMax;}
    public void setUmbralHumedadSueloMin(BigDecimal umbralHumedadSueloMin) {this.umbralHumedadSueloMin = umbralHumedadSueloMin;}
    public void setUmbralCO2Max(BigDecimal umbralCO2Max) {this.umbralCO2Max = umbralCO2Max;}
}
