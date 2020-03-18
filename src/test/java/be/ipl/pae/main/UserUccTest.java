package be.ipl.pae.main;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.ipl.pae.biz.dto.MobiliteDto;
import be.ipl.pae.biz.dto.UserDto;
import be.ipl.pae.biz.factory.BizFactory;
import be.ipl.pae.biz.ucc.UserUcc;
import be.ipl.pae.domaine.UserDaoMock;
import be.ipl.pae.domaine.UserStub;
import be.ipl.pae.enums.Role;
import be.ipl.pae.exceptions.VersionException;
import be.ipl.pae.utils.Context;
import be.ipl.pae.utils.InjectionDistributor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

public class UserUccTest {

  private UserUcc userUcc;
  private UserStub userExistant;
  private BizFactory bf;
  private UserDaoMock dao;

  /**
   * Set up avant chaque test.
   */
  @BeforeEach
  public void setUp() {
    Context.load("test.properties");
    userUcc = (UserUcc) InjectionDistributor.getDependancy(UserUcc.class);
    bf = (BizFactory) InjectionDistributor.getDependancy(BizFactory.class);
    dao = (UserDaoMock) InjectionDistributor.getDependancy(UserDaoMock.class);
    userExistant = new UserStub("ok", "aa", "aa", "aa", "aa", Role.P);
  }

  @Test
  public void testVerifAdminPresent() {
    assertDoesNotThrow(() -> userUcc.verifAdminPresent());
    userUcc.verifAdminPresent();
    assertFalse(dao.adminPresent);
    userUcc.verifAdminPresent();
    assertTrue(dao.adminPresent);
  }

  @Test
  public void testConnexion() {
    assertNull(userUcc.seConnecter("ko", "aa"), "L'utilisateur est pourtant inexistant");
    assertNotNull(userUcc.seConnecter("ok", "Admin"), "L'utilisateur est pourtant bien existant");
    assertNull(userUcc.seConnecter("ok", "ab"), "Il aurait dû prévenir d'un mauvais mot de passe");
    assertNull(userUcc.seConnecter("exc", "ab"));
  }


  @Test
  public void testInscriptionUtilisateur() {
    assertFalse(userUcc.inscrireUtilisateur((UserDto) userExistant), "L'utilisateur existe déjà");
    UserStub newUser = new UserStub("ko", "aa", "aa", "aa", "aa", Role.E);
    assertTrue(userUcc.inscrireUtilisateur((UserDto) newUser),
        "L'utilisateur aurait dû être ajouté");
    newUser.setPseudo("exc");
    assertFalse(userUcc.inscrireUtilisateur(newUser));
  }

  @Test
  public void testAjouterInfosUtilisateur() {
    assertDoesNotThrow(
        () -> userUcc.ajouterInfosUtilisateur((UserDto) userExistant, null,
            userExistant.getMotDePasse()),
        "L'utilisateur aurait dû pouvoir ajouter des informations");
    userExistant.setMotDePasse(null);
    assertDoesNotThrow(
        () -> userUcc.ajouterInfosUtilisateur((UserDto) userExistant, null,
            userExistant.getMotDePasse()),
        "L'utilisateur aurait dû pouvoir ajouter des informations");
    UserStub newUser = new UserStub("ko", "aa", "aa", "aa", "aa", Role.E);
    assertDoesNotThrow(
        () -> userUcc.ajouterInfosUtilisateur((UserDto) newUser, newUser.getPseudo(), null),
        "L'utilisateur n'aurait pas dû pouvoir ajouter des informations");
    assertDoesNotThrow(
        () -> userUcc.ajouterInfosUtilisateur((UserDto) newUser, newUser.getPseudo(), "a"),
        "L'utilisateur n'aurait pas dû pouvoir ajouter des informations");
    newUser.setPseudo("exc");
    assertDoesNotThrow(
        () -> userUcc.ajouterInfosUtilisateur((UserDto) newUser, newUser.getPseudo(), "a"));
  }


