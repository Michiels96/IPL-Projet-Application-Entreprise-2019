package be.ipl.pae.dal;

import java.sql.PreparedStatement;
import java.sql.SQLException;


public interface DalBackendServices {

  /**
   * Permet de récupérer un PreparedStatement permettant l'éxecution d'une requête sql.
   * 
   * @param request - String contenant la requête sql
   * @return PreparedStatement
   * @throws SQLException - En cas d'erreur SQL
   */
  PreparedStatement getPreparedStatement(String request) throws SQLException;

}
