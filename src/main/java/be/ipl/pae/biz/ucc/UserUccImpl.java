package be.ipl.pae.biz.ucc;

import be.ipl.pae.annotations.Inject;
import be.ipl.pae.biz.dto.MobiliteDto;
import be.ipl.pae.biz.dto.UserDto;
import be.ipl.pae.biz.interfce.User;
import be.ipl.pae.dal.DalServices;
import be.ipl.pae.dal.MobiliteDao;
import be.ipl.pae.dal.UserDao;
import be.ipl.pae.enums.EtatMobilite;
import be.ipl.pae.enums.Role;
import be.ipl.pae.exceptions.DalException;
import be.ipl.pae.exceptions.VersionException;
import be.ipl.pae.utils.Logging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

class UserUccImpl implements UserUcc {

  private static final Logger LOGGER = Logging.getLogger(UserUccImpl.class.getName());

  @Inject
  private MobiliteDao mobDao;
  @Inject
  private UserDao dao;
  @Inject
  private DalServices dal;

  /**
   * regarde si il y a déjà un user en bd.
   */
  @Override
  public void verifAdminPresent() {
    dal.startTransaction();
    try {
      dao.isAdminPresent();
    } catch (DalException dalExc) {
      LOGGER.severe("La méthode verifAdminPresent a echouée : " + dalExc.getMessage());
      dal.rollBackTransaction();
    }
    dal.commitTransaction();
  }

  /**
   * Connecte un utilisateur sur base de son pseudo et de son mot de passe si celui ci existe.
   * 
   * @param pseudo - String correspondant au pseudo du user
   * @param password - String correspondant au mot de passe du user
   * @return true si la connexion a réussie, false sinon
   */
  @Override
  public UserDto seConnecter(String pseudo, String password) {
    dal.startTransaction();
    User user;
    try {
      user = (User) dao.findUserByLogin(pseudo);
      if (user != null) {
        if (user.checkPassword(password)) {
          LOGGER.info("L'utilisateur " + pseudo + " vient de se connecter.");
          dal.commitTransaction();
          return (UserDto) user;
        } else {
          LOGGER.info("L'utilisateur " + pseudo + " s'est trompé de mot de passe.");
        }
      } else {
        LOGGER.info("L'utilisateur " + pseudo + " n'existe pas.");
      }
    } catch (DalException dalExc) {
      dal.rollBackTransaction();
      LOGGER.severe("La méthode seConnecter a echouée : " + dalExc.getMessage());
    }
    dal.rollBackTransaction();
    return null;
  }

  /**
   * Ajoute un utilisateur s'il n'existe pas déjà.
   * 
   * @param userDto - DTO permettant d'envoyer des données utilisateur dans la couche business
   * @return true si l'inscription s'est faites, false sinon
   */
  @Override
  public boolean inscrireUtilisateur(UserDto userDto) {
    dal.startTransaction();
    try {
      if (dao.findUserByLogin(userDto.getPseudo()) != null) {
        LOGGER.info("L'utilisateur " + userDto.getPseudo()
            + " existait déjà. L'inscription à été refusée.");
        dal.rollBackTransaction();
        return false;
      }
      User user = (User) userDto;
      user.cryptPassword();
      dao.insertUser((UserDto) user);
      dal.commitTransaction();
      LOGGER.info("L'utilisateur " + userDto.getPseudo() + " vient de s'inscrire.");
      return true;
    } catch (DalException exc) {
      dal.rollBackTransaction();
      LOGGER.severe("La méthode inscrireUtilisateur a echouée : " + exc.getMessage());
      return false;
    }

  }

  /**
   * Ajout des infos supp de l'utilisateur.
   * 
   * @param userDto - utilisateur qui effectue l'action
   * @param nouveauPseudo - le pseudo actuel ou bien nouveau de l'utilisateur
   * @param nouveau mot de passe de l'utilisateur
   * @return true si ajout ok, false sinon.
   */
  @Override
  public boolean ajouterInfosUtilisateur(UserDto userDto, String nouveauPseudo, String nouveauMdp) {
    dal.startTransaction();
    try {
      if (dao.findUserByLogin(userDto.getPseudo()) == null) {
        dal.rollBackTransaction();
        LOGGER.info("L'utilisateur " + userDto.getPseudo() + " n'existe pas.");
        return false;
      }
      if (nouveauMdp != null) {
        User user = (User) userDto;
        user.setMotDePasse(nouveauMdp);
        user.cryptPassword();
        userDto.setMotDePasse(user.getMotDePasse());
      }
      dao.addInfosUser(userDto, nouveauPseudo);
      dal.commitTransaction();
      LOGGER.info(
          "L'utilisateur " + userDto.getPseudo() + " à ajouter des informations à son compte.");
      return true;
    } catch (DalException | VersionException exc) {
      dal.rollBackTransaction();
      LOGGER.severe("La méthode ajouterInfosUtilisateur a echouée : " + exc.getMessage());
      return false;
    }
  }

