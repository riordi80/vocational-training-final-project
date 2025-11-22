package com.example.gardenmonitor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;


@Entity
@Table(name = "dispositivo_esp32", indexes = {
        @Index(name = "idx_dispositivo_esp32_activo", columnList = "activo")
})
public class DispositivoEsp32 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="mac_address", unique = true, nullable = false)
    @Pattern(regexp = "^([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}$")
    private String macAddress;

    @OneToOne(mappedBy = "dispositivoEsp32")
    private Arbol arbol;

    @Column(name = "activo", nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private boolean activo;

    @Column(name = "ultima_conexion", columnDefinition = "TIMESTAMPTZ")
    private LocalDateTime ultimaConexion;

    @Column(name = "frecuencia_lectura_min", columnDefinition = "INTEGER DEFAULT 15")
    private int frecuenciaLecturaMin;

    public DispositivoEsp32() {}

    public DispositivoEsp32(String macAddress, Arbol arbol, boolean activo, int frecuenciaLecturaMin) {
        this.macAddress = macAddress;
        this.arbol = arbol;
        this.activo = activo;
        this.frecuenciaLecturaMin = frecuenciaLecturaMin;
    }

    public long getId() {return id;}
    public String getMacAddress() {return macAddress;}
    public Arbol getArbol() {return arbol;}
    public boolean isActivo() {return activo;}
    public LocalDateTime getUltimaConexion() {return ultimaConexion;}
    public int getFrecuenciaLecturaMin() {return frecuenciaLecturaMin;}

    public void setMacAddress(String macAddress) {this.macAddress = macAddress;}
    public void setArbol(Arbol arbol) {this.arbol = arbol;}
    public void setActivo(boolean activo) {this.activo = activo;}
    public void setUltimaConexion(LocalDateTime ultimaConexion) {this.ultimaConexion = ultimaConexion;}
    public void setFrecuenciaLecturaMin(int frecuenciaLecturaMin) {this.frecuenciaLecturaMin = frecuenciaLecturaMin;}


    @PrePersist
    protected void onCreate(){
        if (ultimaConexion == null) {
            ultimaConexion = LocalDateTime.now();
        }
    }

}
