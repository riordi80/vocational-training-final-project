package com.example.gardenmonitor.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad JPA que representa un usuario del sistema Proyecto Árboles.
 * <p>
 * Esta clase mapea la tabla 'usuario' de la base de datos PostgreSQL
 * y gestiona los usuarios que interactúan con el sistema de monitorización
 * de árboles en centros educativos.
 * </p>
 *
 * @author Richard Ortiz y Enrique Pérez
 * @version 1.0
 */

@Entity
@Table(name = "usuario", indexes = {
        @Index(name = "idx_usuario_rol", columnList = "rol"),
        @Index(name = "idx_usuario_activo", columnList = "activo")
})
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "email", unique = true, nullable = false, length = 150)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    /**
     * Rol del usuario en el sistema.
     *
     * @see Rol
     */
    @Column(name = "rol", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Rol rol;

    /**
     * Fecha y hora de creación del usuario.
     * <p>
     * Se establece automáticamente al crear el usuario mediante @PrePersist.
     * </p>
     */
    @Column(name = "fecha_creacion", nullable = false, updatable = false, columnDefinition = "TIMESTAMPTZ")
    private LocalDateTime fechaCreacion;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    /**
     * Constructor vacío requerido por JPA.
     */
    public Usuario() {
    }

    /**
     * Constructor para crear un nuevo usuario con datos básicos.
     * <p>
     * Los campos id, fechaCreacion y activo se establecen automáticamente:
     * </p>
     *
     * @param nombre       nombre completo del usuario (máx 100 caracteres)
     * @param email        correo electrónico único del usuario (máx 150 caracteres)
     * @param passwordHash contraseña hasheada con BCrypt (máx 255 caracteres)
     * @param rol          rol del usuario en el sistema
     */
    public Usuario(String nombre, String email, String passwordHash, Rol rol) {
        this.nombre = nombre;
        this.email = email;
        this.passwordHash = passwordHash;
        this.rol = rol;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Rol getRol() {
        return rol;
    }
    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Boolean getActivo() {
        return activo;
    }
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    /**
     * Callback ejecutado antes de persistir un nuevo usuario en la base de datos.
     * <p>
     * Establece valores por defecto si no han sido proporcionados:
     * </p>
     * <ul>
     *   <li>fechaCreacion: fecha y hora actual si es null</li>
     *   <li>activo: true si es null</li>
     * </ul>
     */
    @PrePersist
    protected void onCreate() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
        if (activo == null) {
            activo = true;
        }
    }

    /**
     * @return representación en String del usuario
     */
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", rol=" + rol +
                ", fechaCreacion=" + fechaCreacion +
                ", activo=" + activo +
                '}';
    }

    /**
     * Compara este usuario con otro objeto para determinar igualdad.
     * <p>
     * Dos usuarios se consideran iguales si tienen el mismo ID (clave primaria).
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
     * @param o objeto a comparar con este usuario
     * @return true si los objetos son iguales (mismo ID), false en caso contrario
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return id != null && id.equals(usuario.getId());
    }

    /**
     * Genera un código hash para este usuario.
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
     * @return código hash basado en la clase (constante para todos los usuarios)
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