  /**
   * Effectue une recherche dans la base de données sur base d'un pattern dans la recherche.
   * 
   * @param userDto - DTO permettant d'envoyer des données utilisateurs
   * @param pattern - String permettant de matcher avec un nom / prenom / mail / pseudo d'un user
   * @return ArrayList - list de tous les users qui on matché avec le pattern.
   */
  @Override
  public ArrayList<UserDto> rechercherUser(UserDto userDto, String pattern) {
    dal.startTransaction();
    try {
      if (dao.findUserByLogin(userDto.getPseudo()) == null) {
        dal.rollBackTransaction();
        LOGGER.info("L'utilisateur " + userDto.getPseudo() + " n'existe pas.");
        return null;
      }
      dal.commitTransaction();
      LOGGER.info("L'utilisateur " + userDto.getPseudo()
          + " à fait une recherche d'utilisateur via le pattern : << " + pattern + " >>.");
      return dao.rechercherUser(pattern);
    } catch (DalException exc) {
      dal.rollBackTransaction();
      LOGGER.severe("La méthode rechercherUser a echouée : " + exc.getMessage());
      return null;
    }
  }

  /**
   * Permet de lister tous les utilisateurs présent en base de données.
   * 
   * @param userDto - utilisateur qui effectue l'action.
   * @return Liste des utilisateurs présent en base de données.
   */
  @Override
  public ArrayList<UserDto> listerUser(UserDto userDto) {
    dal.startTransaction();
    try {
      if (dao.findUserByLogin(userDto.getPseudo()) == null) {
        dal.rollBackTransaction();
        LOGGER.info("L'utilisateur " + userDto.getPseudo() + " n'existe pas.");
        return null;
      }
      if (userDto.getRole().equals(Role.P) || userDto.getRole().equals(Role.A)) {
        ArrayList<UserDto> users = dao.listerUser();
        dal.commitTransaction();
        LOGGER.info("L'utilisateur " + userDto.getPseudo() + " a listé les utilisateurs.");
        return users;
      }
      LOGGER
          .info("L'utilisateur " + userDto.getPseudo() + " a échoué pour lister les utilisateurs.");
      dal.rollBackTransaction();
      return null;
    } catch (DalException exc) {
      dal.rollBackTransaction();
      LOGGER.severe("La méthode listerUser a echouée : " + exc.getMessage());
      return null;
    }
  }

  /**
   * Permet de lister tous les utilisateurs étudiants présent en base de données.
   * 
   * @param userDto - utilisateur qui effectue l'action.
   * @return Liste des utilisateurs etudiants présent en base de données.
   */
  @Override
  public ArrayList<UserDto> listerEtudiants(UserDto userDto) {
    dal.startTransaction();
    try {
      if (dao.findUserByLogin(userDto.getPseudo()) == null) {
        dal.rollBackTransaction();
        LOGGER.info("L'utilisateur " + userDto.getPseudo() + " n'existe pas.");
        return null;
      }
      if (userDto.getRole().equals(Role.P) || userDto.getRole().equals(Role.A)) {
        ArrayList<UserDto> etudiants = dao.listerEtudiants();
        dal.commitTransaction();
        LOGGER
            .info("L'utilisateur " + userDto.getPseudo() + " a listé les utilisateurs étudiants.");
        return etudiants;
      }
      LOGGER.info("L'utilisateur " + userDto.getPseudo()
          + " a échoué pour lister les utilisateurs étudiants.");
      dal.rollBackTransaction();
      return null;
    } catch (DalException exc) {
      dal.rollBackTransaction();
      LOGGER.severe("La méthode listerEtudiants a echouée : " + exc.getMessage());
      return null;
    }

  }

