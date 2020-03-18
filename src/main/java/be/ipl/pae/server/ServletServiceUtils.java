package be.ipl.pae.server;

import be.ipl.pae.utils.Logging;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

public class ServletServiceUtils {

  private static final Logger LOGGER = Logging.getLogger(ServletServiceUtils.class.getName());

  //////////////// DEFINITIONS DES CODES WEB ////////////////
  static final int SUCCESS = 200;
  static final int PAS_DE_CONTENU = 204;
  static final int ERREUR_SERVEUR = 500;
  static final int REDIRECTION = 301;
  static final int UTILISATEUR_NON_AUTHENTIFIE = 401;
  static final int ACCES_REFUSE = 403;
  static final int PAGE_NON_TROUVEE = 404;
  ////////////////////////////////////////////////////////////

  /**
   * Transmet une réponse au frontEnd.
   * 
   * @param resp - réponse jetty.
   * @param statut - code de la réponse web.
   * @param msg - message à transmettre au frontEnd.
   */
  public void endMessageLauncher(HttpServletResponse resp, int statut, String msg) {
    resp.setStatus(statut);
    resp.setContentType("text/html");
    byte[] msgBytes;
    try {
      msgBytes = msg.getBytes("UTF-8");
      resp.setContentLength(msgBytes.length);
      resp.setCharacterEncoding("utf-8");
      resp.getOutputStream().write(msgBytes);
    } catch (IOException ioe) {
      ioe.printStackTrace();
      LOGGER.severe("La méthode endMessageLauncher vient d'échouer avec " + "le message : << " + msg
          + " >> et le statut : << " + Integer.toString(statut) + ">>.");
    }

  }

  /**
   * Valide ou non un email selon le pattern suivant : "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$".
   * 
   * @param email - String identifiant l'email.
   * @return true si le mail est valide, false dans le cas contraire.
   */
  boolean mailValidator(String email) {
    Matcher matcher =
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)
            .matcher(email);
    boolean result = matcher.find();
    LOGGER.info("La méthode mailValidator à répondu : << " + result + " >> pour la mail : << "
        + email + " >>");
    return result;
  }
}
