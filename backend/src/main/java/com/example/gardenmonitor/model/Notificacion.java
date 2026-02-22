package com.example.gardenmonitor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

/**
 * Entidad JPA que representa una notificación en el sistema Proyecto Árboles.
 * <p>
 * Esta clase mapea la tabla 'notificacion' de la base de datos PostgreSQL
 * y almacena las notificaciones enviadas a los usuarios cuando se genera
 * una alerta relacionada con los árboles de sus centros asignados.
 * </p>
 *
 * @author Richard Ortiz y Enrique Pérez
 * @version 1.0
 */
@Entity
@Table(name = "notificacion", indexes = {
        @Index(name = "idx_notificacion_usuario_leida", columnList = "usuario_id, leida"),
        @Index(name = "idx_notificacion_fecha", columnList = "fecha_envio")
})
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Usuario destinatario de la notificación.
     * <p>
     * Relación Many-to-One: un usuario puede recibir múltiples notificaciones.
     * Si se elimina el usuario, se eliminan en cascada todas sus notificaciones.
     * </p>
     */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Usuario usuario;

    /**
     * Alerta que origina esta notificación.
     * <p>
     * Relación Many-to-One: una alerta puede generar múltiples notificaciones
     * (una por cada usuario destinatario).
     * Si se elimina la alerta, se eliminan en cascada sus notificaciones.
     * </p>
     */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "alerta_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Alerta alerta;

    /**
     * Indica si el usuario ha leído la notificación.
     * <p>
     * Valor por defecto: false (no leída).
     * </p>
     */
    @Column(name = "leida", nullable = false)
    private boolean leida = false;

    /**
     * Fecha y hora en que se envió la notificación.
     * <p>
     * Se establece automáticamente al crear la notificación mediante @PrePersist.
     * </p>
     */
    @Column(name = "fecha_envio", nullable = false, columnDefinition = "TIMESTAMPTZ")
    private LocalDateTime fechaEnvio;

    /**
     * Constructor vacío requerido por JPA.
     */
    public Notificacion() {}

    /**
     * Constructor para crear una nueva notificación.
     * <p>
     * La notificación se crea como no leída. La fecha de envío se establece
     * automáticamente mediante @PrePersist.
     * </p>
     *
     * @param usuario usuario destinatario de la notificación
     * @param alerta  alerta que origina la notificación
     */
    public Notificacion(Usuario usuario, Alerta alerta) {
        this.usuario = usuario;
        this.alerta = alerta;
        this.leida = false;
    }

    /**
     * Callback ejecutado antes de persistir una nueva notificación en la base de datos.
     * <p>
     * Establece la fecha de envío automáticamente si no ha sido proporcionada:
     * </p>
     * <ul>
     *   <li>fechaEnvio: fecha y hora actual si es null</li>
     * </ul>
     */
    @PrePersist
    protected void onCreate() {
        if (fechaEnvio == null) {
            fechaEnvio = LocalDateTime.now();
        }
    }

    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public Alerta getAlerta() { return alerta; }
    public boolean isLeida() { return leida; }
    public LocalDateTime getFechaEnvio() { return fechaEnvio; }

    public void setId(Long id) { this.id = id; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public void setAlerta(Alerta alerta) { this.alerta = alerta; }
    public void setLeida(boolean leida) { this.leida = leida; }
    public void setFechaEnvio(LocalDateTime fechaEnvio) { this.fechaEnvio = fechaEnvio; }

    /**
     * @return representación en String de la notificación
     */
    @Override
    public String toString() {
        return "Notificacion{" +
                "id=" + id +
                ", usuarioId=" + (usuario != null ? usuario.getId() : null) +
                ", alertaId=" + (alerta != null ? alerta.getId() : null) +
                ", leida=" + leida +
                ", fechaEnvio=" + fechaEnvio +
                '}';
    }

    /**
     * Compara esta notificación con otro objeto para determinar igualdad.
     * <p>
     * Dos notificaciones se consideran iguales si tienen el mismo ID (clave primaria).
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
     * @param o objeto a comparar con esta notificación
     * @return true si los objetos son iguales (mismo ID), false en caso contrario
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notificacion)) return false;
        Notificacion notificacion = (Notificacion) o;
        return id != null && id.equals(notificacion.getId());
    }

    /**
     * Genera un código hash para esta notificación.
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
     * @return código hash basado en la clase (constante para todas las notificaciones)
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
