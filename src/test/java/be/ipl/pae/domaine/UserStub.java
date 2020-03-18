package be.ipl.pae.domaine;

import be.ipl.pae.biz.interfce.User;
import be.ipl.pae.enums.Role;

import java.util.Date;

public class UserStub implements User {

  private String pseudo;
  private String motDePasse;
  private String nom;
  private String prenom;
  private String mail;
  private Role role;

  /**
   * Constructeur du UserStub.
   * 
   * @param pseudo - String correspondant au pseudo
   * @param motDePasse - String correspondant au mot de passe
   * @param nom - String correspondant au nom
   * @param prenom - String correspondant au prenom
   * @param mail - String correspondant au mail
   * @param role - boolean correspondant au role
   */
  public UserStub(String pseudo, String motDePasse, String nom, String prenom, String mail,
      Role role) {
    super();
    this.pseudo = pseudo;
    this.motDePasse = motDePasse;
    this.nom = nom;
    this.prenom = prenom;
    this.mail = mail;
    this.role = role;
  }

  @Override
  public String getPseudo() {
    return pseudo;
  }

  @Override
  public void setPseudo(String pseudo) {
    this.pseudo = pseudo;
  }

  @Override
  public String getMotDePasse() {
    return motDePasse;
  }

  @Override
  public void setMotDePasse(String motDePasse) {
    this.motDePasse = motDePasse;
  }

  @Override
  public String getNom() {
    return nom;
  }

  @Override
  public void setNom(String nom) {
    this.nom = nom;
  }

  @Override
  public String getPrenom() {
    return prenom;
  }

  @Override
  public void setPrenom(String prenom) {
    this.prenom = prenom;
  }

  @Override
  public String getEmail() {
    return mail;
  }

  @Override
  public void setEmail(String mail) {
    this.mail = mail;
  }

  @Override
  public String getAdresse() {
    return null;
  }

  @Override
  public void setAdresse(String adresse) {

  }

  @Override
  public String getCommune() {
    return null;
  }

  @Override
  public void setCommune(String commune) {

  }

  @Override
  public String getNumCompte() {
    return null;
  }

  @Override
  public void setNumCompte(String numCompte) {

  }

  @Override
  public String getTitulaireCompte() {
    return null;
  }

  @Override
  public void setTitulaireCompte(String titulaireCompte) {

  }

  @Override
  public String getNomBanque() {
    return null;
  }

  @Override
  public void setNomBanque(String nomBanque) {

  }

  @Override
  public Role getRole() {
    return role;
  }

  @Override
  public void setRole(Role role) {
    this.role = role;
  }

  @Override
  public boolean isHomme() {
    return false;
  }

  @Override
  public void setHomme(boolean homme) {

  }

  @Override
  public Date getDateNaissance() {
    return null;
  }

  @Override
  public void setDateNaissance(Date dateNaissance) {

  }

  @Override
  public int getCodePostal() {
    return 0;
  }

  @Override
  public void setCodePostal(int codePostal) {

  }

  @Override
  public String getNumTel() {
    return "0";
  }

  @Override
  public void setNumTel(String telephone) {

  }

  @Override
  public int getNombreAnneeReussie() {
    return 0;
  }

  @Override
  public void setNombreAnneeReussie(int nombreAnneeReussies) {

  }

  @Override
  public String getCodeBic() {
    return "0";
  }

  @Override
  public void setCodeBic(String codeBic) {

  }

  @Override
  public boolean checkPassword(String password) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void cryptPassword() {
    // TODO Auto-generated method stub

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
    return 0;
  }

  @Override
  public void setIdUtilisateur(int id) {

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
