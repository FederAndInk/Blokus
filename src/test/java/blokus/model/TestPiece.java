package blokus.model;

import static org.testng.Assert.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import blokus.GenerateCornersPieces;
import blokus.model.Piece;
import blokus.model.PieceReader;

import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.Runtime;

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

    @Test
    public void test_getCorners() {
        GenerateCornersPieces generated = new GenerateCornersPieces("cornersOut");
        generated.generate();
        Process run;
        try {
            run = Runtime.getRuntime().exec("diff cornersOut cornersUp");
            try {
                int diff = run.waitFor();
                assertNotEquals(diff, 1);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void test_computeSize() {
        ArrayList<Piece> piecesUp = loadFile("piecesUP");
        String res = "";
        for (Piece p : piecesUp) {
            res += p.computeSize();
            res += "\n";
        }
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter("computedSizes"));
            writer.write(res);
            writer.close();
            Process run = Runtime.getRuntime().exec("diff computedSizes sizesUP");
            try {
                int diff = run.waitFor();
                assertNotEquals(diff, 1);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}