# Frontend - Proyecto Árboles

Aplicación web desarrollada con React para el sistema de monitorización de árboles.

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

## Páginas/Vistas (Requisito DAD Mínimo)

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

### 4. Listado de Árboles (`/arboles`) - IMPLEMENTADO
- Tabla responsive con árboles (desktop: tabla, mobile: cards)
- Botón "Añadir Árbol" que navega a `/arboles/nuevo`
- Click en fila para ir al detalle
- Filtro por centro educativo (dropdown funcional)
- Estados de loading (Spinner) y errores (Alert)
- Integración con arbolesService y centrosService

### 5. Detalle de Árbol (`/arboles/:id`) - IMPLEMENTADO
- Vista completa con información general y umbrales de monitorización
- Botones: Volver, Editar, Eliminar
- Modal de confirmación antes de eliminar
- Manejo de estados (loading, error, árbol no encontrado)
- Formateo de fechas mejorado
- Integración con getArbolById y deleteArbol
- Navegación con state para feedback

### 6. Formulario Árbol (`/arboles/nuevo` y `/arboles/:id/editar`) - IMPLEMENTADO
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

### 1. Preparar proyecto

El archivo `vercel.json` ya está configurado en la raíz de `frontend/`:

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

Este archivo es necesario para que React Router funcione correctamente.

### 2. Configuración en Vercel Dashboard

1. Ve a [vercel.com](https://vercel.com) y crea una cuenta con GitHub
2. Click en "Add New Project"
3. Importa el repositorio: `vocational-training-final-project`
4. Configuración del proyecto:
   - **Framework Preset**: Vite
   - **Root Directory**: `frontend`
   - **Build Command**: `npm run build`
   - **Output Directory**: `dist`

### 3. Variables de entorno

En Vercel Dashboard → Settings → Environment Variables:

```
VITE_API_BASE_URL=https://tu-backend.railway.app/api
```

Actualiza la URL cuando despliegues el backend en Railway.

### 4. Desplegar

**Opción A: Desde GitHub (Recomendada)**
1. Configuración automática desde paso 2
2. Cada push despliega automáticamente
3. Vercel crea URLs de preview para PRs

**Opción B: Desde CLI**
```bash
npm install -g vercel
cd frontend
vercel --prod
```

### 5. Verificar

- Build command: `npm run build`
- Output directory: `dist`
- Framework: Vite

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
  - [ ] Desplegar en Vercel (pendiente)
  - [x] Navegación dinámica
  - [x] Gestión de CRUD establecidos (Árboles completo: crear, editar, eliminar, listar, detalle)
  - [x] Establecer roles (mock básico implementado)
  - [x] Feedback al usuario (componentes Alert, Spinner, modales de confirmación)

## Plugins de Vite

Este proyecto usa:

- [@vitejs/plugin-react](https://github.com/vitejs/vite-plugin-react) - Usa Babel para Fast Refresh

## Estado

Fase 3 completada. Fase 4 COMPLETADA:
- Servicios API implementados (4.1)
- Componentes comunes listos (4.2)
- ListadoArboles implementado (4.3)
- DetalleArbol implementado (4.4)
- FormularioArbol implementado (4.5)
- Rutas configuradas (4.6)
- Testing y mejoras responsive (4.7):
  - CRUD completo de árboles funcional
  - Menú hamburguesa implementado
  - Login/Register refactorizados con componentes comunes
  - Header usando componente Button
  - Responsive verificado en todas las páginas

## Documentación Relacionada

- [Índice de Documentación](../docs/00.%20INDICE.md) - Índice completo de la documentación
- [Hoja de Ruta Completa](../docs/02.%20HOJA_DE_RUTA.md) - Planificación del proyecto
- [Especificación Técnica](../docs/03.%20ESPECIFICACION_TECNICA.md) - Requisitos y arquitectura
- [Backend README](../backend/README.md) - API REST con Spring Boot

## Contacto

Proyecto Final DAM 2025-2026