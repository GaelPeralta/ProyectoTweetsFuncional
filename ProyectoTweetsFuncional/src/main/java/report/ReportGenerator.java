package report;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import model.Tweet;

/**
 *
 * @author Gael 
 */
public class ReportGenerator {


    public void guardarTweetsLimpios(List<Tweet> tweets, String rutaSalida) {
        try { //Se convierten los objetos tweets a unos de tipo string
            List<String> lineas = tweets.stream()
                    .map(t -> t.getId() + "," + t.getEntidad() + "," + t.getSentimiento() + "," + t.getTextoDelTweet())
                    .collect(Collectors.toList());
            //aqui se escriben las lineas en el archivo
            Files.write(Paths.get(rutaSalida), lineas);
            System.out.println("Archivo de tweets guardado exitosamente en: " + rutaSalida);
        } catch (IOException e) {
            System.err.println("Error al guardar los tweets: " + e.getMessage());
        }
    }

    public void guardarResumenEstadisticas(String resumen, String rutaSalida) {
        try {
            Files.write(Paths.get(rutaSalida), resumen.getBytes());
            System.out.println("Reporte de estadisticas guardado en: " + rutaSalida);
        } catch (IOException e) {
            System.err.println("Error al guardar estadísticas: " + e.getMessage());
        }
    }
    
}
