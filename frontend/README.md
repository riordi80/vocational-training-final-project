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
│   │   ├── common/          # Componentes comunes (Button, Input, etc.)
│   │   ├── layout/          # Layout (Header, Sidebar, Footer)
│   │   └── ...
│   ├── pages/               # Páginas/Vistas principales
│   │   ├── Login.jsx
│   │   ├── Register.jsx
│   │   ├── Dashboard.jsx
│   │   ├── Arboles/
│   │   ├── Centros/
│   │   ├── Usuarios/
│   │   └── Alertas/
│   ├── services/            # Llamadas a API
│   │   ├── api.js           # Configuración axios
│   │   ├── authService.js
│   │   ├── arbolesService.js
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

## Páginas/Vistas Principales (Requisito DAD)

### 1. Login (`/login`)
- Formulario de autenticación
- Validación de email y contraseña
- Mensaje de error en caso de credenciales incorrectas
- Link a registro

### 2. Register (`/register`)
- Formulario de registro de usuario
- Validación de campos (email, contraseña, confirmación, etc.)
- Selección de rol (si aplicable)
- Registro exitoso redirige a login

### 3. Dashboard Principal (`/dashboard`)
- Resumen general del sistema
- Tarjetas con estadísticas: total de árboles, alertas activas, centros
- Gráfica de resumen de lecturas recientes
- Acceso rápido a secciones principales
- Navegación clara con menú lateral o superior

### 4. Listado de Árboles (`/arboles`)
- Tabla/Grid con todos los árboles (filtrable por centro)
- Búsqueda y filtros
- Indicador visual de estado (alertas activas)
- Botón para añadir nuevo árbol (Admin/Profesor)
- Click en árbol lleva a vista de detalle
- **CRUD completo** (requisito DAD)

### 5. Detalle de Árbol (`/arboles/:id`)
- Información del árbol (especie, fecha plantación, ubicación)
- Datos en tiempo real: temperatura, humedad, pH, nivel agua
- Gráficas históricas (últimas 24h, 7 días, 30 días)
- Historial de alertas
- Botones de edición y eliminación (según permisos)

### 6. Gestión de Centros Educativos (`/centros`)
- Listado de centros (Admin/Profesor)
- CRUD de centros
- Asignación de usuarios a centros
- Vista de árboles por centro

### 7. Gestión de Usuarios (`/usuarios`)
- Listado de usuarios (Admin)
- Creación, edición y eliminación de usuarios
- Asignación de roles
- Asignación de centros a usuarios (relación N:M)

### 8. Configuración de Alertas (`/alertas`)
- Configuración de umbrales por árbol
- Histórico de alertas
- Marcar alertas como resueltas
- Configuración de notificaciones

## Autenticación

El token JWT se almacena en `localStorage` tras login exitoso:

```javascript
localStorage.setItem('token', token);
```

Se incluye en todas las peticiones mediante interceptor de axios:

```javascript
axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
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

## Despliegue

### Opción 1: VPS con Nginx

```nginx
server {
    listen 80;
    server_name tu-dominio.com;

    root /var/www/proyecto-arboles/frontend/dist;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

### Opción 2: Netlify / Vercel

Configurar `netlify.toml` o `vercel.json` para SPA:

```toml
[[redirects]]
  from = "/*"
  to = "/index.html"
  status = 200
```

## Requisitos Académicos Cumplidos

- **[DAD]**:
  - ✅ Componentes organizados y reutilizables
  - ✅ React Router DOM implementado
  - ✅ Login y Register creados
  - ✅ Mínimo 6 ventanas (Dashboard, Árboles, Detalle, Centros, Usuarios, Alertas)
  - ✅ CRUD completo de Árboles
  - ✅ Consumo de API REST
  - ✅ Formularios con validaciones
  - ✅ Navegación clara (menú, cabecera)
  - ✅ Estilización coherente con Tailwind CSS

## Plugins de Vite

Este proyecto usa:

- [@vitejs/plugin-react](https://github.com/vitejs/vite-plugin-react) - Usa Babel para Fast Refresh

## Estado

En desarrollo

## Contacto

Proyecto Final DAM 2025-2026