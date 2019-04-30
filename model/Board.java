package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

import javafx.scene.paint.Color;

/**
 * Class Board
 */
public class Board extends Observable {
  private static final int SIZE = 20;

  //
  // Fields
  //

  private ArrayList<ArrayList<Color>> board;

  //
  // Constructors
  //
  public Board() {
    for (int i = 0; i < SIZE; i++) {
      board.add(new ArrayList<>());
      for (int j = 0; j < SIZE; j++) {
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
  public boolean canAdd(Piece piece, Coord pos) {
    // TODO to complete
    return false;
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
