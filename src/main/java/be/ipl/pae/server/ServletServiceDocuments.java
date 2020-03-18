package be.ipl.pae.server;

import be.ipl.pae.annotations.Inject;
import be.ipl.pae.biz.dto.DocumentDepartDto;
import be.ipl.pae.biz.dto.DocumentRetourDto;
import be.ipl.pae.biz.factory.BizFactory;
import be.ipl.pae.biz.ucc.DocumentsUcc;

import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletServiceDocuments {

  @Inject
  private ServletServiceUtils servUtils;

  @Inject
  private DocumentsUcc documentsUcc;

  @Inject
  private BizFactory bf;

  private Genson genson = new GensonBuilder().useDateFormat(new SimpleDateFormat("yyyy-MM-dd"))
      .useIndentation(true).useConstructorWithArguments(true).create();

  /**
   * Récupère les documents de départ.
   * 
   * @param req - les données venant du frontend
   * @param resp - les données allant vers le frontend
   */
  public void getDocumentDepart(HttpServletRequest req, HttpServletResponse resp) {
    String json = genson
        .serialize(documentsUcc.getDocumentDepart(Integer.parseInt(req.getParameter("idDoc"))));
    servUtils.endMessageLauncher(resp, servUtils.SUCCESS, json);
  }

  /**
   * Récupère les documents de retour.
   * 
   * @param req - les données venant du frontend
   * @param resp - les données allant vers le frontend
   */
  public void getDocumentRetour(HttpServletRequest req, HttpServletResponse resp) {
    String json = genson
        .serialize(documentsUcc.getDocumentRetour(Integer.parseInt(req.getParameter("idDoc"))));
    servUtils.endMessageLauncher(resp, servUtils.SUCCESS, json);
  }

  /**
   * update les documents de départ.
   * 
   * @param req - les données venant du frontend
   * @param resp - les données allant vers le frontend
   */
  public void updateDocumentsDepart(HttpServletRequest req, HttpServletResponse resp) {
    DocumentDepartDto doc = bf.getDocumentDepartDto();
    doc.setIdDocumentsDepart(Integer.parseInt(req.getParameter("idDoc")));
    doc.setVersion(Integer.parseInt(req.getParameter("version")));
    HashMap<String, Boolean> map =
        (HashMap<String, Boolean>) genson.deserialize(req.getParameter("json"), Map.class);
    doc.setPreuveTestLinguistique(map.get("preuveTestLinguistique"));
    doc.setContratBourse(map.get("contratBourse"));
    doc.setConventionStageEtude(map.get("conventionStageEtude"));
    doc.setCharteEtudiant(map.get("charteEtudiant"));
    doc.setDocumentEngagement(map.get("documentEngagement"));
    doc.setDocumentEnvoieDocument(map.get("envoieDocumentDepart"));
    doc.setPaiementEffectue(map.get("confirmationPaiementDepart"));
    documentsUcc.updateDocumentDepart(doc, Integer.parseInt(req.getParameter("idMobilite")),
        Integer.parseInt(req.getParameter("versionMob")), req.getParameter("etatMobilite"),
        Integer.parseInt(req.getParameter("idDocRetour")));
  }

  /**
   * update les documents de retour.
   * 
   * @param req - les données venant du frontend
   * @param resp - les données allant vers le frontend
   */
  public void updateDocumentsRetour(HttpServletRequest req, HttpServletResponse resp) {
    HashMap<String, Boolean> map =
        (HashMap<String, Boolean>) genson.deserialize(req.getParameter("json"), Map.class);
    DocumentRetourDto doc = bf.getDocumentRetourDto();

    doc.setVersion(Integer.parseInt(req.getParameter("version")));
    doc.setIdDocumentsRetour(Integer.parseInt(req.getParameter("idDoc")));
    doc.setAttestationSejour(map.get("attestationSejour"));
    doc.setPreuvePassageTest(map.get("preuvePassageTest"));
    doc.setRapportFinal(map.get("rapportFinal"));
    doc.setStageReleveNote(map.get("stageReleveNote"));
    doc.setDocumentEnvoieDocument(map.get("envoieDocumentRetour"));
    doc.setPaiementEffectue(map.get("confirmationPaiementRetour"));
    documentsUcc.updateDocumentRetour(doc, Integer.parseInt(req.getParameter("idMobilite")),
        Integer.parseInt(req.getParameter("versionMob")), req.getParameter("etatMobilite"),
        Integer.parseInt(req.getParameter("idDocDepart")));
  }

}
