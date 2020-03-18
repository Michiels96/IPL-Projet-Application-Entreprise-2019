package be.ipl.pae.domaine;

import be.ipl.pae.annotations.Inject;
import be.ipl.pae.biz.dto.MobiliteDto;
import be.ipl.pae.biz.factory.BizFactory;
import be.ipl.pae.dal.MobiliteDao;
import be.ipl.pae.enums.Periode;
import be.ipl.pae.enums.TypeProgramme;
import be.ipl.pae.exceptions.DalException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MobiliteDaoMock implements MobiliteDao {

  @Inject
  private BizFactory bf;

  private int excAllMobilite = 0;

  @Override
  public ArrayList<MobiliteDto> getMobiliteParPseudo(String pseudo) {
    if (pseudo.equals("ko")) {
      return null;
    }
    if (pseudo.equals("exc")) {
      throw new DalException();
    }
    MobiliteDto mobDto = bf.getMobiliteDto();
    mobDto.setFinancement(42);
    mobDto.setIdMobilite(1);
    ArrayList<MobiliteDto> liste = new ArrayList<MobiliteDto>();
    liste.add(mobDto);
    return liste;
  }

  @Override
  public void insertMobilite(MobiliteDto mobilite) {
    if (mobilite.getFinancement() == 0) {
      throw new DalException();
    }
  }

  @Override
  public void updateMobilite(MobiliteDto mobilite) {
    if (mobilite.getFinancement() == 0) {
      throw new DalException();
    }

  }

  @Override
  public void removeMobilite(MobiliteDto mobilite) {
    if (mobilite.getFinancement() == 0) {
      throw new DalException();
    }

  }

  @Override
  public int getNbMobiliteParPseudo(String pseudo) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public MobiliteDto getMobiliteParId(int id) {
    MobiliteDto dto;
    if (id == -1) {
      return null;
    }
    if (id == 42) {
      throw new DalException();
    }
    if (id == 0) {
      dto = bf.getMobiliteDto();
      dto.setFinancement(0);
      return dto;
    }
    dto = bf.getMobiliteDto();
    dto.setFinancement(66);
    dto.setProgramme(TypeProgramme.ERABEL);
    dto.setPeriode(Periode.Q1);
    return dto;
  }

  @Override
  public ArrayList<MobiliteDto> getAllMobilite() {
    if (excAllMobilite == 1) {
      excAllMobilite++;
      return null;
    }
    if (excAllMobilite == 2) {
      throw new DalException();
    }
    MobiliteDto mobDto = bf.getMobiliteDto();
    mobDto.setFinancement(42);
    ArrayList<MobiliteDto> liste = new ArrayList<MobiliteDto>();
    liste.add(mobDto);
    excAllMobilite++;
    return liste;
  }

  @Override
  public void validerMobilite(MobiliteDto mob) {
    if (mob.getFinancement() == 42) {
      throw new DalException();
    }

  }

  @Override
  public void insertMessageAnnulationMobilite(MobiliteDto mob, String message) {
    if (message.equals("exc")) {
      throw new DalException();
    }
  }

  @Override
  public Map<Integer, String> getNotificationsParId(int id) {
    if (id == 42) {
      throw new DalException();
    }
    Map<Integer, String> map = new HashMap<Integer, String>();
    if (id == 12) {
      return map;
    }
    map.put(2, "");
    map.put(3, "");
    if (id == 66) {
      map.put(42, "");
    }
    return map;
  }

}
