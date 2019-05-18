package blokus.model;

import java.util.ArrayList;
import java.util.HashMap;

import blokus.controller.Game;
import javafx.scene.paint.Color;

/**
 * Computer
 */

public class Computer extends APlayer {
  Game game;
  int maxDepth = 3;
  int maxPiece = Integer.MAX_VALUE;
  PieceChooser pChooser;

  public Computer(Color color, ArrayList<Piece> pieces, PieceChooser pChooser) {
    super(color, pieces);
    this.pChooser = pChooser;
  }

  public Computer(Color color, ArrayList<Piece> pieces, PieceChooser pChooser, int maxDepth) {
    this(color, pieces, pChooser);
    this.maxDepth = maxDepth;
  }

  public Computer(Color color, ArrayList<Piece> pieces, PieceChooser pChooser, int maxDepth, int maxPiece) {
    this(color, pieces, pChooser, maxDepth);
    this.maxPiece = maxPiece;
  }

  @Override
  public Move completeMove(Game game) {
    this.game = game;
    return minimax(game.getCurPlayer(), 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
  }

  private Move minimax(APlayer curPlayer, int depth, int alpha, int beta) {
    ArrayList<Placement> posPlacements = curPlayer.whereToPlayAll(game.getBoard());
    if (depth >= maxDepth || (posPlacements.isEmpty() && game.isEndOfGame())) {
      return new Move(curPlayer, null, game.getBoard(), evaluate());
    } else {
      int posPlacementsSize = Math.min(posPlacements.size(), maxPiece);
      if (depth == 0) {
        System.out.println(posPlacementsSize + " plays to explore");
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
      for (int no = 1; no <= posPlacementsSize; no++) {
        Placement pl = pChooser.pickPlacement(posPlacements);
        posPlacements.remove(pl);
        Move m = new Move(curPlayer, pl, game.getBoard(), 0);
        m.doMove();
        m.setValue(minimax(game.nextPlayer(curPlayer), depth + 1, alpha, beta).getValue());
        m.undoMove();
        bestMove = updateMM.updateBestMove(m, bestMove);
        alpha = updateMM.updateAlpha(alpha, bestMove.getValue());
        beta = updateMM.updateBeta(beta, bestMove.getValue());
        if (depth == 0) {
          System.out.println(no + "/" + posPlacementsSize + " plays explored");
        }
        if (beta <= alpha) {
          break;
        }
      }
      return bestMove;
    }
  }

  private int evaluate() {
    int eval = 0;
    HashMap<Color, Integer> scores = game.getScore();
    eval += scores.remove(getColor());
    for (int sc : scores.values()) {
      eval -= sc;
    }
    return eval;
  }

  private interface UpdateMM {
    default int updateAlpha(int alpha, int bestVal) {
      return alpha;
    }

    default int updateBeta(int beta, int bestVal) {
      return beta;
    }

    Move updateBestMove(Move m, Move bestMove);
  }

  private static final UpdateMM maxUpdater = new UpdateMM() {
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

  private static final UpdateMM minUpdater = new UpdateMM() {
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
