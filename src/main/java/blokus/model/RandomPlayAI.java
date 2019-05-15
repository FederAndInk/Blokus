package blokus.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javafx.scene.paint.Color;

/**
 * RandomPlayAI
 */
public class RandomPlayAI extends APlayer {
    Random r = new Random();
    PieceChooser pc;

    public RandomPlayAI(Color color, ArrayList<Piece> pieces, PieceChooser pieceC) {
        super(color, pieces);
        pc = pieceC;

    }

    class Placement {
        Piece piece;
        PieceTransform trans;
        Coord pos;

        public Placement(Piece p, PieceTransform tr, Coord c) {
            piece = p;
            trans = tr;
            pos = c;
        }

    }

    @Override
    public Move completeMove(Board board) {
        Move m = null;
        ArrayList<Placement> placements = new ArrayList<>();
        HashMap<Piece, HashMap<PieceTransform, HashSet<Coord>>> res = whereToPlayAll(board);
        if (!res.isEmpty()) {
            for (Piece p : res.keySet()) {
                HashMap<PieceTransform, HashSet<Coord>> res2 = res.get(p);
                Set<PieceTransform> transformations = res2.keySet();
                for (PieceTransform tr : transformations) {
                    HashSet<Coord> coords = res2.get(tr);
                    for (Coord c : coords) {
                        Placement temp = new Placement(p, tr, c);
                        placements.add(temp);
                    }
                }
            }

            Placement rand = pc.pickPlacement(placements);
            rand.piece.apply(rand.trans);
            m = new Move(this, rand.piece, board, rand.pos);
        } else {
            System.out.println(this + " can't play");
        }
        return m;
    }
}