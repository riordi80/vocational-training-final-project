# Configuración de Propiedades

## Archivos de configuración

Este proyecto usa múltiples archivos de properties para gestionar la configuración:

### 1. `application.properties` ✅ SE COMMITEA
- Configuración base compartida por todos
- Valores de ejemplo/placeholder (sin credenciales reales)
- Este archivo SÍ se sube a Git

### 2. `application-local.properties` ❌ NO SE COMMITEA
- Configuración local de desarrollo
- **Contiene tus credenciales reales**
- Este archivo NO se sube a Git (está en `.gitignore`)
- **Debes crearlo en tu máquina local**

### 3. `application-dev.properties` ❌ NO SE COMMITEA (futuro)
- Configuración para entorno de desarrollo compartido
- Para cuando tengas un servidor de desarrollo

### 4. `application-prod.properties` ❌ NO SE COMMITEA (futuro)
- Configuración para producción
- Usará variables de entorno

## ¿Cómo funciona?

Spring Boot carga los archivos en este orden (el último sobrescribe al anterior):

1. `application.properties` (base)
2. `application-local.properties` (sobrescribe valores locales)

Por lo tanto:
- La contraseña en `application.properties` es `your_password_here` (placeholder)
- La contraseña en `application-local.properties` es tu contraseña real
- Spring Boot usa la de `application-local.properties` ✅

## Configuración inicial

**Edita** `application-local.properties` y cambia:

```properties
spring.datasource.password=TU_PASSWORD_REAL_AQUI
```

Por tu contraseña real de PostgreSQL.

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

## Activar perfil específico

Si quieres usar otros perfiles (dev, prod), ejecuta:

```bash
# Perfil dev
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Perfil prod
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

## Variables de entorno (producción)

En producción, es mejor usar variables de entorno:

```bash
export DB_PASSWORD="mi_password_seguro"
```

Y en `application-prod.properties`:
```properties
spring.datasource.password=${DB_PASSWORD}
```
