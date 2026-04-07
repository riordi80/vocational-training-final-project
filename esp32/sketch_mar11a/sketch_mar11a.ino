#include <Arduino.h>
#include <MHZ19.h>
#include <WiFi.h>
#include <HTTPClient.h>
#include <ArduinoJson.h>

// ── Pines ────────────────────────────────────────────────────────────────────
#define RX_PIN 16
#define TX_PIN 17
const int pinTierra = 4;
const int pinLDR = 5;

// ── WiFi y Backend 
#include "config.h"

// ── Sensores ──────────────────────────────────────────────────────────────────
MHZ19 myMHZ19;
HardwareSerial mySerial(1);

// ── Intervalo de envío ────────────────────────────────────────────────────────
const unsigned long INTERVALO_MS = 30000; // 30 segundos
unsigned long ultimoEnvio = 0;

// ─────────────────────────────────────────────────────────────────────────────

void conectarWiFi() {
  Serial.print("Conectando a WiFi");
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println();
  Serial.print("WiFi conectado. IP: ");
  Serial.println(WiFi.localIP());
}

bool enviarLectura(int co2, float temperatura, int humedadSuelo) {
  if (WiFi.status() != WL_CONNECTED) {
    Serial.println("[WiFi] Desconectado, reconectando...");
    conectarWiFi();
  }

  // Construir JSON — campos que espera LecturaRequest
  // humedadAmbiente no tienes sensor aún → se envía 0.0 como placeholder
  StaticJsonDocument<256> doc;
  doc["temperatura"]     = temperatura;
  doc["humedadAmbiente"] = 0.0;        // TODO: añadir sensor DHT/SHT cuando esté disponible
  doc["humedadSuelo"]    = humedadSuelo;
  doc["co2"]             = co2;

  String payload;
  serializeJson(doc, payload);

  HTTPClient http;
  http.begin(BACKEND_URL);
  http.addHeader("Content-Type", "application/json");

  Serial.println("[HTTP] Enviando: " + payload);
  int httpCode = http.POST(payload);

  bool ok = false;
  if (httpCode == 201) {
    Serial.println("[HTTP] Lectura registrada correctamente (201)");
    ok = true;
  } else if (httpCode > 0) {
    Serial.printf("[HTTP] Respuesta inesperada: %d — %s\n",
                  httpCode, http.getString().c_str());
  } else {
    Serial.printf("[HTTP] Error de conexión: %s\n",
                  http.errorToString(httpCode).c_str());
  }

  http.end();
  return ok;
}

// ─────────────────────────────────────────────────────────────────────────────

void setup() {
  Serial.begin(115200);

  mySerial.begin(9600, SERIAL_8N1, RX_PIN, TX_PIN);
  myMHZ19.begin(mySerial);
  myMHZ19.autoCalibration();

  Serial.println("Sensores iniciados...");
  conectarWiFi();
}

void loop() {
  unsigned long ahora = millis();
  if (ahora - ultimoEnvio < INTERVALO_MS) return; // Esperar intervalo
  ultimoEnvio = ahora;

  // ── Leer CO2 y temperatura interna del MH-Z19 ────────────────────────────
  int co2         = myMHZ19.getCO2();
  int tempSensor  = myMHZ19.getTemperature();

  // ── Leer humedad de suelo ────────────────────────────────────────────────
  int lecturaTierra = analogRead(pinTierra);
  int humedadSuelo  = map(lecturaTierra, 3000, 1200, 0, 100);
  humedadSuelo      = constrain(humedadSuelo, 0, 100);
  int lecturaLDR  = analogRead(pinLDR);
  int luz         = map(lecturaLDR, 0, 4095, 0, 100); // 0% oscuro, 100% máxima luz

  // ── Log local ────────────────────────────────────────────────────────────
  Serial.printf("CO2: %d ppm | Temp: %d°C | Humedad Suelo: %d%%\n | Luz: %d %%\n",
                co2, tempSensor, humedadSuelo, luz);

  // ── Enviar al backend ─────────────────────────────────────────────────────
  enviarLectura(co2, (float)tempSensor, humedadSuelo);
}