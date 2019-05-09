package model;

import java.util.ArrayList;
import java.util.Observable;

import javafx.scene.paint.Color;
import utils.Utils;

/**
 * APlayer
 */
public abstract class APlayer extends Observable {
  Color color;
  ArrayList<Piece> pieces = new ArrayList<>();

  //
  // Constructors
  //
  public APlayer(Color color, ArrayList<Piece> pieces) {
    this.color = color;
    populatePieces(pieces);
  }

  //
  // Methods
  //

  public void play(Piece piece, Board board, Coord pos) {
    if (pieces.remove(piece)) {
      setChanged();
      notifyObservers(piece);
      board.add(piece, pos, color);
    } else {
      throw new IllegalArgumentException("piece does not exists");
    }
  }

  public void completeMove(Board board) {
  }

  public boolean hasToPass(Board b) {
    for (Piece p : pieces) {
      if (!b.whereToPlay(p, color).isEmpty())
        return false;
    }
    return true;
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

  /**
   * @return the pieces
   */
  public ArrayList<Piece> getPieces() {
    return pieces;
  }

  public void addPiece(Piece piece) {
    System.out.println("adding piece: ");
    System.out.println(piece);
    pieces.add(piece);
  }

  private void populatePieces(ArrayList<Piece> ps) {
    for (Piece p : ps) {
      pieces.add(new Piece(p));
    }
  }

  @Override
  public String toString() {
    return "Player " + Utils.getAnsi(color) + Board.getColorName(color) + Utils.ANSI_RESET;
  }
}