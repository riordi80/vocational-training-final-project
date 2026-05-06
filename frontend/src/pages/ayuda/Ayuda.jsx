import { useState } from "react";
import { ChevronDown, ChevronUp, HelpCircle, LogIn, LayoutDashboard, School, TreePine, Cpu, Bell, MessageCircleQuestion } from "lucide-react";

const SECTIONS = [
  {
    id: "login",
    icon: LogIn,
    title: "Inicio de sesión y registro",
    items: [
      {
        q: "¿Cómo inicio sesión?",
        a: "Introduce tu email y contraseña en la pantalla de Login y pulsa «Iniciar Sesión». Si tus credenciales son correctas accederás al Dashboard.",
      },
      {
        q: "Mensajes de error habituales al iniciar sesión",
        a: (
          <ul className="list-disc pl-5 space-y-1">
            <li><strong>«Usuario o contraseña incorrecta»</strong> — Verifica que email y contraseña son correctos.</li>
            <li><strong>«Usuario no activo»</strong> — Tu cuenta está desactivada; contacta al administrador.</li>
            <li><strong>«Error al iniciar sesión»</strong> — El servidor puede estar en cold start (primera carga del día). Espera 30-60 segundos y vuelve a intentarlo.</li>
          </ul>
        ),
      },
      {
        q: "¿Cómo me registro?",
        a: "Haz clic en «Registrarse» desde la pantalla de login. Completa nombre completo, email, contraseña y confirmación de contraseña. Si el registro es correcto, la app inicia sesión automáticamente. Los nuevos usuarios se registran como COORDINADOR por defecto.",
      },
      {
        q: "¿Por qué no puedo registrarme con mi email?",
        a: "El email ya existe en el sistema. Usa la opción de inicio de sesión o contacta al administrador si crees que es un error.",
      },
    ],
  },
  {
    id: "navegacion",
    icon: LayoutDashboard,
    title: "Navegación",
    items: [
      {
        q: "¿Cómo navego en escritorio?",
        a: "Encontrarás una barra horizontal en la parte superior con: Logo (lleva al Dashboard), Dashboard, Centros, Usuarios (solo visible para ADMIN) y tu perfil con la opción de cerrar sesión.",
      },
      {
        q: "¿Cómo navego en móvil o tablet?",
        a: "Pulsa el icono de menú (≡) en la esquina superior derecha. Se desplegará un menú con las mismas opciones que en escritorio.",
      },
      {
        q: "No veo la sección «Usuarios» en el menú",
        a: "La sección Usuarios solo es visible para cuentas con rol ADMIN. Si eres COORDINADOR, no tienes acceso a la gestión de usuarios.",
      },
    ],
  },
  {
    id: "dashboard",
    icon: LayoutDashboard,
    title: "Dashboard",
    items: [
      {
        q: "¿Qué muestra el Dashboard?",
        a: (
          <ul className="list-disc pl-5 space-y-1">
            <li><strong>Bienvenida personalizada</strong> con tu nombre y rol.</li>
            <li><strong>4 métricas</strong>: centros educativos, árboles plantados, dispositivos activos y kg de CO₂ absorbidos al año.</li>
            <li><strong>Acceso rápido</strong> a la sección Centros Educativos.</li>
            <li><strong>Tarjetas externas</strong>: enlaces para sumarse al proyecto o apadrinar un árbol.</li>
          </ul>
        ),
      },
      {
        q: "Los datos del Dashboard no se cargan",
        a: "El servidor puede estar arrancando tras un periodo de inactividad (cold start en Render). Espera 30-60 segundos y recarga la página con F5.",
      },
    ],
  },
  {
    id: "centros",
    icon: School,
    title: "Centros educativos",
    items: [
      {
        q: "¿Cómo veo los centros educativos?",
        a: "Haz clic en «Centros» en el menú de navegación. Verás el listado con nombre, población e isla de cada centro. Haz clic en un centro para ver su detalle.",
      },
      {
        q: "¿Qué información tiene el detalle de un centro?",
        a: "El detalle muestra los datos del centro, la tabla de árboles asociados y los dispositivos IoT registrados. Desde aquí puedes gestionar árboles y dispositivos si tienes permisos.",
      },
      {
        q: "¿Cómo creo o edito un centro?",
        a: "Necesitas rol ADMIN. Desde el listado de centros pulsa «Nuevo centro». Para editar, entra al detalle del centro y usa el botón de edición.",
      },
    ],
  },
  {
    id: "arboles",
    icon: TreePine,
    title: "Árboles",
    items: [
      {
        q: "¿Dónde gestiono los árboles?",
        a: "Los árboles se gestionan desde el detalle de cada centro educativo, no tienen sección propia en el menú. Ve a Centros → selecciona un centro → sección Árboles del Centro.",
      },
      {
        q: "¿Qué información tiene el detalle de un árbol?",
        a: "Nombre común y científico, especie, cantidad de ejemplares, fecha de plantación, absorción de CO₂ anual y ubicación en el mapa.",
      },
      {
        q: "¿Cómo creo o edito un árbol?",
        a: "Necesitas rol ADMIN o COORDINADOR. Desde el detalle del centro pulsa «Añadir árbol». Para editar, entra al detalle del árbol y usa el botón de edición.",
      },
    ],
  },
  {
    id: "dispositivos",
    icon: Cpu,
    title: "Dispositivos IoT",
    items: [
      {
        q: "¿Qué es un dispositivo IoT?",
        a: "Es un sensor ESP32 instalado en un centro que mide temperatura, humedad, CO₂, humedad del suelo y luz. Los datos se envían automáticamente al sistema.",
      },
      {
        q: "¿Cómo veo las lecturas de un dispositivo?",
        a: "Ve al detalle del centro → sección Dispositivos → haz clic en el dispositivo → «Ver histórico». Podrás filtrar por periodo y ver gráficas de evolución.",
      },
      {
        q: "¿Cómo registro un nuevo dispositivo?",
        a: "Necesitas rol ADMIN o COORDINADOR. Desde el detalle del centro pulsa «Añadir dispositivo». Introduce el nombre y la dirección MAC del ESP32.",
      },
      {
        q: "¿Cómo configuro los umbrales de alerta?",
        a: "Entra al detalle del dispositivo y edita los valores de umbral para cada sensor. Cuando una lectura supere el umbral configurado, se generará una alerta automáticamente.",
      },
    ],
  },
  {
    id: "alertas",
    icon: Bell,
    title: "Alertas y notificaciones",
    items: [
      {
        q: "¿Qué es una alerta?",
        a: "Una alerta se genera automáticamente cuando una lectura de un sensor supera el umbral configurado para ese dispositivo. Puede tener estado ACTIVA o RESUELTA.",
      },
      {
        q: "¿Dónde veo las alertas?",
        a: "Las alertas están disponibles en el detalle del dispositivo. Cada alerta muestra el sensor afectado, el valor registrado y la fecha.",
      },
      {
        q: "¿Cómo marco una alerta como resuelta?",
        a: "Entra al detalle de la alerta y cambia su estado a RESUELTA una vez hayas actuado sobre el problema detectado.",
      },
    ],
  },
  {
    id: "faq",
    icon: MessageCircleQuestion,
    title: "Preguntas frecuentes",
    items: [
      {
        q: "¿Por qué la aplicación tarda en cargar la primera vez?",
        a: "El servidor backend está alojado en Render con plan gratuito. Cuando lleva un tiempo sin recibir peticiones entra en «reposo». La primera carga del día puede tardar entre 30 y 60 segundos. Espera y recarga la página.",
      },
      {
        q: "¿Qué rol tengo al registrarme?",
        a: "Los nuevos usuarios se registran como COORDINADOR. Solo un administrador puede elevar una cuenta a ADMIN.",
      },
      {
        q: "¿Qué navegadores son compatibles?",
        a: "Chrome, Firefox, Safari y Edge en sus versiones más recientes. Se recomienda Chrome o Firefox para la mejor experiencia.",
      },
      {
        q: "¿Cómo contacto al administrador?",
        a: "Usa el formulario de contacto disponible en el Dashboard o en la sección de información del proyecto.",
      },
    ],
  },
];

