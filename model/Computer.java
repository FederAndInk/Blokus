package model;

import java.util.ArrayList;

import javafx.scene.paint.Color;

/**
 * Computer
 */
public class Computer extends APlayer {

  public Computer(Color color, ArrayList<Piece> pieces) {
    super(color, pieces);
  }

  @Override
  public void completeMove(Board board) {
    // TODO: complete AI
  }
}