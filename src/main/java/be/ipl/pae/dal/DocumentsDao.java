package be.ipl.pae.dal;

import be.ipl.pae.biz.dto.DocumentDepartDto;
import be.ipl.pae.biz.dto.DocumentRetourDto;
import be.ipl.pae.biz.dto.MobiliteDto;

public interface DocumentsDao {

  /**
   * Query pour récupérer les documents de départ.
   * 
   * @param mob - dto de mobilité
   * @return Dto de documents de départ
   */
  DocumentDepartDto findDocumentsDepartByMobiliteId(MobiliteDto mob);

  /**
   * Query pour récupérer les documents de retour.
   * 
   * @param mob - dto de mobilité
   * @return Dto de documents de retour
   */
  DocumentRetourDto findDocumentsRetourByMobiliteId(MobiliteDto mob);

  /**
   * Crée un nouveau document de depart en db.
   * 
   */
  void insertDocumentRetour();

  /**
   * Crée un nouveau document de retour en db.
   * 
   */
  void insertDocumentDepart();

  /**
   * Permet d'obtenir l'id du dernier ajout sur les documents départ.
   * 
   * @return id du dernier doc
   */
  int getLastInsertIdDocumentDepart();

  /**
   * Permet d'obtenir l'id du dernier ajout sur les documents retour.
   * 
   * @return id du dernier doc
   */
  int getLastInsertIdDocumentRetour();

  /**
   * Récupère les informations d'un document depart en db.
   * 
   * @param idDoc - l'id du document de depart.
   * @return un dto contenant les informations.
   */
  DocumentDepartDto getDocumentDepartById(int idDoc);

  /**
   * Récupère les informations d'un document de retour en db.
   * 
   * @param idDoc - l'id du document de depart.
   * @return un dto contenant les informations.
   */
  DocumentRetourDto getDocumentRetourById(int idDoc);

  /**
   * Met à jour en db les champs du document de départ.
   * 
   * @param doc - dto contenant les informations du document.
   */
  void updateDocumentDepart(DocumentDepartDto doc);

  /**
   * Met à jour en db les champs du document de retour.
   * 
   * @param doc - dto contenant les informations du document.
   */
  void updateDocumentRetour(DocumentRetourDto doc);
}
