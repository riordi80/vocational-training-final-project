# Manual de Usuario - Proyecto Árboles (Garden Monitor)

**Última actualización**: 2025-12-07

---

## Introducción

Bienvenido a **Garden Monitor** (Proyecto Árboles), un sistema de gestión y monitorización de árboles plantados en centros educativos.

### ¿Qué es Garden Monitor?

Garden Monitor es una plataforma que permite:
- **Gestionar centros educativos** y sus árboles plantados
- **Registrar información** de cada árbol (especie, fecha de plantación, ubicación)
- **Monitorizar datos ambientales** mediante sensores IoT (temperatura, humedad, CO2)
- **Consultar información** desde aplicaciones web y móvil
- **Hacer seguimiento educativo** del crecimiento de los árboles

### ¿Para quién es este sistema?

- **Profesores**: Gestionar y monitorizar árboles de su centro educativo
- **Estudiantes**: Consultar información de los árboles plantados
- **Administradores**: Gestión completa del sistema
- **Invitados**: Consulta de información pública

---

## Índice

1. [Acceso al Sistema](#1-acceso-al-sistema)
2. [Aplicación Web](#2-aplicación-web)
3. [Aplicación Android](#3-aplicación-android)
4. [Preguntas Frecuentes](#4-preguntas-frecuentes)

---

## 1. Acceso al Sistema

Garden Monitor está disponible en dos plataformas:

### Aplicación Web

- **URL**: https://vocational-training-final-project.vercel.app/
- **Dispositivos**: Ordenadores, tablets y móviles (responsive)
- **Navegadores**: Chrome, Firefox, Safari, Edge (últimas versiones)

### Aplicación Android

- **Plataforma**: Android 7.0 (API 24) o superior
- **Instalación**: APK o Google Play (según disponibilidad)
- **Detalles**: Ver [Manual de Usuario Android](./MANUAL_DE_USUARIO_ANDROID.md)

---

## 2. Aplicación Web

La aplicación web es responsive y se adapta automáticamente a ordenadores, tablets y móviles.

### 2.1 Registro e Inicio de Sesión

#### Registro de Nueva Cuenta

1. Accede a https://vocational-training-final-project.vercel.app/
2. Haz clic en **"Registrarse"**
3. Completa el formulario:
   - **Nombre completo**: Tu nombre y apellidos
   - **Email**: Dirección de correo válida
   - **Contraseña**: Mínimo 6 caracteres
   - **Confirmar contraseña**: Debe coincidir con la anterior
4. Haz clic en **"Registrarse"**
5. Serás redirigido automáticamente al inicio de sesión

**Validaciones**:
- El email debe tener formato válido (ejemplo@dominio.com)
- Las contraseñas deben coincidir
- Todos los campos son obligatorios

**IMPORTANTE - Sistema de Autenticación Mock**:
- La autenticación actual es un **sistema mock** (simulado) para desarrollo
- **Puedes usar CUALQUIER email y contraseña** para acceder
- No hay validación real contra base de datos de usuarios
- Los datos se guardan solo en localStorage del navegador
- Este sistema se reemplazará con autenticación real en futuras versiones

#### Iniciar Sesión

1. En la pantalla de login, ingresa:
   - **Email**: Cualquier email con formato válido
   - **Contraseña**: Cualquier contraseña (no se valida)
2. Haz clic en **"Iniciar Sesión"**
3. Serás redirigido al Dashboard

**Nota**: La sesión se mantiene activa incluso si cierras el navegador (localStorage).

#### Cerrar Sesión

1. Haz clic en tu nombre de usuario (esquina superior derecha)
2. Selecciona **"Cerrar Sesión"**

---

### 2.2 Dashboard (Pantalla Principal)

Al iniciar sesión, verás el Dashboard con:

- **Mensaje de bienvenida** personalizado con tu nombre
- **Tarjetas de acceso rápido**:
  - Gestión de Árboles
  - Gestión de Centros Educativos (si tienes permisos)
- **Menú de navegación** (barra superior o menú hamburguesa en móvil)

#### Navegación

**En ordenador**: Menú en la barra superior
- Inicio
- Árboles
- Centros (si tienes permisos)
- Perfil / Cerrar Sesión

**En móvil**: Menú hamburguesa (≡) en la esquina superior izquierda

---

### 2.3 Gestión de Árboles

La sección de árboles te permite visualizar, crear, editar y eliminar información de árboles.

#### Ver Lista de Árboles

1. Desde el Dashboard, haz clic en **"Árboles"** o en la tarjeta "Gestión de Árboles"
2. Verás una lista/tabla con todos los árboles registrados

**Información mostrada**:
- Nombre del árbol
- Especie (nombre científico)
- Fecha de plantación
- Centro educativo al que pertenece
- Ubicación

**Funcionalidades**:
- **Filtrar por centro**: Usa el desplegable en la parte superior para filtrar árboles de un centro específico
- **Ver detalles**: Haz clic en cualquier fila/tarjeta para ver información completa
- **Añadir árbol**: Botón verde "Añadir Árbol" en la esquina superior derecha

**Vista responsive**:
- **Ordenador**: Tabla con columnas
- **Móvil/Tablet**: Tarjetas apiladas verticalmente

#### Ver Detalles de un Árbol

1. En la lista de árboles, haz clic en el árbol que deseas ver
2. Verás dos secciones:

**Información General**:
- Nombre
- Especie
- Fecha de plantación
- Ubicación
- Centro educativo

**Umbrales de Monitorización** (si están configurados):
- Temperatura (min/max)
- Humedad del suelo (min/max)
- Humedad ambiental (min/max)
- Nivel de CO2 (máximo)

**Acciones disponibles**:
- **Volver**: Regresa a la lista
- **Editar**: Modifica la información (botón verde)
- **Eliminar**: Elimina el árbol (botón rojo, requiere confirmación)

#### Añadir un Nuevo Árbol

1. En la lista de árboles, haz clic en **"Añadir Árbol"**
2. Completa el formulario:

**Campos obligatorios** (marcados con *):
- **Nombre**: Nombre común del árbol
- **Especie**: Nombre científico (ej: Quercus robur)
- **Fecha de plantación**: No puede ser futura
- **Centro educativo**: Selecciona de la lista desplegable

**Campos opcionales**:
- **Ubicación**: Descripción de dónde está plantado

**Umbrales de monitorización** (opcionales):
- Temperatura mínima/máxima (°C)
- Humedad del suelo mínima/máxima (%)
- Humedad ambiental mínima/máxima (%)
- Nivel máximo de CO2 (ppm)

3. Haz clic en **"Guardar"**
4. Verás un mensaje de éxito y serás redirigido a la lista

**Validaciones**:
- La fecha de plantación no puede ser futura
- Todos los campos obligatorios deben completarse
- Los umbrales deben ser valores numéricos válidos
- La temperatura mínima debe ser menor que la máxima
- La humedad debe estar entre 0 y 100%

#### Editar un Árbol

1. En los detalles del árbol, haz clic en **"Editar"**
2. Modifica los campos que desees cambiar
3. Haz clic en **"Guardar"**
4. Verás un mensaje de confirmación

**Nota**: Puedes modificar cualquier campo excepto el ID del árbol.

#### Eliminar un Árbol

1. En los detalles del árbol, haz clic en **"Eliminar"** (botón rojo)
2. Aparecerá un modal de confirmación:
   - **Título**: "Confirmar eliminación"
   - **Mensaje**: "¿Estás seguro de que deseas eliminar este árbol? Esta acción no se puede deshacer."
3. Confirma haciendo clic en **"Eliminar"**
4. El árbol se eliminará permanentemente
5. Serás redirigido a la lista de árboles

**IMPORTANTE**: La eliminación es permanente y no se puede deshacer.

---

### 2.4 Gestión de Centros Educativos

*(Esta sección estará disponible según tus permisos de usuario)*

La gestión de centros permite administrar los centros educativos registrados en el sistema.

**Funcionalidades** (similares a la gestión de árboles):
- Ver lista de centros
- Ver detalles de un centro
- Crear nuevo centro
- Editar centro existente
- Eliminar centro
- Ver árboles asociados a cada centro

**Nota**: Solo usuarios con rol de Administrador o Profesor pueden gestionar centros.

---

### 2.5 Mensajes del Sistema

El sistema proporciona feedback visual constante:

#### Mensajes de Éxito (Verde)
- "Árbol creado correctamente"
- "Árbol actualizado correctamente"
- "Árbol eliminado correctamente"
- "Inicio de sesión exitoso"

#### Mensajes de Error (Rojo)
- "No se pudo conectar con el servidor. Si es la primera carga, puede estar iniciándose (30-60 seg). Recarga la página en unos momentos."
- "Por favor completa todos los campos obligatorios"
- "La fecha de plantación no puede ser futura"
- "Error de conexión con el servidor"

#### Estados de Carga
- **Spinner de carga**: Mientras se obtienen datos del servidor
- **Mensaje "Cargando..."**: Durante operaciones en proceso

---

### 2.6 Responsive Design

La aplicación web se adapta automáticamente al dispositivo:

#### Vista Ordenador (Desktop)
- Menú horizontal en la barra superior
- Tablas con múltiples columnas
- Formularios en formato de grid
- Botones con texto completo

#### Vista Tablet
- Menú adaptado
- Tablas compactas o tarjetas
- Formularios ajustados

#### Vista Móvil
- **Menú hamburguesa** (≡) plegable
- **Tarjetas verticales** en lugar de tablas
- **Formularios apilados** verticalmente
- **Botones grandes** fáciles de tocar

---

## 3. Aplicación Android

La aplicación móvil Android ofrece funcionalidades similares a la web, optimizadas para dispositivos móviles.

### Características Principales

- Login y registro de usuarios
- Lista de árboles del sistema
- Detalles completos de cada árbol
- Edición de información
- Eliminación de árboles
- Datos de sensores en tiempo real (simulados)

### Manual Completo

Para instrucciones detalladas de uso de la aplicación Android, consulta:
- **[Manual de Usuario Android](./MANUAL_DE_USUARIO_ANDROID.md)**

### Funcionalidades Específicas de Android

#### Datos de Sensores
La app Android muestra datos simulados de sensores:
- Temperatura ambiente (°C)
- Humedad ambiental (%)
- Humedad del suelo (%)
- Nivel de CO2 (ppm)

#### Modo Edición
- Botón "Editar" para activar edición
- Campos se convierten en editables
- Botones "Guardar" y "Cancelar"

#### Sistema de Fallback
Si no hay conexión al servidor:
- La app carga datos de ejemplo desde archivos XML locales
- Los datos de sensores se generan aleatoriamente
- Puedes seguir usando la app sin conexión

---

## 4. Preguntas Frecuentes (FAQ)

### ¿Puedo usar el sistema sin registrarme?

No, necesitas "iniciar sesión" para acceder al sistema. Sin embargo, dado que la autenticación es mock, puedes usar cualquier email y contraseña (solo necesitan tener formato válido).

### ¿Qué pasa si olvido mi contraseña?

Como la autenticación actual es mock (simulada), no hay contraseñas reales guardadas. Puedes iniciar sesión con cualquier email y cualquier contraseña. El sistema actual no valida credenciales contra una base de datos.

### ¿Por qué la autenticación es "mock"?

La autenticación mock es temporal para permitir el desarrollo y prueba de las funcionalidades principales del sistema sin necesidad de implementar un sistema completo de gestión de usuarios. En versiones futuras se implementará autenticación real con el backend.

### ¿Puedo acceder desde cualquier dispositivo?

Sí, la aplicación web funciona en cualquier dispositivo con navegador moderno (ordenador, tablet, móvil). La aplicación Android requiere Android 7.0 o superior.

### ¿Los cambios que hago en la web se reflejan en Android?

Sí, ambas aplicaciones comparten la misma base de datos. Los cambios se sincronizan en tiempo real.

### ¿Puedo eliminar un árbol por accidente?

No fácilmente. El sistema siempre pide confirmación antes de eliminar cualquier elemento. Lee el mensaje de confirmación cuidadosamente.

### ¿Puedo recuperar un árbol eliminado?

No, la eliminación es permanente. No hay papelera de reciclaje. Por eso el sistema siempre pide confirmación.

### ¿Los datos de sensores son reales?

En la versión actual, los datos de sensores son simulados para demostración. En futuras versiones se integrarán sensores reales ESP32.

### ¿Qué navegadores son compatibles?

La aplicación web funciona en:
- Google Chrome (recomendado)
- Mozilla Firefox
- Safari
- Microsoft Edge
- Cualquier navegador moderno con JavaScript habilitado

### ¿Necesito internet para usar la aplicación?

**Aplicación Web**: Sí, requiere conexión a internet constante.

**Aplicación Android**: Funciona con datos de fallback sin conexión, pero necesitas internet para sincronizar cambios.

### ¿Puedo ver árboles de otros centros educativos?

Sí, todos los usuarios pueden ver todos los árboles del sistema. Las restricciones por centro se implementarán en futuras versiones.

### ¿Cómo sé si un árbol está fuera de los umbrales normales?

Los umbrales de monitorización se establecen al crear/editar el árbol. En futuras versiones, el sistema generará alertas automáticas cuando los sensores detecten valores fuera de rango.

### ¿Puedo exportar los datos?

Esta funcionalidad no está disponible en la versión actual. Está planificada para futuras versiones.

### ¿Por qué aparece el mensaje de que el servidor está iniciándose?

**Mensaje completo**: "No se pudo conectar con el servidor. Si es la primera carga, puede estar iniciándose (30-60 seg). Recarga la página en unos momentos."

**Causa**: El backend está desplegado en Render (versión gratuita), que entra en suspensión tras 15 minutos de inactividad (cold start).

**Solución**:
1. Espera 30-60 segundos para que el backend se reactive
2. Recarga la página (F5 o Ctrl+R)
3. El sistema funcionará normalmente una vez activo

**Esto es normal y esperado en el free tier de Render**. No significa que haya un error.

### ¿Por qué la primera carga es tan lenta?

Si es la primera vez que accedes o han pasado más de 15 minutos desde el último acceso, el backend de Render necesita "despertar" desde suspensión. Este proceso tarda 30-60 segundos. Las siguientes cargas serán inmediatas mientras el backend permanezca activo.

### La lista de árboles no carga, ¿qué hago?

1. Verifica tu conexión a internet
2. Recarga la página (F5 o Ctrl+R)
3. Cierra sesión y vuelve a entrar
4. Limpia la caché del navegador
5. Si el problema persiste, contacta al administrador

### ¿Puedo añadir fotos a los árboles?

No en la versión actual. La funcionalidad de subida de imágenes está planificada para el futuro.

### ¿Hay límite de árboles que puedo registrar?

No hay límite establecido en el sistema.

### ¿Puedo cambiar mi email o contraseña?

Esta funcionalidad no está implementada porque la autenticación actual es mock. Puedes simplemente cerrar sesión e iniciar sesión con cualquier otro email si lo deseas.

---

## Consejos de Uso

### Para obtener la mejor experiencia:

1. **Usa un navegador moderno** actualizado a la última versión
2. **Mantén activa la sesión** - el sistema recuerda tu login
3. **Lee los mensajes de error** - proporcionan información útil
4. **Confirma antes de eliminar** - la eliminación es permanente
5. **Completa los campos opcionales** - mejora la información del sistema
6. **Configura umbrales realistas** - para futuras alertas automáticas
7. **Verifica los datos** antes de guardar cambios

### Seguridad:

**NOTA**: El sistema actual usa autenticación mock (simulada). Las siguientes recomendaciones aplicarán cuando se implemente autenticación real:

1. **No compartas tu contraseña** con otros usuarios
2. **Cierra sesión** en ordenadores compartidos
3. **Usa contraseñas seguras** (mínimo 8 caracteres, combina letras y números)

**Actualmente**: Como la autenticación es mock, cualquier usuario puede acceder con cualquier email/contraseña. No hay validación real de credenciales.

---

## Flujos de Trabajo Comunes

### Flujo 1: Registrar un Nuevo Árbol Plantado

1. Login en el sistema
2. Ir a "Árboles"
3. Clic en "Añadir Árbol"
4. Completar información básica (nombre, especie, fecha, centro)
5. Añadir ubicación específica
6. Configurar umbrales de monitorización (opcional)
7. Guardar
8. Verificar que aparece en la lista

### Flujo 2: Consultar Estado de un Árbol

1. Login en el sistema
2. Ir a "Árboles"
3. Filtrar por centro (si aplica)
4. Clic en el árbol deseado
5. Revisar información general
6. Revisar umbrales configurados
7. *(En Android: Ver datos actuales de sensores)*

### Flujo 3: Actualizar Información de un Árbol

1. Login en el sistema
2. Ir a "Árboles"
3. Localizar el árbol (usar filtros si necesario)
4. Clic en el árbol
5. Clic en "Editar"
6. Modificar los campos necesarios
7. Guardar cambios
8. Confirmar mensaje de éxito

---

## Glosario

- **Centro Educativo**: Institución donde se plantan y monitorizan árboles (IES, colegio, universidad)
- **Árbol**: Registro individual de un árbol plantado
- **Especie**: Nombre científico del árbol (ej: Quercus robur para el roble)
- **Umbrales**: Valores mínimos y máximos aceptables para parámetros ambientales
- **Sensor**: Dispositivo IoT (ESP32) que mide parámetros ambientales
- **Dashboard**: Pantalla principal después del login
- **CRUD**: Crear, Leer, Actualizar, Eliminar (operaciones básicas)

---

## Soporte y Contacto

Para soporte técnico o preguntas:

1. Consulta este manual y las FAQ
2. Revisa la sección de Troubleshooting del [Manual de Instalación](./MANUAL_DE_INSTALACION.md)
3. Contacta al administrador del sistema
4. Reporta bugs o solicita funcionalidades al equipo de desarrollo

---

## Actualizaciones y Mejoras Futuras

El sistema está en desarrollo activo. Próximas funcionalidades planificadas:

- Integración real con sensores ESP32
- Alertas automáticas cuando los valores superen umbrales
- Gráficas históricas de datos ambientales
- Sistema de notificaciones por email
- Gestión de perfiles de usuario
- Exportación de datos a CSV/Excel
- Subida de fotografías de árboles
- Mapa interactivo con ubicación de árboles
- Sistema de comentarios y observaciones
- Gestión de roles y permisos avanzados

---

## Información del Proyecto

**Nombre**: Garden Monitor - Sistema de Monitorización de Árboles

**Institución**: IES El Rincón

**Curso**: Desarrollo de Aplicaciones Multiplataforma (DAM) 2025-2026

**Repositorio**: [github.com/riordi80/vocational-training-final-project](https://github.com/riordi80/vocational-training-final-project)

**Última actualización**: 2025-12-08

### Colaboradores

[![riordi80](https://img.shields.io/badge/riordi80-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/riordi80) [![Enrique36247](https://img.shields.io/badge/Enrique36247-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/Enrique36247)

---

**Proyecto Final DAM 2025-2026** | Desarrollado con Spring Boot, React, Android y ESP32
