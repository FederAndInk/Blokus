package blokus.model;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;

import org.testng.annotations.Test;

/**
 * TestPiece A TERMINER
 */
public class TestPiece {

    public ArrayList<Piece> loadFile(String fileName) {
        ArrayList<Piece> pieces = new ArrayList<>();
        Piece p;
        PieceReader pr = new PieceReader(Config.loadRsc("transformationResources/" + fileName));

        while ((p = pr.nextPiece()) != null) {
            pieces.add(p);
        }

        return pieces;
    }

    @Test
    public void test_piece() {
        ArrayList<Piece> downToRight = loadFile("piecesDOWN");
        ArrayList<Piece> right = loadFile("piecesRIGHT");
        for (Piece p : downToRight) {
            p.left();
        }
        assertEquals(downToRight, right);

        for (int i = 0; i < right.size(); i++) {
            System.out.println(downToRight.get(i));
            System.out.println(right.get(i));
        }
    }

}