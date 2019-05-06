package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Observable;

import javafx.scene.paint.Color;
import utils.Utils;

/**
 * Class Board
 */
public class Board extends Observable {
  public static final Coord SIZE = new Coord(20, 20);

  public static final ArrayList<Color> colors = new ArrayList<>();
  public static final ArrayList<String> colorsName = new ArrayList<>();
  //
  // Fields
  //

  /**
   * y line of x colors
   */
  private ArrayList<ArrayList<Color>> board = new ArrayList<>();

  private HashMap<Color, ArrayList<Piece>> pieces = new HashMap<>();

  //
  // Constructors
  //
  public Board() {
    addColor(Color.BLUE, "Blue");
    addColor(Color.YELLOW, "Yellow");
    addColor(Color.RED, "Red");
    addColor(Color.GREEN, "Green");

    for (int i = 0; i < SIZE.y; i++) {
      board.add(new ArrayList<>());
      for (int j = 0; j < SIZE.x; j++) {
        board.get(i).add(null);
      }
    }
  }

  public Board(Board b) {
    for (int i = 0; i < SIZE.y; i++) {
      board.add(new ArrayList<>());
      for (int j = 0; j < SIZE.x; j++) {
        board.get(i).add(b.get(j, i));
      }
    }

    pieces.putAll(b.pieces);
  }

  //
  // Methods
  //

  /**
   * @param piece
   * @param pos
   * @param color
   */
  public void add(Piece piece, Coord pos, Color color) {
    if (canAdd(piece, pos, color)) {
      piece.translate(pos);
      for (Coord c : piece.getShape()) {
        set(c, color);
      }
      pieces.computeIfAbsent(color, (_c) -> {
        return new ArrayList<>();
      }).add(piece);

    } else {
      throw new IllegalArgumentException(
          "can't place " + Utils.getAnsi(color) +  getColorName(color) + Utils.ANSI_RESET + " piece at " + pos + "\npiece:\n" + piece);
    }

    notifyObservers();
  }

  /**
   * @return HashMap<Color, Integer>
   */
  public HashMap<Color, Integer> score() {
    HashMap<Color, Integer> map = new HashMap<>();

    for (ArrayList<Color> l : board) {
      for (Color c : l) {
        if (c != null) {
          map.put(c, map.getOrDefault(c, 0) + 1);
        }
      }
    }

    return map;
  }

  /**
   * @param piece
   * @param pos
   */
  public boolean canAdd(Piece piece, Coord pos, Color color) {
    boolean ret = true;
    try {
      for (Coord c : piece.getShape()) {
        Coord tmp = pos.add(c);
        ret = ret && canAdd(tmp, color);
      }
      boolean cornerCheck = false;
      if (isFirst(color)) {
        for (Coord c : piece.getShape()) {
          Coord tmp = pos.add(c);
          cornerCheck = cornerCheck || isCorner(tmp);
        }
      } else {
        for (Coord cornerRel : piece.getCorners()) {
          Coord corner = cornerRel.add(pos);
          cornerCheck = cornerCheck || (isIn(corner) && get(corner) == color);
        }
      }
      ret = ret && cornerCheck;

    } catch (ArrayIndexOutOfBoundsException e) {
      ret = false;
    }
    return ret;
  }

  private boolean canAdd(Coord c, Color color) {
    boolean ret = isIn(c) && get(c) == null;
    for (Direction o : Direction.values()) {
      Coord tmp = c.add(o);
      ret = ret && (!isIn(tmp) || get(tmp) != color);
    }

    return ret;
  }

  public HashSet<Coord> getAccCorners(Color color) {
    HashSet<Coord> res = new HashSet<>();
    if (pieces.containsKey(color)) {
      for (Piece p : pieces.get(color)) {
        for (Coord c : p.getCorners()) {
          if (isIn(c) && get(c) == null) {
            res.add(c);
          }
        }
      }
    } else {
      res.add(new Coord(0, 0));
      res.add(new Coord(SIZE.x - 1, 0));
      res.add(new Coord(0, SIZE.y - 1));
      res.add(new Coord(SIZE.x - 1, SIZE.y - 1));
      Iterator<Coord> it = res.iterator();
      for (Coord c = it.next(); it.hasNext(); c = it.next()) {
        if (get(c) != null) {
          it.remove();
        }
      }
    }
    return res;
  }

  public HashMap<PieceTransform, HashSet<Coord>> whereToPlay(Piece p, Color c) {
    HashMap<PieceTransform, HashSet<Coord>> map = new HashMap<>();
    Piece pTmp = new Piece(p);
    HashSet<Coord> accCorners = getAccCorners(c);

    for (PieceTransform t : pTmp.getTransforms()) {
      pTmp.apply(t);
      for (Coord cAcc : accCorners) {
        HashSet<Coord> shapeTmp = pTmp.getShape();
        for (Coord cPiece : shapeTmp) {
          Coord pos = cAcc.sub(cPiece);
          if (canAdd(pTmp, pos, c)) {
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
  // Accessor methods
  //

  /**
   * check bounds of the board
   */
  boolean isIn(Coord c) {
    return c.x >= 0 && c.y >= 0 && c.x < SIZE.x && c.y < SIZE.y;
  }

  Color get(int x, int y) {
    return board.get(y).get(x);
  }

  Color get(Coord pos) {
    return get(pos.x, pos.y);
  }

  void set(int x, int y, Color c) {
    board.get(y).set(x, c);
  }

  void set(Coord pos, Color c) {
    board.get(pos.y).set(pos.x, c);
  }

  /**
   * return true if c is at a corner of the board</br>
   * 
   * *---*</br>
   * -----</br>
   * -----</br>
   * *---*</br>
   */
  private boolean isCorner(Coord c) {
    return c.equals(new Coord(0, 0)) || c.equals(new Coord(0, SIZE.y - 1)) //
        || c.equals(new Coord(SIZE.x - 1, 0)) || c.equals(new Coord(SIZE.x - 1, SIZE.y - 1));
  }

  /**
   * 
   * @param color
   * @return true if color hasn't played before in the board
   */
  private boolean isFirst(Color color) {
    return !pieces.containsKey(color);
  }

  public void addColor(Color color, String colorName) {
    colors.add(color);
    colorsName.add(colorName);
  }

  private String getColorName(Color c) {
    return colorsName.get(getColorId(c));
  }

  private int getColorId(Color c) {
    return colors.indexOf(c);
  }

  //
  // Other methods
  //

  @Override
  public String toString() {
    String ret = "\n";
    for (ArrayList<Color> l : board) {
      for (Color c : l) {
        if (c == null) {
          ret += "☐ ";
        } else {
          ret += Utils.getAnsi(c) + Integer.toString(getColorId(c)) + Utils.ANSI_RESET + " ";
        }
      }
      ret += "\n";
    }
    return ret;
  }

}
