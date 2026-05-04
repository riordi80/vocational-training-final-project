package com.example.proyectoarboles.util;

public class IslaUtils {

    public static final String[] ETIQUETAS = {
            "-- Seleccionar isla --",
            "Gran Canaria", "Tenerife", "Lanzarote", "Fuerteventura",
            "La Palma", "La Gomera", "El Hierro"
    };

    public static final String[] VALORES = {
            "", "GRAN_CANARIA", "TENERIFE", "LANZAROTE", "FUERTEVENTURA",
            "LA_PALMA", "LA_GOMERA", "EL_HIERRO"
    };

    public static String formatear(String valor) {
        if (valor == null || valor.isEmpty()) return "-";
        switch (valor) {
            case "GRAN_CANARIA":  return "Gran Canaria";
            case "TENERIFE":      return "Tenerife";
            case "LANZAROTE":     return "Lanzarote";
            case "FUERTEVENTURA": return "Fuerteventura";
            case "LA_PALMA":      return "La Palma";
            case "LA_GOMERA":     return "La Gomera";
            case "EL_HIERRO":     return "El Hierro";
            default:              return valor;
        }
    }
}
