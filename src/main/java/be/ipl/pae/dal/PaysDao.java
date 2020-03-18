package be.ipl.pae.dal;

import be.ipl.pae.biz.dto.PaysDto;

import java.util.ArrayList;

public interface PaysDao {

  /**
   * Méthode qui recherche le pays dans la base de donnée par rapport à son id.
   * 
   * @param paysId - l'id du pays
   * @return un dto du pays
   */
  PaysDto getPaysById(int paysId);

  /**
   * Permet de récupérer la liste des pays en BD.
   * 
   * @return liste des pays
   */
  ArrayList<PaysDto> getAllPays();

}
