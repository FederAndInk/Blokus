package model;

/**
 * DiagonalDirection
 */
public enum DiagonalDirection {
  UP_RIGHT(Direction.UP, Direction.RIGHT), //
  UP_LEFT(Direction.UP, Direction.LEFT), //
  DOWN_RIGHT(Direction.DOWN, Direction.RIGHT), //
  DOWN_LEFT(Direction.DOWN, Direction.LEFT);

  Direction d1;
  Direction d2;

  DiagonalDirection(Direction d1, Direction d2) {
    this.d1 = d1;
    this.d2 = d2;
  }
}