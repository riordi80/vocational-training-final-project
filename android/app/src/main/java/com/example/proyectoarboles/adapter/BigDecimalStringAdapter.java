package com.example.proyectoarboles.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
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
            } catch (Exception e) {
                out.nullValue(); // Fallback
            }
        }
    }

    @Override
    public String read(JsonReader in) throws IOException {
        try {
            // Backend envía BigDecimal → lo leemos como número
            BigDecimal decimal = new BigDecimal(in.nextString());
            return decimal.toPlainString(); // Volvemos a String limpio
        } catch (Exception e) {
            return null;
        }
    }



}
