package blokus.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import blokus.utils.Utils;
import javafx.scene.paint.Color;

/**
 * Class Board
 */
public class Board {
  private int size;

  private static final ArrayList<Color> colors = new ArrayList<>();
  private static final ArrayList<String> colorsName = new ArrayList<>();
  //
  // Fields
  //

  /**
   * y line of x colors
   */
  private byte[] boardColor;
  private byte[] boardPresence;

  private HashMap<Color, ArrayList<Piece>> pieces = new HashMap<>();

  //
  // Constructors
  //
  public Board(int size) {
    this.size = size;
    boardColor = new byte[(int) Math.ceil((getSize() * getSize()) / 4.0)];
    boardPresence = new byte[(int) Math.ceil((getSize() * getSize()) / 8.0)];
    initColors();
  }

  public Board(Board b) {
    size = b.size;
    boardColor = new byte[(int) Math.ceil((getSize() * getSize()) / 4.0)];
    boardPresence = new byte[(int) Math.ceil((getSize() * getSize()) / 8.0)];
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
  }

  /**
   * @return HashMap<Color, Integer>
   */
  public HashMap<Color, Integer> numOfEachColor() {
    HashMap<Color, Integer> map = new HashMap<>();

    for (int y = 0; y < getSize(); ++y) {
      for (int x = 0; x < getSize(); ++x) {
        Color c = get(x, y);
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
      res.add(new Coord(getSize() - 1, 0));
      res.add(new Coord(0, getSize() - 1));
      res.add(new Coord(getSize() - 1, getSize() - 1));
      Iterator<Coord> it = res.iterator();
      for (Coord c = it.next(); it.hasNext(); c = it.next()) {
        if (get(c) != null) {
          it.remove();
        }
      }
    }
    return res;
  }

  //
  // Accessor methods
  //

  /**
   * check bounds of the board
   */
  public boolean isIn(Coord c) {
    return c.x >= 0 && c.y >= 0 && c.x < getSize() && c.y < getSize();
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
    return x + y * getSize();
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
    return c.equals(new Coord(0, 0)) || c.equals(new Coord(0, getSize() - 1)) //
        || c.equals(new Coord(getSize() - 1, 0)) || c.equals(new Coord(getSize() - 1, getSize() - 1));
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

  private static void addColor(Color color, String colorName) {
    colors.add(color);
    colorsName.add(colorName);
  }

  private static void initColors() {
    if (colors.isEmpty()) {
      addColor(Color.BLUE, "Blue");
      addColor(Color.YELLOW, "Yellow");
      addColor(Color.RED, "Red");
      addColor(Color.GREEN, "Green");
    }
  }

  public static String getColorName(Color c) {
    initColors();
    return colorsName.get(getColorId(c));
  }

  public static int getColorId(Color c) {
    initColors();
    return colors.indexOf(c);
  }

  public static Color getColor(int i) {
    initColors();
    return colors.get(i);
  }

  private Color getColor(byte val) {
    return colors.get(val);
  }

  /**
   * @return the size
   */
  public int getSize() {
    return size;
  }

  //
  // Other methods
  //

  @Override
  public String toString() {
    String ret = "\n";
    for (int i = 0; i < getSize(); ++i) {
      for (int j = 0; j < getSize(); j++) {
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
