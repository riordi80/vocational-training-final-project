# ESP32 - Proyecto Árboles

Firmware para ESP32 que recopila datos de sensores ambientales y de suelo, transmitiéndolos al backend mediante WiFi.

## Tecnologías

- **Lenguaje**: C/C++ (Arduino Framework)
- **Plataforma**: ESP32 (ESP-WROOM-32 o compatible)
- **IDE**: Arduino IDE 2.x / PlatformIO (VS Code)
- **Conectividad**: WiFi 2.4GHz
- **Protocolo**: HTTP/HTTPS REST
- **Formato de datos**: JSON (ArduinoJson)

## Sensores Utilizados

- **DHT22**: Sensor de temperatura y humedad ambiental (digital)
- **Sensor de Humedad de Suelo Capacitivo**: Medición de humedad del suelo (analógico)
- **PH-4502C**: Sensor de pH del suelo (analógico)
- **HC-SR04**: Sensor ultrasónico para medir nivel de agua (digital)

## Características

- Conectividad WiFi para transmisión de datos
- Lectura multi-sensor (temperatura, humedad, pH, nivel de agua)
- Modo Deep Sleep para ahorro energético
- Reintentos automáticos en caso de fallo de conexión
- Autenticación con token API
- Intervalos de lectura configurables (15-30 minutos)
- Buffer local para lecturas offline (opcional)
- Optimización de batería con despertar programado

## Estructura del Proyecto

```
esp32/
├── src/
│   ├── main.cpp              # Programa principal
│   ├── config.h              # Configuración WiFi y API (NO COMMITEAR)
│   ├── config.example.h      # Plantilla de configuración
│   ├── sensors.h             # Declaraciones de funciones de sensores
│   ├── sensors.cpp           # Implementación de lectura de sensores
│   ├── api_client.h          # Cliente HTTP para backend
│   ├── api_client.cpp        # Implementación de peticiones HTTP
│   └── utils.h/.cpp          # Funciones auxiliares
├── lib/                      # Librerías externas (si aplica)
├── platformio.ini            # Configuración de PlatformIO
├── .gitignore
└── README.md
```

## Requisitos Previos

### Hardware

- **Placa ESP32**: ESP-WROOM-32 o compatible
- **Sensores**:
  - DHT22 (temperatura y humedad)
  - Sensor capacitivo de humedad de suelo
  - Módulo PH-4502C
  - HC-SR04 (sensor ultrasónico)
- **Cables jumper** (macho-hembra y macho-macho)
- **Protoboard**
- **Cable USB** (para programación y alimentación)
- **Fuente de alimentación**: USB, batería LiPo, panel solar con regulador

### Software

- **PlatformIO** (recomendado) o Arduino IDE 2.x
- **Drivers USB**: CP2102 o CH340 (según tu ESP32)
- **Python 3** (para esptool si usas PlatformIO)

## Instalación y Configuración

### Opción 1: PlatformIO (Recomendado)

#### 1. Instalar Visual Studio Code

