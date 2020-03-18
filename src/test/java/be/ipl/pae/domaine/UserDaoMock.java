package be.ipl.pae.domaine;

import be.ipl.pae.annotations.Inject;
import be.ipl.pae.biz.dto.UserDto;
import be.ipl.pae.biz.factory.BizFactory;
import be.ipl.pae.dal.UserDao;
import be.ipl.pae.exceptions.DalException;

import java.util.ArrayList;

public class UserDaoMock implements UserDao {

  @Inject
  private BizFactory bf;

  private int promouvoirUserExc = 1;
  private int deleteUserExc = 1;

  public static boolean adminPresent = false;
  private static int nbrTest = 0;

  @Override
  public void isAdminPresent() {
    if (nbrTest == 0) {
      nbrTest++;
      throw new DalException();
    }
    if (nbrTest == 2) {
      adminPresent = true;
    }
    nbrTest++;
  }

  @Override
  public UserDto findUserByLogin(String ok) {
    UserDto user = bf.getUserDto();
    if ("ok".equals(ok)) {
      user.setPseudo("ok");
    } else {
      user.setPseudo(ok);
    }
    if (ok.equals("exc")) {
      throw new DalException();
    }
    user.setMotDePasse("$2a$10$/USAP02Ftd3ooXao2mz9XOuvtSlWQTkSAYPxUNBxFLbFboFF9ZJiy");
    return "ko".equals(ok) ? null : user;
  }

  @Override
  public void insertUser(UserDto userDto) {
    // TODO Auto-generated method stub
  }

  @Override
  public void addInfosUser(UserDto userDto, String nouveauPseudo) {
    // TODO Auto-generated method stub
  }

  @Override
  public ArrayList<UserDto> rechercherUser(String pattern) {
    ArrayList<UserDto> list = new ArrayList<UserDto>();
    if (pattern.equals("test")) {
      UserDto user = bf.getUserDto();
      user.setPseudo("test");
      list.add(user);
    } else {
      list = null;
    }
    return list;
  }

  @Override
  public void deleteUser(UserDto userDto) {
    if (deleteUserExc == 0) {
      throw new DalException();
    }
    deleteUserExc--;
  }

  @Override
  public ArrayList<UserDto> listerUser() {
    ArrayList<UserDto> list = new ArrayList<UserDto>();
    UserDto user = bf.getUserDto();
    user.setPseudo("test");
    list.add(user);
    return list;
  }

  @Override
  public ArrayList<UserDto> listerEtudiants() {
    ArrayList<UserDto> list = new ArrayList<UserDto>();
    UserDto user = bf.getUserDto();
    user.setPseudo("test");
    list.add(user);
    return list;
  }

  @Override
  public int getNbrMobilitesParEtudiant(String pseudo) {
    if (pseudo.equals("exc")) {
      throw new DalException();
    }
    return 42;
  }

  @Override
  public int getNbrMobilitesAnnuleesParEtudiant(String pseudo) {
    if (pseudo.equals("exc")) {
      throw new DalException();
    }
    return 42;
  }

  @Override
  public void promouvoirUser(UserDto userDto) {
    // TODO Auto-generated method stub
    if (promouvoirUserExc == 0) {
      throw new DalException();
    }
    promouvoirUserExc--;
  }

  @Override
  public UserDto findUserById(int id) {
    if (id == 42) {
      throw new DalException();
    }
    UserDto dto = bf.getUserDto();
    dto.setPseudo("ok");
    return dto;
  }
}
