package be.ipl.pae.biz.impl;

import be.ipl.pae.biz.interfce.Partenaire;

class PartenaireImpl implements Partenaire {
  private int idPartenaire;
  private String nomLegal;
  private String nomAffaire;
  private String nomComplet;
  private String departement;
  private String typeOrganisation;
  private int nombreEmploye;
  private String adresse;
  private int pays; // FK
  private String region;
  private String codePostal;
  private String ville;
  private String email;
  private String siteWeb;
  private String numTel;
  private String typeMobilite; // censé être char(3) qu'est ce donc ?
  private int version;

  public PartenaireImpl() {

  }

  public PartenaireImpl(int idPartenaire, String nomLegal, String nomAffaire, String nomComplet,
      String departement, String typeOrganisation, int nombreEmploye, String adresse, int pays,
      String region, String codePostal, String ville, String email, String siteWeb, String numTel,
      String typeMobilite) {
    super();
    this.idPartenaire = idPartenaire;
    this.nomLegal = nomLegal;
    this.nomAffaire = nomAffaire;
    this.nomComplet = nomComplet;
    this.departement = departement;
    this.typeOrganisation = typeOrganisation;
    this.nombreEmploye = nombreEmploye;
    this.adresse = adresse;
    this.pays = pays;
    this.region = region;
    this.codePostal = codePostal;
    this.ville = ville;
    this.email = email;
    this.siteWeb = siteWeb;
    this.numTel = numTel;
    this.typeMobilite = typeMobilite;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PartenaireDto#getIdPartenaire()
   */
  @Override
  public int getIdPartenaire() {
    return idPartenaire;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PartenaireDto#setIdPartenaire(int)
   */
  @Override
  public void setIdPartenaire(int idPartenaire) {
    this.idPartenaire = idPartenaire;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PartenaireDto#getNomLegal()
   */
  @Override
  public String getNomLegal() {
    return nomLegal;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PartenaireDto#setNomLegal(java.lang.String)
   */
  @Override
  public void setNomLegal(String nomLegal) {
    this.nomLegal = nomLegal;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PartenaireDto#getNomAffaire()
   */
  @Override
  public String getNomAffaire() {
    return nomAffaire;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PartenaireDto#setNomAffaire(java.lang.String)
   */
  @Override
  public void setNomAffaire(String nomAffaire) {
    this.nomAffaire = nomAffaire;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PartenaireDto#getNomComplet()
   */
  @Override
  public String getNomComplet() {
    return nomComplet;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PartenaireDto#setNomComplet(java.lang.String)
   */
  @Override
  public void setNomComplet(String nomComplet) {
    this.nomComplet = nomComplet;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PartenaireDto#getDepartement()
   */
  @Override
  public String getDepartement() {
    return departement;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PartenaireDto#setDepartement(java.lang.String)
   */
  @Override
  public void setDepartement(String departement) {
    this.departement = departement;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PartenaireDto#getTypeOrganisation()
   */
  @Override
  public String getTypeOrganisation() {
    return typeOrganisation;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PartenaireDto#setTypeOrganisation(java.lang.String)
   */
  @Override
  public void setTypeOrganisation(String typeOrganisation) {
    this.typeOrganisation = typeOrganisation;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PartenaireDto#getNombreEmploye()
   */
  @Override
  public int getNombreEmploye() {
    return nombreEmploye;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PartenaireDto#setNombreEmploye(int)
   */
  @Override
  public void setNombreEmploye(int nombreEmploye) {
    this.nombreEmploye = nombreEmploye;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PartenaireDto#getAdresse()
   */
  @Override
  public String getAdresse() {
    return adresse;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PartenaireDto#setAdresse(java.lang.String)
   */
  @Override
  public void setAdresse(String adresse) {
    this.adresse = adresse;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PartenaireDto#getPays()
   */
  @Override
  public int getPays() {
    return pays;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PartenaireDto#setPays(int)
   */
  @Override
  public void setPays(int pays) {
    this.pays = pays;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PartenaireDto#getCodePostal()
   */
  @Override
  public String getCodePostal() {
    return codePostal;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PartenaireDto#setCodePostal(java.lang.String)
   */
  @Override
  public void setCodePostal(String codePostal) {
    this.codePostal = codePostal;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PartenaireDto#getVille()
   */
  @Override
  public String getVille() {
    return ville;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PartenaireDto#setVille(java.lang.String)
   */
  @Override
  public void setVille(String ville) {
    this.ville = ville;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PartenaireDto#getEmail()
   */
  @Override
  public String getEmail() {
    return email;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PartenaireDto#setEmail(java.lang.String)
   */
  @Override
  public void setEmail(String email) {
    this.email = email;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PartenaireDto#getSiteWeb()
   */
  @Override
  public String getSiteWeb() {
    return siteWeb;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PartenaireDto#setSiteWeb(java.lang.String)
   */
  @Override
  public void setSiteWeb(String siteWeb) {
    this.siteWeb = siteWeb;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PartenaireDto#getNumTel()
   */
  @Override
  public String getNumTel() {
    return numTel;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PartenaireDto#setNumTel(java.lang.String)
   */
  @Override
  public void setNumTel(String numTel) {
    this.numTel = numTel;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PartenaireDto#getTypeMobilie()
   */
  @Override
  public String getTypeMobilite() {
    return typeMobilite;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PartenaireDto#setTypeMobilie(java.lang.String)
   */
  @Override
  public void setTypeMobilite(String typeMobilite) {
    this.typeMobilite = typeMobilite;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + idPartenaire;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    PartenaireImpl other = (PartenaireImpl) obj;
    if (idPartenaire != other.idPartenaire) {
      return false;
    }
    return true;
  }

  @Override
  public int getVersion() {
    return version;
  }

  @Override
  public void setVersion(int version) {
    this.version = version;
  }



}
