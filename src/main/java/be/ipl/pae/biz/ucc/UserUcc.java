package be.ipl.pae.biz.ucc;

import be.ipl.pae.biz.dto.MobiliteDto;
import be.ipl.pae.biz.dto.UserDto;
import be.ipl.pae.exceptions.VersionException;

import java.util.ArrayList;


public interface UserUcc {


  /**
   * regarde si il y a déjà un user en bd.
   */
  void verifAdminPresent();

  /**
   * Cette méthode va gérer l'ajout, la suppression ou la modification de mobilité pour
   * l'utilisateur.
   * 
   * @param newMob - Liste des mobilités venant du front-end
   * @param pseudo - le pseudo de l'utilisateur
   * @return true si toutes les modifications ont réussies, false sinon.
   * @throws VersionException - en csa d'erreur de version
   */
  boolean gestionMobilite(ArrayList<MobiliteDto> newMob, String pseudo) throws VersionException;

  /**
   * Connecte un utilisateur sur base de son pseudo et de son mot de passe si celui ci existe.
   * 
   * @param pseudo - String correspondant au pseudo du user
   * @param password - String correspondant au mot de passe du user
   * @return true si la connexion a réussie, false sinon
   */
  UserDto seConnecter(String pseudo, String password);

  /**
   * Ajoute un utilisateur s'il n'existe pas déjà.
   * 
   * @param userDto - DTO permettant d'envoyer des données utilisateur dans la couche business
   * @return true si l'inscription s'est faites, false sinon
   */
  boolean inscrireUtilisateur(UserDto userDto);

  /**
   * Ajout des infos supp de l'utilisateur.
   * 
   * @param userDto - utilisateur qui effectue l'action
   * @param nouveauPseudo - le pseudo actuel ou bien nouveau de l'utilisateur
   * @param nouveauMdp - nouveau mot de passe de l'utilisateur
   * @return true si ajout ok, false sinon.
   */
  boolean ajouterInfosUtilisateur(UserDto userDto, String nouveauPseudo, String nouveauMdp);

  /**
   * Effectue une recherche dans la base de données sur base d'un pattern dans la recherche.
   * 
   * @param userDto - DTO permettant d'envoyer des données utilisateurs
   * @param pattern - String permettant de matcher avec un nom / prenom / mail / pseudo d'un user
   * @return ArrayList - list de tous les users qui on matché avec le pattern.
   */
  ArrayList<UserDto> rechercherUser(UserDto userDto, String pattern);

  /**
   * Permet de lister tous les utilisateurs présent en base de données.
   * 
   * @param userDto - utilisateur qui effectue l'action.
   * @return Liste des utilisateurs présent en base de données.
   */
  ArrayList<UserDto> listerUser(UserDto userDto);

  /**
   * Permet de lister tous les utilisateurs étudiants présent en base de données.
   * 
   * @param userDto - utilisateur qui effectue l'action.
   * @return Liste des utilisateurs étudiants présent en base de données.
   */
  ArrayList<UserDto> listerEtudiants(UserDto userDto);

  /**
   * Permet de récuperer le nombre de mobilités auxquels l'étudiant est rattaché
   * 
   * @param userDto - utilisateur qui effectue l'action.
   * @return le nombre de mobilités auxquels l'étudiant est rattaché.
   */
  int getNbrMobilitesParEtudiant(UserDto userDto);

  /**
   * Permet de récuperer le nombre de mobilités annulées auxquels l'étudiant est rattaché
   * 
   * @param userDto - utilisateur qui effectue l'action.
   * @return le nombre de mobilités annulées auxquels l'étudiant est rattaché.
   */
  int getNbrMobilitesAnnuleesParEtudiant(UserDto userDto);

  /**
   * Supprime l'utilisateur passé en paramètre.
   * 
   * @param userDto - utilisateur à supprimer.
   */
  void supprimerUtilisateur(UserDto userDto);

  /**
   * Renvoie les informations appartenant à l'utilisateur dont le pseudo est passé en paramètre.
   * 
   * @param pseudo - pseudo de l'utilisateur à chercher.
   * @return les informations de cet utilisateur.
   */
  UserDto getInfoUser(String pseudo);

  /**
   * Renvoie une liste de mobilités pour un utilisateur.
   * 
   * @param pseudo - le pseudo de l'utilisateur
   * @return la liste des mobilités
   */
  ArrayList<MobiliteDto> getMobiliteByUser(String pseudo);

  /**
   * Fait passer un utilisateur du role d'étudiant vers celui de professeur.
   * 
   * @param pseudo - le pseudo de l'utilisateur
   */
  void promouvoirEtudiant(UserDto self, String pseudo);

  /**
   * Permet de récupérer un utilisateur sur base de son id.
   * 
   * @param id - id de l'utilisateur
   * @return un utilisateur dto
   */
  UserDto getPseudoUser(int id);

  /**
   * Fournit les notiifcations de l'utilisateur.
   * 
   * @param id - id de l'utilisateur
   * @return une string contenant les notifications
   */
  String getNotificationByUser(int id);

}
