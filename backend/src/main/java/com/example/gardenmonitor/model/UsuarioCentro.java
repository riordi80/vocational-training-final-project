package com.example.gardenmonitor.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "centro_id", nullable = false)
    private CentroEducativo centroEducativo;

    @Column(name = "fecha_asignacion", nullable = false, updatable = false, columnDefinition = "TIMESTAMPTZ")
    private LocalDateTime fechaAsignacion;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    public UsuarioCentro() {
    }

    public UsuarioCentro(Usuario usuario, CentroEducativo centroEducativo) {
        this.usuario = usuario;
        this.centroEducativo = centroEducativo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public CentroEducativo getCentroEducativo() {
        return centroEducativo;
    }

    public void setCentroEducativo(CentroEducativo centroEducativo) {
        this.centroEducativo = centroEducativo;
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @PrePersist
    protected void onCreate() {
        if (fechaAsignacion == null) {
            fechaAsignacion = LocalDateTime.now();
        }
        if (activo == null) {
            activo = true;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuarioCentro)) return false;
        UsuarioCentro that = (UsuarioCentro) o;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

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
}
