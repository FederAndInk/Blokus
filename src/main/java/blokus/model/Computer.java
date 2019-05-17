package blokus.model;

import java.util.ArrayList;

import blokus.controller.Game;
import blokus.model.Placement;
import javafx.scene.paint.Color;

/**
 * Computer
 */

public class Computer extends APlayer {

  static int MAX = 1000;
  static int MIN = -1000;

  Game game;

  public Computer(Color color, ArrayList<Piece> pieces) {
    super(color, pieces);
  }

  @Override
  public Move completeMove(Game game) {
    this.game = game;

    int bestVal = -10000;
    Placement bestPl = null;
    Board board = game.getBoard();
    ArrayList<Placement> plays = whereToPlayAll(board);
    System.out.println(plays.size() + " plays to explore");
    int no = 1;
    for (Placement pl : plays) {
      Piece p = pl.piece;
      p.apply(pl.trans);
      Move m = new Move(this, p, board, pl.pos);
      m.doMove();
      int moveVal = minimax(game.nextPlayer(this), 2, MIN, MAX);
      m.undoMove();

      if (moveVal > bestVal) {
        bestVal = moveVal;
        bestPl = pl;
      }
      System.out.println(no + "/" + plays.size() + " plays explored");
      no++;
    }
    bestPl.piece.apply(bestPl.trans);
    return new Move(this, bestPl.piece, board, bestPl.pos);
  }

  private int minimax(APlayer curPlayer, int depth, int alpha, int beta) {
    if (game.isEndOfGame() || depth <= 0) {
      return game.getScore().get(this.getColor());
    } else {
      // maximizing Player's turn
      if (curPlayer.equals(this)) {
        int bestVal = -10000;
        ArrayList<Placement> posPlacements = curPlayer.whereToPlayAll(game.getBoard());
        for (Placement pl : posPlacements) {
          Piece p = pl.piece;
          p.apply(pl.trans);
          Move m = new Move(curPlayer, p, game.getBoard(), pl.pos);
          m.doMove();
          int value = minimax(game.nextPlayer(curPlayer), depth - 1, alpha, beta);
          m.undoMove();
          bestVal = Math.max(bestVal, value);
          alpha = Math.max(alpha, bestVal);
          if (beta <= alpha) {
            break;
          }
        }
        return bestVal;

      } else {
        int bestVal = 10000;
        ArrayList<Placement> posPlacements = curPlayer.whereToPlayAll(game.getBoard());
        for (Placement pl : posPlacements) {
          Piece p = pl.piece;
          p.apply(pl.trans);
          Move m = new Move(curPlayer, p, game.getBoard(), pl.pos);
          m.doMove();
          int value = minimax(game.nextPlayer(curPlayer), depth - 1, alpha, beta);
          m.undoMove();
          bestVal = Math.min(bestVal, value);
          beta = Math.min(beta, bestVal);
          if (beta <= alpha) {
            break;
          }
        }
        return bestVal;

      }
    }
  }
}
