package be.ipl.pae.main;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.ipl.pae.biz.dto.MobiliteDto;
import be.ipl.pae.biz.factory.BizFactory;
import be.ipl.pae.biz.ucc.MobiliteUcc;
import be.ipl.pae.utils.Context;
import be.ipl.pae.utils.InjectionDistributor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class MobiliteUccTest {

  private MobiliteUcc mobUcc;

  private BizFactory bf;

  /**
   * Set up avant chaque test.
   */
  @BeforeEach
  public void setUp() {
    Context.load("test.properties");
    mobUcc = (MobiliteUcc) InjectionDistributor.getDependancy(MobiliteUcc.class);
    bf = (BizFactory) InjectionDistributor.getDependancy(BizFactory.class);
  }

  @Test
  public void testGetMobiliteByPseudo() {
    assertNull(mobUcc.getMobiliteByPseudo("ko"), "Ce partenaire n'existe pas");
    ArrayList<MobiliteDto> mob = mobUcc.getMobiliteByPseudo("ok");
    assertEquals(42, mob.get(0).getFinancement(), "La mobilité aurait dû être ok");
    assertNull(mobUcc.getMobiliteByPseudo("exc"));
  }

  @Test
  public void testGetAllMobilite() {
    ArrayList<MobiliteDto> mob = mobUcc.getAllMobilite();
    assertEquals(42, mob.get(0).getFinancement(), "La mobilité aurait dû être ok");
    assertNull(mobUcc.getAllMobilite(), "Ce partenaire n'existe pas");
    assertNull(mobUcc.getAllMobilite());
  }

  @Test
  public void testValiderMobilite() {
    MobiliteDto mob = bf.getMobiliteDto();
    mob.setFinancement(42);
    assertDoesNotThrow(() -> mobUcc.validerMobilite(mob));
    mob.setFinancement(0);
    assertDoesNotThrow(() -> mobUcc.validerMobilite(mob));
  }

  @Test
  public void testGetMobiliteById() {
    assertDoesNotThrow(() -> mobUcc.getMobiliteParId(42));
    assertNull(mobUcc.getMobiliteParId(-1));
    assertEquals(66, mobUcc.getMobiliteParId(5).getFinancement());
  }

  @Test
  public void testAnnulerMobilite() {
    assertThrows(NullPointerException.class, () -> mobUcc.annulerMobilite(42));
    assertThrows(NullPointerException.class, () -> mobUcc.annulerMobilite(-1));
    assertDoesNotThrow(() -> mobUcc.annulerMobilite(4));
    assertDoesNotThrow(() -> mobUcc.annulerMobilite(0));
  }

  @Test
  public void testAjouterMessageAnnulation() {
    assertDoesNotThrow(() -> mobUcc.ajouterMessageAnnulation(1, "exc"));
    assertDoesNotThrow(() -> mobUcc.ajouterMessageAnnulation(5, ""));
  }

}
