package be.ipl.pae.server;

import be.ipl.pae.annotations.Inject;
import be.ipl.pae.biz.ucc.PaysUcc;
import be.ipl.pae.utils.Logging;

import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;

import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class ServletServicePays {

  private static final Logger LOGGER = Logging.getLogger(ServletServicePays.class.getName());

  @Inject
  private ServletServiceUtils servUtils;
  // @Inject
  // private BizFactory bf;
  @Inject
  private PaysUcc paysUcc;

  private Genson genson = new GensonBuilder().useDateFormat(new SimpleDateFormat("yyyy-MM-dd"))
      .useIndentation(true).useConstructorWithArguments(true).create();


  /**
   * Amène les infos d'un pays vers le front-end.
   * 
   * @param req - infos venant du front-end
   * @param resp - infos allant vers le front-end
   */
  void getPaysById(HttpServletRequest req, HttpServletResponse resp) {
    String json =
        genson.serialize(paysUcc.getPaysById(Integer.parseInt(req.getParameter("idPays"))));
    LOGGER.info("Le pays à été récupéré.");
    servUtils.endMessageLauncher(resp, servUtils.SUCCESS, json);
  }

  /**
   * Amène la liste des pays vers le front-end.
   * 
   * @param req - infos venant du front-end
   * @param resp - infos allant vers le front-end
   */
  void getAllPays(HttpServletRequest req, HttpServletResponse resp) {
    String json = genson.serialize(paysUcc.getAllPays());
    LOGGER.info("Tous les pays ont été récupérés.");
    servUtils.endMessageLauncher(resp, servUtils.SUCCESS, json);
  }

}
