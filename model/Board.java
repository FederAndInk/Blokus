package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

import javafx.scene.paint.Color;

/**
 * Class Board
 */
public class Board extends Observable {
  public static final Coord SIZE = new Coord(20, 20);

  //
  // Fields
  //

  /**
   * y line of x colors
   */
  private ArrayList<ArrayList<Color>> board = new ArrayList<>();

  //
  // Constructors
  //
  public Board() {
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
      for (Coord c : piece.getShape()) {
        set(pos.add(c), color);
      }
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
        map.put(c, map.getOrDefault(c, 0) + 1);
      }
    }

    return map;
  }

  /**
   * @return undef
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
    } catch (ArrayIndexOutOfBoundsException e) {
      ret = false;
    }
    return ret;
  }

  private boolean canAdd(Coord c, Color color) {
    boolean ret = get(c) == null;
    for (Direction o : Direction.values()) {
      ret = ret && get(c.add(o)) != color;
    }

    // FIXME: do this for the all piece at corners
    boolean hasColorNear = false;
    for (DiagonalDirection dd : DiagonalDirection.values()) {
      hasColorNear = hasColorNear || get(c.add(dd)) == color;
    }

    return ret;
  }

  //
  // Accessor methods
  //
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

  //
  // Other methods
  //

}
