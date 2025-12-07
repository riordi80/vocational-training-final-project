# Proyecto Árboles

Sistema de monitorización y gestión de árboles plantados en centros educativos mediante sensores IoT.

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
- Sistema de roles (mock)
- Componentes reutilizables (Button, Input, Alert, Spinner)
- Feedback usuario (mensajes éxito/error, validaciones)
- Configurado para despliegue en Vercel

### `/android`
Aplicación móvil con **Android (Java)**
- Listado de árboles por centro
- Visualización de detalles
- Modificar y eliminar árboles

### `/esp32`
Firmware **ESP32 (C/C++)** - Opcional
- Lectura de sensores
- WiFi + envío de datos

### `/docs`
Documentación completa del proyecto
- Especificación técnica
- Diagramas (E/R, UML, Relacional)
- Manuales de instalación y usuario

## Tecnologías Utilizadas

| Componente | Tecnología | Versión |
|------------|-----------|---------|
| Backend | Spring Boot (Java) | 3.x / Java 21 |
| Frontend | React | 18+ |
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
│   ├── Aplicaciones/                     # Documentación de aplicaciones
│   │   ├── BACKEND.md                    # Documentación backend
│   │   ├── FRONTEND.md                   # Documentación frontend
│   │   ├── ANDROID.md                    # Documentación Android
│   │   └── ESP32.md                      # Documentación ESP32
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

## Inicio Rápido

### Requisitos Previos

- **Backend**: Java 21, Maven
- **Frontend**: Node.js 18+, npm
- **Android**: Android Studio, JDK 21
- **ESP32**: Arduino IDE o PlatformIO
- **Base de Datos**: PostgreSQL 16+ con TimescaleDB

### Instalación y Configuración

#### 1. Base de Datos
```bash
# Ver scripts de creación en:
./backend/create_database.sql          # Crear todas las tablas
./backend/drop_tables.sql              # Eliminar todas las tablas (si necesario)
```

Consulta [`docs/04b. CONFIGURACION_POSTGRESQL.md`](./docs/04b.%20CONFIGURACION_POSTGRESQL.md) para instalación detallada de PostgreSQL y TimescaleDB.

#### 2. Backend
```bash
cd backend
./mvnw clean install
./mvnw spring-boot:run
```

⚠️ **Importante**: Antes de ejecutar, configura las credenciales de la base de datos. Ver [`backend/src/main/resources/README_CONFIG.md`](./backend/src/main/resources/README_CONFIG.md) para detalles sobre configuración segura.

#### 3. Frontend y Android
Cada componente tiene su propio README con instrucciones detalladas en su respectiva carpeta.

Para instrucciones completas de instalación, consulta el [Manual de Instalación](./docs/MANUAL_INSTALACION.md) _(pendiente)_.

## Estado Actual

- [x] Modelo de datos (E/R, UML, Relacional)
- [x] PostgreSQL 16 + TimescaleDB 2.23.1
- [x] 4 Entidades JPA con validaciones completas
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
  - [x] DetalleArbol (vista completa, eliminar con confirmación)
  - [x] FormularioArbol (crear/editar, validaciones completas)
  - [x] Rutas configuradas y funcionando
  - [x] Refactorización Login/Register con componentes comunes
  - [x] Configuración Vercel (vercel.json, documentación)
- [ ] App Android (Fase 5)
- [ ] ESP32 (Opcional - después del 8 dic)

## Estado del Proyecto

**Fase actual**: Fase 4 COMPLETADA - Frontend React CRUD Árboles

### Completado (Fase 0)
- Configuración de entornos de desarrollo
- Base de datos PostgreSQL 16.10 + TimescaleDB 2.23.1 instalada y configurada
- Modelo de datos completo con diagramas E/R, UML y Relacional
- Scripts SQL de creación y eliminación de tablas
- Configuración de Spring Boot funcional
- Estructura de proyecto Git establecida

### Completado (Fase 1 - Backend: Base de Datos y Modelo)
- [x] **Entidades JPA completadas con Javadoc y equals/hashCode**: Usuario, Rol, CentroEducativo, Arbol (con validaciones), DispositivoEsp32
- [x] **Repositorios JPA completados con queries derivadas**: UsuarioRepository, CentroEducativoRepository, ArbolRepository, DispositivoEsp32Repository
- [x] **Relaciones bidireccionales implementadas**:
  - CentroEducativo ↔ Arbol (OneToMany/ManyToOne)
  - Arbol ↔ DispositivoEsp32 (OneToOne bidireccional)
- [x] **Aplicación Spring Boot arranca correctamente**
- [x] **Compilación exitosa con Maven**

