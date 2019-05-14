package blokus.utils;

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

  public static byte get(byte b, int pos) {
    return (byte) ((b >>> pos) & 1);
  }

  public static byte set(byte b, int pos, int val) {
    b = (byte) (b & ~(1 << pos));
    return (byte) (b | (val << pos));
  }

  public static byte get2(byte b, int pos) {
    pos = pos << 1;
    return (byte) ((b >>> (pos)) & 3);
  }

  public static byte set2(byte b, int pos, int val) {
    pos = pos << 1;
    b = (byte) (b & ~(3 << (pos)));
    return (byte) (b | (val << pos));
  }

  public static void printByte(byte b) {
    for (int i = 7; i >= 0; i--) {
      System.out.print(get(b, i));
    }
    System.out.println();
  }

}
