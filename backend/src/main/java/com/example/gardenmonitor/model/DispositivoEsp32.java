package com.example.gardenmonitor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * Entidad JPA que representa un dispositivo ESP32 en el sistema Garden Monitor.
 * <p>
 * Esta clase mapea la tabla 'dispositivo_esp32' de la base de datos PostgreSQL
 * y almacena información de los dispositivos ESP32 utilizados para la monitorización
 * de árboles mediante sensores (temperatura, humedad, CO2, etc.).
 * </p>
 *
 * @author Richard Ortiz y Enrique Pérez
 * @version 1.0
 */

@Entity
@Table(name = "dispositivo_esp32", indexes = {
        @Index(name = "idx_dispositivo_esp32_activo", columnList = "activo")
})
public class DispositivoEsp32 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Dirección MAC del dispositivo ESP32.
     * <p>
     * Formato: XX:XX:XX:XX:XX:XX (donde X es un dígito hexadecimal).
     * Debe ser única en el sistema.
     * </p>
     */
    @Column(name="mac_address", unique = true, nullable = false)
    @Pattern(regexp = "^([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}$")
    private String macAddress;

    /**
     * Árbol asociado al dispositivo ESP32.
     * <p>
     * Relación One-to-One bidireccional: un dispositivo monitoriza un único árbol.
     * </p>
     */
    @OneToOne(mappedBy = "dispositivoEsp32")
    private Arbol arbol;

    @Column(name = "activo", nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private boolean activo;

    /**
     * Fecha y hora de la última conexión del dispositivo.
     * <p>
     * Se establece automáticamente al crear el dispositivo mediante @PrePersist.
     * Se actualiza cada vez que el dispositivo envía datos al sistema.
     * </p>
     */
    @Column(name = "ultima_conexion", columnDefinition = "TIMESTAMPTZ")
    private LocalDateTime ultimaConexion;

    /**
     * Frecuencia de lectura de sensores en minutos.
     * <p>
     * Define cada cuántos minutos el ESP32 debe tomar mediciones.
     * Por defecto: 15 minutos.
     * </p>
     */
    @Column(name = "frecuencia_lectura_min", columnDefinition = "INTEGER DEFAULT 15")
    private int frecuenciaLecturaMin;

    /**
     * Constructor vacío requerido por JPA.
     */
    public DispositivoEsp32() {}

    /**
     * Constructor para crear un nuevo dispositivo ESP32.
     * <p>
     * El campo id y ultimaConexion se establecen automáticamente.
     * </p>
     *
     * @param macAddress dirección MAC del dispositivo (formato XX:XX:XX:XX:XX:XX)
     * @param arbol árbol asociado al dispositivo (puede ser null)
     * @param activo indica si el dispositivo está activo
     * @param frecuenciaLecturaMin frecuencia de lectura en minutos (por defecto 15)
     */
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

     public void setId(Long id) {this.id = id;}
    public void setMacAddress(String macAddress) {this.macAddress = macAddress;}
    public void setArbol(Arbol arbol) {this.arbol = arbol;}
    public void setActivo(boolean activo) {this.activo = activo;}
    public void setUltimaConexion(LocalDateTime ultimaConexion) {this.ultimaConexion = ultimaConexion;}
    public void setFrecuenciaLecturaMin(int frecuenciaLecturaMin) {this.frecuenciaLecturaMin = frecuenciaLecturaMin;}

    /**
     * Callback ejecutado antes de persistir un nuevo dispositivo ESP32 en la base de datos.
     * <p>
     * Establece la fecha y hora de última conexión automáticamente si no ha sido proporcionada:
     * </p>
     * <ul>
     *   <li>ultimaConexion: fecha y hora actual si es null</li>
     * </ul>
     */
    @PrePersist
    protected void onCreate(){
        if (ultimaConexion == null) {
            ultimaConexion = LocalDateTime.now();
        }
    }

}
