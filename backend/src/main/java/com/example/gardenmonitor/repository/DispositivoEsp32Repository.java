package com.example.gardenmonitor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.gardenmonitor.model.DispositivoEsp32;


@Repository
public interface DispositivoEsp32Repository extends JpaRepository<DispositivoEsp32, Long>{
    Optional<DispositivoEsp32> findByMacAddress(String macAddress);
    boolean existsByMacAddress(String macAddress);
    List<DispositivoEsp32> findByActivo(boolean activo);
    List<DispositivoEsp32> findByCentroEducativo_Id(Long centroId);
}
