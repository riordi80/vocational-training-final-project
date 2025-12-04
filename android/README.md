# Android - Proyecto Árboles

Aplicación móvil Android nativa para monitorización de árboles mediante sensores IoT.

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

Editar la URL base en el cliente API para apuntar a tu backend local o servidor.

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

## Requisitos Académicos

### [PGL] Programación Multimedia y Dispositivos Móviles

- [x] Mínimo 4 actividades (Login, Registrer, ListarArboles, ArbolDetalles)
- [x] Listar árboles por centro educativo
- [x] Visualizar detalles del árbol con datos en tiempo real
- [ ] Modificar datos del árbol
- [ ] Eliminar árbol

## Estado del Proyecto

**Estado**: En desarrollo - Fase 5

## Documentación Relacionada

- [Índice de Documentación](../docs/00.%20INDICE.md) - Índice completo de la documentación
- [Hoja de Ruta Completa](../docs/02.%20HOJA_DE_RUTA.md) - Planificación del proyecto
- [Especificación Técnica](../docs/03.%20ESPECIFICACION_TECNICA.md) - Requisitos y arquitectura
- [Backend README](../backend/README.md) - API REST con Spring Boot

## Contacto

Proyecto Final DAM 2025-2026
