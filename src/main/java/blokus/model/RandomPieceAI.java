package blokus.model;

import java.util.ArrayList;
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

    public RandomPieceAI(RandomPieceAI rpai) {
        this(rpai.getColor(), rpai.getPieces(), rpai.pc);
    }

    @Override
    public APlayer copy() {
        return new RandomPieceAI(this);
    }

    @Override
    public Move completeMove(Game game) {
        return makeMove(game, this, pc);
    }

    public static Move makeMove(Game game, APlayer player, PieceChooser pChooser) {
        Move m = null;
        Board board = game.getBoard();
        Piece piece = null;
        ArrayList<Placement> possiblePlacements;
        ArrayList<Piece> piecesTmp = new ArrayList<>(player.getPieces());
        if (!piecesTmp.isEmpty()) {
            do {
                piece = pChooser.pickPiece(piecesTmp);
                piecesTmp.remove(piece);

                possiblePlacements = player.whereToPlay(piece, board);
            } while (possiblePlacements.isEmpty() && !piecesTmp.isEmpty());

            if (!possiblePlacements.isEmpty()) {
                Placement placement = possiblePlacements.get(r.nextInt(possiblePlacements.size()));

                m = new Move(player, placement, game, 0);
            } else {
                System.out.println(player + " can't play");
            }
        }

        return m;
    }

}
