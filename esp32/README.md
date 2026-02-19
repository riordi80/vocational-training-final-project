# ESP32 - Proyecto Árboles

**Estado**: Implementado (fase inicial)

Componente IoT del sistema Proyecto Árboles para recopilación de datos de sensores ambientales mediante dispositivos ESP32. El firmware está operativo y enviando lecturas periódicas al backend vía HTTP REST.

## Descripción

Este componente implementa la parte IoT del sistema Proyecto Árboles. Realiza lecturas periódicas de sensores ambientales instalados en los árboles y las envía automáticamente al backend.

## Tecnologías

- **Plataforma**: ESP32 (Arduino Framework)
- **Lenguaje**: C/C++
- **Conectividad**: WiFi 2.4GHz
- **Protocolo**: HTTP REST hacia el backend
- **Formato de datos**: JSON
- **Librerías**: ESP32Servo, WiFi, HTTPClient, ArduinoJson, DHT

## Hardware Implementado

| Componente | Pin | Función |
|---|---|---|
| DHT22 | GPIO 33 | Temperatura y humedad ambiente |
| Sensor de agua analógico | GPIO 39 | Humedad de suelo (0-4095 normalizado a 0-100%) |
| LED Rojo | GPIO 4 | Indicador de error |
| LED Amarillo | GPIO 16 | Indicador de conexión WiFi en progreso |
| LED Verde | GPIO 5 | Indicador de operación correcta |
| Servo SG90 | GPIO 25 | Control de riego |

## Funcionalidades Implementadas

- Conexión a WiFi con reintentos (LED amarillo durante conexión)
- Lectura periódica de sensores (intervalo configurable: 30 s en testing, 15 min en producción)
- Construcción de payload JSON con `macAddress`, temperatura, humedad ambiente y humedad de suelo
- Envío HTTP POST a `POST /api/lecturas` del backend
- Indicadores LED de estado (verde = ok, rojo = error)
- Reconexión WiFi automática ante pérdida de señal

## Componentes Considerados

Se han evaluado los siguientes componentes para el sistema de monitoreo:

### Sensores
- **Temperatura/Ambiente**: DHT11 o SHT31 (más preciso) - 12€
- **Humedad de Suelo**: Vegetronix VH400 - 95-110€ (se puede sustituir por alternativa más económica)
- **CO2**: MH-Z19 o Senseair S8 - 26-40€
- **Dendómetro**: Ecomatik o potenciómetro banda - 80€ (para medir crecimiento del tronco, no esencial en presupuesto ajustado)

### Hardware Principal
- **Microcontrolador**: ESP32 + regulador 5V - 12€
- **Panel Solar**: 6V 3-5W - 15-18€
- **Controlador Carga Solar**: CN3791 (para LiFePO4) - 12€
- **Batería**: LiFePO4 3.2V 10000mAh - 35-40€ (se puede sustituir por batería más económica de 4000mAh)
- **Caja Robusta**: IP66 + pasacables - 22-25€ (se puede sustituir por modelo imprimible en 3D)
- **Soporte/Instalación**: Pérgola o sistema de fijación - 10€

### Notas sobre Componentes
- **Vegetronix VH400**: Sensor de alta calidad pero caro. Se puede sustituir por alternativa más económica.
- **Dendómetro**: Componente no esencial para el precio final. Mide crecimiento del diámetro del tronco.
- **Batería**: Se puede optar por batería de 4000mAh más económica si el presupuesto es limitado.
- **Caja**: Se puede sustituir por modelo imprimible en 3D más económico.

**Presupuesto estimado**: 200-350€ por unidad (dependiendo de las alternativas seleccionadas)

**Referencia completa**: Ver [lista detallada de componentes y precios](../docs/Componentes%20para%20ESP32/Componentes.png)

**Nota**: La lista es orientativa. La selección final dependerá de disponibilidad, presupuesto y requisitos específicos del despliegue.

## Funcionalidades Pendientes

- Sensor de CO2 (MH-Z19)
- Dendómetro (medición de crecimiento de tronco)
- Modo de bajo consumo (Deep Sleep) para autonomía energética
- Almacenamiento temporal local en caso de pérdida de conexión

## Integración con el Backend

