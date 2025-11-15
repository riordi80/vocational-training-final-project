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
| Base de Datos | PostgreSQL + TimescaleDB | 15+ |
| Control de Versiones | Git / GitHub | - |

## Estructura del Repositorio

```
proyecto-arboles/
├── .github/
│   └── workflows/          # GitHub Actions CI/CD
├── docs/                   # Documentación
│   ├── ESPECIFICACION_TECNICA.md
│   ├── DIAGRAMA_ER.md
│   ├── DIAGRAMA_UML.md
│   ├── MODELO_RELACIONAL.md
│   ├── MANUAL_INSTALACION.md
│   └── MANUAL_USUARIO.md
├── backend/                # API REST (Spring Boot)
├── frontend/               # Web App (React)
├── android/                # Mobile App (Android)
├── esp32/                  # Firmware ESP32
├── .gitignore
└── README.md
```

## Inicio Rápido

### Requisitos Previos

- **Backend**: Java 21, Maven
- **Frontend**: Node.js 18+, npm
- **Android**: Android Studio, JDK 21
- **ESP32**: Arduino IDE o PlatformIO
- **Base de Datos**: PostgreSQL 15+

### Instalación y Configuración

Cada componente tiene su propio README con instrucciones detalladas de instalación y configuración en su respectiva carpeta.

Para instrucciones detalladas de instalación completa, consulta el [Manual de Instalación](./docs/MANUAL_INSTALACION.md).

## Características Principales

- Monitorización en tiempo real de parámetros ambientales
- Gestión multi-centro (varios centros educativos)
- Sistema de roles y permisos (Admin, Profesor, Estudiante, Invitado)
- Alertas configurables por umbrales
- Visualización histórica con gráficas
- Aplicaciones web y móvil sincronizadas
- Arquitectura escalable y segura (JWT, HTTPS)

## Requisitos Académicos

Este proyecto cumple con los requisitos de los siguientes módulos:

- **[PGV]** Programación de Servicios y Procesos
- **[DAD]** Desarrollo de Aplicaciones Web
- **[AED]** Acceso a Datos
- **[PGL]** Programación Multimedia y Dispositivos Móviles

Para más detalles, consulta la [Especificación Técnica](./docs/ESPECIFICACION_TECNICA.md).

## Contribución

Este proyecto es desarrollado por un equipo de 2 personas como parte del proyecto final de DAM.

### Flujo de Trabajo Git

1. Trabajar siempre en una rama feature:
   ```bash
   git checkout -b feature/nombre-funcionalidad
   ```

2. Hacer commits siguiendo convenciones:
   ```bash
   git commit -m "feat(backend): añadir endpoint de árboles"
   ```

3. Push y crear Pull Request:
   ```bash
   git push origin feature/nombre-funcionalidad
   ```

4. Revisión de código antes de merge a `develop`

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

- [Especificación Técnica](./docs/ESPECIFICACION_TECNICA.md)
- [Diagrama E/R](./docs/DIAGRAMA_ER.md) _(pendiente)_
- [Diagrama UML](./docs/DIAGRAMA_UML.md) _(pendiente)_
- [Modelo Relacional](./docs/MODELO_RELACIONAL.md) _(pendiente)_
- [Manual de Instalación](./docs/MANUAL_INSTALACION.md) _(pendiente)_
- [Manual de Usuario](./docs/MANUAL_USUARIO.md) _(pendiente)_



---

## Miembros

[![riordi80](https://img.shields.io/badge/GitHub-riordi80-181717?style=plastic&logo=github&logoColor=white)](https://github.com/riordi80) [![Enrique36247](https://img.shields.io/badge/GitHub-Enrique36247-181717?style=plastic&logo=github&logoColor=white)](https://github.com/Enrique36247)


**Proyecto**: Proyecto Árboles



**Estado del Proyecto**: En desarrollo
**Última actualización**: 2025-11-15