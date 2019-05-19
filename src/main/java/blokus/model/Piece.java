package blokus.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Class Piece
 */
public class Piece {

  //
  // Fields
  //
  private HashSet<Coord> shape = new HashSet<>();
  private PieceTransform state;
  private ArrayList<PieceTransform> transforms;
  private HashMap<PieceTransform, PieceTransform> transformsMap;

  //
  // Constructors
  //
  public Piece(ArrayList<Coord> shape) {
    this.shape.addAll(shape);
    transforms = new ArrayList<>();
    transformsMap = new HashMap<>();
    state = PieceTransform.UP;
    normalize();
    computeTransformations();
  };

  public Piece(Piece p) {
    for (Coord c : p.shape) {
      shape.add(new Coord(c));
    }
    state = p.state;
    transforms = p.transforms;
    transformsMap = p.transformsMap;
  }

  //
  // Methods
  //

  /**
   *
   * @param c
   * @return the corners of the coord c considering the shape
   */
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

  /**
   *
   * @return the corner where another piece can be put
   */
  public HashSet<Coord> getCorners() {
    HashSet<Coord> corn = new HashSet<>();
    for (Coord c : shape) {
      corn.addAll(getCorners(c));
    }
    return corn;
  }

  public void translate(Coord c) {
    HashSet<Coord> nShape = new HashSet<>();
    for (Coord cT : shape) {
      nShape.add(cT.add_equal(c));
    }
    shape = nShape;
  }

  /**
   * put the piece at (0, 0) post: forall x, y in shape x >= 0, y >=0
   */
  private void normalize() {
    Coord min = new Coord();
    for (Coord c : shape) {
      if (c.x < min.x) {
        min.x = c.x;
      }
      if (c.y < min.y) {
        min.y = c.y;
      }
    }

    translate(min.sub());
  }

  /**
   * compute all different transformation for that piece
   */
  private void computeTransformations() {
    HashMap<Piece, PieceTransform> t = new HashMap<>();

    for (PieceTransform pt : PieceTransform.values()) {
      apply(pt);
      t.putIfAbsent(new Piece(this), state);
      transformsMap.put(pt, t.get(this));
    }
    transforms.addAll(t.values());
    Collections.sort(transforms);
    apply(PieceTransform.UP);
  }

  public PieceTransform mapState() {
    return transformsMap.get(getState());
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

  /**
   * rotate clockwise
   */
  public void right() {
    for (Coord c : shape) {
      int tempX = c.x;
      int tempY = c.y;
      c.x = -tempY;
      c.y = tempX;
    }
    state = state.right();
    normalize();
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
    }
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
    for (Coord c : shape) {
      c.x = -c.x;
    }
    state = state.revertY();
    normalize();
  }

  /**
   * symmetry from x axis
   */
  public void revertX() {
    for (Coord c : shape) {
      c.y = -c.y;
    }
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
  public HashSet<Coord> getShape() {
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
    if (obj instanceof Piece) {
      Piece p = (Piece) obj;
      return shape.equals(p.shape);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return shape.hashCode();
  }
}
