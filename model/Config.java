package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Config {
    static Config instance = null;
    Properties prop;
    Logger logger;

    public static Config i() {
        if (instance == null)
            instance = new Config();
        return instance;
    }

    private void chargerProprietes(Properties p, InputStream in, String nom) {
        try {
            if (in != null) {
                p.load(in);
                logger().info("Fichier de propriétés " + nom + " chargé");
            } else {
                logger().info("Fichier de propriétés " + nom + " introuvé");
            }
        } catch (IOException e) {
            System.err.println("Impossible de charger " + nom);
            System.err.println(e.toString());
            System.exit(1);
        }
    }

    private Config() {
        // On charge les propriétés
        Properties defaut = new Properties();
        chargerProprietes(defaut, load("default.cfg"), "default.cfg");
        // Il faut attendre le dernier moment pour utiliser le logger
        // car celui-ci s'initialise avec les propriétés
        String nom = System.getProperty("user.home") + "/.Blokus";
        prop = new Properties(defaut);
        try {
            chargerProprietes(prop, new FileInputStream(nom), nom);
        } catch (FileNotFoundException e) {
            logger().info("Fichier de propriétés " + nom + " introuvé");
        }
        logger.setLevel(Level.parse(get("LogLevel")));
    }

    public String get(String nom) {
        String value = prop.getProperty(nom);
        if (value != null) {
            return value;
        } else {
            throw new NoSuchElementException("Propriété " + nom + " manquante");
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
		return load("resources/" + rsc);
	}

}
