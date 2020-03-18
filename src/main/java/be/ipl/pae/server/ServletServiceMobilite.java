package be.ipl.pae.server;

import be.ipl.pae.annotations.Inject;
import be.ipl.pae.biz.dto.MobiliteDto;
import be.ipl.pae.biz.dto.PartenaireDto;
import be.ipl.pae.biz.ucc.DocumentsUcc;
import be.ipl.pae.biz.ucc.MobiliteUcc;
import be.ipl.pae.biz.ucc.PartenaireUcc;
import be.ipl.pae.biz.ucc.UserUcc;
import be.ipl.pae.enums.EtatMobilite;
import be.ipl.pae.exceptions.VersionException;
import be.ipl.pae.utils.Logging;

import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class ServletServiceMobilite {

  private static final Logger LOGGER = Logging.getLogger(ServletServiceMobilite.class.getName());

  @Inject
  private ServletServiceUtils servUtils;
  @Inject
  private MobiliteUcc mobiliteUcc;
  @Inject
  private PartenaireUcc partenaireUcc;
  @Inject
  private UserUcc userUcc;
  @Inject
  private ServletServiceUser ssu;
  @Inject
  private DocumentsUcc documentsUcc;


  private Genson genson = new GensonBuilder().useDateFormat(new SimpleDateFormat("yyyy-MM-dd"))
      .useIndentation(true).useConstructorWithArguments(true).create();


  /*
   * Permet d'envoyer vers le front- end la liste des mobilités pour un utilisateur donné.
   * 
   * @param req - les données venant du frontend
   * 
   * @param resp - les données allant vers le frontend
   */
  void getMobiliteByPseudo(HttpServletRequest req, HttpServletResponse resp) {
    Cookie[] cookies = req.getCookies();
    String pseudo = null;
    String json = null;
    if (req.getParameter("demandeProf") != null) {
      pseudo = req.getParameter("pseudoEtudiant");
      List<HashMap<Object, Object>> mobilitesForFrontEnd = genererMobilitesByPseudo(pseudo);

      if (req.getParameter("champ") != null && req.getParameter("sens") != null) {
        switch (req.getParameter("champ")) {
          case "nrCandidature":
            if (req.getParameter("sens").equals("croissant")) {
              mobilitesForFrontEnd = mobilitesForFrontEnd.stream().sorted((s1, s2) -> {
                int ii = (int) s2.get("nrCandidature");
                int jj = (int) s1.get("nrCandidature");
                return ii - jj;
              }).collect(Collectors.toList());
            } else {
              mobilitesForFrontEnd = mobilitesForFrontEnd.stream().sorted((s1, s2) -> {
                int ii = (int) s1.get("nrCandidature");
                int jj = (int) s2.get("nrCandidature");
                return ii - jj;
              }).collect(Collectors.toList());
            }
            break;
          case "nomPrenomEtudiant":
            if (req.getParameter("sens").equals("croissant")) {
              mobilitesForFrontEnd = mobilitesForFrontEnd.stream().sorted((s1, s2) -> {
                String ii = (String) s2.get("nomPrenomEtudiant");
                String jj = (String) s1.get("nomPrenomEtudiant");
                int result = ii.compareTo(jj);
                if (result == 0) {
                  int newI = (int) s2.get("nrCandidature");
                  int newJ = (int) s1.get("nrCandidature");
                  return newI - newJ;
                }
                return result;
              }).collect(Collectors.toList());
            } else {
              mobilitesForFrontEnd = mobilitesForFrontEnd.stream().sorted((s1, s2) -> {
                String ii = (String) s1.get("nomPrenomEtudiant");
                String jj = (String) s2.get("nomPrenomEtudiant");
                int result = ii.compareTo(jj);
                if (result == 0) {
                  int newI = (int) s2.get("nrCandidature");
                  int newJ = (int) s1.get("nrCandidature");
                  return newI - newJ;
                }
                return result;
              }).collect(Collectors.toList());
            }
            break;
          case "departement":
            if (req.getParameter("sens").equals("croissant")) {
              mobilitesForFrontEnd = mobilitesForFrontEnd.stream().sorted((s1, s2) -> {
                String ii = (String) s2.get("departement");
                String jj = (String) s1.get("departement");
                int result = ii.compareTo(jj);
                if (result == 0) {
                  int newI = (int) s2.get("nrCandidature");
                  int newJ = (int) s1.get("nrCandidature");
                  return newI - newJ;
                }
                return result;
              }).collect(Collectors.toList());
            } else {
              mobilitesForFrontEnd = mobilitesForFrontEnd.stream().sorted((s1, s2) -> {
                String ii = (String) s1.get("departement");
                String jj = (String) s2.get("departement");
                int result = ii.compareTo(jj);
                if (result == 0) {
                  int newI = (int) s2.get("nrCandidature");
                  int newJ = (int) s1.get("nrCandidature");
                  return newI - newJ;
                }
                return result;
              }).collect(Collectors.toList());
            }
            break;
          case "preference":
            if (req.getParameter("sens").equals("croissant")) {
              mobilitesForFrontEnd = mobilitesForFrontEnd.stream().sorted((s1, s2) -> {
                int ii = (int) s2.get("niveauPreference");
                int jj = (int) s1.get("niveauPreference");
                int result = ii - jj;
                if (result == 0) {
                  int newI = (int) s2.get("nrCandidature");
                  int newJ = (int) s1.get("nrCandidature");
                  return newI - newJ;
                }
                return result;
              }).collect(Collectors.toList());
            } else {
              mobilitesForFrontEnd = mobilitesForFrontEnd.stream().sorted((s1, s2) -> {
                int ii = (int) s1.get("niveauPreference");
                int jj = (int) s2.get("niveauPreference");
                int result = ii - jj;
                if (result == 0) {
                  int newI = (int) s2.get("nrCandidature");
                  int newJ = (int) s1.get("nrCandidature");
                  return newI - newJ;
                }
                return result;
              }).collect(Collectors.toList());
            }
            break;
          case "typeMobilite":
            if (req.getParameter("sens").equals("croissant")) {
              mobilitesForFrontEnd = mobilitesForFrontEnd.stream().sorted((s1, s2) -> {
                String ii = (String) s2.get("typeMobilite");
                String jj = (String) s1.get("typeMobilite");
                int result = ii.compareTo(jj);
                if (result == 0) {
                  int newI = (int) s2.get("nrCandidature");
                  int newJ = (int) s1.get("nrCandidature");
                  return newI - newJ;
                }
                return result;
              }).collect(Collectors.toList());
            } else {
              mobilitesForFrontEnd = mobilitesForFrontEnd.stream().sorted((s1, s2) -> {
                String ii = (String) s1.get("typeMobilite");
                String jj = (String) s2.get("typeMobilite");
                int result = ii.compareTo(jj);
                if (result == 0) {
                  int newI = (int) s2.get("nrCandidature");
                  int newJ = (int) s1.get("nrCandidature");
                  return newI - newJ;
                }
                return result;
              }).collect(Collectors.toList());
            }
            break;
          case "sMSSMP":
            if (req.getParameter("sens").equals("croissant")) {
              mobilitesForFrontEnd = mobilitesForFrontEnd.stream().sorted((s1, s2) -> {
                String ii = (String) s2.get("SMSSMP");
                String jj = (String) s1.get("SMSSMP");
                int result = ii.compareTo(jj);
                if (result == 0) {
                  int newI = (int) s2.get("nrCandidature");
                  int newJ = (int) s1.get("nrCandidature");
                  return newI - newJ;
                }
                return result;
              }).collect(Collectors.toList());
            } else {
              mobilitesForFrontEnd = mobilitesForFrontEnd.stream().sorted((s1, s2) -> {
                String ii = (String) s1.get("SMSSMP");
                String jj = (String) s2.get("SMSSMP");
                int result = ii.compareTo(jj);
                if (result == 0) {
                  int newI = (int) s2.get("nrCandidature");
                  int newJ = (int) s1.get("nrCandidature");
                  return newI - newJ;
                }
                return result;
              }).collect(Collectors.toList());
            }
            break;
          case "periode":
            if (req.getParameter("sens").equals("croissant")) {
              mobilitesForFrontEnd = mobilitesForFrontEnd.stream().sorted((s1, s2) -> {
                String ii = (String) s2.get("periode");
                String jj = (String) s1.get("periode");
                int result = ii.compareTo(jj);
                if (result == 0) {
                  int newI = (int) s2.get("nrCandidature");
                  int newJ = (int) s1.get("nrCandidature");
                  return newI - newJ;
                }
                return result;
              }).collect(Collectors.toList());
            } else {
              mobilitesForFrontEnd = mobilitesForFrontEnd.stream().sorted((s1, s2) -> {
                String ii = (String) s1.get("periode");
                String jj = (String) s2.get("periode");
                int result = ii.compareTo(jj);
                if (result == 0) {
                  int newI = (int) s2.get("nrCandidature");
                  int newJ = (int) s1.get("nrCandidature");
                  return newI - newJ;
                }
                return result;
              }).collect(Collectors.toList());
            }
            break;
          case "partenaire":
            if (req.getParameter("sens").equals("croissant")) {
              mobilitesForFrontEnd = mobilitesForFrontEnd.stream().sorted((s1, s2) -> {
                String ii = (String) s2.get("partenaire");
                String jj = (String) s1.get("partenaire");
                int result = ii.compareTo(jj);
                if (result == 0) {
                  int newI = (int) s2.get("nrCandidature");
                  int newJ = (int) s1.get("nrCandidature");
                  return newI - newJ;
                }
                return result;
              }).collect(Collectors.toList());
            } else {
              mobilitesForFrontEnd = mobilitesForFrontEnd.stream().sorted((s1, s2) -> {
                String ii = (String) s1.get("partenaire");
                String jj = (String) s2.get("partenaire");
                int result = ii.compareTo(jj);
                if (result == 0) {
                  int newI = (int) s2.get("nrCandidature");
                  int newJ = (int) s1.get("nrCandidature");
                  return newI - newJ;
                }
                return result;
              }).collect(Collectors.toList());
            }
            break;
          default:
            LOGGER.severe("Mauvais switch");
            break;
        }
        json = genson.serialize(mobilitesForFrontEnd);
      } else {
        json = genson.serialize(mobilitesForFrontEnd);
      }
    } else {
      pseudo = ssu.getPseudoByCookie(cookies);
      json = genson.serialize(mobiliteUcc.getMobiliteByPseudo(pseudo));
    }
    servUtils.endMessageLauncher(resp, servUtils.SUCCESS, json);
  }



  /*
   * Permet de generer la liste des mobilités plus des informations nécessaires pour un utilisateur
   * donné.
   * 
   * @param pseudo - pseudo de l'utilisateur
   * 
   * @return la liste des mobilités plus des informations nécessaires pour un utilisateur donné.
   */
  List<HashMap<Object, Object>> genererMobilitesByPseudo(String pseudo) {
    TreeMap<Date, Integer> triOrdreCandidature = new TreeMap<Date, Integer>();
    List<HashMap<Object, Object>> mobilitesForFrontEnd = new ArrayList<HashMap<Object, Object>>();
    for (MobiliteDto mob : mobiliteUcc.getMobiliteByPseudo(pseudo)) {
      HashMap<Object, Object> map = new HashMap<Object, Object>();
      // partie mobilite
      map.put("typeMobilite", mob.getProgramme().name());
      map.put("periode", mob.getPeriode().name());
      map.put("niveauPreference", mob.getNiveauPreference());
      map.put("SMSSMP", mob.getTypeMobilite());
      map.put("numVersion", mob.getVersion());
      map.put("nrCandidature", mob.getDateIntroduction());
      map.put("idDocDepart", mob.getDocumentsDepart());
      map.put("idDocRetour", mob.getDocumentsRetour());
      map.put("id", mob.getIdMobilite());
      map.put("etat", mob.getEtatMobilite().toString());
      map.put("docDepart", mob.getDocumentsDepart());
      map.put("docRetour", mob.getDocumentsRetour());
      map.put("idPays", mob.getPays());
      map.put("mobiliteAnnule", mob.isMobiliteAnnule());
      triOrdreCandidature.put(mob.getDateIntroduction(), 0);
      // partie partenaire
      if (mob.getPartenaire() == 0) {
        map.put("partenaire", "null");
        map.put("departement", "null");
      } else {
        PartenaireDto partenaireForFrontEnd = partenaireUcc.getPartenaireById(mob.getPartenaire());
        map.put("partenaire", partenaireForFrontEnd.getNomComplet());
        map.put("departement", partenaireForFrontEnd.getDepartement());
      }
      // partie utilisateur
      map.put("nomPrenomEtudiant",
          userUcc.getInfoUser(pseudo).getNom() + " " + userUcc.getInfoUser(pseudo).getPrenom());
      mobilitesForFrontEnd.add(map);
    }
    int ii = 1;
    for (Entry<Date, Integer> entry : triOrdreCandidature.entrySet()) {
      triOrdreCandidature.put(entry.getKey(), ii);
      ii++;
    }
    for (HashMap<Object, Object> entry : mobilitesForFrontEnd) {
      Date date = (Date) entry.get("nrCandidature");
      int nrCandidature = triOrdreCandidature.get(date);
      entry.put("nrCandidature", nrCandidature);
    }
    LOGGER.info("Les mobilités de " + pseudo + " ont été récupérées.");
    return mobilitesForFrontEnd;
  }

  /*
   * Permet de créer un fichier csv contenant la liste des mobilités pour un utilisateur donné.
   * 
   * @param req - les données venant du frontend
   * 
   * @param resp - les données allant vers le frontend
   */
  void genererCsv(HttpServletRequest req, HttpServletResponse resp) {
    String[] titres = new String[8];
    titres[0] = "N° d'ordre de candidature";
    titres[1] = "Nom/prénom de l'étudiant";
    titres[2] = "Département";
    titres[3] = "N° d'ordre de préférence";
    titres[4] = "Type de mobilité";
    titres[5] = "Stage/académique";
    titres[6] = "Quadrimèstre pendant lequel aura lieu la mobilité";
    titres[7] = "Partenaire";
    List<String[]> liste = new ArrayList<String[]>();
    liste.add(titres);
    String pseudoEtudiant = req.getParameter("pseudoEtudiant");
    List<HashMap<Object, Object>> mobilitesEtudiant = genererMobilitesByPseudo(pseudoEtudiant);
    mobilitesEtudiant = mobilitesEtudiant.stream().sorted((s1, s2) -> {
      int ii = (int) s1.get("nrCandidature");
      int jj = (int) s2.get("nrCandidature");
      return ii - jj;
    }).collect(Collectors.toList());
    for (HashMap<Object, Object> entry : mobilitesEtudiant) {
      String[] mob = new String[8];
      mob[0] = (String) entry.get("nrCandidature").toString();
      mob[1] = (String) entry.get("nomPrenomEtudiant");
      String departement = (String) entry.get("departement");
      if (departement.equals("null")) {
        departement = "non précisé";
      }
      mob[2] = departement;
      mob[3] = (String) entry.get("niveauPreference").toString();
      mob[4] = (String) entry.get("typeMobilite");
      mob[5] = (String) entry.get("SMSSMP");
      mob[6] = (String) entry.get("periode");
      String partenaire = (String) entry.get("partenaire");
      if (partenaire.equals("null")) {
        partenaire = "non précisé";
      }
      mob[7] = partenaire;
      liste.add(mob);
    }
    String nomFichierCsv = req.getParameter("nomFichierCSV");
    writeToCsvFile(liste, ";", nomFichierCsv);
    LOGGER.info("CSV créé");
    servUtils.endMessageLauncher(resp, servUtils.SUCCESS, "ok");
  }

  private void writeToCsvFile(List<String[]> thingsToWrite, String separateur,
      String nomFichierCsv) {
    Path pp = new File("www/csv/" + nomFichierCsv + ".csv").toPath();
    try (Writer out = Files.newBufferedWriter(pp, StandardCharsets.UTF_8)) {
      for (String[] strings : thingsToWrite) {
        for (int i = 0; i < strings.length; i++) {
          out.write(strings[i]);
          if (i < (strings.length - 1)) {
            out.write(separateur);
          }
        }
        out.write(System.lineSeparator());
      }
      out.flush();
    } catch (IOException exc) {
      LOGGER.severe("writeToCsvFile a échoué : " + exc.getMessage());
    }
  }

  /**
   * Récupère les données de mobilités.
   * 
   * @param req - les données venant du frontend
   * @param tmp - liste de data
   * @param resp - les données allant vers le frontend
   */
  public void autoCompleteMobilite(HttpServletResponse resp, List<String> tmp,
      HttpServletRequest req) {
    String term = req.getParameter("term");
    for (MobiliteDto dto : mobiliteUcc.getAllMobilite()) {
      tmp.add(dto.getTypeMobilite() + " " + userUcc.getPseudoUser(dto.getEtudiant()).getNom() + " "
          + userUcc.getPseudoUser(dto.getEtudiant()).getPrenom() + " choix n°"
          + dto.getNiveauPreference() + " dans l'état " + dto.getEtatMobilite().getEtatMobilite()
          + " [mobilité]");
    }
    List<String> retour = new ArrayList<String>();
    for (String string : tmp) {
      if (string.contains(term)) {
        retour.add(string);
      }
    }
    servUtils.endMessageLauncher(resp, servUtils.SUCCESS, genson.serialize(retour));
  }



  public void validerMob(HttpServletRequest req, HttpServletResponse resp) {
    MobiliteDto mob = mobiliteUcc.getMobiliteParId(Integer.parseInt(req.getParameter("id")));
    if (req.getParameter("etat").equals("valide")
        && mob.getEtatMobilite().toString().equals("CREE")) {
      documentsUcc.initDocuments();
      mob.setDocumentsDepart(documentsUcc.getIdDernierDocDepart());
      mob.setDocumentsRetour(documentsUcc.getIdDernierDocretour());
    } else if (req.getParameter("etat").equals("termine")) {
      mob.setEtatMobilite(EtatMobilite.A_PAYER);
    }
    mobiliteUcc.validerMobilite(mob);
    servUtils.endMessageLauncher(resp, servUtils.SUCCESS, "updateReussi");
  }



  public void getMobById(HttpServletRequest req, HttpServletResponse resp) {
    MobiliteDto mob = mobiliteUcc.getMobiliteParId(Integer.parseInt(req.getParameter("id")));
    servUtils.endMessageLauncher(resp, servUtils.SUCCESS, genson.serialize(mob));
  }

  public void annulerMobilite(HttpServletRequest req, HttpServletResponse resp)
      throws NumberFormatException, VersionException {
    int id = Integer.parseInt(req.getParameter("id"));
    mobiliteUcc.annulerMobilite(id);
    mobiliteUcc.ajouterMessageAnnulation(id, req.getParameter("message"));
    servUtils.endMessageLauncher(resp, servUtils.SUCCESS, "ok");
  }
}
