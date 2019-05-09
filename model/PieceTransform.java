package model;

import java.util.Random;

/**
 * PieceTransform
 */
public enum PieceTransform {
  UP(Direction.UP, false), //
  RIGHT(Direction.RIGHT, false), //
  DOWN(Direction.DOWN, false), //
  LEFT(Direction.LEFT, false), //
  UP_REVERTED(Direction.UP, true), //
  RIGHT_REVERTED(Direction.RIGHT, true), //
  DOWN_REVERTED(Direction.DOWN, true), //
  LEFT_REVERTED(Direction.LEFT, true);

  Direction dir;
  boolean reverted;

  PieceTransform(Direction dir, boolean reverted) {
    this.dir = dir;
    this.reverted = reverted;
  }

  public static PieceTransform get(Direction dir, boolean reverted) {
    switch (dir) {
    case UP:
      return !reverted ? UP : UP_REVERTED;
    case RIGHT:
      return !reverted ? RIGHT : RIGHT_REVERTED;
    case DOWN:
      return !reverted ? DOWN : DOWN_REVERTED;
    case LEFT:
      return !reverted ? LEFT : LEFT_REVERTED;
    }
    return null;
  }

  public PieceTransform right() {
    return get(dir.right(), reverted);
  }

  public PieceTransform left() {
    return get(dir.left(), reverted);
  }

  public PieceTransform revertY() {
    return get(dir.revertY(), !reverted);
  }

  public PieceTransform revertX() {
    return get(dir.revertX(), !reverted);
  }

}