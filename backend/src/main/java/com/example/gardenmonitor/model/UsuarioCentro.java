package com.example.gardenmonitor.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad JPA que representa la relación N:M entre Usuario y CentroEducativo.
 * <p>
 * Esta clase mapea la tabla 'usuario_centro' de la base de datos PostgreSQL
 * y almacena las asignaciones de coordinadores a centros educativos.
 * Un coordinador puede gestionar múltiples centros y un centro puede tener
 * múltiples coordinadores asignados.
 * </p>
 *
 * @author Richard Ortiz y Enrique Pérez
 * @version 1.0
 */
@Entity
@Table(name = "usuario_centro", uniqueConstraints = {
        @UniqueConstraint(name = "uq_usuario_centro", columnNames = {"usuario_id", "centro_id"})
}, indexes = {
        @Index(name = "idx_usuario_centro_usuario", columnList = "usuario_id"),
        @Index(name = "idx_usuario_centro_centro", columnList = "centro_id")
})
public class UsuarioCentro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Usuario (coordinador) asignado al centro educativo.
     * <p>
     * Relación Many-to-One: un usuario puede estar asignado a múltiples centros.
     * Se carga de forma inmediata (EAGER) para disponer del rol en cada consulta.
     * </p>
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /**
     * Centro educativo al que se asigna el coordinador.
     * <p>
     * Relación Many-to-One: un centro puede tener múltiples coordinadores asignados.
     * Se carga de forma inmediata (EAGER) para disponer del nombre en cada consulta.
     * </p>
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "centro_id", nullable = false)
    private CentroEducativo centroEducativo;

    /**
     * Fecha y hora en que se realizó la asignación.
     * <p>
     * Se establece automáticamente al crear la asignación mediante @PrePersist.
     * No es actualizable una vez registrada.
     * </p>
     */
    @Column(name = "fecha_asignacion", nullable = false, updatable = false, columnDefinition = "TIMESTAMPTZ")
    private LocalDateTime fechaAsignacion;

    /**
     * Indica si la asignación está activa.
     * <p>
     * Valor por defecto: true. Permite desactivar asignaciones sin eliminarlas.
     * </p>
     */
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    /**
     * Constructor vacío requerido por JPA.
     */
    public UsuarioCentro() {
    }

    /**
     * Constructor para crear una nueva asignación de coordinador a centro.
     * <p>
     * La asignación se crea como activa. La fecha de asignación se establece
     * automáticamente mediante @PrePersist.
     * </p>
     *
     * @param usuario          usuario con rol COORDINADOR a asignar
     * @param centroEducativo  centro educativo al que se asigna el coordinador
     */
    public UsuarioCentro(Usuario usuario, CentroEducativo centroEducativo) {
        this.usuario = usuario;
        this.centroEducativo = centroEducativo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public CentroEducativo getCentroEducativo() { return centroEducativo; }
    public void setCentroEducativo(CentroEducativo centroEducativo) { this.centroEducativo = centroEducativo; }

    public LocalDateTime getFechaAsignacion() { return fechaAsignacion; }
    public void setFechaAsignacion(LocalDateTime fechaAsignacion) { this.fechaAsignacion = fechaAsignacion; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    /**
     * Callback ejecutado antes de persistir una nueva asignación en la base de datos.
     * <p>
     * Establece valores por defecto si no han sido proporcionados:
     * </p>
     * <ul>
     *   <li>fechaAsignacion: fecha y hora actual si es null</li>
     *   <li>activo: true si es null</li>
     * </ul>
     */
    @PrePersist
    protected void onCreate() {
        if (fechaAsignacion == null) {
            fechaAsignacion = LocalDateTime.now();
        }
        if (activo == null) {
            activo = true;
        }
    }

    /**
     * @return representación en String de la asignación usuario-centro
     */
    @Override
    public String toString() {
        return "UsuarioCentro{" +
                "id=" + id +
                ", usuarioId=" + (usuario != null ? usuario.getId() : null) +
                ", centroId=" + (centroEducativo != null ? centroEducativo.getId() : null) +
                ", fechaAsignacion=" + fechaAsignacion +
                ", activo=" + activo +
                '}';
    }

    /**
     * Compara esta asignación con otro objeto para determinar igualdad.
     * <p>
     * Dos asignaciones se consideran iguales si tienen el mismo ID (clave primaria).
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
     * @param o objeto a comparar con esta asignación
     * @return true si los objetos son iguales (mismo ID), false en caso contrario
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuarioCentro)) return false;
        UsuarioCentro that = (UsuarioCentro) o;
        return id != null && id.equals(that.getId());
    }

    /**
     * Genera un código hash para esta asignación.
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
     * @return código hash basado en la clase (constante para todas las asignaciones)
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
