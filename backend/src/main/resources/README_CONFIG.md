# Configuración de Propiedades

## Archivos de configuración

Este proyecto usa **Spring Profiles** para gestionar diferentes entornos:

### 1. `application.properties` - SE COMMITEA
- Configuración base compartida por todos
- Define el perfil activo: `spring.profiles.active=local`
- **NO contiene credenciales** (solo configuración genérica)
- Este archivo SÍ se sube a Git

### 2. `application-local.properties` - NO SE COMMITEA
- Configuración para desarrollo local
- **Contiene tus credenciales reales de PostgreSQL local**
- Este archivo NO se sube a Git (está en `.gitignore`)
- **Debes crearlo manualmente en tu máquina**

### 3. `application-prod.properties` - SE COMMITEA
- Configuración para producción (Render)
- **NO contiene credenciales hardcoded** (usa variables de entorno)
- Este archivo SÍ se sube a Git
- Render configura las variables de entorno automáticamente

## ¿Cómo funciona?

Spring Boot usa el sistema de **perfiles**:

**En desarrollo local** (perfil `local`):
1. `application.properties` se carga primero (configuración base)
2. `application-local.properties` se carga después y sobrescribe lo necesario
3. Spring Boot usa las credenciales de `application-local.properties`

**En producción** (perfil `prod`):
1. `application.properties` se carga primero
2. `application-prod.properties` se carga después
3. Las credenciales se leen desde variables de entorno de Render

## Configuración inicial (Desarrollo Local)

**Crear el archivo** `src/main/resources/application-local.properties`:

```properties
# Configuración de Base de Datos LOCAL
spring.datasource.url=jdbc:postgresql://localhost:5432/proyecto_arboles
spring.datasource.username=arboles_user
spring.datasource.password=TU_PASSWORD_REAL_AQUI

# Configuración JPA (opcional)
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

**IMPORTANTE**: Reemplaza `TU_PASSWORD_REAL_AQUI` con tu contraseña real de PostgreSQL.

## Verificar que está en .gitignore

Ejecuta en terminal:
```bash
git status
```

NO debe aparecer `application-local.properties` en la lista de archivos.

Si aparece, añádelo a `.gitignore`:
```bash
echo "**/application-local.properties" >> .gitignore
```

## Cambiar entre perfiles manualmente

Por defecto, el proyecto usa el perfil `local`. Si quieres usar el perfil de producción localmente (para testing):

```bash
# Usar perfil prod (requiere variables de entorno configuradas)
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

**NOTA**: En producción (Render), el perfil `prod` se activa automáticamente mediante la variable de entorno `SPRING_PROFILES_ACTIVE=prod`.

## Variables de entorno (Producción en Render)

En Render, las credenciales se configuran mediante variables de entorno:

- `SPRING_DATASOURCE_URL`: URL JDBC de PostgreSQL
- `SPRING_DATASOURCE_USERNAME`: Usuario de la base de datos
- `SPRING_DATASOURCE_PASSWORD`: Contraseña de la base de datos
- `SPRING_PROFILES_ACTIVE=prod`: Activa el perfil de producción

Estas variables se leen automáticamente en `application-prod.properties`:

```properties
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
```
