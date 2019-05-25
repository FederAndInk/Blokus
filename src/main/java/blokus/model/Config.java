package blokus.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Config {
  public static final String NB_PLAYER = "nb_player";
  public static final String LOG_LEVEL = "log_level";

  private static Config instance = null;
  private Properties prop;
  private Logger logger;
  private String userConfName;

  public static Config i() {
    if (instance == null)
      instance = new Config();
    return instance;
  }

  private void chargerProprietes(Properties p, InputStream in, String name) {
    try {
      if (in != null) {
        p.load(in);
        logger().info("Fichier de propriétés " + name + " chargé");
      } else {
        logger().info("Fichier de propriétés " + name + " introuvé");
      }
    } catch (IOException e) {
      System.err.println("Impossible de charger " + name);
      System.err.println(e.toString());
      System.exit(1);
    }
  }

  private Config() {
    // On charge les propriétés
    prop = new Properties();
    chargerProprietes(prop, load("default.cfg"), "default.cfg");
    // Il faut attendre le dernier moment pour utiliser le logger
    // car celui-ci s'initialise avec les propriétés
    userConfName = System.getProperty("user.home") + "/.Blokus";
    try {
      chargerProprietes(prop, new FileInputStream(userConfName), userConfName);
    } catch (FileNotFoundException e) {
      logger().info("Fichier de propriétés " + userConfName + " introuvé");
    }
    logger.setLevel(Level.parse(get(LOG_LEVEL)));
  }

  public String get(String name) {
    String value = prop.getProperty(name);
    if (value != null) {
      return value;
    } else {
      throw new NoSuchElementException("Propriété " + name + " manquante");
    }
  }

  public void set(String name, String value) {
    prop.setProperty(name, value);
    File f = new File(userConfName);
    try {
      prop.store(new FileOutputStream(f), "");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Logger logger() {
    if (logger == null) {
      System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s : %5$s%n");
      logger = Logger.getLogger("Blokus.Logger");
    }
    return logger;
  }

  public static InputStream load(String s) {
    return ClassLoader.getSystemResourceAsStream(s);
  }

  public static InputStream loadRsc(String rsc) {
    return load(rsc);
  }

}
