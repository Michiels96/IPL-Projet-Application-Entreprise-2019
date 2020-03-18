package be.ipl.pae.biz.dto;



public interface DocumentRetourDto {

  void setPaiementEffectue(boolean paiementEffectue);

  boolean isPaiementEffectue();

  void setDocumentEnvoieDocument(boolean envoieDocument);

  boolean isEvnoieDocument();

  int getIdDocumentsRetour();

  void setIdDocumentsRetour(int idDocumentsRetour);

  boolean isAttestationSejour();

  void setAttestationSejour(boolean attestationSejour);

  boolean isStageReleveNote();

  void setStageReleveNote(boolean stageReleveNote);

  boolean isRapportFinal();

  void setRapportFinal(boolean rapportFinal);

  boolean isPreuvePassageTest();

  void setPreuvePassageTest(boolean preuvePassageTest);

  int getVersion();

  void setVersion(int version);

}
