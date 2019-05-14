package blokus.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Observable;

import blokus.utils.Utils;
import javafx.scene.paint.Color;

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
  private byte[] boardColor = new byte[(int) Math.ceil((SIZE.y * SIZE.x) / 4.0)];
  private byte[] boardPresence = new byte[(int) Math.ceil((SIZE.y * SIZE.x) / 8.0)];

  private HashMap<Color, ArrayList<Piece>> pieces = new HashMap<>();

  //
  // Constructors
  //
  public Board() {
    addColor(Color.BLUE, "Blue");
    addColor(Color.YELLOW, "Yellow");
    addColor(Color.RED, "Red");
    addColor(Color.GREEN, "Green");
  }

  public Board(Board b) {
    System.arraycopy(b.boardColor, 0, boardColor, 0, boardColor.length);
    System.arraycopy(b.boardPresence, 0, boardPresence, 0, boardPresence.length);

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
      throw new IllegalArgumentException("can't place " + Utils.getAnsi(color) + getColorName(color) + Utils.ANSI_RESET
          + " piece at " + pos + "\npiece:\n" + piece);
    }

    setChanged();
    notifyObservers();
  }

  /**
   * @return HashMap<Color, Integer>
   */
  public HashMap<Color, Integer> numOfEachColor() {
    HashMap<Color, Integer> map = new HashMap<>();

    for (int i = 0; i < boardColor.length; ++i) {
      Color c = get(i);
      if (c != null) {
        map.put(c, map.getOrDefault(c, 0) + 1);
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
  public boolean isIn(Coord c) {
    return c.x >= 0 && c.y >= 0 && c.x < SIZE.x && c.y < SIZE.y;
  }

  public Color get(int x, int y) {
    return get(toI(x, y));
  }

  public Color get(Coord pos) {
    return get(pos.x, pos.y);
  }

  private Color get(int i) {
    if (Utils.get(boardPresence[i / 8], i % 8) == 1) {
      return getColor(Utils.get2(boardColor[i / 4], i % 4));
    } else {
      return null;
    }
  }

  public void set(int x, int y, Color c) {
    set(toI(x, y), c);
  }

  public void set(Coord pos, Color c) {
    set(pos.x, pos.y, c);
  }

  private void set(int i, Color c) {
    if (c != null) {
      boardColor[i / 4] = Utils.set2(boardColor[i / 4], i % 4, getColorId(c));
    }
    boardPresence[i / 8] = Utils.set(boardPresence[i / 8], i % 8, c == null ? 0 : 1);
  }

  private int toI(int x, int y) {
    return x + y * SIZE.x;
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

  public ArrayList<Piece> getPlayed(Color c) {
    return pieces.get(c);
  }

  public void addColor(Color color, String colorName) {
    colors.add(color);
    colorsName.add(colorName);
  }

  public static String getColorName(Color c) {
    return colorsName.get(getColorId(c));
  }

  private static int getColorId(Color c) {
    return colors.indexOf(c);
  }

  private Color getColor(byte val) {
    return colors.get(val);
  }

  //
  // Other methods
  //

  @Override
  public String toString() {
    String ret = "\n";
    for (int i = 0; i < SIZE.y; ++i) {
      for (int j = 0; j < SIZE.x; j++) {
        Color c = get(j, i);
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
