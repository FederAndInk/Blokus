package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javafx.scene.paint.Color;
import model.Board;
import model.Config;
import model.Coord;
import model.Piece;
import model.PieceReader;
import model.PieceTransform;

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
    System.out.println(b.getAccCorners(Color.BLUE));
    System.out.println(b.getAccCorners(Color.YELLOW));
    // Test t1 = new Test("test getAccCorners");

    p = pieces.get(5);
    System.out.println(p);

    Color player = Color.RED;
    HashMap<PieceTransform, HashSet<Coord>> map = b.whereToPlay(p, player);
    Piece pTmp;
    Board bTmp;
    for (PieceTransform pt : map.keySet()) {
      p.apply(pt);
      for (Coord c : map.get(pt)) {
        pTmp = new Piece(p);
        System.out.println(pTmp);
        bTmp = new Board(b);
        bTmp.add(pTmp, c, player);
        System.out.println(pTmp);
        System.out.println(bTmp);
      }
    }
    System.out.println(map);
  }
}