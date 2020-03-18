package be.ipl.pae.biz.impl;

import be.ipl.pae.biz.interfce.Pays;
import be.ipl.pae.enums.TypeProgramme;

class PaysImpl implements Pays {
  private int idPays;
  private String nom;
  private String codePays;
  private TypeProgramme programme;

  public PaysImpl() {}

  public PaysImpl(int idPays, String nom, String codePays, TypeProgramme programme) {
    super();
    this.idPays = idPays;
    this.nom = nom;
    this.codePays = codePays;
    this.programme = programme;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PaysDto#getIdPays()
   */
  @Override
  public int getIdPays() {
    return idPays;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PaysDto#setIdPays(int)
   */
  @Override
  public void setIdPays(int idPays) {
    this.idPays = idPays;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PaysDto#getNom()
   */
  @Override
  public String getNom() {
    return nom;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PaysDto#setNom(java.lang.String)
   */
  @Override
  public void setNom(String nom) {
    this.nom = nom;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PaysDto#getCodePays()
   */
  @Override
  public String getCodePays() {
    return codePays;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PaysDto#setCodePays(java.lang.String)
   */
  @Override
  public void setCodePays(String codePays) {
    this.codePays = codePays;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PaysDto#getTypeProgramme()
   */
  @Override
  public TypeProgramme getProgramme() {
    return programme;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.PaysDto#setTypeProgramme(be.ipl.pae.utils.TypeProgramme)
   */
  @Override
  public void setProgramme(TypeProgramme typeProgramme) {
    this.programme = typeProgramme;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + idPays;
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
    PaysImpl other = (PaysImpl) obj;
    if (idPays != other.idPays) {
      return false;
    }
    return true;
  }



}