### Completado (Fase 2 - Endpoints 1:N) - 100% completado
- [x] **Validaciones completas**:
  - @Valid en ArbolController y CentroEducativoController (POST y PUT)
  - @NotBlank/@NotNull en CentroEducativo
  - @JsonIgnore en List<Arbol> para evitar loops
- [x] **ArbolController completo**: GET, POST, PUT, DELETE /api/arboles (con @Valid)
- [x] **CentroEducativoController completo**:
  - GET, POST, PUT, DELETE /api/centros (con @Valid)
  - GET /api/centros/{id}/arboles (demuestra relación 1:N) ⭐
- [x] **Testing Postman completo**:
  - CRUD de Árboles y Centros probado
  - Validaciones verificadas (400, 409)
  - Relación 1:N funcionando correctamente

### Completado (Fase 3 - Frontend Estructura) - 100% completado
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

### Completado (Fase 4 - Frontend CRUD Árboles) - 100% completado
- [x] **Servicios API**: arbolesService.js y centrosService.js con CRUD completo
- [x] **ListadoArboles**: Tabla responsive (desktop), cards (móvil), filtros por centro
- [x] **DetalleArbol**: Vista completa, botón eliminar con confirmación modal
- [x] **FormularioArbol**: Dual-mode (crear/editar), validaciones client-side completas
- [x] **Rutas configuradas**: /arboles, /arboles/:id, /arboles/nuevo, /arboles/:id/editar
- [x] **Testing manual**: CRUD completo verificado y funcionando
- [x] **Responsive verificado**: Todas las páginas funcionan en móvil/tablet/desktop
- [x] **Configuración Vercel**: vercel.json, documentación de despliegue lista

### Despliegue Completado
- [x] **Frontend**: Desplegado en Vercel → https://vocational-training-final-project.vercel.app/
- [x] **Backend**: Desplegado en Render → https://proyecto-arboles-backend.onrender.com
- [x] **Base de Datos**: PostgreSQL en Render (Internal Database)
- [x] **Configuración CORS**: Implementada y funcionando correctamente

### Próximos Hitos (Deadline: 8 dic)
- **Fase 5**: App Android - CRUD Árboles (opcional)
- **Fase 6**: Documentación (manuales finales)

## Requisitos Académicos

Este proyecto cumple con los requisitos de los siguientes módulos:

- **[PGV]** Programación de Servicios y Procesos
- **[DAD]** Desarrollo de Aplicaciones Web
- **[AED]** Acceso a Datos
- **[PGL]** Programación Multimedia y Dispositivos Móviles

Para más detalles, consulta la [Especificación Técnica](./docs/03.%20ESPECIFICACION_TECNICA.md).

## Contribución

Este proyecto ha sido desarrollado como parte del proyecto final de DAM del IES El Rincón:

[![riordi80](https://img.shields.io/badge/GitHub-riordi80-181717?style=plastic&logo=github&logoColor=white)](https://github.com/riordi80) [![Enrique36247](https://img.shields.io/badge/GitHub-Enrique36247-181717?style=plastic&logo=github&logoColor=white)](https://github.com/Enrique36247)

### Flujo de Trabajo Git

📖 **[Ver guía completa de Git Workflow](./docs/01.%20GIT_WORKFLOW.md)**

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

**README por Componente:**
- [Backend README](./backend/README.md) - API REST con Spring Boot
- [Frontend README](./frontend/README.md) - Aplicación web con React
- [Android README](./android/README.md) - Aplicación móvil Android
- [ESP32 README](./esp32/README.md) - Firmware para dispositivos IoT

**Manuales (Pendientes):**
- [ ] Manual de Instalación - Guía completa para instalar todos los componentes
- [ ] Manual de Usuario - Guía de uso de las aplicaciones web y móvil

### Scripts y Archivos de Configuración

- [`backend/create_database.sql`](./backend/create_database.sql) - Script SQL para crear todas las tablas del proyecto
- [`backend/drop_tables.sql`](./backend/drop_tables.sql) - Script SQL para eliminar todas las tablas (útil para resetear BD)
- [`backend/src/main/resources/README_CONFIG.md`](./backend/src/main/resources/README_CONFIG.md) - Guía de configuración segura de Spring Boot
- [`docs/install-timescaledb.sh`](./docs/install-timescaledb.sh) - Script para instalar TimescaleDB en Linux
- [`docs/Componentes para ESP32/Componentes.png`](./docs/Componentes%20para%20ESP32/Componentes.png) - Lista de componentes hardware y precios



---

**Proyecto**: Proyecto Árboles


**Estado del Proyecto**: - [x] Fases 1, 2, 3 y 4 Completadas | [x] Despliegue Completo (Vercel + Render)
**Última actualización**: 2025-12-07