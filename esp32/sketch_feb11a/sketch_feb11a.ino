#include <ESP32Servo.h>
#include <WiFi.h>
#include <HTTPClient.h>
#include <ArduinoJson.h>

// ============================================
// CONFIGURACIÓN PRIVADA (WiFi + Backend URL)
// ============================================
// Copia config.h.example como config.h y rellena tus datos.
// config.h está en .gitignore y no se sube al repositorio.
#include "config.h"

// Intervalo de envío de lecturas (milisegundos)
const unsigned long INTERVALO_LECTURA = 30000;  // 30s para testing (en producción: 900000 = 15 min)

// ============================================
// PINES
// ============================================
#define ledRojo 4
#define ledAmarillo 16
#define ledVerde 5
#define servoPin 25

const int sensorAgua = 39;

// ============================================
// VARIABLES GLOBALES
// ============================================
int estadoAnterior = 0;  // 0 = sin agua, 1 = con agua
unsigned long ultimoEnvio = 0;
String macAddress = "";

Servo miServo;
const int posicionSinAgua = 0;
const int posicionConAgua = 90;

// ============================================
// FUNCIONES WiFi
// ============================================

/**
 * Conecta a la red WiFi configurada.
 * Parpadea el LED amarillo mientras intenta conectar.
 * Enciende verde brevemente al conectar, o rojo si falla.
 */
void conectarWiFi() {
  Serial.print("Conectando a WiFi: ");
  Serial.println(WIFI_SSID);

  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);

  int intentos = 0;
  while (WiFi.status() != WL_CONNECTED && intentos < 20) {
    // Parpadeo amarillo mientras conecta
    digitalWrite(ledAmarillo, HIGH);
    delay(250);
    digitalWrite(ledAmarillo, LOW);
    delay(250);
    Serial.print(".");
    intentos++;
  }

  if (WiFi.status() == WL_CONNECTED) {
    Serial.println("\nWiFi conectado!");
    Serial.print("IP: ");
    Serial.println(WiFi.localIP());

    macAddress = WiFi.macAddress();
    Serial.print("MAC: ");
    Serial.println(macAddress);

    // Parpadeo verde para confirmar conexión
    for (int i = 0; i < 3; i++) {
      digitalWrite(ledVerde, HIGH);
      delay(200);
      digitalWrite(ledVerde, LOW);
      delay(200);
    }
  } else {
    Serial.println("\nError: No se pudo conectar a WiFi");
    // Parpadeo rojo para indicar error
    for (int i = 0; i < 5; i++) {
      digitalWrite(ledRojo, HIGH);
      delay(200);
      digitalWrite(ledRojo, LOW);
      delay(200);
    }
  }
}

// ============================================
// FUNCIONES de lectura y envío
// ============================================

/**
 * Normaliza el valor del sensor de agua (0-4095) a porcentaje (0-100).
 */
float normalizarHumedadSuelo(int valorSensor) {
  return map(valorSensor, 0, 4095, 0, 10000) / 100.0;
}

/**
 * Genera un valor aleatorio float dentro de un rango.
 * Se usa para simular sensores que aún no están conectados.
 */
float valorSimulado(float minimo, float maximo) {
  return minimo + (random(0, 1000) / 1000.0) * (maximo - minimo);
}

/**
 * Lee los sensores, construye el JSON y envía HTTP POST al backend.
 * - humedad_suelo: valor real del sensor de agua normalizado a %
 * - temperatura: simulada (15-35 °C)
 * - humedad_ambiente: simulada (40-80 %)
 */
void enviarLectura() {
  if (WiFi.status() != WL_CONNECTED) {
    Serial.println("WiFi desconectado. Reintentando...");
    conectarWiFi();
    if (WiFi.status() != WL_CONNECTED) {
      return;  // No se pudo reconectar
    }
  }

  // Leer sensor real
  int valorAgua = analogRead(sensorAgua);
  float humedadSuelo = normalizarHumedadSuelo(valorAgua);

  // Simular sensores no conectados
  float temperatura = valorSimulado(15.0, 35.0);
  float humedadAmbiente = valorSimulado(40.0, 80.0);

  // Construir JSON
  JsonDocument doc;
  doc["macAddress"] = macAddress;
  doc["temperatura"] = round(temperatura * 100) / 100.0;
  doc["humedadAmbiente"] = round(humedadAmbiente * 100) / 100.0;
  doc["humedadSuelo"] = round(humedadSuelo * 100) / 100.0;
  doc["co2"] = (char*)NULL;           // Sin sensor CO2
  doc["diametroTronco"] = (char*)NULL; // Sin dendrómetro

  String jsonString;
  serializeJson(doc, jsonString);

  Serial.println("Enviando lectura:");
  Serial.println(jsonString);

  // Enviar HTTP POST
  HTTPClient http;
  http.begin(BACKEND_URL);
  http.addHeader("Content-Type", "application/json");

  int httpCode = http.POST(jsonString);

  if (httpCode > 0) {
    Serial.print("Respuesta: ");
    Serial.print(httpCode);
    Serial.print(" - ");
    Serial.println(http.getString());
  } else {
    Serial.print("Error HTTP: ");
    Serial.println(http.errorToString(httpCode));
  }

  http.end();
}

// ============================================
// SETUP
// ============================================
void setup() {
  Serial.begin(115200);

  // Inicializar pines
  pinMode(ledRojo, OUTPUT);
  pinMode(ledAmarillo, OUTPUT);
  pinMode(ledVerde, OUTPUT);
  pinMode(sensorAgua, INPUT);

  miServo.attach(servoPin);
  miServo.write(posicionSinAgua);

  // Conectar WiFi
  conectarWiFi();

  // Semilla para valores simulados
  randomSeed(analogRead(0));
}

// ============================================
// LOOP
// ============================================
void loop() {
  int valorAgua = analogRead(sensorAgua);
  Serial.print("Valor sensor: ");
  Serial.println(valorAgua);

  // --- Lógica de LEDs y servo (existente) ---
  if (valorAgua > 1000) {
    // HAY AGUA - cambiar a verde
    if (estadoAnterior == 0) {
      digitalWrite(ledRojo, LOW);
      for (int i = 0; i < 1; i++) {  // Parpadea 1 vez
        digitalWrite(ledAmarillo, HIGH);
        delay(500);
        digitalWrite(ledAmarillo, LOW);
        delay(500);
      }
      miServo.write(posicionConAgua);
      estadoAnterior = 1;
    }
    digitalWrite(ledVerde, HIGH);
    digitalWrite(ledAmarillo, LOW);
    digitalWrite(ledRojo, LOW);

  } else {
    // NO HAY AGUA - cambiar a rojo
    if (estadoAnterior == 1) {
      digitalWrite(ledVerde, LOW);
      for (int i = 0; i < 1; i++) {  // Parpadea 1 vez
        digitalWrite(ledAmarillo, HIGH);
        delay(500);
        digitalWrite(ledAmarillo, LOW);
        delay(500);
      }
      miServo.write(posicionSinAgua);
      estadoAnterior = 0;
    }
    digitalWrite(ledRojo, HIGH);
    digitalWrite(ledAmarillo, LOW);
    digitalWrite(ledVerde, LOW);
  }

  // --- Envío periódico al backend ---
  unsigned long ahora = millis();
  if (ahora - ultimoEnvio >= INTERVALO_LECTURA) {
    enviarLectura();
    ultimoEnvio = ahora;
  }

  delay(500);  // Leer sensor cada medio segundo
}
