package be.ipl.pae.biz.impl;


import be.ipl.pae.biz.interfce.Mobilite;
import be.ipl.pae.enums.EtatMobilite;
import be.ipl.pae.enums.Periode;
import be.ipl.pae.enums.TypeProgramme;

import java.util.Date;

class MobiliteImpl implements Mobilite {
  private int idMobilite;
  private TypeProgramme programme;
  private double financement;
  private Periode periode;
  private int partenaire; // FK
  private int etudiant; // FK
  private int documentsDepart; // FK
  private int documentsRetour; // FK
  private int niveauPreference;
  private EtatMobilite etatMobilite;
  private boolean mobiliteAnnule;
  private int pays; // FK
  private Date dateIntroduction;
  private String typeMobilite; // censé être char(3) qu'est ce donc ?
  private int version;

  public MobiliteImpl() {

  }

  public MobiliteImpl(int idMobilite, TypeProgramme programme, double financement, Periode periode,
      int partenaire, int etudiant, int documentsDepart, int documentsRetour, int niveauPreference,
      EtatMobilite etatMobilite, boolean mobiliteAnnule, int pays, Date dateIntroduction,
      String typeMobilite) {
    super();
    this.idMobilite = idMobilite;
    this.programme = programme;
    this.financement = financement;
    this.periode = periode;
    this.partenaire = partenaire;
    this.etudiant = etudiant;
    this.documentsDepart = documentsDepart;
    this.documentsRetour = documentsRetour;
    this.niveauPreference = niveauPreference;
    this.etatMobilite = etatMobilite;
    this.mobiliteAnnule = mobiliteAnnule;
    this.pays = pays;
    this.dateIntroduction = dateIntroduction;
    this.typeMobilite = typeMobilite;
  }



  public int getIdMobilite() {
    return idMobilite;
  }



  public void setIdMobilite(int idMobilite) {
    this.idMobilite = idMobilite;
  }



  public TypeProgramme getProgramme() {
    return programme;
  }



  public void setProgramme(TypeProgramme programme) {
    this.programme = programme;
  }



  public double getFinancement() {
    return financement;
  }



  public void setFinancement(double financement) {
    this.financement = financement;
  }



  public Periode getPeriode() {
    return periode;
  }



  public void setPeriode(Periode periode) {
    this.periode = periode;
  }



  public int getPartenaire() {
    return partenaire;
  }



  public void setPartenaire(int partenaire) {
    this.partenaire = partenaire;
  }



  public int getEtudiant() {
    return etudiant;
  }



  public void setEtudiant(int etudiant) {
    this.etudiant = etudiant;
  }



  public int getDocumentsDepart() {
    return documentsDepart;
  }



  public void setDocumentsDepart(int documentsDepart) {
    this.documentsDepart = documentsDepart;
  }



  public int getDocumentsRetour() {
    return documentsRetour;
  }



  public void setDocumentsRetour(int documentsRetour) {
    this.documentsRetour = documentsRetour;
  }



  public int getNiveauPreference() {
    return niveauPreference;
  }



  public void setNiveauPreference(int niveauPreference) {
    this.niveauPreference = niveauPreference;
  }



  public EtatMobilite getEtatMobilite() {
    return etatMobilite;
  }



  public void setEtatMobilite(EtatMobilite etatMobilite) {
    this.etatMobilite = etatMobilite;
  }



  public boolean isMobiliteAnnule() {
    return mobiliteAnnule;
  }



  public void setMobiliteAnnule(boolean mobiliteAnnule) {
    this.mobiliteAnnule = mobiliteAnnule;
  }



  public int getPays() {
    return pays;
  }



  public void setPays(int pays) {
    this.pays = pays;
  }


  public Date getDateIntroduction() {
    return dateIntroduction;
  }


  public void setDateIntroduction(Date dateIntroduction) {
    this.dateIntroduction = dateIntroduction;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.MobiliteDto#getTypeMobilie()
   */
  @Override
  public String getTypeMobilite() {
    return typeMobilite;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.MobiliteDto#setTypeMobilie(java.lang.String)
   */
  @Override
  public void setTypeMobilite(String typeMobilite) {
    this.typeMobilite = typeMobilite;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + idMobilite;
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
    MobiliteImpl other = (MobiliteImpl) obj;
    if (idMobilite != other.idMobilite) {
      return false;
    }
    return true;
  }

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }

}
