package be.ipl.pae.biz.ucc;

import be.ipl.pae.annotations.Inject;
import be.ipl.pae.biz.dto.PartenaireDto;
import be.ipl.pae.dal.DalServices;
import be.ipl.pae.dal.PartenaireDao;
import be.ipl.pae.exceptions.DalException;
import be.ipl.pae.utils.Logging;

import java.util.ArrayList;
import java.util.logging.Logger;

class PartenaireUccImpl implements PartenaireUcc {

  private static final Logger LOGGER = Logging.getLogger(PartenaireUccImpl.class.getName());

  @Inject
  private PartenaireDao partenaire;
  @Inject
  private DalServices dal;

  public ArrayList<PartenaireDto> getAllPartenaire() {
    dal.startTransaction();
    try {
      ArrayList<PartenaireDto> partenaires;
      partenaires = partenaire.getAllPartenaire();
      dal.commitTransaction();
      return partenaires;
    } catch (DalException exc) {
      dal.rollBackTransaction();
      LOGGER.severe("La méthode getAllPartenaire a echouée : " + exc.getMessage());
      return null;
    }
  }

  @Override
  public PartenaireDto getPartenaireById(int idPartenaire) {
    dal.startTransaction();
    try {
      PartenaireDto part;
      part = partenaire.getPartenaireById(idPartenaire);
      dal.commitTransaction();
      return part;
    } catch (DalException exc) {
      dal.rollBackTransaction();
      LOGGER.severe("La méthode getPartenaireById a echouée : " + exc.getMessage());
      return null;
    }
  }

  @Override
  public void addPartenaire(PartenaireDto partenaireAdd) {
    try {
      dal.startTransaction();
      partenaire.insererPartenaire(partenaireAdd);
      dal.commitTransaction();
    } catch (DalException exc) {
      dal.rollBackTransaction();
      LOGGER.severe("La méthode addPartenaire a echouée : " + exc.getMessage());
    }
  }
}
