package be.ipl.pae.biz.ucc;

import be.ipl.pae.annotations.Inject;
import be.ipl.pae.biz.dto.DocumentDepartDto;
import be.ipl.pae.biz.dto.DocumentRetourDto;
import be.ipl.pae.biz.dto.MobiliteDto;
import be.ipl.pae.biz.factory.BizFactory;
import be.ipl.pae.dal.DalServices;
import be.ipl.pae.dal.DocumentsDao;
import be.ipl.pae.dal.MobiliteDao;
import be.ipl.pae.enums.EtatMobilite;
import be.ipl.pae.exceptions.DalException;
import be.ipl.pae.utils.Logging;

import java.util.logging.Logger;

public class DocumentsUccImpl implements DocumentsUcc {

  private static final Logger LOGGER = Logging.getLogger(DocumentsUccImpl.class.getName());

  @Inject
  private DocumentsDao docDao;

  @Inject
  private MobiliteDao mobDao;

  @Inject
  private DalServices dal;

  @Inject
  private BizFactory bf;

  @Override
  public DocumentDepartDto listerDocumentsDepart(int id) {
    dal.startTransaction();
    try {
      docDao.findDocumentsDepartByMobiliteId(mobDao.getMobiliteParId(id));
    } catch (DalException exc) {
      dal.rollBackTransaction();
      LOGGER.severe("La méthode listerDocumentsDepart a echouée : " + exc.getMessage());
    }
    dal.commitTransaction();
    return null;
  }

  @Override
  public DocumentRetourDto listerDocumentsRetour(int id) {
    dal.startTransaction();
    try {
      docDao.findDocumentsRetourByMobiliteId(mobDao.getMobiliteParId(id));
    } catch (DalException exc) {
      dal.rollBackTransaction();
      LOGGER.severe("La méthode listerDocumentsRetour a echouée : " + exc.getMessage());
    }
    dal.commitTransaction();
    return null;
  }

  @Override
  public DocumentDepartDto getDocumentDepart(int idDoc) {
    dal.startTransaction();
    DocumentDepartDto doc = null;
    try {
      doc = docDao.getDocumentDepartById(idDoc);
    } catch (DalException exc) {
      dal.rollBackTransaction();
      LOGGER.severe("La méthode getDocumentDepart a échouée : " + exc.getMessage());
    }
    dal.commitTransaction();
    return doc;
  }

  @Override
  public DocumentRetourDto getDocumentRetour(int idDoc) {
    dal.startTransaction();
    DocumentRetourDto doc = null;
    try {
      doc = docDao.getDocumentRetourById(idDoc);
    } catch (DalException exc) {
      dal.rollBackTransaction();
      LOGGER.severe("La méthode getDocumentRetour a échouée : " + exc.getMessage());
    }
    dal.commitTransaction();
    return doc;
  }

  @Override
  public void updateDocumentDepart(DocumentDepartDto doc, int idMobilite, int versionMob,
      String etatMobilite, int idDocRetour) {
    int nbrTrue = 0;
    if (doc.isCharteEtudiant()) {
      nbrTrue++;
    }
    if (doc.isContratBourse()) {
      nbrTrue++;
    }
    if (doc.isConventionStageEtude()) {
      nbrTrue++;
    }
    if (doc.isDocumentEngagement()) {
      nbrTrue++;
    }
    if (doc.isPreuveTestLinguistique()) {
      nbrTrue++;
    }
    dal.startTransaction();
    try {
      if ((etatMobilite.equals("CREE") || etatMobilite.equals("EN_PREPARATION")) && nbrTrue > 4) {
        MobiliteDto mob = bf.getMobiliteDto();
        mob.setIdMobilite(idMobilite);
        mob.setVersion(versionMob);
        mob.setDocumentsDepart(doc.getIdDocumentsDepart());
        mob.setDocumentsRetour(idDocRetour);
        mob.setEtatMobilite(EtatMobilite.A_PAYER);
        mobDao.validerMobilite(mob);
      } else if (etatMobilite.equals("CREE") && nbrTrue > 0) {
        MobiliteDto mob = bf.getMobiliteDto();
        mob.setIdMobilite(idMobilite);
        mob.setVersion(versionMob);
        mob.setDocumentsDepart(doc.getIdDocumentsDepart());
        mob.setDocumentsRetour(idDocRetour);
        mob.setEtatMobilite(EtatMobilite.EN_PREPARATION);
        mobDao.validerMobilite(mob);
      }
      docDao.updateDocumentDepart(doc);
    } catch (DalException exc) {
      dal.rollBackTransaction();
      LOGGER.severe("La méthode updateDocumentDepart a éhoué : " + exc.getMessage());
    }
    dal.commitTransaction();
  }

  @Override
  public void updateDocumentRetour(DocumentRetourDto doc, int idMobilite, int versionMob,
      String etatMobilite, int idDocDepart) {
    int nbrTrue = 0;
    if (doc.isAttestationSejour()) {
      nbrTrue++;
    }
    if (doc.isPreuvePassageTest()) {
      nbrTrue++;
    }
    if (doc.isRapportFinal()) {
      nbrTrue++;
    }
    if (doc.isStageReleveNote()) {
      nbrTrue++;
    }

    dal.startTransaction();
    try {
      if (etatMobilite.equals("A_PAYER") && nbrTrue > 3) {
        MobiliteDto mob = bf.getMobiliteDto();
        mob.setIdMobilite(idMobilite);
        mob.setVersion(versionMob);
        mob.setDocumentsDepart(idDocDepart);
        mob.setDocumentsRetour(doc.getIdDocumentsRetour());
        mob.setEtatMobilite(EtatMobilite.A_PAYER_SOLDE);
        mobDao.validerMobilite(mob);
      }
      docDao.updateDocumentRetour(doc);
    } catch (DalException exc) {
      dal.rollBackTransaction();
      LOGGER.severe("La méthode updateDocumentRetour a éhoué : " + exc.getMessage());
    }
    dal.commitTransaction();

  }

  @Override
  public void initDocuments() {
    // TODO Auto-generated method stub
    dal.startTransaction();
    try {
      docDao.insertDocumentDepart();
      docDao.insertDocumentRetour();
    } catch (DalException exc) {
      dal.rollBackTransaction();
      LOGGER.severe("La méthode updateDocumentRetour a éhoué : " + exc.getMessage());
    }
    dal.commitTransaction();
  }

  @Override
  public int getIdDernierDocDepart() {
    dal.startTransaction();
    try {
      int atReturn = docDao.getLastInsertIdDocumentDepart();
      dal.commitTransaction();
      return atReturn;
    } catch (DalException exc) {
      dal.rollBackTransaction();
      LOGGER.severe("La méthode updateDocumentRetour a éhoué : " + exc.getMessage());
    }
    dal.commitTransaction();
    return 0;
  }

  @Override
  public int getIdDernierDocretour() {
    dal.startTransaction();
    try {
      int atReturn = docDao.getLastInsertIdDocumentRetour();
      dal.commitTransaction();
      return atReturn;
    } catch (DalException exc) {
      dal.rollBackTransaction();
      LOGGER.severe("La méthode updateDocumentRetour a éhoué : " + exc.getMessage());
    }
    dal.commitTransaction();
    return 0;
  }

}
