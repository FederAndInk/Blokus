package blokus.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
  private HashMap<Integer, ArrayList<Move>> moves = null;

  //
  // Constructors
  //
  public APlayer(PColor color, ArrayList<Piece> pieces) {
    this.color = color;
    populatePieces(pieces);
  }

  public APlayer(APlayer p, Game g) {
    this(p.color, p.pieces);
    if (p.moves != null) {
      moves = new HashMap<>();
      for (int pieNo : p.moves.keySet()) {
        moves.put(pieNo, new ArrayList<>());
        for (Move m : p.moves.get(pieNo)) {
          Move mCpy = new Move(m);
          mCpy.changeGame(g);
          moves.get(pieNo).add(mCpy);
        }
      }
    }
  }

  //
  // Methods
  //

  public void play(Piece piece, Game game, Coord pos) {
    Board b = game.getBoard();
    if (pieces.remove(piece)) {
      b.add(piece, pos, color);
      if (moves != null) {
        moves.remove(piece.no);
      }
      // TODO
      cleanMoves(b);
      HashSet<Coord> accCorners = new HashSet<>();
      for (Coord c : piece.getCorners()) {
        if (b.canAdd(c.x, c.y, color.getId())) {
          accCorners.add(c);
        }
      }
      whereToPlayAll(game, accCorners);
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
    moves = null;
  }

  public Move completeMove(Game game) {
    return null;
  }

  public boolean canPlay(Board b) {
    if (!passed) {
      /// TODO: now use #whereToPlayAll()
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

  public ArrayList<Move> whereToPlayAllFlat(Game g) {
    Map<Integer, ArrayList<Move>> ms = whereToPlayAll(g);
    ArrayList<Move> res = new ArrayList<>();
    for (int pNo : ms.keySet()) {
      res.addAll(ms.get(pNo));
    }
    return res;
  }

  public Map<Integer, ArrayList<Move>> whereToPlayAll(Game g) {
    if (moves == null) {
      whereToPlayAllFull(g);
    } else {
      cleanMoves(g.getBoard());
    }
    return Collections.unmodifiableMap(moves);
  }

  private void whereToPlayAllFull(Game game) {
    moves = new HashMap<>();
    if (!passed) {
      for (Piece p : pieces) {
        ArrayList<Move> res = whereToPlayFull(p, game, new ArrayList<>());
        if (!res.isEmpty()) {
          moves.put(p.no, res);
        }
      }
      passed = moves.isEmpty();
    }
  }

  private void whereToPlayAll(Game game, Set<Coord> accCorners) {
    if (moves == null) {
      moves = new HashMap<>();
    }
    if (!passed) {
      for (Piece p : pieces) {
        ArrayList<Move> res = moves.computeIfAbsent(p.no, (k) -> new ArrayList<>());
        whereToPlay(p, game, res, accCorners);
        if (res.isEmpty()) {
          moves.remove(p.no);
        }
      }
    }
  }

  public List<Move> whereToPlay(Piece p, Game game) {
    if (moves == null) {
      whereToPlayAllFull(game);
    } else {
      cleanMoves(game.getBoard());
    }
    return Collections.unmodifiableList(moves.get(p.no));
  }

  private void cleanMoves(Board b) {
    if (moves != null) {
      for (Iterator<Integer> itp = moves.keySet().iterator(); itp.hasNext();) {
        Piece p = getPiece(itp.next());
        ArrayList<Move> ms = moves.get(p.no);
        for (Iterator<Move> itm = ms.iterator(); itm.hasNext();) {
          Move m = itm.next();
          p.apply(m.getTrans());
          if (!b.canAdd(p, m.getPos(), color)) {
            itm.remove();
          }
        }
        if (ms.isEmpty()) {
          itp.remove();
        }
      }
    }
  }

  /**
   * recompute for all accessible corners
   * 
   * @param p
   * @param game
   * @param placements
   * @return
   */
  private ArrayList<Move> whereToPlayFull(Piece p, Game game, ArrayList<Move> placements) {
    return whereToPlay(p, game, placements, game.getBoard().getAccCorners(color));
  }

  private ArrayList<Move> whereToPlay(Piece p, Game game, ArrayList<Move> placements, Set<Coord> accCorners) {
    if (!passed) {
      Board b = game.getBoard();
      PieceTransform ptOld = p.getState();
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
    return "Player(" + this.getClass().getName() + ") " + Utils.getAnsi(color.primaryColor()) + color.getName()
        + Utils.ANSI_RESET;
  }

  abstract public APlayer copy(Game g);
}
