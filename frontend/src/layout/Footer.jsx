const Footer = () => {
    return (
        <footer className="w-full text-white overflow-x-hidden">

            {/* Fila principal */}
            <div className="flex flex-col lg:flex-row w-full">

                {/* Col 1 — Logo + descripción */}
                <div className="bg-brand-primary lg:w-5/12 flex flex-row items-center gap-6 px-10 py-6 min-w-0">
                    <img
                        src="/pa-logotipo-footer.png"
                        alt="Proyecto Árboles"
                        className="h-20 w-auto shrink-0"
                    />
                    <p className="text-xs text-white/70 leading-relaxed min-w-0">
                        El Proyecto Árboles nace con el propósito de concienciar a la comunidad
                        educativa y a la sociedad sobre la importancia de los árboles para combatir
                        el cambio climático y mejorar la calidad de vida.
                    </p>
                </div>

                {/* Col 2 — Navegación + contacto */}
                <div className="bg-brand-primary lg:w-4/12 flex flex-col justify-center px-10 py-6 gap-3 border-t lg:border-t-0 lg:border-l border-white/10 min-w-0">
                    <nav className="flex flex-row flex-wrap gap-x-5 gap-y-1">
                        <a href="#" className="text-sm text-brand-accent hover:text-brand-secondary tracking-wide font-semibold transition">El proyecto</a>
                        <a href="#" className="text-sm text-brand-accent hover:text-brand-secondary tracking-wide font-semibold transition">Plantaciones</a>
                        <a href="#" className="text-sm text-brand-accent hover:text-brand-secondary tracking-wide font-semibold transition">Actualidad</a>
                        <a href="#" className="text-sm text-brand-accent hover:text-brand-secondary tracking-wide font-semibold transition">Contacto</a>
                    </nav>
                    <div className="flex flex-wrap items-center gap-4">
                        <a href="tel:+34667894205" className="text-sm text-white/70 hover:text-brand-accent transition">
                            +34 667 894 205
                        </a>
                        <a href="mailto:info@proyectoarboles.org" className="text-sm text-white/70 hover:text-brand-accent transition">
                            info@proyectoarboles.org
                        </a>
                    </div>
                </div>

                {/* Col 3 — Impulsado por + logos */}
                <div className="bg-brand-secondary lg:w-3/12 flex flex-col items-center justify-center px-10 py-6 gap-3">
                    <p className="text-sm font-semibold text-white tracking-wide">
                        Impulsado por
                    </p>
                    <div className="flex flex-row flex-wrap justify-center items-center gap-x-6 gap-y-3">
                        <img src="/pa-fsa-blanco.png"     alt="Fundación Sergio Alonso" className="h-6" />
                        <img src="/pa-foresta-blanco.png" alt="Foresta"                 className="h-6" />
                        <img src="/pa-acuorum-blanco.png" alt="Acuorum"                 className="h-6" />
                    </div>
                </div>

            </div>

            {/* Barra de copyright */}
            <div className="bg-brand-primary border-t border-white/20 px-10 py-2 flex flex-wrap items-center gap-3">
                <p className="text-xs text-white/60">© 2025 – Fundación Sergio Alonso</p>
                <span className="text-white/30">|</span>
                <a href="#" className="text-xs text-white/60 hover:text-brand-accent transition">Aviso Legal</a>
                <span className="text-white/30">|</span>
                <a href="#" className="text-xs text-white/60 hover:text-brand-accent transition">Política de Privacidad</a>
            </div>

        </footer>
    );
};

export default Footer;
