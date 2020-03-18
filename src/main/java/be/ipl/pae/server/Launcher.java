package be.ipl.pae.server;

import be.ipl.pae.exceptions.FatalException;
import be.ipl.pae.utils.Context;
import be.ipl.pae.utils.InjectionDistributor;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import javax.servlet.http.HttpServlet;

public class Launcher {

  /**
   * Execute le lancement du serveur web.
   * 
   */
  public void startServer() {
    WebAppContext context = new WebAppContext(); // objet de configuration
    HttpServlet ms = (Servlet) InjectionDistributor.getDependancy(Servlet.class);
    ServletHolder sh = new ServletHolder(ms);
    sh.setInitParameter("cacheControl", "max-age=0,public");
    context.addServlet(sh, "/");
    context.setResourceBase("www");
    Server server = new Server(Context.getIntProperty("portServer"));
    server.setHandler(context);
    try {
      server.start();
    } catch (Exception exc) {
      exc.printStackTrace();
      throw new FatalException("Le serveur n'a pas su démarrer");
    }
  }

}
