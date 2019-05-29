package blokus.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import blokus.utils.Utils;

/**
 * Class Board
 */
public class Board implements Serializable {
  private static final long serialVersionUID = -6102540568979907776L;

  private int size;

  //
  // Fields
  //

  /**
   * y line of x colors
   */
  private byte[] boardColor;

  private HashMap<PColor, ArrayList<Piece>> pieces = new HashMap<>();
  private HashMap<PColor, HashSet<Coord>> accCorners = new HashMap<>();

  private GameType gt;

  //
  // Constructors
  //
  public Board(GameType gt) {
    this.gt = gt;
    switch (gt) {
    case BLOKUS:
      initBoard(20);
      break;
    case DUO:
      initBoard(14);
      break;
    }
  }

  public Board(Board b) {
    this.gt = b.gt;
    initBoard(b.size);
    System.arraycopy(b.boardColor, 0, boardColor, 0, boardColor.length);

    for (Entry<PColor, ArrayList<Piece>> ent : b.pieces.entrySet()) {
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
  public void add(Piece piece, Coord pos, PColor color) {
    byte colorId = color.getId();
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
        set(c.add(pos), PColor.DEBUG.getId());
      }
      throw new IllegalArgumentException("can't place " + Utils.getAnsi(color.primaryColor()) + color.getName()
          + Utils.ANSI_RESET + " piece at " + pos + "\n------------pieces------------:\n" + pieces
          + "\n------------piece------------:\n" + piece + "\n------------Board------------:\n" + this);
    }
  }

  public void remove(Piece piece, PColor color) {
    pieces.get(color).remove(piece);
    for (Coord c : piece.getShape()) {
      set(c, PColor.NO_COLOR.getId());
    }
  }

  /**
   * @return HashMap<PColor, Integer>
   */
  public HashMap<PColor, Integer> numOfEachColor() {
    HashMap<PColor, Integer> map = new HashMap<>();

    for (int y = 0; y < getSize(); ++y) {
      for (int x = 0; x < getSize(); ++x) {
        PColor c = get(x, y);
        if (c.isColor()) {
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
  public boolean canAdd(Piece piece, Coord pos, PColor color) {
    boolean ret = true;
    byte colorId = color.getId();
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
    boolean ret = isIn(x, y) && !PColor.isColor(getId(x, y));
    for (int i = -1; i < 2; i += 2) {
      int tmpX = x + i;
      int tmpY = y + i;
      ret = ret && (!isIn(tmpX, y) || getId(tmpX, y) != color);
      ret = ret && (!isIn(x, tmpY) || getId(x, tmpY) != color);
    }
    return ret;
  }

  public Set<Coord> getAccCorners(PColor color) {
    if (!accCorners.containsKey(color)) {
      byte colorId = color.getId();
      HashSet<Coord> res = new HashSet<>();
      if (hasPlayed(color)) {
        for (Piece p : pieces.get(color)) {
          for (Coord c : p.getCorners()) {
            if (canAdd(c.x, c.y, colorId)) {
              res.add(c);
            }
          }
        }
      } else {
        res.add(generateFirstCorner());
      }
      accCorners.put(color, res);
    }
    return Collections.unmodifiableSet(accCorners.get(color));
  }

  public Set<Coord> getAdvAccCorners(PColor color) {
    HashSet<Coord> ret = new HashSet<>();
    for (PColor c : pieces.keySet()) {
      if (c != color) {
        ret.addAll(getAccCorners(c));
      }
    }
    return ret;
  }

  public ArrayList<Coord> generateFirstCorners() {
    ArrayList<Coord> list = new ArrayList<>();
    switch (gt) {
    case BLOKUS:
      list.add(new Coord(0, 0));
      list.add(new Coord(getSize() - 1, getSize() - 1));
      list.add(new Coord(getSize() - 1, 0));
      list.add(new Coord(0, getSize() - 1));
      break;
    case DUO:
      list.add(new Coord(4, 4));
      list.add(new Coord(getSize() - 5, getSize() - 5));
      break;
    }
    return list;
  }

  private Coord generateFirstCorner() {
    ArrayList<Coord> list = generateFirstCorners();
    Coord m = null;
    for (Iterator<Coord> it = list.iterator(); m == null && it.hasNext();) {
      Coord c = it.next();
      if (!PColor.isColor(getId(c))) {
        m = c;
      }
    }

    return m;
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

  public PColor get(int x, int y) {
    return get(toI(x, y));
  }

  public PColor get(Coord pos) {
    return get(pos.x, pos.y);
  }

  private PColor get(int i) {
    return PColor.get(getId(i));
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
   * return true if c is the next available first corner for a new color to
   * play</br>
   */
  private boolean isFirstCorner(Coord c) {
    Coord gen = generateFirstCorner();
    return c.equals(gen);
  }

  /**
   *
   * @param color
   * @return true if color has played in the board
   */
  public boolean hasPlayed(PColor color) {
    return pieces.containsKey(color) && !pieces.get(color).isEmpty();
  }

  public ArrayList<Piece> getPlayed(PColor c) {
    return pieces.get(c);
  }

  private void initBoard(int size) {
    this.size = size;
    boardColor = new byte[getSize() * getSize()];
    Arrays.fill(boardColor, PColor.NO_COLOR.getId());
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
  public HashMap<PColor, ArrayList<Piece>> getPieces() {
    return pieces;
  }

  /**
   * @return the pieces
   */
  public ArrayList<Piece> getPieces(PColor color) {
    return pieces.get(color);
  }

  /**
   * @return the pieces
   */
  public Piece getPiece(PColor color, int pieceNo) {
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
        PColor c = get(j, i);
        if (!c.isColor()) {
          ret += "_ ";
        } else {
          ret += Utils.getAnsi(c.primaryColor()) + Integer.toString(c.getId()) + Utils.ANSI_RESET + " ";
        }
      }
      ret += "\n";
    }
    return ret;
  }
}
