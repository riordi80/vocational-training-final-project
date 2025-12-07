package com.example.proyectoarboles.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.math.BigDecimal;

public class BigDecimalStringAdapter extends TypeAdapter<String> {

    @Override
    public void write(JsonWriter out, String value) throws IOException {
        if (value == null || value.isEmpty()) {
            out.nullValue();
        } else {
            try {
                // Convertir String -> BigDecimal para enviar al backend
                BigDecimal decimal = new BigDecimal(value);
                out.value(decimal);
            } catch (NumberFormatException e) {
                // Si no es un número válido, enviar como string
                out.value(value);
            }
        }
    }

    @Override
    public String read(JsonReader in) throws IOException {
        // Verificar si el valor es null
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        try {
            // Intentar leer como número
            if (in.peek() == JsonToken.NUMBER) {
                // Leer como double y convertir a string
                double numero = in.nextDouble();
                return String.valueOf(numero);
            } else if (in.peek() == JsonToken.STRING) {
                // Leer directamente como string
                String value = in.nextString();

                // Intentar parsearlo como número para verificar
                try {
                    new BigDecimal(value);
                    return value;
                } catch (NumberFormatException e) {
                    // Si no es número, devolver el string tal cual
                    return value;
                }
            } else {
                // Cualquier otro tipo, intentar leerlo como string
                return in.nextString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}