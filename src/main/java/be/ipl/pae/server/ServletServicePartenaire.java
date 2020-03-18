package be.ipl.pae.server;

import be.ipl.pae.annotations.Inject;
import be.ipl.pae.biz.dto.PartenaireDto;
import be.ipl.pae.biz.factory.BizFactory;
import be.ipl.pae.biz.ucc.PartenaireUcc;
import be.ipl.pae.biz.ucc.PaysUcc;
import be.ipl.pae.utils.Logging;

import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class ServletServicePartenaire {

  private static final Logger LOGGER = Logging.getLogger(ServletServicePartenaire.class.getName());

  @Inject
  private ServletServiceUtils servUtils;
  @Inject
  private BizFactory bf;
  @Inject
  private PartenaireUcc partenaireUcc;
  @Inject
  PaysUcc paysUcc;
  private Genson genson = new GensonBuilder().useDateFormat(new SimpleDateFormat("yyyy-MM-dd"))
      .useIndentation(true).useConstructorWithArguments(true).create();


  /**
   * Méthode qui amène vers le front-end la liste de tous les partenaires présent en BD.
   * 
   * @param req - infos venant du front-end
   * @param resp - infos allant vers le front-en
   */
  void getAllPartenaire(HttpServletRequest req, HttpServletResponse resp) {
    String json = genson.serialize(partenaireUcc.getAllPartenaire());
    LOGGER.info("Tous les partenaires ont été récupérés.");
    servUtils.endMessageLauncher(resp, servUtils.SUCCESS, json);
  }

  /**
   * Méthode qui permet d'amener les infos concernant un partenaire vers le front-end.
   * 
   * @param req - infos venant du front-end
   * @param resp - infos allant vers le front-end
   */
  void getPartenaireById(HttpServletRequest req, HttpServletResponse resp) {
    String json = genson.serialize(
        partenaireUcc.getPartenaireById(Integer.parseInt(req.getParameter("idPartenaire"))));
    LOGGER.info("Le partenaire a été récupéré.");
    servUtils.endMessageLauncher(resp, servUtils.SUCCESS, json);
  }

  /**
   * Ajoute un partenaire.
   * 
   * @param req - les données venant du frontend
   * @param resp - les données allant vers le frontend
   */
  public void ajouterPartenaire(HttpServletRequest req, HttpServletResponse resp) {
    // TODO Auto-generated method stub
    Map<String, String> info = genson.deserialize(req.getParameter("info"), Map.class);
    PartenaireDto partenaire = bf.getPartenaireDto();
    partenaire.setAdresse(info.get("adressePartenaire"));
    partenaire.setCodePostal(info.get("codePostalPartenaire"));
    partenaire.setDepartement(info.get("departement"));
    if (!servUtils.mailValidator(info.get("mail"))) {
      servUtils.endMessageLauncher(resp, servUtils.ACCES_REFUSE, "adresseMailInvalide");
      return;
    }
    partenaire.setEmail(info.get("mail"));
    partenaire.setNomAffaire(info.get("nomBuisness"));
    if (!info.get("nbEmploye").equals("")) {
      partenaire.setNombreEmploye(Integer.parseInt(info.get("nbEmploye")));
    } else {
      partenaire.setNombreEmploye(0);
    }
    partenaire.setNomComplet(info.get("nomCompletLegal"));
    partenaire.setNomLegal(info.get("nomLegal"));
    partenaire.setNumTel(info.get("numTelPartenaire"));
    partenaire.setPays(Integer.parseInt(info.get("select-pays")));
    partenaire.setRegion(info.get("region"));
    partenaire.setSiteWeb(info.get("siteWeb"));
    partenaire.setTypeMobilite(info.get("typeMobilite"));
    partenaire.setTypeOrganisation(info.get("typeOrganisation"));
    partenaire.setVille(info.get("ville"));
    partenaireUcc.addPartenaire(partenaire);
    LOGGER.info("partenaire ajouté");
    servUtils.endMessageLauncher(resp, servUtils.SUCCESS, "yeah");

  }

  /**
   * Complète une liste de partenaire.
   * 
   * @param retour - liste de data de partenaire
   * @return une liste de data
   */
  public List<String> autocompletePartenaire(List<String> retour) {
    for (PartenaireDto dto : partenaireUcc.getAllPartenaire()) {
      retour.add(dto.getNomComplet() + " pays : " + paysUcc.getPaysById(dto.getPays()).getNom()
          + " ville : " + dto.getVille() + " [partenaire]");
    }
    return retour;
  }

}
