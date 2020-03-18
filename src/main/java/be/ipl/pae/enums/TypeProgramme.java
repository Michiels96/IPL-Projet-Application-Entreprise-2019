package be.ipl.pae.enums;

public enum TypeProgramme {
  ERASMUS("erasmus+"), ERABEL("erabel"), FAME("fame"), AUTRE("autre");

  private String genre;

  TypeProgramme(String gen) {
    genre = gen;
  }

  public String getTypeProgramme() {
    return genre;
  }
}
