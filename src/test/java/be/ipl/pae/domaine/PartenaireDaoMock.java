package be.ipl.pae.domaine;

import be.ipl.pae.annotations.Inject;
import be.ipl.pae.biz.dto.PartenaireDto;
import be.ipl.pae.biz.factory.BizFactory;
import be.ipl.pae.dal.PartenaireDao;
import be.ipl.pae.exceptions.DalException;

import java.util.ArrayList;

public class PartenaireDaoMock implements PartenaireDao {

  @Inject
  BizFactory bf;

  private int getAllPartenaireExc = 1;

  @Override
  public ArrayList<PartenaireDto> getAllPartenaire() {
    if (getAllPartenaireExc == 0) {
      throw new DalException();
    }
    getAllPartenaireExc--;
    return null;
  }

  @Override
  public PartenaireDto getPartenaireById(int idPartenaire) {
    if (idPartenaire == -1) {
      return null;
    }
    if (idPartenaire == 42) {
      throw new DalException();
    }
    PartenaireDto part = bf.getPartenaireDto();
    part.setNomComplet("ok");
    return part;
  }


  @Override
  public void insererPartenaire(PartenaireDto partenaire) {
    if (partenaire == null) {
      throw new DalException();
    }
  }

}
