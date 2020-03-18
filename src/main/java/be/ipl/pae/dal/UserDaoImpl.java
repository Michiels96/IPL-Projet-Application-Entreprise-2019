package be.ipl.pae.dal;

import be.ipl.pae.annotations.Inject;
import be.ipl.pae.biz.dto.UserDto;
import be.ipl.pae.biz.factory.BizFactory;
import be.ipl.pae.enums.Role;
import be.ipl.pae.exceptions.DalException;
import be.ipl.pae.exceptions.VersionException;
import be.ipl.pae.utils.Logging;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.logging.Logger;

class UserDaoImpl implements UserDao {

  private static final Logger LOGGER = Logging.getLogger(UserDaoImpl.class.getName());

  @Inject
  private BizFactory bf;
  @Inject
  private DalBackendServices dal;


  // Boolean permettant de savoir si il y a un admin (premier user en BD)
  private static boolean adminPresent;

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.dal.UserDao#isAdminPresent()
   */
  @Override
  public void isAdminPresent() {
    // Si admin présent, ne pas revérifier ?
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dal.getPreparedStatement("SELECT count(id_utilisateur) FROM SMMDB.utilisateurs");
      rs = ps.executeQuery();
      while (rs.next()) {
        if (rs.getInt(1) > 0) {
          LOGGER.info("L'admin est présent");
          adminPresent = true;
        } else {
          LOGGER.info("L'admin est absent");
          adminPresent = false;
        }
      }
    } catch (SQLException sqlE) {
      LOGGER.severe("La méthode isAdminPresent a échoué avec la"
          + " query : << SELECT count(id_utilisateur) FROM SMMDB.utilisateurs >>.");
      throw new DalException();
    } finally {
      closeQuitlyRs(rs);
      closeQuitlyPs(ps);
    }
    System.out.println(adminPresent);
  }


  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.dal.UserDao#findUserByLogin(java.lang.String)
   */
  @Override
  public UserDto findUserByLogin(String pseudo) {
    return findUserByIdOrLogin(false, 0, pseudo);
  }

  @Override
  public UserDto findUserById(int id) {
    return findUserByIdOrLogin(true, id, null);
  }

  private UserDto findUserByIdOrLogin(boolean isId, int id, String pseudo) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    UserDto dto;
    try {
      dto = bf.getUserDto();
      if (isId) {
        ps = dal.getPreparedStatement("SELECT * FROM SMMDB.utilisateurs WHERE id_utilisateur = ?");
        ps.setInt(1, id);
      } else {
        ps = dal.getPreparedStatement("SELECT * " + "FROM SMMDB.utilisateurs WHERE pseudo = ?");
        ps.setString(1, pseudo);
      }
      rs = ps.executeQuery();
      while (rs.next()) {
        dto.setPseudo(rs.getString(2));
        dto.setMotDePasse(rs.getString(3));
        dto.setNom(rs.getString(4));
        dto.setPrenom(rs.getString(5));
        dto.setIdUtilisateur(rs.getInt(1));
        dto.setEmail(rs.getString(6));
        dto.setDateNaissance(rs.getTimestamp(8));
        dto.setDateInscription(rs.getTimestamp(9));
        dto.setAdresse(rs.getString(10));
        dto.setCommune(rs.getString(11));
        dto.setCodePostal(rs.getInt(12));
        if (rs.getString(13) != null) {
          dto.setNumTel(rs.getString(13));
        } else {
          dto.setNumTel("0");
        }
        dto.setHomme(rs.getBoolean(14));
        dto.setNombreAnneeReussie(rs.getInt(15));
        dto.setNumCompte(rs.getString(16));
        dto.setTitulaireCompte(rs.getString(17));
        dto.setNomBanque(rs.getString(18));
        if (rs.getString(19) != null) {
          dto.setCodeBic(rs.getString(19));
        } else {
          dto.setCodeBic("0");
        }
        dto.setNationalite(rs.getInt(20));
        dto.setVersion(rs.getInt(21));

        switch (rs.getString(7)) {
          case "A":
            dto.setRole(Role.A);
            break;
          case "P":
            dto.setRole(Role.P);
            break;
          case "E":
            dto.setRole(Role.E);
            break;
          default:
            LOGGER.severe("Mauvais switch");
            break;
        }
      }
    } catch (SQLException sqle) {
      LOGGER.severe("La méthode findUserByIdorLogin a échoué avec la query : "
          + "<< SELECT nom, prenom, pseudo, mot_de_passe, role "
          + "FROM SMMDB.utilisateurs WHERE id_utilisateur = " + id + " ou pseudo = " + pseudo
          + ">>.");
      throw new DalException("Problème avec findUserByIdorLogin");
    } finally {
      closeQuitlyRs(rs);
      closeQuitlyPs(ps);
    }
    LOGGER.info("La méthode findUserByIdorLogin a renvoyé "
        + (dto.getPseudo() == null ? "null" : dto.getPseudo().toString()) + ".");
    return dto.getPseudo() == null ? null : dto;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.dal.UserDao#insertUser(be.ipl.pae.biz.dto.UserDto)
   */
  @Override
  public void insertUser(UserDto userDto) {
    PreparedStatement ps = null;
    try {
      ps = dal.getPreparedStatement("insert into SMMDB.utilisateurs(pseudo, mot_de_passe, nom,"
          + " prenom, email, role, date_inscription) values(?, ?, ?, ?, ?, ?, DEFAULT)");
      ps.setString(1, userDto.getPseudo());
      ps.setString(2, userDto.getMotDePasse());
      ps.setString(3, userDto.getNom());
      ps.setString(4, userDto.getPrenom());
      ps.setString(5, userDto.getEmail());
      if (!adminPresent) {
        ps.setString(6, "A");
      } else {
        ps.setString(6, "E");
      }
      ps.executeUpdate();
    } catch (SQLException sqle) {
      LOGGER.severe("La méthode insertUser à échoué avec la query : << insert into "
          + "SMMDB.utilisateurs(pseudo, mot_de_passe, nom, prenom, email, role, date_inscription) "
          + "values(" + userDto.getPseudo() + ", ******, " + userDto.getNom() + ", "
          + userDto.getPrenom() + ", " + userDto.getPrenom() + ", " + userDto.getEmail() + ") >>.");
      throw new DalException("Problème avec insertUser");
    } finally {
      closeQuitlyPs(ps);
    }
    LOGGER.info("L'utilisateur " + userDto.getPseudo() + " a été inséré dans la BD.");
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.dal.UserDao#addInfosUser(be.ipl.pae.biz.dto.UserDto)
   */
  @Override
  public void addInfosUser(UserDto userDto, String nouveauPseudo) throws VersionException {
    PreparedStatement ps = null;
    try {
      ps = dal.getPreparedStatement(
          "update SMMDB.utilisateurs set pseudo = ?, mot_de_passe = ?, nom = ?, prenom = ?,"
              + " email = ?, role = ?,  date_naissance = ?, adresse = ?, "
              + "commune = ?, code_postal = ?, num_tel = ?, "
              + "esthomme = ?, nombre_annee_reussie = ?, num_compte = ?, titulaire_compte = ?, "
              + "nom_banque = ?, code_bic = ?, nationalite = ?, version = version+1 "
              + "where pseudo = ? AND version = ?");
      ps.setString(1, nouveauPseudo);
      ps.setString(2, userDto.getMotDePasse());
      ps.setString(3, userDto.getNom());
      ps.setString(4, userDto.getPrenom());
      ps.setString(5, userDto.getEmail());
      ps.setString(6, "" + userDto.getRole());
      if (userDto.getDateNaissance() != null) {
        ps.setTimestamp(7, new Timestamp(userDto.getDateNaissance().getTime()));
      } else {
        ps.setNull(7, Types.TIMESTAMP_WITH_TIMEZONE);
      }
      if (userDto.getAdresse() != null) {
        ps.setString(8, userDto.getAdresse());
      } else {
        ps.setNull(8, Types.VARCHAR);
      }
      if (userDto.getCommune() != null) {
        ps.setString(9, userDto.getCommune());
      } else {
        ps.setNull(9, Types.VARCHAR);
      }
      if (userDto.getCodePostal() != 0) {
        ps.setInt(10, userDto.getCodePostal());
      } else {
        ps.setNull(10, Types.INTEGER);
      }
      if (userDto.getNumTel() != null) {
        ps.setString(11, userDto.getNumTel());
      } else {
        ps.setNull(11, Types.VARCHAR);
      }
      ps.setBoolean(12, userDto.isHomme());
      ps.setInt(13, userDto.getNombreAnneeReussie());
      if (userDto.getNumCompte() != null) {
        ps.setString(14, userDto.getNumCompte());
      } else {
        ps.setNull(14, Types.VARCHAR);
      }
      if (userDto.getTitulaireCompte() != null) {
        ps.setString(15, userDto.getTitulaireCompte());
      } else {
        ps.setNull(15, Types.VARCHAR);
      }
      if (userDto.getNomBanque() != null) {
        ps.setString(16, userDto.getNomBanque());
      } else {
        ps.setNull(16, Types.VARCHAR);
      }
      if (userDto.getCodeBic() != null) {
        ps.setString(17, userDto.getCodeBic());
      } else {
        ps.setNull(17, Types.VARCHAR);
      }
      // comme les utilisateurs qui n'ont pas de pays on la valeur 0 de base, il faut eviter de les
      // ajouter
      if (userDto.getNationalite() != 0) {
        ps.setInt(18, userDto.getNationalite());
      } else {
        ps.setNull(18, Types.INTEGER);
      }
      ps.setString(19, userDto.getPseudo());
      ps.setInt(20, userDto.getVersion());
      ps.executeUpdate();
    } catch (SQLException sqle) {
      LOGGER.severe("La méthode addInfosUser à échouée (Avec la query suivante) "
          + "update SMMDB.utilisateurs set pseudo = " + nouveauPseudo + ", mot_de_passe = "
          + userDto.getMotDePasse() + ", nom = " + userDto.getNom() + ", prenom = "
          + userDto.getPrenom() + ", email = " + userDto.getEmail() + ", role = "
          + userDto.getRole() + ", date_naissance = "
          + new Timestamp(userDto.getDateNaissance().getTime()) + "" + ", adresse = "
          + userDto.getAdresse() + "," + "commune = " + userDto.getCommune() + ", code_postal = "
          + userDto.getCodePostal() + ", num_tel = " + userDto.getNumTel() + ","
          + "esthomme = H, nombre_annee_reussie = " + userDto.getNombreAnneeReussie()
          + ", num_compte = " + userDto.getNumCompte() + ", titulaire_compte = "
          + userDto.getTitulaireCompte() + "," + "nom_banque = " + userDto.getNomBanque()
          + ", code_bic = " + userDto.getCodeBic() + ", nationalite = " + userDto.getNationalite()
          + " where pseudo = " + userDto.getPseudo() + " AND version = " + userDto.getVersion());
      if (getVersionUser(userDto) != userDto.getVersion()) {
        throw new VersionException("La version a changée");
      } else {
        throw new DalException("Problème avec addInfosUser");
      }
    } finally {
      closeQuitlyPs(ps);
    }
    LOGGER.info("L'utilisateur " + userDto.getPseudo() + " à ajouté des infos.");
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.dal.UserDao#deleteUser(be.ipl.pae.biz.dto.UserDto)
   */
  @Override
  public void deleteUser(UserDto userDto) {
    // TODO vérifier qu'il en reste un pour l'admin.
    PreparedStatement ps = null;
    try {
      ps = dal.getPreparedStatement("delete from SMMDB.utilisateurs where pseudo = ?");
      ps.setString(1, userDto.getPseudo());
      ps.executeQuery();
      LOGGER.info("L'utilisateur " + userDto.getPseudo() + " à été enlevé de la DB");
    } catch (SQLException sqle) {
      LOGGER.severe("La méthode deleteUser à échoué avec la"
          + " query : << delete from SMMDB.utilisateurs where pseudo = " + userDto.getPseudo()
          + ".");
      throw new DalException("Problème avec deleteUser");
    } finally {
      closeQuitlyPs(ps);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.dal.UserDao#rechercherUser(be.ipl.pae.biz.dto.UserDto)
   */
  @Override
  public ArrayList<UserDto> rechercherUser(String pattern) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    ArrayList<UserDto> liste = new ArrayList<UserDto>();
    try {
      ps = dal.getPreparedStatement("SELECT nom, prenom, pseudo FROM SMMDB.utilisateurs"
          + " WHERE pseudo LIKE ? OR nom LIKE ? OR prenom LIKE ?");
      ps.setString(1, pattern);
      ps.setString(2, pattern);
      ps.setString(3, pattern);
      rs = ps.executeQuery();
      UserDto dto = bf.getUserDto();
      dto.setPseudo(rs.getString(3));
      dto.setNom(rs.getString(1));
      dto.setPrenom(rs.getString(2));
      liste.add(dto);
    } catch (SQLException sqle) {
      LOGGER.severe("La méthode rechercherUser à échouée avec la "
          + "query : << SELECT nom, prenom, pseudo FROM SMMDB.utilisateurs" + " WHERE pseudo LIKE "
          + pattern + " OR nom LIKE " + pattern + " OR prenom LIKE " + pattern + ".");
      throw new DalException("Problème avec rechercherUser");
    } finally {
      closeQuitlyRs(rs);
      closeQuitlyPs(ps);
    }
    LOGGER
        .info("La méthode rechercherUser à renvoyé la liste d'utilisateur correspondant au pattern "
            + pattern + ".");
    return liste;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.dal.UserDao#listerUser(be.ipl.pae.biz.dto.UserDto)
   */
  @Override
  public ArrayList<UserDto> listerUser() {
    ArrayList<UserDto> list = new ArrayList<UserDto>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dal
          .getPreparedStatement("SELECT pseudo, nom, prenom, email, role FROM SMMDB.utilisateurs");
      rs = ps.executeQuery();
      while (rs.next()) {
        UserDto dto = bf.getUserDto();
        dto.setPseudo(rs.getString(1));
        dto.setNom(rs.getString(2));
        dto.setPrenom(rs.getString(3));
        dto.setEmail(rs.getString(4));
        switch (rs.getString(5)) {
          case "A":
            dto.setRole(Role.A);
            break;
          case "P":
            dto.setRole(Role.P);
            break;
          case "E":
            dto.setRole(Role.E);
            break;
          default:
            LOGGER.severe("What the switch ?!");
            break;
        }
        list.add(dto);
      }
    } catch (SQLException exception) {
      LOGGER.severe("La méthode listerUser à echouée avec "
          + "la query : << SELECT pseudo, nom, prenom, email FROM SMMDB.utilisateurs >>.");
      throw new DalException("Problème avec listerUser");
    } finally {
      closeQuitlyRs(rs);
      closeQuitlyPs(ps);
    }
    LOGGER.info("La méthode listerUser à renvoyé la liste d'utilisateur");
    return list;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.dal.UserDao#listerEtudiants(be.ipl.pae.biz.dto.UserDto)
   */
  @Override
  public ArrayList<UserDto> listerEtudiants() {
    ArrayList<UserDto> list = new ArrayList<UserDto>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dal.getPreparedStatement(
          "SELECT pseudo, nom, prenom, email FROM SMMDB.utilisateurs where role = 'E'");
      rs = ps.executeQuery();
      while (rs.next()) {
        UserDto dto = bf.getUserDto();
        dto.setPseudo(rs.getString(1));
        dto.setNom(rs.getString(2));
        dto.setPrenom(rs.getString(3));
        dto.setEmail(rs.getString(4));
        dto.setRole(Role.E);
        list.add(dto);
      }
    } catch (SQLException exception) {
      LOGGER.severe("La méthode listerEtudiants à echouée avec "
          + "la query : << SELECT pseudo, nom, prenom, email "
          + "FROM SMMDB.utilisateurs where role = 'E'>>.");
      throw new DalException("Problème avec listerUser");
    } finally {
      closeQuitlyRs(rs);
      closeQuitlyPs(ps);
    }
    LOGGER.info("La méthode listerEtudiants à renvoyé la liste d'utilisateur");
    return list;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.dal.UserDao#getNbrMobilitesParEtudiant(be.ipl.pae.biz.dto.UserDto)
   */
  @Override
  public int getNbrMobilitesParEtudiant(String pseudo) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dal.getPreparedStatement(
          "SELECT count(m.id_mobilite) FROM smmdb.utilisateurs u, smmdb.mobilites m "
              + "WHERE m.etudiant = u.id_utilisateur and u.pseudo = ?");
      ps.setString(1, pseudo);
      rs = ps.executeQuery();
      if (rs.next()) {
        return rs.getInt(1);
      }
    } catch (SQLException exception) {
      LOGGER.severe("La méthode getNbrMobilitesParEtudiant à echouée avec la query : "
          + "<<SELECT count(m.id_mobilite) " + "FROM smmdb.utilisateurs u, smmdb.mobilites m "
          + "WHERE m.etudiant = u.id_utilisateur and u.pseudo = " + pseudo + " >>.");
      throw new DalException("Problème avec getNbrMobilitesParEtudiant");
    } finally {
      closeQuitlyRs(rs);
      closeQuitlyPs(ps);
    }
    LOGGER.info("La méthode getNbrMobilitesParEtudiant à renvoyé le "
        + "nombre de mobilités auxquels l'utilisateur est rattaché");
    return -1;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.dal.UserDao#getNbrMobilitesAnnuleesParEtudiant(be.ipl.pae.biz.dto.UserDto)
   */
  @Override
  public int getNbrMobilitesAnnuleesParEtudiant(String pseudo) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dal.getPreparedStatement(
          "SELECT count(m.id_mobilite) FROM smmdb.utilisateurs u, smmdb.mobilites m "
              + "WHERE m.etudiant = u.id_utilisateur and m.mobilite_annule = true and "
              + "u.pseudo = ?");
      ps.setString(1, pseudo);
      rs = ps.executeQuery();
      if (rs.next()) {
        return rs.getInt(1);
      }
    } catch (SQLException exception) {
      LOGGER.severe("La méthode getNbrMobilitesAnnuleesParEtudiant à echouée avec "
          + "la query : <<SELECT count(m.id_mobilite) "
          + "FROM smmdb.utilisateurs u, smmdb.mobilites m "
          + "WHERE m.etudiant = u.id_utilisateur and m.mobilite_annule = true and u.pseudo = "
          + pseudo + " >>.");
      throw new DalException("Problème avec getNbrMobilitesAnnuleesParEtudiant");
    } finally {
      closeQuitlyRs(rs);
      closeQuitlyPs(ps);
    }
    LOGGER.info("La méthode getNbrMobilitesAnnuleesParEtudiant à renvoyé le "
        + "nombre de mobilités annulées auxquels l'utilisateur est rattaché.");
    return -1;
  }


  @Override
  public void promouvoirUser(UserDto userDto) throws VersionException {
    PreparedStatement ps = null;
    try {
      ps = dal.getPreparedStatement("update SMMDB.utilisateurs "
          + "set role = 'P',version = version+1 where pseudo = ? AND version = ?");
      ps.setString(1, userDto.getPseudo());
      ps.setInt(2, userDto.getVersion());
      ps.executeUpdate();
    } catch (SQLException sqle) {
      LOGGER.severe("La méthode promouvoirUser à échouée (Avec la query suivante) "
          + "update SMMDB.utilisateurs set role = 'P', version = version+1 where pseudo = "
          + userDto.getPseudo() + " AND version = " + userDto.getVersion());
      if (getVersionUser(userDto) != userDto.getVersion()) {
        throw new VersionException("La version a changée");
      } else {
        throw new DalException("Problème avec promouvoirUser");
      }
    } finally {
      closeQuitlyPs(ps);
    }
    LOGGER.info("L'utilisateur " + userDto.getPseudo() + " à été promu.");
  }

  private int getVersionUser(UserDto userDto) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dal.getPreparedStatement("select version from SMMDB.utilisateurs where pseudo = ?");
      ps.setString(1, userDto.getPseudo());
      rs = ps.executeQuery();
      if (rs.next()) {
        return rs.getInt(1);
      }
    } catch (SQLException sqle) {
      LOGGER.severe(
          "Problème query private getVersionUser avec l'utilisateur " + userDto.getPseudo());
      throw new DalException("Problème avec getVersionUser");
    } finally {
      closeQuitlyRs(rs);
      closeQuitlyPs(ps);
    }
    return -1;
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
