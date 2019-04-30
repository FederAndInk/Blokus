package model;

/**
 * Class Coord
 */
public class Coord {

  //
  // Fields
  //

  public int x;
  public int y;

  //
  // Constructors
  //
  public Coord() {
    this(0, 0);
  };

  public Coord(int x, int y) {
    this.x = x;
    this.y = y;
  };

  public Coord(Coord c) {
    x = c.x;
    y = c.y;
  }

  //
  // Methods
  //

  public Coord add(Coord c) {
    Coord tmp = new Coord(this);
    return tmp.add_equal(c);
  }
  
  public Coord add_equal(Coord c) {
    x += c.x;
    y += c.y;
    return this;
  }

  //
  // Accessor methods
  //

  
  //
  // Other methods
  //

}
