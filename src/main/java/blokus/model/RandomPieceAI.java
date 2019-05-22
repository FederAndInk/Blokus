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

    @Override
    public Move completeMove(Game game) {
        return Move.makeRandomPieceMove(game, this, pc);
    }
}
