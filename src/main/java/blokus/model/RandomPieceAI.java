package blokus.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import blokus.controller.Game;
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
    public Move completeMove(Game game) {
        Move m = null;
        Board board = game.getBoard();
        Coord pos = new Coord(0, 0);
        Piece piece = null;
        HashMap<PieceTransform, HashSet<Coord>> possiblePlacements = new HashMap<>();
        ArrayList<Piece> piecesTmp = new ArrayList<>(getPieces());
        do {
            piece = pc.pickPiece(piecesTmp);
            piecesTmp.remove(piece);

            possiblePlacements = whereToPlay(piece, board);
        } while (possiblePlacements.isEmpty() && !piecesTmp.isEmpty());

        if (!possiblePlacements.isEmpty()) {
            PieceTransform pt = (PieceTransform) possiblePlacements.keySet().toArray()[r
                    .nextInt(possiblePlacements.keySet().size())];
            piece.apply(pt);
            pos = (Coord) possiblePlacements.get(pt).toArray()[r.nextInt(possiblePlacements.get(pt).size())];

            m = new Move(this, piece, board, pos);
        } else {
            System.out.println(this + " can't play");
        }
        return m;
    }
}
