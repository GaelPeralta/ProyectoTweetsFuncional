package app;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import model.Tweet;
import report.ReportGenerator;
import service.TweetsAnalyzeService;

/**
 *
 * @author Gael Valerio
 */
public class Main {

    public static void main(String[] args) {
        TweetsAnalyzeService metodos = new TweetsAnalyzeService();
        ReportGenerator reportes = new ReportGenerator();
        Runnable principal = procesoPrincipal(metodos, reportes);
        principal.run();
    }

    public static Runnable procesoPrincipal(TweetsAnalyzeService metodos, ReportGenerator reportes) {
        return () -> {
            String sentimientoDefinido= "Positive";  //positive, negative, Irrelevant o neutral
            
            //se cargan los twwets desde el archivo cvs
            System.out.println("cargando la info desde el archivo...");
            Supplier<List<Tweet>> lector = metodos.crearLectorTweets("data/twitters.csv");
            List<Tweet> listaOriginal = lector.get();
            System.out.println("numero total de tweets leidos: " + listaOriginal.size());

            //se transforman los tweets con las indicaciones de mayusuculas, sin menciones y demas
            System.out.println("convirtiendo los tweets...");
            Function<Tweet, Tweet> logicaLimpieza = metodos.obtenerFuncionLimpieza();
            List<Tweet> tweetsLimpios = metodos.transformarTweets(listaOriginal, logicaLimpieza);

            //se analizan los tweets y se identifican los distintos tipos segun su sentimiendo o emocion
            System.out.println("Se estan generando las estadisticas...");
            Map<String, Long> conteo = metodos.contarTweetsPorSentimiento(tweetsLimpios);
            double promedio = metodos.calcularPromedioLongitud(tweetsLimpios, sentimientoDefinido); 

            // se crea el reporte
            StringBuilder sb = new StringBuilder();
            sb.append("reencuento\n================\n");
            sb.append("total de tweets encontrados por sentimiento:\n");
            conteo.forEach((k, v) -> sb.append(" - ").append(k).append(": ").append(v).append("\n"));
            sb.append("\npromedio de longitud de tweets de tipo " + sentimientoDefinido + ": ").append(String.format("%.2f", promedio));
            String textoReporte = sb.toString();
            System.out.println(textoReporte); //se muestra en consola antes de cargarlo al archivo

            // se termina de procesar y se da la instruccion de guardas las respuestas en los archivos
            System.out.println("guardando archivos...");
            reportes.guardarTweetsLimpios(tweetsLimpios, "output/tweets_procesados.txt");
            reportes.guardarResumenEstadisticas(textoReporte, "output/resumen_estadisticas.txt");

        };
    }

}
