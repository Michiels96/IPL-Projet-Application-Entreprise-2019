package be.ipl.pae.dal;

import be.ipl.pae.annotations.Inject;
import be.ipl.pae.biz.dto.PartenaireDto;
import be.ipl.pae.biz.factory.BizFactory;
import be.ipl.pae.exceptions.DalException;
import be.ipl.pae.utils.Logging;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

class PartenaireDaoImpl implements PartenaireDao {

  private static final Logger LOGGER = Logging.getLogger(PartenaireDaoImpl.class.getName());

  @Inject
  private BizFactory bf;

  @Inject
  private DalBackendServices dal;

  @Override
  public ArrayList<PartenaireDto> getAllPartenaire() {
    ArrayList<PartenaireDto> partenaires = new ArrayList<PartenaireDto>();
    try (PreparedStatement ps = dal.getPreparedStatement("SELECT * FROM SMMDB.partenaires");
        ResultSet rs = ps.executeQuery()) {

      partenaires = fillDto(rs);
    } catch (SQLException sqle) {
      LOGGER.severe("La méthode getAllPartenaire à échouée avec la "
          + "query : << SELECT * FROM SMMDB.partenaires");
      throw new DalException("Problème avec getAllPartenaire");
    }
    LOGGER.info("La méthode getAllPartenaire a renvoyé la liste de partenaires");
    return partenaires;
  }

  @Override
  public PartenaireDto getPartenaireById(int idPartenaire) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    PartenaireDto dto = null;
    try {
      ps = dal.getPreparedStatement("SELECT * FROM SMMDB.partenaires WHERE id_partenaire = ?");
      ps.setInt(1, idPartenaire);
      rs = ps.executeQuery();
      dto = setAllDto(rs);
    } catch (SQLException sqle) {
      LOGGER.severe("La méthode getPartenaireById à échouée avec la "
          + "query : << SELECT * FROM SMMDB.partenaires WHERE id_partenaire " + idPartenaire);
      throw new DalException("Problème avec rechercherUser");
    } finally {
      closeQuitlyRs(rs);
      closeQuitlyPs(ps);
    }
    LOGGER.info("La méthode getPartenaireById a renvoyé le partenaire avec l'id " + idPartenaire);
    return dto;
  }

  @Override
  public void insererPartenaire(PartenaireDto partenaire) {
    PreparedStatement ps = null;
    try {
      ps = dal.getPreparedStatement("INSERT INTO SMMDB.partenaires "
          + "VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
      ps.setString(1, partenaire.getNomLegal());
      ps.setString(2, partenaire.getNomAffaire());
      ps.setString(3, partenaire.getNomComplet());
      if (!partenaire.getDepartement().equals("")) {
        ps.setString(4, partenaire.getDepartement());
      } else {
        ps.setNull(4, 0);
      }
      ps.setString(5, partenaire.getTypeOrganisation());
      if (partenaire.getNombreEmploye() == 0) {
        ps.setNull(6, 0);
      } else {
        ps.setInt(6, partenaire.getNombreEmploye());
      }
      ps.setString(7, partenaire.getAdresse());
      ps.setInt(8, partenaire.getPays());
      if (!partenaire.getRegion().equals("")) {
        ps.setString(9, partenaire.getRegion());
      } else {
        ps.setNull(9, 0);
      }
      ps.setString(10, partenaire.getCodePostal());
      ps.setString(11, partenaire.getVille());
      ps.setString(12, partenaire.getEmail());
      if (!partenaire.getSiteWeb().equals("")) {
        ps.setString(13, partenaire.getSiteWeb());
      } else {
        ps.setNull(13, 0);
      }
      ps.setString(14, partenaire.getNumTel());
      switch (partenaire.getTypeMobilite()) {
        case "SMS":
          ps.setString(15, "SMS");
          break;
        case "SMP":
          ps.setString(15, "SMP");
          break;
        default:
          LOGGER.severe("mauvais switch");
          break;
      }
      ps.setInt(16, 0);
      ps.execute();
    } catch (SQLException exception) {
      LOGGER.severe("La méthode getPartenaireById à échouée avec la "
          + "query : << INSERT INTO SMMDB.partenaires " + "VALUES (DEFAULT, "
          + partenaire.getNomLegal() + ", " + partenaire.getNomAffaire() + ", "
          + partenaire.getNomComplet() + ", "
          + (!partenaire.getDepartement().equals("") ? partenaire.getSiteWeb() : "null") + ", "
          + partenaire.getTypeOrganisation() + ", "
          + (partenaire.getNombreEmploye() == 0 ? "null" : partenaire.getNombreEmploye()) + ", "
          + partenaire.getAdresse() + ", " + partenaire.getPays() + ", "
          + (!partenaire.getRegion().equals("") ? partenaire.getRegion() : "null") + ", "
          + partenaire.getCodePostal() + ", " + partenaire.getVille() + ", " + partenaire.getEmail()
          + ", " + (!partenaire.getSiteWeb().equals("") ? partenaire.getSiteWeb() : "null") + ", "
          + partenaire.getNumTel() + ", SMS ou SMP, 0");
      throw new DalException("Problème avec insererPartenaire");
    } finally {
      closeQuitlyPs(ps);
    }
  }

  private ArrayList<PartenaireDto> fillDto(ResultSet rs) throws SQLException {
    ArrayList<PartenaireDto> partenaires = new ArrayList<PartenaireDto>();
    while (rs.next()) {
      PartenaireDto dto = bf.getPartenaireDto();
      dto.setIdPartenaire(rs.getInt(1));
      dto.setNomLegal(rs.getString(2));
      dto.setNomAffaire(rs.getString(3));
      dto.setNomComplet(rs.getString(4));
      dto.setDepartement(rs.getString(5));
      dto.setTypeOrganisation(rs.getString(6));
      dto.setNombreEmploye(rs.getInt(7));
      dto.setAdresse(rs.getString(8));
      dto.setPays(rs.getInt(9));
      dto.setRegion(rs.getString(10));
      dto.setCodePostal(rs.getString(11));
      dto.setVille(rs.getString(12));
      dto.setEmail(rs.getString(13));
      dto.setSiteWeb(rs.getString(14));
      dto.setNumTel(rs.getString(15));
      dto.setTypeMobilite(rs.getString(16));
      partenaires.add(dto);
    }
    return partenaires;
  }

  private PartenaireDto setAllDto(ResultSet rs) throws SQLException {
    return fillDto(rs).get(0);
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
