package be.ipl.pae.dal;

import be.ipl.pae.exceptions.DalException;
import be.ipl.pae.exceptions.FatalException;
import be.ipl.pae.utils.Context;
import be.ipl.pae.utils.Logging;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;


class DalServicesImpl implements DalBackendServices, DalServices {

  private static final Logger LOGGER = Logging.getLogger(DalServicesImpl.class.getName());

  private BasicDataSource connectionPool;
  private final ThreadLocal<Connection> connections;

  public DalServicesImpl() {
    connections = new ThreadLocal<Connection>();
    try {
      Properties prop = new Properties();
      prop.put("driverClassName", Context.getProperty("driverDb"));
      prop.put("url", Context.getProperty("urlSql"));
      prop.put("username", Context.getProperty("pseudoSql"));
      prop.put("password", Context.getProperty("mdpSql"));
      this.connectionPool = BasicDataSourceFactory.createDataSource(prop);
      LOGGER.info("La connection à la base de données à été établie");
    } catch (Exception exc) {
      LOGGER.severe("FatalException ==> Impossible de se connecter à la base de données");
      throw new FatalException("Impossible de se connecter à la base de données");
    }
  }

  /*
   * {@inheritDoc}
   */
  @Override
  public PreparedStatement getPreparedStatement(String request) throws SQLException {
    if (connections.get() == null) {
      LOGGER.severe("La connection n'existe pas.");
      throw new DalException("La connection n'existe pas.");
    }
    LOGGER.info("un PS a été renvoyé");
    return connections.get().prepareStatement(request);
  }

  /*
   * {@inheritDoc}
   */
  @Override
  public void startTransaction() {
    if (connections.get() != null) {
      LOGGER.severe("Transaction déjà ouverte");
      throw new FatalException("Transaction déjà ouverte");
    }
    try {
      Connection connection = connectionPool.getConnection();
      connections.set(connection);
      connection.setAutoCommit(false);
      LOGGER.info("La connexion est prise");
    } catch (SQLException exc) {
      LOGGER.severe("Problème de transaction : " + exc.getMessage());
      throw new DalException("Problème de transaction");
    }
  }

  /*
   * {@inheritDoc}
   */
  @Override
  public void rollBackTransaction() {
    if (connections.get() == null) {
      LOGGER.severe("La transaction était fermée");
      throw new FatalException("La transaction était fermée");
    }
    Connection connection = connections.get();
    try {
      connection.rollback();
      connection.setAutoCommit(true);

    } catch (SQLException exc) {
      LOGGER.severe("Problème de rollback : " + exc.getMessage());
      throw new DalException("Problème de rollback");
    } finally {
      try {
        connection.close();
      } catch (SQLException exc) {
        LOGGER.severe(exc.getMessage());
      }
      connections.remove();
      LOGGER.info("La connexion est libérée");
    }
  }

  /*
   * {@inheritDoc}
   */
  @Override
  public void commitTransaction() {
    if (connections.get() == null) {
      LOGGER.severe("La transaction était fermée");
      throw new FatalException("La transaction était fermée");
    }
    Connection connection = connections.get();
    try {
      connection.commit();
      connection.setAutoCommit(true);

    } catch (SQLException exc) {
      LOGGER.severe("Problème de commit : " + exc.getMessage());
      throw new DalException("Problème de commit");
    } finally {

      try {
        connection.close();
      } catch (SQLException exc) {
        LOGGER.severe(exc.getMessage());

      }
      connections.remove();
      LOGGER.info("La connexion est libérée");
    }
  }

}
