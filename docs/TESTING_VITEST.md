# Testing Proyecto Árboles (Frontend)

Stack: Vitest 4.x + React Testing Library + @testing-library/user-event

Este documento describe la suite de tests del frontend de Proyecto Árboles. La suite cubre las funcionalidades críticas del sistema: autenticación de usuarios, control de acceso por roles (ADMIN y COORDINADOR), validación de formularios y comunicación con la API REST del backend.

Los tests están organizados en 7 archivos con un total de 21 tests, cubriendo desde lógica pura sin dependencias hasta flujos de UI completos con navegación.

---

## 1. Requisitos / Setup

**Node.js recomendado**: v20 o superior
**Instalar dependencias**:

```bash
npm install
```

Las dependencias de testing ya están incluidas en `package.json`:

| Paquete | Versión | Propósito |
|---------|---------|-----------|
| `vitest` | 4.0 | Runner de tests |
| `@testing-library/react` | 16 | Renderizar componentes React |
| `@testing-library/user-event` | 14 | Simular interacciones de usuario |
| `@testing-library/jest-dom` | 6 | Matchers adicionales (`toBeInTheDocument`, etc.) |
| `@vitest/coverage-v8` | 4.0 | Cobertura de código |
| `@vitest/ui` | 4.0 | Interfaz gráfica para tests |
| `jsdom` |  | Entorno DOM para tests (sin navegador) |

---

## 2. Cómo ejecutar tests

| Comando | Descripción |
|---------|-------------|
| `npm test -- run` | Ejecutar todos los tests una vez |
| `npm test -- run --coverage` | Todos los tests + informe de cobertura |
| `npm run test:ui` | Interfaz gráfica interactiva |
| `npm run test:watch` | Modo watch (re-ejecuta al guardar) |

**Ejecutar un archivo específico:**

```bash
npm test -- run src/utils/permissions.test.js
npm test -- run src/context/AuthContext.test.jsx
```

**Ejecutar tests por nombre** (flag `-t`):

```bash
npm test -- run -t "shows error when email"
```

---

## 3. Cobertura

**Generar informe:**

```bash
npm test -- run --coverage
```

**Ver informe HTML:**

```bash
# Abrir en el navegador
xdg-open coverage/index.html       # Linux
open coverage/index.html           # macOS
```

**Métricas actuales** (21 tests):

| Métrica | Resultado |
|---------|-----------|
| Statements | ~60% |
| Branches | ~46% |
| Functions | ~56% |
| Lines | ~61% |

Estas cifras son el resultado de una decisión de diseño deliberada: la suite se centra en las funcionalidades críticas del sistema (autenticación, permisos, validaciones de formulario, seguridad de rutas), no en alcanzar un porcentaje arbitrario. El código no cubierto corresponde principalmente a rutas lternativas de servicios con la misma estructura ya testeada (por ejemplo, `createArbol` o `updateArbol` siguen el mismo patrón que el `getArboles` ya testeado) y a ramas de carga y edición del `FormularioArbol` que requerirían tests de integración.

La carpeta `coverage/` está en `.gitignore` y no se sube al repositorio.

---

## 4. Qué se está testeando

### Tests 1-4. Lógica de permisos (`permissions.test.js`)

Lógica pura sin mocks. Verifica el sistema de roles del proyecto (solo ADMIN y COORDINADOR).

| # | Test | Qué verifica |
|---|------|--------------|
| 1 | `isAdminUser` → `true` para ADMIN | Un usuario con rol ADMIN es reconocido como administrador |
| 2 | `isAdminUser` → `false` para COORDINADOR | Un coordinador no tiene privilegios de admin |
| 3 | `isCoordinadorInCenter` → `true` para ADMIN en cualquier centro | ADMIN tiene acceso a todos los centros, aunque su lista esté vacía |
| 4 | `isCoordinadorInCenter` → `false` si no está asignado | COORDINADOR sin asignación al centro 2 no puede operar en él |

---

### Tests 5-8. Contexto de autenticación (`AuthContext.test.jsx`)

Mock de la instancia axios (`api.js`). Usa `renderHook` con `AuthProvider` como wrapper.

| # | Test | Qué verifica |
|---|------|--------------|
| 5 | `login('')` lanza error | La función `login` rechaza con `'Email y contraseña son requeridos'` si el email está vacío |
| 6 | Login exitoso guarda en localStorage | Tras un POST exitoso, el usuario queda en `localStorage` bajo la clave `'user'` |
| 7 | Error 401 lanza mensaje correcto | La respuesta HTTP 401 se traduce en `'Usuario o contraseña incorrecta'` (no el error genérico) |
| 8 | `logout()` limpia localStorage | Tras llamar a `logout()`, `localStorage.getItem('user')` devuelve `null` |

---

### Tests 9-10. Servicio de árboles (`arbolesService.test.js`)

Mock de `api.js`. Verifica que el servicio construye correctamente las peticiones HTTP.

