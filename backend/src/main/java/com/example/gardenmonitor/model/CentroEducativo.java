package com.example.gardenmonitor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA que representa un centro educativo en el sistema Garden Monitor.
 * <p>
 * Esta clase mapea la tabla 'centro_educativo' de la base de datos PostgreSQL
 * y almacena información de los centros educativos que participan en el
 * proyecto de monitorización de árboles.
 * </p>
 *
 * @author Richard Ortiz y Enrique Pérez
 * @version 1.0
 */

@Entity
@Table(name = "centro_educativo", indexes = {
        @Index(name = "idx_centro_educativo_nombre", columnList = "nombre")
})
public class CentroEducativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @Column(name = "direccion", nullable = false, length = 300)
    private String direccion;

    /**
     * Latitud de la ubicación del centro educativo.
     * <p>
     * Coordenada geográfica en formato decimal con precisión de 8 decimales.
     * Rango válido: -90.00000000 a 90.00000000
     * </p>
     */
    @Column(name = "latitud", precision = 10, scale = 8)
    private BigDecimal latitud;

    /**
     * Longitud de la ubicación del centro educativo.
     * <p>
     * Coordenada geográfica en formato decimal con precisión de 8 decimales.
     * Rango válido: -180.00000000 a 180.00000000
     * </p>
     */
    @Column(name = "longitud", precision = 11, scale = 8)
    private BigDecimal longitud;

    @Column(name = "responsable", length = 100)
    private String responsable;

    /**
     * Fecha y hora de creación del centro educativo.
     * <p>
     * Se establece automáticamente al crear el centro mediante @PrePersist.
     * </p>
     */
    @Column(name = "fecha_creacion", nullable = false, updatable = false, columnDefinition = "TIMESTAMPTZ")
    private LocalDateTime fechaCreacion;

    /**
     * Lista de árboles asociados a este centro educativo.
     * <p>
     * Relación One-to-Many bidireccional: un centro puede tener múltiples árboles.
     * El lado "propietario" de la relación está en la entidad Arbol (campo centroEducativo).
     * </p>
     */
    @JsonIgnore
    @OneToMany(mappedBy = "centroEducativo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Arbol> arboles = new ArrayList<>();

    /**
     * Constructor vacío requerido por JPA.
     */
    public CentroEducativo() {
    }

    /**
     * Constructor para crear un nuevo centro educativo con datos básicos.
     * <p>
     * Los campos id y fechaCreacion se establecen automáticamente.
     * </p>
     *
     * @param nombre     nombre del centro educativo (máx 200 caracteres)
     * @param direccion  dirección postal completa del centro (máx 300 caracteres)
     * @param latitud    coordenada geográfica de latitud (rango: -90 a 90)
     * @param longitud   coordenada geográfica de longitud (rango: -180 a 180)
     * @param responsable nombre del responsable del centro (máx 100 caracteres)
     */
    public CentroEducativo(String nombre, String direccion, BigDecimal latitud, BigDecimal longitud, String responsable) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.responsable = responsable;
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

    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public BigDecimal getLatitud() { return latitud; }
    public void setLatitud(BigDecimal latitud) { this.latitud = latitud;  }

    public BigDecimal getLongitud() { return longitud; }
    public void setLongitud(BigDecimal longitud) { this.longitud = longitud; }

    public String getResponsable() { return responsable; }
    public void setResponsable(String responsable) { this.responsable = responsable; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public List<Arbol> getArboles() {
        return arboles;
    }

    // Métodos helper para mantener la sincronización bidireccional
    public void addArbol(Arbol arbol) {
        arboles.add(arbol);
        arbol.setCentroEducativo(this);
    }

    public void removeArbol(Arbol arbol) {
        arboles.remove(arbol);
        arbol.setCentroEducativo(null);
    }

    /**
     * Callback ejecutado antes de persistir un nuevo centro educativo en la base de datos.
     * <p>
     * Establece la fecha de creación automáticamente si no ha sido proporcionada:
     * </p>
     * <ul>
     *   <li>fechaCreacion: fecha y hora actual si es null</li>
     * </ul>
     */
    @PrePersist
    protected void onCreate() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
    }


    /**
     * @return representación en String del centro educativo
     */
    @Override
    public String toString() {
        return "CentroEducativo{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", responsable='" + responsable + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", numeroArboles=" + (arboles != null ? arboles.size() : 0) +
                '}';
    }

    /**
     * Compara este centro educativo con otro objeto para determinar igualdad.
     * <p>
     * Dos centros educativos se consideran iguales si tienen el mismo ID (clave primaria).
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
     * @param o objeto a comparar con este centro educativo
     * @return true si los objetos son iguales (mismo ID), false en caso contrario
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CentroEducativo)) return false;
        CentroEducativo centro = (CentroEducativo) o;
        return id != null && id.equals(centro.getId());
    }

    /**
     * Genera un código hash para este centro educativo.
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
     * @return código hash basado en la clase (constante para todos los centros educativos)
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
