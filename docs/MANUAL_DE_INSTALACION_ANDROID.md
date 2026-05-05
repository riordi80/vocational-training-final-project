# Manual de Instalación - Proyecto Árboles (Android)

## Requisitos Previos

### Software Necesario
- **Android Studio** (versión Arctic Fox o superior - recomendado 2024.1+)
- **JDK 11** (mínimo) o JDK 21+ (compatible)
- **SDK de Android** (API Level 36 - Android 15 compileSdk)
- **Gradle** 8.0+ (incluido con Android Studio)
- **Git** (para clonar el repositorio)

### Hardware Recomendado
- 8 GB de RAM mínimo (16 GB recomendado)
- 15 GB de espacio libre en disco (SDK + dependencias + compilación)
- Procesador multinúcleo (preferiblemente con virtualización habilitada para emulador)

## Configuración del Backend

La aplicación Android utiliza **Product Flavors** (configuración de sabores) para gestionar la conexión a diferentes backends sin modificar código. Esto permite cambiar fácilmente entre desarrollo local y producción.

**Product Flavors disponibles:**

| Entorno | Flavor | URL del Backend |
|---------|--------|-----------------|
| Emulador + Backend Local | `localEmulator` | `http://10.0.2.2:8080/` |
| Dispositivo Físico + Backend Local | `localDevice` | `http://192.168.1.158:8080/` |
| Producción (Render) | `production` | `https://proyecto-arboles-backend.onrender.com/` |

**Nota**: Si tu IP local es diferente a `192.168.1.158`, deberás actualizar el valor en `android/app/build.gradle.kts`.

Para más detalles sobre cómo seleccionar un Build Variant, consulta la **Sección 8: Build Variants**.

## Instalación Paso a Paso

### 1. Clonar o Descargar el Proyecto

```bash
git clone https://github.com/riordi80/vocational-training-final-project
cd vocational-training-final-project
```

O descarga el proyecto como ZIP desde GitHub y descomprímelo.

**NOTA**: Si ya clonaste el proyecto siguiendo el [Manual de Instalación General](./MANUAL_DE_INSTALACION.md), puedes saltar este paso.

### 2. Abrir el Proyecto en Android Studio

1. Abre Android Studio
2. Selecciona **File > Open**
3. Navega hasta la carpeta del proyecto
4. Haz clic en **OK**

### 3. Sincronizar Dependencias

Android Studio automáticamente comenzará a sincronizar las dependencias de Gradle. Si no lo hace:

1. Haz clic en **File > Sync Project with Gradle Files**
2. Espera a que se descarguen todas las dependencias

### 4. Configurar el Emulador o Dispositivo Físico

#### Opción A: Usar Emulador

1. Ve a **Tools > Device Manager**
2. Haz clic en **Create Device**
3. Selecciona un dispositivo (recomendado: Pixel 5 o superior)
4. Selecciona una imagen del sistema (API 30 o superior, recomendado: API 35 o 36)
5. Finaliza la configuración y espera a que inicie el emulador

#### Opción B: Usar Dispositivo Físico

1. Habilita las **Opciones de Desarrollador** en tu dispositivo Android:
   - Ve a Ajustes > Acerca del teléfono
   - Toca 7 veces en "Número de compilación"
2. Activa **Depuración USB** en Opciones de Desarrollador
3. Conecta el dispositivo al ordenador mediante USB (cable de datos)
4. Acepta la autorización de depuración USB en el dispositivo
5. Verifica la conexión en Android Studio: **Tools > Device Manager** (debe aparecer tu dispositivo)

### 5. Verificar la Conexión al Backend

Antes de ejecutar la app, verifica que:

1. El backend esté ejecutándose
2. La URL en `RetrofitClient.java` sea correcta
3. Si usas dispositivo físico, tu ordenador y el dispositivo estén en la misma red WiFi

### 6. Compilar y Ejecutar

1. Selecciona tu dispositivo/emulador en la barra superior
2. Haz clic en el botón **Run** (▶️) o presiona `Shift + F10`
3. Espera a que la aplicación se compile e instale

## Dependencias del Proyecto

El proyecto utiliza las siguientes bibliotecas (definidas en `build.gradle.kts`):

