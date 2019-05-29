package blokus.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import blokus.controller.Game;
import blokus.model.piecechooser.PieceChooser;
import blokus.model.piecechooser.RandPieceChooser;

/**
 * Computer
 */

public class Computer extends APlayer {
  private Game game;
  private int maxDepth = 3;
  private int maxBranch = 110;
  private int minBranch = 50;
  private float maxPercentBranch = 0.9f;
  private PieceChooser pChooser;

  private int explored;

  public Computer(PColor color, ArrayList<Piece> pieces, PieceChooser pChooser) {
    super(color, pieces);
    this.pChooser = pChooser;
  }

  public Computer(PColor color, ArrayList<Piece> pieces, PieceChooser pChooser, int maxDepth) {
    this(color, pieces, pChooser);
    this.maxDepth = maxDepth;
  }

  public Computer(PColor color, ArrayList<Piece> pieces, PieceChooser pChooser, int maxDepth, int maxBranch) {
    this(color, pieces, pChooser, maxDepth);
    this.maxBranch = maxBranch;
  }

  public Computer(Computer c) {
    this(c.getColor(), c.getPieces(), c.pChooser, c.maxDepth, c.maxBranch);
    this.maxPercentBranch = c.maxPercentBranch;
  }

  public Computer(APlayer aPlayer, PieceChooser pc) {
    super(aPlayer);
    this.pChooser = pc;
  }

  public APlayer copy() {
    return new Computer(this);
  }

  @Override
  public Move completeMove(Game game) {
    this.game = game;
    // System.out.println("before chance to win: " + monteCarlo(200));
    Move m;
    explored = 0;
    m = minimaxLimit(game.getCurPlayer(), 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    System.out.println(explored + " branches explored");
    // System.out.println("after chance to win: " + monteCarlo(200));
    return m;
  }

  private Move minimaxLimit(APlayer curPlayer, int depth, int alpha, int beta) {
    ArrayList<Move> posPlacements = curPlayer.whereToPlayAll(game);
    ++explored;
    if (depth >= maxDepth || (posPlacements.isEmpty() && game.isEndOfGame())) {
      return new Move(evaluate());
    } else if (posPlacements.isEmpty()) { // if the current player has to pass
      return new Move(minimaxLimit(game.nextPlayer(curPlayer), depth + 1, alpha, beta).getValue());
    } else {
      int posPlacementsSize = posPlacements.size();
      if (maxBranch != Integer.MAX_VALUE || maxPercentBranch < 1) {
        if (posPlacementsSize > minBranch) {
          posPlacementsSize = Math.min(posPlacementsSize, (int) (maxPercentBranch * posPlacements.size()));
          posPlacementsSize = Math.min(posPlacementsSize, maxBranch);
        }
      }
      // if (depth == 0) {
      // System.out.println(posPlacementsSize + " plays to explore");
      // }
      Move bestMove;
      UpdateMM updateMM;

      if (curPlayer.equals(this)) {
        bestMove = new Move(Integer.MIN_VALUE);
        updateMM = maxUpdater;
      } else {
        bestMove = new Move(Integer.MAX_VALUE);
        updateMM = minUpdater;
      }
      for (int no = 1; no <= posPlacementsSize; ++no) {
        Move m = pChooser.pickMove(posPlacements);
        posPlacements.remove(m);
        m.doMove();
        m.setValue(minimaxLimit(game.nextPlayer(curPlayer), depth + 1, alpha, beta).getValue());
        m.undoMove();
        bestMove = updateMM.updateBestMove(m, bestMove);
        alpha = updateMM.updateAlpha(alpha, bestMove.getValue());
        beta = updateMM.updateBeta(beta, bestMove.getValue());
        // if (depth == 0) {
        // System.out.println(no + "/" + posPlacementsSize + " plays explored");
        // }
        if (beta <= alpha) {
          break;
        }
      }
      return bestMove;
    }
  }

  private int evaluate() {
    int eval = 0;
    HashMap<PColor, Integer> scores = game.getScore();
    eval += scores.remove(getColor());
    for (int sc : scores.values()) {
      eval -= sc;
    }
    return eval;
  }

  public static double monteCarlo(int nbGames, Game game, APlayer player) {
    PieceChooser pc = new RandPieceChooser();
    long bTime = System.currentTimeMillis();
    Stack<Move> moves = new Stack<>();
    int nbWin = 0;
    for (int i = 0; i < nbGames; ++i) {
      APlayer cur = game.getCurPlayer();
      boolean endOfGame = game.isEndOfGame();
      while (!endOfGame) {
        Move m = Move.makeRandomPieceMove(game, cur, pc);
        if (m != null) {
          moves.push(m);
          m.doMove();
        }
        cur = game.nextPlayer(cur);
        endOfGame = game.isEndOfGame();
        if (!endOfGame) {
          while (cur.hasPassed() || cur.whereToPlayAll(game).isEmpty()) {
            cur = game.nextPlayer(cur);
          }
        }
      }
      if (game.getWinner().contains(player)) {
        ++nbWin;
      }
      while (!moves.isEmpty()) {
        moves.pop().undoMove();
      }
    }
    System.out.println(nbGames + " games MC computed in " + (System.currentTimeMillis() - bTime) / 1_000.0 + "sc");
    return ((double) nbWin) / nbGames;
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
