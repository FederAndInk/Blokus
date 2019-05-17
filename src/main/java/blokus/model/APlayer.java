package blokus.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import blokus.controller.Game;
import blokus.utils.Utils;
import javafx.scene.paint.Color;

/**
 * APlayer
 */
public abstract class APlayer {
  private Color color;
  private ArrayList<Piece> pieces = new ArrayList<>();

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

  public void undo(Piece piece, Board board) {
    board.remove(piece, getColor());
    piece.normalize();
    pieces.add(piece);
  }

  public Move completeMove(Game game) {
    return null;
  }

  public ArrayList<Placement> whereToPlayAll(Board b) {
    ArrayList<Placement> res = new ArrayList<>();
    for (Piece p : pieces) {
      HashMap<PieceTransform, HashSet<Coord>> posPlacement = whereToPlay(p, b);
      if (!posPlacement.isEmpty()) {
        for (PieceTransform pt : posPlacement.keySet()) {
          for (Coord c : posPlacement.get(pt)) {
            res.add(new Placement(p, pt, c));
          }
        }
      }
    }
    return res;
  }

  public HashMap<PieceTransform, HashSet<Coord>> whereToPlay(Piece p, Board b) {
    HashMap<PieceTransform, HashSet<Coord>> map = new HashMap<>();
    Piece pTmp = new Piece(p);
    HashSet<Coord> accCorners = b.getAccCorners(color);

    for (PieceTransform t : pTmp.getTransforms()) {
      pTmp.apply(t);
      for (Coord cAcc : accCorners) {
        HashSet<Coord> shapeTmp = pTmp.getShape();
        for (Coord cPiece : shapeTmp) {
          Coord pos = cAcc.sub(cPiece);
          if (b.canAdd(pTmp, pos, color)) {
            map.computeIfAbsent(t, (k) -> {
              return new HashSet<>();
            }).add(pos);
          }
        }
      }
    }
    return map;
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
