package be.ipl.pae.biz.impl;

import be.ipl.pae.biz.dto.DocumentDepartDto;

public class DocumentDepartImpl implements DocumentDepartDto {

  private int idDocumentsDepart;
  private boolean preuveTestLinguistique;
  private boolean contratBourse;
  private boolean conventionStageEtude;
  private boolean charteEtudiant;
  private boolean documentEngagement;
  private boolean envoieDocument;
  private boolean paiementEffectue;
  private int version;

  public DocumentDepartImpl() {

  }

  /**
   * Constructeur.
   * 
   * @param idDocumentsDepart - id
   * @param preuveTestLinguistique - preuve
   * @param contratBourse - contrat
   * @param conventionStageEtude - convention
   * @param charteEtudiant - charte
   * @param documentEngagement - document
   * @param version - version
   */
  public DocumentDepartImpl(int idDocumentsDepart, boolean preuveTestLinguistique,
      boolean contratBourse, boolean conventionStageEtude, boolean charteEtudiant,
      boolean documentEngagement, boolean envoieDocument, boolean paiementEffectue, int version) {
    super();
    this.idDocumentsDepart = idDocumentsDepart;
    this.preuveTestLinguistique = preuveTestLinguistique;
    this.contratBourse = contratBourse;
    this.conventionStageEtude = conventionStageEtude;
    this.charteEtudiant = charteEtudiant;
    this.documentEngagement = documentEngagement;
    this.envoieDocument = envoieDocument;
    this.paiementEffectue = paiementEffectue;
    this.version = version;
  }

  @Override
  public int getIdDocumentsDepart() {
    return idDocumentsDepart;
  }

  @Override
  public void setIdDocumentsDepart(int idDocumentsDepart) {
    this.idDocumentsDepart = idDocumentsDepart;
  }

  @Override
  public boolean isPreuveTestLinguistique() {
    return preuveTestLinguistique;
  }

  @Override
  public void setPreuveTestLinguistique(boolean preuveTestLinguistique) {
    this.preuveTestLinguistique = preuveTestLinguistique;
  }

  @Override
  public boolean isContratBourse() {
    return contratBourse;
  }

  @Override
  public void setContratBourse(boolean contratBourse) {
    this.contratBourse = contratBourse;
  }

  @Override
  public boolean isConventionStageEtude() {
    return conventionStageEtude;
  }

  @Override
  public void setConventionStageEtude(boolean conventionStageEtude) {
    this.conventionStageEtude = conventionStageEtude;
  }

  @Override
  public boolean isCharteEtudiant() {
    return charteEtudiant;
  }

  @Override
  public void setCharteEtudiant(boolean charteEtudiant) {
    this.charteEtudiant = charteEtudiant;
  }

  @Override
  public boolean isDocumentEngagement() {
    return documentEngagement;
  }

  @Override
  public void setDocumentEngagement(boolean documentEngagement) {
    this.documentEngagement = documentEngagement;
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
