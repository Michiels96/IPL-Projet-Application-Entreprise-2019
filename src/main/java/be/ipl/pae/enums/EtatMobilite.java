package be.ipl.pae.enums;

public enum EtatMobilite {
  CREE("CREE"), EN_PREPARATION("EN_PREPARATION"), A_PAYER("A_PAYER"), A_PAYER_SOLDE(
      "A_PAYER_SOLDE");

  private String genre;

  EtatMobilite(String gen) {
    genre = gen;
  }

  public String getEtatMobilite() {
    return genre;
  }
}
