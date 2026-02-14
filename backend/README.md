# Backend - Garden Monitor (Proyecto Árboles)

API REST desarrollada con Spring Boot para el sistema de monitorización de árboles.

## Aplicación Desplegada

**URL en producción**: [https://proyecto-arboles-backend.onrender.com](https://proyecto-arboles-backend.onrender.com)

**Plataforma**: Render (deployment con Docker)

## Tecnologías

- **Framework**: Spring Boot 3.5.7
- **Lenguaje**: Java 21
- **Build**: Maven (con Maven Wrapper incluido)
- **Base de Datos**: PostgreSQL 16+ con TimescaleDB
- **ORM**: Spring Data JPA

## Estructura del Proyecto

```
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/gardenmonitor/
│   │   │       ├── GardenmonitorApplication.java  # Clase principal
│   │   │       ├── config/         # Configuración (CORS, etc.)
│   │   │       ├── controller/     # Controladores REST
│   │   │       ├── dto/            # DTOs (LoginRequest, RegisterRequest, AuthResponse, etc.)
│   │   │       ├── repository/     # Repositorios JPA
│   │   │       └── model/          # Entidades JPA
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application-local.properties (NO commitear)
│   └── test/
├── .mvn/                   # Maven Wrapper
├── mvnw                    # Maven Wrapper (Linux/Mac)
├── mvnw.cmd                # Maven Wrapper (Windows)
├── pom.xml
├── create_database.sql
├── drop_tables.sql
├── .gitignore
└── README.md
```

## Requisitos Previos

- Java 21
- PostgreSQL 16+
- TimescaleDB (extensión de PostgreSQL)

**Nota**: No necesitas instalar Maven manualmente, el proyecto incluye Maven Wrapper (`mvnw`).

## Instalación y Configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/riordi80/vocational-training-final-project
cd backend
```

### 2. Configurar la Base de Datos

#### Opción 1: Usar el script SQL completo (Recomendado)

```bash
# Ejecutar script de creación completo
psql -U postgres -f create_database.sql
```

El archivo [`create_database.sql`](./create_database.sql) contiene:
- Creación de base de datos
- Habilitación de extensión TimescaleDB
- Creación de todas las tablas (8 entidades)
- Configuración de hypertable para series temporales
- Todos los índices y constraints

#### Opción 2: Manual

```sql
CREATE DATABASE proyecto_arboles;
\c proyecto_arboles
CREATE EXTENSION IF NOT EXISTS timescaledb;
```

⚠️ **Para resetear la base de datos**, usar el script [`drop_tables.sql`](./drop_tables.sql):
```bash
psql -U arboles_user -d proyecto_arboles -f drop_tables.sql
```

### 3. Configurar `application-local.properties`

⚠️ **IMPORTANTE**: Lee primero [`src/main/resources/README_CONFIG.md`](./src/main/resources/README_CONFIG.md) para configuración segura.

El proyecto usa **Spring Profiles** para separar configuración local de producción:

- **`application.properties`** (commiteado): Configuración base **SIN credenciales**
- **`application-local.properties`** (NO commiteado): Credenciales locales **CON tu password**

#### Crear el archivo `src/main/resources/application-local.properties`:

```properties
# Configuración de Base de Datos LOCAL
spring.datasource.url=jdbc:postgresql://localhost:5432/proyecto_arboles
spring.datasource.username=arboles_user
spring.datasource.password=TU_PASSWORD_REAL_AQUI

# Configuración JPA (opcional, ya viene en application.properties)
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

**IMPORTANTE**:
- **NO modifiques** `application.properties` (ese archivo SÍ se commitea)
- Este archivo ya está en `.gitignore` y NO se debe commitear
- Para producción, se usan variables de entorno (ver sección "Despliegue en Render")

### 4. Compilar el proyecto

**Linux/Mac**:
```bash
./mvnw clean install
```

**Windows**:
```cmd
mvnw.cmd clean install
```

### 5. Ejecutar la aplicación

**Linux/Mac**:
```bash
./mvnw spring-boot:run
```

**Windows**:
```cmd
mvnw.cmd spring-boot:run
```

El servidor estará disponible en: `http://localhost:8080`

## Endpoints

### Centros Educativos
- `GET /api/centros` - Listar centros
- `GET /api/centros/{id}` - Obtener centro
- `POST /api/centros` - Crear centro
- `PUT /api/centros/{id}` - Actualizar centro
- `DELETE /api/centros/{id}` - Eliminar centro

### Árboles (Relación 1:N)
- `GET /api/centros/{id}/arboles` - Listar árboles de un centro
- `GET /api/arboles` - Listar todos los árboles
- `GET /api/arboles/{id}` - Obtener árbol
- `POST /api/arboles` - Crear árbol
- `PUT /api/arboles/{id}` - Actualizar árbol
- `DELETE /api/arboles/{id}` - Eliminar árbol

### Usuarios
- `GET /api/usuarios` - Listar todos
- `GET /api/usuarios/{id}` - Obtener por ID
- `POST /api/usuarios` - Crear nuevo
- `PUT /api/usuarios/{id}` - Actualizar
- `DELETE /api/usuarios/{id}` - Eliminar

### Asignación Usuario-Centro (Relación N:M)
- `GET /api/usuario-centro/usuario/{usuarioId}` - Centros de un usuario
- `GET /api/usuario-centro/centro/{centroId}` - Coordinadores de un centro
- `POST /api/usuario-centro` - Asignar coordinador a centro
- `DELETE /api/usuario-centro/{id}` - Desasignar coordinador

### Autenticación
- `POST /api/auth/login` - Login con email y password (valida contra BD)
- `POST /api/auth/register` - Registro de nuevo usuario (rol COORDINADOR por defecto)

## Testing

**Linux/Mac**:
```bash
# Ejecutar tests unitarios
./mvnw test

# Ejecutar tests de integración
./mvnw verify

# Generar reporte de cobertura
./mvnw jacoco:report
```

**Windows**:
```cmd
mvnw.cmd test
mvnw.cmd verify
mvnw.cmd jacoco:report
```

## Build para Producción

**Linux/Mac**:
```bash
./mvnw clean package -DskipTests
```

**Windows**:
```cmd
mvnw.cmd clean package -DskipTests
```

El archivo `.jar` se generará en `target/gardenmonitor-0.0.1-SNAPSHOT.jar`

## Variables de Entorno (Producción)

Para producción en Render, las variables de entorno se configuran en el dashboard:

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://[HOST]:5432/[DATABASE]
SPRING_DATASOURCE_USERNAME=[USER]
SPRING_DATASOURCE_PASSWORD=[PASSWORD]
SPRING_PROFILES_ACTIVE=prod
```

Ver sección "Despliegue en Render" más abajo para detalles completos.

## Requisitos Académicos

### [PGV] Programación de Servicios y Procesos - Noviembre
- [x] 2 endpoints con relación 1:N (CentroEducativo → Arbol)
- [x] GET, POST, PUT, DELETE implementados
- [x] Validaciones de datos

### [AED] Acceso a Datos
- [x] Modelo de datos documentado
- [x] Mapeo ORM con JPA completado (4 entidades JPA)
- [x] Repositorios JPA con queries derivadas
- [x] Relaciones bidireccionales implementadas

## Estado del Backend

**API REST completada y desplegada en producción**

- [x] 6 Entidades JPA con anotaciones completas, Javadoc y validaciones
- [x] Repositorios JPA con queries derivadas
- [x] Relaciones bidireccionales (CentroEducativo ↔ Arbol)
- [x] Relación N:M (Usuario ↔ CentroEducativo via UsuarioCentro)
- [x] CRUD completo para Árboles, Centros Educativos y Usuarios
- [x] Autenticación real contra BD (login/register via AuthController)
- [x] Sistema de roles (ADMIN, COORDINADOR)
- [x] Validaciones con Bean Validation
- [x] Testing Postman completado
- [x] Desplegado en Render con PostgreSQL 16
- [x] CORS configurado para frontend en producción
- [x] Perfiles Spring Boot (local/prod)

## Archivos Importantes del Backend

### Scripts SQL
- [`create_database.sql`](./create_database.sql) - Script completo de creación de base de datos (8 tablas)
- [`drop_tables.sql`](./drop_tables.sql) - Script para eliminar todas las tablas (resetear BD)

### Configuración
- [`src/main/resources/README_CONFIG.md`](./src/main/resources/README_CONFIG.md) - **LEER PRIMERO**: Guía de configuración segura
- `src/main/resources/application.properties` - Configuración base (commiteada)
- `src/main/resources/application-local.properties` - Credenciales locales (NO commitear)

### Documentación Relacionada

#### Manuales de Usuario
- [Manual de Instalación](../docs/MANUAL_DE_INSTALACION.md) - Guía completa de instalación
- [Manual de Usuario](../docs/MANUAL_DE_USUARIO.md) - Guía de uso del sistema

#### Documentación Técnica
- [Índice de Documentación](../docs/00.%20INDICE.md) - Índice completo
- [Hoja de Ruta](../docs/02.%20HOJA_DE_RUTA.md) - Planificación por fases
- [Especificación Técnica](../docs/03.%20ESPECIFICACION_TECNICA.md) - Requisitos y arquitectura
- [Modelo de Datos](../docs/04.%20MODELO_DATOS.md) - Diagramas E/R, UML y Relacional
- [Configuración PostgreSQL](../docs/04b.%20CONFIGURACION_POSTGRESQL.md) - Instalación de BD
- [Testing Postman](../docs/TESTING_POSTMAN_RESULTS.md) - Resultados de pruebas

## Despliegue en Render

### Aplicación Desplegada

- **URL**: https://proyecto-arboles-backend.onrender.com
- **Plataforma**: Render
- **Tipo**: Web Service (Docker)
- **Base de Datos**: PostgreSQL 16 (Internal Database en Render)

### Configuración del Despliegue

#### 1. Dockerfile

El proyecto incluye un `Dockerfile` con build multi-stage optimizado:

```dockerfile
# Stage 1: Build
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### 2. Configuración en Render

**Settings → Build & Deploy:**
- **Environment**: Docker
- **Branch**: main
- **Dockerfile Path**: backend/Dockerfile (si está en monorepo) o ./Dockerfile
- **Auto-Deploy**: Yes (deploy automático en cada push a main)

**Environment Variables:**
```
SPRING_DATASOURCE_URL=jdbc:postgresql://[HOST]:5432/[DATABASE]
SPRING_DATASOURCE_USERNAME=[USER]
SPRING_DATASOURCE_PASSWORD=[PASSWORD]
SPRING_PROFILES_ACTIVE=prod
```

#### 3. Configuración de CORS

El backend está configurado para permitir requests desde:
- `http://localhost:5173` (desarrollo local)
- `https://vocational-training-final-project.vercel.app` (frontend en producción)

Archivo: `backend/src/main/java/com/example/gardenmonitor/config/CorsConfig.java`

#### 4. Perfiles de Spring Boot

- **local**: Para desarrollo local (usa `application-local.properties`)
- **prod**: Para producción en Render (usa `application-prod.properties` con variables de entorno)

#### 5. Base de Datos

**PostgreSQL en Render:**
- Crear PostgreSQL database en Render
- Copiar "Internal Database URL" y separar en componentes:
  - URL: `jdbc:postgresql://[HOST]:5432/[DATABASE]`
  - Username: [USER]
  - Password: [PASSWORD]
- Configurar como variables de entorno en el Web Service

### Verificación del Despliegue

```bash
# Verificar que el backend responde
curl https://proyecto-arboles-backend.onrender.com/api/centros

# Verificar endpoint específico
curl https://proyecto-arboles-backend.onrender.com/api/arboles
```

### Troubleshooting

**Error: CORS**
- Verificar que CorsConfig.java incluye la URL del frontend en producción
- Verificar que no hay anotaciones `@CrossOrigin` en los controllers

**Error: Base de datos**
- Verificar que las variables de entorno están configuradas correctamente
- Verificar que la URL de base de datos usa el formato JDBC correcto
- Verificar que el usuario/contraseña son correctos

**Error: Build**
- Verificar que el Dockerfile está en la raíz correcta
- Verificar que Java 21 está configurado en el Dockerfile
- Revisar logs de build en Render Dashboard

### Limitaciones del Free Tier de Render

El backend está desplegado en Render con el plan gratuito, que tiene las siguientes características:

**Suspensión automática (Cold Start)**:
- El servicio se suspende tras 15 minutos de inactividad
- Al primer acceso después de suspensión, tarda 30-60 segundos en reactivarse
- Esto es comportamiento normal y esperado del free tier

**Recomendaciones**:
- Para entornos de producción real, considerar plan de pago
- El frontend está configurado para manejar este comportamiento
- Los timeouts están configurados en 60 segundos

## Configuración: Desarrollo Local vs Producción

### Para Desarrollo Local

El proyecto está configurado por defecto para desarrollo local:

1. **Perfil activo**: `local` (ver `application.properties`)
2. **Configuración**: `application-local.properties`
3. **Base de datos**: PostgreSQL local (localhost:5432)
4. **Puerto**: 8080
5. **No requiere cambios** para trabajar en local

### Para Producción (Render)

Render activa automáticamente el perfil de producción:

1. **Perfil activo**: `prod` (variable de entorno `SPRING_PROFILES_ACTIVE=prod`)
2. **Configuración**: `application-prod.properties`
3. **Base de datos**: PostgreSQL de Render (variables de entorno)
4. **CORS**: Configurado para frontend en Vercel
5. **Deploy automático** desde la rama `main`

Ver [Manual de Instalación](../docs/MANUAL_DE_INSTALACION.md) para más detalles sobre configuración de entornos.

---

## Información del Proyecto

**Nombre**: Garden Monitor - Sistema de Monitorización de Árboles

**Institución**: IES El Rincón

**Curso**: Desarrollo de Aplicaciones Multiplataforma (DAM) 2025-2026

**Repositorio**: [github.com/riordi80/vocational-training-final-project](https://github.com/riordi80/vocational-training-final-project)

**Última actualización**: 2026-02-14

### Colaboradores

[![riordi80](https://img.shields.io/badge/riordi80-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/riordi80) [![Enrique36247](https://img.shields.io/badge/Enrique36247-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/Enrique36247)

---

**Proyecto Final DAM 2025-2026** | Desarrollado con Spring Boot, React, Android y ESP32
