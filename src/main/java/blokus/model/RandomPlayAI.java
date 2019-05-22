package blokus.model;

import java.util.ArrayList;
import java.util.Random;

import blokus.controller.Game;
import javafx.scene.paint.Color;

/**
 * RandomPlayAI
 */
public class RandomPlayAI extends APlayer {
    static Random r = new Random();
    PieceChooser pc;

    public RandomPlayAI(Color color, ArrayList<Piece> pieces, PieceChooser pieceC) {
        super(color, pieces);
        pc = pieceC;

    }

    public RandomPlayAI(RandomPlayAI rpai) {
        this(rpai.getColor(), rpai.getPieces(), rpai.pc);
    }

    @Override
    public APlayer copy() {
        return new RandomPlayAI(this);
    }

    @Override
    public Move completeMove(Game game) {
        return Move.makeRandomPlayMove(game, this, pc);
    }
}