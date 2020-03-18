package be.ipl.pae.utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Logging {

  /**
   * Crée un logger pré-configuré.
   * 
   * @param name - nom du logger (habituellement le nom de la classe).
   * @return un logger.
   */
  public static Logger getLogger(String name) {
    FileHandler fh = null;
    Logger log = Logger.getLogger(name);
    try {
      fh = new FileHandler("./logging/" + log.getName() + ".log", true);
    } catch (SecurityException | IOException exc) {
      exc.printStackTrace();
    }
    fh.setFormatter(new SimpleFormatter());
    fh.setLevel(Level.ALL);
    log.addHandler(fh);
    log.setLevel(Level.ALL);
    return log;
  }

}
