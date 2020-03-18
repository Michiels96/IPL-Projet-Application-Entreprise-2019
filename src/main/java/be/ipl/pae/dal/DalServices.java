package be.ipl.pae.dal;

public interface DalServices {

  /**
   * Ouvre une transaction.
   */
  void startTransaction();

  /**
   * Fait un roll-back de la transaction.
   */
  void rollBackTransaction();

  /**
   * Envoie les résultats de la transaction.
   */
  void commitTransaction();

}
