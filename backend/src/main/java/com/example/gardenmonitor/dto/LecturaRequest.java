package com.example.gardenmonitor.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;

/**
 * DTO que representa la petición de lectura enviada por un dispositivo ESP32.
 * <p>
 * El ESP32 se identifica mediante su MAC address. El backend resuelve
 * el dispositivo y el árbol asociado a partir de ella.
 * </p>
 *
 * @author Richard Ortiz y Enrique Pérez
 * @version 1.0
 */
public class LecturaRequest {

    @NotNull
    @Pattern(regexp = "^([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}$",
            message = "MAC address debe tener formato XX:XX:XX:XX:XX:XX")
    private String macAddress;

    @NotNull
    @DecimalMin(value = "-50.00")
    @DecimalMax(value = "80.00")
    private BigDecimal temperatura;

    @NotNull
    @DecimalMin(value = "0.00")
    @DecimalMax(value = "100.00")
    private BigDecimal humedadAmbiente;

    @NotNull
    @DecimalMin(value = "0.00")
    @DecimalMax(value = "100.00")
    private BigDecimal humedadSuelo;

    @DecimalMin(value = "0.00")
    @DecimalMax(value = "10000.00")
    private BigDecimal co2;

    @DecimalMin(value = "0.00")
    @DecimalMax(value = "5000.00")
    private BigDecimal diametroTronco;

    public LecturaRequest() {}

    public String getMacAddress() { return macAddress; }
    public BigDecimal getTemperatura() { return temperatura; }
    public BigDecimal getHumedadAmbiente() { return humedadAmbiente; }
    public BigDecimal getHumedadSuelo() { return humedadSuelo; }
    public BigDecimal getCo2() { return co2; }
    public BigDecimal getDiametroTronco() { return diametroTronco; }

    public void setMacAddress(String macAddress) { this.macAddress = macAddress; }
    public void setTemperatura(BigDecimal temperatura) { this.temperatura = temperatura; }
    public void setHumedadAmbiente(BigDecimal humedadAmbiente) { this.humedadAmbiente = humedadAmbiente; }
    public void setHumedadSuelo(BigDecimal humedadSuelo) { this.humedadSuelo = humedadSuelo; }
    public void setCo2(BigDecimal co2) { this.co2 = co2; }
    public void setDiametroTronco(BigDecimal diametroTronco) { this.diametroTronco = diametroTronco; }
}
