package blokus.model;

import javafx.scene.paint.Color;

/**
 * PColor
 */
public enum PColor {
  BLUE("Bleu"), //
  YELLOW("Jaune"), //
  RED("Rouge"), //
  GREEN("Vert"), //
  NO_COLOR("No color"), //
  DEBUG("Debug Color"),//
  ;

  String name;

  PColor(String name) {
    this.name = name;
  }

  static public PColor get(byte id) {
    return PColor.values()[id];
  }

  public byte getId() {
    return (byte) ordinal();
  }

  public Color primaryColor() {
    switch (this) {
    case BLUE:
      return Color.web("#1879c9");
    case YELLOW:
      return Color.web("#f2e126");
    case RED:
      return Color.web("#fc1942");
    case GREEN:
      return Color.web("#22c157");
    case NO_COLOR:
      return null;
    case DEBUG:
      return Color.BLUEVIOLET;
    }
    return null;
  }

  public Color secondaryColor() {
    switch (this) {
    case BLUE:
      return Color.web("#5494c9");
    case YELLOW:
      return Color.web("#fcf174");
    case RED:
      return Color.web("#fc6480");
    case GREEN:
      return Color.web("#75f49f");
    case NO_COLOR:
      return null;
    case DEBUG:
      return Color.BLUEVIOLET;
    }
    return null;
  }

  public boolean isColor() {
    return this != NO_COLOR;
  }

  public static boolean isColor(byte id) {
    return id != NO_COLOR.getId();
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

}