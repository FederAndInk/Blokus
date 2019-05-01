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

  //
  // Constructors
  //
  public APlayer(Color color) {
    this.color = color;
    nextMove = null;
    populatePieces();
  }

  //
  // Methods
  //
  public void makeMove(Piece piece, Coord pos) {
    nextMove = new Move(this, piece, pos);
  }

  public void play(Piece piece, Board board, Coord pos) {
    if (pieces.remove(piece)) {
      notifyObservers(piece);
      board.add(piece, pos, color);
    } else {
      throw new IllegalArgumentException("piece does not exists");
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

  //
  // Accessors
  //

  /**
   * @return the color
   */
  public Color getColor() {
    return color;
  }

  public void addPiece(Piece piece) {
    System.out.println("adding piece: ");
    System.out.println(piece);
    pieces.add(piece);
  }

  private void populatePieces() {
    Piece tmp = new Piece();
    tmp.add(new Coord(0, 0));
    addPiece(tmp);

    tmp = new Piece();
    tmp.add(new Coord(0, 0));
    tmp.add(new Coord(1, 0));
    addPiece(tmp);

    tmp = new Piece();
    tmp.add(new Coord(-1, 0));
    tmp.add(new Coord(0, 0));
    tmp.add(new Coord(1, 0));
    addPiece(tmp);

    tmp = new Piece();
    tmp.add(new Coord(-1, 0));
    tmp.add(new Coord(0, 0));
    tmp.add(new Coord(1, 0));
    tmp.add(new Coord(2, 0));
    addPiece(tmp);

    tmp = new Piece();
    tmp.add(new Coord(-2, 0));
    tmp.add(new Coord(-1, 0));
    tmp.add(new Coord(0, 0));
    tmp.add(new Coord(1, 0));
    tmp.add(new Coord(2, 0));
    addPiece(tmp);

    tmp = new Piece();
    tmp.add(new Coord(-1, 0));
    tmp.add(new Coord(0, 0));
    tmp.add(new Coord(0, 1));
    addPiece(tmp);
  }
}