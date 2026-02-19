package com.example.gardenmonitor.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

/**
 * Entidad JPA que representa un árbol en el sistema Proyecto Árboles.
 * <p>
 * Esta clase mapea la tabla 'arbol' de la base de datos PostgreSQL
 * y almacena información de los árboles monitorizados en los centros educativos,
 * incluyendo datos de plantación, umbrales de alertas y asociación con dispositivos ESP32.
 * </p>
 *
 * @author Richard Ortiz y Enrique Pérez
 * @version 1.0
 */

@Entity
@Table(name = "arbol", indexes = {
        @Index(name = "idx_arbol_centro", columnList = "centro_id"),
        @Index(name = "idx_arbol_dispositivo_esp32", columnList = "dispositivo_id"),
        @Index(name = "idx_arbol_especie", columnList = "especie")
})
public class Arbol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Centro educativo al que pertenece el árbol.
     * <p>
     * Relación Many-to-One: muchos árboles pueden pertenecer a un centro.
     * Si se elimina el centro, se eliminan en cascada todos sus árboles.
     * </p>
     */
    @ManyToOne
    @JoinColumn(name = "centro_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CentroEducativo centroEducativo;

    @NotBlank
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @NotBlank
    @Column(name = "especie", nullable = false, length = 150)
    private String especie;

    /**
     * Fecha de plantación del árbol.
     * <p>
     * Debe ser una fecha en el pasado. Se valida con @Past.
     * </p>
     */
    @Past
    @NotNull
    @Column(name = "fecha_plantacion", nullable = false)
    private LocalDate fechaPlantacion;

    @Column(name = "ubicacion_especifica", length = 200)
    private String ubicacionEspecifica;

    /**
     * Dispositivo ESP32 asociado al árbol para monitorización.
     * <p>
     * Relación One-to-One: un árbol tiene un único dispositivo.
     * Si se elimina el dispositivo, se establece a NULL (ON DELETE SET NULL).
     * </p>
     */
    @OneToOne
    @JoinColumn(name = "dispositivo_id", foreignKey = @ForeignKey(name = "fk_dispositivo_esp32", foreignKeyDefinition = "FOREIGN KEY (dispositivo_id) REFERENCES dispositivo_esp32(id) ON DELETE SET NULL"))
    private DispositivoEsp32 dispositivoEsp32;

    @Column(name = "umbral_temp_min", columnDefinition = "DECIMAL(5,2) DEFAULT 5.00")
    @DecimalMin(value = "-15")
    private BigDecimal umbralTempMin;

    @Column(name = "umbral_temp_max", columnDefinition = "DECIMAL(5,2) DEFAULT 40.00")
    @DecimalMax(value = "45")
    private BigDecimal umbralTempMax;

    @Column(name = "umbral_humedad_ambiente_min", columnDefinition = "DECIMAL(5,2) DEFAULT 30.00")
    @DecimalMin(value = "0.01")
    @DecimalMax(value = "100")
    private BigDecimal umbralHumedadAmbienteMin;

    @Column(name = "umbral_humedad_ambiente_max", columnDefinition = "DECIMAL(5,2) DEFAULT 90.00")
    @DecimalMin(value = "0.01")
    @DecimalMax(value = "100")
    private BigDecimal umbralHumedadAmbienteMax;

    @Column(name = "umbral_humedad_suelo_min", columnDefinition = "DECIMAL(5,2) DEFAULT 30.00")
    @DecimalMin(value = "0.01")
    @DecimalMax(value = "100")
    private BigDecimal umbralHumedadSueloMin;

    @Column(name = "umbral_co2_max", columnDefinition = "DECIMAL(5,2) DEFAULT 1000.00")
    private BigDecimal umbralCO2Max;

    @Column(name = "absorcion_co2_anual", columnDefinition = "DECIMAL(8,2)")
    @DecimalMin(value = "0")
    private BigDecimal absorcionCo2Anual;

    /**
     * Constructor vacío requerido por JPA.
     */
    public Arbol() {
    }

    /**
     * Constructor para crear un nuevo árbol con todos sus datos.
     * <p>
     * El campo id se establece automáticamente.
     * </p>
     *
     * @param centroEducativo centro educativo al que pertenece el árbol
     * @param nombre nombre del árbol (máx 100 caracteres)
     * @param especie especie del árbol (máx 150 caracteres)
     * @param fechaPlantacion fecha de plantación (debe ser en el pasado)
     * @param ubicacionEspecifica ubicación específica dentro del centro (máx 200 caracteres)
     * @param dispositivoEsp32 dispositivo ESP32 asociado (puede ser null)
     * @param umbralTempMin temperatura mínima de alerta (rango: -15 a 45°C)
     * @param umbralTempMax temperatura máxima de alerta (rango: -15 a 45°C)
     * @param umbralHumedadAmbienteMin humedad ambiente mínima de alerta (rango: 0.01 a 100%)
     * @param umbralHumedadAmbienteMax humedad ambiente máxima de alerta (rango: 0.01 a 100%)
     * @param umbralHumedadSueloMin humedad de suelo mínima de alerta (rango: 0.01 a 100%)
     * @param umbralCO2Max nivel máximo de CO2 de alerta (ppm)
     */
    public Arbol(
            CentroEducativo centroEducativo,
            String nombre,
            String especie,
            LocalDate fechaPlantacion,
            String ubicacionEspecifica,
            DispositivoEsp32 dispositivoEsp32,
            BigDecimal umbralTempMin,
            BigDecimal umbralTempMax,
            BigDecimal umbralHumedadAmbienteMin,
            BigDecimal umbralHumedadAmbienteMax,
            BigDecimal umbralHumedadSueloMin,
            BigDecimal umbralCO2Max) {

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

    public Long getId() {
        return id;
    }

    public CentroEducativo getCentroEducativo() {
        return centroEducativo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public LocalDate getFechaPlantacion() {
        return fechaPlantacion;
    }

    public String getUbicacionEspecifica() {
        return ubicacionEspecifica;
    }

    public DispositivoEsp32 getDispositivoEsp32() {
        return dispositivoEsp32;
    }

    public BigDecimal getUmbralTempMin() {
        return umbralTempMin;
    }

    public BigDecimal getUmbralTempMax() {
        return umbralTempMax;
    }

    public BigDecimal getUmbralHumedadAmbienteMin() {
        return umbralHumedadAmbienteMin;
    }

    public BigDecimal getUmbralHumedadAmbienteMax() {
        return umbralHumedadAmbienteMax;
    }

    public BigDecimal getUmbralHumedadSueloMin() {
        return umbralHumedadSueloMin;
    }

    public BigDecimal getUmbralCO2Max() {
        return umbralCO2Max;
    }

    public BigDecimal getAbsorcionCo2Anual() {
        return absorcionCo2Anual;
    }

    public void setCentroEducativo(CentroEducativo centroEducativo) {
        this.centroEducativo = centroEducativo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public void setFechaPlantacion(LocalDate fechaPlantacion) {
        this.fechaPlantacion = fechaPlantacion;
    }

    public void setUbicacionEspecifica(String ubicacionEspecifica) {
        this.ubicacionEspecifica = ubicacionEspecifica;
    }

    public void setDispositivoEsp32(DispositivoEsp32 dispositivoEsp32) {
        this.dispositivoEsp32 = dispositivoEsp32;
    }

    public void setUmbralTempMin(BigDecimal umbralTempMin) {
        this.umbralTempMin = umbralTempMin;
    }

    public void setUmbralTempMax(BigDecimal umbralTempMax) {
        this.umbralTempMax = umbralTempMax;
    }

    public void setUmbralHumedadAmbienteMin(BigDecimal umbralHumedadAmbienteMin) {
        this.umbralHumedadAmbienteMin = umbralHumedadAmbienteMin;
    }

    public void setUmbralHumedadAmbienteMax(BigDecimal umbralHumedadAmbienteMax) {
        this.umbralHumedadAmbienteMax = umbralHumedadAmbienteMax;
    }

    public void setUmbralHumedadSueloMin(BigDecimal umbralHumedadSueloMin) {
        this.umbralHumedadSueloMin = umbralHumedadSueloMin;
    }

    public void setUmbralCO2Max(BigDecimal umbralCO2Max) {
        this.umbralCO2Max = umbralCO2Max;
    }

    public void setAbsorcionCo2Anual(BigDecimal absorcionCo2Anual) {
        this.absorcionCo2Anual = absorcionCo2Anual;
    }

    /**
     * Compara este árbol con otro objeto para determinar igualdad.
     * <p>
     * Dos árboles se consideran iguales si tienen el mismo ID (clave primaria).
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
     * @param o objeto a comparar con este árbol
     * @return true si los objetos son iguales (mismo ID), false en caso contrario
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Arbol)) return false;
        Arbol arbol = (Arbol) o;
        return id != null && id.equals(arbol.getId());
    }

    /**
     * Genera un código hash para este árbol.
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
     * @return código hash basado en la clase (constante para todos los árboles)
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
