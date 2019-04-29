
package utils;

import java.io.InputStream;

/**
 * Utils
 */
public class Utils {

  public static InputStream loadRsc(String s) {
    return ClassLoader.getSystemResourceAsStream(s);
  }
}