| # | Test | Qué verifica |
|---|------|--------------|
| 9 | `getArboles` llama a `GET /arboles` | La URL es exactamente `/arboles` y se devuelve `response.data` |
| 10 | `deleteArbol` relanza el error original | En caso de fallo de red, el error llega sin transformar al componente llamante |

---

### Tests 11-12. Servicio de lecturas (`lecturasService.test.js`)

Mock de `api.js`. Verifica los dos comportamientos de `getUltimaLectura`: cuando no hay lecturas y cuando las hay.

| # | Test | Qué verifica |
|---|------|--------------|
| 11 | `getUltimaLectura` devuelve `null` sin lecturas | Si `content` está vacío, la función retorna `null` (no `undefined` ni error) |
| 12 | `getUltimaLectura` devuelve el primer elemento cuando hay lecturas | Si `content` tiene datos, devuelve `content[0]` correctamente |

---

### Tests 13-14. Formulario de árbol (`FormularioArbol.test.jsx`)

Renderizado con `MemoryRouter`. Mocks de servicios, `usePermissions` y `useAuth`. Se usa `fireEvent.submit(form)` para evitar que la validación nativa HTML5 bloquee el submit.

| # | Test | Qué verifica |
|---|------|--------------|
| 13 | Campos obligatorios vacíos muestran errores | Al enviar sin rellenar, aparecen `'El nombre es obligatorio'` y `'La especie es obligatoria'` |
| 14 | `umbralTempMin` >= `umbralTempMax` muestra error | Solo se activa si **ambos** campos tienen valor; muestra `'La temperatura mínima debe ser menor que la máxima'` |

---

### Test 15. Ruta protegida (`ProtectedRoute.test.jsx`)

Usa `MemoryRouter` con `Routes` reales para observar la navegación resultante.

| # | Test | Qué verifica |
|---|------|--------------|
| 15 | Usuario anónimo es redirigido a `/login` | Con `user: null`, el componente renderiza `<Navigate to="/login">` en lugar del contenido protegido |

---

### Tests 16-21. Página de Login (`Login.test.jsx`)

Mock de `useAuth` con `vi.fn()`. Combina interacción de usuario, validación, navegación y estado asíncrono.

| # | Test | Qué verifica |
|---|------|--------------|
| 16 | Renderiza inputs de email y contraseña | Los campos están en el DOM y son accesibles por su label |
| 17 | Email vacío muestra error | Sin rellenar el email, aparece `'El email es requerido'` |
| 18 | Contraseña vacía muestra error | Con email pero sin contraseña, aparece `'La contraseña es requerida'` |
| 19 | Error de API se muestra en pantalla | Si `login()` rechaza con un error, su mensaje se muestra en el Alert del formulario |
| 20 | Login exitoso navega a `/dashboard` | Tras un login correcto, el componente `Dashboard` se renderiza en lugar del Login |
| 21 | Botón desactivado mientras carga | Durante la llamada a `login()`, el botón muestra `'Iniciando sesión...'` y está `disabled` |

---

## 5. Guía para escribir tests

Todos los tests siguen el patrón AAA (Arrange-Act-Assert):

```javascript
it('descripción clara de lo que hace el test', async () => {
  // Arrange — preparar el escenario
  const mockData = { id: 1, nombre: 'Pino' };
  api.get.mockResolvedValue({ data: mockData });

  // Act — ejecutar la acción
  const result = await getArbolById(1);

  // Assert — verificar el resultado
  expect(api.get).toHaveBeenCalledWith('/arboles/1');
  expect(result).toEqual(mockData);
});
```

**Ejemplo real del proyecto**: test de validación de formulario (test 14):

```javascript
it('shows error when umbralTempMin is not less than umbralTempMax', async () => {
  // Arrange
  const user = userEvent.setup();
  renderForm();
  await screen.findByRole('option', { name: 'IES Test' }); // esperar async

  // Act — rellenar campos y enviar
  await user.type(screen.getByLabelText(/nombre del árbol/i), 'Test Árbol');
  await user.type(screen.getByLabelText(/especie/i), 'Pinus sylvestris');
  fireEvent.change(screen.getByLabelText(/fecha de plantación/i), { target: { value: '2020-01-01' } });
  await user.selectOptions(screen.getByRole('combobox'), '1');
  await user.type(screen.getByLabelText(/temperatura mínima/i), '30');
  await user.type(screen.getByLabelText(/temperatura máxima/i), '10');
  await user.click(screen.getByRole('button', { name: /crear árbol/i }));

  // Assert
  expect(await screen.findByText('La temperatura mínima debe ser menor que la máxima')).toBeInTheDocument();
});
```

**Reglas de nomenclatura:**

- `describe`: nombre del módulo o componente — `describe('AuthContext', () => ...)`
- `it`: frase que describe el comportamiento esperado — empieza con verbo en tercera persona
  - Correcto: `it('returns null when content array is empty', ...)`
  - Incorrecto: `it('test getUltimaLectura', ...)`

---

## 6. Mocks y aislamiento

### Mockear `api.js` (instancia axios)

