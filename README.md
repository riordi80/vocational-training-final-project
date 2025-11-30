# Proyecto Árboles

Sistema de monitorización y gestión de árboles plantados en centros educativos mediante sensores IoT.

## Descripción

Este proyecto permite recopilar datos ambientales (temperatura, humedad del suelo, pH, nivel de agua) a través de dispositivos ESP32 con sensores, y visualizarlos en tiempo real mediante aplicaciones web y móvil. El sistema está diseñado para centros educativos que desean monitorizar el crecimiento y estado de sus árboles.

## Componentes del Proyecto

Este es un **monorepo** que contiene todos los componentes del sistema:

### 📁 `/backend`
API REST desarrollada con **Spring Boot (Java)**
- Autenticación y autorización (JWT)
- Gestión de usuarios, centros, árboles y dispositivos
- Ingesta de datos de sensores
- Sistema de alertas
- Base de datos: PostgreSQL + TimescaleDB

### 📁 `/frontend`
Aplicación web desarrollada con **React**
- Dashboard interactivo
- Visualización de datos en tiempo real
- Gráficas históricas
- Gestión completa (CRUD)
- Configuración de alertas

### 📁 `/android`
Aplicación móvil desarrollada con **Android (Java)**
- Visualización de datos en tiempo real
- Gestión de árboles
- Notificaciones push
- Filtrado por centros educativos

### 📁 `/esp32`
Firmware para dispositivos **ESP32 (C/C++)**
- Lectura de sensores (temperatura, humedad, pH, nivel de agua)
- Conectividad WiFi
- Envío de datos al backend
- Modo ahorro de energía

### 📁 `/docs`
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

## Características Principales

- ✅ Modelo de datos completo (E/R, UML, Relacional)
- ✅ Base de datos optimizada para series temporales (PostgreSQL 16 + TimescaleDB 2.23.1)
- ✅ Entidades JPA completas con validaciones y Javadoc
- ✅ Repositorios JPA con queries derivadas
- ✅ Sistema de roles (Admin, Profesor, Estudiante, Invitado)
- ⏳ API REST con relaciones 1:N (Fase 2)
- ⏳ Frontend React con CRUD (Fases 3-4)
- ⏳ App Android (Fases 5-6)
- ⏸️ Sistema de autenticación JWT completa (Fase 8 - pospuesta)
- ⏳ Monitorización en tiempo real con ESP32 (Fase 14 - Opcional)

## Estado del Proyecto

**Fase actual**: ✅ Fase 1 COMPLETADA | Iniciando Fase 2 (API REST 1:N)

### ✅ Completado (Fase 0)
- Configuración de entornos de desarrollo
- Base de datos PostgreSQL 16.10 + TimescaleDB 2.23.1 instalada y configurada
- Modelo de datos completo con diagramas E/R, UML y Relacional
- Scripts SQL de creación y eliminación de tablas
- Configuración de Spring Boot funcional
- Estructura de proyecto Git establecida

### ✅ Completado (Fase 1 - Backend: Base de Datos y Modelo)
- ✅ **Entidades JPA completadas con Javadoc y equals/hashCode**: Usuario, Rol, CentroEducativo, Arbol, DispositivoEsp32
- ✅ **Repositorios JPA completados con queries derivadas**: UsuarioRepository, CentroEducativoRepository, ArbolRepository, DispositivoEsp32Repository
- ✅ **Relaciones bidireccionales implementadas**:
  - CentroEducativo ↔ Arbol (OneToMany/ManyToOne con métodos helper)
  - Arbol ↔ DispositivoEsp32 (OneToOne bidireccional)
- ✅ **Aplicación Spring Boot arranca correctamente**
- ✅ **Compilación exitosa con Maven**
- ⏳ Pendiente (pospuesto):
  - Crear application-dev.properties y application-prod.properties

### 📅 Próximos Hitos
- **Fase 2**: API REST con relaciones 1:N - CRUD Centros y Árboles ← **SIGUIENTE**
- **Fase 3**: Frontend React - Estructura y navegación
- **Fase 4**: Frontend React - CRUD Árboles completo
- **Fases 5-6**: App Android - CRUD Árboles
- **Fase 7**: Documentación y manuales

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

### 📚 Documentos Principales

- [Índice de Documentación](./docs/00.%20INDICE.md) - Índice completo de toda la documentación del proyecto
- [Git Workflow](./docs/01.%20GIT_WORKFLOW.md) - Guía completa de flujo de trabajo con feature branches
- [Hoja de Ruta](./docs/02.%20HOJA_DE_RUTA.md) - Planificación completa del proyecto por fases
- [Especificación Técnica](./docs/03.%20ESPECIFICACION_TECNICA.md) - Requisitos y arquitectura del sistema
- [Modelo de Datos](./docs/04.%20MODELO_DATOS.md) - Diagramas E/R, UML y Relacional completos
- [Configuración PostgreSQL](./docs/04b.%20CONFIGURACION_POSTGRESQL.md) - Guía de instalación de BD

**Documentación de Aplicaciones:**
- [Proyecto Backend](./docs/Aplicaciones/BACKEND.md) - Documentación del API REST
- [Proyecto Frontend](./docs/Aplicaciones/FRONTEND.md) - Documentación de la aplicación web
- [Proyecto Android](./docs/Aplicaciones/ANDROID.md) - Documentación de la app móvil
- [Proyecto ESP32](./docs/Aplicaciones/ESP32.md) - Documentación del firmware IoT
- [Manual de Instalación](./docs/MANUAL_INSTALACION.md) _(pendiente)_
- [Manual de Usuario](./docs/MANUAL_USUARIO.md) _(pendiente)_

### 🛠️ Scripts y Archivos de Configuración

- [`backend/create_database.sql`](./backend/create_database.sql) - Script SQL para crear todas las tablas del proyecto
- [`backend/drop_tables.sql`](./backend/drop_tables.sql) - Script SQL para eliminar todas las tablas (útil para resetear BD)
- [`backend/src/main/resources/README_CONFIG.md`](./backend/src/main/resources/README_CONFIG.md) - Guía de configuración segura de Spring Boot
- [`docs/install-timescaledb.sh`](./docs/install-timescaledb.sh) - Script para instalar TimescaleDB en Linux
- [`docs/Componentes para ESP32/Componentes.png`](./docs/Componentes%20para%20ESP32/Componentes.png) - Lista de componentes hardware y precios



---

**Proyecto**: Proyecto Árboles


**Estado del Proyecto**: ✅ Fase 1 Completada | Iniciando Fase 2
**Última actualización**: 2025-11-30