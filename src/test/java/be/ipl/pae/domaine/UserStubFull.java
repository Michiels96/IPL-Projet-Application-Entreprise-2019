package be.ipl.pae.domaine;

import be.ipl.pae.biz.interfce.User;
import be.ipl.pae.enums.Role;

import java.util.Date;

public class UserStubFull implements User {

  private int id;
  private String pseudo;
  private String motDePasse;
  private String nom;
  private String prenom;
  private String mail;
  private String adresse;
  private String commune;
  private String numCompte;
  private String titulaireCompte;
  private String nomBanque;
  private Role role;
  private boolean isHomme;
  private Date dateNaissance;
  private int codePostal;
  private String telephone;
  private int nombreAnneeReussies;
  private String codeBic;
  private boolean checkpwd;



  /**
   * Constructeur de userStubFull.
   * 
   * @param pseudo - pseudo unique
   * @param motDePasse - mot de passe
   * @param nom - nom
   * @param prenom - prenom
   * @param mail - mail
   * @param adresse - adresse
   * @param commune - commune
   * @param numCompte - numéro de compte
   * @param titulaireCompte - titulaire de compte
   * @param nomBanque - nom de banque
   * @param role - role (P,A,E)
   * @param isHomme - homme ou femme
   * @param dateNaissance - date de naissance
   * @param codePostal - code postal
   * @param telephone - numéro de téléphone
   * @param nombreAnneeReussies - nombre d'années réussies
   * @param codeBic - code bic
   * @param checkpwd - true ou false selon le comportement souhaité
   */
  public UserStubFull(String pseudo, String motDePasse, String nom, String prenom, String mail,
      String adresse, String commune, String numCompte, String titulaireCompte, String nomBanque,
      Role role, boolean isHomme, Date dateNaissance, int codePostal, String telephone,
      int nombreAnneeReussies, String codeBic, boolean checkpwd) {
    super();
    this.pseudo = pseudo;
    this.motDePasse = motDePasse;
    this.nom = nom;
    this.prenom = prenom;
    this.mail = mail;
    this.adresse = adresse;
    this.commune = commune;
    this.numCompte = numCompte;
    this.titulaireCompte = titulaireCompte;
    this.nomBanque = nomBanque;
    this.role = role;
    this.isHomme = isHomme;
    this.dateNaissance = dateNaissance;
    this.codePostal = codePostal;
    this.telephone = telephone;
    this.nombreAnneeReussies = nombreAnneeReussies;
    this.codeBic = codeBic;
    this.checkpwd = checkpwd;
  }

  @Override
  public void cryptPassword() {
    return;
  }

