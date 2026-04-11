package com.example.gardenmonitor.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * Entidad JPA que representa un dispositivo ESP32 en el sistema Proyecto Árboles.
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
        @Index(name = "idx_dispositivo_esp32_activo", columnList = "activo"),
        @Index(name = "idx_dispositivo_centro", columnList = "centro_id")
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
     * Centro educativo al que pertenece este dispositivo ESP32.
     * <p>
     * Relación Many-to-One: un centro puede tener varios dispositivos.
     * Si se elimina el centro, se eliminan en cascada todos sus dispositivos.
     * </p>
     */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "centro_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CentroEducativo centroEducativo;

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
     * Frecuencia de lectura de sensores en segundos.
     * <p>
     * Define cada cuántos segundos el ESP32 debe tomar mediciones.
     * Por defecto: 30 segundos (fase de prototipo).
     * En producción: 900 segundos (15 minutos).
     * </p>
     */
    @Column(name = "frecuencia_lectura_seg", columnDefinition = "INTEGER DEFAULT 30")
    private int frecuenciaLecturaSeg;

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
     * @param centroEducativo centro educativo al que pertenece el dispositivo
     * @param activo indica si el dispositivo está activo
     * @param frecuenciaLecturaSeg frecuencia de lectura en segundos (por defecto 30)
     */
    public DispositivoEsp32(String macAddress, CentroEducativo centroEducativo, boolean activo, int frecuenciaLecturaSeg) {
        this.macAddress = macAddress;
        this.centroEducativo = centroEducativo;
        this.activo = activo;
        this.frecuenciaLecturaSeg = frecuenciaLecturaSeg;
    }

    public Long getId() {return id;}
    public String getMacAddress() {return macAddress;}
    public CentroEducativo getCentroEducativo() {return centroEducativo;}
    public boolean isActivo() {return activo;}
    public LocalDateTime getUltimaConexion() {return ultimaConexion;}
    public int getFrecuenciaLecturaSeg() {return frecuenciaLecturaSeg;}

    public void setId(Long id) {this.id = id;}
    public void setMacAddress(String macAddress) {this.macAddress = macAddress;}
    public void setCentroEducativo(CentroEducativo centroEducativo) {this.centroEducativo = centroEducativo;}
    public void setActivo(boolean activo) {this.activo = activo;}
    public void setUltimaConexion(LocalDateTime ultimaConexion) {this.ultimaConexion = ultimaConexion;}
    public void setFrecuenciaLecturaSeg(int frecuenciaLecturaSeg) {this.frecuenciaLecturaSeg = frecuenciaLecturaSeg;}

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

    /**
     * Compara este dispositivo ESP32 con otro objeto para determinar igualdad.
     * <p>
     * Dos dispositivos ESP32 se consideran iguales si tienen el mismo ID (clave primaria).
     * Esto es consistente con la lógica de base de datos relacional.
     * </p>
     * <p>
     * Implementación optimizada para entidades JPA:
     * </p>
     * <ul>
     *   <li>Solo compara IDs (no todos los campos)</li>
     *   <li>Verifica que el ID no sea null antes de comparar</li>
     *   <li>Funciona correctamente antes y después de persistir</li>
     * </ul>
     *
     * @param o objeto a comparar con este dispositivo ESP32
     * @return true si los objetos son iguales (mismo ID), false en caso contrario
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DispositivoEsp32)) return false;
        DispositivoEsp32 dispositivoEsp32 = (DispositivoEsp32) o;
        return id != null && id.equals(dispositivoEsp32.getId());
    }

    /**
     * Genera un código hash para este dispositivo ESP32.
     * <p>
     * Implementación optimizada para entidades JPA que garantiza que el hashCode
     * permanece constante durante toda la vida del objeto, incluso cuando cambian
     * los campos (incluido el ID al ser persistido).
     * </p>
     * <p>
     * Esto es crucial para que los objetos funcionen correctamente en colecciones
     * como HashSet o HashMap.
     * </p>
     *
     * @return código hash basado en la clase (constante para todos los dispositivos ESP32)
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
