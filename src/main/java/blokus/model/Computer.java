package blokus.model;

import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.Function;

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

  public class WeightedMove {
    Placement pl;
    int weight;

    WeightedMove(Placement p, int w) {
      pl = p;
      weight = w;
    }
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

  public interface UpdateMM {
    default int updateAlpha(int alpha, int bestVal) {
      return alpha;
    }

    default int updateBeta(int beta, int bestVal) {
      return beta;
    }

    int updateBest(int val, int bestVal);
  }

  private int minimax(APlayer curPlayer, int depth, int alpha, int beta) {
    if (game.isEndOfGame() || depth <= 0) {
      return game.getScore().get(this.getColor());
    } else {
      // maximizing Player's turn
      ArrayList<Placement> posPlacements = curPlayer.whereToPlayAll(game.getBoard());
      int bestVal;
      UpdateMM updateMM;
      if (curPlayer.equals(this)) {
        bestVal = -10000;
        updateMM = new UpdateMM() {
          @Override
          public int updateBest(int bestVal, int val) {
            return Math.max(bestVal, val);
          }

          @Override
          public int updateAlpha(int alpha, int bestVal) {
            return Math.max(bestVal, alpha);
          }
        };
      } else {
        bestVal = 10000;

        updateMM = new UpdateMM() {
          @Override
          public int updateBest(int bestVal, int val) {
            return Math.min(bestVal, val);
          }

          @Override
          public int updateBeta(int beta, int bestVal) {
            return Math.min(bestVal, beta);
          }
        };
      }
      for (Placement pl : posPlacements) {
        Piece p = pl.piece;
        p.apply(pl.trans);
        Move m = new Move(curPlayer, p, game.getBoard(), pl.pos);
        m.doMove();
        int value = minimax(game.nextPlayer(curPlayer), depth - 1, alpha, beta);
        m.undoMove();
        bestVal = updateMM.updateBest(value, bestVal);
        alpha = updateMM.updateAlpha(alpha, bestVal);
        beta = updateMM.updateBeta(beta, bestVal);
        if (beta <= alpha) {
          break;
        }
      }
      return bestVal;
    }
  }
}
