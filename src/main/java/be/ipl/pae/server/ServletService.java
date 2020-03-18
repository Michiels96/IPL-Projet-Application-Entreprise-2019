package be.ipl.pae.server;

import be.ipl.pae.annotations.Inject;
import be.ipl.pae.biz.dto.UserDto;
import be.ipl.pae.biz.factory.BizFactory;
import be.ipl.pae.biz.ucc.UserUcc;
import be.ipl.pae.utils.Context;
import be.ipl.pae.utils.Logging;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class ServletService {

  private static final Logger LOGGER = Logging.getLogger(ServletService.class.getName());

  @Inject
  private BizFactory bf;
  @Inject
  private UserUcc userUcc;
  // @Inject
  // private MobiliteUcc mobUcc;
  private static final String JWTSECRET = Context.getProperty("jwtSecret");
  private Genson genson = new GensonBuilder().useDateFormat(new SimpleDateFormat("yyyy-MM-dd"))
      .useIndentation(true).useConstructorWithArguments(true).create();
  private JWTVerifier verifier;
  private Algorithm algorithm;

  //////////////// DEFINITIONS DES CODES WEB ////////////////
  static final int SUCCESS = 200;
  static final int ERREUR_SERVEUR = 500;
  static final int REDIRECTION = 301;
  static final int UTILISATEUR_NON_AUTHENTIFIE = 401;
  static final int ACCES_REFUSE = 403;
  static final int PAGE_NON_TROUVEE = 404;
  ////////////////////////////////////////////////////////////

  /**
   * Constructeur du Servlet, initialise un algorithme de cryptage ainsi que le jwtVerifier.
   */
  ServletService() {
    try {
      this.algorithm = Algorithm.HMAC256(JWTSECRET);
      this.verifier = JWT.require(algorithm).withIssuer("auth0").build(); // Reusable
                                                                          // verifier //
                                                                          // instance
    } catch (JWTVerificationException exception) {
      // Invalid signature/claims
      exception.printStackTrace();
    }
  }

  /**
   * Deconnecte l'utilisateur.
   * 
   * @param req - les données venant du frontend
   * @param resp - les données allant vers le frontend
   */
  void deconnexion(HttpServletRequest req, HttpServletResponse resp) {
    Cookie[] cookies = req.getCookies();
    if (cookies != null) {
      for (Cookie c : cookies) {
        if ("user".equals(c.getName())) {
          c.setValue("");
          c.setPath("/");
          c.setMaxAge(0);
          resp.addCookie(c);
        }
      }
    }
    LOGGER.info("deconnexion");
    endMessageLauncher(resp, SUCCESS, "deconnexion");
  }

  /**
   * Verifie les cookies.
   * 
   * @param req - les données venant du frontend
   * @param resp - les données allant vers le frontend
   */
  void verification(HttpServletRequest req, HttpServletResponse resp) {
    if (verifCookies(req.getCookies())) {
      LOGGER.info("verification de cookie acceptée");
      endMessageLauncher(resp, SUCCESS, "cookie ok");
    } else {
      LOGGER.info("verification de cookie refusée");
      endMessageLauncher(resp, ACCES_REFUSE, "cookie ko");
    }
  }

  /**
   * connecte un utilisateur.
   * 
   * @param req - les données venant du frontend
   * @param resp - les données allant vers le frontend
   */
  void login(HttpServletRequest req, HttpServletResponse resp) {
    Map<String, String> infoConnexion =
        genson.deserialize(req.getParameter("infoInscription"), Map.class);
    UserDto user = null;
    if ((user =
        userUcc.seConnecter(infoConnexion.get("pseudo"), infoConnexion.get("mdp"))) != null) {
      Map<String, Object> claims = new HashMap<String, Object>();
      claims.put("id", user.getPseudo());
      String ltoken = "";
      try {
        ltoken = JWT.create().withIssuer("auth0").withHeader(claims).sign(algorithm);
      } catch (JWTCreationException exception) {
        LOGGER.severe("exception de login : " + exception.getMessage());
      }
      Cookie cookie = new Cookie("user", ltoken);// generation des cookies
      cookie.setPath("/");
      cookie.setMaxAge(60 * 60 * 24 * 365);
      resp.addCookie(cookie);
      LOGGER.info("connexion réussie");
      endMessageLauncher(resp, SUCCESS, "connexion reussie");
    } else {
      LOGGER.info("connexion refusée");
      endMessageLauncher(resp, ACCES_REFUSE, "connexion refusee");
    }
  }

  private boolean verifCookies(Cookie[] cookies) {
    String token = null;
    if (cookies != null) {
      for (Cookie c : cookies) {
        if ("user".equals(c.getName()) && c.getSecure()) {
          token = c.getValue();
        } else if ("user".equals(c.getName()) && token == null) {
          token = c.getValue();
        }
      }
    }
    String pseudo = null;
    try {
      pseudo = this.verifier.verify(token).getHeaderClaim("id").toString();
    } catch (Exception exception) {
      LOGGER.severe("verifCookies exception : " + exception.getMessage());
    }
    if (pseudo != null) {
      LOGGER.info("User valide : " + pseudo);
      return true;
    } else {
      LOGGER.info("User invalide : " + pseudo);
      return false;
    }
  }

  /**
   * Vérifie l'inscription d'un utilisateur.
   * 
   * @param req - les données venant du frontend
   * @param resp - les données allant vers le frontend
   */
  void verifInscription(HttpServletRequest req, HttpServletResponse resp) {
    Map<String, String> infoInscription =
        genson.deserialize(req.getParameter("infoInscription"), Map.class);
    if (!infoInscription.get("mdp").equals(infoInscription.get("mdpRepetition"))) {
      endMessageLauncher(resp, ACCES_REFUSE, "motDePassePasIdentique");
    }
    if (!mailValidator((String) infoInscription.get("mail"))) {
      endMessageLauncher(resp, ACCES_REFUSE, "adresseMailInvalide");
    }
    UserDto userDto = bf.getUserDto();

    userDto.setMotDePasse(infoInscription.get("mdp"));
    userDto.setEmail(infoInscription.get("mail"));
    userDto.setPseudo(infoInscription.get("pseudo"));
    userDto.setPrenom(infoInscription.get("prenom"));
    userDto.setNom(infoInscription.get("nom"));

    if (!userUcc.inscrireUtilisateur(userDto)) {
      LOGGER.info("Pseudo déjà existant : " + userDto.getPseudo());
      endMessageLauncher(resp, ACCES_REFUSE, "pseudoDejaUtilise");
    } else {
      LOGGER.info("Pseudo libre : " + userDto.getPseudo());
      endMessageLauncher(resp, SUCCESS, "true");
    }
  }


  /**
   * Valide ou non un email selon le pattern suivant : "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$".
   * 
   * @param email - String identifiant l'email
   * @return true si le mail est valide, false dans le cas contraire.
   */
  boolean mailValidator(String email) {
    Matcher matcher =
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)
            .matcher(email);
    return matcher.find();
  }

  /**
   * Transmet une réponse au frontEnd.
   * 
   * @param resp - réponse jetty.
   * @param statut - code de la réponse web.
   * @param msg - message à transmettre au frontEnd.
   */
  void endMessageLauncher(HttpServletResponse resp, int statut, String msg) {
    resp.setStatus(statut);
    resp.setContentType("text/html");
    byte[] msgBytes;
    LOGGER.info("End message ==> statut : " + statut + "message : " + msg);
    try {
      msgBytes = msg.getBytes("UTF-8");
      resp.setContentLength(msgBytes.length);
      resp.setCharacterEncoding("utf-8");
      resp.getOutputStream().write(msgBytes);
    } catch (IOException ioe) {
      LOGGER.severe("exception endMessageLauncher" + ioe.getMessage());
    }

  }
}
