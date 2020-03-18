package be.ipl.pae.biz.dto;

import be.ipl.pae.enums.Role;

import java.util.Date;


public interface UserDto {

  int getIdUtilisateur();

  void setIdUtilisateur(int idUtilisateur);

  String getPseudo();

  void setPseudo(String pseudo);

  String getMotDePasse();

  void setMotDePasse(String motDePasse);

  String getNom();

  void setNom(String nom);

  String getPrenom();

  void setPrenom(String prenom);

  String getEmail();

  void setEmail(String email);

  String getAdresse();

  void setAdresse(String adresse);

  String getCommune();

  void setCommune(String commune);

  String getNumCompte();

  void setNumCompte(String numCompte);

  String getTitulaireCompte();

  void setTitulaireCompte(String titulaireCompte);

  String getNomBanque();

  void setNomBanque(String nomBanque);

  Role getRole();

  void setRole(Role role);

  boolean isHomme();

  void setHomme(boolean homme);

  Date getDateNaissance();

  void setDateNaissance(Date dateNaissance);

  Date getDateInscription();

  void setDateInscription(Date dateInscription);

  int getCodePostal();

  void setCodePostal(int codePostal);

  String getNumTel();

  void setNumTel(String numTel);

  int getNombreAnneeReussie();

  void setNombreAnneeReussie(int nombreAnneeReussie);

  String getCodeBic();

  void setCodeBic(String codeBic);

  int getNationalite();

  void setNationalite(int nationalite);

  int getVersion();

  void setVersion(int version);

}
