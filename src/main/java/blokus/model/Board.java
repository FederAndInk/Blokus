package blokus.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
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
  private static final byte NO_COLOR = 4;
  private static final byte DEBUG_COLOR = 5;
  //
  // Fields
  //

  /**
   * y line of x colors
   */
  private byte[] boardColor;

  private HashMap<Color, ArrayList<Piece>> pieces = new HashMap<>();
  private HashMap<Color, HashSet<Coord>> accCorners = new HashMap<>();

  //
  // Constructors
  //
  public Board(int size) {
    this.size = size;
    initBoard();
    initColors();
  }

  public Board(Board b) {
    size = b.size;
    initBoard();
    System.arraycopy(b.boardColor, 0, boardColor, 0, boardColor.length);

    for (Entry<Color, ArrayList<Piece>> ent : b.pieces.entrySet()) {
      pieces.put(ent.getKey(), new ArrayList<>());
      for (Piece p : ent.getValue()) {
        pieces.get(ent.getKey()).add(new Piece(p));
      }
    }
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
      for (Coord c : piece.getShape()) {
        set(c.add(pos), DEBUG_COLOR);
      }
      throw new IllegalArgumentException("can't place " + Utils.getAnsi(color) + getColorName(color) + Utils.ANSI_RESET
          + " piece at " + pos + "\n------------pieces------------:\n" + pieces + "\n------------piece------------:\n"
          + piece + "\n------------Board------------:\n" + this);
    }
  }

  public void remove(Piece piece, Color color) {
    pieces.get(color).remove(piece);
    for (Coord c : piece.getShape()) {
      set(c, NO_COLOR);
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
      if (!hasPlayed(color)) {
        for (int i = 0; !cornerCheck && i < shape.size(); ++i) {
          tmp = pos.add(shape.get(i));
          cornerCheck = cornerCheck || isCorner(tmp);
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
    boolean ret = isIn(x, y) && getId(x, y) == NO_COLOR;
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
      HashSet<Coord> res = new HashSet<>();
      byte colorId = getColorId(color);
      if (hasPlayed(color)) {
        for (Piece p : pieces.get(color)) {
          for (Coord c : p.getCorners()) {
            if (canAdd(c.x, c.y, colorId)) {
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
          if (getId(c) != NO_COLOR) {
            it.remove();
          }
        }
      }
      accCorners.put(color, res);
    }
    return Collections.unmodifiableSet(accCorners.get(color));
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
  private boolean isCorner(Coord c) {
    return (c.x == 0 || c.x == getSize() - 1) && (c.y == 0 || c.y == getSize() - 1);
  }

  /**
   *
   * @param color
   * @return true if color has played in the board
   */
  public boolean hasPlayed(Color color) {
    return pieces.containsKey(color) && !pieces.get(color).isEmpty();
  }

  public ArrayList<Piece> getPlayed(Color c) {
    return pieces.get(c);
  }

  private static void addColor(Color color, String colorName) {
    colors.add(color);
    colorsName.add(colorName);
  }

  private void initBoard() {
    boardColor = new byte[getSize() * getSize()];
    Arrays.fill(boardColor, NO_COLOR);
  }

  private static void initColors() {
    if (colors.isEmpty()) {
      addColor(Color.BLUE, "Blue");
      addColor(Color.YELLOW, "Yellow");
      addColor(Color.RED, "Red");
      addColor(Color.GREEN, "Green");
      addColor(null, "NULL");
      addColor(Color.BLUEVIOLET, "Debug color");
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
   */
  public static byte getColorId(Color c) {
    initColors();
    return (byte) colors.indexOf(c);
  }

  /**
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

  /**
   * @return the pieces
   */
  public ArrayList<Piece> getPieces(Color color) {
    return pieces.get(color);
  }

  /**
   * @return the pieces
   */
  public Piece getPiece(Color color, int pieceNo) {
    return Utils.findIf(pieces.get(color), (p) -> {
      return p.no == pieceNo;
    });
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
