package be.ipl.pae.domaine;

import be.ipl.pae.annotations.Inject;
import be.ipl.pae.biz.dto.PaysDto;
import be.ipl.pae.biz.factory.BizFactory;
import be.ipl.pae.dal.PaysDao;
import be.ipl.pae.exceptions.DalException;

import java.util.ArrayList;

public class PaysDaoMock implements PaysDao {

  @Inject
  private BizFactory bf;

  private int getAllPaysExc = 1;

  @Override
  public PaysDto getPaysById(int paysId) {
    if (paysId == -1) {
      return null;
    }
    if (paysId == 42) {
      throw new DalException();
    }
    PaysDto pays = bf.getPaysDto();
    pays.setNom("ok");
    return pays;
  }

  @Override
  public ArrayList<PaysDto> getAllPays() {
    if (getAllPaysExc == 0) {
      throw new DalException();
    }
    getAllPaysExc--;
    return null;
  }

}
