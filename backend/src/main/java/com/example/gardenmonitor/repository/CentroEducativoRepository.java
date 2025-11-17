package com.example.gardenmonitor.repository;

import com.example.gardenmonitor.model.CentroEducativo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CentroEducativoRepository extends JpaRepository<CentroEducativo, Long> {

    Optional<CentroEducativo> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
    List<CentroEducativo> findByNombreContainingIgnoreCase(String nombre);
    List<CentroEducativo> findByResponsable(String responsable);
    List<CentroEducativo> findAllByOrderByNombreAsc();

}
