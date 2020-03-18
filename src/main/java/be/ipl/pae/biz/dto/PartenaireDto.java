package be.ipl.pae.biz.dto;



public interface PartenaireDto {

  int getIdPartenaire();

  void setIdPartenaire(int idPartenaire);

  String getNomLegal();

  void setNomLegal(String nomLegal);

  String getNomAffaire();

  void setNomAffaire(String nomAffaire);

  String getNomComplet();

  void setNomComplet(String nomComplet);

  String getDepartement();

  void setDepartement(String departement);

  String getTypeOrganisation();

  void setTypeOrganisation(String typeOrganisation);

  int getNombreEmploye();

  void setNombreEmploye(int nombreEmploye);

  String getAdresse();

  void setAdresse(String adresse);

  int getPays();

  void setPays(int pays);

  String getRegion();

  void setRegion(String region);

  String getCodePostal();

  void setCodePostal(String codePostal);

  String getVille();

  void setVille(String ville);

  String getEmail();

  void setEmail(String email);

  String getSiteWeb();

  void setSiteWeb(String siteWeb);

  String getNumTel();

  void setNumTel(String numTel);

  String getTypeMobilite();

  void setTypeMobilite(String typeMobilite);

  int getVersion();

  void setVersion(int version);

}
