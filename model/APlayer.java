package model;

import java.util.ArrayList;
import java.util.Observable;

import javafx.scene.paint.Color;

/**
 * APlayer
 */
public abstract class APlayer extends Observable {
  Color color;
  Move nextMove;
  ArrayList<Piece> pieces = new ArrayList<>();

  public APlayer(Color color) {
    this.color = color;
    nextMove = null;
  }

  public void makeMove(Piece piece, Coord pos) {
    nextMove = new Move(this, piece, pos);
  }

  public void play(Piece piece, Board board, Coord pos) {
    if (pieces.remove(piece)) {
      notifyObservers(piece);
      board.add(piece, pos, color);
    } else {
      throw new IllegalArgumentException("APlayer.play: piece does not exists");
    }
  }

  public boolean completeMove(Board board) {
    return false;
  }

  public Move popNextMove() {
    Move tmp = nextMove;
    nextMove = null;
    return tmp;
  }

}