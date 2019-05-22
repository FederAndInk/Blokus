package blokus.model;

import static org.testng.Assert.*;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import blokus.GenerateCornersPieces;
import blokus.model.Piece;
import blokus.model.PieceReader;

import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.Runtime;
import java.nio.charset.StandardCharsets;

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
                    "diff src/test/resources/piecesResources/cornersOut src/test/resources/piecesResources/cornersUp"); // cornersUp

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
        ArrayList<Piece> piecesUp = loadFile("piecesUP");
        String res = "";
        for (Piece p : piecesUp) {
            res += p.computeSize();
            res += "\n";
        }
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter("src/test/resources/piecesResources/computedSizes"));
            writer.write(res);
            writer.close();
            Process run = Runtime.getRuntime()
                    .exec("diff src/test/resources/piecesResources/computedSizes src/test/resources/sizesUP"); // sizesUP
            int diff = run.waitFor();
            System.out.println(convertInputStreamToString(run.getInputStream()));
            System.err.println(convertInputStreamToString(run.getErrorStream()));
            assertEquals(diff, 0);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}