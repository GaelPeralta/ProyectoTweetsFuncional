
package model;

/**
 *
 * @author Gael Valerio
 */
public class Tweet {
    private int id;
    private String entidad;
    private String sentimiento;
    private String textoDelTweet;
    
    public Tweet(int id, String entidad, String sentimiento, String textoDelTweet) {
        this.id = id;
        this.entidad = entidad;
        this.sentimiento = sentimiento;
        this.textoDelTweet = textoDelTweet;
    }

    public int getId() {
        return id;
    }

    public String getEntidad() {
        return entidad;
    }

    public String getSentimiento() {
        return sentimiento;
    }
    
    public String getTextoDelTweet() {
        return textoDelTweet;
    }

    @Override
    public String toString() {
        return "Tweet{" + "id=" + id + ", entidad=" + entidad + ", sentimiento=" + sentimiento + ", textoDelTweet=" + textoDelTweet + '}';
    }
    
}