```gradle
dependencies {
    // AndroidX - UI Fundamentals
    implementation(libs.appcompat)                    // AppCompat 1.6.1+
    implementation(libs.material)                     // Material Design 1.11+
    implementation(libs.activity)                     // Activity 1.8+
    implementation(libs.constraintlayout)             // ConstraintLayout
    implementation(libs.recyclerview)                 // RecyclerView 1.3.2+
    implementation(libs.preference)                   // Preferences
    
    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")              // API REST Client
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")        // JSON Converter
    implementation("com.squareup.okhttp3:okhttp:4.11.0")                 // HTTP Client
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")    // Request Logging
    
    // JSON Serialization
    implementation("com.google.code.gson:gson:2.10.1")
    
    // Charts & Graphics
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")            // Gráficas de datos
    
    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-messaging")              // Push notifications
    
    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
```

**Librerías principales:**
- **Retrofit**: Cliente HTTP para consumir API REST
- **OkHttp**: Interceptor para logging de peticiones
- **Gson**: Serialización/deserialización JSON
- **MPAndroidChart**: Gráficas interactivas para datos de sensores
- **Firebase**: Notificaciones push

## Estructura del Proyecto

```
app/src/main/java/com/example/proyectoarboles/
├── activities/              # Actividades principales
│   ├── MainActivity.java
│   └── ...
├── fragments/               # Fragmentos (pantallas de navegación)
│   ├── DashboardFragment.java
│   ├── ArbolDetallesFragment.java
│   ├── CrearArbolFragment.java
│   ├── DetalleCentroFragment.java
│   ├── AdminUsuariosFragment.java
│   ├── DetalleUsuarioFragment.java
│   ├── FormularioUsuarioFragment.java
│   ├── FormularioCentroFragment.java
│   ├── FormularioDispositivoFragment.java
│   ├── HistoricoDispositivoFragment.java
│   └── ...
├── adapter/                 # Adaptadores para RecyclerView
│   ├── ArbolAdapter.java
│   ├── UsuarioAdapter.java
│   ├── CentroEducativoAdapter.java
│   ├── BigDecimalStringAdapter.java
│   └── ...
├── api/                     # Configuración de Retrofit
│   ├── ArbolService.java
│   ├── CentroEducativoService.java
│   ├── UsuarioService.java
│   ├── DispositivoService.java
│   ├── RetrofitClient.java
│   └── ...
├── model/                   # Modelos de datos (DTOs)
│   ├── Arbol.java
│   ├── CentroEducativo.java
│   ├── DispositivoEsp32.java
│   ├── Lectura.java
│   └── ...
├── dto/                     # Data Transfer Objects
│   └── ...
├── util/                    # Utilidades
│   ├── PermissionManager.java
│   ├── IslaUtils.java
│   └── ...
└── AndroidManifest.xml      # Configuración de la aplicación
```

**Explicación de los componentes:**

- **Activities**: Contenedores principales de la aplicación
- **Fragments**: Vistas reutilizables dentro de activities
- **Adapters**: Conectan los datos con las vistas (RecyclerView)
- **API Services**: Interfaces Retrofit para consumir el backend
- **Models**: Clases POJO que representan las entidades del dominio
- **Utils**: Clases auxiliares (gestión de permisos, utilidades generales)

## Solución de Problemas

### Error de Conexión al Backend

**Problema**: La aplicación muestra "Error de conexión"

**Soluciones**:
- Verifica que el backend esté ejecutándose
- Comprueba la URL en `RetrofitClient.java`
- Si usas emulador, usa `10.0.2.2` en lugar de `localhost`
- Si usas dispositivo físico, verifica que esté en la misma red WiFi
- Desactiva temporalmente el firewall para probar

### Error de Compilación

**Problema**: Gradle no puede sincronizar las dependencias

**Soluciones**:
- Verifica tu conexión a Internet
- Invalida caché: **File > Invalidate Caches / Restart**
- Limpia el proyecto: **Build > Clean Project**
- Reconstruye: **Build > Rebuild Project**

### La App se Cierra Inesperadamente

**Problema**: La aplicación se cierra al iniciar

**Soluciones**:
- Revisa los logs en **Logcat** (View > Tool Windows > Logcat)
- Verifica que el `BASE_URL` sea correcto
- Asegúrate de que el backend esté respondiendo correctamente

### Permisos de Internet

Si la app no puede conectarse, verifica que el `AndroidManifest.xml` incluya:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

## Compilar APK para Distribución

Para generar un APK instalable para producción:

