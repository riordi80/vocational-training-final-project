# Manual de Instalación - Proyecto Árboles (Garden Monitor)

**Última actualización**: 2025-12-07

---

## Introducción

Este manual describe el proceso de instalación completo del sistema **Garden Monitor** (Proyecto Árboles), un sistema de monitorización IoT para árboles en centros educativos.

### Descripción del Sistema

Garden Monitor permite:
- Monitorizar el estado de árboles mediante sensores IoT
- Gestionar centros educativos y sus árboles
- Visualizar datos en tiempo real desde aplicaciones web y móvil
- Almacenar histórico de datos ambientales

### Arquitectura del Sistema

El sistema está compuesto por 4 componentes principales:

```mermaid
graph TB
    subgraph Capa de Presentación
        USER[Usuarios Finales]
        WEB[Frontend Web<br/>React + Vite + Tailwind]
        ANDROID[App Android<br/>Java + Material Design]
    end

    subgraph Capa de Aplicación
        API[Backend API REST<br/>Spring Boot 3 + Java 21]
    end

    subgraph Capa de Datos
        DB[(PostgreSQL 16<br/>+ TimescaleDB)]
    end

    USER -->|Navegador| WEB
    USER -->|Dispositivo móvil| ANDROID
    WEB -->|HTTP/REST| API
    ANDROID -->|HTTP/REST| API
    API -->|JPA/Hibernate| DB

    style WEB fill:#10b981,stroke:#059669,color:#fff
    style ANDROID fill:#10b981,stroke:#059669,color:#fff
    style API fill:#3b82f6,stroke:#2563eb,color:#fff
    style DB fill:#8b5cf6,stroke:#7c3aed,color:#fff
    style USER fill:#f59e0b,stroke:#d97706,color:#fff
```

### Componentes

1. **Base de Datos**: PostgreSQL 16 + TimescaleDB (almacenamiento)
2. **Backend**: API REST con Spring Boot (lógica de negocio)
3. **Frontend**: Aplicación web con React (interfaz web)
4. **Android**: Aplicación móvil nativa (interfaz móvil)

---

## Requisitos Previos

### Hardware Recomendado

- **RAM**: 8 GB mínimo (16 GB recomendado)
- **Disco**: 20 GB libres mínimo
- **Procesador**: Multinúcleo (4 cores recomendado)

### Software Necesario

| Componente | Versión Requerida | Verificación |
|------------|------------------|--------------|
| Java JDK | 21 | `java -version` |
| Node.js | 18+ | `node -v` |
| npm | 9+ | `npm -v` |
| PostgreSQL | 16+ | `psql --version` |
| Git | 2.0+ | `git --version` |
| Android Studio | Última estable | (Opcional, solo para Android) |

### Verificación de Requisitos

```bash
# Verificar todas las versiones
java -version      # Debe mostrar Java 21
node -v            # Debe mostrar v18.x o superior
npm -v             # Debe mostrar 9.x o superior
psql --version     # Debe mostrar PostgreSQL 16.x
git --version      # Debe mostrar Git 2.x
```

---

## Orden de Instalación Recomendado

**IMPORTANTE**: Sigue este orden para evitar problemas de dependencias:

1. Base de Datos (PostgreSQL + TimescaleDB)
2. Backend (Spring Boot)
3. Frontend (React)
4. Android (Opcional)

---

## 1. Instalación de la Base de Datos

### 1.1 Instalar PostgreSQL 16

Si no tienes PostgreSQL 16 instalado, consulta la [Guía de Configuración PostgreSQL](./04b.%20CONFIGURACION_POSTGRESQL.md).

### 1.2 Clonar el Repositorio

```bash
git clone https://github.com/riordi80/vocational-training-final-project
cd vocational-training-final-project
```

### 1.3 Crear la Base de Datos

**Opción 1: Usar el script completo (Recomendado)**

```bash
# Ejecutar desde la raíz del proyecto
psql -U postgres -f backend/create_database.sql
```

El script `create_database.sql` crea:
- Base de datos `proyecto_arboles`
- Usuario `arboles_user`
- Extensión TimescaleDB
- Todas las tablas necesarias
- Índices y constraints

**Opción 2: Creación manual**

