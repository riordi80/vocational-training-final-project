package com.example.gardenmonitor.repository;

import com.example.gardenmonitor.model.Alerta;
import com.example.gardenmonitor.model.EstadoAlerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Long> {
    List<Alerta> findByDispositivo_Id(Long dispositivoId);
    List<Alerta> findByEstado(EstadoAlerta estado);
    List<Alerta> findByDispositivo_IdAndEstado(Long dispositivoId, EstadoAlerta estado);
}
