package be.ipl.pae.biz.ucc;

import be.ipl.pae.annotations.Inject;
import be.ipl.pae.biz.dto.MobiliteDto;
import be.ipl.pae.dal.DalServices;
import be.ipl.pae.dal.MobiliteDao;
import be.ipl.pae.exceptions.DalException;
import be.ipl.pae.exceptions.VersionException;
import be.ipl.pae.utils.Logging;

import java.util.ArrayList;
import java.util.logging.Logger;

class MobiliteUccImpl implements MobiliteUcc {

  private static final Logger LOGGER = Logging.getLogger(MobiliteUccImpl.class.getName());

  @Inject
  private MobiliteDao dao;
  @Inject
  private DalServices dal;

  @Override
  public ArrayList<MobiliteDto> getMobiliteByPseudo(String pseudo) {
    try {
      dal.startTransaction();
      ArrayList<MobiliteDto> mobilites;
      mobilites = dao.getMobiliteParPseudo(pseudo);
      dal.commitTransaction();
      return mobilites;
    } catch (DalException exc) {
      dal.rollBackTransaction();
      LOGGER.severe("La méthode getMobiliteByPseudo a echouée : " + exc.getMessage());
      return null;
    }

  }

  @Override
  public ArrayList<MobiliteDto> getAllMobilite() {
    try {
      dal.startTransaction();
      ArrayList<MobiliteDto> mobilites;
      mobilites = dao.getAllMobilite();
      dal.commitTransaction();
      return mobilites;
    } catch (DalException exc) {
      dal.rollBackTransaction();
      LOGGER.severe("La méthode getAllMobilite a echouée : " + exc.getMessage());
      return null;
    }

  }

  @Override
  public void validerMobilite(MobiliteDto mob) {
    // TODO Auto-generated method stub
    dal.startTransaction();
    try {
      dao.validerMobilite(mob);
    } catch (DalException exc) {
      dal.rollBackTransaction();
      LOGGER.severe("La méthode updateDocumentRetour a éhoué : " + exc.getMessage());
    }
    dal.commitTransaction();
  }

  @Override
  public MobiliteDto getMobiliteParId(int id) {
    MobiliteDto dto;
    dal.startTransaction();
    try {
      dto = dao.getMobiliteParId(id);
      dal.commitTransaction();
      return dto;
    } catch (DalException exc) {
      dal.rollBackTransaction();
      LOGGER.severe("La méthode updateDocumentRetour a éhoué : " + exc.getMessage());
    }
    return null;
  }

  @Override
  public void annulerMobilite(int id) throws VersionException {
    MobiliteDto dto = getMobiliteParId(id);
    dal.startTransaction();
    try {
      dto.setMobiliteAnnule(true);
      dao.updateMobilite(dto);
      dal.commitTransaction();
    } catch (DalException exc) {
      dal.rollBackTransaction();
      LOGGER.severe("La méthode annulerMobilite a éhoué : " + exc.getMessage());
    }

  }

  @Override
  public void ajouterMessageAnnulation(int idMob, String message) {
    MobiliteDto dto = getMobiliteParId(idMob);
    dal.startTransaction();
    try {
      dao.insertMessageAnnulationMobilite(dto, message);
    } catch (DalException exc) {
      dal.rollBackTransaction();
      LOGGER.severe("La méthode ajouterMessageAnnulation a éhoué : " + exc.getMessage());
    }
    dal.commitTransaction();
  }

}
