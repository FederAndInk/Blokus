package blokus.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import javafx.scene.paint.Color;

/**
 * RandomComputer
 */
public class RandomComputer extends APlayer {

    Random r = new Random();

    public RandomComputer(Color color, ArrayList<Piece> pieces) {
        super(color, pieces);
    }

    @Override
    public void completeMove(Board board) {
        Move m = null;

        if (!hasToPass(board)) {
            Coord pos = new Coord(0, 0);
            Piece piece = null;
            HashMap<PieceTransform, HashSet<Coord>> possiblePlacements;
            do {
                piece = getPieces().get(r.nextInt(getPieces().size()));
                possiblePlacements = board.whereToPlay(piece, getColor());
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
