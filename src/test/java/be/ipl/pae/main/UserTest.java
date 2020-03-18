package be.ipl.pae.main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.ipl.pae.biz.interfce.User;
import be.ipl.pae.domaine.UserStubFull;
import be.ipl.pae.enums.Role;
import be.ipl.pae.utils.Context;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

public class UserTest {

  private User userStub;


  /**
   * Initialise les tests.
   * 
   * @throws Exception - En cas d'exception
   */
  @BeforeEach
  public void setUp() throws Exception {
    this.userStub = new UserStubFull("pseudoStub", "mdpStub", "nomStub", "prenomStub", "mailStub",
        "adresseStub", "communeStub", "numCompteStub", "titulaireCompteStub", "nomBanqueStub",
        Role.E, true, Date.valueOf(LocalDate.now()), 1420, "12345678", 3, "12", true);
    Context.load("test.properties");
  }

  @Test
  public void testSetterEtGetterPseudo() {
    assertEquals("pseudoStub", userStub.getPseudo());
    userStub.setPseudo("hasChanged");
    assertEquals("hasChanged", userStub.getPseudo());
  }

  @Test
  public void testSetterEtGetterMotDePasse() {
    assertEquals("mdpStub", userStub.getMotDePasse());
    userStub.setMotDePasse("hasChanged");
    assertEquals("hasChanged", userStub.getMotDePasse());
  }

  @Test
  public void testSetterEtGetterNom() {
    assertEquals("nomStub", userStub.getNom());
    userStub.setNom("hasChanged");
    assertEquals("hasChanged", userStub.getNom());
  }

  @Test
  public void testSetterEtGetterPrenom() {
    assertEquals("prenomStub", userStub.getPrenom());
    userStub.setPrenom("hasChanged");
    assertEquals("hasChanged", userStub.getPrenom());
  }

  @Test
  public void testSetterEtGetterMail() {
    assertEquals("mailStub", userStub.getEmail());
    userStub.setEmail("hasChanged");
    assertEquals("hasChanged", userStub.getEmail());
  }

  @Test
  public void testSetterEtGetterRole() {
    assertEquals(Role.E, userStub.getRole());
    userStub.setRole(Role.P);
    assertEquals(Role.P, userStub.getRole());
  }

  @Test
  public void testSetterEtGetterAdresse() {
    assertEquals("adresseStub", userStub.getAdresse());
    userStub.setAdresse("hasChanged");
    assertEquals("hasChanged", userStub.getAdresse());
  }

  @Test
  public void testSetterEtGetterCommune() {
    assertEquals("communeStub", userStub.getCommune());
    userStub.setCommune("hasChanged");
    assertEquals("hasChanged", userStub.getCommune());
  }

  @Test
  public void testSetterEtGetterNumCompte() {
    assertEquals("numCompteStub", userStub.getNumCompte());
    userStub.setNumCompte("hasChanged");
    assertEquals("hasChanged", userStub.getNumCompte());
  }

  @Test
  public void testSetterEtGetterTitulaireCompte() {
    assertEquals("titulaireCompteStub", userStub.getTitulaireCompte());
    userStub.setTitulaireCompte("hasChanged");
    assertEquals("hasChanged", userStub.getTitulaireCompte());
  }

  @Test
  public void testSetterEtGetterNomBanque() {
    assertEquals("nomBanqueStub", userStub.getNomBanque());
    userStub.setNomBanque("hasChanged");
    assertEquals("hasChanged", userStub.getNomBanque());
  }

  @Test
  public void testSetterEtGetterHomme() {
    assertTrue(userStub.isHomme());
    userStub.setHomme(false);
    assertFalse(userStub.isHomme());
  }

  @Test
  public void testSetterEtGetterDateDeNaissance() {
    assertEquals(Date.valueOf(LocalDate.now()), userStub.getDateNaissance());
    Date date = Date.valueOf(LocalDate.now());
    userStub.setDateNaissance(date);
    assertEquals(date, userStub.getDateNaissance());
  }

  /*
   * @Test public void testSetterEtGetterDateInscription() {
   * assertEquals(Date.valueOf(LocalDate.now()), userStub.getDateInscription()); Date date =
   * Date.valueOf(LocalDate.now()); userStub.setDateInscription(date); assertEquals(date,
   * userStub.getDateInscription()); }
   */

  @Test
  public void testSetterEtGetterCodePostal() {
    assertEquals(1420, userStub.getCodePostal());
    userStub.setCodePostal(0);
    assertEquals(0, userStub.getCodePostal());
  }

  @Test
  public void testSetterEtGetterTelephone() {
    assertEquals("12345678", userStub.getNumTel());
    userStub.setNumTel("0");
    assertEquals("0", userStub.getNumTel());
  }

  @Test
  public void testSetterEtGetterNombreAnneeReussies() {
    assertEquals(3, userStub.getNombreAnneeReussie());
    userStub.setNombreAnneeReussie(0);
    assertEquals(0, userStub.getNombreAnneeReussie());
  }

  @Test
  public void testSetterEtGetterCodeBic() {
    assertEquals("12", userStub.getCodeBic());
    userStub.setCodeBic("0");
    assertEquals("0", userStub.getCodeBic());
  }

  @Test
  public void testCheckPassword() {
    assertTrue(userStub.checkPassword(""));
  }

}
