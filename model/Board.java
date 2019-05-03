package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Observable;

import javafx.scene.paint.Color;

/**
 * Class Board
 */
public class Board extends Observable {
  public static final Coord SIZE = new Coord(20, 20);

  public static final ArrayList<Color> colors = new ArrayList<>();
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
    colors.add(Color.BLUE);
    colors.add(Color.YELLOW);
    colors.add(Color.RED);
    colors.add(Color.GREEN);

    for (int i = 0; i < SIZE.y; i++) {
      board.add(new ArrayList<>());
      for (int j = 0; j < SIZE.x; j++) {
        board.get(i).add(null);
      }
    }
  };

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
      throw new IllegalArgumentException("can't place piece at " + pos + "\npiece:\n" + piece);
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
          cornerCheck = cornerCheck || isCorner(c);
        }
      } else {
        for (Coord corner : piece.getCorners()) {
          cornerCheck = cornerCheck || get(corner.add(pos)) == color;
        }
      }
      ret = ret && cornerCheck;

    } catch (ArrayIndexOutOfBoundsException e) {
      ret = false;
    }
    return ret;
  }

  private boolean canAdd(Coord c, Color color) {
    boolean ret = get(c) == null;
    for (Direction o : Direction.values()) {
      Coord tmp = c.add(o);
      ret = ret && (!isIn(tmp) || get(tmp) != color);
    }

    return ret;
  }

  public HashSet<Coord> getAccCorners(Color color) {
    HashSet<Coord> res = new HashSet<>();
    for (Piece p : pieces.get(color)) {
      for (Coord c : p.getCorners()) {
        if (isIn(c) && get(c) == null) {
          res.add(c);
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
          ret += Integer.toString(getColorId(c)) + " ";
        }
      }
      ret += "\n";
    }
    return ret;
  }

}
