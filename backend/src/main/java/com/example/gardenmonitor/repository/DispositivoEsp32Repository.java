package com.example.gardenmonitor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.gardenmonitor.model.DispositivoEsp32;
import com.example.gardenmonitor.model.Arbol;


@Repository
public interface DispositivoEsp32Repository extends JpaRepository<DispositivoEsp32, Long>{
    Optional<DispositivoEsp32> findByMacAddress(String macAddress);
    boolean existsByMacAddress(String macAddress);
    Optional<DispositivoEsp32> findByArbol(Arbol arbol);
    List<DispositivoEsp32> findByActivo(boolean activo);
}
