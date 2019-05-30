package blokus;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import blokus.model.Config;
import blokus.model.Coord;
import blokus.model.Piece;
import blokus.model.PieceReader;

/**
 * generateDirectionFiles
 */
public class GenerateDirectionFiles {
    public static void writeTransformation(String file, String transformationList) throws IOException {

        BufferedWriter writer = new BufferedWriter(
                new FileWriter("src/test/resources/transformationResources/" + file));
        writer.write(transformationList);
        writer.close();
    }

    public static String toTransformationString(Piece p) {

        String res = "";

        Coord sz = p.computeSize();
        char tab[][] = new char[sz.y][sz.x];
        for (int i = 0; i < tab.length; i++) {
            for (int j = 0; j < tab[i].length; j++) {
                tab[i][j] = '.';
            }
        }
        for (Coord c : p.getShape()) {
            tab[c.y][c.x] = '*';
        }

        for (int i = 0; i < tab.length; i++) {
            for (int j = 0; j < tab[i].length; j++) {
                res += tab[i][j];
            }
            res += "\n";
        }

        res += "\n";

        return res;
    }

    public static void main(String[] args) throws IOException {
        ArrayList<Piece> pieces = new ArrayList<>();
        Piece p;
        PieceReader pr = new PieceReader(Config.loadRsc("pieces"));

        while ((p = pr.nextPiece()) != null) {
            pieces.add(p);
        }

        for (int i = 0; i < 7; i++) {
            String res = "";
            for (Piece tmp : pieces) {
                if (i == 3) {
                    tmp.revertY();
                } else {
                    tmp.right();
                }
                res += toTransformationString(tmp);
            }
            writeTransformation("pieces" + i, res);
        }
    }

}