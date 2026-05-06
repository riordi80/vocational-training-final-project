# Manual de Usuario - Proyecto Árboles (Frontend Web)

## Introducción

Este manual está actualizado al estado actual de la app web y pensado para usuarios nuevos.

- **Versión del manual**: 2026-05-05  
- **Plataforma**: Web responsive (desktop, tablet, móvil)
- **Navegadores**: Chrome, Firefox, Safari, Edge (últimas versiones)
- **Referencia técnica**: [Manual de Instalación](./MANUAL_DE_INSTALACION.md)

---

## Índice

1. [Antes de empezar](#1-antes-de-empezar)
2. [Inicio de sesión y registro](#2-inicio-de-sesión-y-registro)
3. [Navegación principal](#3-navegación-principal)
4. [Dashboard (Inicio)](#4-dashboard-inicio)
5. [Gestión de árboles](#5-gestión-de-árboles)
6. [Gestión de centros educativos](#6-gestión-de-centros-educativos)
7. [Gestión de dispositivos IoT](#7-gestión-de-dispositivos-iot)
8. [Histórico de lecturas](#8-histórico-de-lecturas)
9. [Gestión de usuarios (solo ADMIN)](#9-gestión-de-usuarios-solo-admin)
10. [Permisos por rol](#10-permisos-por-rol)
11. [Validaciones y mensajes frecuentes](#11-validaciones-y-mensajes-frecuentes)
12. [Resolución de problemas](#12-resolución-de-problemas)
13. [Soporte](#13-soporte)

---

## 1. Antes de empezar

Para usar la app web:

1. Accede a **https://vocational-training-final-project.vercel.app/**
2. Necesitas conexión a Internet
3. El backend puede tardar 30-60 seg en cold start (la primera vez o tras inactividad)

Si la página no carga, espera unos segundos y recarga (F5).

---

## 2. Inicio de sesión y registro

### 2.1 Pantalla de Login

![Captura: Formulario de login con email y contraseña](img/frontend/captura-login.png)

En la pantalla de **Login**:

- Introduce tu **email** registrado
- Introduce tu **contraseña**
- Pulsa **"Iniciar Sesión"**

Si las credenciales son correctas, accederás al Dashboard.

**Mensajes de error comunes**:

- `"Usuario o contraseña incorrecta"` → Verifica tus datos
- `"Usuario no activo"` → Contacta al administrador
- `"No se pudo conectar con el servidor..."` → El backend está en cold start, espera e intenta de nuevo

**Enlaces útiles**:

- **¿No tienes cuenta?** → Haz clic en **"Registrarse"**

### 2.2 Pantalla de Registro

![Captura: Formulario de registro con nombre, email y contraseña](img/frontend/captura-registro.png)

Para crear una **nueva cuenta**:

1. Haz clic en **"Registrarse"** desde la pantalla de login
2. Completa el formulario:
   - **Nombre completo**: Tu nombre y apellidos
   - **Email**: Correo válido y único (no puede estar registrado)
   - **Contraseña**: Contraseña segura
   - **Confirmar contraseña**: Debe coincidir con la anterior

3. Pulsa **"Registrarse"**
4. Si el registro es correcto, la app inicia sesión automáticamente

**Validaciones**:

- Email con formato válido (ejemplo@dominio.com)
- Contraseñas deben coincidir
- Email no debe estar registrado previamente
- Todos los campos obligatorios

**Mensajes típicos**:

- `"El email ya está registrado"` → Usa otro email o intenta login
- `"Las contraseñas no coinciden"` → Verifica que sean iguales
- `"Por favor completa todos los campos"` → Rellena campos vacíos

**Rol asignado**: Los nuevos usuarios se registran como **COORDINADOR** por defecto.

---

## 3. Navegación principal

### 3.1 Menú en Desktop

![Captura: Barra de navegación superior con links a Dashboard, Centros, Usuarios](img/frontend/captura-menu-desktop.png)

En desktop se ve una **barra de navegación horizontal** en la parte superior con:

- **Logo** (click va a Dashboard)
- **Dashboard** → Panel de inicio
- **Centros** → Gestión de centros educativos
- **Usuarios** → (solo visible si eres ADMIN)
- **Perfil del usuario** (esquina superior derecha)
  - Muestra tu nombre y rol
  - Opción **"Cerrar Sesión"**

### 3.2 Menú en Móvil/Tablet

![Captura: Menú hamburguesa desplegado en versión móvil](img/frontend/captura-menu-mobile.png)

En móvil/tablet se usa un **menú hamburguesa (≡)** que:

- Está en la esquina superior izquierda
- Al pulsar se expande mostrando las opciones
- Las opciones son las mismas que en desktop
- Pulsa de nuevo para cerrar

---

## 4. Dashboard (Inicio)

### 4.1 ¿Qué ves en el Dashboard?

![Captura: Dashboard con métricas (centros, árboles, dispositivos, CO2) y acceso rápido](img/frontend/captura-dashboard.png)

El Dashboard es tu **pantalla de inicio** y muestra:

**Zona de bienvenida**:
- Saludo personalizado con tu nombre
- Mensaje de estado según tu rol

**Métricas del sistema** (4 tarjetas de resumen):

| Métrica | Descripción |
|---------|-------------|
| **Centros Educativos** | Total de centros registrados en el sistema |
| **Árboles Plantados** | Cantidad acumulada de todos los árboles |
| **Dispositivos Activos** | Número de sensores IoT conectados y activos |
| **CO₂ Absorbidos/año** | Kilos estimados de CO₂ absorbidos anualmente |

**Acceso rápido**:
- Enlace/tarjeta a **"Centros Educativos"** para navegar directamente

### 4.2 Información según tu rol

- **COORDINADOR**: Ve datos de sus centros asignados
- **ADMIN**: Ve datos de todo el sistema

---

## 5. Gestión de árboles

Los árboles **no tienen una sección independiente** en el menú. Se gestionan **desde dentro de cada centro educativo**. Esto significa que los árboles pertenecen a un centro específico.

### 5.1 Acceso a los árboles de un centro

![Captura: Tabla de árboles dentro del detalle del centro](img/frontend/captura-centro-arboles-tabla.png)

Para **ver y gestionar los árboles**:

1. En el menú principal, haz clic en **"Centros"**
2. Selecciona un centro de la lista
3. En el detalle del centro, verás una **tabla de Árboles del Centro** con:

**Información mostrada**:
- **Nombre**: Identificador del árbol
- **Especie**: Nombre científico (ej: Quercus robur)
- **Cantidad**: Número de árboles de esa especie
- **Acciones**: Botones para ver detalle, editar o eliminar

**Acciones disponibles**:

- **Ver Detalle**: Haz clic en la fila o en el botón para ver toda la información
- **Añadir Árbol**: Botón para crear un nuevo árbol en este centro

### 5.2 Detalle de un árbol

![Captura: Página de detalle de árbol con toda la información](img/frontend/captura-arbol-detalle.png)

Al hacer clic en "Ver Detalle" ves:

**Información general**:
- Nombre completo del árbol
- Especie (nombre científico)
- Fecha de plantación
- Centro educativo al que pertenece
- Ubicación específica (si está registrada)
- Cantidad de árboles
- Absorción CO₂ anual

**Botones de acción**:
- **Volver**: Regresa al detalle del centro
- **Editar**: Abre el formulario de edición (si tienes permisos)
- **Eliminar**: Elimina el árbol con confirmación (si tienes permisos)

### 5.3 Crear un nuevo árbol

![Captura: Formulario de creación de árbol en el centro](img/frontend/captura-arbol-crear.png)

Para **crear un árbol en un centro**:

1. En el detalle del centro, en la tabla de Árboles, pulsa **"+ Añadir Árbol"**
2. Se abre un formulario con campos:

**Campos obligatorios** (marcados con *):
- **Nombre**: Identificador del árbol (ej: "Roble 1", "Pino del patio")
- **Especie**: Nombre científico (ej: "Quercus robur", "Pinus pinea")
- **Fecha de plantación**: Fecha pasada (selector de calendario)
- **Cantidad**: Número de árboles de esa especie
- **Centro educativo**: Preseleccionado con el centro actual

**Campos opcionales**:
- **Ubicación específica**: Descripción (ej: "Patio norte, junto a fuente")
- **Absorción CO₂ anual**: Si se conoce, en kg/año

**Validaciones**:
- Fecha de plantación no puede ser futura
- Cantidad debe ser número positivo (≥ 1)
- Campos obligatorios no pueden estar vacíos

**Mensaje de éxito**: `"Árbol creado correctamente"`

**Luego**: Serás redirigido al detalle del centro y verás el árbol en la tabla

### 5.4 Editar un árbol

![Captura: Formulario de edición de árbol con datos ya rellenos](img/frontend/captura-arbol-editar.png)

Para **editar un árbol**:

**Desde el detalle del árbol**

1. Abre el detalle del árbol
2. Pulsa **"Editar"**

3. Se abre el formulario con los datos actuales
4. Modifica los campos que necesites:
   - Nombre, especie, fecha, cantidad, ubicación
   - Umbrales de monitorización
5. Pulsa **"Guardar"**

**Campos editables**: Todos excepto el ID del árbol

**Mensaje de éxito**: `"Árbol actualizado correctamente"`

**Luego**: Vuelves a la vista anterior (tabla o detalle)

### 5.5 Eliminar un árbol

![Captura: Modal de confirmación antes de eliminar árbol](img/frontend/captura-arbol-eliminar.png)

Para **eliminar un árbol**:

**Desde el detalle del árbol**:
1. En la página de detalle, pulsa **"Eliminar"** (botón rojo)
2. Aparece un modal pidiendo confirmación

**En el modal de confirmación**:
- Se muestra el nombre del árbol
- Advierte que la acción es **irreversible**
- Botón **"Cancelar"** → No elimina nada
- Botón **"Eliminar"** → Confirma la eliminación

3. Pulsa **"Eliminar"** para confirmar
4. El árbol se elimina permanentemente
5. Se actualiza la tabla del centro

**⚠️ IMPORTANTE**: La eliminación es **permanente** y **no se puede deshacer**. El árbol desaparecerá de la tabla inmediatamente.

---

---

## 6. Gestión de centros educativos

### 6.1 Listado de centros

![Captura: Lista de centros con nombre, ubicación e información](img/frontend/captura-centros-lista.png)

En **Centros** ves una tabla/listado de todos los centros:

**Información mostrada**:
- Nombre del centro
- Isla
- Dirección
- Responsable
- Fecha de creación

**Acciones**:
- **Ver detalles**: Haz clic en un centro para ver información completa
- **Añadir Centro**: Botón para crear nuevo centro (solo si eres ADMIN)

### 6.2 Detalle de un centro

![Captura: Detalle de centro con información, árboles y dispositivos en pestañas](img/frontend/captura-centro-detalle.png)

Al hacer clic en un centro ves:

**Botones de acción** (según permisos):

- **Volver**: Regresa al listado de centros
- **Editar**: Modifica datos del centro
- **Eliminar**: Elimina el centro
- **Árbol**: Crear nuevo árbol en este centro
- **Dispositivo**: Registrar nuevo dispositivo ESP32

**Información del centro**:
- Nombre completo
- Responsable
- Teléfono
- Correo electrónico
- Fecha de creación
- Dirección
- Población
- Código postal
- Provincia

**Mapa**:
- Visualización en mapa de la ubicación del centro

**Tabla de Dispositivos IoT**:
- Lista de sensores ESP32 conectados
- Columnas: MAC, Estado, Frecuencia, Última conexión
- Botón para ver histórico de lecturas
- Opción **"+ Añadir Dispositivo"** en este centro

**Tabla de Árboles del Centro**:

- Lista de todos los árboles plantados
- Columnas: Nombre, Especie, Ubicación y Cantidad
- Botón para ver detalles de cada árbol
- Opción **"+ Añadir Árbol"** en este centro

### 6.3 Crear un nuevo centro

![Captura: Formulario de creación de centro](img/frontend/captura-centro-crear.png)

Para **crear un centro** (solo ADMIN):

1. En el listado de centros, haz clic en **"+ Añadir Centro"**
2. Completa el formulario:

**Campos**:
- **Nombre**: Nombre oficial del centro educativo
- **Dirección**: Dirección completa
- **Responsable**: Nombre del coordinador

- **Isla**: Primaria, Secundaria, etc.
- **Población**
- **Provincia**
- **Código Postal**
- **Teléfono**: Contacto
- **Email**: Correo de contacto
- **Latitud/Longitud**: Ubicación del centro en Google Maps

3. Pulsa **"Guardar"**

**Mensaje de éxito**: `"Centro creado correctamente"`

### 6.4 Editar centro

![Captura: Formulario de edición de centro con datos rellenos](img/frontend/captura-centro-editar.png)

Para **editar un centro** (solo ADMIN y COORDINADOR del centro):

1. En el detalle, pulsa **"Editar"**
2. Modifica los campos necesarios
3. Pulsa **"Actualizar Centro"**

**Mensaje de éxito**: `"Centro actualizado correctamente"`

### 6.5 Eliminar centro

![Captura: Modal de confirmación antes de eliminar centro](img/frontend/captura-centro-eliminar.png)

Para **eliminar un centro** (solo ADMIN):

1. En el detalle, pulsa **"Eliminar"** (botón rojo)
2. Confirma en el modal que aparece
3. El centro se elimina permanentemente

**⚠️ IMPORTANTE**: Se eliminarán también todos los árboles y dispositivos del centro.

---

## 7. Gestión de dispositivos IoT

### 7.1 Acceso a dispositivos

Los dispositivos IoT ESP32 se gestionan desde el **detalle del centro**.

![Captura: Tabla de dispositivos en el detalle del centro](img/frontend/captura-dispositivos-tabla.png)

En la tabla de dispositivos ves:

**Columnas**:
- **MAC**: Dirección única del dispositivo (XX:XX:XX:XX:XX:XX)
- **Estado**: Activo / Inactivo
- **Frecuencia**: Segundos entre lecturas
- **Última sincronización**: Fecha y hora

**Acciones**:
- **Histórico**: Abre la gráfica de lecturas
- **Editar**: Modifica configuración del dispositivo
- **Eliminar**: Elimina el dispositivo del sistema

### 7.2 Crear nuevo dispositivo

![Captura: Formulario de creación de dispositivo con campos MAC, frecuencia y umbrales](img/frontend/captura-dispositivo-crear.png)

Para **añadir un dispositivo** en el centro:

1. En el detalle del centro, pulsa **"+ Añadir Dispositivo"**
2. Completa el formulario:

**Campos**:

- **Dirección MAC**: Identificador único del ESP32
  - Formato: `XX:XX:XX:XX:XX:XX` (ejemplo: `AA:BB:CC:DD:EE:FF`)
- **Centro**: Se preselecciona el actual
- **Frecuencia de lectura**: Segundos entre lecturas
  - Recomendado: 30-60 segundos
- **Estado**: Activo/Inactivo (checkbox)

- **Umbrales de monitorización**:
  - Temperatura mín/máx (°C)
  - Humedad suelo min (%)
  - Humedad ambiental mín/máx (%)
  - CO₂ máximo (ppm)

3. Pulsa **"Registrar Dispositivo"**

**Validaciones**:
- MAC con formato válido `XX:XX:XX:XX:XX:XX`
- Frecuencia número positivo
- Umbrales valores numéricos coherentes

**Mensaje de éxito**: `"Dispositivo registrado correctamente"`

### 7.3 Editar dispositivo

![Captura: Formulario de edición de dispositivo](img/frontend/captura-dispositivo-editar.png)

Para **editar un dispositivo**:

1. En la tabla, pulsa el botón **"Editar"**
2. Modifica los parámetros
3. Pulsa **"Actualizar Dispositivo"**

### 7.4 Eliminar dispositivo

![Captura: Página de histórico con gráficos de temperatura, humedad, CO2](img/frontend/captura-eliminar-dispositivo.png)

Para **eliminar un dispositivo**:

1. En la tabla, pulsa el botón **"Eliminar"**
2. Confirma en el modal que aparece
3. El dispositivo se elimina y deja de enviar lecturas

---

## 8. Histórico de lecturas

### 8.1 Ver gráficas de sensores

![Captura: Página de histórico con gráficos de temperatura, humedad, CO2](img/frontend/captura-historico-graficos.png)

Para **ver el histórico de un dispositivo**:

1. En el detalle del centro, en la tabla de dispositivos, pulsa **"Ver histórico"**
2. Se abre la página de **"Histórico de Lecturas"** mostrando gráficas:

**Gráficas superior**:

- **Temperatura ambiente** (°C)
- **Humedad ambiental** (%)
- **Humedad del suelo** (%)
- **Nivel de luz (2 sensores)**

**Gráficas inferior**:

- **Nivel de CO₂** (ppm)

**Tabla de datos**:

- **Temperatura ambiente** (°C)
- **Humedad ambiental** (%)
- **Humedad del suelo** (%)
- **Nivel de luz (2 sensores)**
- **Nivel de CO₂** (ppm)

**Funcionalidades**:
- Cada gráfica muestra datos en tiempo real
- Eje X: Fecha y hora
- Eje Y: Valor del sensor
- Líneas de color para cada métrica

### 8.2 Filtros y controles

![Captura: Filtros de fecha y opciones de descarga](img/frontend/captura-historico-filtros.png)

Con los filtros del histórico puedes ajustar el rango de fechas para mostrar en las gráficas y en la tabla de datos


- **Volver**: Regresa al detalle del centro

---

## 9. Gestión de usuarios (solo ADMIN)

Solo los administradores pueden gestionar usuarios del sistema.

### 9.1 Listado de usuarios

![Captura: Lista de usuarios con nombre, email, rol y centros asignados](img/frontend/captura-usuarios-lista.png)

En **Usuarios** (solo visible para ADMIN) ves una tabla con:

**Columnas**:
- Nombre del usuario
- Email
- Rol (ADMIN, COORDINADOR)
- Estado (Activo/Inactivo)

**Acciones**:
- **Ver detalles**: Haz clic para abrir información completa
- **+ Añadir Usuario**: Botón para crear nuevo usuario

### 9.2 Detalle de un usuario

![Captura: Pantalla de detalle de usuario con información y centros asignados](img/frontend/captura-usuario-detalle.png)

Al hacer clic en un usuario ves:

**Información personal**:
- Nombre completo
- Email
- Rol actual (ADMIN / COORDINADOR)
- Fecha de creación
- Estado (Activo / Inactivo)

**Centros asignados** (si es COORDINADOR):

Si estás editando un coordinador se muestra un listado con los centros para asignar

![Captura: Pantalla de detalle de usuario con información y centros asignados](img/frontend/captura-coordinador-detalle.png)

- Tabla con los centros donde puede gestionar
- Botón **"Desasignar"** para cada centro
- Desplegable Asignar para añadir nuevos centros asociados al usuario

**Botones de acción**:
- **Volver**: Regresa al listado
- **Editar**: Abre formulario de edición
- **Eliminar**: Elimina el usuario del sistema
- **Activar/Desactivar**: Habilita/deshabilita la cuenta

### 9.3 Crear nuevo usuario

![Captura: Formulario de creación de usuario](img/frontend/captura-usuario-crear.png)

Para **crear un usuario** (solo ADMIN):

1. En el listado de usuarios, pulsa **"+ Añadir Usuario"**
2. Completa el formulario:

**Campos**:

- **Nombre completo**: Nombre y apellidos
- **Email**: Correo único (no puede estar registrado)
- **Contraseña**: Contraseña inicial segura
- **Rol**: Selecciona ADMIN o COORDINADOR

**Si es COORDINADOR**:
- **Centros asignados**: Selecciona los centros que gestionará
  - Puede ser más de uno

3. Pulsa **"Crear Usuario"**

**Validaciones**:
- Email formato válido y no duplicado
- Todos los campos obligatorios rellenos
- Si es COORDINADOR, debe tener al menos un centro

**Mensaje de éxito**: `"Usuario creado correctamente"`

### 9.4 Editar usuario

![Captura: Formulario de edición de usuario](img/frontend/captura-usuario-editar.png)

Para **editar un usuario**:

1. En el detalle, pulsa **"Editar"**
2. Modifica:
   - Nombre
   - Email
   - Rol
   - Contraseña
   - Centros asignados (para el rol COORDINADOR)
   
3. Pulsa **"Guardar"**

**Cambiar rol**:
- Si cambias COORDINADOR → ADMIN: obtiene permisos totales
- Si cambias ADMIN → COORDINADOR: debes asignar centros específicos

### 9.5 Asignar/Desasignar centros

Para **asignar un centro** a un COORDINADOR:

1. En el detalle del usuario, en la sección "Centros asignados"
2. En el desplegable selecciona el centro
3. Pulsa **"Asignar"**
4. Pulsa **"Actualizar Usuario"**

Para **desasignar un centro**:

1. En la tabla de centros asignados
2. Pulsa **"Desaignar"** en el centro deseado
3. Pulsa **"Actualizar Usuario"**

### 9.6 Desactivar usuario

Para **desactivar una cuenta** (reversible):

1. En el detalle del usuario, pulsa el toggle **"Activo"** para cambiar el estado
2. La cuenta permanecerá pero no podrá iniciar sesión
3. Para reactivar, pulsa el toggle **"Activo"** para cambiar el estado

### 9.7 Eliminar usuario

![Captura: Modal de confirmación antes de eliminar usuario](img/frontend/captura-usuario-eliminar.png)

Para **eliminar un usuario** (permanente):

1. En el detalle, pulsa **"Eliminar"** (botón rojo)
2. Confirma en el modal
3. El usuario se elimina permanentemente del sistema

**⚠️ IMPORTANTE**: La eliminación es permanente y no se puede deshacer.

---

## 10. Permisos por rol

### 10.1 Rol COORDINADOR

**Permisos**:
- Ver Dashboard (con datos de sus centros)
- Ver todos los árboles
- Crear/Editar/Eliminar árboles en sus centros asignados
- Ver todos los centros
- Editar centros asignados
- Ver dispositivos IoT de sus centros
- Crear/Editar/Eliminar dispositivos en sus centros
- Ver histórico de lecturas
- No puede gestionar usuarios
- No puede crear/editar centros nuevos
- No puede ver la sección "Usuarios"

**Restricciones**:
- Solo puede gestionar centros asignados por el admin
- No puede cambiar su propio rol
- No puede crear nuevos centros

### 10.2 Rol ADMIN

**Permisos**:
- Acceso total a todas las funciones
- Ver Dashboard (datos de todo el sistema)
- Crear/Editar/Eliminar árboles en cualquier centro
- Crear/Editar/Eliminar centros
- Crear/Editar/Eliminar dispositivos en cualquier centro
- Gestionar usuarios:
  - Crear usuarios
  - Editar usuarios
  - Cambiar roles
  - Asignar/desasignar centros a coordinadores
  - Activar/desactivar cuentas
  - Eliminar usuarios

**Responsabilidades**:
- Mantener la integridad de datos
- Gestionar accesos de otros usuarios
- Administrar dispositivos IoT

---

## 11. Validaciones y mensajes frecuentes

### 11.1 Mensajes de éxito (Verde)

```
✓ "Árbol creado correctamente"
✓ "Árbol actualizado correctamente"
✓ "Árbol eliminado correctamente"
✓ "Centro creado correctamente"
✓ "Centro actualizado correctamente"
✓ "Dispositivo registrado correctamente"
✓ "Usuario creado correctamente"
✓ "Operación completada"
```

### 11.2 Mensajes de error (Rojo)

```
✗ "Por favor completa todos los campos obligatorios"
✗ "La fecha de plantación no puede ser futura"
✗ "El email ya está registrado"
✗ "Email o contraseña incorrecta"
✗ "Usuario no activo"
✗ "No se pudo conectar con el servidor..."
✗ "Error de conexión. Intenta de nuevo"
```

### 11.3 Validaciones comunes

**Campos de fecha**:
- No pueden ser fechas futuras
- Formato: DD/MM/YYYY

**Email**:
- Debe tener formato válido (ejemplo@dominio.com)
- No puede estar registrado previamente

**Dirección MAC**:
- Formato exacto: `XX:XX:XX:XX:XX:XX`
- Donde X son dígitos hexadecimales (0-9, A-F)

**Números positivos** (cantidad, frecuencia, etc.):
- Deben ser mayor que 0
- No se aceptan negativos

**Contraseña**:
- Las dos contraseñas deben coincidir en registro

---

## 12. Resolución de problemas

### 12.1 "No se pudo conectar con el servidor"

**Causas posibles**:
1. No tienes conexión a Internet
2. El backend está en cold start (inactividad prolongada)
3. El servidor está en mantenimiento

**Solución**:
1. Verifica tu conexión a Internet
2. Espera 30-60 segundos
3. Recarga la página (F5 o Ctrl+R)
4. Intenta de nuevo

### 12.2 Página en blanco o no carga

**Causas posibles**:
1. Caché del navegador corrupto
2. JavaScript deshabilitado

**Solución**:
1. Limpia la caché (Ctrl+Shift+Supr)
2. Verifica que JavaScript esté habilitado
3. Prueba otro navegador
4. Recarga la página (Ctrl+F5)

### 12.3 Cambios no aparecen

**Causas posibles**:
1. La página no se ha recargado después de guardar
2. Caché de navegador

**Solución**:
1. Recarga la página (F5)
2. Limpia caché (Ctrl+Shift+Supr)
3. Intenta de nuevo

### 12.4 Sesión cerrada inesperadamente

**Causas posibles**:
1. Inactividad prolongada
2. Token de sesión expirado
3. Cambios en contraseña

**Solución**:
1. Inicia sesión de nuevo
2. Verifica tu contraseña
3. Intenta después de unos minutos

### 12.5 No puedo crear/editar/eliminar

**Causas posibles**:
1. No tienes permisos suficientes
2. El elemento está asociado a otro

**Solución**:
1. Verifica tu rol (Dashboard → Perfil)
2. Contacta al administrador si necesitas más permisos
3. Comprueba si no hay dependencias

---

## 13. Responsive Design

### 13.1 Desktop

- Menú horizontal en barra superior
- Tablas con todas las columnas
- Formularios en grid (múltiples columnas)
- Textos completos en botones

### 13.2 Tablet

- Menú adaptado
- Tablas compactas
- Formularios ajustados

### 13.3 Móvil

- Menú hamburguesa (≡) plegable
- Tarjetas verticales en lugar de tablas
- Formularios apilados verticalmente
- Botones grandes
- Espaciado optimizado para dedos

**Prueba en tu dispositivo**: La app se adapta automáticamente al tamaño de pantalla.

---

## 14. Soporte

Si tienes problemas:

1. **Verifica tu conexión a Internet**
2. **Limpia caché**: Ctrl+Shift+Supr
3. **Recarga la página**: F5 o Ctrl+F5
4. **Prueba otro navegador**
5. **Contacta al administrador** con detalles del error:
   - Navegador y versión
   - Dispositivo (desktop/móvil)
   - Qué intentabas hacer
   - Mensaje de error exacto

---


**¿Más ayuda?** Contacta con el administrador o consulta el [Manual de Instalación](./MANUAL_DE_INSTALACION.md).

