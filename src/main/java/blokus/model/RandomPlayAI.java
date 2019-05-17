package blokus.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import blokus.controller.Game;
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

    @Override
    public Move completeMove(Game game) {
        Move m = null;
        Board board = game.getBoard();
        ArrayList<Placement> res = whereToPlayAll(board);
        if (!res.isEmpty()) {
            Placement rand = pc.pickPlacement(res);
            rand.piece.apply(rand.trans);
            m = new Move(this, rand.piece, board, rand.pos);
        } else {
            System.out.println(this + " can't play");
        }
        return m;
    }
}