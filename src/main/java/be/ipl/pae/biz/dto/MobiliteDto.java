package be.ipl.pae.biz.dto;

import be.ipl.pae.enums.EtatMobilite;
import be.ipl.pae.enums.Periode;
import be.ipl.pae.enums.TypeProgramme;

import java.util.Date;


public interface MobiliteDto {

  int getIdMobilite();

  void setIdMobilite(int idMobilite);

  TypeProgramme getProgramme();

  void setProgramme(TypeProgramme programme);

  double getFinancement();

  void setFinancement(double financement);

  Periode getPeriode();

  void setPeriode(Periode periode);

  int getPartenaire();

  void setPartenaire(int partenaire);

  int getEtudiant();

  void setEtudiant(int etudiant);

  int getDocumentsDepart();

  void setDocumentsDepart(int documentsDepart);

  int getDocumentsRetour();

  void setDocumentsRetour(int documentsRetour);

  int getNiveauPreference();

  void setNiveauPreference(int niveauPreference);

  EtatMobilite getEtatMobilite();

  void setEtatMobilite(EtatMobilite etatMobilite);

  boolean isMobiliteAnnule();

  void setMobiliteAnnule(boolean mobiliteAnnule);

  int getPays();

  void setPays(int pays);

  Date getDateIntroduction();

  void setDateIntroduction(Date dateIntroduction);

  String getTypeMobilite();

  void setTypeMobilite(String typeMobilite);

  int getVersion();

  void setVersion(int version);

}
