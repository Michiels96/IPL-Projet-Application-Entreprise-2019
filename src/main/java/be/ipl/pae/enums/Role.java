package be.ipl.pae.enums;

public enum Role {
  A("Admin"), P("Professeur"), E("Etudiant");

  private String genre;

  Role(String gen) {
    genre = gen;
  }

  /**
   * Renvoie le rôle en toute lettre.
   * 
   * @return le rôle du user.
   */
  public String getRole() {
    return genre;
  }
}
