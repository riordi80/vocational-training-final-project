# ESP32 - Proyecto Árboles

**Estado**: Planificación - No implementado

Componente futuro del sistema para recopilación de datos de sensores ambientales mediante dispositivos IoT ESP32.

## Descripción

Este componente está planificado como una extensión futura del sistema Garden Monitor. Permitiría la recopilación automática de datos ambientales mediante dispositivos IoT instalados en los árboles.

## Tecnologías Planificadas

- **Plataforma**: ESP32 (ESP-WROOM-32 o compatible)
- **Lenguaje**: C/C++ (Arduino Framework o PlatformIO)
- **Conectividad**: WiFi 2.4GHz
- **Protocolo**: HTTP/HTTPS REST hacia el backend
- **Formato de datos**: JSON

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

## Funcionalidades Planificadas

- Lectura periódica de sensores ambientales
- Transmisión de datos al backend vía WiFi
- Modo de bajo consumo (Deep Sleep) para autonomía energética
- Gestión de errores y reintentos de conexión
- Identificación única de cada dispositivo
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

Cuando se inicie el desarrollo de este componente:

1. **Definir especificaciones técnicas** de sensores y hardware
2. **Adquirir componentes** según presupuesto disponible
3. **Diseñar esquema de conexiones** y prototipo
4. **Implementar firmware básico** de lectura de sensores
5. **Integrar con backend** mediante API REST
6. **Calibrar sensores** con valores conocidos
7. **Probar en entorno real** (instalación en árbol)
8. **Optimizar consumo energético** para autonomía
9. **Documentar** proceso de instalación y mantenimiento

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

**Este componente NO está implementado**. Es una extensión futura planificada para después del 8 de diciembre de 2025.

El sistema actual (Backend, Frontend, Android) funciona sin este componente. Los datos de sensores en las aplicaciones son actualmente simulados/aleatorios para propósitos de demostración.

## Documentación Relacionada

- [Índice de Documentación](../docs/00.%20INDICE.md) - Índice completo de la documentación
- [Hoja de Ruta Completa](../docs/02.%20HOJA_DE_RUTA.md) - Planificación del proyecto
- [Especificación Técnica](../docs/03.%20ESPECIFICACION_TECNICA.md) - Requisitos y arquitectura
- [Backend README](../backend/README.md) - API REST con Spring Boot
- [Modelo de Datos](../docs/04.%20MODELO_DATOS.md) - Incluye entidades para lecturas de sensores

---

## Información del Proyecto

**Nombre**: Garden Monitor - Sistema de Monitorización de Árboles

**Institución**: IES El Rincón

**Curso**: Desarrollo de Aplicaciones Multiplataforma (DAM) 2025-2026

**Repositorio**: [github.com/riordi80/vocational-training-final-project](https://github.com/riordi80/vocational-training-final-project)

**Última actualización**: 2025-12-08

### Colaboradores

[![riordi80](https://img.shields.io/badge/riordi80-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/riordi80) [![Enrique36247](https://img.shields.io/badge/Enrique36247-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/Enrique36247)

---

**Proyecto Final DAM 2025-2026** | Desarrollado con Spring Boot, React, Android y ESP32
