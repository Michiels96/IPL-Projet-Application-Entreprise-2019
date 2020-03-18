package be.ipl.pae.dal;

import be.ipl.pae.annotations.Inject;
import be.ipl.pae.biz.dto.DocumentDepartDto;
import be.ipl.pae.biz.dto.DocumentRetourDto;
import be.ipl.pae.biz.dto.MobiliteDto;
import be.ipl.pae.biz.factory.BizFactory;
import be.ipl.pae.exceptions.DalException;
import be.ipl.pae.utils.Logging;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DocumentsDaoImpl implements DocumentsDao {

  private static final Logger LOGGER = Logging.getLogger(DocumentsDaoImpl.class.getName());

  @Inject
  private BizFactory bf;
  @Inject
  private DalBackendServices dal;

  @Override
  public DocumentDepartDto findDocumentsDepartByMobiliteId(MobiliteDto mob) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    DocumentDepartDto dto;
    try {
      dto = bf.getDocumentDepartDto();
      ps = dal.getPreparedStatement(
          "SELECT * FROM SMMDB.documents_depart WHERE id_documents_depart = ?");
      ps.setInt(1, mob.getDocumentsDepart());
      rs = ps.executeQuery();
      while (rs.next()) {
        dto.setIdDocumentsDepart(rs.getInt(1));
        dto.setPreuveTestLinguistique(rs.getBoolean(2));
        dto.setContratBourse(rs.getBoolean(3));
        dto.setConventionStageEtude(rs.getBoolean(4));
        dto.setCharteEtudiant(rs.getBoolean(5));
        dto.setDocumentEngagement(rs.getBoolean(6));
        dto.setVersion(rs.getInt(7));
      }
    } catch (SQLException sqle) {
      LOGGER.severe("La méthode XXXXX a échoué avec la query : << XXXXX >>.");
      throw new DalException("Problème avec XXXXX");
    } finally {
      closeQuitlyRs(rs);
      closeQuitlyPs(ps);
    }
    LOGGER.info("La méthode XXXXXX a renvoyé XXXX");
    return dto;
  }

  @Override
  public DocumentRetourDto findDocumentsRetourByMobiliteId(MobiliteDto mob) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    DocumentRetourDto dto;
    try {
      dto = bf.getDocumentRetourDto();
      ps = dal.getPreparedStatement(
          "SELECT * FROM SMMDB.documents_retour WHERE id_documents_retour = ?");
      ps.setInt(1, mob.getDocumentsDepart());
      rs = ps.executeQuery();
      while (rs.next()) {
        dto.setIdDocumentsRetour(rs.getInt(1));
        dto.setAttestationSejour(rs.getBoolean(2));
        dto.setStageReleveNote(rs.getBoolean(3));
        dto.setRapportFinal(rs.getBoolean(4));
        dto.setPreuvePassageTest(rs.getBoolean(5));
        dto.setVersion(rs.getInt(6));
      }
    } catch (SQLException sqle) {
      LOGGER.severe("La méthode XXXXX a échoué avec la query : << XXXXX >>.");
      throw new DalException("Problème avec XXXXX");
    } finally {
      closeQuitlyRs(rs);
      closeQuitlyPs(ps);
    }
    LOGGER.info("La méthode XXXXXX a renvoyé XXXX");
    return dto;
  }

  @Override
  public void insertDocumentRetour() {
    PreparedStatement ps = null;
    try {
      ps = dal.getPreparedStatement("INSERT INTO SMMDB.documents_retour VALUES"
          + " (DEFAULT, false, false, false, false, false, false, 0)");
      // int id =
      ps.executeUpdate();
    } catch (SQLException sqlE) {
      LOGGER.severe("La méthode insertDocumentRetour a échouée");
      throw new DalException("Problème avec insert document retour");
    } finally {
      closeQuitlyPs(ps);
    }
  }

  @Override
  public void insertDocumentDepart() {
    PreparedStatement ps = null;
    try {
      ps = dal.getPreparedStatement("INSERT INTO SMMDB.documents_depart VALUES "
          + " (DEFAULT, false, false, false, false, false, false, false,    0)");
      // int id =
      ps.executeUpdate();
    } catch (SQLException sqlE) {
      LOGGER.severe("La méthode insertDocumentDepart a échouée");
      throw new DalException("Problème avec insert document retour");
    } finally {
      closeQuitlyPs(ps);
    }
  }

  @Override
  public int getLastInsertIdDocumentDepart() {
    int id = 0;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dal.getPreparedStatement("SELECT MAX(id_documents_depart) FROM SMMDB.documents_depart");
      rs = ps.executeQuery();
      while (rs.next()) {
        id = rs.getInt(1);
      }
    } catch (SQLException sqlE) {
      LOGGER.severe("La méthode getLastInsertIdDocumentDepart a échouée");
      throw new DalException(
          "Problème lors de la récupération de l'id du dernier document de depart");
    } finally {
      closeQuitlyRs(rs);
      closeQuitlyPs(ps);
    }
    return id;
  }

  @Override
  public int getLastInsertIdDocumentRetour() {
    int id = 0;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dal.getPreparedStatement("SELECT MAX(id_documents_retour) FROM SMMDB.documents_retour");
      rs = ps.executeQuery();
      while (rs.next()) {
        id = rs.getInt(1);
      }
    } catch (SQLException sqlE) {
      LOGGER.severe("La méthode getLastInsertIdDocumentRetour a échouée");
      throw new DalException(
          "Problème lors de la récupération de l'id du dernier document de retour");
    } finally {
      closeQuitlyRs(rs);
      closeQuitlyPs(ps);
    }
    return id;
  }

  @Override
  public DocumentDepartDto getDocumentDepartById(int idDoc) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    DocumentDepartDto docRet = bf.getDocumentDepartDto();
    try {
      ps = dal.getPreparedStatement(
          "SELECT id_documents_depart, preuve_test_linguistique, contrat_bourse, "
              + "convention_stage_etude, charte_etudiant, document_engagement, "
              + "envoie_document, paiment_effectue, version "
              + "FROM SMMDB.documents_depart where id_documents_depart = ?");
      ps.setInt(1, idDoc);
      rs = ps.executeQuery();
      while (rs.next()) {
        docRet.setIdDocumentsDepart(rs.getInt(1));
        docRet.setPreuveTestLinguistique(rs.getBoolean(2));
        docRet.setContratBourse(rs.getBoolean(3));
        docRet.setConventionStageEtude(rs.getBoolean(4));
        docRet.setCharteEtudiant(rs.getBoolean(5));
        docRet.setDocumentEngagement(rs.getBoolean(6));
        docRet.setDocumentEnvoieDocument(rs.getBoolean(7));
        docRet.setPaiementEffectue(rs.getBoolean(8));
        docRet.setVersion(rs.getInt(9));
      }
    } catch (SQLException sqlE) {
      LOGGER.severe("La méthode getDocumentDepartById a échoué : " + ps.toString());
      throw new DalException("Problème getDocumentDepartById");
    } finally {
      closeQuitlyRs(rs);
      closeQuitlyPs(ps);
    }
    return docRet;
  }

  @Override
  public DocumentRetourDto getDocumentRetourById(int idDoc) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    DocumentRetourDto docRet = bf.getDocumentRetourDto();
    try {
      ps = dal.getPreparedStatement(
          "SELECT id_documents_retour, attestation_sejour, stage_releve_note, "
              + "rapport_final, preuve_passage_test, envoie_document, paiement_effectue, version "
              + "FROM SMMDB.documents_retour where id_documents_retour = ?");
      ps.setInt(1, idDoc);
      rs = ps.executeQuery();
      while (rs.next()) {
        docRet.setIdDocumentsRetour(rs.getInt(1));
        docRet.setAttestationSejour(rs.getBoolean(2));
        docRet.setStageReleveNote(rs.getBoolean(3));
        docRet.setRapportFinal(rs.getBoolean(4));
        docRet.setPreuvePassageTest(rs.getBoolean(5));
        docRet.setDocumentEnvoieDocument(rs.getBoolean(6));
        docRet.setPaiementEffectue(rs.getBoolean(7));
        docRet.setVersion(rs.getInt(8));
      }
    } catch (SQLException sqlE) {
      LOGGER.severe("La méthode getDocumentRetourById a échoué : " + ps.toString());
      throw new DalException("Problème getDocumentRetourById");
    } finally {
      closeQuitlyRs(rs);
      closeQuitlyPs(ps);
    }
    return docRet;
  }

  @Override
  public void updateDocumentDepart(DocumentDepartDto doc) {
    PreparedStatement ps = null;
    try {
      ps = dal.getPreparedStatement(
          "UPDATE SMMDB.documents_depart SET preuve_test_linguistique = ?, contrat_bourse = ?, "
              + "convention_stage_etude = ?, charte_etudiant = ?, document_engagement = ?, "
              + "envoie_document = ?, paiment_effectue = ?, version = version+1 "
              + "WHERE id_documents_depart = ? AND version = ?");
      ps.setBoolean(1, doc.isPreuveTestLinguistique());
      ps.setBoolean(2, doc.isContratBourse());
      ps.setBoolean(3, doc.isConventionStageEtude());
      ps.setBoolean(4, doc.isCharteEtudiant());
      ps.setBoolean(5, doc.isDocumentEngagement());
      ps.setBoolean(6, doc.isEvnoieDocument());
      ps.setBoolean(7, doc.isPaiementEffectue());
      ps.setInt(8, doc.getIdDocumentsDepart());
      ps.setInt(9, doc.getVersion());
      ps.executeUpdate();
    } catch (SQLException sqlE) {
      LOGGER.severe("La méthode updateDocumentDepart a échoué : " + ps.toString());
      throw new DalException("Problème updateDocumentDepart");
    } finally {
      closeQuitlyPs(ps);
    }
  }

  @Override
  public void updateDocumentRetour(DocumentRetourDto doc) {
    PreparedStatement ps = null;
    try {
      ps = dal.getPreparedStatement("UPDATE SMMDB.documents_retour SET attestation_sejour = ?, "
          + "stage_releve_note = ?, rapport_final = ?, "
          + "preuve_passage_test = ?, envoie_document = ?, "
          + "paiement_effectue = ? ,version = version+1 "
          + "WHERE id_documents_retour = ? AND version = ?");
      ps.setBoolean(1, doc.isAttestationSejour());
      ps.setBoolean(2, doc.isStageReleveNote());
      ps.setBoolean(3, doc.isRapportFinal());
      ps.setBoolean(4, doc.isPreuvePassageTest());
      ps.setBoolean(5, doc.isEvnoieDocument());
      ps.setBoolean(6, doc.isPaiementEffectue());
      ps.setInt(7, doc.getIdDocumentsRetour());
      ps.setInt(8, doc.getVersion());
      ps.executeUpdate();
    } catch (SQLException sqlE) {
      LOGGER.severe("La méthode updateDocumentRetour a échoué : " + ps.toString());
      throw new DalException("Problème updateDocumentRetour");
    } finally {
      closeQuitlyPs(ps);
    }
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
