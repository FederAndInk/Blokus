
package utils;

import javafx.scene.paint.Color;

/**
 * Utils
 */
public class Utils {

  public static final String ANSI_RESET = "\u001B[0m";

  public static String getAnsi(Color c) {
    return "\u001B[38;2;" + (int) (c.getRed() * 255) + ";" + (int) (c.getGreen() * 255) + ";"
        + (int) (c.getBlue() * 255) + "m";
  }

}