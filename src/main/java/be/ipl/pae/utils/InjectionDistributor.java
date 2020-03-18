package be.ipl.pae.utils;

import be.ipl.pae.annotations.Inject;
import be.ipl.pae.exceptions.FatalException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class InjectionDistributor {

  public static final String OS_NAME = System.getProperty("os.name");
  public static final boolean is_OS_LINUX = OS_NAME.startsWith("Linux");
  public static final boolean is_OS_WINDOWS = OS_NAME.startsWith("Windows");

  private static Map<String, Object> mapInstance = new HashMap<String, Object>();


  /**
   * Permet d'obtenir une dépendance.
   * 
   * @param classe - classe dont on veut l'instance
   * @return l'instance de la classe en paramètre
   */
  public static Object getDependancy(Class classe) {
    Object toInject = null;
    try {
      Constructor cons = null;
      Class classeIntrospecte = classe;
      if (Context.getProperty(classe.getSimpleName()) != null) {
        classeIntrospecte = (Class) Class.forName(Context.getProperty(classe.getSimpleName()));
      }
      if (mapInstance.containsKey(classeIntrospecte.getSimpleName())) {
        return mapInstance.get(classeIntrospecte.getSimpleName());
      }
      cons = (Constructor) classeIntrospecte.getDeclaredConstructor();
      cons.setAccessible(true);
      toInject = cons.newInstance();
      mapInstance.put(classeIntrospecte.getSimpleName(), toInject);
      injectionToMakeRecursif(toInject);
    } catch (NoSuchMethodException | SecurityException | InstantiationException
        | IllegalAccessException | IllegalArgumentException | InvocationTargetException
        | ClassNotFoundException exc) {
      exc.printStackTrace();
    }
    return toInject;
  }

  /**
   * Cette méthode permet d'injecter récursivement les dépendances.
   * 
   * @param toInject - objet à injecter
   */
  private static void injectionToMakeRecursif(Object toInject) {

    try {
      for (Field field : Class.forName(toInject.getClass().getName()).getDeclaredFields()) {
        if (field.isAnnotationPresent(Inject.class)) {
          field.setAccessible(true);
          Object newInstance = getDependancy(field.getType());
          field.set(toInject, newInstance);
        }
      }
    } catch (SecurityException | ClassNotFoundException | IllegalAccessException
        | IllegalArgumentException exception) {
      exception.printStackTrace();
      throw new FatalException("Error injection");
    }
  }
}