1. Selecciona el Build Variant **`productionRelease`** (View > Tool Windows > Build Variants)
2. Ve a **Build > Build Bundle(s) / APK(s) > Build APK(s)**
3. Espera a que se complete la compilación
4. Haz clic en **locate** para encontrar el APK generado
5. El APK estará en: `app/build/outputs/apk/production/release/app-production-release.apk`

**Nota**: Para desarrollo/pruebas, el APK debug estará en `app/build/outputs/apk/[variant]/debug/`

## Notas Adicionales

### Arquitectura de la Aplicación

- **Patrón MVVM** con Fragments para navegación moderna
- **Retrofit + OkHttp** para consumo de APIs REST
- **RecyclerView** con Adapters para listas de datos
- **Firebase Cloud Messaging** para notificaciones push
- **MPAndroidChart** para visualización de datos de sensores

### Características Implementadas

✅ Autenticación y registro de usuarios  
✅ CRUD completo de árboles  
✅ Gestión de centros educativos  
✅ Administración de usuarios (solo admin)  
✅ Gestión de dispositivos ESP32  
✅ Visualización de datos históricos con gráficas  
✅ Notificaciones push via Firebase  
✅ Soporte multi-isla (Canarias)  
✅ Control de permisos basado en roles  
✅ Timeout configurado para cold start de Render (60 seg)  

### Configuración de API Keys

