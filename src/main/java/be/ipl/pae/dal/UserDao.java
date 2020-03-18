package be.ipl.pae.dal;

import be.ipl.pae.biz.dto.UserDto;
import be.ipl.pae.exceptions.VersionException;

import java.util.ArrayList;

public interface UserDao {

  /**
   * Au lancement du server, regarde si il y a déjà un user en bd. Si il n'y en a pas alors
   * adminPresent = false. Si il y en 1 ou plus alors adminPresent = true.
   * 
   */
  void isAdminPresent();

  /**
   * Permet de trouver un utilisateur en base de donnée à l'aide de son pseudo.
   * 
   * @param pseudo - String identifiant unique de l'user
   * @return dto - rempli si tout s'est bien passé, null sinon
   */
  UserDto findUserByLogin(String pseudo);

  /**
   * Permet d'insérer un nouvel user en base de donnée.
   * 
   * @param userDto - UserDto objet contenant la structure d'un user
   */
  void insertUser(UserDto userDto);

  /**
   * Ajout des informations sur l'utilisateur.
   * 
   * @param userDto - l'utilisateur qui aura un ajout d'information
   * @param nouveauPseudo - le pseudo actuel ou bien nouveau de l'utilisateur
   * @throws VersionException - en cas de souci de version
   */
  void addInfosUser(UserDto userDto, String nouveauPseudo) throws VersionException;

  /**
   * Recherche un utilisateur sur base d'un pattern.
   * 
   * @param pattern - String permettant de matcher avec un user en DB
   * @return une liste des users qui correspondaient au pattern.
   */
  ArrayList<UserDto> rechercherUser(String pattern);

  /**
   * Liste tous les utilisateurs présent en base de données.
   * 
   * @return liste des utilisateurs
   */
  ArrayList<UserDto> listerUser();

  /**
   * récupère le nombre de mobilités auxquels l'étudiant passé en paramètre est rattaché.
   * 
   * @param pseudo - String identifiant unique de l'user
   * @return int - nombre de mobilités auxquels l'étudiant est rattaché
   */
  int getNbrMobilitesParEtudiant(String pseudo);

  /**
   * récupère le nombre de mobilités annulées auxquels l'étudiant passé en paramètre est rattaché.
   * 
   * @param pseudo - String identifiant unique de l'user
   * @return int - nombre de mobilités annulées auxquels l'étudiant est rattaché
   */
  int getNbrMobilitesAnnuleesParEtudiant(String pseudo);

  /**
   * Liste tous les utilisateurs étudiant présent en base de données.
   * 
   * @return liste des utilisateurs étudiant
   */
  ArrayList<UserDto> listerEtudiants();

  /**
   * Supprime un utilisateur de la base de données (important pour les tests).
   * 
   * @param userDto - Utilisateur à supprimer
   */
  void deleteUser(UserDto userDto);

  /**
   * Fait passer un utilisateur du role d'étudiant vers celui de professeur.
   * 
   * @param userDto - Utilisateur à promouvoir
   * @throws VersionException - en cas de souci de version
   */
  void promouvoirUser(UserDto userDto) throws VersionException;

  /**
   * Trouve un utilisateur sur base de son id.
   * 
   * @param id - id de l'utilisateur
   * @return un utilisateur dto
   */
  UserDto findUserById(int id);

}
