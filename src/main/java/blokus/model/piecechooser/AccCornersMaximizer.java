package blokus.model.piecechooser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import blokus.controller.Game;
import blokus.model.APlayer;
import blokus.model.Move;
import blokus.model.Node;
import blokus.model.PColor;
import blokus.model.Piece;

/**
 * AccCornersMaximizer
 */
public class AccCornersMaximizer extends PieceChooser {
  private static Random random = new Random();

  public AccCornersMaximizer(PColor color) {
    super(color);
  }

  @Override
  public Piece pickPiece(List<Piece> availablePieces, Game game) {
    List<Piece> ret = selectPieces(availablePieces, game);
    return ret.get(random.nextInt(ret.size()));
  }

  @Override
  public List<Piece> selectPieces(List<Piece> availablePieces, Game game) {
    APlayer p = game.getPlayer(getColor());

    double[] means = new double[Game.getNbPieces()];

    // SEE: to choose between mean or max
    for (Piece piece : availablePieces) {
      ArrayList<Move> placements = p.whereToPlay(piece, game);
      if (!placements.isEmpty()) {
        for (Move m : placements) {
          means[piece.no] += m.accessibleCornersCount();
        }
        means[piece.no] /= placements.size();
      }
    }

    ArrayList<Piece> ret = new ArrayList<>();

    double max = 0.0001;
    for (int i = 0; i < means.length; i++) {
      if (means[i] > max) {
        max = means[i];
        ret.clear();
        ret.add(p.getPiece(i));
      } else if (means[i] > (max - 0.5)) {
        ret.add(p.getPiece(i));
      }
    }

    return ret;
  }

  @Override
  public Move pickMove(List<Move> moves) {
    List<Move> ret = selectMoves(moves);
    return ret.get(random.nextInt(ret.size()));
  }

  @Override
  public List<Move> selectMoves(List<Move> moves) {
    ArrayList<Move> list = new ArrayList<>();
    int max = 0;

    for (Move m : moves) {
      int nb = m.accessibleCornersCount();
      if (nb > max) {
        max = nb;
        list.clear();
        list.add(m);
      } else if (nb == max) {
        list.add(m);
      }
    }

    return list;
  }

  @Override
  public Node pickNode(List<Node> nodes) {
    throw new UnsupportedOperationException("not supported for now");
  }

  @Override
  public List<Node> selectNodes(List<Node> nodes) {
    throw new UnsupportedOperationException("not supported for now");
  }
}