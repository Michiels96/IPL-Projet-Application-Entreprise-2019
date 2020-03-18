package be.ipl.pae.main;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;

import be.ipl.pae.biz.dto.DocumentDepartDto;
import be.ipl.pae.biz.dto.DocumentRetourDto;
import be.ipl.pae.biz.factory.BizFactory;
import be.ipl.pae.biz.ucc.DocumentsUcc;
import be.ipl.pae.utils.Context;
import be.ipl.pae.utils.InjectionDistributor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentsUccTest {

  private DocumentsUcc docUcc;

  private BizFactory bf;

  /**
   * Set up avant chaque test.
   */
  @BeforeEach
  public void setUp() {
    Context.load("test.properties");
    docUcc = (DocumentsUcc) InjectionDistributor.getDependancy(DocumentsUcc.class);
    bf = (BizFactory) InjectionDistributor.getDependancy(BizFactory.class);
  }

  @Test
  public void testListerDocumentsDepart() {
    assertNull(docUcc.listerDocumentsDepart(-1));
    assertNull(docUcc.listerDocumentsDepart(42));
  }

  @Test
  public void testListerDocumentsRetour() {
    assertNull(docUcc.listerDocumentsRetour(-1));
    assertNull(docUcc.listerDocumentsRetour(42));
  }

  @Test
  public void testGetDocumentDepart() {
    assertDoesNotThrow(() -> docUcc.getDocumentDepart(42));
    assertNull(docUcc.getDocumentDepart(1));
  }

  @Test
  public void testGetDocumentRetour() {
    assertDoesNotThrow(() -> docUcc.getDocumentRetour(42));
    assertNull(docUcc.getDocumentRetour(1));
  }

  @Test
  public void testUpdateDocumentDepart() {
    DocumentDepartDto dto = bf.getDocumentDepartDto();
    dto.setCharteEtudiant(false);
    dto.setContratBourse(false);
    dto.setConventionStageEtude(false);
    dto.setDocumentEngagement(false);
    dto.setPreuveTestLinguistique(false);
    dto.setIdDocumentsDepart(1);
    assertDoesNotThrow(() -> docUcc.updateDocumentDepart(dto, 1, 1, "", 1));
    dto.setIdDocumentsDepart(42);
    assertDoesNotThrow(() -> docUcc.updateDocumentDepart(dto, 1, 1, "CREE", 1));
    dto.setIdDocumentsDepart(1);
    dto.setPreuveTestLinguistique(true);
    assertDoesNotThrow(() -> docUcc.updateDocumentDepart(dto, 1, 1, "CREE", 1));
    dto.setCharteEtudiant(true);
    dto.setContratBourse(true);
    dto.setConventionStageEtude(true);
    dto.setDocumentEngagement(true);
    dto.setPreuveTestLinguistique(true);
    dto.setIdDocumentsDepart(1);
    assertDoesNotThrow(() -> docUcc.updateDocumentDepart(dto, 1, 1, "", 1));
    assertDoesNotThrow(() -> docUcc.updateDocumentDepart(dto, 1, 1, "EN_PREPARATION", 1));
    dto.setPreuveTestLinguistique(false);
    assertDoesNotThrow(() -> docUcc.updateDocumentDepart(dto, 1, 1, "EN_PREPARATION", 1));
  }

  @Test
  public void testUpdateDocumentRetour() {
    DocumentRetourDto dto = bf.getDocumentRetourDto();
    dto.setAttestationSejour(false);
    dto.setPreuvePassageTest(false);
    dto.setRapportFinal(false);
    dto.setStageReleveNote(false);
    dto.setIdDocumentsRetour(1);
    assertDoesNotThrow(() -> docUcc.updateDocumentRetour(dto, 1, 1, "", 1));
    dto.setIdDocumentsRetour(42);
    assertDoesNotThrow(() -> docUcc.updateDocumentRetour(dto, 1, 1, "A_PAYER", 1));
    dto.setIdDocumentsRetour(1);
    dto.setAttestationSejour(true);
    dto.setPreuvePassageTest(true);
    dto.setRapportFinal(true);
    dto.setStageReleveNote(true);
    dto.setIdDocumentsRetour(1);
    assertDoesNotThrow(() -> docUcc.updateDocumentRetour(dto, 1, 1, "", 1));
    assertDoesNotThrow(() -> docUcc.updateDocumentRetour(dto, 1, 1, "A_PAYER", 1));
  }

  @Test
  public void testGetIdDernierDocDepart() {
    assertDoesNotThrow(() -> docUcc.getIdDernierDocDepart());
    assertDoesNotThrow(() -> docUcc.getIdDernierDocDepart());
  }

  @Test
  public void testGetIdDernierDocretour() {
    assertDoesNotThrow(() -> docUcc.getIdDernierDocretour());
    assertDoesNotThrow(() -> docUcc.getIdDernierDocretour());
  }

  @Test
  public void testInitDocuments() {
    assertDoesNotThrow(() -> docUcc.initDocuments());
    assertDoesNotThrow(() -> docUcc.initDocuments());
  }



}
