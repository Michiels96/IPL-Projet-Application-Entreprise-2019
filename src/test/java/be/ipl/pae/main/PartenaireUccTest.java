package be.ipl.pae.main;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import be.ipl.pae.biz.dto.PartenaireDto;
import be.ipl.pae.biz.factory.BizFactory;
import be.ipl.pae.biz.ucc.PartenaireUcc;
import be.ipl.pae.utils.Context;
import be.ipl.pae.utils.InjectionDistributor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PartenaireUccTest {

  private PartenaireUcc partUcc;

  private BizFactory bf;

  /**
   * Set up avant chaque test.
   */
  @BeforeEach
  public void setUp() {
    Context.load("test.properties");
    partUcc = (PartenaireUcc) InjectionDistributor.getDependancy(PartenaireUcc.class);
    bf = (BizFactory) InjectionDistributor.getDependancy(BizFactory.class);
  }

  @Test
  public void testGetPartenaireById() {
    assertNull(partUcc.getPartenaireById(-1), "Ce partenaire n'existe pas");
    PartenaireDto pays = partUcc.getPartenaireById(10);
    assertEquals("ok", pays.getNomComplet(), "Le partenaire aurait dû être ok");
    assertNull(partUcc.getPartenaireById(42));
  }

  @Test
  public void testGetAllPartenaire() {
    assertDoesNotThrow(() -> partUcc.getAllPartenaire());
    assertNull(partUcc.getAllPartenaire());
  }

  @Test
  public void testInsererPartenaire() {
    assertDoesNotThrow(() -> partUcc.addPartenaire(null));
    assertDoesNotThrow(() -> partUcc.addPartenaire(bf.getPartenaireDto()));
  }

}
