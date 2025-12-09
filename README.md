=============================================================================
PROYECTO: Procesamiento Funcional de Tweets (Java)
AUTOR: Gael Valerio
FECHA: 8 de diciembre 2025
=============================================================================

1. DESCRIPCIÓN DEL PROYECTO
Este proyecto es una aplicación Java desarrollada bajo el paradigma de 
programación funcional. Su objetivo es leer un gran conjunto de datos 
(archivo CSV con tweets), aplicar transformaciones de limpieza de texto, 
realizar cálculos estadísticos (conteo y promedios) y generar reportes 
de salida.

El flujo principal utiliza Streams y Lambdas para evitar bucles tradicionales 
y garantizar un código declarativo y modular.

-----------------------------------------------------------------------------

2. ESTRUCTURA DE PAQUETES Y CARPETAS
El proyecto sigue una arquitectura de capas definida:

RAÍZ DEL PROYECTO
├── src/
│   ├── app/       -> Contiene la clase principal de arranque y orquestación.
│   ├── model/     -> Contiene la definición de datos).
│   ├── service/   -> Contiene la lógica de negocio y métodos funcionales.
│   └── report/    -> Contiene la lógica de persistencia (guardado de archivos).
│
├── data/          -> Ubicación del archivo de entrada (twitters.csv).
├── output/        -> Destino de los reportes generados.
├── README.txt     -> Documentación del proyecto.
└── DECLARATORIA_IA.txt -> Declaración de herramientas utilizadas.

-----------------------------------------------------------------------------

3. MAPA DE IMPLEMENTACIÓN FUNCIONAL
A continuación se detalla dónde se implementan los requisitos funcionales 
solicitados en la rúbrica:

A) SUPPLIER <T>
   - Ubicación: Clase service.TweetsAnalyzeService
   - Método: crearLectorTweets(String rutaArchivo)
   - Descripción: Se utiliza para proveer el flujo de datos 
     desde el archivo CSV hacia la aplicación. Retorna un Supplier<List<Tweet>>

B) FUNCTION <T, R>
   - Ubicación: Clase service.TweetsAnalyzeService y uso en app.Main
   - Métodos: 
     1. obtenerFuncionLimpieza(): Define la lógica de transformación 
        (Mayúsculas, eliminar @, eliminar #).
     2. transformarTweets(List, Function): Aplica la función a la lista.
   - Descripción: Transforma un objeto Tweet en otro nuevo con el texto limpio.

C) CONSUMER <T>
   - Ubicación: Clase service.TweetsAnalyzeService y app.Main
   - Método: procesarTweets(List, Function, Consumer)
   - Descripción: Recibe los tweets ya transformados y ejecuta una acción final 
     que no retorna valor (como imprimir en consola o preparar datos para reporte).

D) RUNNABLE
   - Ubicación: Clase app.Main
   - Método: crearPipelinePrincipal(...) / lambda en main
   - Descripción: Encapsula todo el flujo de ejecución (Carga -> Transformación 
     -> Análisis -> Reporte) en una sola tarea coherente para ser ejecutada.

E) STREAMS API
   - Ubicación: Clase service.TweetsAnalyzeService
   - Uso: Todos los métodos de cálculo (`calcularPromedioLongitud`, 
     `contarTweetsPorSentimiento`) utilizan `stream().filter().map().collect()` 
     para procesar los datos de manera eficiente.

