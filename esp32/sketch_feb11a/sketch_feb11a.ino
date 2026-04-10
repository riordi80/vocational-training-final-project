#include <Arduino.h>
#include <Wire.h>
#include <WiFi.h>
#include <HTTPClient.h>
#include <ArduinoJson.h>
#include "config.h"

// ============================================
// PINES
// ============================================
#define RX_PIN 16
#define TX_PIN 17
#define SHT40_ADDR 0x44
const int pinTierra = 4;
const int pinLDR1   = 5;
const int pinLDR2   = 35;  // TODO: verificar pin con el hardware final

// ============================================
// CONSTANTES
// ============================================
const unsigned long INTERVALO_LECTURA = 30000;  // 30s para testing (en producción: 900000 = 15 min)
const int SECO   = 3000;
const int HUMEDO = 1000;
#define UMBRAL_CO2 1100

// ============================================
// VARIABLES GLOBALES
// ============================================
unsigned long ultimoEnvio = 0;
float temperatura     = 0.0;
float humedadAmbiente = 0.0;
float humedadSuelo    = 0.0;
int   co2             = 0;
int   luz1            = 0;
int   luz2            = 0;

byte cmdCO2[9]      = {0xFF, 0x01, 0x86, 0, 0, 0, 0, 0, 0x79};
byte responseCO2[9] = {0};

// ============================================
// PROTOTIPOS
// ============================================
void conectarWiFi();
void leerSHT40();
void leerHumedadSuelo();
void leerCO2();
void leerLDR();
void enviarLectura();

// ============================================
// SETUP
// ============================================
void setup() {
  Serial.begin(115200);

  Wire.begin(8, 9);                                    // SDA=8, SCL=9 para SHT40
  Serial2.begin(9600, SERIAL_8N1, RX_PIN, TX_PIN);    // UART para MH-Z19D
  analogReadResolution(12);                            // ADC 0-4095

  Serial.println("Sistema de sensores iniciado");
  Serial.println("SHT40 + Humedad suelo + CO2 MH-Z19D");
  Serial.println("=====================================");

  conectarWiFi();
}

// ============================================
// LOOP
// ============================================
void loop() {
  leerSHT40();
  leerHumedadSuelo();
  leerCO2();
  leerLDR();
  Serial.println("=====================================");

  unsigned long ahora = millis();
  if (ahora - ultimoEnvio >= INTERVALO_LECTURA) {
    enviarLectura();
    ultimoEnvio = ahora;
  }

  delay(5000);
}

// ============================================
// WiFi
// ============================================
void conectarWiFi() {
  Serial.print("Conectando a WiFi: ");
  Serial.println(WIFI_SSID);

  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println();
  Serial.print("WiFi conectado. IP: ");
  Serial.println(WiFi.localIP());
  Serial.print("MAC: ");
  Serial.println(WiFi.macAddress());
}

// ============================================
// LECTURA SHT40 (I2C)
// Actualiza variables globales: temperatura, humedadAmbiente
// ============================================
void leerSHT40() {
  Wire.beginTransmission(SHT40_ADDR);
  Wire.write(0xFD);  // Medición de alta precisión

  if (Wire.endTransmission() != 0) {
    Serial.println("Error: no se pudo comunicar con el SHT40");
    return;
  }

  delay(20);
  Wire.requestFrom(SHT40_ADDR, 6);

  if (Wire.available() == 6) {
    uint16_t rawTemp = (Wire.read() << 8) | Wire.read();
    Wire.read();  // CRC temperatura (ignorado)
    uint16_t rawHum  = (Wire.read() << 8) | Wire.read();
    Wire.read();  // CRC humedad (ignorado)

    temperatura     = -45.0 + 175.0 * (rawTemp / 65535.0);
    humedadAmbiente = -6.0  + 125.0 * (rawHum  / 65535.0);
    humedadAmbiente = constrain(humedadAmbiente, 0.0, 100.0);

    Serial.print("Temperatura: ");
    Serial.print(temperatura, 2);
    Serial.print(" °C | Humedad ambiente: ");
    Serial.print(humedadAmbiente, 2);
    Serial.println(" %");
  } else {
    Serial.println("Error: lectura incompleta del SHT40");
  }
}

