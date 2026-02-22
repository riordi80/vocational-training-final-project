package com.example.gardenmonitor.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

/**
 * Entidad JPA que representa una lectura de sensores en el sistema Proyecto Árboles.
 * <p>
 * Esta clase mapea la tabla 'lectura' de la base de datos PostgreSQL + TimescaleDB.
 * La tabla está configurada como hypertable particionada por timestamp.
 * </p>
 * <p>
 * La PK en la BD es compuesta (id, timestamp) por requisito de TimescaleDB,
 * pero JPA usa solo id como @Id ya que BIGSERIAL garantiza unicidad.
 * Hibernate no soporta @GeneratedValue en PKs compuestas (@IdClass),
 * por lo que se mantiene el mapping simplificado.
 * </p>
 *
 * @author Richard Ortiz y Enrique Pérez
 * @version 1.0
 */
@Entity
@Table(name = "lectura", indexes = {
        @Index(name = "idx_lectura_arbol_timestamp", columnList = "arbol_id, timestamp DESC"),
        @Index(name = "idx_lectura_dispositivo_timestamp", columnList = "dispositivo_id, timestamp DESC")
})
public class Lectura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Momento en que se registró la lectura.
     * <p>
     * Forma parte de la PK compuesta en la BD (requerido por TimescaleDB para
     * particionar la hypertable), pero en JPA es un campo regular.
     * Se asigna en el controller al recibir la lectura del ESP32.
     * </p>
     */
    @NotNull
    @Column(name = "timestamp", nullable = false, columnDefinition = "TIMESTAMPTZ")
    private LocalDateTime timestamp;

    /**
     * Árbol asociado a esta lectura.
     * <p>
     * Relación Many-to-One: un árbol genera muchas lecturas.
     * Si se elimina el árbol, se eliminan en cascada todas sus lecturas.
     * </p>
     */
    @ManyToOne
    @JoinColumn(name = "arbol_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Arbol arbol;

    /**
     * Dispositivo ESP32 que envió esta lectura.
     * <p>
     * Relación Many-to-One: un dispositivo envía muchas lecturas.
     * Si se elimina el dispositivo, se eliminan en cascada todas sus lecturas.
     * </p>
     */
    @ManyToOne
    @JoinColumn(name = "dispositivo_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private DispositivoEsp32 dispositivo;

    @NotNull
    @DecimalMin(value = "-50.00")
    @DecimalMax(value = "80.00")
    @Column(name = "temperatura", nullable = false, precision = 5, scale = 2)
    private BigDecimal temperatura;

    @NotNull
    @DecimalMin(value = "0.00")
    @DecimalMax(value = "100.00")
    @Column(name = "humedad_ambiente", nullable = false, precision = 5, scale = 2)
    private BigDecimal humedadAmbiente;

    @NotNull
    @DecimalMin(value = "0.00")
    @DecimalMax(value = "100.00")
    @Column(name = "humedad_suelo", nullable = false, precision = 5, scale = 2)
    private BigDecimal humedadSuelo;

    @DecimalMin(value = "0.00")
    @DecimalMax(value = "10000.00")
    @Column(name = "co2", precision = 7, scale = 2)
    private BigDecimal co2;

    @DecimalMin(value = "0.00")
    @DecimalMax(value = "5000.00")
    @Column(name = "diametro_tronco", precision = 6, scale = 2)
    private BigDecimal diametroTronco;

    /**
     * Constructor vacío requerido por JPA.
     */
    public Lectura() {}

    public Long getId() { return id; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public Arbol getArbol() { return arbol; }
    public DispositivoEsp32 getDispositivo() { return dispositivo; }
    public BigDecimal getTemperatura() { return temperatura; }
    public BigDecimal getHumedadAmbiente() { return humedadAmbiente; }
    public BigDecimal getHumedadSuelo() { return humedadSuelo; }
    public BigDecimal getCo2() { return co2; }
    public BigDecimal getDiametroTronco() { return diametroTronco; }

    public void setId(Long id) { this.id = id; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public void setArbol(Arbol arbol) { this.arbol = arbol; }
    public void setDispositivo(DispositivoEsp32 dispositivo) { this.dispositivo = dispositivo; }
    public void setTemperatura(BigDecimal temperatura) { this.temperatura = temperatura; }
    public void setHumedadAmbiente(BigDecimal humedadAmbiente) { this.humedadAmbiente = humedadAmbiente; }
    public void setHumedadSuelo(BigDecimal humedadSuelo) { this.humedadSuelo = humedadSuelo; }
    public void setCo2(BigDecimal co2) { this.co2 = co2; }
    public void setDiametroTronco(BigDecimal diametroTronco) { this.diametroTronco = diametroTronco; }

    /**
     * @return representación en String de la lectura
     */
    @Override
    public String toString() {
        return "Lectura{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", arbolId=" + (arbol != null ? arbol.getId() : null) +
                ", dispositivoId=" + (dispositivo != null ? dispositivo.getId() : null) +
                ", temperatura=" + temperatura +
                ", humedadAmbiente=" + humedadAmbiente +
                ", humedadSuelo=" + humedadSuelo +
                '}';
    }

    /**
     * Compara esta lectura con otro objeto para determinar igualdad.
     * <p>
     * Dos lecturas se consideran iguales si tienen el mismo ID (clave primaria).
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
     * @param o objeto a comparar con esta lectura
     * @return true si los objetos son iguales (mismo ID), false en caso contrario
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lectura)) return false;
        Lectura lectura = (Lectura) o;
        return id != null && id.equals(lectura.getId());
    }

    /**
     * Genera un código hash para esta lectura.
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
     * @return código hash basado en la clase (constante para todas las lecturas)
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
