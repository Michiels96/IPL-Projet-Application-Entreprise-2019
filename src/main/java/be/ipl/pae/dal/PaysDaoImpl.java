package be.ipl.pae.dal;

import be.ipl.pae.annotations.Inject;
import be.ipl.pae.biz.dto.PaysDto;
import be.ipl.pae.biz.factory.BizFactory;
import be.ipl.pae.enums.TypeProgramme;
import be.ipl.pae.exceptions.DalException;
import be.ipl.pae.utils.Logging;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

class PaysDaoImpl implements PaysDao {

  private static final Logger LOGGER = Logging.getLogger(PaysDaoImpl.class.getName());

  @Inject
  private BizFactory bf;

  @Inject
  private DalBackendServices dal;

  @Override
  public PaysDto getPaysById(int paysId) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    PaysDto pays = null;
    try {
      ps = dal.getPreparedStatement("SELECT * FROM SMMDB.pays WHERE id_pays = ?");
      ps.setInt(1, paysId);
      rs = ps.executeQuery();
      while (rs.next()) {
        pays = bf.getPaysDto();
        pays.setIdPays(rs.getInt(1));
        pays.setNom(rs.getString(2));
        pays.setCodePays(rs.getString(3));
        switch (rs.getString(4)) {
          case "erasmus+":
            pays.setProgramme(TypeProgramme.ERASMUS);
            break;
          case "fame":
            pays.setProgramme(TypeProgramme.FAME);
            break;
          case "erabel":
            pays.setProgramme(TypeProgramme.ERABEL);
            break;
          default:
            break;
        }
      }
    } catch (SQLException sqle) {
      LOGGER.severe("La méthode getPaysById à échouée avec la "
          + "query : << SELECT * FROM SMMDB.pays WHERE id_pays = " + paysId);
      throw new DalException("Problème avec rechercherUser");
    } finally {
      closeQuitlyRs(rs);
      closeQuitlyPs(ps);
    }
    LOGGER.info("La méthode getPaysById a renvoyé un pays avec l'id : " + paysId);
    return pays;
  }

  @Override
  public ArrayList<PaysDto> getAllPays() {
    ArrayList<PaysDto> listePays = new ArrayList<PaysDto>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dal.getPreparedStatement("SELECT * FROM SMMDB.pays");
      rs = ps.executeQuery();
      while (rs.next()) {
        PaysDto pays = null;
        pays = bf.getPaysDto();
        pays.setIdPays(rs.getInt(1));
        pays.setNom(rs.getString(2));
        pays.setCodePays(rs.getString(3));
        switch (rs.getString(4)) {
          case "erasmus+":
            pays.setProgramme(TypeProgramme.ERASMUS);
            break;
          case "fame":
            pays.setProgramme(TypeProgramme.FAME);
            break;
          case "erabel":
            pays.setProgramme(TypeProgramme.ERABEL);
            break;
          default:
            break;
        }
        listePays.add(pays);
      }
    } catch (SQLException sqle) {
      LOGGER.severe(
          "La méthode getAllPays à échouée avec la " + "query : << SELECT * FROM SMMDB.pays");
      throw new DalException("Problème avec getAllPays au niveau de la requête SQL");
    } finally {
      closeQuitlyRs(rs);
      closeQuitlyPs(ps);
    }
    LOGGER.info("La méthode getPaysById a renvoyé une liste de pays");
    return listePays;
  }

  private void closeQuitlyRs(ResultSet rs) {
    if (rs != null) {
      try {
        rs.close();
      } catch (SQLException sqle) {
        LOGGER.severe(sqle.getMessage());
      }
    }
  }

  private void closeQuitlyPs(PreparedStatement ps) {
    if (ps != null) {
      try {
        ps.close();
      } catch (SQLException sqle) {
        LOGGER.severe(sqle.getMessage());
      }
    }
  }
}
