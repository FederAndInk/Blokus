package blokus;

import java.util.ArrayList;

import blokus.model.Config;
import blokus.model.Piece;
import blokus.model.PieceReader;

/**
 * Tools
 */
public class Tools {

    public static ArrayList<Piece> loadFile(String fileName) {
        ArrayList<Piece> pieces = new ArrayList<>();
        Piece p;
        PieceReader pr = new PieceReader(Config.loadRsc("transformationResources/" + fileName));

        while ((p = pr.nextPiece()) != null) {
            pieces.add(p);
        }

        return pieces;
    }
}