package com.example.gardenmonitor.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Proyección de Spring Data JPA para lecturas muestreadas (stride sampling).
 * Devuelve lecturas REALES (no promedios) con un paso calculado dinámicamente
 * para que el resultado nunca supere ~400 puntos, independientemente del rango.
 *
 * Los nombres de los getters mapean con los alias de la query nativa:
 *   id, timestamp, temperatura, humedad_ambiente, humedad_suelo, co2, diametro_tronco
 */
public interface LecturaMuestraProjection {

    Long getId();

    LocalDateTime getTimestamp();

    BigDecimal getTemperatura();

    BigDecimal getHumedadAmbiente();

    BigDecimal getHumedadSuelo();

    BigDecimal getCo2();

    BigDecimal getDiametroTronco();
}
