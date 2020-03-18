package be.ipl.pae.enums;

public enum Periode {
  Q1("Q1"), Q2("Q2");

  private String genre;

  Periode(String gen) {
    genre = gen;
  }

  public String getPeriode() {
    return genre;
  }
}
