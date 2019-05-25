package blokus.model;

import java.util.ArrayList;
import java.util.Random;

import blokus.controller.Game;
import blokus.model.piecechooser.PieceChooser;
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
    return Move.makeRandomPieceMove(game, this, pc);
  }
}
