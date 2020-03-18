package be.ipl.pae.biz.interfce;

import be.ipl.pae.biz.dto.UserDto;

public interface User extends UserDto {
  /* Méthodes business */
  boolean checkPassword(String password);

  void cryptPassword();
}
