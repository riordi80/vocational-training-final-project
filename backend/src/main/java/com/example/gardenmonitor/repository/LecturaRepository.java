package com.example.gardenmonitor.repository;

import com.example.gardenmonitor.model.Arbol;
import com.example.gardenmonitor.model.DispositivoEsp32;
import com.example.gardenmonitor.model.Lectura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface LecturaRepository extends JpaRepository<Lectura, Long> {

    List<Lectura> findByArbolOrderByTimestampDesc(Arbol arbol);

    List<Lectura> findByDispositivoOrderByTimestampDesc(DispositivoEsp32 dispositivo);

    List<Lectura> findByArbolAndTimestampBetweenOrderByTimestampDesc(
            Arbol arbol, LocalDateTime desde, LocalDateTime hasta);
}
