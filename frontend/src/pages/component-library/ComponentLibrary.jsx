import { useState } from "react";
import Button from "../../components/common/Button";
import Input from "../../components/common/Input";
import Alert from "../../components/common/Alert";
import Spinner from "../../components/common/Spinner";

const ComponentLibrary = () => {
  const [inputValue, setInputValue] = useState("");
  const [showSuccessAlert, setShowSuccessAlert] = useState(true);
  const [loading, setLoading] = useState(false);

  const handleLoadingTest = () => {
    setLoading(true);
    setTimeout(() => setLoading(false), 2000);
  };

  const CodeBlock = ({ code }) => (
    <pre className="bg-gray-800 text-gray-100 p-4 rounded-md overflow-x-auto text-sm">
      <code>{code}</code>
    </pre>
  );

  return (
    <div className="min-h-screen bg-gray-100 p-8">
      <div className="max-w-6xl mx-auto">
        <div className="bg-white rounded-lg shadow-md p-8 mb-6">
          <h1 className="text-4xl font-bold text-gray-800 mb-3">
            Biblioteca de Componentes
          </h1>
          <p className="text-gray-600">
            Referencia visual y documentación de los componentes comunes reutilizables del proyecto.
          </p>
        </div>

        {/* Buttons */}
        <section className="bg-white rounded-lg shadow-md p-6 mb-6">
          <h2 className="text-2xl font-semibold mb-2">Button</h2>
          <p className="text-gray-600 mb-4">
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
          <h2 className="text-2xl font-semibold mb-2">Input</h2>
          <p className="text-gray-600 mb-4">
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
          <h2 className="text-2xl font-semibold mb-2">Alert</h2>
          <p className="text-gray-600 mb-4">
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
          <h2 className="text-2xl font-semibold mb-2">Spinner</h2>
          <p className="text-gray-600 mb-4">
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

        {/* Footer */}
        <div className="bg-green-50 border border-green-200 rounded-lg p-6 text-center">
          <p className="text-gray-700">
            Estos componentes están diseñados para ser reutilizables en todo el proyecto.
            <br />
            Documentación actualizada: {new Date().toLocaleDateString('es-ES')}
          </p>
        </div>
      </div>
    </div>
  );
};

export default ComponentLibrary;
