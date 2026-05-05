import { Link } from "react-router-dom";
import { useState, useEffect } from "react";
import { useAuth } from "../../context/AuthContext";
import { School, TreePine, Leaf, Cpu, Users, Heart } from "lucide-react";
import { getCentros } from "../../services/centrosService";
import { getArboles } from "../../services/arbolesService";
import { getDispositivos } from "../../services/dispositivosService";

const Dashboard = () => {
  const { user } = useAuth();
  const [numCentros, setNumCentros] = useState(null);
  const [numArboles, setNumArboles] = useState(null);
  const [co2Total, setCo2Total] = useState(null);
  const [dispositivosActivos, setDispositivosActivos] = useState(null);

  useEffect(() => {
    getCentros().then(data => setNumCentros(data.length)).catch(() => setNumCentros('—'));
    getDispositivos().then(data => setDispositivosActivos(data.filter(d => d.activo).length)).catch(() => setDispositivosActivos('—'));
    getArboles().then(data => {
      setNumArboles(data.reduce((sum, a) => sum + (a.cantidad ?? 1), 0));
      const co2 = data.reduce((sum, a) => sum + ((a.absorcionCo2Anual ?? 0) * (a.cantidad ?? 1)), 0);
      setCo2Total(co2.toFixed(1));
    }).catch(() => {
      setNumArboles('—');
      setCo2Total('—');
    });
  }, []);

  return (
    <div className="max-w-4xl mx-auto">
      {/* Bienvenida */}
      <div className="bg-white rounded-lg shadow-md p-6 mb-6">
        <h1 className="text-3xl font-bold text-brand-primary mb-2">
          {user ? `¡Bienvenido, ${user.nombre}!` : '¡Bienvenido a Proyecto Árboles!'}
        </h1>
        <p className="text-brand-text">
          {user
            ? 'Gestiona los árboles y centros educativos desde tu panel de control.'
            : 'Explora los árboles y centros educativos. Inicia sesión para gestionar contenido.'}
        </p>
      </div>

      {/* Métricas */}
      <div className="bg-brand-primary rounded-lg shadow-md p-5 mb-6 grid grid-cols-2 sm:grid-cols-4 gap-4">
        <div className="flex items-center gap-3">
          <School className="w-7 h-7 text-brand-bg-green shrink-0" />
          <div>
            <p className="text-2xl font-bold text-white leading-none">{numCentros ?? '…'}</p>
            <p className="text-xs text-white/60 mt-1">Centros educativos</p>
          </div>
        </div>
        <div className="flex items-center gap-3">
          <TreePine className="w-7 h-7 text-brand-bg-green shrink-0" />
          <div>
            <p className="text-2xl font-bold text-white leading-none">{numArboles ?? '…'}</p>
            <p className="text-xs text-white/60 mt-1">Árboles plantados</p>
          </div>
        </div>
        <div className="flex items-center gap-3">
          <Cpu className="w-7 h-7 text-brand-bg-green shrink-0" />
          <div>
            <p className="text-2xl font-bold text-white leading-none">{dispositivosActivos ?? '…'}</p>
            <p className="text-xs text-white/60 mt-1">Dispositivos activos</p>
          </div>
        </div>
        <div className="flex items-center gap-3">
          <Leaf className="w-7 h-7 text-brand-bg-green shrink-0" />
          <div>
            <p className="text-2xl font-bold text-white leading-none">{co2Total ?? '…'}</p>
            <p className="text-xs text-white/60 mt-1">kg CO₂ absorbidos/año</p>
          </div>
        </div>
      </div>

      {/* Tarjetas de acceso rápido */}
      <div className="grid grid-cols-1 gap-6">
        {/* Tarjeta Centros */}
        <Link
          to="/centros"
          className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition group"
        >
          <div className="flex items-center mb-4">
            <School className="w-10 h-10 mr-4 text-brand-secondary shrink-0" />
            <h2 className="text-2xl font-bold text-brand-primary transition">
              Centros Educativos
            </h2>
          </div>
          <p className="text-brand-text">
            Administra los centros educativos y consulta los árboles asociados a cada uno.
          </p>
        </Link>

        {/* Tarjetas externas */}
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-6">
          <a
            href="https://proyectoarboles.org/#contacto"
            target="_blank"
            rel="noopener noreferrer"
            className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition"
          >
            <div className="flex items-start gap-4 mb-3">
              <Users className="w-10 h-10 text-brand-secondary shrink-0" />
              <h2 className="text-xl font-bold text-brand-primary">
                Súmate al proyecto
              </h2>
            </div>
            <p className="text-brand-text font-light">
              Si eres directivo o responsable de un centro, solicita la plantación de árboles y transforma tu colegio en un entorno más verde, saludable y sostenible.
            </p>
          </a>

          <a
            href="https://proyectoarboles.org/#contacto"
            target="_blank"
            rel="noopener noreferrer"
            className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition"
          >
            <div className="flex items-start gap-4 mb-3">
              <Heart className="w-10 h-10 text-brand-secondary shrink-0" />
              <h2 className="text-xl font-bold text-brand-primary">
                Apadrina un árbol
              </h2>
            </div>
            <p className="text-brand-text font-light">
              Personas, empresas y entidades pueden sumarse apadrinando un árbol: un legado vivo que genera sombra, oxígeno e impacto real frente al cambio climático.
            </p>
          </a>
        </div>
      </div>

      {/* Descripción del proyecto */}
      <div className="bg-white rounded-lg shadow-md p-6 mt-6 space-y-3 text-brand-text leading-relaxed font-light text-justify">
        <p>
          El <span className="font-medium text-brand-primary">Proyecto Árboles</span> es una iniciativa impulsada por la <span className="font-medium">Fundación Sergio Alonso</span>, la <span className="font-medium">Fundación Acuorum</span> y la <span className="font-medium">Fundación Foresta</span> que promueve la educación ambiental y la acción climática a través de la plantación de árboles en centros educativos de Canarias.
        </p>
        <p>
          Mediante la creación de espacios de sombra en colegios e institutos, el proyecto mejora el bienestar de la comunidad educativa, transforma los entornos escolares y refuerza valores como el respeto por el medioambiente y la sostenibilidad.
        </p>
        <p>
          La iniciativa se completa con talleres de educación ambiental dirigidos al alumnado, donde docentes y estudiantes descubren el papel clave de los árboles frente al cambio climático, su capacidad para absorber CO₂ y las prácticas necesarias para su cuidado y conservación.
        </p>
      </div>

      {/* Info del usuario */}
      {user && (
        <div className="bg-white rounded-lg shadow-md p-4 mt-6 text-center">
          <p className="text-sm text-gray-700">
            <span className="font-medium">Rol:</span> {user.rol} |
            <span className="font-medium ml-3">Email:</span> {user.email}
          </p>
        </div>
      )}
    </div>
  );
};

export default Dashboard;
