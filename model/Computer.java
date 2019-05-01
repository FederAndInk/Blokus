package model;

import javafx.scene.paint.Color;

/**
 * Computer
 */
public class Computer extends APlayer {

  public Computer(Color color) {
    super(color);
  }

  @Override
  public boolean completeMove(Board board) {
    // TODO: complete AI
    return false;
  }
}