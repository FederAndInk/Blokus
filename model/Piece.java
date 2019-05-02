package model;

import java.util.HashSet;

/**
 * Class Piece
 */
public class Piece {

  //
  // Fields
  //
  private HashSet<Coord> shape = new HashSet<>();

  //
  // Constructors
  //
  public Piece() {
  };

  public Piece(Piece p) {
    for (Coord c : p.shape) {
      shape.add(new Coord(c));
    }
  }

  //
  // Methods
  //

  public void add(Coord c) {
    shape.add(c);
  }

  /**
   * return the shape with orientation and reverted applied
   */
  public HashSet<Coord> getShape() {
    return shape;
  }

  public HashSet<Coord> getCorners(Coord c) {
    if (!shape.contains(c)) {
      throw new IllegalArgumentException("coord " + c + " isn't in piece");
    }
    HashSet<Coord> corn = new HashSet<>();
    for (DiagonalDirection dd : DiagonalDirection.values()) {
      if (!(shape.contains(c.add(dd.d1)) || shape.contains(c.add(dd.d2)))) {
        corn.add(c.add(dd));
      }
    }

    return corn;
  }

  public HashSet<Coord> getCorners() {
    HashSet<Coord> corn = new HashSet<>();
    for (Coord c : shape) {
      corn.addAll(getCorners(c));
    }
    return corn;
  }

  public boolean isEmpty() {
    return shape.isEmpty();
  }

  //
  // Accessor methods
  //

  /**
   * rotate clockwise
   */
  public void right() {
    for (Coord c : shape) {
      int tempX = c.x;
      int tempY = c.y;
      c.x = -tempY;
      c.y = tempX;
      System.out.println("x=" + c.x + " y=" + c.y);
    }
  }

  /**
   * rotate counter-clockwise
   */
  public void left() {
    for (Coord c : shape) {
      int tempX = c.x;
      int tempY = c.y;
      c.x = tempY;
      c.y = -tempX;
      System.out.println("x=" + c.x + " y=" + c.y);
    }
  }

  /**
   * symmetry from y axis
   */
  public void revertY() {
    for (Coord c : shape) {
      c.y = -c.y;
      System.out.println("x=" + c.x + " y=" + c.y);
    }

  }

  /**
   * symmetry from x axis
   */
  public void revertX() {
    for (Coord c : shape) {
      c.x = -c.x;
      System.out.println("x=" + c.x + " y=" + c.y);
    }

  }

  //
  // Other methods
  //

  @Override
  public String toString() {
    String res = "";
    char tab[][] = new char[15][15];
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        tab[i][j] = ' ';
      }
    }
    for (Coord c : shape) {
      tab[c.y + 5][c.x + 5] = '█';
    }
    for (Coord c : getCorners()) {
      tab[c.y + 5][c.x + 5] = '*';
    }

    for (int i = 0; i < tab.length; i++) {
      for (int j = 0; j < tab[i].length; j++) {
        res += tab[i][j] + " ";
      }
      res += "\n";
    }

    return res;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Piece) {
      Piece p = (Piece) obj;
      return shape.equals(p.shape);
    }
    return false;
  }

}
