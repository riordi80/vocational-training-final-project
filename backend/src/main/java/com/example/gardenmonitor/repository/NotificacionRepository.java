package com.example.gardenmonitor.repository;

import com.example.gardenmonitor.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByUsuario_Id(Long usuarioId);
    List<Notificacion> findByUsuario_IdAndLeida(Long usuarioId, boolean leida);
    List<Notificacion> findByAlerta_Id(Long alertaId);
}
