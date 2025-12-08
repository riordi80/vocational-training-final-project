# Frontend - Proyecto Árboles

Aplicación web desarrollada con React para el sistema de monitorización de árboles.

## Aplicación Desplegada

**URL en producción**: [https://vocational-training-final-project.vercel.app/](https://vocational-training-final-project.vercel.app/)

**Plataforma**: Vercel (deployment automático desde GitHub)

## Tecnologías

- **Framework**: React 18+
- **Lenguaje**: JavaScript/TypeScript
- **Build Tool**: Vite
- **Routing**: React Router DOM
- **Estilización**: Tailwind CSS (configurado)
- **HTTP Client**: Axios o Fetch API
- **State Management**: Context API / Redux (a definir)

## Estructura del Proyecto

```
frontend/
├── public/
│   └── assets/              # Recursos estáticos
├── src/
│   ├── components/          # Componentes reutilizables
│   │   ├── common/          # Button, Input, Alert, Spinner, ProtectedRoute
│   │   ├── layout/          # Header, MainLayout
│   │   └── ...
│   ├── pages/               # Páginas/Vistas principales
│   │   ├── login/
│   │   │   └── Login.jsx
│   │   ├── register/
│   │   │   └── Register.jsx
│   │   ├── dashboard/
│   │   │   └── Dashboard.jsx
│   │   ├── component-library/
│   │   │   └── ComponentLibrary.jsx
│   │   ├── arboles/
│   │   ├── centros/
│   │   └── ...
│   ├── services/            # Llamadas a API
│   │   ├── api.js           # Configuración axios
│   │   ├── arbolesService.js # CRUD completo de árboles
│   │   ├── centrosService.js # CRUD completo de centros
│   │   └── ...
│   ├── context/             # Context API para estado global
│   │   ├── AuthContext.jsx
│   │   └── ...
│   ├── hooks/               # Custom hooks
│   ├── utils/               # Utilidades
│   ├── App.jsx
│   └── main.jsx
├── index.html
├── package.json
├── vite.config.js
├── tailwind.config.js
├── .gitignore
└── README.md
```

## Requisitos Previos

- Node.js 18+
- npm o yarn

## Instalación y Configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/riordi80/vocational-training-final-project
cd frontend
```

### 2. Instalar dependencias

```bash
npm install
```

### 3. Configurar variables de entorno

Crear archivo `.env` en la raíz de `frontend/`:

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

Para producción:
```env
VITE_API_BASE_URL=https://tu-dominio.com/api
```

**IMPORTANTE**: No commitear archivos `.env` con credenciales reales.

### 4. Ejecutar en modo desarrollo

```bash
npm run dev
```

La aplicación estará disponible en: `http://localhost:5173`

## Scripts Disponibles

```bash
# Desarrollo
npm run dev              # Inicia servidor de desarrollo con HMR

# Build
npm run build            # Genera build de producción en dist/
npm run preview          # Preview del build de producción

# Linting
npm run lint             # Ejecuta ESLint para verificar código
```

## Páginas/Vistas (Requisito DAD)

### 1. Login (`/login`)
- Formulario con email y contraseña
- Mock: guarda en localStorage, redirige a Dashboard
- Validación básica

### 2. Register (`/register`)
- Formulario de registro
- Validación (email válido, contraseñas coinciden)
- Redirige a Login

### 3. Dashboard (`/dashboard`)
- Mensaje de bienvenida
- Links a gestión de árboles
- Navegación (menú/cabecera)

### 4. Listado de Árboles (`/arboles`)
- Tabla responsive con árboles (desktop: tabla, mobile: cards)
- Botón "Añadir Árbol" que navega a `/arboles/nuevo`
- Click en fila para ir al detalle
- Filtro por centro educativo (dropdown funcional)
- Estados de loading (Spinner) y errores (Alert)
- Integración con arbolesService y centrosService

### 5. Detalle de Árbol (`/arboles/:id`)
- Vista completa con información general y umbrales de monitorización
- Botones: Volver, Editar, Eliminar
- Modal de confirmación antes de eliminar
- Manejo de estados (loading, error, árbol no encontrado)
- Formateo de fechas mejorado
- Integración con getArbolById y deleteArbol
- Navegación con state para feedback

### 6. Formulario Árbol (`/arboles/nuevo` y `/arboles/:id/editar`)
- Componente dual: crear Y editar en uno solo
- Campos obligatorios: nombre, especie, fecha plantación, centro educativo
- Campos opcionales: ubicación, umbrales de monitorización
- Validaciones client-side completas (campos requeridos, fecha no futura, rangos de umbrales)
- Carga de centros desde API para select
- Detección automática de modo (crear/editar) con useParams
- Navegación con state para mensajes de éxito
- Integración con createArbol y updateArbol

## Requisitos Funcionales Adicionales

### Responsive Design
- Diseño adaptable a móvil, tablet y desktop
- Mobile-first approach con Tailwind CSS
- Menú hamburguesa en móvil (implementado con iconos SVG)
- Todas las páginas optimizadas para móvil (Login, Register, Dashboard, CRUD Árboles)

### Persistencia de Datos
- Login/Register guardan en localStorage
- Sesión persiste al recargar página
- Logout limpia localStorage

### Sistema de Roles
- Roles: Admin, Profesor, Estudiante (mock)
- Rutas protegidas según rol
- Funcionalidades restringidas por rol

### Feedback al Usuario
- Mensajes de éxito (toast/alert verde)
- Mensajes de error (toast/alert rojo)
- Loading spinners durante peticiones
- Confirmaciones antes de acciones destructivas

### Navegación Dinámica
- Menú cambia según rol
- Breadcrumbs para ubicación
- Transiciones suaves entre páginas

### Despliegue
- Configurado para Vercel
- Variables de entorno para API
- Build optimizado

## Autenticación (Mock)

Login mock con localStorage:

```javascript
// Login.jsx
localStorage.setItem('user', JSON.stringify({ email, name: 'Usuario' }));
```

## Consumo de API REST

Ejemplo de servicio (`src/services/arbolesService.js`):

```javascript
import api from './api';

export const getArboles = async () => {
  const response = await api.get('/arboles');
  return response.data;
};

export const getArbolById = async (id) => {
  const response = await api.get(`/arboles/${id}`);
  return response.data;
};

export const createArbol = async (arbol) => {
  const response = await api.post('/arboles', arbol);
  return response.data;
};

export const updateArbol = async (id, arbol) => {
  const response = await api.put(`/arboles/${id}`, arbol);
  return response.data;
};

export const deleteArbol = async (id) => {
  const response = await api.delete(`/arboles/${id}`);
  return response.data;
};
```

## Validación de Formularios

Todos los formularios incluyen validación client-side:

- Email válido
- Campos requeridos
- Rangos numéricos (umbrales)
- Fechas coherentes
- Confirmación de contraseña

## Estilos y Componentes

- **Tailwind CSS**: Para estilos utilitarios
- **Componentes reutilizables**: Button, Input, Alert, Spinner (usados en todas las páginas)
- **Responsive Design**: Adaptable a móvil, tablet y desktop con menú hamburguesa
- **Feedback visual**: Loading states (Spinner), mensajes de error/éxito (Alert), confirmaciones (modales)
- **Consistencia**: Login, Register y Header refactorizados para usar componentes comunes

## Build para Producción

```bash
npm run build
```

Los archivos optimizados se generan en `dist/`

### Servir build con servidor estático

```bash
npm install -g serve
serve -s dist -p 3000
```

## Despliegue en Vercel (Requisito DAD)

### Aplicación Desplegada

- **URL**: https://vocational-training-final-project.vercel.app/
- **Plataforma**: Vercel
- **Deploy**: Automático desde rama `main`
- **Backend**: https://proyecto-arboles-backend.onrender.com

### Configuración Implementada

#### 1. Archivo vercel.json

Para monorepo con frontend en subcarpeta, se configuraron DOS archivos `vercel.json`:

**Raíz del proyecto** (`vercel.json`):
```json
{
  "buildCommand": "cd frontend && npm install && npm run build",
  "outputDirectory": "frontend/dist",
  "installCommand": "cd frontend && npm install"
}
```

**Dentro de frontend** (`frontend/vercel.json`):
```json
{
  "rewrites": [
    {
      "source": "/(.*)",
      "destination": "/index.html"
    }
  ]
}
```

El segundo archivo es necesario para que React Router funcione correctamente (SPA routing).

#### 2. Configuración en Vercel Dashboard

**Settings → General:**
- **Framework Preset**: Vite
- **Root Directory**: (dejar vacío, se maneja con vercel.json)
- **Build Command**: Configurado en vercel.json raíz
- **Output Directory**: Configurado en vercel.json raíz

**Settings → Git:**
- **Production Branch**: main
- **Auto-Deploy**: Enabled

#### 3. Variables de Entorno

**Settings → Environment Variables:**

```
Name: VITE_API_BASE_URL
Value: https://proyecto-arboles-backend.onrender.com/api
Environments: Production, Preview, Development (todos marcados)
```

**IMPORTANTE**: Las variables de entorno de Vite (prefijo `VITE_`) se bake durante el build, no en runtime. Después de cambiar variables, hacer redeploy con "Clear cache & deploy".

#### 4. Deployment Automático

- **Push a main**: Deploy automático a producción
- **Pull Requests**: Vercel crea URLs de preview únicas
- **Rollbacks**: Disponibles desde Deployments tab

### Verificación del Despliegue

```bash
# Verificar que el frontend carga
curl https://vocational-training-final-project.vercel.app/

# Verificar que conecta con backend
# (abrir DevTools → Network mientras navegas por la app)
```

### Troubleshooting

**Error: "Page not found" en rutas**
- Verificar que `frontend/vercel.json` tiene los rewrites configurados
- El archivo debe estar en `frontend/vercel.json`, NO en la raíz

**Error: "Cannot GET /api/..."**
- Verificar variable `VITE_API_BASE_URL` en Vercel Dashboard
- Hacer redeploy con "Clear cache & deploy"
- Verificar que la variable está marcada para "Production"

**Error: CORS al llamar backend**
- Verificar que backend tiene configurado CORS para la URL de Vercel
- Ver `backend/src/main/java/com/example/gardenmonitor/config/CorsConfig.java`

**Build falla**
- Verificar que `vercel.json` raíz apunta correctamente a `frontend/`
- Revisar logs de build en Vercel Dashboard → Deployments

## Requisitos Académicos

- **[DAD]**:

  **Estructura:**
  - [x] Componentes organizados y reutilizables
  - [x] React Router DOM
  - [x] Login + Register (refactorizados con componentes comunes)
  - [x] Mínimo 4 ventanas (Dashboard, ListadoArboles, DetalleArbol, FormularioArbol)

  **Consumo API:**
  - [x] Servicios API implementados (arbolesService.js y centrosService.js)
  - [x] Integración completa en todas las páginas de árboles
  - [x] CRUD completo funcional

  **Diseño:**
  - [x] Estilización con Tailwind CSS
  - [x] Formularios funcionales con validaciones (Login/Register/Árboles)
  - [x] Navegación clara (Header con menú hamburguesa responsive)

  **Requisitos funcionales/no funcionales:**
  - [x] Responsive (todas las páginas optimizadas, menú hamburguesa)
  - [x] Login/Register con persistencia (LocalStorage)
  - [x] Desplegar en Vercel (COMPLETADO - https://vocational-training-final-project.vercel.app/)
  - [x] Navegación dinámica
  - [x] Gestión de CRUD establecidos (Árboles completo: crear, editar, eliminar, listar, detalle)
  - [x] Establecer roles (mock básico implementado)
  - [x] Feedback al usuario (componentes Alert, Spinner, modales de confirmación)

## Plugins de Vite

Este proyecto usa:

- [@vitejs/plugin-react](https://github.com/vitejs/vite-plugin-react) - Usa Babel para Fast Refresh

## Estado de la Aplicación Frontend

**Aplicación completada y desplegada en producción**

- [x] Servicios API implementados (arbolesService, centrosService)
- [x] Componentes comunes reutilizables (Button, Input, Alert, Spinner)
- [x] Sistema de autenticación mock con Context API
- [x] CRUD completo de árboles:
  - ListadoArboles (listar, filtrar, buscar)
  - DetalleArbol (ver detalles completos)
  - FormularioArbol (crear y editar)
- [x] Diseño responsive con Tailwind CSS
- [x] Menú hamburguesa para móvil
- [x] React Router configurado
- [x] Desplegado en Vercel

## Documentación Relacionada

### Manuales
- [Manual de Instalación](../docs/MANUAL_DE_INSTALACION.md) - Guía completa de instalación
- [Manual de Usuario](../docs/MANUAL_DE_USUARIO.md) - Guía de uso del sistema

### Documentación Técnica
- [Índice de Documentación](../docs/00.%20INDICE.md) - Índice completo
- [Hoja de Ruta](../docs/02.%20HOJA_DE_RUTA.md) - Planificación del proyecto
- [Especificación Técnica](../docs/03.%20ESPECIFICACION_TECNICA.md) - Requisitos y arquitectura
- [Backend README](../backend/README.md) - API REST con Spring Boot

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