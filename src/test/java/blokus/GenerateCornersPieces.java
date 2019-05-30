package blokus;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import blokus.model.Config;
import blokus.model.Piece;
import blokus.model.PieceReader;

/**
 * GenerateCornersPieces
 */
public class GenerateCornersPieces {
    String out;

    public GenerateCornersPieces(String o) {
        this.out = o;
    }

    public static void writeCorners(String file, String pieceList) {
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter("src/test/resources/piecesResources/" + file));
            writer.write(pieceList);
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void generate() {
        ArrayList<Piece> pieces = new ArrayList<>();
        Piece p;
        PieceReader pr = new PieceReader(Config.loadRsc("pieces"));

        while ((p = pr.nextPiece()) != null) {
            pieces.add(p);
        }

        String res = "";
        for (Piece tmp : pieces) {
            res += tmp.toString();
        }
        writeCorners(out, res);
    }

    public static void main(String[] args) {
        GenerateCornersPieces gcp = new GenerateCornersPieces("cornersUp");
        gcp.generate();
    }
}