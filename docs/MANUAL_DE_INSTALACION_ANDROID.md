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

La aplicación Android se conecta al backend desplegado en producción por defecto. Si deseas usar un backend local para desarrollo:

1. El backend debe estar corriendo en el puerto **8080**
2. Anota la dirección IP de tu servidor backend
3. Modifica la URL en `RetrofitClient.java` (ver sección siguiente)

### Configuración de la URL del Backend

La aplicación está configurada para conectarse al backend en producción mediante la clase `RetrofitClient.java`:

```java
// En: app/src/main/java/com/example/proyectoarboles/api/RetrofitClient.java

private static final String BASE_URL = "https://proyecto-arboles-backend.onrender.com/";
```

**Opciones de configuración según tu entorno:**

- **Producción (por defecto)**: `https://proyecto-arboles-backend.onrender.com/`
- **Emulador de Android (desarrollo local)**: `http://10.0.2.2:8080/`
- **Dispositivo físico (desarrollo local)**: `http://TU_IP_LOCAL:8080/`
  - Ejemplo: `http://192.168.1.100:8080/`
  - Asegúrate de que el dispositivo y el ordenador estén en la misma red WiFi

## Instalación Paso a Paso

### 1. Clonar o Descargar el Proyecto

```bash
git clone 
cd vocational-training-final-project
```

O descarga el proyecto como ZIP y descomprímelo.

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

Para generar un APK instalable:

1. Ve a **Build > Build Bundle(s) / APK(s) > Build APK(s)**
2. Espera a que se complete la compilación
3. Haz clic en **locate** para encontrar el APK generado
4. El APK estará en: `app/build/outputs/apk/debug/app-debug.apk`

## Notas Adicionales

- La aplicación **se conecta por defecto al backend en producción** (Render)
- Incluye un sistema de **fallback** con datos XML en caso de que la API no esté disponible
- Los datos de sensores se generan aleatoriamente para demostración
- El modo de edición permite modificar todos los campos del árbol
- Las validaciones básicas están implementadas en el login y registro
- **Timeout configurado**: 60 segundos (adecuado para el free tier de Render)

## 8. Configuración: Desarrollo Local vs Producción

### Configuración de la URL del Backend

La aplicación Android tiene la URL del backend hardcoded en el código fuente. Necesitarás cambiarla manualmente dependiendo de si estás trabajando en desarrollo local o producción.

**Archivo**: `android/app/src/main/java/com/example/proyectoarboles/api/RetrofitClient.java`

### Configuración para Producción (Actual)

```java
private static final String BASE_URL = "https://proyecto-arboles-backend.onrender.com/";
```

Esta es la configuración actual que apunta al backend desplegado en Render.

### Configuración para Desarrollo Local

Si quieres trabajar con el backend en local, necesitas cambiar la URL según el tipo de dispositivo:

#### Para Emulador Android:

```java
private static final String BASE_URL = "http://10.0.2.2:8080/";
```

**Explicación**: El emulador de Android usa `10.0.2.2` como alias para `localhost` de tu ordenador.

#### Para Dispositivo Físico:

```java
private static final String BASE_URL = "http://192.168.1.X:8080/";
```

**Pasos**:
1. Obtén tu IP local:
   - **Linux/Mac**: `ip addr` o `ifconfig`
   - **Windows**: `ipconfig`
2. Reemplaza `192.168.1.X` con tu IP real (ej: `192.168.1.105`)
3. Asegúrate de que el ordenador y el dispositivo estén en la misma red WiFi

### Flujo de Trabajo Recomendado

#### Desarrollo Local:

1. Asegúrate de que el backend esté corriendo localmente:
   ```bash
   cd backend
   ./mvnw spring-boot:run
   # Backend en http://localhost:8080
   ```

2. Modifica `RetrofitClient.java`:
   ```java
   private static final String BASE_URL = "http://10.0.2.2:8080/";
   ```

3. Ejecuta la aplicación desde Android Studio

4. **IMPORTANTE**: No hagas commit de este cambio. Antes de hacer commit, vuelve a poner la URL de producción.

#### Producción:

1. Asegúrate de que `RetrofitClient.java` apunte a Render:
   ```java
   private static final String BASE_URL = "https://proyecto-arboles-backend.onrender.com/";
   ```

2. Compila el APK para distribución

### Notas Importantes

- A diferencia del Frontend y Backend, la aplicación Android **requiere cambio manual** del código fuente
- Recuerda revertir los cambios de URL antes de hacer commit al repositorio
- Si trabajas en equipo, coordina para no commitear la URL de desarrollo local
- Considera usar BuildConfig o variables de entorno en versiones futuras para automatizar esto

---

## Soporte

Para problemas adicionales:
- Revisa los logs en Android Studio
- Verifica la documentación del backend
- Asegúrate de que las versiones de las dependencias sean compatibles