package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import javafx.scene.paint.Color;

/**
 * RandomComputer
 */
public class RandomComputer extends APlayer {

    Random r;

    public RandomComputer(Color color, ArrayList<Piece> pieces) {
        super(color, pieces);
    }

    @Override
    public void completeMove(Board board) {
        Piece piece = this.getPieces().get(0);
        Coord pos = new Coord(0, 0);

        if (!hasToPass(board)) {
            HashMap<PieceTransform, HashSet<Coord>> possiblePlacements;
            do {
                piece = this.getPieces().get(r.nextInt(this.getPieces().size()));
                possiblePlacements = board.whereToPlay(piece, this.getColor());
            } while (possiblePlacements.isEmpty());
            PieceTransform pt = possiblePlacements.keySet().toArray(new PieceTransform[0])[r
                    .nextInt(possiblePlacements.keySet().size())];
            piece.apply(pt);
            pos = (Coord) possiblePlacements.get(pt).toArray()[r.nextInt(possiblePlacements.get(pt).size())];

        } else {
            System.out.println(this + " can't play");
        }

        Move m = new Move(this, piece, pos);
        notifyObservers(m);
    }
}