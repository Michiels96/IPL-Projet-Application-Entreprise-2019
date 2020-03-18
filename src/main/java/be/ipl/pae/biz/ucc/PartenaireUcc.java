package be.ipl.pae.biz.ucc;

import be.ipl.pae.biz.dto.PartenaireDto;

import java.util.ArrayList;

public interface PartenaireUcc {

  /**
   * Permet de récupérer les partenaires.
   * 
   * @return une liste de partenaires dto
   */
  ArrayList<PartenaireDto> getAllPartenaire();

  /**
   * Permet de récupérer un partenaire sur base de son id.
   * 
   * @param idPartenaire - id du partenaire
   * @return un partenaire dto
   */
  PartenaireDto getPartenaireById(int idPartenaire);

  /**
   * Ajoute un partenaire dans la base de données.
   * 
   * @param partenaireAdd - partenaire à ajouter
   */
  void addPartenaire(PartenaireDto partenaireAdd);
}
