package be.ipl.pae.utils;

import be.ipl.pae.exceptions.FatalException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Context {

  static Properties properties;

  /**
   * Charge le fichier .properties passé en paramètre.
   * 
   * @param fichier - nom du fichier properties
   * @throws IOException - en cas de probleme d'entree/sortie
   */
  public static void load(String fichier) {
    properties = new Properties();
    Path path = FileSystems.getDefault().getPath(fichier);
    try (InputStream in = Files.newInputStream(path)) {
      properties.load(in);
    } catch (IOException ioE) {
      ioE.printStackTrace();
      throw new FatalException("Le fichier properties n'a pas été loadé");
    }
  }

  /**
   * Récupère un attribut (String) du fichier properties loadé auparavent.
   * 
   * @param key - clef de l'attribut
   * @return la valeur correspondante à la clef
   */
  public static String getProperty(String key) {
    return properties.getProperty(key);
  }

  /**
   * Modifie un attribut du fichier properties loadé auparavent.
   * 
   * @param key - clef de l'attribut
   * @param value - valeur de la modification
   */
  public static void setProperty(String key, String value) {
    properties.setProperty(key, value);
  }

  /**
   * Récupère un attribut (Integer) du fichier properties loadé auparavent.
   * 
   * @param key - clef de l'attribut
   * @return la valeur correspondante à la clef
   */
  public static int getIntProperty(String key) {
    return Integer.parseInt(properties.getProperty(key));
  }

}
