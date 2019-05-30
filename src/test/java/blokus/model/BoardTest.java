package blokus.model;

import java.util.ArrayList;

import org.testng.annotations.Test;

/**
 * BoardTest
 */
public class BoardTest {

  // @Test
  public void board_test() {
    ArrayList<Piece> pieces = new ArrayList<>();
    Board b = new Board(GameType.BLOKUS);

    Piece p;
    PieceReader pr = new PieceReader(Config.loadRsc("pieces"));
    while ((p = pr.nextPiece()) != null) {
      pieces.add(p);
    }
    System.out.println("board");
    System.out.println(b);
    p = pieces.get(1);
    b.add(p, new Coord(0, 0), PColor.BLUE);
    System.out.println("board");
    System.out.println(b);
    p = pieces.get(2);
    b.add(p, new Coord(b.getSize() - 2, b.getSize() - 2), PColor.YELLOW);
    System.out.println("board");
    System.out.println(b);
    p = pieces.get(3);
    b.add(p, new Coord(2, 1), PColor.BLUE);
    System.out.println("board");
    System.out.println(b);
    System.out.println(b.getAccCorners(PColor.BLUE));
    System.out.println(b.getAccCorners(PColor.YELLOW));
    // Test t1 = new Test("test getAccCorners");

    p = pieces.get(5);
    System.out.println(p);

    // Player player = new Player(Color.BLUE, pieces);
    // ArrayList<Move> map = player.whereToPlay(p, b);
    // Piece pTmp;
    // Board bTmp;
    // for (Placement pl : map) {
    // p = pl.piece;
    // p.apply(pl.trans);
    // pTmp = new Piece(p);
    // System.out.println(pTmp);
    // bTmp = new Board(b);
    // bTmp.add(pTmp, pl.pos, Color.BLUE);
    // System.out.println(pTmp);
    // System.out.println(bTmp);
    // }
    // System.out.println(map);
  }
}