  /**
   * Permet de récuperer le nombre de mobilités auxquels l'étudiant est rattaché
   * 
   * @param userDto - utilisateur qui effectue l'action.
   * @return le nombre de mobilités auxquels l'étudiant est rattaché.
   */
  @Override
  public int getNbrMobilitesParEtudiant(UserDto userDto) {
    dal.startTransaction();
    try {
      if (dao.findUserByLogin(userDto.getPseudo()) == null) {
        dal.rollBackTransaction();
        LOGGER.info("L'utilisateur " + userDto.getPseudo() + " n'existe pas.");
        return -1;
      }
      if (userDto.getRole().equals(Role.E)) {
        int result = dao.getNbrMobilitesParEtudiant(userDto.getPseudo());
        dal.commitTransaction();
        LOGGER.info("L'utilisateur " + userDto.getPseudo()
            + " a récuperé le nombre de mobilités auxquels il est rattaché.");
        return result;
      }
      dal.rollBackTransaction();
      LOGGER.info("L'utilisateur " + userDto.getPseudo()
          + " a échoué pour récuperer le nombre de mobilités auxquels il est rattaché.");
      return -1;
    } catch (DalException exc) {
      dal.rollBackTransaction();
      LOGGER.severe("La méthode getNbrMobilitesParEtudiant a echouée : " + exc.getMessage());
      return -1;
    }

  }

  /**
   * Permet de récuperer le nombre de mobilités annulées auxquels l'étudiant est rattaché
   * 
   * @param userDto - utilisateur qui effectue l'action.
   * @return le nombre de mobilités annulées auxquels l'étudiant est rattaché.
   */
  @Override
  public int getNbrMobilitesAnnuleesParEtudiant(UserDto userDto) {
    dal.startTransaction();
    try {
      if (dao.findUserByLogin(userDto.getPseudo()) == null) {
        dal.rollBackTransaction();
        LOGGER.info("L'utilisateur " + userDto.getPseudo() + " n'existe pas.");
        return -1;
      }
      if (userDto.getRole().equals(Role.E)) {
        int result = dao.getNbrMobilitesAnnuleesParEtudiant(userDto.getPseudo());
        dal.commitTransaction();
        LOGGER.info("L'utilisateur " + userDto.getPseudo()
            + " a récuperé le nombre de mobilités annulées auxquels il est rattaché.");
        return result;
      }
      dal.rollBackTransaction();
      LOGGER.info("L'utilisateur " + userDto.getPseudo()
          + " a échoué pour récuperer le nombre de mobilités annulées auxquels il est rattaché.");
      return -1;
    } catch (DalException exc) {
      dal.rollBackTransaction();
      LOGGER
          .severe("La méthode getNbrMobilitesAnnuleesParEtudiant a echouée : " + exc.getMessage());
      return -1;
    }

  }

  /**
   * Supprime l'utilisateur passé en paramètre.
   * 
   * @param userDto - utilisateur à supprimer.
   */
  @Override
  public void supprimerUtilisateur(UserDto userDto) {
    try {
      dal.startTransaction();
      dao.deleteUser(userDto);
      dal.commitTransaction();
      LOGGER.info("L'utilisateur " + userDto.getPseudo() + " vient d'être supprimé.");
    } catch (DalException exc) {
      dal.rollBackTransaction();
      LOGGER.severe("La méthode supprimerUtilisateur a echouée : " + exc.getMessage());
    }
  }

  /**
   * Renvoie les informations appartenant à l'utilisateur dont le pseudo est passé en paramètre.
   * 
   * @param pseudo - pseudo de l'utilisateur à chercher.
   * @return les informations de cet utilisateur.
   */
  @Override
  public UserDto getInfoUser(String pseudo) {
    dal.startTransaction();
    UserDto dto;
    try {
      dto = dao.findUserByLogin(pseudo);
      dal.commitTransaction();
      LOGGER.info("Les informations de : " + pseudo + " ont été récupérées.");
      return dto;
    } catch (DalException exc) {
      dal.rollBackTransaction();
      LOGGER.severe("La méthode getInfoUser a echouée : " + exc.getMessage());
    }
    return null;
  }

  @Override
  public UserDto getPseudoUser(int id) {
    dal.startTransaction();
    UserDto dto;
    try {
      dto = dao.findUserById(id);
      dal.commitTransaction();
      LOGGER.info("Les informations de : " + dto.getPseudo() + " ont été récupérées.");
      return dto;
    } catch (DalException exc) {
      dal.rollBackTransaction();
      LOGGER.severe("La méthode getInfoUser a echouée : " + exc.getMessage());
    }
    return null;
  }