  @Test
  public void testRechercherUser() {
    assertEquals("test", userUcc.rechercherUser(userExistant, "test").get(0).getPseudo(),
        "Le pattern test aurait dû renvoyer un user");
    assertNull(userUcc.rechercherUser(userExistant, "bob"),
        "Le pattern bob auraît dû renvoyer null");
    assertNull(userUcc.rechercherUser(new UserStub("ko", null, null, null, null, null), "test"),
        "L'user ne doit pas pouvoir faire cette recherche car il n'existe pas");
    assertNull(userUcc.rechercherUser(new UserStub("exc", null, null, null, null, null), "osef"));
  }

  @Test
  public void testListerUser() {
    assertEquals("test", userUcc.listerUser(userExistant).get(0).getPseudo(),
        "La liste de user aurait dû apparaître");
    userExistant.setRole(Role.E);
    assertNull(userUcc.listerUser(userExistant), "Un étudiant ne peut pas lister les users");
    userExistant.setRole(Role.A);
    assertEquals("test", userUcc.listerUser(userExistant).get(0).getPseudo(),
        "Un admin peut lister les users");
    assertNull(userUcc.listerUser(new UserStub("ko", null, null, null, null, null)),
        "L'user ne doit pas pouvoir faire cette recherche car il n'existe pas");
    assertNull(userUcc.listerUser(new UserStub("exc", null, null, null, null, null)));
  }

  @Test
  public void testSupprimerUtilisateur() {
    assertDoesNotThrow(() -> userUcc.supprimerUtilisateur(userExistant), "N'aurait rien du lancer");
    assertDoesNotThrow(() -> userUcc.supprimerUtilisateur(userExistant));
  }

  @Test
  public void testGetInfoUser() {
    assertNull(userUcc.getInfoUser("ko"), "L'utilisateur est pourtant inexistant");
    assertNotNull(userUcc.getInfoUser("ok"), "L'utilisateur est pourtant bien existant");
    assertNull(userUcc.getInfoUser("exc"));
  }

  @Test
  public void testGetInfosUser() {
    assertEquals("ok", userUcc.getInfoUser("ok").getPseudo(), "L'information était incorrecte");
    assertNull(userUcc.getInfoUser("ko"), "Le pseudo n'existait pas");
    assertNull(userUcc.getInfoUser("exc"));
  }

  @Test
  public void testGetMobiliteByUser() {
    assertEquals(42, userUcc.getMobiliteByUser("ok").get(0).getFinancement(),
        "On aurait dû voir les mobilités de l'étudiant");
    assertNull(userUcc.getMobiliteByUser("ko"), "L'étudiant n'existe pas");
    assertNull(userUcc.getMobiliteByUser("exc"));
  }

  @Test
  public void testGestionMobilite() throws VersionException {
    assertFalse(userUcc.gestionMobilite(null, "ko"), "L'étudiant n'existe pas");
    ArrayList<MobiliteDto> liste = new ArrayList<MobiliteDto>();
    MobiliteDto mob = bf.getMobiliteDto();
    mob.setFinancement(0);
    liste.add(mob);
    assertThrows(NullPointerException.class, () -> userUcc.gestionMobilite(liste, "ok"),
        "Souci d'exception");
    liste.get(0).setFinancement(42);
    assertThrows(NullPointerException.class, () -> userUcc.gestionMobilite(liste, "ok"),
        "Souci d'exception");
    assertFalse(userUcc.gestionMobilite(liste, "exc"));
    liste.get(0).setIdMobilite(-1);
    liste.get(0).setFinancement(12);
    assertTrue(userUcc.gestionMobilite(liste, "ok"));
    liste.get(0).setIdMobilite(1);
    liste.get(0).setFinancement(12);
    assertTrue(userUcc.gestionMobilite(liste, "ok"));
  }

