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

  //
  // Methods
  //

  public Piece(Piece p) {
    for (Coord c : p.shape) {
      shape.add(new Coord(c));
    }
  }

  public void add(Coord c) {
    shape.add(c);
  }

  /**
   * return the shape with orientation and reverted applied
   */
  public HashSet<Coord> getShape() {
    // TODO complete
    return shape;
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
  // TODO revertX et revertY ne marche pas
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
    char tab[][] = new char[10][10];
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        tab[i][j] = ' ';
      }
    }
    for (Coord c : shape) {
      tab[c.y + 5][c.x + 5] = '█';

    }
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
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
