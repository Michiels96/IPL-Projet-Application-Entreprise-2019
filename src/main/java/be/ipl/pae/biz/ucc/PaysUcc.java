package be.ipl.pae.biz.ucc;

import be.ipl.pae.biz.dto.PaysDto;

import java.util.ArrayList;

public interface PaysUcc {

  /**
   * Effectue la recherche d'un pays par rapport à son id.
   * 
   * @param paysId - l'id du pays
   * @return un Dto du pays
   */
  PaysDto getPaysById(int paysId);

  /**
   * Récupère la liste des pays en BD.
   * 
   * @return liste des pays
   */
  ArrayList<PaysDto> getAllPays();
}
