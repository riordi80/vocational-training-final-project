package com.example.gardenmonitor.repository;

import com.example.gardenmonitor.model.Arbol;
import com.example.gardenmonitor.model.DispositivoEsp32;
import com.example.gardenmonitor.model.Lectura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface LecturaRepository extends JpaRepository<Lectura, Long> {

    Page<Lectura> findByArbolOrderByTimestampDesc(Arbol arbol, Pageable pageable);

    List<Lectura> findByDispositivoOrderByTimestampDesc(DispositivoEsp32 dispositivo);

    Page<Lectura> findByArbolAndTimestampBetweenOrderByTimestampDesc(
            Arbol arbol, LocalDateTime desde, LocalDateTime hasta, Pageable pageable);
}
