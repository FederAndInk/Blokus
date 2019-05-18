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

    @Override
    public Move completeMove(Game game) {
        return makeMove(game, this, pc);
    }

    public static Move makeMove(Game game, APlayer player, PieceChooser pChooser) {
        Move m = null;
        Board board = game.getBoard();
        ArrayList<Placement> res = player.whereToPlayAll(board);
        if (!res.isEmpty()) {
            Placement rand = pChooser.pickPlacement(res);
            rand.piece.apply(rand.trans);
            m = new Move(player, rand, board, 0);
        } else {
            System.out.println(player + " can't play");
        }
        return m;
    }
}