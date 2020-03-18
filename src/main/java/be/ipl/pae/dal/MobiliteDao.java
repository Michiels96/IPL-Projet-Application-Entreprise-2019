package be.ipl.pae.dal;

import be.ipl.pae.biz.dto.MobiliteDto;
import be.ipl.pae.exceptions.VersionException;

import java.util.ArrayList;
import java.util.Map;

public interface MobiliteDao {

  /**
   * Récupère les mobilités d'un utilisateur sur base de son pseudo.
   * 
   * @param pseudo - pseudo de l'utilisateur
   * @return les mobilités de l'utilisateur
   */
  ArrayList<MobiliteDto> getMobiliteParPseudo(String pseudo);

  /**
   * Récupère une mobilité par son id.
   * 
   * @param id - id de la mobilité
   * @return une mobilité dto
   */
  MobiliteDto getMobiliteParId(int id);

  /**
   * Permet de récupérer le nombre de mobilités d'un user.
   * 
   * @param pseudo - pseudo de l'utilisateur
   * @return le nombre de mobilité de l'utilisateur
   */
  int getNbMobiliteParPseudo(String pseudo);

  /**
   * Permet d'insérer une nouvelle mobilité en base de donnée.
   * 
   * @param mobilite - l'instance de la mobilité à insérer
   */
  void insertMobilite(MobiliteDto mobilite);

  /**
   * Permet de mettre à jour une mobilité en base de donnée.
   * 
   * @param mobilite - l'instance de la mobilité à mettre à jour.
   * @throws VersionException - en cas d'erreur de version
   */
  void updateMobilite(MobiliteDto mobilite) throws VersionException;

  /**
   * Permet de supprimer une mobilité de la base de donnée.
   * 
   * @param mobilite - l'instance de la mobilité à supprimer
   */
  void removeMobilite(MobiliteDto mobilite);

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
   * Insere un message d'annulation.
   * 
   * @param mob - dto de la mobilité
   * @param message - raison de l'annulation
   */
  void insertMessageAnnulationMobilite(MobiliteDto mob, String message);

  /**
   * Récupère la notification.
   * 
   * @param id - id de la notification
   * @return une map de idMob annulée/message
   */
  Map<Integer, String> getNotificationsParId(int id);

}
