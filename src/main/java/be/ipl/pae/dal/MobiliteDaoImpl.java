package be.ipl.pae.dal;

import be.ipl.pae.annotations.Inject;
import be.ipl.pae.biz.dto.MobiliteDto;
import be.ipl.pae.biz.factory.BizFactory;
import be.ipl.pae.enums.EtatMobilite;
import be.ipl.pae.enums.Periode;
import be.ipl.pae.enums.TypeProgramme;
import be.ipl.pae.exceptions.DalException;
import be.ipl.pae.exceptions.VersionException;
import be.ipl.pae.utils.Logging;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

class MobiliteDaoImpl implements MobiliteDao {

  private static final Logger LOGGER = Logging.getLogger(MobiliteDaoImpl.class.getName());
  @Inject
  private BizFactory bf;

  @Inject
  private DalBackendServices dal;

  @Override
  public ArrayList<MobiliteDto> getMobiliteParPseudo(String pseudo) {
    ArrayList<MobiliteDto> mobilites;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dal.getPreparedStatement(
          "SELECT * FROM SMMDB.mobilites WHERE etudiant = (SELECT id_utilisateur "
              + "FROM SMMDB.utilisateurs WHERE pseudo = ?)");
      ps.setString(1, pseudo);
      rs = ps.executeQuery();
      mobilites = fillDto(rs);
    } catch (SQLException sqle) {
      LOGGER.severe("La méthode getMobiliteParPseudo à échouée avec la "
          + "query : << SELECT * FROM SMMDB.mobilites WHERE etudiant = "
          + "(SELECT id_utilisateur FROM SMMDB.utilisateurs WHERE pseudo = " + pseudo + ")");
      throw new DalException("Problème avec getMobiliteParPseudo");
    } finally {
      closeQuitlyRs(rs);
      closeQuitlyPs(ps);
    }
    LOGGER
        .info("La méthode getMobiliteParPseudo a renvoyé la liste des mobilités pour l'utilisateur "
            + pseudo);
    return mobilites;
  }

  @Override
  public ArrayList<MobiliteDto> getAllMobilite() {
    ArrayList<MobiliteDto> mobilites;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dal.getPreparedStatement("SELECT * FROM SMMDB.mobilites");
      rs = ps.executeQuery();
      mobilites = fillDto(rs);
    } catch (SQLException sqle) {
      LOGGER.severe("La méthode getAllMobilite à échouée avec la "
          + "query : << SELECT * FROM SMMDB.mobilites >>");
      throw new DalException("Problème avec getAllMobilite");
    } finally {
      closeQuitlyRs(rs);
      closeQuitlyPs(ps);
    }
    LOGGER.info("La méthode getAllMobilite a renvoyé la liste des mobilités");
    return mobilites;
  }

  @Override
  public MobiliteDto getMobiliteParId(int id) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    MobiliteDto mobilites;
    try {
      ps = dal.getPreparedStatement("SELECT * FROM SMMDB.mobilites WHERE id_mobilite = ?");
      ps.setInt(1, id);
      rs = ps.executeQuery();
      mobilites = bf.getMobiliteDto();
      mobilites = fillDto(rs).get(0);
    } catch (SQLException sqle) {
      LOGGER.severe("La méthode getMobiliteParId à échouée avec la "
          + "query : << SELECT * FROM SMMDB.mobilites WHERE id_mobilite = " + id + " >>");
      throw new DalException("Problème avec getMobiliteParId");
    } finally {
      closeQuitlyRs(rs);
      closeQuitlyPs(ps);
    }
    LOGGER.info("La méthode getMobiliteParId a renvoyé la mobilité avec l'id passé en paramètre");
    return mobilites;
  }

  @Override
  public void insertMobilite(MobiliteDto mobilite) {
    PreparedStatement ps = null;
    try {
      ps = dal.getPreparedStatement(
          "INSERT INTO SMMDB.mobilites VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
      if (mobilite.getProgramme() != null) {
        ps.setString(1, mobilite.getProgramme().getTypeProgramme());
      } else {
        ps.setString(1, "fame");
      }
      ps.setBigDecimal(2, new BigDecimal(mobilite.getFinancement()));
      ps.setString(3, mobilite.getPeriode().getPeriode());
      if (mobilite.getPartenaire() == 0) {
        ps.setNull(4, 0);
      } else {
        ps.setInt(4, mobilite.getPartenaire());
      }
      ps.setInt(5, mobilite.getEtudiant());
      ps.setNull(6, 0);
      ps.setNull(7, 0);
      ps.setInt(8, mobilite.getNiveauPreference());
      ps.setString(9, mobilite.getEtatMobilite().getEtatMobilite());
      ps.setBoolean(10, mobilite.isMobiliteAnnule());
      if (mobilite.getPays() == 0) {
        ps.setNull(11, 0);
      } else {
        ps.setInt(11, mobilite.getPays());
      }
      ps.setDate(12, Date.valueOf(LocalDate.now()));
      ps.setString(13, mobilite.getTypeMobilite());
      ps.setInt(14, 0);
      ps.execute();
    } catch (SQLException exception) {
      LOGGER.info(
          "La méthode insertMobilite à échoué avec la " + "query << " + ps.toString() + " >>.");
      throw new DalException("Problème avec insertMobilite");
    } finally {
      closeQuitlyPs(ps);
    }
    LOGGER.info("La méthode insertMobilite a bien été effectué.");

  }

  @Override
  public void updateMobilite(MobiliteDto mobilite) throws VersionException {
    PreparedStatement ps = null;
    try {
      ps = dal.getPreparedStatement("update SMMDB.mobilites set id_mobilite = ?, programme = ?,"
          + " periode = ?," + " partenaire = ?, " + "niveau_preference = ?, mobilite_annule = ?, "
          + "pays = ?, type_mobilite = ?, version = version+1 "
          + "where id_mobilite = ? AND version = ?");
      ps.setInt(1, mobilite.getIdMobilite());
      ps.setString(2, mobilite.getProgramme().getTypeProgramme());
      ps.setString(3, mobilite.getPeriode().getPeriode());
      if (mobilite.getPartenaire() == 0) {
        ps.setNull(4, 0);
      } else {
        ps.setInt(4, mobilite.getPartenaire());
      }

      ps.setInt(5, mobilite.getNiveauPreference());
      ps.setBoolean(6, mobilite.isMobiliteAnnule());
      // Manque cet attributs dans les implémentations
      ps.setInt(7, mobilite.getPays());
      ps.setString(8, mobilite.getTypeMobilite());
      ps.setInt(9, mobilite.getIdMobilite());
      ps.setInt(10, mobilite.getVersion());
      ps.executeUpdate();
    } catch (SQLException sqle) {
      LOGGER.severe( // TODO
          "La méthode updateMobilite à échouée (Avec la query suivante) " + ps.toString());

      if (getVersionMobilite(mobilite) != mobilite.getVersion()) {
        throw new VersionException("La version a changée");
      } else {
        throw new DalException("Problème avec updateMobilite");
      }
    } finally {
      closeQuitlyPs(ps);
    }
    LOGGER.info("La mobilité " + mobilite.getIdMobilite() + " à modifiée des infos.");
  }

  @Override
  public void removeMobilite(MobiliteDto mobilite) {
    PreparedStatement ps = null;
    try {
      ps = dal.getPreparedStatement("delete from SMMDB.mobilites where id_mobilite = ?");
      ps.setInt(1, mobilite.getIdMobilite());
      ps.executeUpdate();
      LOGGER.info("La mobilite " + mobilite.getIdMobilite() + " à été enlevée de la DB");
    } catch (SQLException sqle) {
      LOGGER.severe("La méthode deleteUser à échoué avec la"
          + " query : << delete from SMMDB.mobilites where id_mobilite = "
          + mobilite.getIdMobilite() + ".");
      throw new DalException("Problème avec deleteMobilite");
    } finally {
      closeQuitlyPs(ps);
    }
  }

  @Override
  public int getNbMobiliteParPseudo(String pseudo) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dal.getPreparedStatement(
          "SELECT count(id_mobilite) FROM SMMDB.mobilites WHERE etudiant = (SELECT id_utilisateur "
              + "FROM SMMDB.utilisateurs WHERE pseudo = ?) AND etat_mobilite = VALIDE");
      ps.setString(1, pseudo);
      rs = ps.executeQuery();
      return rs.getInt(1);
    } catch (SQLException exception) {
      LOGGER.severe("La méthode getMobiliteParId à échouée avec la "
          + "query : << SELECT count(id_mobilite) FROM SMMDB.mobilites "
          + "WHERE etudiant = (SELECT id_utilisateur FROM SMMDB.utilisateurs WHERE pseudo = "
          + pseudo + ") AND etat_mobilite = VALIDE >>");
      throw new DalException("Problème avec getNbMobiliteParPseudo");
    } finally {
      closeQuitlyRs(rs);
      closeQuitlyPs(ps);
    }
  }

  private ArrayList<MobiliteDto> fillDto(ResultSet rs) throws SQLException {
    ArrayList<MobiliteDto> mobilites = new ArrayList<MobiliteDto>();
    while (rs.next()) {
      MobiliteDto dto = bf.getMobiliteDto();
      dto.setIdMobilite(rs.getInt(1));
      switch (rs.getString(2)) {
        case "fame":
          dto.setProgramme(TypeProgramme.FAME);
          break;
        case "erasmus+":
          dto.setProgramme(TypeProgramme.ERASMUS);
          break;
        case "erabel":
          dto.setProgramme(TypeProgramme.ERABEL);
          break;
        case "autre":
          dto.setProgramme(TypeProgramme.AUTRE);
          break;
        default:
          LOGGER.severe("Mauvais switch");
          break;
      }
      dto.setFinancement(rs.getDouble(3));
      switch (rs.getString(4)) {
        case "Q1":
          dto.setPeriode(Periode.Q1);
          break;
        case "Q2":
          dto.setPeriode(Periode.Q2);
          break;
        default:
          LOGGER.severe("Mauvais switch");
          break;
      }
      dto.setPartenaire(rs.getInt(5));
      dto.setEtudiant(rs.getInt(6));
      dto.setDocumentsDepart(rs.getInt(7));
      dto.setDocumentsRetour(rs.getInt(8));
      dto.setNiveauPreference(rs.getInt(9));
      switch (rs.getString(10)) {
        case "CREE":
          dto.setEtatMobilite(EtatMobilite.CREE);
          break;
        case "EN_PREPARATION":
          dto.setEtatMobilite(EtatMobilite.EN_PREPARATION);
          break;
        case "A_PAYER_SOLDE":
          dto.setEtatMobilite(EtatMobilite.A_PAYER_SOLDE);
          break;
        case "A_PAYER":
          dto.setEtatMobilite(EtatMobilite.A_PAYER);
          break;
        default:
          LOGGER.severe("Mauvais switch");
          break;
      }
      dto.setMobiliteAnnule(rs.getBoolean(11));
      dto.setPays(rs.getInt(12));
      dto.setDateIntroduction(rs.getTimestamp(13));
      dto.setTypeMobilite(rs.getString(14));
      dto.setVersion(rs.getInt(15));
      mobilites.add(dto);
    }
    return mobilites;
  }

  private int getVersionMobilite(MobiliteDto mobiliteDto) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dal.getPreparedStatement("select version from SMMDB.mobilites where id_mobilite = ?");
      ps.setInt(1, mobiliteDto.getIdMobilite());
      rs = ps.executeQuery();
      if (rs.next()) {
        return rs.getInt(1);
      }
    } catch (SQLException sqle) {
      LOGGER.severe("Problème query private getVersionMobilite avec la mobilite "
          + mobiliteDto.getIdMobilite());
      throw new DalException("Problème avec getVersionMobilite");
    } finally {
      closeQuitlyRs(rs);
      closeQuitlyPs(ps);
    }
    return -1;
  }

  @Override
  public void validerMobilite(MobiliteDto mob) {
    PreparedStatement ps = null;
    try {
      ps = dal.getPreparedStatement(
          "update SMMDB.mobilites set etat_mobilite = ?, version =  version +1 ,"
              + "documents_depart = ?, documents_retour = ? where id_mobilite = ? AND version= ? ");
      ps.setString(1, mob.getEtatMobilite().getEtatMobilite());
      ps.setInt(2, mob.getDocumentsDepart());
      ps.setInt(3, mob.getDocumentsRetour());
      ps.setInt(4, mob.getIdMobilite());
      ps.setInt(5, mob.getVersion());
      ps.executeUpdate();
    } catch (SQLException sqle) {
      LOGGER
          .severe("La méthode updateMobilite à échouée (Avec la query suivante) " + ps.toString());
      throw new DalException();
    } finally {
      closeQuitlyPs(ps);
    }
    LOGGER.info("La mobilité " + mob.getIdMobilite() + " à été modifiée.");

  }

  @Override
  public void insertMessageAnnulationMobilite(MobiliteDto mob, String message) {
    PreparedStatement ps = null;
    if (message.equals("vide")) {
      message = "Aucune raison n'a été donnée";
    }
    try {
      ps = dal.getPreparedStatement(
          "insert into SMMDB.messages_annulation values(DEFAULT, ?, ?, DEFAULT,?)");
      ps.setInt(1, mob.getEtudiant());
      ps.setInt(2, mob.getIdMobilite());
      ps.setString(3, message);
      ps.execute();
    } catch (SQLException sqle) {
      LOGGER.severe("La méthode insertMessageAnnulationMobilite"
          + " à échouée (Avec la query suivante) " + ps.toString());
      throw new DalException();
    } finally {
      closeQuitlyPs(ps);
    }
    LOGGER.info("Message inséré.");
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

  @Override
  public Map<Integer, String> getNotificationsParId(int id) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    Map<Integer, String> map = new HashMap<Integer, String>();
    try {
      ps = dal.getPreparedStatement("SELECT * FROM SMMDB.messages_annulation "
          + "WHERE etudiant = ? AND est_etudiant_averti = FALSE");
      ps.setInt(1, id);
      rs = ps.executeQuery();
      while (rs.next()) {
        map.put(rs.getInt(3), rs.getString(5));
        notifsLues(rs.getInt(1));
      }
      return map;
    } catch (SQLException exception) {
      LOGGER.severe(
          "La méthode getNotificationsParId à échouée avec la " + "query : " + ps.toString());
      throw new DalException("Problème avec getNbMobiliteParPseudo");
    } finally {
      closeQuitlyRs(rs);
      closeQuitlyPs(ps);
    }
  }

  private void notifsLues(int id) {
    // Nul besoin d'optimistic lock dans ce cas-ci
    PreparedStatement ps = null;
    try {
      ps = dal.getPreparedStatement(
          "update SMMDB.messages_annulation set est_etudiant_averti = TRUE where id_message = ?");
      ps.setInt(1, id);
      ps.executeUpdate();
    } catch (SQLException sqle) {
      LOGGER.severe("La méthode notifsLues à échouée (Avec la query suivante) " + ps.toString());
      throw new DalException();
    } finally {
      closeQuitlyPs(ps);
    }
    LOGGER.info("La notif " + id + " à été lue.");

  }
}
