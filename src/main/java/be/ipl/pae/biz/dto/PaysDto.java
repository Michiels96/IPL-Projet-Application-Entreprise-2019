package be.ipl.pae.biz.dto;

import be.ipl.pae.enums.TypeProgramme;


public interface PaysDto {

  int getIdPays();

  void setIdPays(int idPays);

  String getNom();

  void setNom(String nom);

  String getCodePays();

  void setCodePays(String codePays);

  TypeProgramme getProgramme();

  void setProgramme(TypeProgramme programme);

}
