package com.example.gardenmonitor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

/**
 * Entidad JPA que representa una alerta en el sistema Proyecto Árboles.
 * <p>
 * Esta clase mapea la tabla 'alerta' de la base de datos PostgreSQL
 * y almacena las alertas generadas automáticamente cuando los valores
 * de los sensores de un árbol superan los umbrales configurados.
 * </p>
 *
 * @author Richard Ortiz y Enrique Pérez
 * @version 1.0
 */
@Entity
@Table(name = "alerta", indexes = {
        @Index(name = "idx_alerta_arbol", columnList = "arbol_id"),
        @Index(name = "idx_alerta_estado", columnList = "estado"),
        @Index(name = "idx_alerta_timestamp", columnList = "timestamp")
})
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Árbol al que pertenece esta alerta.
     * <p>
     * Relación Many-to-One: un árbol puede generar múltiples alertas.
     * Si se elimina el árbol, se eliminan en cascada todas sus alertas.
     * </p>
     */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "arbol_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Arbol arbol;

    /**
     * Tipo de alerta según el sensor o condición que la originó.
     * <p>
     * Almacenado como texto en BD (EnumType.STRING) para legibilidad.
     * Los valores válidos están definidos en {@link TipoAlerta}.
     * </p>
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_alerta", nullable = false, length = 50)
    private TipoAlerta tipoAlerta;

    /**
     * Fecha y hora en que se generó la alerta.
     * <p>
     * Se establece automáticamente al crear la alerta mediante @PrePersist.
     * </p>
     */
    @Column(name = "timestamp", nullable = false, columnDefinition = "TIMESTAMPTZ")
    private LocalDateTime timestamp;

    @NotBlank
    @Column(name = "mensaje", nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    /**
     * Estado actual de la alerta.
     * <p>
     * Valor por defecto: ACTIVA. Puede transicionar a RESUELTA o IGNORADA.
     * Almacenado como texto en BD (EnumType.STRING).
     * Los valores válidos están definidos en {@link EstadoAlerta}.
     * </p>
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoAlerta estado = EstadoAlerta.ACTIVA;

    /**
     * Fecha y hora en que se resolvió o ignoró la alerta.
     * <p>
     * Es null mientras el estado sea ACTIVA.
     * Se establece cuando el estado cambia a RESUELTA o IGNORADA.
     * </p>
     */
    @Column(name = "fecha_resolucion", columnDefinition = "TIMESTAMPTZ")
    private LocalDateTime fechaResolucion;

    /**
     * Constructor vacío requerido por JPA.
     */
    public Alerta() {}

    /**
     * Constructor para crear una nueva alerta.
     * <p>
     * El estado se inicializa a ACTIVA. El timestamp se establece
     * automáticamente mediante @PrePersist.
     * </p>
     *
     * @param arbol      árbol que originó la alerta
     * @param tipoAlerta tipo de condición que disparó la alerta
     * @param mensaje    descripción detallada de la alerta
     */
    public Alerta(Arbol arbol, TipoAlerta tipoAlerta, String mensaje) {
        this.arbol = arbol;
        this.tipoAlerta = tipoAlerta;
        this.mensaje = mensaje;
        this.estado = EstadoAlerta.ACTIVA;
    }

    /**
     * Callback ejecutado antes de persistir una nueva alerta en la base de datos.
     * <p>
     * Establece el timestamp automáticamente si no ha sido proporcionado:
     * </p>
     * <ul>
     *   <li>timestamp: fecha y hora actual si es null</li>
     * </ul>
     */
    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }

    public Long getId() { return id; }
    public Arbol getArbol() { return arbol; }
    public TipoAlerta getTipoAlerta() { return tipoAlerta; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getMensaje() { return mensaje; }
    public EstadoAlerta getEstado() { return estado; }
    public LocalDateTime getFechaResolucion() { return fechaResolucion; }

    public void setId(Long id) { this.id = id; }
    public void setArbol(Arbol arbol) { this.arbol = arbol; }
    public void setTipoAlerta(TipoAlerta tipoAlerta) { this.tipoAlerta = tipoAlerta; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public void setEstado(EstadoAlerta estado) { this.estado = estado; }
    public void setFechaResolucion(LocalDateTime fechaResolucion) { this.fechaResolucion = fechaResolucion; }

    /**
     * @return representación en String de la alerta
     */
    @Override
    public String toString() {
        return "Alerta{" +
                "id=" + id +
                ", arbolId=" + (arbol != null ? arbol.getId() : null) +
                ", tipoAlerta=" + tipoAlerta +
                ", timestamp=" + timestamp +
                ", estado=" + estado +
                '}';
    }

    /**
     * Compara esta alerta con otro objeto para determinar igualdad.
     * <p>
     * Dos alertas se consideran iguales si tienen el mismo ID (clave primaria).
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
     * @param o objeto a comparar con esta alerta
     * @return true si los objetos son iguales (mismo ID), false en caso contrario
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Alerta)) return false;
        Alerta alerta = (Alerta) o;
        return id != null && id.equals(alerta.getId());
    }

    /**
     * Genera un código hash para esta alerta.
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
     * @return código hash basado en la clase (constante para todas las alertas)
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
