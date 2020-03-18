package be.ipl.pae.dal;

import be.ipl.pae.biz.dto.PartenaireDto;

import java.util.ArrayList;

public interface PartenaireDao {

  /**
   * Récupère tous les partenaires.
   * 
   * @return une liste de partenaire dto
   */
  ArrayList<PartenaireDto> getAllPartenaire();

  /**
   * Insère un partenaire dans la base de données.
   * 
   * @param partenaire - partenaire dto
   */
  void insererPartenaire(PartenaireDto partenaire);

  /**
   * Récupère un partenaire sur base de son id.
   * 
   * @param idPartenaire - numéro d'id du partenaire
   * @return un partenaire dto
   */
  PartenaireDto getPartenaireById(int idPartenaire);
}
