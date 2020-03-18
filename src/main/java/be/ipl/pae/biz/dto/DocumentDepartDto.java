package be.ipl.pae.biz.dto;



public interface DocumentDepartDto {

  void setPaiementEffectue(boolean paiementEffectue);

  boolean isPaiementEffectue();

  void setDocumentEnvoieDocument(boolean envoieDocument);

  boolean isEvnoieDocument();

  int getIdDocumentsDepart();

  void setIdDocumentsDepart(int idDocumentsDepart);

  boolean isPreuveTestLinguistique();

  void setPreuveTestLinguistique(boolean preuveTestLinguistique);

  boolean isContratBourse();

  void setContratBourse(boolean contratBourse);

  boolean isConventionStageEtude();

  void setConventionStageEtude(boolean conventionStageEtude);

  boolean isCharteEtudiant();

  void setCharteEtudiant(boolean charteEtudiant);

  boolean isDocumentEngagement();

  void setDocumentEngagement(boolean documentEngagement);

  int getVersion();

  void setVersion(int version);

}
