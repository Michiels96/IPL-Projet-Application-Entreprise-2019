package be.ipl.pae.biz.impl;

import be.ipl.pae.biz.interfce.User;
import be.ipl.pae.enums.Role;

import org.mindrot.bcrypt.BCrypt;

import java.util.Date;

class UserImpl implements User {

  private static String sel = BCrypt.gensalt();

  private int idUtilisateur;
  private String pseudo;
  private String motDePasse;
  private String nom;
  private String prenom;
  private String email;
  private String adresse;
  private String commune;
  private String numCompte;
  private String titulaireCompte;
  private String nomBanque;
  private Role role;
  private boolean estHomme;
  private Date dateNaissance;
  private Date dateInscription;
  private int codePostal;
  private String numTel;
  private int nombreAnneeReussie;
  private String codeBic;
  private int nationalite;
  private int version;

  public UserImpl() {

  }

  public UserImpl(int idUtilisateur, String pseudo, String motDePasse, String nom, String prenom,
      String email, String adresse, String commune, String numCompte, String titulaireCompte,
      String nomBanque, Role role, boolean estHomme, Date dateNaissance, Date dateInscription,
      int codePostal, String numTel, int nombreAnneeReussie, String codeBic, int nationalite) {
    super();
    this.idUtilisateur = idUtilisateur;
    this.pseudo = pseudo;
    this.motDePasse = motDePasse;
    this.nom = nom;
    this.prenom = prenom;
    this.email = email;
    this.adresse = adresse;
    this.commune = commune;
    this.numCompte = numCompte;
    this.titulaireCompte = titulaireCompte;
    this.nomBanque = nomBanque;
    this.role = role;
    this.estHomme = estHomme;
    this.dateNaissance = dateNaissance;
    this.dateInscription = dateInscription;
    this.codePostal = codePostal;
    this.numTel = numTel;
    this.nombreAnneeReussie = nombreAnneeReussie;
    this.codeBic = codeBic;
    this.nationalite = nationalite;
  }

  /**
   * Permet de crypter le mot de passe.
   */
  @Override
  public void cryptPassword() {
    this.setMotDePasse(BCrypt.hashpw(this.getMotDePasse(), sel));
  }

  /**
   * Vérifie si le mot de passe passé en paramètre est le même que celui du dto (this).
   * 
   * @param password - mot de passe entré par l'utilisateur.
   * @return true si les mots de passe correspondent, false sinon.
   */
  @Override
  public boolean checkPassword(String password) {
    return BCrypt.checkpw(password, this.motDePasse);
  }

