package blokus.model;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Config {
  public static final String NB_PLAYER = "nb_player";
  public static final String LOG_LEVEL = "log_level";
  /**
   * to be used with {@link #getf(String)}
   */
  public static final String VOLUME = "volume";

  /**
   * to be used with {@link #getMany(String)}
   */
  public static final String MUSIC = "music";

  /**
   * append {@link GameType#toString()}</br>
   * 
   * to be used with {@link #getMany(String)}
   */
  public static final String RULES = "rules_";
  public static final String CONTROLES = "controles";

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

  public float getf(String name) {
    return Float.parseFloat(get(name));
  }

  public int geti(String name) {
    return Integer.parseInt(get(name));
  }

  public boolean getb(String name) {
    return Boolean.parseBoolean(get(name));
  }

  public ArrayList<String> getMany(String name) {
    ArrayList<String> ret = new ArrayList<>();

    int size = geti("nb_" + name);
    for (int i = 0; i < size; i++) {
      ret.add(get(name + i));
    }

    return ret;
  }

  public void set(String name, Object value) {
    prop.setProperty(name, value.toString());
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
    InputStream is = ClassLoader.getSystemResourceAsStream(s);
    if (is == null) {
      try {
        is = new BufferedInputStream(new FileInputStream(s));
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }
    return is;
  }

  public static InputStream loadRsc(String rsc) {
    return load(rsc);
  }

}
