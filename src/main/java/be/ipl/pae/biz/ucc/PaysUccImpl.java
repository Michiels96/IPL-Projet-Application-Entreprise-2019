package be.ipl.pae.biz.ucc;

import be.ipl.pae.annotations.Inject;
import be.ipl.pae.biz.dto.PaysDto;
import be.ipl.pae.dal.DalServices;
import be.ipl.pae.dal.PaysDao;
import be.ipl.pae.exceptions.DalException;
import be.ipl.pae.utils.Logging;

import java.util.ArrayList;
import java.util.logging.Logger;

class PaysUccImpl implements PaysUcc {

  private static final Logger LOGGER = Logging.getLogger(PaysUccImpl.class.getName());

  @Inject
  private PaysDao pays;
  @Inject
  private DalServices dal;


  @Override
  public PaysDto getPaysById(int paysId) {
    try {
      PaysDto pa;
      dal.startTransaction();
      pa = pays.getPaysById(paysId);
      dal.commitTransaction();
      return pa;
    } catch (DalException exc) {
      dal.rollBackTransaction();
      LOGGER.severe("La méthode getPaysById a echouée : " + exc.getMessage());
      return null;
    }
  }


  @Override
  public ArrayList<PaysDto> getAllPays() {
    dal.startTransaction();
    try {
      ArrayList<PaysDto> listePays;
      listePays = pays.getAllPays();
      dal.commitTransaction();
      return listePays;
    } catch (DalException exc) {
      dal.rollBackTransaction();
      LOGGER.severe("La méthode getPaysById a echouée : " + exc.getMessage());
      return null;
    }
  }

}