// ============================================
// LECTURA HUMEDAD DE SUELO (ADC)
// Actualiza variable global: humedadSuelo
// ============================================
void leerHumedadSuelo() {
  int lecturaBruta = analogRead(pinTierra);
  int porcentaje   = map(lecturaBruta, SECO, HUMEDO, 0, 100);
  porcentaje       = constrain(porcentaje, 0, 100);
  humedadSuelo     = (float)porcentaje;

  Serial.print("ADC suelo: ");
  Serial.print(lecturaBruta);
  Serial.print(" | Humedad suelo: ");
  Serial.print(porcentaje);
  Serial.println(" %");
}

// ============================================
// LECTURA CO2 MH-Z19D (UART manual)
// Actualiza variable global: co2
// ============================================
void leerCO2() {
  // Limpiar buffer
  while (Serial2.available()) Serial2.read();

  Serial2.write(cmdCO2, 9);
  delay(50);

  if (Serial2.available() >= 9) {
    Serial2.readBytes(responseCO2, 9);

    if (responseCO2[0] == 0xFF && responseCO2[1] == 0x86) {
      co2 = (responseCO2[2] << 8) | responseCO2[3];

      Serial.print("CO2: ");
      Serial.print(co2);
      Serial.println(" ppm");

      if (co2 > UMBRAL_CO2) {
        Serial.println("AVISO: concentracion de CO2 elevada");
      }
    } else {
      Serial.println("Error: respuesta no valida del MH-Z19D");
    }
  } else {
    Serial.println("Error: sin respuesta del MH-Z19D");
  }
}

// ============================================
// LECTURA LDR (ADC)
// Actualiza variables globales: luz1, luz2
// Devuelve porcentaje 0-100 (0=oscuro, 100=máxima luz)
// ============================================
void leerLDR() {
  int rawLuz1 = analogRead(pinLDR1);
  int rawLuz2 = analogRead(pinLDR2);

  luz1 = map(rawLuz1, 0, 4095, 0, 100);
  luz2 = map(rawLuz2, 0, 4095, 0, 100);
  luz1 = constrain(luz1, 0, 100);
  luz2 = constrain(luz2, 0, 100);

  Serial.print("LDR1 (ADC=");
  Serial.print(rawLuz1);
  Serial.print("): ");
  Serial.print(luz1);
  Serial.print(" % | LDR2 (ADC=");
  Serial.print(rawLuz2);
  Serial.print("): ");
  Serial.print(luz2);
  Serial.println(" %");
}

// ============================================
// ENVÍO AL BACKEND
// ============================================
void enviarLectura() {
  if (WiFi.status() != WL_CONNECTED) {
    Serial.println("WiFi desconectado. Reintentando...");
    conectarWiFi();
    if (WiFi.status() != WL_CONNECTED) {
      Serial.println("No se pudo reconectar. Envío cancelado.");
      return;
    }
  }

  JsonDocument doc;
  doc["macAddress"]     = WiFi.macAddress();
  doc["temperatura"]    = round(temperatura     * 100) / 100.0;
  doc["humedadAmbiente"]= round(humedadAmbiente * 100) / 100.0;
  doc["humedadSuelo"]   = round(humedadSuelo    * 100) / 100.0;
  doc["co2"]            = co2;
  doc["luz1"]           = luz1;
  doc["luz2"]           = luz2;

  String jsonString;
  serializeJson(doc, jsonString);

  Serial.println("Enviando lectura:");
  Serial.println(jsonString);

  HTTPClient http;
  http.begin(BACKEND_URL);
  http.addHeader("Content-Type", "application/json");

  int httpCode = http.POST(jsonString);

  if (httpCode == 201) {
    Serial.println("[HTTP] Lectura registrada (201)");
  } else if (httpCode > 0) {
    Serial.print("[HTTP] Respuesta: ");
    Serial.print(httpCode);
    Serial.print(" - ");
    Serial.println(http.getString());
  } else {
    Serial.print("[HTTP] Error: ");
    Serial.println(http.errorToString(httpCode));
  }

  http.end();
}
