# Android - Proyecto Árboles

Aplicación móvil Android nativa para monitorización de árboles mediante sensores IoT.

## Tecnologías

- **Lenguaje**: Java
- **SDK**: Android SDK 24+ (Android 7.0 Nougat)
- **IDE**: Android Studio (última versión estable)
- **Build**: Gradle
- **Cliente HTTP**: Retrofit 2 + OkHttp
- **Diseño**: Material Design 3
- **Notificaciones**: Firebase Cloud Messaging (FCM)
- **Almacenamiento local**: SharedPreferences
- **Imágenes**: Glide (si aplica)

## Estructura del Proyecto

```
android/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/arboles/
│   │   │   │   ├── activities/         # Actividades principales
│   │   │   │   │   ├── LoginActivity.java
│   │   │   │   │   ├── ListadoArbolesActivity.java
│   │   │   │   │   ├── DetalleArbolActivity.java
│   │   │   │   │   ├── EditarArbolActivity.java
│   │   │   │   │   └── AlertasActivity.java
│   │   │   │   ├── adapters/           # Adaptadores RecyclerView
│   │   │   │   │   └── ArbolesAdapter.java
│   │   │   │   ├── api/                # Interfaces Retrofit
│   │   │   │   │   ├── ApiClient.java
│   │   │   │   │   ├── ApiService.java
│   │   │   │   │   └── AuthInterceptor.java
│   │   │   │   ├── models/             # Modelos de datos (POJOs)
│   │   │   │   │   ├── Arbol.java
│   │   │   │   │   ├── Lectura.java
│   │   │   │   │   ├── Centro.java
│   │   │   │   │   └── LoginResponse.java
│   │   │   │   └── utils/              # Utilidades
│   │   │   │       └── DateFormatter.java
│   │   │   ├── res/
│   │   │   │   ├── layout/             # Layouts XML
│   │   │   │   ├── values/             # Strings, colores, temas
│   │   │   │   ├── drawable/           # Iconos y recursos gráficos
│   │   │   │   └── menu/               # Menús
│   │   │   └── AndroidManifest.xml
│   │   └── test/
│   ├── build.gradle
│   └── google-services.json            # Firebase (no commitear)
├── gradle/
├── build.gradle
├── settings.gradle
├── .gitignore
└── README.md
```

## Requisitos Previos

- Android Studio (última versión estable)
- JDK 21
- Android SDK con API 24 (Android 7.0) como mínimo
- Dispositivo Android o emulador
- Cuenta de Firebase (para notificaciones push)

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

Editar `app/src/main/java/com/arboles/api/ApiClient.java`:

```java
public class ApiClient {
    private static final String BASE_URL = "http://your-backend-url:8080/api/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor())
                .addInterceptor(new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

            retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        }
        return retrofit;
    }
}
```

**Para desarrollo local**:
- Si usas **emulador**: `http://10.0.2.2:8080/api/`
- Si usas **dispositivo físico**: `http://TU_IP_LOCAL:8080/api/` (ejemplo: `http://192.168.1.100:8080/api/`)

### 4. Configurar Firebase Cloud Messaging (Notificaciones Push)

