package model;

/**
 * Orientation
 */
public enum Orientation {
  UP, //
  RIGHT, //
  DOWN, //
  LEFT //
  ;

  /**
   * rotate clockwise
   */
  Orientation right() {
    return values()[(ordinal() + 1) % values().length];
  }

  /**
   * rotate counter-clockwise
   */
  Orientation left() {
    int len = values().length;
    return values()[(ordinal() + len - 1) % len];
  }

}