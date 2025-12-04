# Backend - Garden Monitor (Proyecto Árboles)

API REST desarrollada con Spring Boot para el sistema de monitorización de árboles.

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
│   │   │       ├── controller/     # Controladores REST
│   │   │       ├── service/        # Lógica de negocio
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

- Java 21 [x]
- PostgreSQL 15+ (a instalar)
- TimescaleDB (extensión de PostgreSQL, a instalar)

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

### 3. Configurar `application.properties`

⚠️ **IMPORTANTE**: Lee primero [`src/main/resources/README_CONFIG.md`](./src/main/resources/README_CONFIG.md) para configuración segura.

El proyecto usa un patrón de configuración de 2 capas:

1. **`application.properties`** (commiteado): Configuración base sin credenciales
2. **`application-local.properties`** (NO commiteado): Credenciales reales

#### Crear `application-local.properties`:

```properties
# Copiar desde application.properties y completar con valores reales
spring.datasource.url=jdbc:postgresql://localhost:5432/proyecto_arboles
spring.datasource.username=arboles_user
spring.datasource.password=TU_PASSWORD_REAL_AQUI

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=TU_JWT_SECRET_MUY_SEGURO_AQUI
jwt.expiration=86400000
```

**Alternativa**: Usar variables de entorno (recomendado para producción).

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

## Endpoints (Requisito PGV Noviembre)

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

```bash
export DB_HOST=tu_host_db
export DB_PORT=5432
export DB_NAME=proyecto_arboles
export DB_USER=tu_usuario
export DB_PASSWORD=tu_password
export JWT_SECRET=tu_jwt_secret_muy_seguro
```

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

## Estado del Proyecto

**Fase actual**: Fase 2 COMPLETADA - API REST 1:N

### Completado (Fase 0)
- [x] Configuración de PostgreSQL + TimescaleDB
- [x] Modelo de datos diseñado (8 entidades)
- [x] Scripts SQL de creación (`create_database.sql`)
- [x] Scripts SQL de eliminación (`drop_tables.sql`)
- [x] Configuración de Spring Boot (`application.properties`)
- [x] Estructura del proyecto establecida

### Completado (Fase 1 - Backend: Base de Datos y Modelo)
- [x] **Entidades JPA completadas con Javadoc y equals/hashCode**:
  - `Usuario` con anotaciones JPA completas, Javadoc, equals/hashCode optimizado para JPA
  - `Rol` (enum: ADMIN, PROFESOR, ESTUDIANTE, INVITADO)
  - `CentroEducativo` con anotaciones JPA completas, Javadoc, equals/hashCode optimizado, relación bidireccional @OneToMany, **validaciones @NotBlank/@NotNull**
  - `Arbol` con anotaciones JPA completas, Javadoc, equals/hashCode optimizado, **validaciones @NotBlank, @Past, @DecimalMin/@Max**
  - `DispositivoEsp32` con anotaciones JPA completas, Javadoc, equals/hashCode optimizado
- [x] **Repositorios JPA completados con queries derivadas**:
  - `UsuarioRepository` (findByEmail, existsByEmail, findByActivo, findByRol)
  - `CentroEducativoRepository` (findByNombre, existsByNombre, findByNombreContainingIgnoreCase, findByResponsable, findAllByOrderByNombreAsc)
  - `ArbolRepository` (findByEspecie, findByCentroEducativo, findByDispositivoEsp32, findByNombreContainingIgnoreCase, findAllByOrderByNombreAsc, existsByNombreAndCentroEducativo)
  - `DispositivoEsp32Repository` (findByMacAddress, existsByMacAddress, findByArbol)
- [x] **Relaciones bidireccionales implementadas**:
  - CentroEducativo ↔ Arbol (OneToMany/ManyToOne)
  - Arbol ↔ DispositivoEsp32 (OneToOne bidireccional)
- [x] **Aplicación Spring Boot arranca correctamente**
- [x] **Compilación exitosa con Maven**

### Completado (Fase 2 - Endpoints 1:N)
- [x] **Validaciones completas**:
  - @Valid en ArbolController y CentroEducativoController (POST y PUT)
  - @NotBlank/@NotNull en CentroEducativo
  - @JsonIgnore en List<Arbol> para evitar loops
- [x] **ArbolController completo**:
  - GET /api/arboles
  - GET /api/arboles/{id}
  - POST /api/arboles (con @Valid)
  - PUT /api/arboles/{id} (con @Valid)
  - DELETE /api/arboles/{id}
  - Endpoints adicionales (por centro, especie, búsqueda)
- [x] **CentroEducativoController completo**:
  - GET /api/centros
  - GET /api/centros/{id}
  - POST /api/centros (con @Valid)
  - PUT /api/centros/{id} (con @Valid)
  - DELETE /api/centros/{id}
  - GET /api/centros/{id}/arboles (relación 1:N)
- [x] **Testing Postman completo**:
  - CRUD de Árboles y Centros probado
  - Validaciones verificadas (400, 409)
  - Relación 1:N funcionando correctamente

### Próximos Hitos
- **Fase 3**: Frontend React - CRUD Árboles
- **Fase 4**: Android App
- **Fase 5**: Despliegue

## Archivos Importantes del Backend

### Scripts SQL
- [`create_database.sql`](./create_database.sql) - Script completo de creación de base de datos (8 tablas)
- [`drop_tables.sql`](./drop_tables.sql) - Script para eliminar todas las tablas (resetear BD)

### Configuración
- [`src/main/resources/README_CONFIG.md`](./src/main/resources/README_CONFIG.md) - **LEER PRIMERO**: Guía de configuración segura
- `src/main/resources/application.properties` - Configuración base (commiteada)
- `src/main/resources/application-local.properties` - Credenciales locales (NO commitear)

### Documentación Relacionada

- [Índice de Documentación](../docs/00.%20INDICE.md) - Índice completo de la documentación
- [Hoja de Ruta Completa](../docs/02.%20HOJA_DE_RUTA.md) - Planificación del proyecto
- [Especificación Técnica](../docs/03.%20ESPECIFICACION_TECNICA.md) - Requisitos y arquitectura
- [Modelo de Datos](../docs/04.%20MODELO_DATOS.md) - Diagramas E/R, UML y Relacional
- [Configuración PostgreSQL](../docs/04b.%20CONFIGURACION_POSTGRESQL.md) - Guía de instalación de BD
- [Testing Postman](../docs/TESTING_POSTMAN_RESULTS.md) - Resultados de pruebas de endpoints

## Contacto

Proyecto Final DAM 2025-2026
