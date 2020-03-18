package be.ipl.pae.biz.factory;

import be.ipl.pae.biz.dto.DocumentDepartDto;
import be.ipl.pae.biz.dto.DocumentRetourDto;
import be.ipl.pae.biz.dto.MobiliteDto;
import be.ipl.pae.biz.dto.PartenaireDto;
import be.ipl.pae.biz.dto.PaysDto;
import be.ipl.pae.biz.dto.UserDto;
import be.ipl.pae.exceptions.FatalException;
import be.ipl.pae.utils.Context;
import be.ipl.pae.utils.Logging;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

class BizFactoryImpl implements BizFactory {

  private static final Logger LOGGER = Logging.getLogger(BizFactoryImpl.class.getName());

  /**
   * Renvoi une instance vide de UserDto.
   */
  @Override
  public UserDto getUserDto() {
    Constructor cons = null;
    try {
      cons = (Constructor) Class.forName(Context.getProperty("user")).getDeclaredConstructor();
    } catch (NoSuchMethodException | SecurityException | ClassNotFoundException exc) {
      LOGGER.severe("Problème pour trouver la classe user : " + exc.getMessage());
      throw new FatalException("Problème pour trouver la classe");
    }
    cons.setAccessible(true);
    UserDto user = null;
    try {
      user = (UserDto) cons.newInstance();
    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
        | InvocationTargetException exc) {
      LOGGER.severe("Problème pour créer une instance de la classe user : " + exc.getMessage());
      throw new FatalException("Problème pour créer une instance de la classe");
    }
    return user;
  }

  /**
   * Renvoi une instance vide de MobiliteDto.
   */
  @Override
  public MobiliteDto getMobiliteDto() {
    Constructor cons = null;
    try {
      cons = (Constructor) Class.forName(Context.getProperty("mobilite")).getDeclaredConstructor();
    } catch (NoSuchMethodException | SecurityException | ClassNotFoundException exc) {
      LOGGER.severe("Problème pour trouver la classe mobilite : " + exc.getMessage());
      throw new FatalException("Problème pour trouver la classe");
    }
    cons.setAccessible(true);
    MobiliteDto mob = null;
    try {
      mob = (MobiliteDto) cons.newInstance();
    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
        | InvocationTargetException exc) {
      LOGGER.severe("Problème pour créer une instance de la classe mobilite : " + exc.getMessage());
      throw new FatalException("Problème pour créer une instance de la classe");
    }
    return mob;
  }

  /**
   * Renvoie une instance vide de PartenaireDto.
   */
  @Override
  public PartenaireDto getPartenaireDto() {
    Constructor cons = null;
    try {
      cons =
          (Constructor) Class.forName(Context.getProperty("partenaire")).getDeclaredConstructor();
    } catch (NoSuchMethodException | SecurityException | ClassNotFoundException exc) {
      LOGGER.severe("Problème pour trouver la classe partenaire : " + exc.getMessage());
      throw new FatalException("Problème pour trouver la classe");
    }
    cons.setAccessible(true);
    PartenaireDto part = null;
    try {
      part = (PartenaireDto) cons.newInstance();
    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
        | InvocationTargetException exc) {
      LOGGER
          .severe("Problème pour créer une instance de la classe partenaire : " + exc.getMessage());
      throw new FatalException("Problème pour créer une instance de la classe");
    }
    return part;
  }

  /**
   * Renvoie une instance vide de PaysDto.
   */
  @Override
  public PaysDto getPaysDto() {
    Constructor cons = null;
    try {
      cons = (Constructor) Class.forName(Context.getProperty("pays")).getDeclaredConstructor();
    } catch (NoSuchMethodException | SecurityException | ClassNotFoundException exc) {
      LOGGER.severe("Problème pour trouver la classe pays : " + exc.getMessage());
      throw new FatalException("Problème pour trouver la classe");
    }
    cons.setAccessible(true);
    PaysDto pays = null;
    try {
      pays = (PaysDto) cons.newInstance();
    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
        | InvocationTargetException exc) {
      LOGGER.severe("Problème pour créer une instance de la classe pays : " + exc.getMessage());
      throw new FatalException("Problème pour créer une instance de la classe");
    }
    return pays;
  }

  public DocumentDepartDto getDocumentDepartDto() {
    Constructor cons = null;
    try {
      cons = (Constructor) Class.forName(Context.getProperty("documentDepart"))
          .getDeclaredConstructor();
    } catch (NoSuchMethodException | SecurityException | ClassNotFoundException exc) {
      LOGGER.severe("Problème pour trouver la classe documentDepart : " + exc.getMessage());
      throw new FatalException("Problème pour trouver la classe");
    }
    cons.setAccessible(true);
    DocumentDepartDto doc = null;
    try {
      doc = (DocumentDepartDto) cons.newInstance();
    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
        | InvocationTargetException exc) {
      LOGGER.severe(
          "Problème pour créer une instance de la classe documentDepart : " + exc.getMessage());
      throw new FatalException("Problème pour créer une instance de la classe");
    }
    return doc;
  }

  public DocumentRetourDto getDocumentRetourDto() {
    Constructor cons = null;
    try {
      cons = (Constructor) Class.forName(Context.getProperty("documentRetour"))
          .getDeclaredConstructor();
    } catch (NoSuchMethodException | SecurityException | ClassNotFoundException exc) {
      LOGGER.severe("Problème pour trouver la classe documentRetour : " + exc.getMessage());
      throw new FatalException("Problème pour trouver la classe");
    }
    cons.setAccessible(true);
    DocumentRetourDto doc = null;
    try {
      doc = (DocumentRetourDto) cons.newInstance();
    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
        | InvocationTargetException exc) {
      LOGGER.severe(
          "Problème pour créer une instance de la classe documentRetour : " + exc.getMessage());
      throw new FatalException("Problème pour créer une instance de la classe");
    }
    return doc;
  }

}
