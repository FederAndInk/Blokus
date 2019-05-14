package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import javafx.scene.paint.Color;

/**
 * RandomPieceAI
 */
public class RandomPieceAI extends APlayer {

    Random r = new Random();
    PieceChooser pc;

    public RandomPieceAI(Color color, ArrayList<Piece> pieces, PieceChooser pieceC) {
        super(color, pieces);
        pc = pieceC;

    }

    @Override
    public void completeMove(Board board) {
        Move m = null;
        HashMap<Piece, HashMap<PieceTransform, HashSet<Coord>>> res = whereToPlayAll(board);
        if (!res.isEmpty()) {
            Coord pos = new Coord(0, 0);
            Piece piece = null;
            HashMap<PieceTransform, HashSet<Coord>> possiblePlacements = new HashMap<>();
            ArrayList<Piece> piecesTmp = new ArrayList<>(getPieces());
            do {
                piece = pc.pickPiece(piecesTmp);
                piecesTmp.remove(piece);

                if (res.containsKey(piece)) {
                    possiblePlacements = res.get(piece);
                }
            } while (possiblePlacements.isEmpty());
            PieceTransform pt = (PieceTransform) possiblePlacements.keySet().toArray()[r
                    .nextInt(possiblePlacements.keySet().size())];
            piece.apply(pt);
            pos = (Coord) possiblePlacements.get(pt).toArray()[r.nextInt(possiblePlacements.get(pt).size())];

            m = new Move(this, piece, board, pos);
            setChanged();
            notifyObservers(m);
        } else {
            System.out.println(this + " can't play");
        }
    }
}