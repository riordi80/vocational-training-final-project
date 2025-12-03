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

- Java 21 ✅
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

- **[PGV] Noviembre**: ⏳ 2 endpoints con relación 1:N (CentroEducativo → Arbol) con GET, POST, PUT, DELETE y validaciones
- **[AED]**: ✅ Modelo de datos documentado | ✅ Mapeo ORM con JPA completado (4 entidades JPA + repositorios + relaciones bidireccionales)

## Estado del Proyecto

**Fase actual**: ⏳ Fase 2 EN CURSO (40% completada) - API REST 1:N

### ✅ Completado (Fase 0)
- ✅ Configuración de PostgreSQL + TimescaleDB
- ✅ Modelo de datos diseñado (8 entidades)
- ✅ Scripts SQL de creación (`create_database.sql`)
- ✅ Scripts SQL de eliminación (`drop_tables.sql`)
- ✅ Configuración de Spring Boot (`application.properties`)
- ✅ Estructura del proyecto establecida

### ✅ Completado (Fase 1 - Backend: Base de Datos y Modelo)
- ✅ **Entidades JPA completadas con Javadoc y equals/hashCode**:
  - `Usuario` con anotaciones JPA completas, Javadoc, equals/hashCode optimizado para JPA
  - `Rol` (enum: ADMIN, PROFESOR, ESTUDIANTE, INVITADO)
  - `CentroEducativo` con anotaciones JPA completas, Javadoc, equals/hashCode optimizado, relación bidireccional @OneToMany
  - `Arbol` con anotaciones JPA completas, Javadoc, equals/hashCode optimizado, **validaciones @NotBlank, @Past, @DecimalMin/@Max**
  - `DispositivoEsp32` con anotaciones JPA completas, Javadoc, equals/hashCode optimizado
- ✅ **Repositorios JPA completados con queries derivadas**:
  - `UsuarioRepository` (findByEmail, existsByEmail, findByActivo, findByRol)
  - `CentroEducativoRepository` (findByNombre, existsByNombre, findByNombreContainingIgnoreCase, findByResponsable, findAllByOrderByNombreAsc)
  - `ArbolRepository` (findByEspecie, findByCentroEducativo, findByDispositivoEsp32, findByNombreContainingIgnoreCase, findAllByOrderByNombreAsc, existsByNombreAndCentroEducativo)
  - `DispositivoEsp32Repository` (findByMacAddress, existsByMacAddress, findByArbol)
- ✅ **Relaciones bidireccionales implementadas**:
  - CentroEducativo ↔ Arbol (OneToMany/ManyToOne)
  - Arbol ↔ DispositivoEsp32 (OneToOne bidireccional)
- ✅ **Aplicación Spring Boot arranca correctamente**
- ✅ **Compilación exitosa con Maven**

### ⏳ En Curso (Fase 2 - Endpoints 1:N) - 40% completado
- ✅ **ArbolController completo**:
  - GET /api/arboles
  - GET /api/arboles/{id}
  - POST /api/arboles
  - PUT /api/arboles/{id}
  - DELETE /api/arboles/{id}
  - Endpoints adicionales (por centro, especie, búsqueda)
- ⏳ **Pendiente**:
  - CentroEducativoController (todos los endpoints)
  - @JsonIgnore en List<Arbol> para evitar loops
  - Validaciones @NotBlank/@NotNull en CentroEducativo
  - @Valid en controllers POST/PUT

### 📅 Próximos Hitos
- **Fase 2**: Completar CentroEducativoController y validaciones ← **SIGUIENTE**
- **Fase 3**: Frontend React - Estructura básica
- **Fase 4**: Frontend React - CRUD Árboles
- **Fase 5**: Android App

## Archivos Importantes del Backend

### Scripts SQL
- [`create_database.sql`](./create_database.sql) - Script completo de creación de base de datos (8 tablas)
- [`drop_tables.sql`](./drop_tables.sql) - Script para eliminar todas las tablas (resetear BD)

### Configuración
- [`src/main/resources/README_CONFIG.md`](./src/main/resources/README_CONFIG.md) - **LEER PRIMERO**: Guía de configuración segura
- `src/main/resources/application.properties` - Configuración base (commiteada)
- `src/main/resources/application-local.properties` - Credenciales locales (NO commitear)

### Documentación Relacionada

- [Hoja de Ruta Completa](../docs/02.%20HOJA_DE_RUTA.md)
- [Especificación Técnica](../docs/03.%20ESPECIFICACION_TECNICA.md)
- [Documentación Backend](../docs/Aplicaciones/BACKEND.md)
- [Modelo de Datos](../docs/04.%20MODELO_DATOS.md) - Diagramas E/R, UML y Relacional
- [Configuración PostgreSQL](../docs/04b.%20CONFIGURACION_POSTGRESQL.md)

## Contacto

Proyecto Final DAM 2025-2026
