# Android - Proyecto Árboles

Aplicación móvil Android nativa para monitorización de árboles mediante sensores IoT.

## Estado de Implementación

**COMPLETADA** - Integración con backend en producción

- Backend conectado: https://proyecto-arboles-backend.onrender.com
- 4 Activities implementadas
- CRUD funcional (Listar, Detalles, Editar, Eliminar)
- Timeouts configurados para cold start de Render (60 seg)
- Documentación completa (manuales de instalación y usuario)

## Tecnologías

- **Lenguaje**: Java
- **SDK**: Android SDK 24+ (Android 7.0 Nougat)
- **IDE**: Android Studio
- **Build**: Gradle
- **Cliente HTTP**: Retrofit 2 + OkHttp
- **Diseño**: Material Design

## Requisitos Previos

- Android Studio (última versión estable)
- JDK 21
- Android SDK con API 24 como mínimo
- Dispositivo Android o emulador

## Instalación y Configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/riordi80/vocational-training-final-project
cd android
```

### 2. Abrir en Android Studio

- File → Open
- Seleccionar carpeta `android/`
- Esperar a que Gradle sincronice las dependencias

### 3. Configurar URL del Backend

La aplicación está configurada para conectarse al backend en producción:

**RetrofitClient.java:**
```java
private static final String BASE_URL = "https://proyecto-arboles-backend.onrender.com/";
```

Para desarrollo local, cambiar a:
```java
private static final String BASE_URL = "http://10.0.2.2:8080/"; // Emulador Android
// o
private static final String BASE_URL = "http://TU_IP_LOCAL:8080/"; // Dispositivo físico
```

### 4. Compilar y ejecutar

```bash
./gradlew assembleDebug
```

O desde Android Studio: Run → Run 'app'

## Estructura del Proyecto

```
android/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/proyectoarboles/
│   │   │   │       ├── activities/    # Login, Registrer, ListarArboles, ArbolDetalles
│   │   │   │       ├── adapter/       # ArbolAdapter, BigDecimalStringAdapter
│   │   │   │       ├── api/           # ApiClient, ApiService, AuthInterceptor
│   │   │   │       └── model/         # Arbol, CentroEducativo
│   │   │   ├── res/
│   │   │   │   ├── layout/           # activity_*.xml, item_arbol.xml
│   │   │   │   └── values/           # strings.xml, colors.xml, themes.xml
│   │   │   └── AndroidManifest.xml
│   │   └── test/
│   ├── build.gradle
│   └── proguard-rules.pro
├── gradle/
├── build.gradle
├── settings.gradle
├── .gitignore
└── README.md
```

## Funcionalidades Implementadas

### Activities (4)

1. **Login.java**
   - Validación de usuario y contraseña
   - Navegación a ListarArboles tras autenticación

2. **Registrer.java**
   - Formulario de registro de nuevos usuarios
   - Validaciones de campos

3. **ListarArboles.java**
   - RecyclerView con todos los árboles del backend
   - Botón eliminar por cada árbol con confirmación
   - Click en árbol para ver detalles
   - Carga desde API de Render con fallback local

4. **ArbolDetalles.java**
   - Visualización de todos los datos del árbol
   - Modo edición con botón "Editar"
   - Datos de sensores: temperatura, humedad, CO2, humedad del suelo
   - Actualización en API con PUT
   - Botón cancelar para descartar cambios

### API Integration (Retrofit)

**Endpoints implementados:**
- GET `/api/arboles` - Listar todos los árboles
- GET `/api/arboles/{id}` - Obtener árbol por ID
- PUT `/api/arboles/{id}` - Actualizar árbol
- DELETE `/api/arboles/{id}` - Eliminar árbol
- Búsquedas por especie, centro, dispositivo, nombre

**Configuración:**
- Timeouts: 60 segundos (adecuado para Render free tier)
- Conversor: Gson con BigDecimalStringAdapter
- Manejo de errores con Toast y Logs

### Modelos

- **Arbol.java**: id, nombre, especie, fechaPlantacion, ubicacion, sensores
- **CentroEducativo.java**: id, nombre

### Características Adicionales

- Permisos de Internet configurados
- Manejo de errores robusto
- Datos de fallback en XML
- RecyclerView optimizado con ViewHolder
- Diálogos de confirmación para acciones destructivas
- Logging detallado para debugging

## Requisitos Académicos

### [PGL] Programación Multimedia y Dispositivos Móviles

- [x] Mínimo 4 actividades (Login, Registrer, ListarArboles, ArbolDetalles)
- [x] Listar árboles por centro educativo
- [x] Visualizar detalles del árbol con datos en tiempo real (sensores simulados)
- [x] Modificar datos del árbol (modo edición en ArbolDetalles)
- [x] Eliminar árbol (con confirmación)

**Estado**: TODOS LOS REQUISITOS CUMPLIDOS

## Estado de la Aplicación Android

**Aplicación completada e integrada con backend en producción**

La app Android está completamente funcional, conectada al backend desplegado en Render, con CRUD completo de árboles y manejo de cold start.

## Documentación Relacionada

### Manuales
- [Manual de Instalación Android](../docs/MANUAL_DE_INSTALACION_ANDROID.md) - Guía de instalación y configuración
- [Manual de Usuario Android](../docs/MANUAL_DE_USUARIO_ANDROID.md) - Guía de uso de la aplicación
- [Manual de Instalación General](../docs/MANUAL_DE_INSTALACION.md) - Instalación completa del sistema
- [Manual de Usuario General](../docs/MANUAL_DE_USUARIO.md) - Guía de usuario web y móvil

### Documentación Técnica
- [Índice de Documentación](../docs/00.%20INDICE.md) - Índice completo
- [Hoja de Ruta](../docs/02.%20HOJA_DE_RUTA.md) - Planificación del proyecto
- [Especificación Técnica](../docs/03.%20ESPECIFICACION_TECNICA.md) - Requisitos y arquitectura
- [Backend README](../backend/README.md) - API REST con Spring Boot

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
