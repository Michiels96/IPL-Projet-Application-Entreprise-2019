package be.ipl.pae.main;


import be.ipl.pae.server.Launcher;
import be.ipl.pae.utils.Context;

public class Main {

  /**
   * Coeur de l'application.
   * 
   * @param args - arguments de l'application
   */
  public static void main(String[] args) {
    System.out.println(System.getProperty("os.name"));
    Context.load("prod.properties");
    Launcher launch = new Launcher();
    launch.startServer();
    System.out.println("Serveur prêt !");
  }
}
