package be.ipl.pae.server;

import be.ipl.pae.annotations.Inject;
import be.ipl.pae.exceptions.FatalException;

import org.eclipse.jetty.servlet.DefaultServlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Servlet extends DefaultServlet {
  private static final long serialVersionUID = -2300493049456943083L;

  @Inject
  private ServletServiceUtils servUtils;
  @Inject
  private ServletServiceUser ssu;
  @Inject
  private ServletServicePartenaire ssp;
  @Inject
  private ServletServicePays sspays;
  @Inject
  private ServletServiceMobilite ssmob;
  @Inject
  private ServletServiceDocuments ssdoc;

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    try {
      String action = req.getParameter("action");
      switch (action) {
        case "login":
          ssu.login(req, resp);
          break;
        case "inscription":
          ssu.verifInscription(req, resp);
          break;
        case "verification":
          ssu.verification(req, resp);
          break;
        case "deconnexion":
          ssu.deconnexion(req, resp);
          break;
        case "getJsonUser":
          ssu.getJsonUser(req, resp);
          break;
        case "updateAllInfoUser":
          ssu.updateAllInfoUser(req, resp);
          break;
        case "allInfoUser":
          ssu.allInfoUser(req, resp);
          break;
        case "listerEtudiants":
          ssu.getEtudiants(req, resp);
          break;
        case "getAllPartenaire":
          ssp.getAllPartenaire(req, resp);
          break;
        case "getPartenaireById":
          ssp.getPartenaireById(req, resp);
          break;
        case "getPaysById":
          sspays.getPaysById(req, resp);
          break;
        case "enregistrerMobilite":
          ssu.gestionMobilite(req, resp);
          break;
        case "annulerMobilite":
          ssmob.annulerMobilite(req, resp);
          break;
        case "annulerMobDefinitif":
          ssmob.annulerMobilite(req, resp);
          break;
        case "getMobiliteByPseudo":
          ssmob.getMobiliteByPseudo(req, resp);
          break;
        case "getAllPays":
          sspays.getAllPays(req, resp);
          break;
        case "promouvoir":
          ssu.promouvoirUser(req, resp);
          break;
        case "getDocumentsDepart":
          ssdoc.getDocumentDepart(req, resp);
          break;
        case "getDocumentsRetour":
          ssdoc.getDocumentRetour(req, resp);
          break;
        case "updateDocumentsDepart":
          ssdoc.updateDocumentsDepart(req, resp);
          break;
        case "updateDocumentsRetour":
          ssdoc.updateDocumentsRetour(req, resp);
          break;
        case "getDernierePage":
          ssu.getDernierePage(req, resp);
          break;
        case "setDernierePage":
          ssu.setDernierePage(req, resp);
          break;
        case "ajouterPartenaire":
          ssp.ajouterPartenaire(req, resp);
          break;
        case "generateCsvFileForMobilites":
          ssmob.genererCsv(req, resp);
          break;
        case "recupererInfosAutocomplete":
          List<String> retour = ssu.autocompleteUser(req);
          retour = ssp.autocompletePartenaire(retour);
          ssmob.autoCompleteMobilite(resp, retour, req);
          break;
        case "validerMob":
          ssmob.validerMob(req, resp);
          break;
        case "getMobById":
          ssmob.getMobById(req, resp);
          break;
        case "notifications":
          ssu.notifications(req, resp);
          break;
        default:
          servUtils.endMessageLauncher(resp, servUtils.PAGE_NON_TROUVEE, "Cette page n'existe pas");
      }
    } catch (Exception exc) {
      exc.printStackTrace();
      servUtils.endMessageLauncher(resp, servUtils.ERREUR_SERVEUR, exc.getMessage());
    }
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    // si get url a seulement le / alors envoyer tout le fichier index.html
    // sinon renvoyer super(); pour envoyer de manière automatique vers le client les fichiers css
    // et js demandés
    // System.out.println("CLIENT DEMANDE via doGet -> " + req.getRequestURI());
    // va servir pour charger les fichiers css et js contenus dans des balises html du fichier
    // html

    // generation fichier index
    String html = "";
    html += lectureFichierHtml("header.html");
    html += lectureFichierHtml("login.html");
    html += lectureFichierHtml("register.html");
    html += lectureFichierHtml("vueDocuments.html");
    html += lectureFichierHtml("accueil.html");
    html += lectureFichierHtml("profil.html");
    html += lectureFichierHtml("mobilite.html");
    html += lectureFichierHtml("mobiliteProf.html");
    html += lectureFichierHtml("listeEtudiants.html");
    html += lectureFichierHtml("partenaire.html");
    html += lectureFichierHtml("ajoutPartenaire.html");
    html += lectureFichierHtml("footer.html");

    if (!req.getRequestURI().equals("/")) {
      try {
        super.doGet(req, resp);
      } catch (ServletException | IOException exc) {
        exc.printStackTrace();
        throw new FatalException("Problème de lecture de fichier pour le single page");
      }
    } else {
      envoisFichierHtml(resp, html);
    }
  }

  private String lectureFichierHtml(String path) {
    String codeReturn = "";
    Path pat = FileSystems.getDefault().getPath("www/inc/" + path);
    try {
      BufferedReader br = Files.newBufferedReader(pat);
      Scanner sc = new Scanner(br);
      while ((codeReturn += sc.nextLine() + "\n") != null) {
        sc.toString(); // pour PMD
      }
    } catch (IOException exc1) {
      exc1.printStackTrace();
      throw new FatalException("Problème de lecture de fichier pour le single page");
    } catch (NoSuchElementException exc2) {
      // procedure normale
      return codeReturn;
    }
    return null;
  }

  private void envoisFichierHtml(HttpServletResponse resp, String html) {
    try {
      byte[] byt = html.getBytes("utf-8");
      resp.setContentLength(byt.length);
      resp.setContentType("text/html");
      resp.setCharacterEncoding("utf-8");
      resp.getOutputStream().write(byt);
    } catch (IOException exc3) {
      exc3.printStackTrace();
      throw new FatalException("Problème de lecture de fichier pour le single page");
    }
  }
}
