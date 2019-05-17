package blokus.model;

import java.util.ArrayList;
import java.util.Currency;
import java.util.function.BiFunction;
import java.util.function.Function;

import blokus.controller.Game;
import blokus.model.Placement;
import javafx.scene.paint.Color;

/**
 * Computer
 */

public class Computer extends APlayer {
  Game game;
  int maxDepth = 3;

  public Computer(Color color, ArrayList<Piece> pieces) {
    super(color, pieces);
  }

  @Override
  public Move completeMove(Game game) {
    this.game = game;
    return minimax(game.getCurPlayer(), 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
  }

  private Move minimax(APlayer curPlayer, int depth, int alpha, int beta) {
    if (game.isEndOfGame() || depth >= maxDepth) {
      return new Move(curPlayer, null, game.getBoard(), game.getScore().get(this.getColor()));
    } else {
      ArrayList<Placement> posPlacements = curPlayer.whereToPlayAll(game.getBoard());
      if (depth == 0) {
        System.out.println(posPlacements.size() + " plays to explore");
      }
      Move bestMove;
      UpdateMM updateMM;

      if (curPlayer.equals(this)) {
        bestMove = new Move(curPlayer, null, game.getBoard(), Integer.MIN_VALUE);
        updateMM = maxUpdater;
      } else {
        bestMove = new Move(curPlayer, null, game.getBoard(), Integer.MAX_VALUE);
        updateMM = minUpdater;
      }
      int no = 1;
      for (Placement pl : posPlacements) {
        Piece p = pl.piece;
        p.apply(pl.trans);
        Move m = new Move(curPlayer, pl, game.getBoard(), 0);
        m.doMove();
        m.setValue(minimax(game.nextPlayer(curPlayer), depth + 1, alpha, beta).getValue());
        m.undoMove();
        bestMove = updateMM.updateBestMove(m, bestMove);
        alpha = updateMM.updateAlpha(alpha, bestMove.getValue());
        beta = updateMM.updateBeta(beta, bestMove.getValue());
        if (depth == 0) {
          System.out.println(no + "/" + posPlacements.size() + " plays explored");
        }
        no++;
        if (beta <= alpha) {
          break;
        }
      }
      return bestMove;
    }
  }

  public interface UpdateMM {
    default int updateAlpha(int alpha, int bestVal) {
      return alpha;
    }

    default int updateBeta(int beta, int bestVal) {
      return beta;
    }

    Move updateBestMove(Move m, Move bestMove);
  }

  static final UpdateMM maxUpdater = new UpdateMM() {
    @Override
    public Move updateBestMove(Move m, Move bestMove) {
      if (m.getValue() > bestMove.getValue()) {
        return m;
      }
      return bestMove;
    }

    @Override
    public int updateAlpha(int alpha, int bestVal) {
      return Math.max(bestVal, alpha);
    }
  };

  static final UpdateMM minUpdater = new UpdateMM() {
    @Override
    public Move updateBestMove(Move m, Move bestMove) {
      if (m.getValue() < bestMove.getValue()) {
        return m;
      }
      return bestMove;
    }

    @Override
    public int updateBeta(int beta, int bestVal) {
      return Math.min(bestVal, beta);
    }
  };
}
