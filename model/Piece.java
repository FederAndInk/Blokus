package model;

import java.util.ArrayList;

/**
 * Class Piece
 */
public class Piece {

  //
  // Fields
  //

  private boolean reverted = false;
  private Orientation orientation = Orientation.UP;
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
    //TODO complete
    return null;
  }

  //
  // Accessor methods
  //

  /**
   * rotate clockwise
   */
  public void right() {
    orientation = orientation.right();
  }

  /**
   * rotate counter-clockwise
   */
  public void left() {
    orientation = orientation.left();
  }

  /**
   */
  public void revert() {
    reverted = !reverted;
  }

  //
  // Other methods
  //

}
