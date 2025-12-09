package service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import model.Tweet;

/**
 *
 * @author Gael Valerio
 */
public class TweetsAnalyzeService {

    //se leer el archivo y devuleve una nueva lista
    public Supplier<List<Tweet>> crearLectorTweets(String rutaArchivo) {
        return () -> {

            List<Tweet> listaTweets = new ArrayList<>();
            try (Stream<String> lineas = Files.lines(Paths.get(rutaArchivo))) {
                listaTweets = lineas
                        .map(linea -> {
                            try {   //solo se divide la cadena por ',' que no esten dentro del contexto
                                String[] partes = linea.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                                if (partes.length < 4) {
                                    return null; // verificamos que la linea cumpla con las 4 columnas
                                }
                                // convertimos y pasamos los datos
                                int id = Integer.parseInt(partes[0].trim());
                                String entidad = partes[1].trim();
                                String sentimiento = partes[2].trim();
                                String texto = partes[3].trim().replace("\"", "");  // se quitan comillas extra que pueda tener el texto

                                return new Tweet(id, entidad, sentimiento, texto);
                            } catch (Exception e) {
                                return null;
                            }
                        })
                        .filter(tweet -> tweet != null) //se filtra las lineas nulas que no se procesaron
                        .collect(Collectors.toList());      //se guarda en la lista
            } catch (IOException e) {
                System.err.println("Error al leer el archivo: " + e.getMessage());
                return new ArrayList<>();
            }
            return listaTweets;
        };
    }

    //devolvera una funcion sobre los twwets en mayusculas, sin arrobas y demás
    public Function<Tweet, Tweet> obtenerFuncionLimpieza() {
        return tweetOriginal -> {
            String texto = tweetOriginal.getTextoDelTweet();
            texto = texto.toUpperCase();
            texto = texto.replaceAll("@\\w+", "");
            texto = texto.replaceAll("#\\w+", "");
            texto = texto.trim().replaceAll("\\s+", " ");

            return new Tweet(
                    tweetOriginal.getId(),
                    tweetOriginal.getEntidad(),
                    tweetOriginal.getSentimiento(),
                    texto
            );
        };
    }

    //aqui se procesan los nuevos datos 
    public List<Tweet> transformarTweets(List<Tweet> tweets, Function<Tweet, Tweet> transformacion) {
        return tweets.stream()
                .map(transformacion)
                .collect(Collectors.toList());
    }

    //se transforman los datos, pero solo los imprime
    public void procesarTweets(List<Tweet> tweets, Function<Tweet, Tweet> transformacion, Consumer<Tweet> accionFinal) {
        tweets.stream()
                .map(transformacion)
                .forEach(accionFinal);
    }

    //se calcula el promedio del tamaño del texto de los tweets filtrados
    public double calcularPromedioLongitud(List<Tweet> tweets, String sentimiento) {
        return tweets.stream()//se filtra buscando el string sentimiento
                .filter(t -> t.getSentimiento().equalsIgnoreCase(sentimiento))
               .mapToInt(t -> t.getTextoDelTweet().length())//se obtiene la longitud del texto y se trabaja con enteros con maptoint
                .average()  //se calcula el promedio
                .orElse(0.0); //si no hay tweets, devolvemos un 0
    }

    //cuenta cuantos tweets estan por c/u de los sentimientos
    public Map<String, Long> contarTweetsPorSentimiento(List<Tweet> tweets) {
        return tweets.stream()          //el gruping es como el group by de mysql, se agrupan por sentimientos y se cuentan
                .collect(Collectors.groupingBy( 
                        Tweet::getSentimiento,
                        Collectors.counting()
                ));
    }
}
