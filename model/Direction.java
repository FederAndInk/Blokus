package model;

/**
 * Orientation
 */
public enum Direction {
  UP(0, -1), //
  RIGHT(1, 0), //
  DOWN(0, 1), //
  LEFT(-1, 0);

  int x;
  int y;

  Direction(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * rotate clockwise
   */
  Direction right() {
    return values()[(ordinal() + 1) % values().length];
  }

  /**
   * rotate counter-clockwise
   */
  Direction left() {
    int len = values().length;
    return values()[(ordinal() + len - 1) % len];
  }

}