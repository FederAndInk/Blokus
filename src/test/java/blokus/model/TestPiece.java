package blokus.model;

import static org.testng.Assert.*;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import blokus.GenerateCornersPieces;
import blokus.model.Piece;
import blokus.model.PieceReader;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.Runtime;
import java.nio.charset.StandardCharsets;

/**
 * TestPiece A TERMINER
 */
public class TestPiece {

    ArrayList<Piece> piecesUp;
    ArrayList<Piece> piecesRight;
    ArrayList<Piece> piecesDown;
    ArrayList<Piece> piecesLeft;

    ArrayList<Piece> piecesUpReverted;
    ArrayList<Piece> piecesRightReverted;
    ArrayList<Piece> piecesDownReverted;
    ArrayList<Piece> piecesLeftReverted;

    @BeforeClass
    public void loadFiles() {

        piecesUp = loadFile("piecesUP");
        piecesRight = loadFile("piecesRIGHT");
        piecesDown = loadFile("piecesDOWN");
        piecesLeft = loadFile("piecesLEFT");

        piecesUpReverted = loadFile("piecesUP_REVERTED");
        piecesRightReverted = loadFile("piecesRIGHT_REVERTED");
        piecesDownReverted = loadFile("piecesDOWN_REVERTED");
        piecesLeftReverted = loadFile("piecesLEFT_REVERTED");
    }

    public ArrayList<Piece> loadFile(String fileName) {
        ArrayList<Piece> pieces = new ArrayList<>();
        Piece p;
        PieceReader pr = new PieceReader(Config.loadRsc("transformationResources/" + fileName));

        while ((p = pr.nextPiece()) != null) {
            pieces.add(p);
        }

        return pieces;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }

        return result.toString(StandardCharsets.UTF_8.name());

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
            run = Runtime.getRuntime().exec(
                    "diff -u src/test/resources/piecesResources/cornersOut src/test/resources/piecesResources/cornersUp"); // cornersUp

            int diff = run.waitFor();
            System.out.println(convertInputStreamToString(run.getInputStream()));
            System.err.println(convertInputStreamToString(run.getErrorStream()));
            assertEquals(diff, 0);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void test_computeSize() {

        String res = "";
        for (Piece p : piecesUp) {
            res += p.computeSize();
            res += "\n";
        }
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter("src/test/resources/piecesResources/computedSizesUP"));
            writer.write(res);
            writer.close();
            Process run = Runtime.getRuntime()
                    .exec("diff src/test/resources/piecesResources/computedSizesUP src/test/resources/sizesUP"); // sizesUP
            int diff = run.waitFor();
            System.out.println(convertInputStreamToString(run.getInputStream()));
            System.err.println(convertInputStreamToString(run.getErrorStream()));
            assertEquals(diff, 0);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // TODO Changer la façon de tester les pièces, ne tester que les shapes
    // (coordonnées) (pour le
    // left, right et tous les revert) puisque les Directions ont été
    // testées dans TestDirection et
    // TestPieceTransform
    @Test
    public void test_right() {

        ArrayList<Piece> pieces = piecesUp;

        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).right();
            assertTrue(piecesRight.contains(pieces.get(i)), "Piece nr " + i + " to right");
        }
        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).right();
            assertTrue(piecesDown.contains(pieces.get(i)), "Piece nr " + i + " to down");
        }
        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).right();
            assertTrue(piecesLeft.contains(pieces.get(i)), "Piece nr " + i + " to left");
        }
        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).right();
            assertTrue(piecesUp.contains(pieces.get(i)), "Piece nr " + i + " to up");
        }
    }

    @Test
    public void test_left() {

        ArrayList<Piece> pieces = piecesUp;

        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).left();
            assertTrue(piecesLeft.contains(pieces.get(i)), "Piece nr " + i + " to left");
        }
        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).left();
            assertTrue(piecesDown.contains(pieces.get(i)), "Piece nr " + i + " to down");
        }
        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).left();
            assertTrue(piecesRight.contains(pieces.get(i)), "Piece nr " + i + " to right");
        }
        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).left();
            assertTrue(piecesUp.contains(pieces.get(i)), "Piece nr " + i + " to up");
        }
    }

    @Test
    public void test_revertY() {
        ArrayList<Piece> pieces1 = piecesUp;
        ArrayList<Piece> pieces2 = piecesRight;

        for (int i = 0; i < pieces1.size(); i++) {
            pieces1.get(i).revertY();
            assertTrue(piecesUpReverted.contains(pieces1.get(i)), "Piece nr " + i + " to up reverted");
        }
        for (int i = 0; i < pieces1.size(); i++) {
            pieces1.get(i).revertY();
            assertTrue(piecesUp.contains(pieces1.get(i)), "Piece nr " + i + " to up");
        }

        for (int i = 0; i < pieces2.size(); i++) {
            pieces2.get(i).revertY();
            assertTrue(piecesLeftReverted.contains(pieces2.get(i)), "Piece nr " + i + " to left reverted");
        }
        for (int i = 0; i < pieces2.size(); i++) {
            pieces2.get(i).revertY();
            assertTrue(piecesRight.contains(pieces2.get(i)), "Piece nr " + i + " to right");
        }
    }

    @Test
    public void test_revertX() {
        ArrayList<Piece> pieces1 = piecesUp;
        ArrayList<Piece> pieces2 = piecesRight;

        for (int i = 0; i < pieces1.size(); i++) {
            pieces1.get(i).revertX();
            assertTrue(piecesDownReverted.contains(pieces1.get(i)), "Piece nr " + i + " to down reverted");
        }
        for (int i = 0; i < pieces1.size(); i++) {
            pieces1.get(i).revertX();
            assertTrue(piecesUp.contains(pieces1.get(i)), "Piece nr " + i + " to up");
        }

        for (int i = 0; i < pieces2.size(); i++) {
            pieces2.get(i).revertX();
            assertTrue(piecesRightReverted.contains(pieces2.get(i)), "Piece nr " + i + " to right reverted");
        }
        for (int i = 0; i < pieces2.size(); i++) {
            pieces2.get(i).revertX();
            assertTrue(piecesRight.contains(pieces2.get(i)), "Piece nr " + i + " to right");
        }
    }

    @Test
    public void test_normalize() {

        assertTrue(compare_normalize(piecesUp));
        ArrayList<Piece> piecesRight = new ArrayList<Piece>();
        for (Piece p : piecesUp) {
            p.right();
            piecesRight.add(p);
            p.left();
        }
        assertTrue(compare_normalize(piecesRight));
    }

    public boolean compare_normalize(ArrayList<Piece> pieces) {
        boolean res = true;
        for (Piece p : pieces) {
            for (Coord c : p.getShape()) {
                if (c.x < 0 || c.y < 0) {
                    System.out.println(p + "\n" + c);
                    res = false;
                }
            }
        }
        return res;
    }

    @Test
    public void test_revert() {
        ArrayList<Piece> pieces1 = piecesUp;

        for (int i = 0; i < pieces1.size(); i++) {
            pieces1.get(i).revert();
        }
        assertEquals(piecesUpReverted, pieces1);
    }
}