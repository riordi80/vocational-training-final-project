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
    private long id;

    @Column(name="mac_address", unique = true, nullable = false)
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "Solo se permiten letras y números")
    private String macAddress;

    /*
     * En la relacion OneToOne dispositivo esp32 deberia borrarse si se borra un arbol
     * por lo tanto arbol es el 'owner' de la relacion por eso ponemos mappedby dispositivo_esp32
     * 
     */
    @OneToOne(mappedBy = "dispositivo_esp32")
    private Arbol arbol;

    @Column(name = "activo", nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private boolean activo;

    @Column(name = "ultima_conexion", columnDefinition = "TIMESTAMPTZ")
    private LocalDateTime ultimaConexion;
    private int frecuenciaLecturaMin;

    public DispositivoEsp32() {
    }

    public DispositivoEsp32(String macAddress, Arbol arbol, boolean activo, int frecuenciaLecturaMin) {
        this.macAddress = macAddress;
        this.arbol = arbol;
        this.activo = activo;
        this.frecuenciaLecturaMin = frecuenciaLecturaMin;
    }

    public long getId() {return id;}
    public String getMacAddress() {return macAddress;}
    public Arbol getArbolId() {return arbol;}
    public boolean isActivo() {return activo;}
    public LocalDateTime getUltimaConexion() {return ultimaConexion;}
    public int getFrecuenciaLecturaMin() {return frecuenciaLecturaMin;}

    public void setMacAddress(String macAddress) {this.macAddress = macAddress;}
    public void setArbolId(Arbol arbol) {this.arbol = arbol;}
    public void setActivo(boolean activo) {this.activo = activo;}
    public void setUltimaConexion(LocalDateTime ultimaConexion) {this.ultimaConexion = ultimaConexion;}
    public void setFrecuenciaLecturaMin(int frecuenciaLecturaMin) {this.frecuenciaLecturaMin = frecuenciaLecturaMin;}

    protected void onCreate(){
        if (ultimaConexion == null) {
            ultimaConexion = LocalDateTime.now();
        }
    }

}
