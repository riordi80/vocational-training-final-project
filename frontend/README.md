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

### 5. Detalle de Árbol (`/arboles/:id`)
- Información del árbol
- Botones: Editar, Eliminar
- Confirmación antes de eliminar

### 6. Formulario Árbol (`/arboles/nuevo` y `/arboles/:id/editar`)
- Crear/editar árbol
- Campos: nombre, especie, fecha plantación, centro
- Validaciones

## Requisitos Funcionales Adicionales

### Responsive Design
- Diseño adaptable a móvil, tablet y desktop
- Mobile-first approach con Tailwind CSS
- Menú hamburguesa en móvil

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
- **Componentes reutilizables**: Button, Input, Card, Modal, etc.
- **Responsive Design**: Adaptable a móvil, tablet y desktop
- **Feedback visual**: Loading states, toasts, confirmaciones

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

Crear archivo `vercel.json` en la raíz de `frontend/`:

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

### 2. Variables de entorno

En Vercel Dashboard → Settings → Environment Variables:

```
VITE_API_BASE_URL=https://tu-backend-url.com/api
```

### 3. Desplegar

**Opción A: Desde CLI**
```bash
npm install -g vercel
vercel --prod
```

**Opción B: Desde GitHub**
1. Push a GitHub
2. Conectar repo en Vercel Dashboard
3. Auto-deploy en cada push

### 4. Verificar
- URL: `https://tu-proyecto.vercel.app`
- Build command: `npm run build`
- Output directory: `dist`

## Requisitos Académicos

- **[DAD]**:

  **Estructura:**
  - [x] Componentes organizados y reutilizables
  - [x] React Router DOM
  - [x] Login + Register (mock funcionales)
  - [ ] Mínimo 4 ventanas (Dashboard, Login, Register, ListadoArboles listos - faltan Detalle y Formulario)

  **Consumo API:**
  - [x] Servicios API implementados (arbolesService.js y centrosService.js)
  - [x] Integración en ListadoArboles (consumiendo getArboles, getCentros, getArbolesByCentro)
  - [ ] Integración en resto de páginas (pendiente)

  **Diseño:**
  - [x] Estilización con Tailwind CSS
  - [ ] Formularios funcionales con validaciones (Login/Register listos)
  - [x] Navegación clara (Header implementado)

  **Requisitos funcionales/no funcionales:**
  - [x] Responsive (Header, auth, Dashboard, ListadoArboles responsive)
  - [x] Login/Register con persistencia (LocalStorage)
  - [ ] Desplegar en Vercel
  - [x] Navegación dinámica
  - [ ] Gestión de CRUD establecidos (parcial - falta crear, editar, eliminar)
  - [ ] Establecer roles (mock básico implementado)
  - [ ] Feedback al usuario (mensajes error en forms)

## Plugins de Vite

Este proyecto usa:

- [@vitejs/plugin-react](https://github.com/vitejs/vite-plugin-react) - Usa Babel para Fast Refresh

## Estado

Fase 3 completada. Fase 4 en progreso:
- Servicios API implementados (4.1)
- Componentes comunes listos (4.2)
- ListadoArboles implementado (4.3)
- Faltan: DetalleArbol, FormularioArbol, integración de rutas

## Documentación Relacionada

- [Índice de Documentación](../docs/00.%20INDICE.md) - Índice completo de la documentación
- [Hoja de Ruta Completa](../docs/02.%20HOJA_DE_RUTA.md) - Planificación del proyecto
- [Especificación Técnica](../docs/03.%20ESPECIFICACION_TECNICA.md) - Requisitos y arquitectura
- [Backend README](../backend/README.md) - API REST con Spring Boot

## Contacto

Proyecto Final DAM 2025-2026