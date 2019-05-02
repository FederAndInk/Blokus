package model;

import java.util.ArrayList;

/**
 * Class Piece
 */
public class Piece {

  //
  // Fields
  //
  private ArrayList<Coord> shape = new ArrayList<>();

  //
  // Constructors
  //
  public Piece() {
  };

  //
  // Methods
  //

  public void add(Coord c) {
    shape.add(c);
  }

  /**
   * return the shape with orientation and reverted applied
   */
  public ArrayList<Coord> getShape() {
    // TODO complete
    return shape;
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

}