```bash
# Acceder a PostgreSQL
sudo -i -u postgres
psql

# Crear usuario
CREATE USER arboles_user WITH PASSWORD 'TU_PASSWORD_SEGURA';
ALTER USER arboles_user CREATEDB;

# Crear base de datos
CREATE DATABASE proyecto_arboles;
GRANT ALL PRIVILEGES ON DATABASE proyecto_arboles TO arboles_user;

# Conectar a la BD
\c proyecto_arboles

# Habilitar TimescaleDB
CREATE EXTENSION IF NOT EXISTS timescaledb;

# Dar permisos
GRANT ALL ON SCHEMA public TO arboles_user;
```

### 1.4 Verificar la Instalación

```bash
# Conectarse a la BD
psql -U arboles_user -d proyecto_arboles

# Verificar tablas (debería mostrar: arboles, centros_educativos, etc.)
\dt

# Salir
\q
```

**Para más detalles**: Ver [backend/README.md - Sección "Configurar la Base de Datos"](../backend/README.md#2-configurar-la-base-de-datos)

---

## 2. Instalación del Backend

### 2.1 Navegar al directorio del backend

```bash
cd backend
```

### 2.2 Configurar `application-local.properties`

Crear el archivo `src/main/resources/application-local.properties`:

```properties
# Configuración de Base de Datos
spring.datasource.url=jdbc:postgresql://localhost:5432/proyecto_arboles
spring.datasource.username=arboles_user
spring.datasource.password=TU_PASSWORD_REAL_AQUI

# Configuración JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

**IMPORTANTE**:
- Este archivo **NO debe commitearse** (ya está en `.gitignore`)
- Lee [backend/src/main/resources/README_CONFIG.md](../backend/src/main/resources/README_CONFIG.md) para configuración segura

### 2.3 Compilar el Proyecto

**Linux/Mac**:
```bash
./mvnw clean install
```

**Windows**:
```cmd
mvnw.cmd clean install
```

### 2.4 Ejecutar el Backend

**Linux/Mac**:
```bash
./mvnw spring-boot:run
```

**Windows**:
```cmd
mvnw.cmd spring-boot:run
```

### 2.5 Verificar que Funciona

El backend estará disponible en: `http://localhost:8080`

```bash
# Verificar que responde
curl http://localhost:8080/api/centros

# Debería devolver un array JSON (puede estar vacío: [])
```

**Para más detalles**: Ver [backend/README.md](../backend/README.md)

---

## 3. Instalación del Frontend

### 3.1 Navegar al directorio del frontend

```bash
# Desde la raíz del proyecto
cd frontend
```

### 3.2 Instalar Dependencias

```bash
npm install
```

### 3.3 Configurar Variables de Entorno

Crear archivo `.env` en la raíz de `frontend/`:

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

**IMPORTANTE**: Existe un archivo `.env.example` como referencia.

### 3.4 Ejecutar el Frontend

```bash
npm run dev
```

La aplicación estará disponible en: `http://localhost:5173`

### 3.5 Verificar que Funciona

1. Abre el navegador en `http://localhost:5173`
2. Deberías ver la pantalla de Login
3. Intenta registrarte y hacer login
4. Navega a "Árboles" (debería mostrar lista vacía o con datos)

**Para más detalles**: Ver [frontend/README.md](../frontend/README.md)

---

## 4. Instalación de Android (Opcional)

La aplicación Android es opcional y requiere Android Studio.

### 4.1 Requisitos Previos

- Android Studio (Arctic Fox o superior)
- JDK 21
- Android SDK (API Level 24+)

### 4.2 Proceso de Instalación

**Para instrucciones completas de instalación Android**, consulta:
- [MANUAL_DE_INSTALACION_ANDROID.md](./MANUAL_DE_INSTALACION_ANDROID.md)

**Pasos resumidos**:

1. Abrir Android Studio
2. File → Open → Seleccionar carpeta `android/`
3. Esperar sincronización de Gradle
4. Configurar URL del backend en `RetrofitClient.java`:
   ```java
   // Para emulador
   private static final String BASE_URL = "http://10.0.2.2:8080/";

   // Para dispositivo físico (usar tu IP local)
   private static final String BASE_URL = "http://192.168.1.X:8080/";
   ```
5. Run → Run 'app'

**Documentación técnica**: Ver [android/README.md](../android/README.md)

---

## 5. Verificación del Sistema Completo

### 5.1 Verificar Integración Backend ↔ Base de Datos

```bash
# Crear un centro educativo desde el backend
curl -X POST http://localhost:8080/api/centros \
  -H "Content-Type: application/json" \
  -d '{"nombre":"IES Test","direccion":"Calle Test 123","ciudad":"Madrid"}'

# Verificar que se creó
curl http://localhost:8080/api/centros
```

### 5.2 Verificar Integración Frontend ↔ Backend

1. Abrir navegador en `http://localhost:5173`
2. Login/Register
3. Ir a sección "Árboles"
4. Intentar crear un nuevo árbol
5. Verificar que aparece en la lista

### 5.3 Verificar Integración Android ↔ Backend (si aplica)

1. Ejecutar app Android
2. Login
3. Ver lista de árboles (deberían aparecer los mismos que en web)
4. Intentar editar o eliminar un árbol
5. Verificar cambios en la web

---

## 6. Solución de Problemas Comunes

### Problema: Backend no arranca

**Error**: `Cannot connect to database`

**Solución**:
1. Verificar que PostgreSQL está corriendo: `sudo systemctl status postgresql`
2. Verificar credenciales en `application-local.properties`
3. Verificar que la BD existe: `psql -U arboles_user -d proyecto_arboles`

---

**Error**: `Port 8080 already in use`

**Solución**:
1. Matar proceso en puerto 8080: `sudo lsof -t -i:8080 | xargs kill -9`
2. O cambiar puerto en `application.properties`: `server.port=8081`

---

### Problema: Frontend no conecta con Backend

**Error**: `Network Error` o `CORS Error`

**Solución**:
1. Verificar que backend está corriendo en `http://localhost:8080`
2. Verificar variable `VITE_API_BASE_URL` en `.env`
3. Reiniciar servidor de desarrollo: `npm run dev`
4. Verificar CORS en backend (ver `backend/src/main/java/com/example/gardenmonitor/config/CorsConfig.java`)

---

### Problema: Android no conecta con Backend

**Error**: `Connection refused` o `Timeout`

**Solución**:

**Para emulador**:
- Usar `http://10.0.2.2:8080/` (NO `localhost`)

**Para dispositivo físico**:
1. Verificar que ordenador y dispositivo están en la misma WiFi
2. Obtener IP local del ordenador: `ip addr` (Linux) o `ipconfig` (Windows)
3. Usar `http://TU_IP_LOCAL:8080/` en `RetrofitClient.java`
4. Desactivar firewall temporalmente para probar

---

### Problema: Dependencias no se instalan

**Maven (Backend)**:
```bash
# Limpiar caché de Maven
./mvnw clean
rm -rf ~/.m2/repository
./mvnw install
```

**npm (Frontend)**:
```bash
# Limpiar caché de npm
npm cache clean --force
rm -rf node_modules package-lock.json
npm install
```

**Gradle (Android)**:
```
Android Studio → File → Invalidate Caches / Restart
Build → Clean Project
Build → Rebuild Project
```

---

## 7. Configuración: Desarrollo Local vs Producción

### Introducción

El sistema está diseñado para funcionar tanto en **desarrollo local** como en **producción** con mínimos cambios de configuración. Esta sección explica cómo está configurado cada componente y qué necesitas modificar si alternas entre entornos.

### Frontend (React + Vite)

El frontend usa **variables de entorno** mediante el archivo `.env`:

**Configuración actual** (`frontend/.env`):
```env
VITE_API_BASE_URL=http://localhost:8080/api
```

**Para desarrollo local**:
- El archivo `.env` ya apunta a `localhost:8080`
- No requiere cambios
- Ejecutar: `npm run dev`

**Para producción (Vercel)**:
- Vercel lee la variable `VITE_API_BASE_URL` de su dashboard
- Configurada como: `https://proyecto-arboles-backend.onrender.com/api`
- Deploy automático desde GitHub

### Backend (Spring Boot)

El backend usa **Spring Profiles** para gestionar diferentes entornos:

**Configuración actual** (`backend/src/main/resources/application.properties`):
```properties
spring.profiles.active=local
```

**Para desarrollo local**:
- Perfil `local` activo por defecto
- Lee configuración de `application-local.properties`
- Se conecta a PostgreSQL local
- No requiere cambios
- Ejecutar: `./mvnw spring-boot:run`

**Para producción (Render)**:
- Render establece `SPRING_PROFILES_ACTIVE=prod` mediante variable de entorno
- Lee configuración de `application-prod.properties`
- Se conecta a PostgreSQL de Render
- Deploy automático desde GitHub

### Resumen

Para trabajar en **desarrollo local** después del despliegue:

| Componente | Cambios Necesarios |
|------------|-------------------|
| **Frontend** | Ninguno (ya configurado para local) |
| **Backend** | Ninguno (perfil local por defecto) |
| **Android** | Ver [MANUAL_DE_INSTALACION_ANDROID.md](./MANUAL_DE_INSTALACION_ANDROID.md) |

Ambos componentes principales (Frontend y Backend) están configurados por defecto para desarrollo local. Solo necesitas iniciar los servidores con `npm run dev` y `./mvnw spring-boot:run` respectivamente.

Para información sobre configuración de la aplicación Android entre local y producción, consulta el manual específico de Android.

---

## 8. Siguientes Pasos

Una vez instalado el sistema:

1. **Lee el Manual de Usuario**: Ver [MANUAL_DE_USUARIO.md](./MANUAL_DE_USUARIO.md)
2. **Explora la documentación técnica**:
   - [Hoja de Ruta](./02.%20HOJA_DE_RUTA.md)
   - [Especificación Técnica](./03.%20ESPECIFICACION_TECNICA.md)
   - [Modelo de Datos](./04.%20MODELO_DATOS.md)
3. **Prueba el sistema**:
   - Crea centros educativos
   - Añade árboles
   - Edita y elimina datos
   - Prueba desde web y móvil

---

## 8. Despliegue en Producción (Opcional)

El sistema ya está desplegado en producción:

- **Frontend**: https://vocational-training-final-project.vercel.app/
- **Backend**: https://proyecto-arboles-backend.onrender.com
- **Base de Datos**: PostgreSQL en Render

### Importante: Limitaciones del Free Tier de Render

El backend está desplegado en Render con el plan gratuito, que tiene las siguientes características:

**Suspensión automática (Cold Start)**:
- El servicio se suspende tras **15 minutos de inactividad**
- Al primer acceso después de suspensión, el backend tarda **30-60 segundos** en reactivarse
- Durante este tiempo, el frontend mostrará: "No se pudo conectar con el servidor. Si es la primera carga, puede estar iniciándose (30-60 seg). Recarga la página en unos momentos."
- **Solución**: Esperar 1 minuto y recargar la página (F5)
- Una vez activo, el sistema funciona normalmente

**Esto es comportamiento normal y esperado** del free tier de Render. No indica un problema en la aplicación.

**Recomendaciones**:
- Para entornos de producción real, considerar plan de pago de Render
- Para desarrollo local, seguir las instrucciones de la Sección 7

Para detalles técnicos de despliegue, consulta:
- [backend/README.md - Sección "Despliegue en Render"](../backend/README.md#despliegue-en-render)
- [frontend/README.md - Sección "Despliegue en Vercel"](../frontend/README.md#despliegue-en-vercel-requisito-dad---completado)

---

## Recursos Adicionales

### Documentación del Proyecto

- [Índice de Documentación](./00.%20INDICE.md)
- [Git Workflow](./01.%20GIT_WORKFLOW.md)
- [Requisitos Académicos](./REQUISITOS.md)
- [Testing Postman](./TESTING_POSTMAN_RESULTS.md)

### Manuales

- [Manual de Usuario](./MANUAL_DE_USUARIO.md)
- [Manual de Instalación Android](./MANUAL_DE_INSTALACION_ANDROID.md)
- [Manual de Usuario Android](./MANUAL_DE_USUARIO_ANDROID.md)

### README por Componente

- [Backend README](../backend/README.md)
- [Frontend README](../frontend/README.md)
- [Android README](../android/README.md)

---

## Soporte

Para problemas o preguntas:

1. Revisar sección "Solución de Problemas" de este manual
2. Consultar README específico del componente
3. Revisar logs de la aplicación
4. Contactar al equipo de desarrollo

---

**Proyecto Final DAM 2025-2026**
