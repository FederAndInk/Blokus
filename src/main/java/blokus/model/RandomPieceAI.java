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

    static Random r = new Random();
    PieceChooser pc;

    public RandomPieceAI(Color color, ArrayList<Piece> pieces, PieceChooser pieceC) {
        super(color, pieces);
        pc = pieceC;
    }

    @Override
    public Move completeMove(Game game) {
        return makeMove(game, this, pc);
    }

    public static Move makeMove(Game game, APlayer player, PieceChooser pChooser) {
        Move m = null;
        Board board = game.getBoard();
        Coord pos;
        Piece piece = null;
        HashMap<PieceTransform, HashSet<Coord>> possiblePlacements;
        ArrayList<Piece> piecesTmp = new ArrayList<>(player.getPieces());
        if (!piecesTmp.isEmpty()) {
            do {
                piece = pChooser.pickPiece(piecesTmp);
                piecesTmp.remove(piece);

                possiblePlacements = player.whereToPlay(piece, board);
            } while (possiblePlacements.isEmpty() && !piecesTmp.isEmpty());

            if (!possiblePlacements.isEmpty()) {
                PieceTransform pt = (PieceTransform) possiblePlacements.keySet().toArray()[r
                        .nextInt(possiblePlacements.keySet().size())];
                piece.apply(pt);
                pos = (Coord) possiblePlacements.get(pt).toArray()[r.nextInt(possiblePlacements.get(pt).size())];

                m = new Move(player, piece, board, pos, pt, 0);
            } else {
                System.out.println(player + " can't play");
            }
        }

        return m;
    }

}
