package com.example.gardenmonitor.repository;

import com.example.gardenmonitor.dto.LecturaMuestraProjection;
import com.example.gardenmonitor.model.Arbol;
import com.example.gardenmonitor.model.DispositivoEsp32;
import com.example.gardenmonitor.model.Lectura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface LecturaRepository extends JpaRepository<Lectura, Long> {

    Page<Lectura> findByArbolOrderByTimestampDesc(Arbol arbol, Pageable pageable);

    List<Lectura> findByDispositivoOrderByTimestampDesc(DispositivoEsp32 dispositivo);

    Page<Lectura> findByArbolAndTimestampBetweenOrderByTimestampDesc(
            Arbol arbol, LocalDateTime desde, LocalDateTime hasta, Pageable pageable);

    /**
     * Stride sampling: devuelve lecturas REALES (sin promedios) garantizando
     * un máximo de ~400 puntos independientemente del volumen de datos.
     *
     * Algoritmo:
     *   1. Numera todas las lecturas del rango cronológicamente (ROW_NUMBER).
     *   2. Calcula el paso: stride = MAX(1, CEIL(total / 400)).
     *   3. Selecciona la lectura 1, 1+stride, 1+2·stride, … más siempre la última.
     *
     * Así con 10.000 lecturas el stride es 25 y se devuelven ~400 puntos reales;
     * con 200 lecturas el stride es 1 y se devuelven todas sin pérdida.
     */
    @Query(value = """
            WITH numbered AS (
                SELECT l.id,
                       l.timestamp,
                       l.temperatura,
                       l.humedad_ambiente,
                       l.humedad_suelo,
                       l.co2,
                       l.diametro_tronco,
                       ROW_NUMBER() OVER (ORDER BY l.timestamp ASC) AS rn,
                       COUNT(*)     OVER ()                          AS total_count
                FROM lectura l
                WHERE l.arbol_id = :arbolId
                  AND l.timestamp >= :desde
                  AND l.timestamp <= :hasta
            )
            SELECT id, timestamp, temperatura, humedad_ambiente,
                   humedad_suelo, co2, diametro_tronco
            FROM numbered
            WHERE MOD(rn - 1, GREATEST(1, CEIL(total_count::float / 400)::int)) = 0
               OR rn = total_count
            ORDER BY timestamp ASC
            """, nativeQuery = true)
    List<LecturaMuestraProjection> findMuestraByArbolAndRango(
            @Param("arbolId") Long arbolId,
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta);
}
