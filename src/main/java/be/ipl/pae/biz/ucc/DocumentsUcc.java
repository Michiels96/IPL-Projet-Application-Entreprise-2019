package be.ipl.pae.biz.ucc;

import be.ipl.pae.biz.dto.DocumentDepartDto;
import be.ipl.pae.biz.dto.DocumentRetourDto;

public interface DocumentsUcc {

  /**
   * Liste les documents de retour d'une mobilité.
   * 
   * @param id - id mobilité
   * @return dto des documents de retour
   */
  DocumentDepartDto listerDocumentsDepart(int id);

  /**
   * Liste les documents de départ d'une mobilité.
   * 
   * @param id - id mobilité
   * @return dto des documents de départ
   */
  DocumentRetourDto listerDocumentsRetour(int id);

  /**
   * Obtient les infos d'un document par rapport à son id.
   * 
   * @param idDoc - id du document.
   * @return un dto avec les infos.
   */
  DocumentDepartDto getDocumentDepart(int idDoc);

  /**
   * Obient les infos d'un document par rapport à sond id.
   * 
   * @param idDoc - id du document.
   * @return un dto avec les infos du document.
   */
  DocumentRetourDto getDocumentRetour(int idDoc);

  /**
   * Met à jour le document de départ fournit en paramètre.
   * 
   * @param doc - dto contenant les informations du document.
   * @param etatMobilite - etat de la mobilite.
   * @param versionMob - version de la mobilite.
   * @param idMobilite - id de la mobilite.
   * @param idDocRetour - id du doc de retour qui est en lien par rapport à la mobilité
   */
  void updateDocumentDepart(DocumentDepartDto doc, int idMobilite, int versionMob,
      String etatMobilite, int idDocRetour);

  /**
   * Met à jour le document de retour fournit en paramètre.
   * 
   * @param doc - dto contenant les informations du document.
   * @param etatMobilite - etat de la mobilité.
   * @param versionMob - version de la mobilité.
   * @param idMobilite - id de la mobilité.
   */
  void updateDocumentRetour(DocumentRetourDto doc, int idMobilite, int versionMob,
      String etatMobilite, int idDocDepart);

  /**
   * Initialise les documents.
   * 
   * 
   */
  void initDocuments();

  /**
   * Recupere id depart dernier document depart.
   * 
   * 
   */
  int getIdDernierDocDepart();

  /**
   * Recupere id depart dernier document retour.
   * 
   * 
   */
  int getIdDernierDocretour();
}
