# Manual de Instalación - Proyecto Árboles (Android)

## Requisitos Previos

### Software Necesario
- **Android Studio** (versión Arctic Fox o superior)
- **JDK 21** (recomendado para este proyecto)
- **SDK de Android** (API Level 24 o superior)
- **Gradle** 7.0+ (incluido con Android Studio)

### Hardware Recomendado
- 8 GB de RAM mínimo (16 GB recomendado)
- 10 GB de espacio libre en disco
- Procesador multinúcleo

## Configuración del Backend

La aplicación Android utiliza **Build Variants** para gestionar la conexión a diferentes backends sin modificar código. Esto permite cambiar fácilmente entre desarrollo local y producción.

**Build Variants disponibles:**

| Entorno | Build Variant | URL |
|---------|---------------|-----|
| Emulador + backend local | `localEmulatorDebug` | `http://10.0.2.2:8080/` |
| Móvil físico + backend local | `localDeviceDebug` | `http://192.168.1.158:8080/` |
| Producción (Render) | `productionDebug` / `productionRelease` | `https://proyecto-arboles-backend.onrender.com/` |

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
3. Selecciona un dispositivo (recomendado: Pixel 5)
4. Selecciona una imagen del sistema (API 30 o superior)
5. Finaliza la configuración

#### Opción B: Usar Dispositivo Físico

1. Habilita las **Opciones de Desarrollador** en tu dispositivo Android:
   - Ve a Ajustes > Acerca del teléfono
   - Toca 7 veces en "Número de compilación"
2. Activa **Depuración USB** en Opciones de Desarrollador
3. Conecta el dispositivo al ordenador mediante USB
4. Acepta la autorización de depuración USB en el dispositivo

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

El proyecto utiliza las siguientes bibliotecas (definidas en `build.gradle`):

```gradle
dependencies {
    // AndroidX
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'androidx.recyclerview:recyclerview:1.3.2'
    
    // Retrofit para conexión con API REST
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    
    // Gson para serialización JSON
    implementation 'com.google.code.gson:gson:2.10.1'
    
    // Material Design
    implementation 'com.google.android.material:material:1.11.0'
}
```

## Estructura del Proyecto

```
app/src/main/java/com/example/proyectoarboles/
├── activities/           # Actividades (pantallas)
│   ├── Login.java
│   ├── Registrer.java
│   ├── ListarArboles.java
│   └── ArbolDetalles.java
├── adapter/             # Adaptadores y conversores
│   ├── ArbolAdapter.java
│   └── BigDecimalStringAdapter.java
├── api/                 # Configuración de Retrofit
│   ├── ArbolApi.java
│   └── RetrofitClient.java
└── model/               # Modelos de datos
    ├── Arbol.java
    └── CentroEducativo.java
```

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

- La aplicación usa **Build Variants** para seleccionar el entorno (local/producción)
- Para producción, usa el variant `productionDebug` o `productionRelease`
- Incluye un sistema de **fallback** con datos XML en caso de que la API no esté disponible
- Los datos de sensores se generan aleatoriamente para demostración
- El modo de edición permite modificar todos los campos del árbol
- Las validaciones básicas están implementadas en el login y registro
- **Timeout configurado**: 60 segundos (adecuado para el free tier de Render)

## 8. Build Variants: Seleccionar Entorno de Desarrollo

### ¿Qué son los Build Variants?

Los Build Variants permiten compilar diferentes versiones de la aplicación sin modificar el código fuente. Cada variant tiene su propia URL de backend configurada automáticamente.

La aplicación tiene **6 Build Variants** disponibles:

