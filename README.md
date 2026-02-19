# Garden Monitor - Sistema de Monitorización de Árboles

Sistema de monitorización y gestión de árboles plantados en centros educativos mediante sensores IoT.

## Tecnologías

![Java](https://img.shields.io/badge/Java_21-ED8B00?style=flat&logo=openjdk&logoColor=white) ![Spring Boot](https://img.shields.io/badge/Spring_Boot_3-6DB33F?style=flat&logo=spring-boot&logoColor=white) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL_16-316192?style=flat&logo=postgresql&logoColor=white) ![TimescaleDB](https://img.shields.io/badge/TimescaleDB-FDB515?style=flat&logo=timescale&logoColor=black) ![React](https://img.shields.io/badge/React_19-20232A?style=flat&logo=react&logoColor=61DAFB) ![Vite](https://img.shields.io/badge/Vite-646CFF?style=flat&logo=vite&logoColor=white) ![TailwindCSS](https://img.shields.io/badge/Tailwind_CSS-38B2AC?style=flat&logo=tailwind-css&logoColor=white) ![Android](https://img.shields.io/badge/Android-3DDC84?style=flat&logo=android&logoColor=white) ![Vercel](https://img.shields.io/badge/Vercel-000000?style=flat&logo=vercel&logoColor=white) ![Render](https://img.shields.io/badge/Render-46E3B7?style=flat&logo=render&logoColor=white)

## Aplicaciones Desplegadas

- **Frontend (React)**: [https://vocational-training-final-project.vercel.app/](https://vocational-training-final-project.vercel.app/)
- **Backend (Spring Boot)**: [https://proyecto-arboles-backend.onrender.com](https://proyecto-arboles-backend.onrender.com)
- **Base de Datos**: PostgreSQL 16 en Render

## Descripción

Este proyecto permite recopilar datos ambientales (temperatura, humedad del suelo, pH, nivel de agua) a través de dispositivos ESP32 con sensores, y visualizarlos en tiempo real mediante aplicaciones web y móvil. El sistema está diseñado para centros educativos que desean monitorizar el crecimiento y estado de sus árboles.

## Componentes del Proyecto

Este es un **monorepo** que contiene todos los componentes del sistema:

### `/backend`
API REST con **Spring Boot (Java)**
- CRUD de centros educativos y árboles
- Relaciones 1:N con validaciones
- PostgreSQL + TimescaleDB

### `/frontend`
Aplicación web con **React**
- Login/Register con persistencia (localStorage)
- Dashboard + CRUD Árboles completo (listar, crear, editar, eliminar, detalle)
- React Router + navegación dinámica
- Responsive con menú hamburguesa (Tailwind CSS)
- Sistema de roles (ADMIN / COORDINADOR, autenticación real contra BD)
- Componentes reutilizables (Button, Input, Alert, Spinner)
- Feedback usuario (mensajes éxito/error, validaciones)
- Visualización de lecturas IoT con gráficas (Recharts) y mapas (Leaflet)
- Configurado para despliegue en Vercel

### `/android`
Aplicación móvil con **Android (Java)**
- 4 Activities (Login, Register, Listar, Detalles)
- RecyclerView con listado completo de árboles
- Visualización de detalles con datos de sensores
- Modo edición para modificar árboles
- Eliminar árboles con confirmación
- Conectado a backend en Render

### `/esp32`
Firmware **ESP32 (C/C++)**
- Implementado: lectura de DHT22 (temperatura/humedad) y sensor de humedad de suelo
- Envío periódico al backend vía HTTP REST

### `/docs`
Documentación completa del proyecto
- Especificación técnica
- Diagramas (E/R, UML, Relacional)
- Manuales de instalación y usuario

## Tecnologías Utilizadas

| Componente | Tecnología | Versión |
|------------|-----------|---------|
| Backend | Spring Boot (Java) | 3.5.7 / Java 21 |
| Frontend | React | 19.2.0 |
| Frontend | Recharts | 3.x |
| Frontend | Leaflet | 1.9.x |
| Mobile | Android (Java) | SDK 24+ |
| ESP32 | C/C++ (Arduino/PlatformIO) | - |
| Base de Datos | PostgreSQL + TimescaleDB | 16+ |
| Control de Versiones | Git / GitHub | - |

## Estructura del Repositorio

```
proyecto-arboles/
├── docs/                                 # Documentación
│   ├── 00. INDICE.md                     # Índice de toda la documentación
│   ├── 01. GIT_WORKFLOW.md               # Flujo de trabajo Git (feature branches)
│   ├── 02. HOJA_DE_RUTA.md               # Planificación por fases
│   ├── 03. ESPECIFICACION_TECNICA.md     # Arquitectura y requisitos
│   ├── 04. MODELO_DATOS.md               # Diagramas E/R, UML, Relacional
│   ├── 04b. CONFIGURACION_POSTGRESQL.md  # Instalación PostgreSQL
│   ├── REQUISITOS.md                     # Requisitos académicos por módulo
│   ├── TESTING_POSTMAN_RESULTS.md        # Resultados testing API REST
│   ├── MANUAL_DE_INSTALACION.md          # Manual de instalación completo
│   ├── MANUAL_DE_USUARIO.md              # Manual de usuario (Web + móvil)
│   ├── MANUAL_DE_INSTALACION_ANDROID.md  # Manual instalación Android
│   ├── MANUAL_DE_USUARIO_ANDROID.md      # Manual de usuario Android
│   ├── Componentes para ESP32/           # Especificaciones hardware
│   ├── install-timescaledb.sh            # Script instalación TimescaleDB
├── backend/                              # API REST (Spring Boot)
│   ├── src/
│   │   └── main/
│   │       └── resources/
│   │           ├── application.properties          # Configuración base
│   │           └── README_CONFIG.md                # Guía de configuración
│   ├── create_database.sql               # Script creación de BD
│   ├── drop_tables.sql                   # Script eliminación de tablas
│   ├── pom.xml
│   └── README.md
├── frontend/                              # Web App (React)
│   └── README.md
├── android/                               # Mobile App (Android)
│   └── README.md
├── esp32/                                 # Firmware ESP32
│   └── README.md
├── .gitignore
└── README.md
```

## Instalación y Uso

### Requisitos Previos

- **Java 21** (Backend)
- **Node.js 18+** (Frontend)
- **PostgreSQL 16+** con TimescaleDB (Base de datos)
- **Android Studio** (Opcional - para app móvil)

### Guías de Instalación

Para instalar el sistema completo, consulta:
- **[Manual de Instalación](./docs/MANUAL_DE_INSTALACION.md)** - Guía paso a paso para instalar todos los componentes

Para usar el sistema ya instalado, consulta:
- **[Manual de Usuario](./docs/MANUAL_DE_USUARIO.md)** - Guía de uso de las aplicaciones web y móvil

### README por Componente

Cada componente tiene documentación técnica detallada:
- [Backend README](./backend/README.md) - Configuración y desarrollo del API REST
- [Frontend README](./frontend/README.md) - Configuración y desarrollo de la app web
- [Android README](./android/README.md) - Configuración y desarrollo de la app móvil

## Estado Actual

- [x] Modelo de datos (E/R, UML, Relacional)
- [x] PostgreSQL 16 + TimescaleDB 2.23.1
- [x] 8 Entidades JPA con validaciones completas
- [x] Repositorios JPA con queries derivadas
- [x] ArbolController (GET, POST, PUT, DELETE con @Valid)
- [x] CentroEducativoController (GET, POST, PUT, DELETE con @Valid)
- [x] Relación 1:N implementada (GET /api/centros/{id}/arboles)
- [x] API REST con relaciones 1:N (Fase 2 - 100% completada)
- [x] Testing Postman completo (CRUD + validaciones)
- [x] Frontend React - Estructura completa (Fase 3 - 100% completada)
- [x] AuthContext + Login/Register con localStorage
- [x] Header responsive con menú hamburguesa, MainLayout, ProtectedRoute
- [x] Dashboard con navegación
- [x] Componentes comunes reutilizables (Button, Input, Alert, Spinner)
- [x] Biblioteca de componentes con documentación
- [x] Frontend React - CRUD Árboles (Fase 4 - 100% completada)
  - [x] Servicios API (arbolesService, centrosService)
  - [x] ListadoArboles (tabla responsive, filtros, cards móvil)
  - [x] DetalleArbol (vista completa, eliminar con confirmación, última lectura IoT)
  - [x] FormularioArbol (crear/editar, validaciones completas)
  - [x] Rutas configuradas y funcionando
  - [x] Refactorización Login/Register con componentes comunes
  - [x] Configuración Vercel (vercel.json, documentación)
- [x] Sistema de roles real (ADMIN / COORDINADOR, autenticación contra BD)
- [x] App Android (Fase 5 - 100% completada)
- [x] Despliegue (Vercel + Render + PostgreSQL)
- [x] Documentación (Fase 6 - 100% completada)
  - [x] Manual de Instalación completo
  - [x] Manual de Usuario (Web + móvil)
  - [x] Manuales específicos de Android
- [x] ESP32 Firmware (lectura sensores + envío al backend)
- [x] Lecturas IoT en frontend (HistoricoArbol con gráfica Recharts + mapa Leaflet, DetalleArbol con última lectura)

## Estado del Proyecto

**Fase actual**: TODAS LAS FASES COMPLETADAS

### Completado (Fase 0 - Configuración Inicial)
- [x] Configuración de entornos de desarrollo
- [x] Base de datos PostgreSQL 16.10 + TimescaleDB 2.23.1 instalada y configurada
- [x] Modelo de datos completo con diagramas E/R, UML y Relacional
- [x] Scripts SQL de creación y eliminación de tablas
- [x] Configuración de Spring Boot funcional
- [x] Estructura de proyecto Git establecida

### Completado (Fase 1 - Backend: Base de Datos y Modelo)
- [x] **Entidades JPA completadas con Javadoc y equals/hashCode**: Usuario, Rol, CentroEducativo, Arbol (con validaciones), DispositivoEsp32
- [x] **Repositorios JPA completados con queries derivadas**: UsuarioRepository, CentroEducativoRepository, ArbolRepository, DispositivoEsp32Repository
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
- [x] **ArbolController completo**: GET, POST, PUT, DELETE /api/arboles (con @Valid)
- [x] **CentroEducativoController completo**:
  - GET, POST, PUT, DELETE /api/centros (con @Valid)
  - GET /api/centros/{id}/arboles (demuestra relación 1:N)
- [x] **Testing Postman completo**:
  - CRUD de Árboles y Centros probado
  - Validaciones verificadas (400, 409)
  - Relación 1:N funcionando correctamente

### Completado (Fase 3 - Frontend Estructura)
- [x] **React + Vite + Tailwind CSS v3**
- [x] **Estructura de carpetas**: components, pages, context, services
- [x] **React Router configurado** con rutas públicas y protegidas
- [x] **AuthContext** con login, register, logout y localStorage
- [x] **Componentes de layout**: Header con menú hamburguesa responsive, MainLayout, ProtectedRoute
- [x] **Componentes comunes reutilizables**:
  - Button (variantes: primary, secondary, danger, outline)
  - Input (con label, validaciones y mensajes de error)
  - Alert (success, error, warning, info)
  - Spinner (loading con diferentes tamaños)
- [x] **Biblioteca de componentes**: Documentación visual con ejemplos de código
- [x] **Páginas de autenticación**: Login y Register con validaciones y componentes comunes
- [x] **Dashboard** con tarjetas de acceso rápido
- [x] **Navegación dinámica** y responsive con menú hamburguesa

### Completado (Fase 4 - Frontend CRUD Árboles)
- [x] **Servicios API**: arbolesService.js y centrosService.js con CRUD completo
- [x] **ListadoArboles**: Tabla responsive (desktop), cards (móvil), filtros por centro
- [x] **DetalleArbol**: Vista completa, botón eliminar con confirmación modal
- [x] **FormularioArbol**: Dual-mode (crear/editar), validaciones client-side completas
- [x] **Rutas configuradas**: /arboles, /arboles/:id, /arboles/nuevo, /arboles/:id/editar
- [x] **Testing manual**: CRUD completo verificado y funcionando
- [x] **Responsive verificado**: Todas las páginas funcionan en móvil/tablet/desktop
- [x] **Configuración Vercel**: vercel.json, documentación de despliegue lista

### Completado (Fase 5 - App Android)
- [x] **4 Activities implementadas**: Login, Register, ListarArboles, ArbolDetalles
- [x] **Retrofit configurado**: Conectado a backend en Render
- [x] **RecyclerView**: Listado de árboles con adapter optimizado
- [x] **CRUD funcional**:
  - Listar todos los árboles desde API
  - Ver detalles de árbol con sensores
  - Editar árbol (modo edición en ArbolDetalles)
  - Eliminar árbol con confirmación
- [x] **Modelos**: Arbol.java, CentroEducativo.java
- [x] **Permisos configurados**: INTERNET, ACCESS_NETWORK_STATE
- [x] **Manejo de errores**: Toast, Logs, fallback a datos locales

### Completado (Despliegue)
- [x] **Frontend**: Desplegado en Vercel → https://vocational-training-final-project.vercel.app/
- [x] **Backend**: Desplegado en Render → https://proyecto-arboles-backend.onrender.com
- [x] **Base de Datos**: PostgreSQL en Render (Internal Database)
- [x] **Configuración CORS**: Implementada y funcionando correctamente

### Completado (Fase 6 - Documentación)
- [x] **Manual de Instalación**: Guía completa para instalar BD, Backend, Frontend y Android
- [x] **Manual de Usuario**: Guía de uso de aplicación web y móvil
- [x] **Manual de Instalación Android**: Guía específica para Android con configuración
- [x] **Manual de Usuario Android**: Guía detallada de uso de la app móvil
- [x] **Índice actualizado**: Todos los documentos referenciados correctamente

## Requisitos Académicos

Este proyecto cumple con los requisitos de los siguientes módulos:

- **[PGV]** Programación de Servicios y Procesos
- **[DAD]** Desarrollo de Aplicaciones Web
- **[AED]** Acceso a Datos
- **[PGL]** Programación Multimedia y Dispositivos Móviles

Para más detalles, consulta la [Especificación Técnica](./docs/03.%20ESPECIFICACION_TECNICA.md).

## Contribución

Este proyecto ha sido desarrollado como parte del proyecto final de DAM del IES El Rincón:

[![riordi80](https://img.shields.io/badge/riordi80-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/riordi80) [![Enrique36247](https://img.shields.io/badge/Enrique36247-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/Enrique36247)

### Flujo de Trabajo Git

[Ver guía completa de Git Workflow](./docs/01.%20GIT_WORKFLOW.md)

Resumen rápido:
1. Actualizar `develop`: `git checkout develop && git pull origin develop`
2. Crear feature branch: `git checkout -b feature/nombre-funcionalidad`
3. Hacer commits: `git commit -m "feat(backend): descripción"`
4. Push: `git push origin feature/nombre-funcionalidad`
5. Crear Pull Request en GitHub: `feature/xxx` → `develop`
6. Merge y actualizar local: `git checkout develop && git pull origin develop`

### Convenciones de Commits

- `feat(componente):` Nueva funcionalidad
- `fix(componente):` Corrección de bug
- `docs(componente):` Cambios en documentación
- `style(componente):` Cambios de formato/estilo
- `refactor(componente):` Refactorización de código
- `test(componente):` Añadir o modificar tests
- `chore(componente):` Tareas de mantenimiento

Componentes: `backend`, `frontend`, `android`, `esp32`, `docs`

## Licencia

Proyecto educativo - DAM (Desarrollo de Aplicaciones Multiplataforma)

## Nota Importante: Cold Start (Render Free Tier)

El backend está desplegado en Render (versión gratuita), que entra en suspensión tras 15 minutos de inactividad.

**Al acceder por primera vez o tras inactividad**:
- El backend tarda 30-60 segundos en reactivarse
- El frontend mostrará: "No se pudo conectar con el servidor. Si es la primera carga, puede estar iniciándose (30-60 seg). Recarga la página en unos momentos."
- **Solución**: Espera 1 minuto y recarga la página (F5)

Esto es comportamiento normal del free tier de Render. Más información en el [Manual de Instalación](./docs/MANUAL_DE_INSTALACION.md#11-despliegue-en-producción-información).

---

## Documentación

### Documentos Principales

- [Índice de Documentación](./docs/00.%20INDICE.md) - Índice completo de toda la documentación del proyecto
- [Git Workflow](./docs/01.%20GIT_WORKFLOW.md) - Guía completa de flujo de trabajo con feature branches
- [Hoja de Ruta](./docs/02.%20HOJA_DE_RUTA.md) - Planificación completa del proyecto por fases
- [Especificación Técnica](./docs/03.%20ESPECIFICACION_TECNICA.md) - Requisitos y arquitectura del sistema
- [Modelo de Datos](./docs/04.%20MODELO_DATOS.md) - Diagramas E/R, UML y Relacional completos
- [Configuración PostgreSQL](./docs/04b.%20CONFIGURACION_POSTGRESQL.md) - Guía de instalación de BD
- [Requisitos Académicos](./docs/REQUISITOS.md) - Requisitos por módulo (PGV, DAD, AED, PGL)
- [Testing Postman](./docs/TESTING_POSTMAN_RESULTS.md) - Resultados de pruebas de endpoints REST

**Manuales de Usuario e Instalación:**
- [Manual de Instalación](./docs/MANUAL_DE_INSTALACION.md) - Guía completa para instalar todos los componentes del sistema
- [Manual de Usuario](./docs/MANUAL_DE_USUARIO.md) - Guía de uso de las aplicaciones web y móvil
- [Manual de Instalación Android](./docs/MANUAL_DE_INSTALACION_ANDROID.md) - Guía específica para la aplicación Android
- [Manual de Usuario Android](./docs/MANUAL_DE_USUARIO_ANDROID.md) - Guía de uso de la aplicación móvil

**README por Componente:**
- [Backend README](./backend/README.md) - API REST con Spring Boot
- [Frontend README](./frontend/README.md) - Aplicación web con React
- [Android README](./android/README.md) - Aplicación móvil Android
- [ESP32 README](./esp32/README.md) - Firmware para dispositivos IoT

### Scripts y Archivos de Configuración

- [`backend/create_database.sql`](./backend/create_database.sql) - Script SQL para crear todas las tablas del proyecto
- [`backend/drop_tables.sql`](./backend/drop_tables.sql) - Script SQL para eliminar todas las tablas (útil para resetear BD)
- [`backend/src/main/resources/README_CONFIG.md`](./backend/src/main/resources/README_CONFIG.md) - Guía de configuración segura de Spring Boot
- [`docs/install-timescaledb.sh`](./docs/install-timescaledb.sh) - Script para instalar TimescaleDB en Linux
- [`docs/Componentes para ESP32/Componentes.png`](./docs/Componentes%20para%20ESP32/Componentes.png) - Lista de componentes hardware y precios



---

## Información del Proyecto

**Nombre**: Garden Monitor - Sistema de Monitorización de Árboles

**Institución**: IES El Rincón

**Curso**: Desarrollo de Aplicaciones Multiplataforma (DAM) 2025-2026

**Repositorio**: [github.com/riordi80/vocational-training-final-project](https://github.com/riordi80/vocational-training-final-project)

**Última actualización**: 2026-02-19

### Colaboradores

[![riordi80](https://img.shields.io/badge/riordi80-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/riordi80) [![Enrique36247](https://img.shields.io/badge/Enrique36247-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/Enrique36247)

---

**Proyecto Final DAM 2025-2026** | Desarrollado con Spring Boot, React, Android y ESP32