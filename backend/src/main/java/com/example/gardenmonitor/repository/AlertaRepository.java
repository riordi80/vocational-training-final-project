package com.example.gardenmonitor.repository;

import com.example.gardenmonitor.model.Alerta;
import com.example.gardenmonitor.model.Arbol;
import com.example.gardenmonitor.model.EstadoAlerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Long> {
    List<Alerta> findByArbol(Arbol arbol);
    List<Alerta> findByArbol_Id(Long arbolId);
    List<Alerta> findByEstado(EstadoAlerta estado);
    List<Alerta> findByArbol_IdAndEstado(Long arbolId, EstadoAlerta estado);
}
