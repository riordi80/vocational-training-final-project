import { useState } from "react";
import { TreePine, School, Users, Plus, ArrowLeft } from "lucide-react";
import Button from "../../components/common/Button";
import Input from "../../components/common/Input";
import Alert from "../../components/common/Alert";
import Spinner from "../../components/common/Spinner";

const CodeBlock = ({ code }) => (
  <pre className="bg-gray-800 text-gray-100 p-4 rounded-md overflow-x-auto text-sm">
    <code>{code}</code>
  </pre>
);

const ComponentLibrary = () => {
  const [inputValue, setInputValue] = useState("");
  const [showSuccessAlert, setShowSuccessAlert] = useState(true);
  const [loading, setLoading] = useState(false);

  const handleLoadingTest = () => {
    setLoading(true);
    setTimeout(() => setLoading(false), 2000);
  };

  return (
    <div className="max-w-6xl mx-auto">
        <div className="bg-white rounded-lg shadow-md p-8 mb-6">
          <h1 className="text-4xl font-bold text-brand-primary mb-3">
            Biblioteca de Componentes
          </h1>
          <p className="text-brand-text">
            Referencia visual y documentación de los componentes comunes reutilizables del proyecto.
          </p>
        </div>

        {/* Tipografía */}
        <section className="bg-white rounded-lg shadow-md p-6 mb-6">
          <h2 className="text-2xl font-semibold text-brand-primary mb-2">Tipografía</h2>
          <p className="text-brand-text mb-6">
            El proyecto utiliza la fuente <strong>Outfit</strong> en todos los tamaños. Definida como fuente <code className="bg-gray-100 px-2 py-1 rounded">sans</code> por defecto en <code className="bg-gray-100 px-2 py-1 rounded">tailwind.config.js</code>.
          </p>
          <div className="space-y-4 mb-6">
            <div className="flex items-baseline gap-6 border-b border-gray-100 pb-4">
              <span className="text-xs text-gray-400 w-24 shrink-0">font-light</span>
              <p className="text-2xl font-light text-brand-text">Proyecto Árboles</p>
            </div>
            <div className="flex items-baseline gap-6 border-b border-gray-100 pb-4">
              <span className="text-xs text-gray-400 w-24 shrink-0">font-normal</span>
              <p className="text-2xl font-normal text-brand-text">Proyecto Árboles</p>
            </div>
            <div className="flex items-baseline gap-6 border-b border-gray-100 pb-4">
              <span className="text-xs text-gray-400 w-24 shrink-0">font-medium</span>
              <p className="text-2xl font-medium text-brand-text">Proyecto Árboles</p>
            </div>
            <div className="flex items-baseline gap-6 border-b border-gray-100 pb-4">
              <span className="text-xs text-gray-400 w-24 shrink-0">font-semibold</span>
              <p className="text-2xl font-semibold text-brand-text">Proyecto Árboles</p>
            </div>
            <div className="flex items-baseline gap-6 pb-2">
              <span className="text-xs text-gray-400 w-24 shrink-0">font-bold</span>
              <p className="text-2xl font-bold text-brand-text">Proyecto Árboles</p>
            </div>
          </div>
          <h3 className="text-lg font-semibold text-brand-primary mb-3">Jerarquía de títulos</h3>
          <div className="space-y-3 bg-brand-bg-warm p-6 rounded-lg">
            <p className="text-4xl font-bold text-brand-primary">Título principal — text-4xl bold</p>
            <p className="text-3xl font-bold text-brand-primary">Título de página — text-3xl bold</p>
            <p className="text-2xl font-semibold text-brand-primary">Título de sección — text-2xl semibold</p>
            <p className="text-xl font-semibold text-brand-primary">Subtítulo — text-xl semibold</p>
            <p className="text-base text-brand-text">Texto de cuerpo — text-base</p>
            <p className="text-sm text-brand-text">Texto secundario — text-sm</p>
            <p className="text-xs text-brand-text/60">Texto auxiliar — text-xs</p>
          </div>
        </section>

        {/* Paleta de Colores */}
        <section className="bg-white rounded-lg shadow-md p-6 mb-6">
          <h2 className="text-2xl font-semibold text-brand-primary mb-2">Paleta de Colores</h2>
          <p className="text-brand-text mb-6">
            Tokens de color definidos en <code className="bg-gray-100 px-2 py-1 rounded">tailwind.config.js</code>. Usar siempre estos tokens en lugar de colores directos de Tailwind.
          </p>

          {/* Colores de marca */}
          <h3 className="text-lg font-semibold mb-3">Marca</h3>
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-6">
            <div className="rounded-lg overflow-hidden border border-gray-200">
              <div className="h-20 bg-brand-primary"></div>
              <div className="p-3">
                <p className="font-mono text-sm font-semibold">brand-primary</p>
                <p className="font-mono text-xs text-gray-500">#2F473D</p>
                <p className="text-xs text-gray-600 mt-1">Header, footer izq., bloques oscuros</p>
              </div>
            </div>
            <div className="rounded-lg overflow-hidden border border-gray-200">
              <div className="h-20 bg-brand-secondary"></div>
              <div className="p-3">
                <p className="font-mono text-sm font-semibold">brand-secondary</p>
                <p className="font-mono text-xs text-gray-500">#67BB6A</p>
                <p className="text-xs text-gray-600 mt-1">Iconos decorativos y elementos secundarios</p>
              </div>
            </div>
            <div className="rounded-lg overflow-hidden border border-gray-200">
              <div className="h-20 bg-brand-accent"></div>
              <div className="p-3">
                <p className="font-mono text-sm font-semibold">brand-accent</p>
                <p className="font-mono text-xs text-gray-500">#FFB141</p>
                <p className="text-xs text-gray-600 mt-1">Decoración y texto sobre fondos oscuros</p>
              </div>
            </div>
          </div>

          {/* Colores de fondo */}
          <h3 className="text-lg font-semibold mb-3">Fondos</h3>
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-6">
            <div className="rounded-lg overflow-hidden border border-gray-200">
              <div className="h-20 bg-brand-bg-warm"></div>
              <div className="p-3">
                <p className="font-mono text-sm font-semibold">brand-bg-warm</p>
                <p className="font-mono text-xs text-gray-500">#FFF6E9</p>
                <p className="text-xs text-gray-600 mt-1">Fondo general de página</p>
              </div>
            </div>
            <div className="rounded-lg overflow-hidden border border-gray-200">
              <div className="h-20 bg-brand-bg-card"></div>
              <div className="p-3">
                <p className="font-mono text-sm font-semibold">brand-bg-card</p>
                <p className="font-mono text-xs text-gray-500">#FFEBCF</p>
                <p className="text-xs text-gray-600 mt-1">Interior de cards cálidas</p>
              </div>
            </div>
            <div className="rounded-lg overflow-hidden border border-gray-200">
              <div className="h-20 bg-brand-bg-green"></div>
              <div className="p-3">
                <p className="font-mono text-sm font-semibold">brand-bg-green</p>
                <p className="font-mono text-xs text-gray-500">#E5F7E5</p>
                <p className="text-xs text-gray-600 mt-1">Secciones alternativas verdes</p>
              </div>
            </div>
          </div>

          {/* Texto */}
          <h3 className="text-lg font-semibold mb-3">Texto</h3>
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-2">
            <div className="rounded-lg overflow-hidden border border-gray-200">
              <div className="h-20 bg-brand-text"></div>
              <div className="p-3">
                <p className="font-mono text-sm font-semibold">brand-text</p>
                <p className="font-mono text-xs text-gray-500">#1D2F2A</p>
                <p className="text-xs text-gray-600 mt-1">Texto cuerpo principal</p>
              </div>
            </div>
          </div>
        </section>

        {/* Buttons */}
        <section className="bg-white rounded-lg shadow-md p-6 mb-6">
          <h2 className="text-2xl font-semibold text-brand-primary mb-2">Button</h2>
          <p className="text-brand-text mb-4">
            Componente de botón reutilizable con múltiples variantes, tamaños y estados.
          </p>

          <h3 className="text-lg font-semibold mb-3">Props:</h3>
          <ul className="text-sm text-gray-700 mb-4 space-y-1">
            <li><code className="bg-gray-100 px-2 py-1 rounded">variant</code>: "primary" | "secondary" | "danger" | "outline" (default: "primary")</li>
            <li><code className="bg-gray-100 px-2 py-1 rounded">size</code>: "sm" | "md" | "lg" (default: "md")</li>
            <li><code className="bg-gray-100 px-2 py-1 rounded">disabled</code>: boolean (default: false)</li>
            <li><code className="bg-gray-100 px-2 py-1 rounded">fullWidth</code>: boolean (default: false)</li>
            <li><code className="bg-gray-100 px-2 py-1 rounded">onClick</code>: function</li>
          </ul>

          <h3 className="text-lg font-semibold mb-3">Variantes:</h3>
          <div className="mb-4">
            <div className="flex flex-wrap gap-3 mb-3">
              <Button variant="primary">Primary</Button>
              <Button variant="secondary">Secondary</Button>
              <Button variant="danger">Danger</Button>
              <Button variant="outline">Outline</Button>
            </div>
            <CodeBlock code={`<Button variant="primary">Primary</Button>
<Button variant="secondary">Secondary</Button>
<Button variant="danger">Danger</Button>
<Button variant="outline">Outline</Button>`} />
          </div>

          <h3 className="text-lg font-semibold mb-3">Tamaños:</h3>
          <div className="mb-4">
            <div className="flex flex-wrap items-center gap-3 mb-3">
              <Button size="sm">Small</Button>
              <Button size="md">Medium</Button>
              <Button size="lg">Large</Button>
            </div>
            <CodeBlock code={`<Button size="sm">Small</Button>
<Button size="md">Medium</Button>
<Button size="lg">Large</Button>`} />
          </div>

          <h3 className="text-lg font-semibold mb-3">Estados:</h3>
          <div className="mb-4">
            <div className="space-y-3 mb-3">
              <Button disabled>Disabled Button</Button>
              <Button fullWidth>Full Width Button</Button>
            </div>
            <CodeBlock code={`<Button disabled>Disabled Button</Button>
<Button fullWidth>Full Width Button</Button>`} />
          </div>
        </section>

        {/* Inputs */}
        <section className="bg-white rounded-lg shadow-md p-6 mb-6">
          <h2 className="text-2xl font-semibold text-brand-primary mb-2">Input</h2>
          <p className="text-brand-text mb-4">
            Campo de entrada reutilizable con label, validaciones y mensajes de error.
          </p>

          <h3 className="text-lg font-semibold mb-3">Props:</h3>
          <ul className="text-sm text-gray-700 mb-4 space-y-1">
            <li><code className="bg-gray-100 px-2 py-1 rounded">label</code>: string - Etiqueta del campo</li>
            <li><code className="bg-gray-100 px-2 py-1 rounded">type</code>: string - Tipo de input (default: "text")</li>
            <li><code className="bg-gray-100 px-2 py-1 rounded">placeholder</code>: string</li>
            <li><code className="bg-gray-100 px-2 py-1 rounded">value</code>: string</li>
            <li><code className="bg-gray-100 px-2 py-1 rounded">onChange</code>: function</li>
            <li><code className="bg-gray-100 px-2 py-1 rounded">error</code>: string - Mensaje de error</li>
            <li><code className="bg-gray-100 px-2 py-1 rounded">required</code>: boolean</li>
            <li><code className="bg-gray-100 px-2 py-1 rounded">disabled</code>: boolean</li>
          </ul>

          <h3 className="text-lg font-semibold mb-3">Ejemplos:</h3>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-4">
            <div>
              <Input
                id="demo1"
                label="Nombre"
                placeholder="Escribe tu nombre"
                value={inputValue}
                onChange={(e) => setInputValue(e.target.value)}
              />
            </div>
            <div>
              <Input
                id="demo2"
                label="Email requerido"
                type="email"
                placeholder="tu@email.com"
                required
              />
            </div>
            <div>
              <Input
                id="demo3"
                label="Con error"
                error="Este campo es obligatorio"
              />
            </div>
            <div>
              <Input
                id="demo4"
                label="Deshabilitado"
                value="No editable"
                disabled
              />
            </div>
          </div>
          <CodeBlock code={`<Input
  id="nombre"
  label="Nombre"
  placeholder="Escribe tu nombre"
  value={inputValue}
  onChange={(e) => setInputValue(e.target.value)}
/>

<Input
  label="Email requerido"
  type="email"
  required
/>

<Input
  label="Con error"
  error="Este campo es obligatorio"
/>

<Input
  label="Deshabilitado"
  value="No editable"
  disabled
/>`} />
        </section>

        {/* Alerts */}
        <section className="bg-white rounded-lg shadow-md p-6 mb-6">
          <h2 className="text-2xl font-semibold text-brand-primary mb-2">Alert</h2>
          <p className="text-brand-text mb-4">
            Componente para mostrar mensajes de éxito, error, advertencia e información.
          </p>

          <h3 className="text-lg font-semibold mb-3">Props:</h3>
          <ul className="text-sm text-gray-700 mb-4 space-y-1">
            <li><code className="bg-gray-100 px-2 py-1 rounded">type</code>: "success" | "error" | "warning" | "info" (default: "info")</li>
            <li><code className="bg-gray-100 px-2 py-1 rounded">message</code>: string - Mensaje a mostrar</li>
            <li><code className="bg-gray-100 px-2 py-1 rounded">onClose</code>: function - Función al cerrar el alert</li>
            <li><code className="bg-gray-100 px-2 py-1 rounded">dismissible</code>: boolean - Si se puede cerrar (default: true)</li>
          </ul>

          <h3 className="text-lg font-semibold mb-3">Tipos:</h3>
          <div className="space-y-3 mb-4">
            <Alert
              type="success"
              message={showSuccessAlert ? "Operación realizada con éxito" : ""}
              onClose={() => setShowSuccessAlert(false)}
            />
            <Alert
              type="error"
              message="Ha ocurrido un error al procesar la solicitud"
              dismissible={false}
            />
            <Alert
              type="warning"
              message="Advertencia: Revisa los datos antes de continuar"
            />
            <Alert
              type="info"
              message="Información importante sobre el sistema"
            />
          </div>
          <CodeBlock code={`<Alert
  type="success"
  message="Operación realizada con éxito"
  onClose={() => setAlert(null)}
/>

<Alert
  type="error"
  message="Ha ocurrido un error"
  dismissible={false}
/>

<Alert
  type="warning"
  message="Advertencia importante"
/>

<Alert
  type="info"
  message="Información del sistema"
/>`} />
        </section>

        {/* Spinners */}
        <section className="bg-white rounded-lg shadow-md p-6 mb-6">
          <h2 className="text-2xl font-semibold text-brand-primary mb-2">Spinner</h2>
          <p className="text-brand-text mb-4">
            Indicador de carga con diferentes tamaños y colores.
          </p>

          <h3 className="text-lg font-semibold mb-3">Props:</h3>
          <ul className="text-sm text-gray-700 mb-4 space-y-1">
            <li><code className="bg-gray-100 px-2 py-1 rounded">size</code>: "sm" | "md" | "lg" (default: "md")</li>
            <li><code className="bg-gray-100 px-2 py-1 rounded">color</code>: "green" | "white" | "gray" (default: "green")</li>
            <li><code className="bg-gray-100 px-2 py-1 rounded">text</code>: string - Texto debajo del spinner</li>
          </ul>

          <h3 className="text-lg font-semibold mb-3">Tamaños:</h3>
          <div className="mb-4">
            <div className="flex items-start gap-12 mb-6 bg-gray-50 p-6 rounded">
              <div className="text-center">
                <p className="text-sm text-gray-600 mb-3">Small</p>
                <Spinner size="sm" />
              </div>
              <div className="text-center">
                <p className="text-sm text-gray-600 mb-3">Medium</p>
                <Spinner size="md" />
              </div>
              <div className="text-center">
                <p className="text-sm text-gray-600 mb-3">Large</p>
                <Spinner size="lg" />
              </div>
            </div>
            <CodeBlock code={`<Spinner size="sm" />
<Spinner size="md" />
<Spinner size="lg" />`} />
          </div>

          <h3 className="text-lg font-semibold mb-3">Con texto:</h3>
          <div className="mb-4">
            <div className="bg-gray-50 p-6 rounded mb-3">
              <Spinner size="md" text="Cargando datos..." />
            </div>
            <CodeBlock code={`<Spinner size="md" text="Cargando datos..." />`} />
          </div>

          <h3 className="text-lg font-semibold mb-3">Ejemplo de uso:</h3>
          <div className="mb-4">
            <Button onClick={handleLoadingTest}>
              Simular Carga (2s)
            </Button>
            {loading && (
              <div className="mt-6 bg-gray-50 p-6 rounded">
                <Spinner size="lg" text="Procesando..." />
              </div>
            )}
          </div>
          <CodeBlock code={`const [loading, setLoading] = useState(false);

const handleClick = () => {
  setLoading(true);
  // Realizar operación...
  setTimeout(() => setLoading(false), 2000);
};

return (
  <>
    <Button onClick={handleClick}>Cargar</Button>
    {loading && <Spinner size="lg" text="Procesando..." />}
  </>
);`} />
        </section>

        {/* Cards */}
        <section className="bg-white rounded-lg shadow-md p-6 mb-6">
          <h2 className="text-2xl font-semibold text-brand-primary mb-2">Card</h2>
          <p className="text-brand-text mb-6">
            Patrón de card estándar del proyecto. Fondo blanco con sombra sobre el fondo general <code className="bg-gray-100 px-2 py-1 rounded">bg-brand-bg-warm</code>.
          </p>

          <h3 className="text-lg font-semibold mb-3">Card estática</h3>
          <div className="bg-brand-bg-warm p-6 rounded-lg mb-4">
            <div className="bg-white rounded-lg shadow-md p-6">
              <h2 className="text-2xl font-bold text-brand-primary mb-2">Título de la card</h2>
              <p className="text-brand-text">Contenido descriptivo de la card.</p>
            </div>
          </div>
          <CodeBlock code={`<div className="bg-white rounded-lg shadow-md p-6">
  <h2 className="text-2xl font-bold text-brand-primary mb-2">Título</h2>
  <p className="text-brand-text">Contenido.</p>
</div>`} />

          <h3 className="text-lg font-semibold mb-3 mt-6">Card navegable (con hover)</h3>
          <div className="bg-brand-bg-warm p-6 rounded-lg mb-4">
            <div className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition group cursor-pointer">
              <div className="flex items-center mb-4">
                <TreePine className="w-10 h-10 mr-4 text-brand-secondary shrink-0" />
                <h2 className="text-2xl font-bold text-brand-primary transition">
                  Árboles
                </h2>
              </div>
              <p className="text-brand-text">Descripción de la sección o contenido enlazado.</p>
            </div>
          </div>
          <CodeBlock code={`import { TreePine } from "lucide-react";

<Link to="/ruta" className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition group">
  <div className="flex items-center mb-4">
    <TreePine className="w-10 h-10 mr-4 text-brand-secondary shrink-0" />
    <h2 className="text-2xl font-bold text-brand-primary transition">
      Título
    </h2>
  </div>
  <p className="text-brand-text">Descripción.</p>
</Link>`} />
        </section>

        {/* Tablas */}
        <section className="bg-white rounded-lg shadow-md p-6 mb-6">
          <h2 className="text-2xl font-semibold text-brand-primary mb-2">Tabla</h2>
          <p className="text-brand-text mb-6">
            Patrón estándar de tabla. Cabecera con <code className="bg-gray-100 px-2 py-1 rounded">bg-brand-primary</code> y texto blanco, filas con separador <code className="bg-gray-100 px-2 py-1 rounded">divide-brand-bg-green</code> y hover sutil.
          </p>

          <div className="overflow-x-auto rounded-lg shadow mb-4">
            <table className="min-w-full divide-y divide-brand-bg-green">
              <thead className="bg-brand-primary">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-white uppercase tracking-wider">Nombre</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-white uppercase tracking-wider">Especie</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-white uppercase tracking-wider">Estado</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-white uppercase tracking-wider">Acciones</th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-brand-bg-green">
                {[
                  { nombre: "Pino canario", especie: "Pinus canariensis", activo: true },
                  { nombre: "Drago milenario", especie: "Dracaena draco", activo: true },
                  { nombre: "Tabaibera", especie: "Euphorbia regis-jubae", activo: false },
                ].map((row, i) => (
                  <tr key={i} className="hover:bg-brand-primary/5 cursor-pointer transition-colors">
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{row.nombre}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{row.especie}</td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${row.activo ? 'bg-brand-bg-green text-brand-primary' : 'bg-red-100 text-red-800'}`}>
                        {row.activo ? 'Activo' : 'Inactivo'}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm">
                      <Button variant="outline" size="sm">Ver Detalle</Button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <CodeBlock code={`<table className="min-w-full divide-y divide-brand-bg-green">
  <thead className="bg-brand-primary">
    <tr>
      <th className="px-6 py-3 text-left text-xs font-medium text-white uppercase tracking-wider">
        Columna
      </th>
    </tr>
  </thead>
  <tbody className="bg-white divide-y divide-brand-bg-green">
    {items.map((item) => (
      <tr
        key={item.id}
        className="hover:bg-brand-primary/5 cursor-pointer transition-colors"
        onClick={() => navigate(\`/ruta/\${item.id}\`)}
      >
        <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
          {item.campo}
        </td>
      </tr>
    ))}
  </tbody>
</table>`} />
        </section>

        {/* Iconos */}
        <section className="bg-white rounded-lg shadow-md p-6 mb-6">
          <h2 className="text-2xl font-semibold text-brand-primary mb-2">Iconos</h2>
          <p className="text-brand-text mb-6">
            El proyecto usa la librería <strong>Lucide React</strong> para todos los iconos. Instalación: <code className="bg-gray-100 px-2 py-1 rounded">npm install lucide-react</code>.
          </p>

          <h3 className="text-lg font-semibold mb-3">Iconos usados en el proyecto</h3>
          <div className="grid grid-cols-2 sm:grid-cols-4 gap-4 mb-6">
            {[
              { icon: <TreePine className="w-8 h-8" />, name: "TreePine", uso: "Árboles" },
              { icon: <School className="w-8 h-8" />, name: "School", uso: "Centros" },
              { icon: <Users className="w-8 h-8" />, name: "Users", uso: "Usuarios" },
              { icon: <Plus className="w-8 h-8" />, name: "Plus", uso: "Botón añadir" },
              { icon: <ArrowLeft className="w-8 h-8" />, name: "ArrowLeft", uso: "Botón volver" },
            ].map(({ icon, name, uso }) => (
              <div key={name} className="flex flex-col items-center gap-2 p-4 border border-gray-200 rounded-lg">
                <span className="text-brand-secondary">{icon}</span>
                <p className="font-mono text-xs font-semibold">{name}</p>
                <p className="text-xs text-gray-500">{uso}</p>
              </div>
            ))}
          </div>

          <h3 className="text-lg font-semibold mb-3">Tamaños recomendados</h3>
          <div className="flex items-end gap-8 bg-brand-bg-warm p-6 rounded-lg mb-4">
            <div className="flex flex-col items-center gap-2">
              <TreePine className="w-4 h-4 text-brand-secondary" />
              <p className="text-xs text-gray-500">w-4 h-4</p>
              <p className="text-xs text-gray-400">botones</p>
            </div>
            <div className="flex flex-col items-center gap-2">
              <TreePine className="w-6 h-6 text-brand-secondary" />
              <p className="text-xs text-gray-500">w-6 h-6</p>
              <p className="text-xs text-gray-400">inline</p>
            </div>
            <div className="flex flex-col items-center gap-2">
              <TreePine className="w-8 h-8 text-brand-secondary" />
              <p className="text-xs text-gray-500">w-8 h-8</p>
              <p className="text-xs text-gray-400">títulos</p>
            </div>
            <div className="flex flex-col items-center gap-2">
              <TreePine className="w-10 h-10 text-brand-secondary" />
              <p className="text-xs text-gray-500">w-10 h-10</p>
              <p className="text-xs text-gray-400">cards</p>
            </div>
          </div>

          <CodeBlock code={`import { TreePine, School, Users, Plus, ArrowLeft } from "lucide-react";

// Icono en título de página
<h1 className="text-3xl font-bold text-brand-primary flex items-center gap-3">
  <TreePine className="w-8 h-8 text-brand-secondary shrink-0" />
  Árboles
</h1>

// Icono en card navegable
<TreePine className="w-10 h-10 mr-4 text-brand-secondary shrink-0" />

// Icono en botón
<Button variant="primary">
  <Plus className="w-4 h-4 mr-1 inline" /> Añadir
</Button>

// Botón volver
<button onClick={() => navigate(-1)}>
  <ArrowLeft className="w-4 h-4" />
  Volver
</button>`} />
        </section>

        {/* Footer */}
        <div className="bg-brand-bg-green border border-brand-secondary rounded-lg p-6 text-center">
          <p className="text-gray-700">
            Estos componentes están diseñados para ser reutilizables en todo el proyecto.
            <br />
            Documentación actualizada: {new Date().toLocaleDateString('es-ES')}
          </p>
        </div>
    </div>
  );
};

export default ComponentLibrary;
