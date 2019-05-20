package blokus.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Class Piece
 */
public class Piece {

  //
  // Fields
  //
  private ArrayList<Coord> shape = new ArrayList<>();
  private ArrayList<Coord> corners = new ArrayList<>();
  private PieceTransform state;
  private ArrayList<PieceTransform> transforms;
  public final int no;

  //
  // Constructors
  //
  public Piece(int no, ArrayList<Coord> shape) {
    this.shape.addAll(shape);
    this.no = no;
    transforms = new ArrayList<>();
    state = PieceTransform.UP;
    normalize();
    computeTransformations();
    computeCorners();
  };

  public Piece(Piece p) {
    for (Coord c : p.shape) {
      shape.add(new Coord(c));
    }
    for (Coord c : p.corners) {
      corners.add(new Coord(c));
    }
    no = p.no;
    state = p.state;
    transforms = p.transforms;
  }

  //
  // Methods
  //


  /**
   *
   * @return the corner where another piece can be put
   */
  public ArrayList<Coord> getCorners() {
    return corners;
  }

  private void computeCorners() {
    HashSet<Coord> corn = new HashSet<>();
    for (Coord c : shape) {
      for (DiagonalDirection dd : DiagonalDirection.values()) {
        if (!(shape.contains(c.add(dd.d1)) || shape.contains(c.add(dd.d2)))) {
          corn.add(c.add(dd));
        }
      }
    }
    corners.addAll(corn);
  }

  public void translate(Coord c) {
    for_each((cT) -> {
      cT.add_eq(c);
    });
  }

  /**
   * put the piece at (0, 0) post: forall x, y in shape x >= 0, y >=0
   */
  public void normalize() {
    Coord min = new Coord(Integer.MAX_VALUE, Integer.MAX_VALUE);
    for (Coord c : shape) {
      if (c.x < min.x) {
        min.x = c.x;
      }
      if (c.y < min.y) {
        min.y = c.y;
      }
    }

    translate(min.sub_eq());
  }

  /**
   * compute all different transformation for that piece
   */
  private void computeTransformations() {
    HashMap<Piece, PieceTransform> t = new HashMap<>();

    for (PieceTransform pt : PieceTransform.values()) {
      apply(pt);
      t.putIfAbsent(new Piece(this), state);
    }
    transforms.addAll(t.values());
    Collections.sort(transforms);
    apply(PieceTransform.UP);
  }

  public Coord computeSize() {
    Coord sz = new Coord();
    for (Coord c : shape) {
      if (c.x > sz.x) {
        sz.x = c.x;
      }
      if (c.y > sz.y) {
        sz.y = c.y;
      }
    }
    ++sz.x;
    ++sz.y;
    return sz;
  }

  public void apply(PieceTransform pt) {
    setDirection(pt.dir);
    setReverted(pt.reverted);
  }

  //
  // Accessor methods
  //

  private void for_each(Loop l) {
    for (Coord c : shape) {
      l.loop(c);
    }
    for (Coord c : corners) {
      l.loop(c);
    }
  }

  /**
   * rotate clockwise
   */
  public void right() {
    for_each((c) -> {
      int tmp = c.x;
      c.x = -c.y;
      c.y = tmp;
    });
    state = state.right();
    normalize();
  }

  /**
   * rotate counter-clockwise
   */
  public void left() {
    for_each((c) -> {
      int tmp = c.x;
      c.x = c.y;
      c.y = -tmp;
    });
    state = state.left();
    normalize();
  }

  /**
   * @param dir the dir to set
   */
  public void setDirection(Direction dir) {
    while (getDirection() != dir) {
      right();
    }
  }

  /**
   * @param reverted the reverted to set
   */
  public void setReverted(boolean reverted) {
    if (isReverted() != reverted) {
      revert();
    }
  }

  /**
   * revert the piece without affecting the direction
   */
  public void revert() {
    switch (getDirection()) {
    case UP:
    case DOWN:
      revertY();
      break;
    case LEFT:
    case RIGHT:
      revertX();
      break;
    }
  }

  /**
   * symmetry from y axis
   */
  public void revertY() {
    for_each((c) -> {
      c.x = -c.x;
    });
    state = state.revertY();
    normalize();
  }

  /**
   * symmetry from x axis
   */
  public void revertX() {
    for_each((c) -> {
      c.y = -c.y;
    });
    state = state.revertX();
    normalize();
  }

  /**
   * @return true if there is no shape
   */
  public boolean isEmpty() {
    return shape.isEmpty();
  }

  /**
   * return the shape with orientation and reverted applied
   */
  public ArrayList<Coord> getShape() {
    return shape;
  }

  /**
   * @return the transforms
   */
  public ArrayList<PieceTransform> getTransforms() {
    return transforms;
  }

  /**
   * @return the dir
   */
  public Direction getDirection() {
    return state.dir;
  }

  /**
   * @return the reverted
   */
  public boolean isReverted() {
    return state.reverted;
  }

  /**
   * @return the state
   */
  public PieceTransform getState() {
    return state;
  }

  public int size() {
    return shape.size();
  }

  //
  // Other methods
  //

  @Override
  public String toString() {
    String res = "\n";
    res += getDirection();
    if (isReverted()) {
      res += " reverted";
    }
    res += "\n";
    Coord sz = computeSize();
    char tab[][] = new char[sz.y + 2][sz.x + 2];
    for (int i = 0; i < tab.length; i++) {
      for (int j = 0; j < tab[i].length; j++) {
        tab[i][j] = ' ';
      }
    }
    for (Coord c : shape) {
      tab[c.y + 1][c.x + 1] = '█';
    }
    for (Coord c : getCorners()) {
      tab[c.y + 1][c.x + 1] = '*';
    }

    for (int i = 0; i < tab.length; i++) {
      for (int j = 0; j < tab[i].length; j++) {
        res += tab[i][j] + " ";
      }
      res += "\n";
    }

    res += "coords:\n";
    for (Coord c : shape) {
      res += c + "\n";
    }

    return res;
  }

  @Override
  public boolean equals(Object obj) {
    boolean ret = true;
    if (obj instanceof Piece) {
      Piece p = (Piece) obj;
      if (shape.size() == p.shape.size()) {
        for (int i = 0; ret && i < shape.size(); i++) {
          ret = ret && p.shape.contains(shape.get(i));
        }
        return ret;
      }
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 0;
    Iterator<Coord> i = shape.iterator();
    while (i.hasNext()) {
      Coord obj = i.next();
      if (obj != null)
        h += obj.hashCode();
    }
    return h;
  }

  /**
   * Loop
   */
  private interface Loop {
    void loop(Coord c);
  }
}
