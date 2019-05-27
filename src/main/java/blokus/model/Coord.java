package blokus.model;

import java.io.Serializable;

/**
 * Class Coord
 */
public class Coord implements Serializable {

  //
  // Fields
  //

  private static final long serialVersionUID = 3981798511029941297L;
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

  public Coord set(Coord other) {
    x = other.x;
    y = other.y;
    return this;
  }

  /**
   * make a copy of this and add c</br>
   * this + c;
   * @param c
   * @return
   */
  public Coord add(Coord c) {
    Coord tmp = new Coord(this);
    return tmp.add_eq(c);
  }

  /**
   * add c coord to this</br>
   * this += c;
   * @param c
   * @return
   */
  public Coord add_eq(Coord c) {
    x += c.x;
    y += c.y;
    return this;
  }

  /**
   * make a copy of this and add +1 in o direction</br>
   * this + o;
   * @param c
   * @return
   */
  public Coord add(Direction o) {
    Coord tmp = new Coord(this);
    return tmp.add_eq(o);
  }

  /**
   * add +1 in direction o</br>
   * this += c;
   * @param c
   * @return
   */
  public Coord add_eq(Direction o) {
    x += o.x;
    y += o.y;
    return this;
  }

  /**
   * make a copy of this and add +1 in o direction</br>
   * this + o;
   * @param c
   * @return
   */
  public Coord add(DiagonalDirection o) {
    Coord tmp = new Coord(this);
    return tmp.add_eq(o);
  }

  /**
   * add +1 in direction o</br>
   * this += o;
   * @param c
   * @return
   */
  public Coord add_eq(DiagonalDirection o) {
    return this.add_eq(o.d1).add_eq(o.d2);
  }

  /**
   * this -= c;
   * @param c
   * @return
   */
  public Coord sub_eq(Coord c) {
    x -= c.x;
    y -= c.y;
    return this;
  }

  /**
   * this = -this;
   * @return
   */
  public Coord sub_eq() {
    x = -x;
    y = -y;
    return this;
  }

  /**
   * this - c;
   * @param c
   * @return
   */
  public Coord sub(Coord c) {
    Coord tmp = new Coord(this);
    return tmp.sub_eq(c);
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
