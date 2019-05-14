package blokus.model;

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

  /**
   * make a copy of this and add c
   *
   * @param c
   * @return
   */
  public Coord add(Coord c) {
    Coord tmp = new Coord(this);
    return tmp.add_equal(c);
  }

  /**
   * add c coord to this
   *
   * @param c
   * @return
   */
  public Coord add_equal(Coord c) {
    x += c.x;
    y += c.y;
    return this;
  }

  /**
   * make a copy of this and add +1 in o direction
   *
   * @param c
   * @return
   */
  public Coord add(Direction o) {
    Coord tmp = new Coord(this);
    return tmp.add_equal(o);
  }

  /**
   * add +1 in direction o
   *
   * @param c
   * @return
   */
  public Coord add_equal(Direction o) {
    x += o.x;
    y += o.y;
    return this;
  }

  /**
   * make a copy of this and add +1 in o direction
   *
   * @param c
   * @return
   */
  public Coord add(DiagonalDirection o) {
    Coord tmp = new Coord(this);
    return tmp.add_equal(o);
  }

  /**
   * add +1 in direction o
   *
   * @param c
   * @return
   */
  public Coord add_equal(DiagonalDirection o) {
    return this.add_equal(o.d1).add_equal(o.d2);
  }

  public Coord sub() {
    Coord tmp = new Coord(this);
    tmp.x = -tmp.x;
    tmp.y = -tmp.y;
    return tmp;
  }

  public Coord sub(Coord c) {
    Coord tmp = new Coord(this);
    tmp.x -= c.x;
    tmp.y -= c.y;
    return tmp;
  }

  //
  // Accessor methods
  //

  //
  // Other methods
  //

  @Override
  public String toString() {
    return "(" + x + ", " + y + ")";
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Coord) {
      Coord c = (Coord) obj;
      return x == c.x && y == c.y;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Integer.hashCode(x) + 27 * Integer.hashCode(y);
  }

}