const AccordionSection = ({ section }) => {
  const [openIndex, setOpenIndex] = useState(null);
  const Icon = section.icon;

  const toggle = (i) => setOpenIndex(openIndex === i ? null : i);

  return (
    <div className="bg-white rounded-xl shadow-sm border border-brand-bg-card overflow-hidden">
      <div className="flex items-center gap-3 px-6 py-4 bg-brand-primary">
        <Icon className="w-5 h-5 text-brand-accent shrink-0" />
        <h2 className="text-lg font-bold text-white">{section.title}</h2>
      </div>

      <div className="divide-y divide-brand-bg-card">
        {section.items.map((item, i) => (
          <div key={i}>
            <button
              onClick={() => toggle(i)}
              className="w-full flex items-center justify-between px-6 py-4 text-left hover:bg-brand-bg-warm transition"
              aria-expanded={openIndex === i}
            >
              <span className="font-semibold text-brand-text pr-4">{item.q}</span>
              {openIndex === i
                ? <ChevronUp className="w-4 h-4 text-brand-accent shrink-0" />
                : <ChevronDown className="w-4 h-4 text-brand-accent shrink-0" />
              }
            </button>
            {openIndex === i && (
              <div className="px-6 pb-4 pt-1 text-brand-text text-sm leading-relaxed bg-brand-bg-green/30">
                {item.a}
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

const Ayuda = () => (
  <div className="max-w-3xl mx-auto space-y-6">
    <div className="bg-white rounded-xl shadow-sm p-6 flex items-start gap-4 border border-brand-bg-card">
      <HelpCircle className="w-10 h-10 text-brand-accent shrink-0 mt-0.5" />
      <div>
        <h1 className="text-2xl font-bold text-brand-primary mb-1">Centro de ayuda</h1>
        <p className="text-brand-text text-sm">
          Encuentra respuestas sobre el uso de la aplicación web. Si no encuentras lo que buscas,
          contacta al administrador del sistema desde el Dashboard.
        </p>
      </div>
    </div>

    {SECTIONS.map((section) => (
      <AccordionSection key={section.id} section={section} />
    ))}
  </div>
);

export default Ayuda;
