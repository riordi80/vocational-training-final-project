# Backend - Proyecto Árboles

API REST desarrollada con Spring Boot para el sistema de monitorización de árboles.

## Tecnologías

- **Framework**: Spring Boot 3.x
- **Lenguaje**: Java 21
- **Build**: Maven
- **Base de Datos**: PostgreSQL 15+ con TimescaleDB
- **ORM**: Spring Data JPA
- **Seguridad**: Spring Security + JWT
- **Documentación API**: Swagger/OpenAPI (a implementar)

## Estructura del Proyecto

```
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/arboles/
│   │   │       ├── config/         # Configuración (Security, CORS, etc.)
│   │   │       ├── controller/     # Controladores REST
│   │   │       ├── service/        # Lógica de negocio
│   │   │       ├── repository/     # Repositorios JPA
│   │   │       ├── model/          # Entidades JPA
│   │   │       ├── dto/            # DTOs para requests/responses
│   │   │       ├── security/       # JWT, UserDetails, etc.
│   │   │       ├── exception/      # Excepciones personalizadas
│   │   │       └── util/           # Utilidades
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       └── application-prod.properties
│   └── test/
├── pom.xml
├── .gitignore
└── README.md
```

## Requisitos Previos

- Java 21
- Maven 3.8+
- PostgreSQL 15+
- TimescaleDB (extensión de PostgreSQL)

## Instalación y Configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/riordi80/vocational-training-final-project
cd backend
```

### 2. Configurar la Base de Datos

Crear base de datos en PostgreSQL:

```sql
CREATE DATABASE proyecto_arboles;
\c proyecto_arboles
CREATE EXTENSION IF NOT EXISTS timescaledb;
```

### 3. Configurar `application.properties`

Editar `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/proyecto_arboles
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=tu_jwt_secret_key
jwt.expiration=86400000
```

**IMPORTANTE**: No commitear credenciales. Usar variables de entorno o `application-local.properties` (ignorado en .gitignore).

### 4. Compilar el proyecto

```bash
mvn clean install
```

### 5. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

El servidor estará disponible en: `http://localhost:8080`

## Endpoints Principales

### Autenticación
- `POST /api/auth/login` - Login de usuario
- `POST /api/auth/register` - Registro (si aplicable)
- `POST /api/auth/logout` - Logout
- `GET /api/auth/me` - Obtener usuario actual

### Usuarios
- `GET /api/users` - Listar usuarios
- `GET /api/users/{id}` - Obtener usuario
- `POST /api/users` - Crear usuario
- `PUT /api/users/{id}` - Actualizar usuario
- `DELETE /api/users/{id}` - Eliminar usuario

### Centros Educativos
- `GET /api/centros` - Listar centros
- `POST /api/centros` - Crear centro
- `PUT /api/centros/{id}` - Actualizar centro
- `DELETE /api/centros/{id}` - Eliminar centro

### Árboles (Relación 1:N - Requisito PGV Noviembre)
- `GET /api/centros/{id}/arboles` - Listar árboles de un centro
- `POST /api/centros/{id}/arboles` - Crear árbol en un centro
- `PUT /api/arboles/{id}` - Actualizar árbol
- `DELETE /api/arboles/{id}` - Eliminar árbol

### Usuario-Centro (Relación N:M - Requisito PGV Diciembre)
- `GET /api/usuarios/{userId}/centros` - Centros de un usuario
- `POST /api/usuarios/{userId}/centros/{centroId}` - Asignar usuario a centro
- `PUT /api/usuarios/{userId}/centros/{centroId}` - Actualizar asignación
- `DELETE /api/usuarios/{userId}/centros/{centroId}` - Eliminar asignación

### Lecturas de Sensores
- `POST /api/arboles/{id}/lecturas` - Crear lectura (desde ESP32)
- `GET /api/arboles/{id}/lecturas` - Obtener lecturas de un árbol
- `GET /api/lecturas/ultimas/{arbolId}` - Última lectura

### Alertas
- `GET /api/alertas` - Listar alertas
- `GET /api/alertas/activas` - Alertas activas
- `PUT /api/alertas/{id}/resolver` - Marcar alerta como resuelta

## Testing

```bash
# Ejecutar tests unitarios
mvn test

# Ejecutar tests de integración
mvn verify

# Generar reporte de cobertura
mvn jacoco:report
```

## Build para Producción

```bash
mvn clean package -DskipTests
```

El archivo `.jar` se generará en `target/proyecto-arboles-backend-{version}.jar`

## Variables de Entorno (Producción)

```bash
export DB_HOST=tu_host_db
export DB_PORT=5432
export DB_NAME=proyecto_arboles
export DB_USER=tu_usuario
export DB_PASSWORD=tu_password
export JWT_SECRET=tu_jwt_secret_muy_seguro
```

## Requisitos Académicos Cumplidos

- **[PGV] Noviembre**: Endpoints con relación 1:N (Centro → Árboles) con GET, POST, PUT, DELETE
- **[PGV] Diciembre**: Endpoints con relación N:M (Usuario ↔ Centro) con validaciones
- **[AED]**: Mapeo ORM con JPA, mínimo 2 entidades (Centro, Arbol), relaciones mapeadas

## Estado

En desarrollo

## Contacto

Proyecto Final DAM 2025-2026
