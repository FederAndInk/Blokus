package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Observable;

import javafx.scene.paint.Color;
import utils.Utils;

/**
 * APlayer
 */
public abstract class APlayer extends Observable {
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
      setChanged();
      notifyObservers(piece);
      board.add(piece, pos, color);
    } else {
      throw new IllegalArgumentException("piece does not exists");
    }
  }

  public void completeMove(Board board) {
  }

  // public boolean hasToPass(Board b) {
  // for (Piece p : pieces) {
  // if (!b.whereToPlay(p, color).isEmpty())
  // return false;
  // }
  // return true;
  // }

  // public HashMap<Piece, HashMap<PieceTransform, HashSet<Coord>>>
  // hasToPass(Board b) {
  // return b.whereToPlayAll(pieces, color);
  // }

  public HashMap<Piece, HashMap<PieceTransform, HashSet<Coord>>> whereToPlayAll(Board b) {
    HashMap<Piece, HashMap<PieceTransform, HashSet<Coord>>> res = new HashMap<>();
    for (Piece p : pieces) {
      HashMap<PieceTransform, HashSet<Coord>> posPlacement = whereToPlay(p, b);
      if (!posPlacement.isEmpty()) {
        res.put(p, posPlacement);
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