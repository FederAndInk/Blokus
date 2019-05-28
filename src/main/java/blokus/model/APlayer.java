package blokus.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import blokus.controller.Game;
import blokus.utils.Utils;

/**
 * APlayer
 */
public abstract class APlayer implements Serializable {
  private static final long serialVersionUID = 5657858218422263717L;
  private PColor color;
  private ArrayList<Piece> pieces = new ArrayList<>();
  private boolean passed = false;

  //
  // Constructors
  //
  public APlayer(PColor color, ArrayList<Piece> pieces) {
    this.color = color;
    populatePieces(pieces);
  }

  public APlayer(APlayer p) {
    this(p.color, p.pieces);
  }

  //
  // Methods
  //

  public void play(Piece piece, Board board, Coord pos) {
    if (pieces.remove(piece)) {
      board.add(piece, pos, color);
    } else {
      throw new IllegalArgumentException("piece does not exist:\n" + piece);
    }
  }

  /**
   * called when a move has been undo</br>
   * mainly to be called by {@link Move#undoMove()}
   * 
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

  public boolean canPlay(Board b) {
    if (!passed) {
      passed = true;
      Set<Coord> accCorners = b.getAccCorners(color);
      if (!accCorners.isEmpty()) {
        Coord pos = new Coord();
        for (int i = 0; passed && i < getPieces().size(); ++i) {
          Piece p = getPieces().get(i);
          PieceTransform ptOld = p.getState();

          for (int j = 0; passed && j < p.getTransforms().size(); ++j) {
            PieceTransform t = p.getTransforms().get(j);
            p.apply(t);
            for (Iterator<Coord> cIt = accCorners.iterator(); passed && cIt.hasNext();) {
              Coord cAcc = cIt.next();
              for (int k = 0; passed && k < p.getShape().size(); ++k) {
                Coord cPiece = p.getShape().get(k);
                pos.set(cAcc).sub_eq(cPiece);
                if (b.canAdd(p, pos, color)) {
                  passed = false;
                }
              }
            }
          }
          p.apply(ptOld);
        }
      }
    }
    return !passed;
  }

  public ArrayList<Move> whereToPlayAll(Game game) {
    ArrayList<Move> res = new ArrayList<>();
    if (!passed) {
      for (Piece p : pieces) {
        whereToPlay(p, game, res);
      }
      passed = res.isEmpty();
    }
    return res;
  }

  public ArrayList<Move> whereToPlay(Piece p, Game game) {
    return whereToPlay(p, game, new ArrayList<>());
  }

  public ArrayList<Move> whereToPlay(Piece p, Game game, ArrayList<Move> placements) {
    if (!passed) {
      Board b = game.getBoard();
      PieceTransform ptOld = p.getState();
      Set<Coord> accCorners = b.getAccCorners(color);
      Coord pos = new Coord();
      for (PieceTransform t : p.getTransforms()) {
        p.apply(t);
        for (Coord cAcc : accCorners) {
          for (Coord cPiece : p.getShape()) {
            pos.set(cAcc).sub_eq(cPiece);
            if (b.canAdd(p, pos, color)) {
              placements.add(new Move(this, p, game, new Coord(pos), t));
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
  public PColor getColor() {
    return color;
  }

  /**
   * @return the pieces
   */
  public ArrayList<Piece> getPieces() {
    return pieces;
  }

  /**
   * 
   * @param noPiece the piece no noPiece
   * @return
   */
  public Piece getPiece(int noPiece) {
    Piece pie = Utils.findIf(pieces, (p) -> {
      return p.no == noPiece;
    });
    return pie;
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
    return "Player " + Utils.getAnsi(color.primaryColor()) + color.getName() + Utils.ANSI_RESET;
  }

  abstract public APlayer copy();
}
