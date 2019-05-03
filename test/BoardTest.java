package test;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import model.Board;
import model.Config;
import model.Coord;
import model.Piece;
import model.PieceReader;

/**
 * BoardTest
 */
public class BoardTest {

  public static void main(String[] args) {
    ArrayList<Piece> pieces = new ArrayList<>();
    Board b = new Board();

    Piece p;
    PieceReader pr = new PieceReader(Config.loadRsc("pieces"));
    while ((p = pr.nextPiece()) != null) {
      pieces.add(p);
    }
    System.out.println("board");
    System.out.println(b);
    p = pieces.get(1);
    b.add(p, new Coord(0, 0), Color.BLUE);
    System.out.println("board");
    System.out.println(b);
    p = pieces.get(2);
    b.add(p, new Coord(18, 0), Color.YELLOW);
    System.out.println("board");
    System.out.println(b);
    p = pieces.get(3);
    b.add(p, new Coord(2, 1), Color.BLUE);
    System.out.println("board");
    System.out.println(b);
  }
}