```javascript
vi.mock('./api');             // en tests de servicios (mismo directorio)
vi.mock('../services/api');  // en tests de contexto

// En el test:
api.get.mockResolvedValue({ data: mockData });
api.post.mockRejectedValue({ response: { status: 401 } });
```

### Mockear un módulo con factory

Cuando se necesita controlar exactamente qué devuelve un hook o función:

```javascript
vi.mock('../../context/AuthContext', () => ({
  useAuth: () => ({ user: { id: 1, rol: 'ADMIN', centros: [] } }),
}));
```

### Mockear y configurar en cada test

Cuando el mock necesita valores distintos por test:

```javascript
vi.mock('../../context/AuthContext'); // auto-mock
import { useAuth } from '../../context/AuthContext';

beforeEach(() => {
  useAuth.mockReturnValue({ login });
});
```

### Mockear `localStorage`

jsdom incluye `localStorage` en el entorno. Solo hay que limpiarlo:

```javascript
afterEach(() => {
  vi.clearAllMocks();
  localStorage.clear();
});
```

### Limpiar mocks entre tests

Siempre incluir en `afterEach`:

```javascript
afterEach(() => vi.clearAllMocks());
```

Esto evita que el estado de un mock de un test afecte al siguiente.

---

## 7. Troubleshooting

### "Unable to find an element". El elemento no aparece

Usa `findBy*` (async) en lugar de `getBy*` (síncrono) para elementos que aparecen tras una operación asíncrona:

```javascript
// Incorrecto: puede fallar si el elemento aún no está en el DOM
expect(screen.getByText('Error')).toBeInTheDocument();

// Correcto: espera hasta que aparezca (timeout 1000ms por defecto)
expect(await screen.findByText('Error')).toBeInTheDocument();
```

### El formulario no llama a `onSubmit` al hacer clic en submit

Los inputs con `required` activan la validación nativa de jsdom, que bloquea el evento `submit`. Usa `fireEvent.submit(form)` para saltarla cuando necesites probar validación personalizada:

```javascript
// Incorrecto: jsdom bloquea el submit si hay inputs required vacíos
await user.click(screen.getByRole('button', { name: /enviar/i }));

// Correcto: dispara onSubmit directamente, saltando validación nativa
const { container } = render(<MiFormulario />);
fireEvent.submit(container.querySelector('form'));
```

### "Failed to resolve import" en el test de un servicio

Los archivos de test están colocados con el archivo que testean. Los imports son relativos al directorio del test, no al proyecto:

```
src/services/
  arbolesService.js
  arbolesService.test.js   ← está aquí
```

```javascript
// Correcto: mismo directorio
import { getArboles } from './arbolesService';
import api from './api';

// Incorrecto: ruta desde src/
import { getArboles } from './services/arbolesService';
```

### "Cannot read properties of undefined (reading 'mockReturnValue')"

El módulo no está siendo mockeado antes de que el import lo resuelva. Asegúrate de que `vi.mock(...)` está al nivel superior del archivo (Vitest lo hoista automáticamente):

```javascript
vi.mock('../../context/AuthContext'); // ← al nivel superior, fuera de describe/it
import { useAuth } from '../../context/AuthContext';
```

### El hook del contexto no funciona en el test

Usa `renderHook` con un `wrapper` que incluya el Provider:

```javascript
import { renderHook, act } from '@testing-library/react';
import { AuthProvider, useAuth } from '../context/AuthContext';

const wrapper = ({ children }) => <AuthProvider>{children}</AuthProvider>;

const { result } = renderHook(() => useAuth(), { wrapper });
```

### Aviso "An update to ... inside a test was not wrapped in act(...)"

Este aviso aparece cuando una promesa controlada en el test resuelve después de que el componente se desmonta. Es una advertencia de React, no un fallo del test. Ocurre en tests que dejan promesas pendientes al terminar (como el test 21 con `resolveLogin`).

El test sigue siendo correcto: hace su aserción antes de que la promesa resuelva y llama a `resolveLogin()` al final solo para liberar recursos. El aviso desaparece si se envuelve la resolución en `act`, pero no es necesario para la validez del test.

---

## 8. Estructura de archivos

Los tests se colocan junto al archivo que testean (convención del proyecto):

```
frontend/src/
├── constants/
│   └── roles.js
├── context/
│   ├── AuthContext.jsx
│   └── AuthContext.test.jsx        ← tests 5-8
├── hooks/
│   └── usePermissions.js
├── pages/
│   ├── arboles/
│   │   ├── FormularioArbol.jsx
│   │   └── FormularioArbol.test.jsx ← tests 13-14
│   └── login/
│       ├── Login.jsx
│       └── Login.test.jsx          ← tests 16-21
├── routes/
│   ├── ProtectedRoute.jsx
│   └── ProtectedRoute.test.jsx     ← test 15
├── services/
│   ├── api.js
│   ├── arbolesService.js
│   ├── arbolesService.test.js      ← tests 9-10
│   ├── lecturasService.js
│   └── lecturasService.test.js     ← tests 11-12
└── utils/
    ├── permissions.js
    └── permissions.test.js         ← tests 1-4
```
