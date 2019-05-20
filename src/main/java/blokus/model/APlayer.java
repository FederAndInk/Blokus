package blokus.model;

import java.util.ArrayList;
import java.util.Set;

import blokus.controller.Game;
import blokus.utils.Utils;
import javafx.scene.paint.Color;

/**
 * APlayer
 */
public abstract class APlayer {
  private Color color;
  private ArrayList<Piece> pieces = new ArrayList<>();
  private boolean passed = false;

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
      board.add(piece, pos, color);
    } else {
      throw new IllegalArgumentException("piece does not exists");
    }
  }

  /**
   * called when a move has been undo</br>
   * mainly to be called by {@link Move#undoMove()}
   * @param piece
   * @param board
   */
  public void undo(Piece piece, Board board) {
    board.remove(piece, getColor());
    piece.normalize();
    pieces.add(piece);
  }

  /**
   * when a move has been undone
   * 
   * to inform the player
   */
  public void undoDone() {
    passed = false;    
  }

  public Move completeMove(Game game) {
    return null;
  }

  public ArrayList<Placement> whereToPlayAll(Board b) {
    ArrayList<Placement> res = new ArrayList<>();
    if (!passed) {
      for (Piece p : pieces) {
        whereToPlay(p, b, res);
      }
      passed = res.isEmpty();
    }
    return res;
  }

  public ArrayList<Placement> whereToPlay(Piece p, Board b) {
    return whereToPlay(p, b, new ArrayList<>());
  }

  public ArrayList<Placement> whereToPlay(Piece p, Board b, ArrayList<Placement> placements) {
    if (!passed) {
      PieceTransform ptOld = p.getState();
      Set<Coord> accCorners = b.getAccCorners(color);

      for (PieceTransform t : p.getTransforms()) {
        p.apply(t);
        for (Coord cAcc : accCorners) {
          for (Coord cPiece : p.getShape()) {
            Coord pos = cAcc.sub(cPiece);
            if (b.canAdd(p, pos, color)) {
              placements.add(new Placement(p, t, pos));
            }
          }
        }
      }
      p.apply(ptOld);
    }
    return placements;
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

  /**
   * @return the passed
   */
  public boolean hasPassed() {
    return passed;
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