Descargar desde: [https://code.visualstudio.com/](https://code.visualstudio.com/)

#### 2. Instalar extensión PlatformIO

- Abrir VS Code
- Extensions → Buscar "PlatformIO IDE" → Instalar
- Reiniciar VS Code

#### 3. Clonar e inicializar proyecto

```bash
git clone https://github.com/riordi80/vocational-training-final-project
cd esp32
pio init --board esp32dev
```

#### 4. Configurar `platformio.ini`

Editar archivo `platformio.ini`:

```ini
[env:esp32dev]
platform = espressif32
board = esp32dev
framework = arduino
monitor_speed = 115200

lib_deps =
    adafruit/DHT sensor library@^1.4.4
    adafruit/Adafruit Unified Sensor@^1.1.9
    bblanchon/ArduinoJson@^6.21.3
```

#### 5. Compilar y subir firmware

```bash
pio run --target upload
pio device monitor
```

### Opción 2: Arduino IDE

#### 1. Instalar Arduino IDE

Descargar desde: [https://www.arduino.cc/en/software](https://www.arduino.cc/en/software)

#### 2. Añadir soporte para ESP32

- File → Preferences → Additional Board Manager URLs:
  ```
  https://espressif.github.io/arduino-esp32/package_esp32_index.json
  ```
- Tools → Board → Boards Manager → Buscar "ESP32" → Instalar "ESP32 by Espressif Systems"

#### 3. Instalar librerías

- Tools → Manage Libraries
- Instalar:
  - DHT sensor library (by Adafruit)
  - Adafruit Unified Sensor
  - ArduinoJson (by Benoit Blanchon)

#### 4. Configurar placa

- Tools → Board → ESP32 Arduino → ESP32 Dev Module
- Tools → Upload Speed → 115200
- Tools → Port → Seleccionar tu puerto (COM3, /dev/ttyUSB0, etc.)

#### 5. Subir sketch

- Sketch → Upload

## Configuración del Firmware

### Crear archivo `src/config.h`

**IMPORTANTE**: Este archivo contiene credenciales y NO debe commitearse.

```cpp
#ifndef CONFIG_H
#define CONFIG_H

// WiFi
const char* WIFI_SSID = "tu_wifi_ssid";
const char* WIFI_PASSWORD = "tu_wifi_password";

// API Backend
const char* API_BASE_URL = "http://tu-backend-url:8080/api";
const char* DEVICE_ID = "ESP32_001";               // Identificador único del dispositivo
const char* API_TOKEN = "tu_token_api";            // Token JWT o API key

// Pines de sensores
#define DHT_PIN 4                                   // GPIO4 para DHT22
#define SOIL_MOISTURE_PIN 34                        // GPIO34 (ADC1_CH6) para humedad suelo
#define PH_SENSOR_PIN 35                            // GPIO35 (ADC1_CH7) para pH
#define WATER_LEVEL_TRIG_PIN 5                      // GPIO5 para trigger HC-SR04
#define WATER_LEVEL_ECHO_PIN 18                     // GPIO18 para echo HC-SR04

// Configuración de lectura
#define SLEEP_TIME_MINUTES 15                       // Intervalo entre lecturas
#define DHT_TYPE DHT22                              // Tipo de sensor DHT

// Configuración de Deep Sleep
#define uS_TO_S_FACTOR 1000000ULL                   // Factor conversión μs a s
#define TIME_TO_SLEEP (SLEEP_TIME_MINUTES * 60)     // Tiempo en segundos

#endif
```

### Crear archivo `src/config.example.h` (template público)

```cpp
#ifndef CONFIG_H
#define CONFIG_H

// WiFi
const char* WIFI_SSID = "YOUR_WIFI_SSID";
const char* WIFI_PASSWORD = "YOUR_WIFI_PASSWORD";

// API Backend
const char* API_BASE_URL = "http://your-backend-url:8080/api";
const char* DEVICE_ID = "ESP32_XXX";
const char* API_TOKEN = "your_api_token_here";

// Pines de sensores
#define DHT_PIN 4
#define SOIL_MOISTURE_PIN 34
#define PH_SENSOR_PIN 35
#define WATER_LEVEL_TRIG_PIN 5
#define WATER_LEVEL_ECHO_PIN 18

// Configuración
#define SLEEP_TIME_MINUTES 15
#define DHT_TYPE DHT22

#define uS_TO_S_FACTOR 1000000ULL
#define TIME_TO_SLEEP (SLEEP_TIME_MINUTES * 60)

#endif
```

**Añadir a `.gitignore`**:

```
src/config.h
secrets.h
```

## Esquema de Conexiones

### Tabla de Pines

| Componente           | Pin ESP32      | Voltaje | Notas                          |
|----------------------|----------------|---------|--------------------------------|
| DHT22 Data           | GPIO 4         | 3.3V    | Resistencia pull-up 10kΩ      |
| DHT22 VCC            | 3.3V           | -       | -                              |
| DHT22 GND            | GND            | -       | -                              |
| Humedad Suelo Analog | GPIO 34 (ADC)  | 3.3V    | Sensor capacitivo              |
| Humedad Suelo VCC    | 3.3V           | -       | -                              |
| Humedad Suelo GND    | GND            | -       | -                              |
| pH Sensor Analog     | GPIO 35 (ADC)  | 3.3V    | **Divisor de voltaje si es 5V**|
| pH Sensor VCC        | 5V o 3.3V      | -       | Verificar voltaje del módulo   |
| pH Sensor GND        | GND            | -       | -                              |
| HC-SR04 Trigger      | GPIO 5         | 3.3V    | -                              |
| HC-SR04 Echo         | GPIO 18        | 3.3V    | **Divisor de voltaje si es 5V**|
| HC-SR04 VCC          | 5V             | -       | -                              |
| HC-SR04 GND          | GND            | -       | -                              |

### Divisor de Voltaje (Para sensores 5V)

Si un sensor funciona a 5V pero su salida es 5V, necesitas un divisor de voltaje para proteger los pines ADC del ESP32 (que soportan máx 3.3V).

**Ejemplo**: Para Echo del HC-SR04

```
5V ───[R1 10kΩ]─┬─── GPIO18 (ESP32)
                │
              [R2 20kΩ]
                │
               GND
```

Fórmula: `Vout = Vin * (R2 / (R1 + R2))`

## Código Principal

### `src/main.cpp`

```cpp
#include <Arduino.h>
#include <WiFi.h>
#include <HTTPClient.h>
#include <DHT.h>
#include <ArduinoJson.h>
#include "config.h"

// Inicializar sensor DHT
DHT dht(DHT_PIN, DHT_TYPE);

// Variables globales
float temperatura = 0.0;
float humedadSuelo = 0.0;
float ph = 0.0;
float nivelAgua = 0.0;

// Prototipos de funciones
void conectarWiFi();
void leerSensores();
float leerHumedadSuelo();
float leerPH();
float leerNivelAgua();
bool enviarDatos();
void entrarDeepSleep();

void setup() {
  Serial.begin(115200);
  delay(1000);

  Serial.println("=== ESP32 Proyecto Árboles ===");

  // Configurar pines
  pinMode(WATER_LEVEL_TRIG_PIN, OUTPUT);
  pinMode(WATER_LEVEL_ECHO_PIN, INPUT);

  // Inicializar DHT
  dht.begin();

  // Conectar WiFi
  conectarWiFi();

  // Leer sensores
  leerSensores();

  // Enviar datos al backend
  if (enviarDatos()) {
    Serial.println("Datos enviados correctamente");
  } else {
    Serial.println("Error al enviar datos");
  }

  // Entrar en Deep Sleep
  entrarDeepSleep();
}

void loop() {
  // No se ejecuta debido a Deep Sleep
}

void conectarWiFi() {
  Serial.print("Conectando a WiFi: ");
  Serial.println(WIFI_SSID);

  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);

  int intentos = 0;
  while (WiFi.status() != WL_CONNECTED && intentos < 20) {
    delay(500);
    Serial.print(".");
    intentos++;
  }

  if (WiFi.status() == WL_CONNECTED) {
    Serial.println("\nWiFi conectado");
    Serial.print("IP: ");
    Serial.println(WiFi.localIP());
  } else {
    Serial.println("\nError al conectar WiFi");
  }
}

void leerSensores() {
  Serial.println("\n--- Leyendo sensores ---");

  // Leer DHT22
  temperatura = dht.readTemperature();
  if (isnan(temperatura)) {
    Serial.println("Error al leer DHT22");
    temperatura = 0.0;
  } else {
    Serial.print("Temperatura: ");
    Serial.print(temperatura);
    Serial.println(" °C");
  }

  // Leer humedad del suelo
  humedadSuelo = leerHumedadSuelo();
  Serial.print("Humedad Suelo: ");
  Serial.print(humedadSuelo);
  Serial.println(" %");

  // Leer pH
  ph = leerPH();
  Serial.print("pH: ");
  Serial.println(ph);

  // Leer nivel de agua
  nivelAgua = leerNivelAgua();
  Serial.print("Nivel Agua: ");
  Serial.print(nivelAgua);
  Serial.println(" cm");
}

float leerHumedadSuelo() {
  int valorAnalogico = analogRead(SOIL_MOISTURE_PIN);

  // Calibración (ajustar según tu sensor)
  const int VALOR_SECO = 3000;   // Sensor en aire
  const int VALOR_MOJADO = 1000;  // Sensor en agua

  // Convertir a porcentaje
  float porcentaje = map(valorAnalogico, VALOR_SECO, VALOR_MOJADO, 0, 100);
  porcentaje = constrain(porcentaje, 0, 100);

  return porcentaje;
}

float leerPH() {
  int valorAnalogico = analogRead(PH_SENSOR_PIN);

  // Convertir lectura ADC a voltaje
  float voltaje = valorAnalogico * (3.3 / 4095.0);

  // Calibración (ajustar con soluciones buffer pH 4.0, 7.0, 10.0)
  // Fórmula de ejemplo (necesita calibración específica)
  float phValue = 7.0 + ((2.5 - voltaje) / 0.18);

  phValue = constrain(phValue, 0, 14);

  return phValue;
}

float leerNivelAgua() {
  digitalWrite(WATER_LEVEL_TRIG_PIN, LOW);
  delayMicroseconds(2);

  digitalWrite(WATER_LEVEL_TRIG_PIN, HIGH);
  delayMicroseconds(10);
  digitalWrite(WATER_LEVEL_TRIG_PIN, LOW);

  long duracion = pulseIn(WATER_LEVEL_ECHO_PIN, HIGH);

  // Calcular distancia en cm
  float distancia = duracion * 0.034 / 2;

  return distancia;
}

bool enviarDatos() {
  if (WiFi.status() != WL_CONNECTED) {
    Serial.println("WiFi no conectado");
    return false;
  }

  HTTPClient http;

  // Construir URL
  String url = String(API_BASE_URL) + "/dispositivos/" + String(DEVICE_ID) + "/lecturas";

  Serial.print("URL: ");
  Serial.println(url);

  http.begin(url);
  http.addHeader("Content-Type", "application/json");
  http.addHeader("Authorization", "Bearer " + String(API_TOKEN));

  // Crear JSON
  StaticJsonDocument<256> doc;
  doc["temperatura"] = temperatura;
  doc["humedad_suelo"] = humedadSuelo;
  doc["ph"] = ph;
  doc["nivel_agua"] = nivelAgua;
  doc["timestamp"] = millis();

  String jsonString;
  serializeJson(doc, jsonString);

  Serial.println("Enviando: " + jsonString);

  // Enviar POST
  int httpCode = http.POST(jsonString);

  Serial.print("HTTP Code: ");
  Serial.println(httpCode);

  if (httpCode > 0) {
    String response = http.getString();
    Serial.println("Respuesta: " + response);
  }

  http.end();

  return (httpCode == 200 || httpCode == 201);
}

void entrarDeepSleep() {
  Serial.println("\n--- Entrando en Deep Sleep ---");
  Serial.print("Despertar en ");
  Serial.print(SLEEP_TIME_MINUTES);
  Serial.println(" minutos");

  esp_sleep_enable_timer_wakeup(TIME_TO_SLEEP * uS_TO_S_FACTOR);

  Serial.flush();
  esp_deep_sleep_start();
}
```

## Calibración de Sensores

### Sensor de Humedad de Suelo

1. **Medición en aire** (seco): Anotar valor analógico (~3000-4095)
2. **Medición en agua**: Anotar valor analógico (~1000-1500)
3. Actualizar constantes `VALOR_SECO` y `VALOR_MOJADO` en el código

### Sensor de pH

1. Obtener **soluciones buffer**: pH 4.0, 7.0, 10.0
2. Sumergir sensor en pH 7.0, anotar voltaje
3. Sumergir en pH 4.0, anotar voltaje
4. Calcular pendiente: `(pH2 - pH1) / (V2 - V1)`
5. Ajustar fórmula en función `leerPH()`

Ejemplo de calibración:
```cpp
// Si a 2.5V → pH 7.0
// Y a 2.0V → pH 4.0
// Pendiente = (7-4) / (2.5-2.0) = 3 / 0.5 = 6 pH/V

float phValue = 7.0 + ((2.5 - voltaje) * 6.0);
```

## Gestión de Energía

### Consumo

- **Modo activo**: ~160-260 mA
- **Deep Sleep**: ~10-20 µA

### Deep Sleep

El ESP32 entra en Deep Sleep tras enviar datos, reduciendo el consumo a niveles mínimos. Se despierta automáticamente tras el tiempo configurado.

```cpp
esp_sleep_enable_timer_wakeup(TIME_TO_SLEEP * uS_TO_S_FACTOR);
esp_deep_sleep_start();
```

### Opciones de Alimentación

1. **USB**: 5V constante (desarrollo)
2. **Batería LiPo + Panel Solar**: Ideal para despliegue exterior
3. **Fuente externa regulada**: 5V/3.3V con suficiente amperaje

**Recomendación para exteriores**:
- Panel solar 5-6W
- Batería LiPo 3.7V 2000-5000 mAh
- Módulo de carga TP4056 con protección

## Comunicación con Backend

### Endpoint

```
POST /api/dispositivos/{DEVICE_ID}/lecturas
Content-Type: application/json
Authorization: Bearer {API_TOKEN}
```

### Payload JSON

```json
{
  "temperatura": 23.5,
  "humedad_suelo": 45.2,
  "ph": 6.8,
  "nivel_agua": 12.5,
  "timestamp": 1234567890
}
```

### Respuesta esperada

```json
{
  "id": 1234,
  "mensaje": "Lectura registrada correctamente"
}
```

## Solución de Problemas

### No conecta al WiFi

- Verificar SSID y contraseña en `config.h`
- Asegurar que es WiFi 2.4 GHz (ESP32 NO soporta 5 GHz)
- Verificar intensidad de señal

### Error al subir firmware

- Mantener pulsado botón **BOOT** durante la subida
- Verificar puerto COM correcto
- Reinstalar drivers USB (CP2102/CH340)

### Lecturas incorrectas

- Verificar conexiones de cables
- Revisar voltajes (3.3V vs 5V)
- Calibrar sensores con valores conocidos
- Añadir delays entre lecturas de ADC

### No envía datos al backend

- Verificar que el backend esté accesible (ping/curl)
- Revisar token de autenticación
- Verificar formato JSON
- Comprobar logs en Serial Monitor (115200 baud)

## Monitoreo Serial

### PlatformIO

```bash
pio device monitor
```

### Arduino IDE

- Tools → Serial Monitor
- Configurar velocidad: **115200 baud**

Ejemplo de salida:

```
=== ESP32 Proyecto Árboles ===
Conectando a WiFi: MiWiFi
........
WiFi conectado
IP: 192.168.1.100

--- Leyendo sensores ---
Temperatura: 23.5 °C
Humedad Suelo: 45.2 %
pH: 6.8
Nivel Agua: 12.5 cm

URL: http://192.168.1.50:8080/api/dispositivos/ESP32_001/lecturas
Enviando: {"temperatura":23.5,"humedad_suelo":45.2,"ph":6.8,"nivel_agua":12.5,"timestamp":45000}
HTTP Code: 201
Respuesta: {"id":1234,"mensaje":"Lectura registrada correctamente"}
Datos enviados correctamente

--- Entrando en Deep Sleep ---
Despertar en 15 minutos
```

## Testing

```bash
# Compilar sin subir
pio run

# Compilar y subir
pio run --target upload

# Monitor serial
pio device monitor
```

## Despliegue en Campo

1. **Protección física**: Caja impermeable IP65
2. **Alimentación solar**: Panel + batería LiPo
3. **Antena WiFi externa**: Mejorar alcance si es necesario
4. **Protección contra rayos**: Si está en exteriores expuestos
5. **Mantenimiento**: Limpieza de sensores cada 2-4 semanas

## Estado

En desarrollo (opcional - después del 8 de diciembre)

## Documentación Relacionada

- [Índice de Documentación](../docs/00.%20INDICE.md) - Índice completo de la documentación
- [Hoja de Ruta Completa](../docs/02.%20HOJA_DE_RUTA.md) - Planificación del proyecto
- [Especificación Técnica](../docs/03.%20ESPECIFICACION_TECNICA.md) - Requisitos y arquitectura
- [Backend README](../backend/README.md) - API REST con Spring Boot
- [Componentes Hardware](../docs/Componentes%20para%20ESP32/Componentes.png) - Lista de componentes y precios

## Contacto

Proyecto Final DAM 2025-2026