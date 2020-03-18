package be.ipl.pae.biz.ucc;

import be.ipl.pae.biz.dto.MobiliteDto;
import be.ipl.pae.exceptions.VersionException;

import java.util.ArrayList;

public interface MobiliteUcc {

  /**
   * Permet de récupérer la liste des mobilités pour un utilisateur donné.
   * 
   * @param pseudo - le pseudo de l'utilisateur
   * @return la liste des mobilités
   */
  ArrayList<MobiliteDto> getMobiliteByPseudo(String pseudo);

  /**
   * Récupère toutes les mobilités.
   * 
   * @return une liste de mobilité dto
   */
  ArrayList<MobiliteDto> getAllMobilite();

  /**
   * Confirme une mobilité.
   * 
   * @param mob - mobilité à valider
   */
  void validerMobilite(MobiliteDto mob);

  /**
   * Récupère une mobilité par son id.
   * 
   * @param id - id de la mobilité
   * @return une mobilité dto
   */
  MobiliteDto getMobiliteParId(int id);

  /**
   * Annule une mobilité.
   * 
   * @param id - id de la mobilité
   * @throws VersionException - en cas d'erreur de version
   */
  void annulerMobilite(int id) throws VersionException;

  /**
   * Permet d'ajouter un message pour expliquer la raison de l'annulation.
   * 
   * @param idMob - id de la mobilité
   * @param message - raison de l'annulation
   */
  void ajouterMessageAnnulation(int idMob, String message);
}