El ESP32 se comunicará con el backend existente mediante:

- **Endpoints REST**: Se crearán endpoints específicos para recibir lecturas de sensores
- **Autenticación**: Sistema de tokens o API keys para identificar dispositivos
- **Formato JSON**: Envío de datos estructurados con timestamp y valores de sensores
- **Base de datos**: Almacenamiento en TimescaleDB (ya configurado) para series temporales

## Hardware Necesario (Estimado)

- Placa **ESP32** (modelo específico por determinar)
- Sensores ambientales (selección pendiente según disponibilidad y presupuesto)
- Cables, protoboard y componentes electrónicos
- Cable USB para programación
- Sistema de alimentación:
  - **Desarrollo**: USB 5V
  - **Producción**: Batería LiPo + panel solar (opcional)

## Software Necesario (Estimado)

- **PlatformIO** o **Arduino IDE** (IDE por confirmar)
- Drivers USB para el modelo de ESP32
- Librerías específicas según sensores elegidos
- Herramientas de monitorización serial

## Próximos Pasos

1. **Añadir sensor CO2** (MH-Z19) y enviar campo correspondiente al backend
2. **Incorporar dendómetro** para medición de crecimiento de tronco
3. **Optimizar consumo energético** (Deep Sleep entre lecturas)
4. **Probar en entorno real** (instalación permanente en árbol exterior)

## Consideraciones Técnicas

### Conectividad WiFi

- El ESP32 solo soporta WiFi 2.4GHz
- Verificar cobertura en ubicación de instalación
- Considerar repetidores WiFi o ubicación alternativa si es necesario

### Alimentación

- **Modo activo**: ~160-260 mA
- **Deep Sleep**: ~10-20 µA
- Evaluar necesidad de sistema solar según frecuencia de lecturas
- Considerar batería de respaldo para días nublados

### Protección

- Caja impermeable (IP65 o superior) para instalación exterior
- Protección contra rayos si está expuesto
- Acceso para mantenimiento y recalibración de sensores

## Documentación de Referencia

Cuando se inicie la implementación, consultar:

- [Documentación oficial ESP32](https://docs.espressif.com/projects/esp-idf/en/latest/esp32/)
- [Arduino Framework para ESP32](https://github.com/espressif/arduino-esp32)
- [PlatformIO ESP32](https://docs.platformio.org/en/latest/boards/espressif32.html)
- Datasheets de sensores específicos seleccionados

## Componentes Hardware

Para referencia de componentes considerados, ver:
- [Lista de componentes y precios](../docs/Componentes%20para%20ESP32/Componentes.png)

**Nota**: Esta lista es orientativa y puede variar según disponibilidad y presupuesto final.

## Estado Actual

El firmware básico está implementado y enviando lecturas al backend.

- **Sensores operativos**: temperatura (DHT22), humedad ambiente (DHT22), humedad de suelo (sensor analógico)
- **Sensores pendientes**: CO2 (MH-Z19), dendómetro

## Documentación Relacionada

- [Índice de Documentación](../docs/00.%20INDICE.md) - Índice completo de la documentación
- [Hoja de Ruta Completa](../docs/02.%20HOJA_DE_RUTA.md) - Planificación del proyecto
- [Especificación Técnica](../docs/03.%20ESPECIFICACION_TECNICA.md) - Requisitos y arquitectura
- [Backend README](../backend/README.md) - API REST con Spring Boot
- [Modelo de Datos](../docs/04.%20MODELO_DATOS.md) - Incluye entidades para lecturas de sensores

---

## Información del Proyecto

**Nombre**: Proyecto Árboles - Sistema de Monitorización de Árboles

**Institución**: IES El Rincón

**Curso**: Desarrollo de Aplicaciones Multiplataforma (DAM) 2025-2026

**Repositorio**: [github.com/riordi80/vocational-training-final-project](https://github.com/riordi80/vocational-training-final-project)

**Última actualización**: 2026-02-19

### Colaboradores

[![riordi80](https://img.shields.io/badge/riordi80-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/riordi80) [![Enrique36247](https://img.shields.io/badge/Enrique36247-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/Enrique36247)

---

**Proyecto Final DAM 2025-2026** | Desarrollado con Spring Boot, React, Android y ESP32
