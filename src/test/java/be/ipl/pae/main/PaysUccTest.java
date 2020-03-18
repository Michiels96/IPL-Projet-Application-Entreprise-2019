package be.ipl.pae.main;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import be.ipl.pae.biz.dto.PaysDto;
import be.ipl.pae.biz.ucc.PaysUcc;
import be.ipl.pae.utils.Context;
import be.ipl.pae.utils.InjectionDistributor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaysUccTest {

  private PaysUcc paysUcc;

  /**
   * Set up avant chaque test.
   */
  @BeforeEach
  public void setUp() {
    Context.load("test.properties");
    paysUcc = (PaysUcc) InjectionDistributor.getDependancy(PaysUcc.class);
  }

  @Test
  public void testGetPaysById() {
    assertNull(paysUcc.getPaysById(-1), "Ce pays n'existe pas");
    PaysDto pays = paysUcc.getPaysById(10);
    assertEquals("ok", pays.getNom(), "Le pays aurait dû être ok");
    assertNull(paysUcc.getPaysById(42), "Le pays aurait dû être ok");
  }

  @Test
  public void testGetAllPays() { // Intestable
    assertDoesNotThrow(() -> paysUcc.getAllPays());
    assertNull(paysUcc.getAllPays(), "Aurait dû lancer une exception");
  }

}