| Build Variant | URL del Backend | Uso |
|---------------|-----------------|-----|
| `localEmulatorDebug` | `http://10.0.2.2:8080/` | Desarrollo con emulador + backend local |
| `localEmulatorRelease` | `http://10.0.2.2:8080/` | Probar release con emulador + backend local |
| `localDeviceDebug` | `http://192.168.1.158:8080/` | Desarrollo con móvil físico + backend local |
| `localDeviceRelease` | `http://192.168.1.158:8080/` | Probar release con móvil físico + backend local |
| `productionDebug` | `https://proyecto-arboles-backend.onrender.com/` | Debugging contra servidor de producción |
| `productionRelease` | `https://proyecto-arboles-backend.onrender.com/` | **APK final para distribución** |

### Cómo Seleccionar un Build Variant

1. En Android Studio, ve a **View > Tool Windows > Build Variants** (o usa el panel lateral izquierdo)
2. En el panel que aparece, haz clic en el dropdown de tu módulo `:app`
3. Selecciona el variant adecuado según tu situación:

#### Flujo de Trabajo Recomendado

| Situación | Build Variant a usar |
|-----------|---------------------|
| Desarrollo diario con emulador y backend local | `localEmulatorDebug` |
| Desarrollo con móvil físico y backend local | `localDeviceDebug` |
| Probar contra el servidor de producción (Render) | `productionDebug` |
| Generar APK final para distribución | `productionRelease` |

### Configuración para Dispositivo Físico

Si usas `localDeviceDebug` o `localDeviceRelease`, necesitas verificar que la IP configurada sea correcta:

1. Obtén tu IP local:
   - **Linux/Mac**: `ip addr` o `ifconfig`
   - **Windows**: `ipconfig`

2. Si tu IP es diferente a `192.168.1.158`, modifica el archivo `android/app/build.gradle.kts`:

   ```kotlin
   create("localDevice") {
       dimension = "environment"
       // Cambia esta IP por la de tu ordenador
       buildConfigField("String", "BASE_URL", "\"http://TU_IP_LOCAL:8080/\"")
   }
   ```

3. Asegúrate de que el ordenador y el dispositivo estén en la misma red WiFi

### Diferencia entre Debug y Release

| Característica | Debug | Release |
|----------------|-------|---------|
| Debuggable | Sí (puedes usar breakpoints) | No |
| Optimizado | No | Sí (si `isMinifyEnabled = true`) |
| Velocidad de compilación | Rápida | Más lenta |
| Firma | Automática (debug keystore) | Manual (requiere keystore de producción) |

**Para desarrollo diario**, usa siempre variantes `Debug`.
**Para distribución final**, usa `productionRelease`.

### Ventajas de usar Build Variants

- **Sin riesgo de errores**: No necesitas modificar código fuente para cambiar de entorno
- **Sin commits accidentales**: La URL correcta está siempre en el código
- **Cambio instantáneo**: Solo selecciona el variant y ejecuta
- **Profesional**: Es la forma estándar en desarrollo Android

### Notas Importantes

- El código accede a la URL mediante `BuildConfig.BASE_URL` (configurado en `RetrofitClient.java`)
- Al cambiar de variant, Android Studio recompilará automáticamente
- Los variants `localDevice*` requieren que configures tu IP local en `build.gradle.kts`

---

## Soporte

Para problemas adicionales:
- Revisa los logs en Android Studio
- Verifica la documentación del backend
- Asegúrate de que las versiones de las dependencias sean compatibles

---

## Información del Proyecto

**Nombre**: Proyecto Árboles - Sistema de Monitorización de Árboles

**Institución**: IES El Rincón

**Curso**: Desarrollo de Aplicaciones Multiplataforma (DAM) 2025-2026

**Repositorio**: [github.com/riordi80/vocational-training-final-project](https://github.com/riordi80/vocational-training-final-project)

**Última actualización**: 2026-01-21

### Colaboradores

[![riordi80](https://img.shields.io/badge/riordi80-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/riordi80) [![Enrique36247](https://img.shields.io/badge/Enrique36247-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/Enrique36247)

---

**Proyecto Final DAM 2025-2026** | Desarrollado con Spring Boot, React, Android y ESP32