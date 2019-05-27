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

    ArrayList<Coord> coordPiece;
    ArrayList<Coord> coordRight;
    ArrayList<Coord> coordRightReverted;
    ArrayList<Coord> coordDown;
    ArrayList<Coord> coordDownReverted;
    ArrayList<Coord> coordLeft;
    ArrayList<Coord> coordLeftReverted;
    ArrayList<Coord> coordUp;
    ArrayList<Coord> coordUpReverted;

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
        ArrayList<Piece> pieces = new ArrayList<Piece>();
        for (Piece p : piecesUp) {
            pieces.add(new Piece(p));
        }
        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).right();
            compareShapes(pieces.get(i), piecesRight.get(i), "right");
        }
        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).right();
            compareShapes(pieces.get(i), piecesDown.get(i), "down");
        }
        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).right();
            compareShapes(pieces.get(i), piecesLeft.get(i), "left");
        }
        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).right();
            compareShapes(pieces.get(i), piecesUp.get(i), "up");
        }
    }

    @Test
    public void test_left() {
        ArrayList<Piece> pieces = new ArrayList<Piece>();
        for (Piece p : piecesUp) {
            pieces.add(new Piece(p));
        }
        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).left();
            compareShapes(pieces.get(i), piecesLeft.get(i), "left");
        }
        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).left();
            compareShapes(pieces.get(i), piecesDown.get(i), "down");
        }
        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).left();
            compareShapes(pieces.get(i), piecesRight.get(i), "right");
        }
        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).left();
            compareShapes(pieces.get(i), piecesUp.get(i), "up");
        }
    }

    @Test
    public void test_revertY() {
        ArrayList<Piece> pieces = new ArrayList<Piece>();
        for (Piece p : piecesUp) {
            pieces.add(new Piece(p));
        }
        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).revertY();
            compareShapes(pieces.get(i), piecesUpReverted.get(i), "up reverted");
        }

        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).revertY();
            compareShapes(pieces.get(i), piecesUp.get(i), "up");
        }
        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).right();
            pieces.get(i).revertY();
            compareShapes(pieces.get(i), piecesLeftReverted.get(i), "left reverted");
        }
        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).revertY();
            compareShapes(pieces.get(i), piecesRight.get(i), "right");
        }
        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).right();
            pieces.get(i).revertY();
            compareShapes(pieces.get(i), piecesDownReverted.get(i), "down reverted");
        }
        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).revertY();
            compareShapes(pieces.get(i), piecesDown.get(i), "down");
        }
        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).right();
            pieces.get(i).revertY();
            compareShapes(pieces.get(i), piecesRightReverted.get(i), "right reverted");
        }
        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).revertY();
            compareShapes(pieces.get(i), piecesLeft.get(i), "left");
        }
    }

    @Test
    public void test_revertX() {
        ArrayList<Piece> pieces = new ArrayList<Piece>();
        for (Piece p : piecesUp) {
            pieces.add(new Piece(p));
        }
        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).revertX();
            compareShapes(pieces.get(i), piecesDownReverted.get(i), "down reverted");
        }

        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).revertX();
            compareShapes(pieces.get(i), piecesUp.get(i), "up");
        }

        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).right();
            pieces.get(i).revertX();
            compareShapes(pieces.get(i), piecesRightReverted.get(i), "right reverted");
        }
        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).revertX();
            compareShapes(pieces.get(i), piecesRight.get(i), "right");
        }
        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).right();
            pieces.get(i).revertX();
            compareShapes(pieces.get(i), piecesUpReverted.get(i), "up reverted");

        }
        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).revertX();
            compareShapes(pieces.get(i), piecesDown.get(i), "down");
        }
        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).right();
            pieces.get(i).revertX();
            compareShapes(pieces.get(i), piecesLeftReverted.get(i), "left reverted");

        }
        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).revertX();
            compareShapes(pieces.get(i), piecesLeft.get(i), "left");
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

    public void compareShapes(Piece piece, Piece pieceRef, String message) {
        Piece init = new Piece(piece);
        ArrayList<Coord> coordRef = pieceRef.getShape();
        ArrayList<Coord> coordPiece = piece.getShape();
        assertEquals(coordRef.size(), coordPiece.size(),
                "Piece " + piece + " turned " + message + " : different amount of coordinates");

        for (Coord coord : coordPiece) {
            assertTrue(coordRef.contains(coord), "Piece " + init + " to " + message + " : " + coord
                    + " shouldn't be present" + " expected : " + pieceRef + " and found : " + piece);
        }
    }

    @Test
    public void test_revert() {
        ArrayList<Piece> pieces = new ArrayList<Piece>();
        for (Piece p : piecesUp) {
            pieces.add(new Piece(p));
        }

        for (int i = 0; i < pieces.size(); i++) {
            pieces.get(i).revert();
        }
        assertEquals(piecesUpReverted, pieces);
    }

    @Test
    public void test_getTransforms() {
        Piece p = piecesUp.get(0);
        assertEquals(p.getTransforms().size(), 1, "piece 0 should have one transformation");
        showAllTrans(p);

        p = piecesUp.get(15);
        assertEquals(p.getTransforms().size(), 8, "piece 15 should have eight transformations");
        showAllTrans(p);

        p = piecesUp.get(1);
        assertEquals(p.getTransforms().size(), 2, "piece 1 should have two transformations");
        showAllTrans(p);
    }

    void showAllTrans(Piece p) {
        int noTrans = 1;
        System.out.println("\n~~~~~~~~~~~~~~~Transformations~~~~~~~~~~~~~~~");
        for (PieceTransform pt : p.getTransforms()) {
            System.out.println("transformation no " + noTrans + ":");
            p.apply(pt);
            System.out.println(p);
            ++noTrans;
        }
    }
}