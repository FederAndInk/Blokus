package blokus.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
  private HashMap<Color, HashSet<Coord>> accCorners = new HashMap<>();

  private GameType gt;

  //
  // Constructors
  //
  public Board(GameType gt) {
    this.gt = gt;
    switch (gt) {
    case BLOKUS:
      this.size = 20;
      break;
    case DUO:
      this.size = 14;
      break;
    }
    boardColor = new byte[getSize() * getSize()];
    boardPresence = new byte[getSize() * getSize()];
    initColors();
  }

  public Board(Board b) {
    size = b.size;
    boardColor = new byte[getSize() * getSize()];
    boardPresence = new byte[getSize() * getSize()];
    System.arraycopy(b.boardColor, 0, boardColor, 0, boardColor.length);

    pieces.putAll(b.pieces);
    accCorners.putAll(b.accCorners);
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
    byte colorId = getColorId(color);
    if (canAdd(piece, pos, color)) {
      piece.translate(pos);
      for (Coord c : piece.getShape()) {
        set(c, colorId);
      }
      pieces.computeIfAbsent(color, (_c) -> {
        return new ArrayList<>();
      }).add(piece);

    } else {
      throw new IllegalArgumentException("can't place " + Utils.getAnsi(color) + getColorName(color) + Utils.ANSI_RESET
          + " piece at " + pos + "\n------------pieces------------:\n" + pieces + "\n------------piece------------:\n"
          + piece + "\n------------Board------------:\n" + this);
    }
  }

  public void remove(Piece piece, Color color) {
    pieces.get(color).remove(piece);
    for (Coord c : piece.getShape()) {
      set(c, (byte) 0);
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
    byte colorId = getColorId(color);
    try {
      ArrayList<Coord> shape = piece.getShape();
      Coord tmp;
      for (int i = 0; ret && i < shape.size(); ++i) {
        tmp = pos.add(shape.get(i));
        ret = ret && canAdd(tmp.x, tmp.y, colorId);
      }
      boolean cornerCheck = false;
      if (isFirst(color)) {
        for (int i = 0; !cornerCheck && i < shape.size(); ++i) {
          tmp = pos.add(shape.get(i));
          cornerCheck = cornerCheck || isFirstCorner(tmp);
        }
      } else {
        ArrayList<Coord> corners = piece.getCorners();
        for (int i = 0; !cornerCheck && i < corners.size(); ++i) {
          Coord corner = pos.add(corners.get(i));
          cornerCheck = cornerCheck || (isIn(corner.x, corner.y) && getId(corner) == colorId);
        }
      }
      ret = ret && cornerCheck;
    } catch (ArrayIndexOutOfBoundsException e) {
      ret = false;
    }
    return ret;
  }

  private boolean canAdd(int x, int y, byte color) {
    boolean ret = isIn(x, y) && getId(x, y) == 0;
    for (int i = -1; i < 2; i += 2) {
      int tmpX = x + i;
      int tmpY = y + i;
      ret = ret && (!isIn(tmpX, y) || getId(tmpX, y) != color);
      ret = ret && (!isIn(x, tmpY) || getId(x, tmpY) != color);
    }
    return ret;
  }

  public Set<Coord> getAccCorners(Color color) {
    if (!accCorners.containsKey(color)) {
      byte colorId = getColorId(color);
      HashSet<Coord> res;
      if (!isFirst(color)) {
        res = new HashSet<>();
        for (Piece p : pieces.get(color)) {
          for (Coord c : p.getCorners()) {
            if (canAdd(c.x, c.y, colorId)) {
              res.add(c);
            }
          }
        }
      } else {
        res = generateFirstCorners();
        Iterator<Coord> it = res.iterator();
        for (Coord c = it.next(); it.hasNext(); c = it.next()) {
          if (getId(c) != 0) {
            it.remove();
          }
        }
      }
      accCorners.put(color, res);
    }
    return Collections.unmodifiableSet(accCorners.get(color));
  }

  private HashSet<Coord> generateFirstCorners() {
    HashSet<Coord> res = new HashSet<>();
    switch (gt) {
    case BLOKUS:
      res.add(new Coord(0, 0));
      res.add(new Coord(getSize() - 1, 0));
      res.add(new Coord(0, getSize() - 1));
      res.add(new Coord(getSize() - 1, getSize() - 1));
      break;
    case DUO:
      res.add(new Coord(4, 4));
      res.add(new Coord(getSize() - 5, getSize() - 5));
      break;
    }
    return res;
  }

  //
  // Accessor methods
  //

  /**
   * check bounds of the board
   */
  public boolean isIn(int x, int y) {
    return x >= 0 && y >= 0 && x < getSize() && y < getSize();
  }

  public Color get(int x, int y) {
    return get(toI(x, y));
  }

  public Color get(Coord pos) {
    return get(pos.x, pos.y);
  }

  private Color get(int i) {
    return getColor(getId(i));
  }

  /**
   * @param i
   * @return 0 no color 1..4: nth color
   */
  private byte getId(int i) {
    return (byte) (boardColor[i]);
  }

  private byte getId(int x, int y) {
    return getId(toI(x, y));
  }

  private byte getId(Coord c) {
    return getId(c.x, c.y);
  }

  private void set(int x, int y, byte c) {
    set(toI(x, y), c);
  }

  private void set(Coord pos, byte c) {
    set(pos.x, pos.y, c);
  }

  private void set(int i, byte c) {
    boardColor[i] = c;
    change();
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
  private boolean isFirstCorner(Coord c) {
    return generateFirstCorners().contains(c);
  }

  /**
   *
   * @param color
   * @return true if color hasn't played before in the board
   */
  private boolean isFirst(Color color) {
    return !pieces.containsKey(color) || pieces.get(color).isEmpty();
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
      addColor(null, "NULL");
      addColor(Color.BLUE, "Bleu");
      addColor(Color.YELLOW, "Jaune");
      addColor(Color.RED, "Rouge");
      addColor(Color.GREEN, "Vert");
    }
  }

  /**
   * @param c
   * @return
   */
  public static String getColorName(Color c) {
    initColors();
    return colorsName.get(getColorId(c));
  }

  /**
   * 0 is null
   * 
   * begins at 1
   */
  public static byte getColorId(Color c) {
    initColors();
    return (byte) colors.indexOf(c);
  }

  /**
   * begins at 1
   * 
   * 0 is null
   */
  public static Color getColor(byte val) {
    initColors();
    return colors.get(val);
  }

  private void change() {
    accCorners.clear();
  }

  /**
   * @return the size
   */
  public int getSize() {
    return size;
  }

  /**
   * @return the pieces
   */
  public HashMap<Color, ArrayList<Piece>> getPieces() {
    return pieces;
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
          ret += "_ ";
        } else {
          ret += Utils.getAnsi(c) + Integer.toString(getColorId(c)) + Utils.ANSI_RESET + " ";
        }
      }
      ret += "\n";
    }
    return ret;
  }
}