**Firebase** (si necesitas habilitar notificaciones push):
- El archivo `google-services.json` debe estar en `app/`
- Se descarga desde [Firebase Console](https://console.firebase.google.com)

### Logging de Requests HTTP

La aplicación incluye un **logging interceptor** para debuggear peticiones HTTP:

```java
HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
logging.setLevel(HttpLoggingInterceptor.Level.BODY);
client.addInterceptor(logging);
```

Ver logs en **Logcat** con el filtro: `okhttp`

### Permisos Requeridos

El `AndroidManifest.xml` debe incluir:

```xml
<!-- Network -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

<!-- Optional: Para notificaciones push -->
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

### Compilación de APK para Distribución

Para generar un APK instalable para producción:

1. Selecciona el Build Variant: **`productionRelease`** en el panel Build Variants
2. Ve a **Build > Build Bundle(s) / APK(s) > Build APK(s)**
3. Espera a que se complete la compilación
4. Abre la carpeta de outputs: **Build > Analyze APK** o **Locate** en la notificación
5. El APK estará en: `app/build/outputs/apk/production/release/app-production-release.apk`

**Para desarrollo/pruebas**, el APK debug estará en:
- `app/build/outputs/apk/[flavor]/debug/`

### Mejores Prácticas

1. **Siempre usa Build Variants** para cambiar de entorno, no modifiques URLs en el código
2. **Revisa Logcat** para errores y debugging de conexiones HTTP
3. **Sincroniza Gradle** después de cualquier cambio en dependencias
4. **Invalida caché** si experimentas comportamientos extraños: **File > Invalidate Caches / Restart**
5. **Usa dispositivo físico** para testing antes de producción
6. **Configura correctamente la IP local** si usas backend local con dispositivo físico

## 8. Build Variants: Seleccionar Entorno de Desarrollo

### ¿Qué son los Build Variants?

Los Build Variants (variantes de compilación) combinan los **Product Flavors** con los **Build Types** para crear diferentes versiones de la aplicación. Permiten compilar diferentes versiones de la aplicación sin modificar el código fuente. Cada variant tiene su propia URL de backend configurada automáticamente en `BuildConfig.BASE_URL`.

### Product Flavors Disponibles

| Flavor | Descripción | URL |
|--------|-------------|-----|
| `localEmulator` | Backend local accesible desde emulador | `http://10.0.2.2:8080/` |
| `localDevice` | Backend local accesible desde dispositivo físico | `http://192.168.1.158:8080/` |
| `production` | Backend en producción (Render) | `https://proyecto-arboles-backend.onrender.com/` |

### Build Types

| Build Type | Descripción | Firma | Optimización |
|-----------|-------------|-------|--------------|
| `debug` | Desarrollo y debugging | Automática (debug keystore) | Desactivada |
| `release` | Producción y distribución | Manual (requiere keystore) | Activada |

### Build Variants Resultantes

La combinación de 3 flavors × 2 build types = **6 variantes diferentes**:

```
localEmulatorDebug / Release
localDeviceDebug / Release
productionDebug / Release
```

### Cómo Seleccionar un Build Variant

1. En Android Studio, abre el panel **Build Variants**:
   - Ve a **View > Tool Windows > Build Variants**
   - O usa el panel lateral izquierdo si está visible

2. En el módulo `:app`, selecciona el variant que necesitas en el dropdown

3. Android Studio recompilará automáticamente con la configuración correcta

### Flujo de Trabajo Recomendado

| Situación | Build Variant a usar |
|-----------|---------------------|
| Desarrollo diario con emulador | `localEmulatorDebug` |
| Probar con dispositivo físico | `localDeviceDebug` |
| Debugging contra servidor de producción | `productionDebug` |
| APK final para distribución | `productionRelease` |
| Testing release con backend local | `localEmulatorRelease` o `localDeviceRelease` |

### Configuración para Dispositivo Físico con Backend Local

Si usas el flavor `localDevice`, necesitas que tu PC tenga la IP correcta configurada:

#### Verificar tu IP local

**Windows:**
```cmd
ipconfig
```

Busca la dirección IPv4 (típicamente algo como `192.168.X.X`)

**Linux/Mac:**
```bash
ip addr show
# o
ifconfig
```

#### Actualizar la configuración

Si tu IP es diferente a `192.168.1.158`, actualiza `android/app/build.gradle.kts`:

```kotlin
create("localDevice") {
    dimension = "environment"
    // Reemplaza esto con tu IP local real
    buildConfigField("String", "BASE_URL", "\"http://TU_IP_LOCAL:8080/\"")
}
```

Luego:
1. Sincroniza Gradle: **File > Sync Now**
2. Recompila: **Build > Rebuild Project**
3. Asegúrate que PC y dispositivo están en la misma red WiFi

### Diferencia entre Debug y Release

| Característica | Debug | Release |
|----------------|-------|---------|
| Debuggable | ✅ Sí (breakpoints) | ❌ No |
| Optimización de código | ❌ No | ✅ Sí (ProGuard configurado) |
| Tamaño del APK | Más grande | Más pequeño |
| Velocidad de compilación | Rápida (< 1 min) | Lenta (2-3 min) |
| Firma de código | Automática (debug keystore) | Manual (keystore de producción) |

**Para desarrollo diario**, usa siempre variantes `Debug`.
**Para distribución final**, usa `productionRelease`.

### Acceso a BuildConfig en el Código

La aplicación accede a la URL configurada mediante:

```java
String baseUrl = BuildConfig.BASE_URL;  // Automáticamente del flavor seleccionado
```

Esto se configura en `RetrofitClient.java` para inicializar el cliente HTTP.

### Ventajas de usar Build Variants

✅ **Sin riesgo de errores**: La URL correcta está siempre configurada automáticamente  
✅ **Sin commits accidentales**: No necesitas modificar el código fuente  
✅ **Cambio instantáneo**: Solo selecciona el variant y compila  
✅ **Profesional**: Forma estándar en desarrollo Android  
✅ **Escalable**: Fácil de agregar nuevos entornos (staging, testing, etc.)

### Notas Importantes

- Al cambiar de variant, Android Studio recompilará automáticamente
- Los flavors `localDevice*` requieren que configures tu IP local correcta
- Firebase está configurado para funcionar en todas las variantes
- El timeout de conexión está configurado a 60 segundos (óptimo para Render)

---

## Información del Proyecto

**Nombre**: Proyecto Árboles - Sistema de Monitorización de Árboles

**Institución**: IES El Rincón

**Curso**: Desarrollo de Aplicaciones Multiplataforma (DAM) 2025-2026

**Repositorio**: [github.com/riordi80/vocational-training-final-project](https://github.com/riordi80/vocational-training-final-project)

**Última actualización**: 2026-05-05

### Stack Tecnológico

- **Frontend Web**: React + Vite + Tailwind CSS
- **Frontend Móvil**: Android (Java)
- **Backend**: Spring Boot 3
- **Base de Datos**: PostgreSQL + TimescaleDB
- **IoT**: Arduino/ESP32
- **Hosting**: Render (backend), Vercel (frontend)

### Colaboradores

[![riordi80](https://img.shields.io/badge/riordi80-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/riordi80) [![Enrique36247](https://img.shields.io/badge/Enrique36247-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/Enrique36247)

---

**Proyecto Final DAM 2025-2026** | Desarrollado con Spring Boot, React, Android y ESP32