1. Ir a [Firebase Console](https://console.firebase.google.com/)
2. Crear nuevo proyecto o usar uno existente
3. Añadir aplicación Android
4. Descargar `google-services.json`
5. Colocar en `app/google-services.json`

**IMPORTANTE**: No commitear `google-services.json` con credenciales reales. Añadirlo al `.gitignore`.

### 5. Compilar y ejecutar

```bash
./gradlew assembleDebug
```

O desde Android Studio: Run → Run 'app'

La aplicación se instalará en el dispositivo/emulador conectado.

## Actividades Principales (Requisito PGL)

### 1. LoginActivity

**Descripción**: Pantalla de autenticación de usuario.

**Funcionalidades**:
- Formulario con email y contraseña
- Validación de campos
- Petición POST a `/api/auth/login`
- Almacenamiento de token JWT en SharedPreferences
- Redirección a ListadoArbolesActivity tras login exitoso

**Layout**: `activity_login.xml`

**Ejemplo de código**:

```java
public class LoginActivity extends AppCompatActivity {
    private EditText emailInput, passwordInput;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> login());
    }

    private void login() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest request = new LoginRequest(email, password);

        ApiService api = ApiClient.getClient().create(ApiService.class);
        Call<LoginResponse> call = api.login(request);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();

                    // Guardar token
                    SharedPreferences prefs = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
                    prefs.edit().putString("jwt_token", token).apply();

                    // Redirigir
                    Intent intent = new Intent(LoginActivity.this, ListadoArbolesActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
```

### 2. ListadoArbolesActivity

**Descripción**: Muestra listado de árboles filtrados por centro educativo del usuario.

**Funcionalidades**:
- RecyclerView con adaptador personalizado
- Petición GET a `/api/centros/{id}/arboles`
- Filtro por centro educativo (Spinner o Tabs)
- Indicadores visuales de alertas activas (icono rojo si hay alertas)
- Click en árbol navega a DetalleArbolActivity
- Botón flotante (FAB) para añadir nuevo árbol (solo Admin/Profesor)

**Layout**: `activity_listado_arboles.xml`

**Ejemplo de código**:

```java
public class ListadoArbolesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArbolesAdapter adapter;
    private List<Arbol> arboles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_arboles);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ArbolesAdapter(arboles, this::onArbolClick);
        recyclerView.setAdapter(adapter);

        cargarArboles();
    }

    private void cargarArboles() {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        Call<List<Arbol>> call = api.getArboles();

        call.enqueue(new Callback<List<Arbol>>() {
            @Override
            public void onResponse(Call<List<Arbol>> call, Response<List<Arbol>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    arboles.clear();
                    arboles.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Arbol>> call, Throwable t) {
                Toast.makeText(ListadoArbolesActivity.this, "Error al cargar árboles", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onArbolClick(Arbol arbol) {
        Intent intent = new Intent(this, DetalleArbolActivity.class);
        intent.putExtra("arbol_id", arbol.getId());
        startActivity(intent);
    }
}
```

### 3. DetalleArbolActivity

**Descripción**: Muestra información completa de un árbol y sus lecturas de sensores.

**Funcionalidades**:
- Información del árbol (especie, fecha plantación, ubicación GPS)
- Última lectura de sensores:
  - Temperatura (°C)
  - Humedad del suelo (%)
  - pH
  - Nivel de agua (%)
- Timestamp de la última lectura
- Botón "Editar" (navega a EditarArbolActivity)
- Botón "Eliminar" (confirma y llama DELETE)

**Layout**: `activity_detalle_arbol.xml`

**Ejemplo de código**:

```java
public class DetalleArbolActivity extends AppCompatActivity {
    private TextView especieText, temperaturaText, humedadText, phText, nivelAguaText;
    private Button editarBtn, eliminarBtn;
    private long arbolId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_arbol);

        arbolId = getIntent().getLongExtra("arbol_id", -1);

        especieText = findViewById(R.id.especieText);
        temperaturaText = findViewById(R.id.temperaturaText);
        humedadText = findViewById(R.id.humedadText);
        phText = findViewById(R.id.phText);
        nivelAguaText = findViewById(R.id.nivelAguaText);
        editarBtn = findViewById(R.id.editarBtn);
        eliminarBtn = findViewById(R.id.eliminarBtn);

        cargarDetalle();

        editarBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditarArbolActivity.class);
            intent.putExtra("arbol_id", arbolId);
            startActivity(intent);
        });

        eliminarBtn.setOnClickListener(v -> confirmarEliminar());
    }

    private void cargarDetalle() {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        Call<Arbol> call = api.getArbolById(arbolId);

        call.enqueue(new Callback<Arbol>() {
            @Override
            public void onResponse(Call<Arbol> call, Response<Arbol> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Arbol arbol = response.body();
                    mostrarDatos(arbol);
                }
            }

            @Override
            public void onFailure(Call<Arbol> call, Throwable t) {
                Toast.makeText(DetalleArbolActivity.this, "Error al cargar detalle", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDatos(Arbol arbol) {
        especieText.setText(arbol.getEspecie());

        Lectura ultimaLectura = arbol.getUltimaLectura();
        if (ultimaLectura != null) {
            temperaturaText.setText(String.format("%.1f °C", ultimaLectura.getTemperatura()));
            humedadText.setText(String.format("%.1f %%", ultimaLectura.getHumedadSuelo()));
            phText.setText(String.format("%.2f", ultimaLectura.getPh()));
            nivelAguaText.setText(String.format("%.1f %%", ultimaLectura.getNivelAgua()));
        }
    }

    private void confirmarEliminar() {
        new AlertDialog.Builder(this)
            .setTitle("Eliminar árbol")
            .setMessage("¿Estás seguro de eliminar este árbol?")
            .setPositiveButton("Eliminar", (dialog, which) -> eliminarArbol())
            .setNegativeButton("Cancelar", null)
            .show();
    }

    private void eliminarArbol() {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = api.deleteArbol(arbolId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DetalleArbolActivity.this, "Árbol eliminado", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(DetalleArbolActivity.this, "Error al eliminar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
```

### 4. EditarArbolActivity

**Descripción**: Formulario para editar información de un árbol.

**Funcionalidades**:
- Formulario prellenado con datos actuales
- Validación de campos
- Petición PUT a `/api/arboles/{id}`
- **Los datos de sensores NO son editables** (solo información del árbol)
- Redirección a DetalleArbolActivity tras edición exitosa

**Layout**: `activity_editar_arbol.xml`

**Ejemplo de código**:

```java
public class EditarArbolActivity extends AppCompatActivity {
    private EditText especieInput, ubicacionInput;
    private Button guardarBtn;
    private long arbolId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_arbol);

        arbolId = getIntent().getLongExtra("arbol_id", -1);

        especieInput = findViewById(R.id.especieInput);
        ubicacionInput = findViewById(R.id.ubicacionInput);
        guardarBtn = findViewById(R.id.guardarBtn);

        cargarArbol();

        guardarBtn.setOnClickListener(v -> guardarCambios());
    }

    private void cargarArbol() {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        Call<Arbol> call = api.getArbolById(arbolId);

        call.enqueue(new Callback<Arbol>() {
            @Override
            public void onResponse(Call<Arbol> call, Response<Arbol> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Arbol arbol = response.body();
                    especieInput.setText(arbol.getEspecie());
                    ubicacionInput.setText(arbol.getUbicacion());
                }
            }

            @Override
            public void onFailure(Call<Arbol> call, Throwable t) {
                Toast.makeText(EditarArbolActivity.this, "Error al cargar árbol", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarCambios() {
        String especie = especieInput.getText().toString().trim();
        String ubicacion = ubicacionInput.getText().toString().trim();

        if (especie.isEmpty()) {
            Toast.makeText(this, "La especie no puede estar vacía", Toast.LENGTH_SHORT).show();
            return;
        }

        Arbol arbolActualizado = new Arbol();
        arbolActualizado.setId(arbolId);
        arbolActualizado.setEspecie(especie);
        arbolActualizado.setUbicacion(ubicacion);

        ApiService api = ApiClient.getClient().create(ApiService.class);
        Call<Arbol> call = api.updateArbol(arbolId, arbolActualizado);

        call.enqueue(new Callback<Arbol>() {
            @Override
            public void onResponse(Call<Arbol> call, Response<Arbol> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditarArbolActivity.this, "Árbol actualizado", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Arbol> call, Throwable t) {
                Toast.makeText(EditarArbolActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
```

### 5. AlertasActivity (opcional pero recomendada)

**Descripción**: Listado de alertas activas para los árboles del usuario.

**Funcionalidades**:
- RecyclerView con alertas
- Petición GET a `/api/alertas/activas`
- Indicador del tipo de alerta (temperatura alta, pH bajo, etc.)
- Botón para marcar como resuelta

## Configuración de Retrofit

### ApiService.java

```java
public interface ApiService {
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("arboles")
    Call<List<Arbol>> getArboles();

    @GET("arboles/{id}")
    Call<Arbol> getArbolById(@Path("id") long id);

    @POST("centros/{centroId}/arboles")
    Call<Arbol> createArbol(@Path("centroId") long centroId, @Body Arbol arbol);

    @PUT("arboles/{id}")
    Call<Arbol> updateArbol(@Path("id") long id, @Body Arbol arbol);

    @DELETE("arboles/{id}")
    Call<Void> deleteArbol(@Path("id") long id);

    @GET("arboles/{id}/lecturas")
    Call<List<Lectura>> getLecturas(@Path("id") long id);

    @GET("alertas/activas")
    Call<List<Alerta>> getAlertasActivas();
}
```

### AuthInterceptor.java

```java
public class AuthInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        // Obtener token de SharedPreferences
        // (Necesita contexto, se puede pasar en constructor)
        String token = getTokenFromPreferences();

        if (token != null) {
            Request request = original.newBuilder()
                .header("Authorization", "Bearer " + token)
                .build();
            return chain.proceed(request);
        }

        return chain.proceed(original);
    }

    private String getTokenFromPreferences() {
        // Implementar acceso a SharedPreferences
        return null; // Placeholder
    }
}
```

## Dependencias

Archivo `app/build.gradle`:

```gradle
plugins {
    id 'com.android.application'
    id 'com.google.gms.google-services'  // Firebase
}

android {
    namespace 'com.arboles'
    compileSdk 34

    defaultConfig {
        applicationId "com.arboles"
        minSdk 24
        targetSdk 34
        versionCode 1
        versionName "1.0"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_21
        targetCompatibility JavaVersion.VERSION_21
    }
}

dependencies {
    // Android Core
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'androidx.recyclerview:recyclerview:1.3.2'

    // Retrofit
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'

    // OkHttp
    implementation 'com.squareup.okhttp3:okhttp:4.11.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.11.0'

    // Gson
    implementation 'com.google.code.gson:gson:2.10.1'

    // Firebase
    implementation platform('com.google.firebase:firebase-bom:32.7.0')
    implementation 'com.google.firebase:firebase-messaging'

    // Testing
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
}
```

## Permisos

Archivo `AndroidManifest.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.ProyectoArboles"
        android:usesCleartextTraffic="true">

        <activity
            android:name=".activities.LoginActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <activity android:name=".activities.ListadoArbolesActivity" />
        <activity android:name=".activities.DetalleArbolActivity" />
        <activity android:name=".activities.EditarArbolActivity" />
        <activity android:name=".activities.AlertasActivity" />

        <!-- Firebase Messaging Service -->
        <service
            android:name=".services.MyFirebaseMessagingService"
            android:exported="false">
            <intent-filter>
                <action android:name="com.google.firebase.MESSAGING_EVENT" />
            </intent-filter>
        </service>
    </application>
</manifest>
```

## Autenticación con JWT

El token JWT se almacena en SharedPreferences tras un login exitoso:

```java
SharedPreferences prefs = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
prefs.edit().putString("jwt_token", token).apply();
```

Y se incluye en todas las peticiones mediante el AuthInterceptor.

## Notificaciones Push con Firebase

Crear servicio `MyFirebaseMessagingService.java`:

```java
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        if (message.getNotification() != null) {
            String title = message.getNotification().getTitle();
            String body = message.getNotification().getBody();

            mostrarNotificacion(title, body);
        }
    }

    private void mostrarNotificacion(String title, String body) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "alertas_channel")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }
}
```

## Build

### Debug APK

```bash
./gradlew assembleDebug
```

El archivo APK se generará en: `app/build/outputs/apk/debug/app-debug.apk`

### Release APK (firmado)

1. Configurar keystore en `app/build.gradle`:

```gradle
android {
    signingConfigs {
        release {
            storeFile file("../keystore/release.keystore")
            storePassword "tu_password"
            keyAlias "tu_alias"
            keyPassword "tu_password"
        }
    }

    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}
```

2. Compilar:

```bash
./gradlew assembleRelease
```

**IMPORTANTE**: No commitear el keystore ni las contraseñas. Usar variables de entorno o archivos locales ignorados en `.gitignore`.

## Testing

```bash
# Tests unitarios
./gradlew test

# Tests instrumentados (requiere emulador/dispositivo)
./gradlew connectedAndroidTest
```

## Requisitos Académicos Cumplidos

- **[PGL]**:
  - ✅ Mínimo 4 actividades (Login, Listado, Detalle, Editar, Alertas)
  - ✅ RecyclerView con adaptador personalizado
  - ✅ Consumo de API REST con Retrofit
  - ✅ Autenticación con JWT
  - ✅ Validación de formularios
  - ✅ Material Design 3 aplicado
  - ✅ Notificaciones push con Firebase
  - ✅ Navegación entre actividades con intents
  - ✅ Manejo de permisos (INTERNET, POST_NOTIFICATIONS)

## Estado

En desarrollo

## Contacto

Proyecto Final DAM 2025-2026