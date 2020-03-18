package be.ipl.pae.server;

import be.ipl.pae.annotations.Inject;
import be.ipl.pae.biz.dto.MobiliteDto;
import be.ipl.pae.biz.dto.UserDto;
import be.ipl.pae.biz.factory.BizFactory;
import be.ipl.pae.biz.ucc.UserUcc;
import be.ipl.pae.enums.Periode;
import be.ipl.pae.enums.Role;
import be.ipl.pae.enums.TypeProgramme;
import be.ipl.pae.exceptions.BizException;
import be.ipl.pae.exceptions.FatalException;
import be.ipl.pae.exceptions.VersionException;
import be.ipl.pae.utils.Context;
import be.ipl.pae.utils.Logging;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class ServletServiceUser {

  private static final Logger LOGGER = Logging.getLogger(ServletServiceUser.class.getName());

  private Genson genson = new GensonBuilder().useDateFormat(new SimpleDateFormat("yyyy-MM-dd"))
      .useIndentation(true).useConstructorWithArguments(true).create();

  @Inject
  private ServletServiceUtils servUtils;
  @Inject
  private static BizFactory bf;
  @Inject
  private static UserUcc userUcc;
  // @Inject
  // private static PaysUcc paysUcc;
  private static final String JWTSECRET = Context.getProperty("jwtSecret");
  private JWTVerifier verifier;
  private Algorithm algorithm;
  private static String lastPage = null;



  /**
   * Constructeur du Servlet, initialise un algorithme de cryptage ainsi que le jwtVerifier.
   */
  ServletServiceUser() {
    try {
      this.algorithm = Algorithm.HMAC256(JWTSECRET);
      this.verifier = JWT.require(algorithm).withIssuer("auth0").build(); // Reusable
                                                                          // verifier //
                                                                          // instance
    } catch (JWTVerificationException exception) {
      // Invalid signature/claims
      exception.printStackTrace();
      LOGGER.severe("Problème de signature/claims");
      throw new FatalException("Problème de signature/claims");
    }

  }

  /**
   * Récupère une mobilité.
   * 
   * @param req - les données venant du frontend
   * @param resp - les données allant vers le frontend
   */
  void recuperationMobilite(HttpServletRequest req, HttpServletResponse resp) {
    Cookie[] cookies = req.getCookies();
    String json = genson.serialize(userUcc.getMobiliteByUser(getPseudoByCookie(cookies)));
    LOGGER.info("Les mobilités pour " + req.getParameter("pseudo") + " ont été récupérées.");
    servUtils.endMessageLauncher(resp, servUtils.SUCCESS, json);
  }

  /**
   * gère une mobilité.
   * 
   * @param req - les données venant du frontend
   * @param resp - les données allant vers le frontend
   * @throws VersionException - en cas d'erreur de version
   */
  void gestionMobilite(HttpServletRequest req, HttpServletResponse resp) throws VersionException {
    ArrayList<MobiliteDto> newMobilites = new ArrayList<>();
    List<Map<Object, Object>> mobilites =
        genson.deserialize(req.getParameter("mobilites"), List.class);
    for (Map map : mobilites) {
      if (map.size() > 0) {
        MobiliteDto mob = bf.getMobiliteDto();
        for (Object obj : map.entrySet()) {
          String[] tabString = obj.toString().split("=");
          switch (tabString[0]) {
            case "idMobilite":
              mob.setIdMobilite(Integer.parseInt(tabString[1]));
              break;
            case "select-pays":
              if (!tabString[1].equals("Choose...")) {
                mob.setPays(Integer.parseInt(tabString[1]));
              }
              break;
            case "select-partenaire":
              if (!tabString[1].equals("Choose...")) {
                mob.setPartenaire(Integer.parseInt(tabString[1]));
              }
              break;
            case "radio-btn-quad":
              if (tabString[1].equals("1quad")) {
                mob.setPeriode(Periode.Q1);
              } else {
                mob.setPeriode(Periode.Q2);
              }
              break;
            case "input-typeMobilite":
              if (tabString.length > 1) {
                if (tabString[1].equals("ERASMUS")) {
                  mob.setProgramme(TypeProgramme.ERASMUS);
                } else if (tabString[1].equals("ERABEL")) {
                  mob.setProgramme(TypeProgramme.ERABEL);
                } else {
                  mob.setProgramme(TypeProgramme.FAME);
                }
              } else {
                mob.setProgramme(TypeProgramme.AUTRE);
              }
              break;
            case "priorite":
              if (!tabString[1].equals("Choose...")) {
                mob.setNiveauPreference(Integer.parseInt(tabString[1]));
              } else {
                mob.setNiveauPreference(1);
              }
              break;
            case "radio-btn-SM":
              if (tabString[1].equals("sms")) {
                mob.setTypeMobilite("SMS");
              } else {
                mob.setTypeMobilite("SMP");
              }
              break;
            case "numVersion":
              mob.setVersion(Integer.parseInt(tabString[1]));
              break;
            default:
              LOGGER.severe("Mauvaise endroit de switch");
              break;
          }
        }
        mob.setMobiliteAnnule(false);
        mob.setFinancement(0);
        newMobilites.add(mob);
      }

    }
    Cookie[] cookies = req.getCookies();
    userUcc.gestionMobilite(newMobilites, getPseudoByCookie(cookies));
  }

  /**
   * Déconnecte l'utilisateur du site.
   * 
   * @param req - requête jetty.
   * @param resp - réponse jetty.
   */
  void deconnexion(HttpServletRequest req, HttpServletResponse resp) {
    Cookie[] cookies = req.getCookies();
    if (cookies != null) {
      for (Cookie coo : cookies) {
        if ("user".equals(coo.getName())) {
          coo.setValue("");
          coo.setPath("/");
          coo.setMaxAge(0);
          resp.addCookie(coo);
        }
      }
    }
    LOGGER.info("Un utilisateur vient de se déconnecter.");
    servUtils.endMessageLauncher(resp, servUtils.SUCCESS, "deconnexion");
  }

  /**
   * Vérifie l'utilisateur du site.
   * 
   * @param req - requête jetty.
   * @param resp - réponse jetty.
   */
  void verification(HttpServletRequest req, HttpServletResponse resp) {
    // TODO
    userUcc.verifAdminPresent();
    if (verifCookies(req.getCookies(), req.getRemoteAddr(), req.getSession())) {
      LOGGER.info("Un utilisateur vient de se faire accepter ses cookies.");
      servUtils.endMessageLauncher(resp, servUtils.SUCCESS, "cookie ok");
    } else {
      LOGGER.info("Un utilisateur vient de se faire refuser ses cookies.");
      servUtils.endMessageLauncher(resp, servUtils.ACCES_REFUSE, "cookie ko");
    }
  }

  /**
   * Connecte l'utilisateur au site.
   * 
   * @param req - requête jetty.
   * @param resp - réponse jetty.
   */
  void login(HttpServletRequest req, HttpServletResponse resp) {
    Map<String, String> infoConnexion =
        genson.deserialize(req.getParameter("infoInscription"), Map.class);
    UserDto user = null;
    user = userUcc.seConnecter(infoConnexion.get("pseudo"), infoConnexion.get("mdp"));
    if (user != null) {
      Map<String, Object> claims = new HashMap<String, Object>();
      claims.put("id", user.getPseudo());
      String ltoken = "";
      try {
        ltoken = JWT.create().withIssuer("auth0").withHeader(claims).sign(algorithm);
      } catch (JWTCreationException exception) {
        LOGGER.severe("La méthode login n'a pas réussi à signer ou a un souci de claims");
        exception.printStackTrace();
      }
      Cookie cookie = new Cookie("user", ltoken);// generation des cookies
      cookie.setPath("/");
      cookie.setMaxAge(60 * 60 * 24 * 365);
      resp.addCookie(cookie);
      LOGGER.info("L'utilisateur à été connecté");
      servUtils.endMessageLauncher(resp, servUtils.SUCCESS, "connexion reussie");
    } else {
      LOGGER.info("L'utilisateur n'a pas su se connecter");
      servUtils.endMessageLauncher(resp, servUtils.ACCES_REFUSE, "connexion refusee");
    }
  }

  /**
   * Vérifie les cookies de l'utilisateur du site.
   * 
   * @param req - requête jetty.
   * @param resp - réponse jetty.
   */
  boolean verifCookies(Cookie[] cookies, String ip, HttpSession session) {
    if (getPseudoByCookie(cookies) != null) { // ici on a pu recuperer un utilisateur valide
      return true;
    } else {
      return false;
    }
  }

  /**
   * Vérifie l'inscription de l'utilisateur du site.
   * 
   * @param req - requête jetty.
   * @param resp - réponse jetty.
   */
  void verifInscription(HttpServletRequest req, HttpServletResponse resp) {
    Map<String, String> infoInscription =
        genson.deserialize(req.getParameter("infoInscription"), Map.class);
    boolean noInscription = false;
    if (!infoInscription.get("mdp").equals(infoInscription.get("mdpRepetition"))) {
      noInscription = true;
      servUtils.endMessageLauncher(resp, servUtils.ACCES_REFUSE, "motDePassePasIdentique");
    }
    if (!servUtils.mailValidator((String) infoInscription.get("mail"))) {
      noInscription = true;
      servUtils.endMessageLauncher(resp, servUtils.ACCES_REFUSE, "adresseMailInvalide");
    }
    if (noInscription == false) {
      UserDto userDto = bf.getUserDto();

      userDto.setMotDePasse(infoInscription.get("mdp"));
      userDto.setEmail(infoInscription.get("mail"));
      userDto.setPseudo(infoInscription.get("pseudo"));
      userDto.setPrenom(infoInscription.get("prenom"));
      userDto.setNom(infoInscription.get("nom"));

      if (!userUcc.inscrireUtilisateur(userDto)) {
        LOGGER.warning("Le pseudo de l'utilisateur était déjà utilisé.");
        servUtils.endMessageLauncher(resp, servUtils.ACCES_REFUSE, "pseudoDejaUtilise");
      } else {
        LOGGER.info("L'utilisateur à été inscrit");
        servUtils.endMessageLauncher(resp, servUtils.SUCCESS, "true");
      }
    }

  }

  /**
   * Crée un json de l'utilisateur du site.
   * 
   * @param req - requête jetty.
   * @param resp - réponse jetty.
   */
  public void getJsonUser(HttpServletRequest req, HttpServletResponse resp) {
    Cookie[] cookies = req.getCookies();
    String pseudo = "";
    boolean faconVerification = false;
    if (req.getParameter("verifExistance") != null) {
      pseudo = req.getParameter("verifExistance");
      faconVerification = true;
    } else {
      pseudo = getPseudoByCookie(cookies);
    }
    if (pseudo != null) {
      UserDto userDto = userUcc.getInfoUser(pseudo);
      Map<String, Object> infoUser = new HashMap<>();
      if (faconVerification == false) {
        infoUser.put("pseudo", userDto.getPseudo());
        infoUser.put("nom", userDto.getNom());
        infoUser.put("prenom", userDto.getPrenom());
        infoUser.put("role", userDto.getRole());
        String json = genson.serialize(infoUser);
        servUtils.endMessageLauncher(resp, servUtils.SUCCESS, json);
      } else if (faconVerification == true && userDto == null) {
        servUtils.endMessageLauncher(resp, servUtils.PAS_DE_CONTENU,
            "aucun utilisateur ayant ce pseudo");
      }
    } else {
      LOGGER
          .warning("La méthode getJsonUser indique un mauvais pseudo de la part de l'utilisateur");
      servUtils.endMessageLauncher(resp, servUtils.ACCES_REFUSE, "probleme de pseudo");
    }
  }

  /**
   * récupère les étudiants.
   * 
   * @param req - les données venant du frontend
   * @param resp - les données allant vers le frontend
   */
  public void getEtudiants(HttpServletRequest req, HttpServletResponse resp) {
    Cookie[] cookies = req.getCookies();
    String pseudo = getPseudoByCookie(cookies);
    if (pseudo != null) {
      UserDto userDto = userUcc.getInfoUser(pseudo);
      List<HashMap<String, String>> etudiantsForFrontEnd = new ArrayList<HashMap<String, String>>();
      ArrayList<UserDto> etudiants = userUcc.listerEtudiants(userDto);
      if (etudiants != null) {
        for (UserDto etudiant : etudiants) {
          HashMap<String, String> temp = new HashMap<String, String>();
          temp.put("pseudo", etudiant.getPseudo());
          temp.put("nom", etudiant.getNom());
          temp.put("prenom", etudiant.getPrenom());
          temp.put("email", etudiant.getEmail());
          int nbrMobilites = userUcc.getNbrMobilitesParEtudiant(etudiant);
          temp.put("nbrMobilites", "" + nbrMobilites);
          int nbrMobilitesAnnules = userUcc.getNbrMobilitesAnnuleesParEtudiant(etudiant);
          temp.put("nbrMobilitesAnnules", "" + nbrMobilitesAnnules);
          etudiantsForFrontEnd.add(temp);
        }
        if (req.getParameter("champ") != null && req.getParameter("sens") != null) {
          switch (req.getParameter("champ")) {
            case "pseudo":
              if (req.getParameter("sens").equals("croissant")) {
                etudiantsForFrontEnd = etudiantsForFrontEnd.stream()
                    .sorted((s1, s2) -> s1.get("pseudo").compareTo(s2.get("pseudo")))
                    .collect(Collectors.toList());
              } else {
                etudiantsForFrontEnd = etudiantsForFrontEnd.stream()
                    .sorted((s1, s2) -> s2.get("pseudo").compareTo(s1.get("pseudo")))
                    .collect(Collectors.toList());
              }
              break;
            case "nom":
              if (req.getParameter("sens").equals("croissant")) {
                etudiantsForFrontEnd = etudiantsForFrontEnd.stream().sorted((s1, s2) -> {
                  int jj = s1.get("nom").compareTo(s2.get("nom"));
                  if (jj == 0) {
                    jj = s1.get("pseudo").compareTo(s2.get("pseudo"));
                  }
                  return jj;
                }).collect(Collectors.toList());
              } else {
                etudiantsForFrontEnd = etudiantsForFrontEnd.stream().sorted((s1, s2) -> {
                  int jj = s2.get("nom").compareTo(s1.get("nom"));
                  if (jj == 0) {
                    jj = s1.get("pseudo").compareTo(s2.get("pseudo"));
                  }
                  return jj;
                }).collect(Collectors.toList());
              }
              break;
            case "prenom":
              if (req.getParameter("sens").equals("croissant")) {
                etudiantsForFrontEnd = etudiantsForFrontEnd.stream().sorted((s1, s2) -> {
                  int jj = s1.get("prenom").compareTo(s2.get("prenom"));
                  if (jj == 0) {
                    jj = s1.get("pseudo").compareTo(s2.get("pseudo"));
                  }
                  return jj;
                }).collect(Collectors.toList());
              } else {
                etudiantsForFrontEnd = etudiantsForFrontEnd.stream().sorted((s1, s2) -> {
                  int jj = s2.get("prenom").compareTo(s1.get("prenom"));
                  if (jj == 0) {
                    jj = s1.get("pseudo").compareTo(s2.get("pseudo"));
                  }
                  return jj;
                }).collect(Collectors.toList());
              }
              break;
            case "email":
              if (req.getParameter("sens").equals("croissant")) {
                etudiantsForFrontEnd = etudiantsForFrontEnd.stream().sorted((s1, s2) -> {
                  int jj = s1.get("email").compareTo(s2.get("email"));
                  if (jj == 0) {
                    jj = s1.get("pseudo").compareTo(s2.get("pseudo"));
                  }
                  return jj;
                }).collect(Collectors.toList());
              } else {
                etudiantsForFrontEnd = etudiantsForFrontEnd.stream().sorted((s1, s2) -> {
                  int jj = s2.get("email").compareTo(s1.get("email"));
                  if (jj == 0) {
                    jj = s1.get("pseudo").compareTo(s2.get("pseudo"));
                  }
                  return jj;
                }).collect(Collectors.toList());
              }
              break;
            case "nbMob":
              if (req.getParameter("sens").equals("croissant")) {
                etudiantsForFrontEnd = etudiantsForFrontEnd.stream().sorted((s1, s2) -> {
                  int jj = s1.get("nbrMobilites").compareTo(s2.get("nbrMobilites"));
                  if (jj == 0) {
                    jj = s1.get("pseudo").compareTo(s2.get("pseudo"));
                  }
                  return jj;
                }).collect(Collectors.toList());
              } else {
                etudiantsForFrontEnd = etudiantsForFrontEnd.stream().sorted((s1, s2) -> {
                  int jj = s2.get("nbrMobilites").compareTo(s1.get("nbrMobilites"));
                  if (jj == 0) {
                    jj = s1.get("pseudo").compareTo(s2.get("pseudo"));
                  }
                  return jj;
                }).collect(Collectors.toList());
              }
              break;
            case "mobAnu":
              if (req.getParameter("sens").equals("croissant")) {
                etudiantsForFrontEnd = etudiantsForFrontEnd.stream().sorted((s1, s2) -> {
                  int jj = s1.get("nbrMobilitesAnnules").compareTo(s2.get("nbrMobilitesAnnules"));
                  if (jj == 0) {
                    jj = s1.get("pseudo").compareTo(s2.get("pseudo"));
                  }
                  return jj;
                }).collect(Collectors.toList());
              } else {
                etudiantsForFrontEnd = etudiantsForFrontEnd.stream().sorted((s1, s2) -> {
                  int jj = s2.get("nbrMobilitesAnnules").compareTo(s1.get("nbrMobilitesAnnules"));
                  if (jj == 0) {
                    jj = s1.get("pseudo").compareTo(s2.get("pseudo"));
                  }
                  return jj;
                }).collect(Collectors.toList());
              }
              break;
            default:
              LOGGER.severe("Mauvais switch");
              break;
          }
        }
        String json = genson.serialize(etudiantsForFrontEnd);
        servUtils.endMessageLauncher(resp, servUtils.SUCCESS, json);
      } else {
        LOGGER.warning("La méthode getEtudiants indique que l'utilisateur "
            + "n'est pas un administrateur ou un professeur");
        servUtils.endMessageLauncher(resp, servUtils.ACCES_REFUSE, "probleme de role");
      }
    } else {
      LOGGER.warning(
          "La méthode getEtudiants indique un mauvais " + "pseudo de la part de l'utilisateur");
      servUtils.endMessageLauncher(resp, servUtils.ACCES_REFUSE, "probleme de pseudo");
    }
  }

  /**
   * Renvoie un pseudo via la liste de cookies.
   * 
   * @param cookies - cookies de l'user.
   * @return le pseudo de l'user.
   */
  String getPseudoByCookie(Cookie[] cookies) {
    String token = null;
    if (cookies != null) {
      for (Cookie coo : cookies) {
        if ("user".equals(coo.getName()) && coo.getSecure()) {
          token = coo.getValue();
        } else if ("user".equals(coo.getName()) && token == null) {
          token = coo.getValue();
        }
      }
    }
    String pseudo = null;
    try {
      pseudo = this.verifier.verify(token).getHeaderClaim("id").asString();
    } catch (Exception exception) {
      LOGGER.info("Erreur normale de la méthode verifCookies. Ne pas y faire attention.");
    }
    return pseudo;
  }


  /**
   * Valide un compte.
   * 
   * @param compte - numéro de compte
   * @return true si le pattern est correct, false sinon
   */
  boolean compteValidator(String compte) {
    Matcher matcher =
        Pattern.compile("BE[0-9]{2}\\s[0-9]{4}\\s[0-9]{4}\\s[0-9]{4}", Pattern.CASE_INSENSITIVE)
            .matcher(compte);
    boolean result = matcher.find();
    LOGGER.info("La méthode compteValidator à répondu : << " + result + " >> pour le compte : << "
        + compte + " >>");
    return result;
  }

  /**
   * Complète les infos de user.
   * 
   * @param req - les données venant du frontend
   * @param resp - les données allant vers le frontend
   */
  void allInfoUser(HttpServletRequest req, HttpServletResponse resp) {
    Cookie[] cookies = req.getCookies();
    String pseudo = getPseudoByCookie(cookies);
    if (pseudo != null) {

      if (req.getParameter("pseudoEtudiant") != null) {
        pseudo = req.getParameter("pseudoEtudiant");
      }
      UserDto userDto = userUcc.getInfoUser(pseudo);
      Map<String, Object> infoUser = new HashMap<String, Object>();
      if (userDto.isHomme()) {
        infoUser.put("selectSexe", "H");
      } else {
        infoUser.put("selectSexe", "F");
      }
      infoUser.put("pseudo", userDto.getPseudo());
      infoUser.put("nom", userDto.getNom());
      infoUser.put("prenom", userDto.getPrenom());
      infoUser.put("mail", userDto.getEmail());
      infoUser.put("role", userDto.getRole());
      infoUser.put("nationalite", userDto.getNationalite());
      infoUser.put("numTel", userDto.getNumTel());
      infoUser.put("dateNaissance", String.valueOf(userDto.getDateNaissance()));
      infoUser.put("adresse", userDto.getAdresse());
      infoUser.put("commune", userDto.getCommune());
      infoUser.put("codePostal", userDto.getCodePostal());
      infoUser.put("nbAnnee", userDto.getNombreAnneeReussie());
      infoUser.put("numBanque", userDto.getNumCompte());
      infoUser.put("titulaireCompte", userDto.getTitulaireCompte());
      infoUser.put("codeBic", userDto.getCodeBic());
      infoUser.put("nomBanque", userDto.getNomBanque());
      String json = genson.serialize(infoUser);
      servUtils.endMessageLauncher(resp, servUtils.SUCCESS, json);
    } else {
      LOGGER
          .warning("La méthode getJsonUser indique un mauvais pseudo de la part de l'utilisateur");
      servUtils.endMessageLauncher(resp, servUtils.ACCES_REFUSE, "probleme de pseudo");
    }
  }

  /**
   * update les infos d'un user.
   * 
   * @param req - les données venant du frontend
   * @param resp - les données allant vers le frontend
   */
  void updateAllInfoUser(HttpServletRequest req, HttpServletResponse resp) {
    UserDto userDto;
    Map<Object, String> infoUserJson = genson.deserialize(req.getParameter("jsonUser"), Map.class);
    if (req.getParameter("modifParProf") != null) {
      String pseudoEtudiantAModifier = req.getParameter("pseudoEtudiant");
      userDto = userUcc.getInfoUser(pseudoEtudiantAModifier);
    } else {
      userDto = userUcc.getInfoUser(this.getPseudoByCookie(req.getCookies()));
    }
    String nouveauPseudo = null;
    String nouveaMdp = null;
    // certains champs ne sont pas envoyés vers le backend car ils n'ont pas été modifiés
    if (infoUserJson.get("selectSexe") != null) {

      if (infoUserJson.get("selectSexe").equals("H")) {
        userDto.setHomme(true);
      } else {
        userDto.setHomme(false);
      }
    }
    if (infoUserJson.get("mdp") != null) {
      nouveaMdp = infoUserJson.get("mdp");
    }
    if (infoUserJson.get("numTel") != null && !infoUserJson.get("numTel").equals("non précisé")) {
      userDto.setNumTel(infoUserJson.get("numTel"));
    }
    if (infoUserJson.get("dateNaissance") != null) {
      userDto.setDateNaissance(Date.valueOf(infoUserJson.get("dateNaissance")));
    }
    if (infoUserJson.get("nationalite") != null) {
      userDto.setNationalite(Integer.parseInt(infoUserJson.get("nationalite")));
    }
    if (infoUserJson.get("adresse") != null) {
      userDto.setAdresse(infoUserJson.get("adresse"));
    }
    if (infoUserJson.get("commune") != null) {
      userDto.setCommune(infoUserJson.get("commune"));
    }
    if (infoUserJson.get("codePostal") != null
        && !infoUserJson.get("codePostal").equals("non précisé")) {
      userDto.setCodePostal(Integer.parseInt(infoUserJson.get("codePostal")));
    }
    if (infoUserJson.get("nbAnnee") != null && !infoUserJson.get("nbAnnee").equals("non précisé")) {
      userDto.setNombreAnneeReussie(Integer.parseInt(infoUserJson.get("nbAnnee")));
    }
    if (infoUserJson.get("numBanque") != null) {
      if (compteValidator(infoUserJson.get("numBanque"))) {
        userDto.setNumCompte(infoUserJson.get("numBanque"));
      } else {
        servUtils.endMessageLauncher(resp, servUtils.ACCES_REFUSE, "numBanqueInvalide");
        return;
      }
    }
    if (infoUserJson.get("titulaireCompte") != null) {
      userDto.setTitulaireCompte(infoUserJson.get("titulaireCompte"));
    }
    if (infoUserJson.get("codeBic") != null) {
      userDto.setCodeBic(infoUserJson.get("codeBic"));
    }
    if (infoUserJson.get("nomBanque") != null) {
      userDto.setNomBanque(infoUserJson.get("nomBanque"));
    }
    if (infoUserJson.get("pseudo") != null) {
      nouveauPseudo = infoUserJson.get("pseudo");
    }
    if (infoUserJson.get("prenom") != null) {
      userDto.setPrenom(infoUserJson.get("prenom"));
    }
    if (infoUserJson.get("nom") != null) {
      userDto.setNom(infoUserJson.get("nom"));
    }
    if (infoUserJson.get("mail") != null) {
      if (!servUtils.mailValidator(infoUserJson.get("mail"))) {
        servUtils.endMessageLauncher(resp, servUtils.ACCES_REFUSE, "adresseMailInvalide");
        return;
      } else {
        userDto.setEmail(infoUserJson.get("mail"));
      }
    }

    if (infoUserJson.get("role") != null) {
      if (infoUserJson.get("role").equals("E")) {
        userDto.setRole(Role.E);
      } else if (infoUserJson.get("role").equals("P")) {
        userDto.setRole(Role.P);
      }
      // impossible de modifier vers un admin
    }
    if (nouveauPseudo == null) {
      userUcc.ajouterInfosUtilisateur(userDto, userDto.getPseudo(), nouveaMdp);
    } else {
      userUcc.ajouterInfosUtilisateur(userDto, nouveauPseudo, nouveaMdp);
    }
    servUtils.endMessageLauncher(resp, servUtils.SUCCESS, "infoBienModifie");
  }

  /**
   * Passe un étudiant en professeur.
   * 
   * @param req - les données venant du frontend
   * @param resp - les données allant vers le frontend
   */
  void promouvoirUser(HttpServletRequest req, HttpServletResponse resp) {
    String pseudo = getPseudoByCookie(req.getCookies());
    UserDto userDto = null;
    if (pseudo != null) {
      userDto = userUcc.getInfoUser(pseudo);
    }
    String pseudoToUp = req.getParameter("pseudoToUp");
    userUcc.promouvoirEtudiant(userDto, pseudoToUp);
    servUtils.endMessageLauncher(resp, servUtils.SUCCESS, "infoBienModifie");
  }

  /**
   * Complète une liste de data User.
   * 
   * @param req - les données venant du frontend
   * @return une liste de data user
   */
  List<String> autocompleteUser(HttpServletRequest req) {
    Cookie[] cookies = req.getCookies();
    String pseudo = getPseudoByCookie(cookies);
    List<String> usersForFrontEnd = new ArrayList<String>();
    ArrayList<UserDto> users;
    if (pseudo != null) {
      UserDto userDto = userUcc.getInfoUser(pseudo);
      if (req.getParameter("role").equals("E")) {
        // users = userUcc.listerEtudiants(userDto);
        return usersForFrontEnd;
        // Actuellement, un étudiant ne peux pas lister les autres.
      } else {
        users = userUcc.listerUser(userDto);
      }
      if (users != null) {
        for (UserDto user : users) {
          String temp = "";
          if (user.getRole().getRole().equals("Etudiant")) {
            temp = user.getNom() + " " + user.getPrenom() + " #" + user.getPseudo() + " [étudiant]";
          } else {
            temp =
                user.getNom() + " " + user.getPrenom() + " #" + user.getPseudo() + " [professeur]";
          }
          usersForFrontEnd.add(temp);
        }
      }
    } else {
      throw new BizException();
    }
    return usersForFrontEnd;
  }

  /**
   * Récupère la dernière page visitée.
   * 
   * @param req - les données venant du frontend
   * @param resp - les données allant vers le frontend
   */
  void getDernierePage(HttpServletRequest req, HttpServletResponse resp) {
    if (this.lastPage == null) {
      servUtils.endMessageLauncher(resp, servUtils.SUCCESS, "null");
    } else {
      servUtils.endMessageLauncher(resp, servUtils.SUCCESS, this.lastPage);
    }
  }

  /**
   * Change la dernière page visitée.
   * 
   * @param req - les données venant du frontend
   * @param resp - les données allant vers le frontend
   */
  void setDernierePage(HttpServletRequest req, HttpServletResponse resp) {
    if (req.getParameter("currentPage").equals("deco")) {
      this.lastPage = null;
    } else {
      this.lastPage = req.getParameter("currentPage");
    }
    servUtils.endMessageLauncher(resp, servUtils.SUCCESS, "ok");
  }

  public void notifications(HttpServletRequest req, HttpServletResponse resp) {
    String pseudo = req.getParameter("pseudoEtud");
    int idUser = userUcc.getInfoUser(pseudo).getIdUtilisateur();
    String msg = userUcc.getNotificationByUser(idUser);
    servUtils.endMessageLauncher(resp, servUtils.SUCCESS, msg);
  }


}