  /**
   * Renvoie un hash du dto basé sur son pseudo.
   * 
   * @return la valeur du hash.
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((pseudo == null) ? 0 : pseudo.hashCode());
    return result;
  }


  /**
   * Vérifie l'égalité structurelle de deux dto.
   * 
   * @param obj - dto que l'on veut comparer.
   * @return true si l'égalité est vérifiée, false sinon.
   */
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
    UserImpl other = (UserImpl) obj;
    if (pseudo == null) {
      if (other.pseudo != null) {
        return false;
      }
    } else if (!pseudo.equals(other.pseudo)) {
      return false;
    }
    return true;
  }


  public int getIdUtilisateur() {
    return idUtilisateur;
  }

  public void setIdUtilisateur(int idUtilisateur) {
    this.idUtilisateur = idUtilisateur;
  }

  /**
   * Renvoie le pseudo du dto.
   * 
   * @return le pseudo du dto.
   */
  public String getPseudo() {
    return pseudo;
  }

  /**
   * Modifie le pseudo du dto.
   * 
   * @param pseudo - le nouveau pseudo du dto.
   */
  public void setPseudo(String pseudo) {
    this.pseudo = pseudo;
  }

  /**
   * Renvoie le mot de passe du dto.
   * 
   * @return le mot de passe du dto.
   */
  public String getMotDePasse() {
    return motDePasse;
  }

  /**
   * Modifie le mot de passe du dto.
   * 
   * @param mot de passe - le nouveau mot de passe du dto.
   */
  public void setMotDePasse(String motDePasse) {
    this.motDePasse = motDePasse;
  }

  /**
   * Renvoie le nom du dto.
   * 
   * @return le nom du dto.
   */
  public String getNom() {
    return nom;
  }

  /**
   * Modifie le nom du dto.
   * 
   * @param nom - le nouveau nom du dto.
   */
  public void setNom(String nom) {
    this.nom = nom;
  }

  /**
   * Renvoie le prenom du dto.
   * 
   * @return le prenom du dto.
   */
  public String getPrenom() {
    return prenom;
  }

  /**
   * Modifie le prenom du dto.
   * 
   * @param prenom - le nouveau prenom du dto.
   */
  public void setPrenom(String prenom) {
    this.prenom = prenom;
  }

  /**
   * Renvoie le mail du dto.
   * 
   * @return le mail du dto.
   */
  public String getEmail() {
    return email;
  }

  /**
   * Modifie le mail du dto.
   * 
   * @param mail - le nouveau mail du dto.
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Renvoie l'adresse du dto.
   * 
   * @return l'adresse du dto.
   */
  public String getAdresse() {
    return adresse;
  }

  /**
   * Modifie l'adresse du dto.
   * 
   * @param adresse - la nouvelle adresse du dto.
   */
  public void setAdresse(String adresse) {
    this.adresse = adresse;
  }

  /**
   * Renvoie la commune du dto.
   * 
   * @return la commune du dto.
   */
  public String getCommune() {
    return commune;
  }

  /**
   * Modifie le commune du dto.
   * 
   * @param commune - la nouvelle commune du dto.
   */
  public void setCommune(String commune) {
    this.commune = commune;
  }

  /**
   * Renvoie le numéro de compte du dto.
   * 
   * @return le numéro de compte du dto.
   */
  public String getNumCompte() {
    return numCompte;
  }

  /**
   * Modifie le numéro de compte du dto.
   * 
   * @param numCompte - le nouveau numéro de compte du dto.
   */
  public void setNumCompte(String numCompte) {
    this.numCompte = numCompte;
  }

  /**
   * Renvoie le titulaire du compte du dto.
   * 
   * @return le titulaire du compte du dto.
   */
  public String getTitulaireCompte() {
    return titulaireCompte;
  }

  /**
   * Modifie le titulaire de compte du dto.
   * 
   * @param titulaireCompte - le nouveau titulaire de compte du dto.
   */
  public void setTitulaireCompte(String titulaireCompte) {
    this.titulaireCompte = titulaireCompte;
  }

  /**
   * Renvoie le nom de la banque du dto.
   * 
   * @return le nom de la banque du dto.
   */
  public String getNomBanque() {
    return nomBanque;
  }

  /**
   * Modifie le nom de la banque du dto.
   * 
   * @param nomBanque - le nouveau nom de la banque du dto.
   */
  public void setNomBanque(String nomBanque) {
    this.nomBanque = nomBanque;
  }

  /**
   * Renvoie le role du dto.
   * 
   * @return le role du dto.
   */
  public Role getRole() {
    return role;
  }

  /**
   * Modifie le role du dto.
   * 
   * @param role - le nouveau role du dto.
   */
  public void setRole(Role role) {
    this.role = role;
  }

  /**
   * Renvoie le sexe du dto.
   * 
   * @return le sexe du dto.
   */
  public boolean isHomme() {
    return estHomme;
  }

  /**
   * Modifie le sexe du dto.
   * 
   * @param isHomme - le nouveau sexe du dto.
   */
  public void setHomme(boolean estHomme) {
    this.estHomme = estHomme;
  }

  /**
   * Renvoie la date de naissance du dto.
   * 
   * @return la date de naissance du dto.
   */
  public Date getDateNaissance() {
    return dateNaissance;
  }

  /**
   * Modifie la date de naissance du dto.
   * 
   * @param dateNaissance - la nouvelle date de naissance du dto.
   */
  public void setDateNaissance(Date dateNaissance) {
    this.dateNaissance = dateNaissance;
  }

  /**
   * Renvoie la date d'inscription du dto.
   * 
   * @return la date d'inscription du dto.
   */
  public Date getDateInscription() {
    return dateInscription;
  }

  /**
   * Modifie la date d'inscription du dto.
   * 
   * @param dateInscription - la nouvelle date d'inscription du dto.
   */
  public void setDateInscription(Date dateInscription) {
    this.dateInscription = dateInscription;
  }

  /**
   * Renvoie le code postal du dto.
   * 
   * @return le code postal du dto.
   */
  public int getCodePostal() {
    return codePostal;
  }

  /**
   * Modifie le code postal du dto.
   * 
   * @param codePostal - le nouveau code postal du dto.
   */
  public void setCodePostal(int codePostal) {
    this.codePostal = codePostal;
  }

  /**
   * Renvoie le numéro de téléphone du dto.
   * 
   * @return le numéro de téléphone du dto.
   */
  public String getNumTel() {
    return numTel;
  }

  /**
   * Modifie le numéro de téléphone du dto.
   * 
   * @param telephone - le nouveau numéro de téléphone du dto.
   */
  public void setNumTel(String numTel) {
    this.numTel = numTel;
  }

  /**
   * Renvoie le nombre d'années réussies du dto.
   * 
   * @return le nombre d'années réussies du dto.
   */
  public int getNombreAnneeReussie() {
    return nombreAnneeReussie;
  }

  /**
   * Modifie le nombre d'années réussies du dto.
   * 
   * @param nombreAnneeReussies - le nouveau nombre d'années réussies du dto.
   */
  public void setNombreAnneeReussie(int nombreAnneeReussie) {
    this.nombreAnneeReussie = nombreAnneeReussie;
  }

  /**
   * Renvoie le code bic du dto.
   * 
   * @return le code bic du dto.
   */
  public String getCodeBic() {
    return codeBic;
  }

  /**
   * Modifie le code bic du dto.
   * 
   * @param codeBic - le nouveau code bic du dto.
   */
  public void setCodeBic(String codeBic) {
    this.codeBic = codeBic;
  }

  /**
   * Renvoie la nationalite du dto.
   * 
   * @return la nationalite du dto.
   */
  public int getNationalite() {
    return nationalite;
  }

  /**
   * Modifie la nationalite du dto.
   * 
   * @param nationalite - la nouvelle nationalite du dto.
   */
  public void setNationalite(int nationalite) {
    this.nationalite = nationalite;
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
