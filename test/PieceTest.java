package test;

import java.io.InputStream;

import model.Config;
import model.Coord;
import model.Piece;
import model.PieceReader;

/**
 * PieceTest
 */
public class PieceTest {

  public static void main(String[] args) {
    Piece p = new Piece();
    System.out.println("test piece:");
    p.add(new Coord(0, 0));
    p.add(new Coord(0, 1));
    p.add(new Coord(0, 2));
    p.add(new Coord(1, 0));
    p.add(new Coord(2, 0));
    System.out.println(p.toString());
    p.right();
    System.out.println(p.toString());
    p.left();
    p.left();
    System.out.println(p.toString());
    p.revertY();
    System.out.println(p.toString());
    p.revertX();
    System.out.println(p.toString());
    p.revertX();
    System.out.println(p.toString());
    p.revertY();
    System.out.println(p.toString());

    System.out.println("Test piece reader:");
    InputStream is = Config.loadRsc("pieces");
    PieceReader pr = new PieceReader(is);

    while ((p = pr.nextPiece()) != null) {
      System.out.println(p);
    }
  }
}
