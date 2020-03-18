package be.ipl.pae.biz.impl;

import be.ipl.pae.biz.dto.DocumentRetourDto;

public class DocumentRetourImpl implements DocumentRetourDto {

  private int idDocumentsRetour;
  private boolean attestationSejour;
  private boolean stageReleveNote;
  private boolean rapportFinal;
  private boolean preuvePassageTest;
  private boolean envoieDocument;
  private boolean paiementEffectue;
  private int version;

  public DocumentRetourImpl() {

  }

  /**
   * Constructeur.
   * 
   * @param idDocumentsRetour - id
   * @param attestationSejour - attestations de séjour
   * @param stageReleveNote - releve de note
   * @param rapportFinal - rapport final
   * @param preuvePassageTest - preuve
   * @param version - version
   */
  public DocumentRetourImpl(int idDocumentsRetour, boolean attestationSejour,
      boolean stageReleveNote, boolean rapportFinal, boolean preuvePassageTest,
      boolean envoieDocument, boolean paiementEffectue, int version) {
    super();
    this.idDocumentsRetour = idDocumentsRetour;
    this.attestationSejour = attestationSejour;
    this.stageReleveNote = stageReleveNote;
    this.rapportFinal = rapportFinal;
    this.preuvePassageTest = preuvePassageTest;
    this.envoieDocument = envoieDocument;
    this.paiementEffectue = paiementEffectue;
    this.version = version;
  }

  @Override
  public int getIdDocumentsRetour() {
    return idDocumentsRetour;
  }

  @Override
  public void setIdDocumentsRetour(int idDocumentsRetour) {
    this.idDocumentsRetour = idDocumentsRetour;
  }

  @Override
  public boolean isAttestationSejour() {
    return attestationSejour;
  }

  @Override
  public void setAttestationSejour(boolean attestationSejour) {
    this.attestationSejour = attestationSejour;
  }

  @Override
  public boolean isStageReleveNote() {
    return stageReleveNote;
  }

  @Override
  public void setStageReleveNote(boolean stageReleveNote) {
    this.stageReleveNote = stageReleveNote;
  }

  @Override
  public boolean isRapportFinal() {
    return rapportFinal;
  }

  @Override
  public void setRapportFinal(boolean rapportFinal) {
    this.rapportFinal = rapportFinal;
  }

  @Override
  public boolean isPreuvePassageTest() {
    return preuvePassageTest;
  }

  @Override
  public void setPreuvePassageTest(boolean preuvePassageTest) {
    this.preuvePassageTest = preuvePassageTest;
  }

  @Override
  public int getVersion() {
    return version;
  }

  @Override
  public void setVersion(int version) {
    this.version = version;
  }

  @Override
  public void setDocumentEnvoieDocument(boolean envoieDocument) {
    this.envoieDocument = envoieDocument;

  }

  @Override
  public boolean isEvnoieDocument() {
    return this.envoieDocument;
  }

  @Override
  public void setPaiementEffectue(boolean paiementEffectue) {
    this.paiementEffectue = paiementEffectue;

  }

  @Override
  public boolean isPaiementEffectue() {
    return this.paiementEffectue;
  }


}
