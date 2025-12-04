Proyecto 5: Proyecto Fundación Sergio Alonso.
Objetivo:
El curso pasado la Fundación Sergio Alonso plantó árboles en distintos centros educativos, por lo que quieren desarrollar una aplicación en la que gracias a unos sensores se obtenga la humedad del árbol y poder monitorizar y gestionar esos datos.
Dicho proyecto tratan de conectarlo con otro proyecto llamado proyecto Árboles que en breve crecerá más. El alumnado tendrá, a su vez, que ir al terreno, instalarlo y conectarlo a una web que están creando y así ver los datos en 'real'.

Componentes:
Componentes posibles: imagen adjunta en carpeta.
Del microcontrolador ESP32 hay varios modelos pero las librerías se pueden utilizar las genéricas de arduino.
No va a ser necesario que la alimentación del ESP32 sea autónoma. Entonces se va a necesitar llevar los portátiles y demás.
Se establecen los siguientes componentes:
    • Placa la ESP32 -S3 (N16R8)
    • Sensores de humedad a elegir entre DTH22 a tres pines o DTH11; o sensor STH40
    • Sensor de lectura de CO2, MH-1
    • Y a parte también un controlador de voltaje para ajustar a las entradas de los distintos sensores

Participantes:
    • Ortiz Díaz, Pedro Ricardo
    • Pérez García, Enrique

Requisitos [módulo]:
[PGV]
    • [Noviembre] Realizar dos endpoints con, al menos, la utilización de cada uno de los métodos HTTP vistos en clase (GET, POST, PUT y DELETE), realizando las validaciones de datos acorde a los datos del proyecto desarrollado. Dichos endpoints deben ser frutos de una relación entre dos entidades de uno a muchos.
    • [Diciembre, si da tiempo] Realizar dos o tres endpoints con, al menos, la utilización de cada uno de los métodos HTTP vistos en clase (GET, POST, PUT y DELETE), realizando las validaciones de datos acorde a los datos del proyecto desarrollado. Puede repetirse alguno de los endpoints anteriores. Dichos endpoints deben ser frutos de una relación entre dos entidades de muchos a muchos.
[DAD]
Estructura del proyecto:
    • Componentes organizados y reutilizables.
    • Rutas implementadas con React Router Dom
    • Crear ventanas de login y register (aunque no sean funcionales)
    • MINIMO 4 ventanas sin contar el login y register
Consumo de la API REST:
    • Mínimo un CRUD
Diseño de interfaz:
    • El alumno es libre de estilizar sus componentes (Uso de CSS Modules, Tailwind o styled-components), siempre y cuando tengan relación con lo que muestran
    • Formularios funcionales con validaciones.
    • Navegación clara y visual (menú, cabecera, etc.).

REQUISITOS MINIMOS DAD:

**REQUISITOS MÍNIMOS**
Estructura del proyecto:

- Componentes organizados y reutilizables.
- Rutas implementadas con React Router Dom
- Crear ventanas de login y register (aunque no sean funcionales)
- MINIMO 4 ventanas sin contar el login y register

Consumo de la API REST:

- Tantos CRUD como sean necesarios.

Diseño de interfaz:

- El alumno es libre de estilizar sus  componentes (Uso de CSS Modules, Tailwind o styled-components), siempre y cuando tengan relación con lo que muestran
- Formularios funcionales con validaciones.
- Navegación clara y visual (menú, cabecera, etc.).

**Requisitos funcionales y no funcionales:**

-Responsive 

-Logueo/Register y persistencia de datos con LocalStorage/SessionStorage/Cookies

-Y poder desplegar la app en Vercel (o otras que conozcáis)

-Navegación Dinámica

-Gestión de los CRUD establecidos 

-Establecer roles

-Dar feedback al usuario: mensajes de existo, mensajes de error y posibles soluciones

[AED]
Modelo de Datos
    • Diseña esquema E/R

    • Transforma E/R a UML
    
    • Transforma UML a Relacional
    
    • Describe y define los campos, claves y relaciones
ORM
    • Definición del mapeo objeto–relacional (ORM).

    • Creación de al menos dos clases de entidad correspondientes a dos tablas del modelo.
    
    • Implementación de al menos una relación mapeada entre las clases.
Documentación
    • Descripción del sistema.

    • Documentación del ERS y/o funciones del sistema.
    
    • Documentación del modelo de datos.
    
    • Elaboración del manual de instalación.
    
    • Elaboración del manual de usuario.
Aplicación
    • Visualización en el frontend de los datos almacenados en la base de datos.
Gestor de tareas Git.
    • Deben usarlo

[PGL]
Haciendo uso de Android Studio, generar una app en lenguaje Java con al menos las siguientes actividades:
    • Actividad para listar los árboles plantados con sensor establecido, según el centro educativo.
    • Actividad para visualizar los detalles recogidos del árbol como especie, fecha_plantacion, humedad (tiempo-real), temperatura (tiempo-real), fecha (tiempo-real), hora (tiempo-real), etc.
    • Modificar datos del árbol, pero no los recogidos por los sensores que serán en tiempo real.
    • Eliminar árbol.
En esta intro del proyecto los datos en tiempo-real serán simulados.