  @Test
  public void testListerEtudiant() {
    assertEquals("test", userUcc.listerEtudiants(userExistant).get(0).getPseudo(),
        "La liste de user aurait dû apparaître");
    userExistant.setRole(Role.E);
    assertNull(userUcc.listerEtudiants(userExistant),
        "Un étudiant ne peut pas lister les étudiants");
    userExistant.setRole(Role.A);
    assertEquals("test", userUcc.listerEtudiants(userExistant).get(0).getPseudo(),
        "Un admin peut lister les users");
    assertNull(userUcc.listerEtudiants(new UserStub("ko", null, null, null, null, null)),
        "L'user ne doit pas pouvoir faire cette recherche car il n'existe pas");
    assertNull(userUcc.listerEtudiants(new UserStub("exc", null, null, null, null, null)));
  }

  @Test
  public void testGetNbrMobilitesParEtudiant() {
    assertEquals(-1,
        userUcc.getNbrMobilitesParEtudiant(new UserStub("ko", null, null, null, null, null)));
    assertEquals(-1,
        userUcc.getNbrMobilitesParEtudiant(new UserStub("ok", null, null, null, null, Role.P)));
    assertEquals(-1,
        userUcc.getNbrMobilitesParEtudiant(new UserStub("ok", null, null, null, null, Role.A)));
    assertEquals(42,
        userUcc.getNbrMobilitesParEtudiant(new UserStub("ok", null, null, null, null, Role.E)));
    assertEquals(-1,
        userUcc.getNbrMobilitesParEtudiant(new UserStub("exc", null, null, null, null, null)));
  }

  @Test
  public void testGetNbrMobilitesAnnuleesParEtudiant() {
    assertEquals(-1, userUcc
        .getNbrMobilitesAnnuleesParEtudiant(new UserStub("ko", null, null, null, null, null)));
    assertEquals(-1, userUcc
        .getNbrMobilitesAnnuleesParEtudiant(new UserStub("ok", null, null, null, null, Role.P)));
    assertEquals(-1, userUcc
        .getNbrMobilitesAnnuleesParEtudiant(new UserStub("ok", null, null, null, null, Role.A)));
    assertEquals(42, userUcc
        .getNbrMobilitesAnnuleesParEtudiant(new UserStub("ok", null, null, null, null, Role.E)));
    assertEquals(-1, userUcc
        .getNbrMobilitesAnnuleesParEtudiant(new UserStub("exc", null, null, null, null, null)));
  }

  @Test
  public void testPromouvoirEtudiant() {
    UserDto userEleve = userExistant;
    userEleve.setRole(Role.E);
    assertDoesNotThrow(() -> userUcc.promouvoirEtudiant(userEleve, null));
    userExistant.setRole(Role.P);
    assertDoesNotThrow(() -> userUcc.promouvoirEtudiant(userExistant, "ko"));
    assertDoesNotThrow(() -> userUcc.promouvoirEtudiant(userExistant, "ok"));
    assertDoesNotThrow(() -> userUcc.promouvoirEtudiant(userExistant, "ok"));
  }

  @Test
  public void testGetPseudoUser() {
    assertDoesNotThrow(() -> userUcc.getPseudoUser(42));
    assertEquals("ok", userUcc.getPseudoUser(5).getPseudo());
  }

  @Test
  public void testGetNotificationByUser() {
    assertEquals("rien", userUcc.getNotificationByUser(12));
    assertNull(userUcc.getNotificationByUser(42));
    assertDoesNotThrow(() -> userUcc.getNotificationByUser(5));
  }

  /**
   * Crée une string aléatoire (utile pour des pseudos etc). Dans le cas de création d'un pseudo,
   * veuillez utiliser une longueur minimale de 7 afin de garantir un risque presque nul de
   * collision (1 chance sur 11 120 967 936).
   */
  @SuppressWarnings("unused")
  private String creationDeStringAleatoire(int longueur) {
    int leftLimit = 97; // lettre 'a'
    int rightLimit = 122; // lettre 'z'
    int targetStringLength = longueur;
    Random random = new Random();
    StringBuilder buffer = new StringBuilder(targetStringLength);
    for (int i = 0; i < targetStringLength; i++) {
      int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
      buffer.append((char) randomLimitedInt);
    }
    return buffer.toString();
  }
}