  @Override
  public boolean checkPassword(String password) {
    return checkpwd;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((pseudo == null) ? 0 : pseudo.hashCode());
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
    UserStubFull other = (UserStubFull) obj;
    if (pseudo == null) {
      if (other.pseudo != null) {
        return false;
      }
    } else if (!pseudo.equals(other.pseudo)) {
      return false;
    }
    return true;
  }



  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.UserDTO#getPseudo()
   */
  @Override
  public String getPseudo() {
    return pseudo;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.UserDTO#setPseudo(java.lang.String)
   */
  @Override
  public void setPseudo(String pseudo) {
    this.pseudo = pseudo;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.UserDTO#getMotDePasse()
   */
  @Override
  public String getMotDePasse() {
    return motDePasse;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.UserDTO#setMotDePasse(java.lang.String)
   */
  @Override
  public void setMotDePasse(String motDePasse) {
    this.motDePasse = motDePasse;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.UserDTO#getNom()
   */
  @Override
  public String getNom() {
    return nom;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.UserDTO#setNom(java.lang.String)
   */
  @Override
  public void setNom(String nom) {
    this.nom = nom;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.UserDTO#getPrenom()
   */
  @Override
  public String getPrenom() {
    return prenom;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.UserDTO#setPrenom(java.lang.String)
   */
  @Override
  public void setPrenom(String prenom) {
    this.prenom = prenom;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.UserDTO#getMail()
   */
  @Override
  public String getEmail() {
    return mail;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.UserDTO#setMail(java.lang.String)
   */
  @Override
  public void setEmail(String mail) {
    this.mail = mail;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.UserDTO#getAdresse()
   */
  @Override
  public String getAdresse() {
    return adresse;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.UserDTO#setAdresse(java.lang.String)
   */
  @Override
  public void setAdresse(String adresse) {
    this.adresse = adresse;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.UserDTO#getCommune()
   */
  @Override
  public String getCommune() {
    return commune;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.UserDTO#setCommune(java.lang.String)
   */
  @Override
  public void setCommune(String commune) {
    this.commune = commune;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.UserDTO#getNumCompte()
   */
  @Override
  public String getNumCompte() {
    return numCompte;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.UserDTO#setNumCompte(java.lang.String)
   */
  @Override
  public void setNumCompte(String numCompte) {
    this.numCompte = numCompte;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.UserDTO#getTitulaireCompte()
   */
  @Override
  public String getTitulaireCompte() {
    return titulaireCompte;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.UserDTO#setTitulaireCompte(java.lang.String)
   */
  @Override
  public void setTitulaireCompte(String titulaireCompte) {
    this.titulaireCompte = titulaireCompte;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.UserDTO#getNomBanque()
   */
  @Override
  public String getNomBanque() {
    return nomBanque;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.UserDTO#setNomBanque(java.lang.String)
   */
  @Override
  public void setNomBanque(String nomBanque) {
    this.nomBanque = nomBanque;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.UserDTO#isRole()
   */
  @Override
  public Role getRole() {
    return role;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.UserDTO#setRole(boolean)
   */
  @Override
  public void setRole(Role role) {
    this.role = role;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.UserDTO#isHomme()
   */
  @Override
  public boolean isHomme() {
    return isHomme;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.UserDTO#setHomme(boolean)
   */
  @Override
  public void setHomme(boolean isHomme) {
    this.isHomme = isHomme;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.UserDTO#getDateNaissance()
   */
  @Override
  public Date getDateNaissance() {
    return dateNaissance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.UserDTO#setDateNaissance(java.util.Date)
   */
  @Override
  public void setDateNaissance(Date dateNaissance) {
    this.dateNaissance = dateNaissance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.UserDTO#getCodePostal()
   */
  @Override
  public int getCodePostal() {
    return codePostal;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.UserDTO#setCodePostal(int)
   */
  @Override
  public void setCodePostal(int codePostal) {
    this.codePostal = codePostal;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.UserDTO#getTelephone()
   */
  @Override
  public String getNumTel() {
    return telephone;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.UserDTO#setTelephone(int)
   */
  @Override
  public void setNumTel(String telephone) {
    this.telephone = telephone;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.UserDTO#getNombreAnneeReussies()
   */
  @Override
  public int getNombreAnneeReussie() {
    return nombreAnneeReussies;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.UserDTO#setNombreAnneeReussies(int)
   */
  @Override
  public void setNombreAnneeReussie(int nombreAnneeReussies) {
    this.nombreAnneeReussies = nombreAnneeReussies;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.UserDTO#getCodeBic()
   */
  @Override
  public String getCodeBic() {
    return codeBic;
  }

  /*
   * (non-Javadoc)
   * 
   * @see be.ipl.pae.biz.impl.UserDTO#setCodeBic(int)
   */
  @Override
  public void setCodeBic(String codeBic) {
    this.codeBic = codeBic;
  }

  @Override
  public int getNationalite() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void setNationalite(int pays) {
    // TODO Auto-generated method stub
  }

  @Override
  public int getIdUtilisateur() {
    // TODO Auto-generated method stub
    return id;
  }

  @Override
  public void setIdUtilisateur(int id) {
    this.id = id;

  }

  @Override
  public int getVersion() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void setVersion(int version) {
    // TODO Auto-generated method stub
  }

  @Override
  public Date getDateInscription() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setDateInscription(Date dateInscription) {
    // TODO Auto-generated method stub
  }
}
