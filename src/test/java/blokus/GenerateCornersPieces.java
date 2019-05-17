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
    public static void writeCorners(String file, String pieceList) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter("test/pieces/" + file));
        writer.write(pieceList);
        writer.close();
	}

    public static void main(String[] args) throws IOException {
        ArrayList<Piece> pieces = new ArrayList<>();
        Piece p;
        PieceReader pr = new PieceReader(Config.loadRsc("cornersUP"));

        while ((p = pr.nextPiece()) != null) {
            pieces.add(p);
        }

        String res = "";
        for (Piece tmp : pieces) {
            res += tmp.toString();
        }
        writeCorners("cornersUP", res);
        }
    }