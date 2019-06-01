package blokus.model;

import java.util.ArrayList;
import java.util.Random;

import blokus.controller.Game;
import blokus.model.piecechooser.PieceChooser;

/**
 * RandomPieceAI
 */
public class RandomPieceAI extends APlayer {

  static Random r = new Random();
  PieceChooser pc;

  public RandomPieceAI(PColor color, ArrayList<Piece> pieces, PieceChooser pieceC) {
    super(color, pieces);
    pc = pieceC;
  }

  public RandomPieceAI(RandomPieceAI rpai) {
    this(rpai.getColor(), rpai.getPieces(), rpai.pc);
  }

  public RandomPieceAI(APlayer aPlayer, PieceChooser pc) {
    super(aPlayer);
    this.pc = pc;
  }

  @Override
  public APlayer copy() {
    return new RandomPieceAI(this);
  }

  @Override
  public Move completeMove(Game game) {
    return Move.makeRandomPieceMove(game, this, pc);
  }

  @Override
  public String info() {
    return "";
  }
}