  @Override
  public ArrayList<MobiliteDto> getMobiliteByUser(String pseudo) {
    try {
      dal.startTransaction();
      ArrayList<MobiliteDto> mobDtos;
      mobDtos = mobDao.getMobiliteParPseudo(pseudo);
      dal.commitTransaction();
      LOGGER.info("Les mobilités de : " + pseudo + " ont été récupérées");
      return mobDtos;
    } catch (DalException exc) {
      dal.rollBackTransaction();
      LOGGER.severe("La méthode getMobiliteByUser a echouée : " + exc.getMessage());
      return null;
    }
  }

  @Override
  public boolean gestionMobilite(ArrayList<MobiliteDto> newMob, String pseudo)
      throws VersionException {
    dal.startTransaction();
    UserDto user;
    try {
      user = dao.findUserByLogin(pseudo);
      if (user == null) {
        dal.rollBackTransaction();
        return false;
      }
      ArrayList<MobiliteDto> oldMob = mobDao.getMobiliteParPseudo(pseudo);
      Map<Integer, Boolean> checkIdMob = new HashMap<Integer, Boolean>();
      for (MobiliteDto mobDto : oldMob) {
        checkIdMob.put(mobDto.getIdMobilite(), false);
      }
      for (MobiliteDto mobDto : newMob) {
        // Récupérer les id mobs déjà présente pour l'étudiant. si mobDto.getIdMob == -1 alors add
        // basic, si idMob > -1 UPDATE, si un id n'a pas été traité --> delete
        // Et check le versionning
        if (mobDto.getIdMobilite() == -1) {
          mobDto.setEtudiant(user.getIdUtilisateur());
          mobDto.setEtatMobilite(EtatMobilite.CREE);
          mobDao.insertMobilite(mobDto);
        } else if (!checkIdMob.get(mobDto.getIdMobilite())) {
          checkIdMob.put(mobDto.getIdMobilite(), true);
          mobDao.updateMobilite(mobDto);
        }
      }
      for (Integer id : checkIdMob.keySet()) {
        if (!checkIdMob.get(id)) {
          for (MobiliteDto mobDto : oldMob) {
            if (mobDto.getIdMobilite() == id) {
              mobDao.removeMobilite(mobDto);
            }
          }
        }
      }
      dal.commitTransaction();
      LOGGER.info("Les mobilités on été mis à jour pour l'utilisateur.");
      return true;
    } catch (DalException exc) {
      dal.rollBackTransaction();
      LOGGER.severe("La méthode gestionMobilite a echouée : " + exc.getMessage());
      return false;
    }
  }

  @Override
  public void promouvoirEtudiant(UserDto self, String pseudo) {
    try {
      dal.startTransaction();
      if (self.getRole() == Role.E) {
        LOGGER
            .info("Un étudiant (" + self.getPseudo() + ") ne peut pas promouvoir un utilisateur.");
        dal.rollBackTransaction();
        return;
      }
      UserDto user;
      user = dao.findUserByLogin(pseudo);
      if (user == null) {
        LOGGER.info("L'étudiant " + pseudo + " n'existe pas.");
        dal.rollBackTransaction();
        return;
      }
      dao.promouvoirUser(user);
      LOGGER.info("L'utilisateur " + user.getPseudo() + " à été promu.");
      dal.commitTransaction();
    } catch (DalException | VersionException exc) {
      dal.rollBackTransaction();
      LOGGER.severe("La méthode promouvoirEtudiant a echouée : " + exc.getMessage());
    }

  }

  @Override
  public String getNotificationByUser(int id) {
    try {
      Map<Integer, String> map = new HashMap<Integer, String>();
      String notifs = "Bien le bonjour très cher,\nVoici les notifications du jour :\n";
      dal.startTransaction();
      map = mobDao.getNotificationsParId(id);
      if (map.isEmpty()) {
        return "rien";
      }
      for (Entry<Integer, String> entr : map.entrySet()) {
        MobiliteDto dto = mobDao.getMobiliteParId(entr.getKey());
        notifs += "Votre mobilité de type " + dto.getTypeMobilite() + " "
            + dto.getProgramme().getTypeProgramme() + " devant commencer au "
            + dto.getPeriode().getPeriode()
            + "à malheureusement été annulée pour la raison suivante : \n" + entr.getValue() + "\n";
      }
      dal.commitTransaction();
      LOGGER.info("Les notifications de : " + id + " ont été récupérées");
      return notifs;
    } catch (DalException exc) {
      dal.rollBackTransaction();
      LOGGER.severe("La méthode getNotificationByUser a echouée : " + exc.getMessage());
      return null;
    }
  }

}
