/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      fontFamily: {
        sans: ['Outfit', 'sans-serif'],
      },
      colors: {
        brand: {
          primary:    '#2F473D', // Verde bosque — header, footer izq, bloques oscuros
          secondary:  '#67BB6A', // Verde medio — footer der, estado activo
          accent:     '#FFB141', // Ámbar — links, títulos, CTAs
          'bg-warm':  '#FFF6E9', // Crema suave — fondo general
          'bg-card':  '#FFEBCF', // Crema saturada — interior de cards
          'bg-green': '#E5F7E5', // Verde suave — secciones alternativas
          text:       '#1D2F2A', // Verde muy oscuro — texto cuerpo
        },
      },
    },
  },
  plugins: [],
}

