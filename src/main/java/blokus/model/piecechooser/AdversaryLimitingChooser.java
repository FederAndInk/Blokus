package blokus.model.piecechooser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import blokus.controller.Game;
import blokus.model.APlayer;
import blokus.model.Coord;
import blokus.model.Move;
import blokus.model.Node;
import blokus.model.PColor;
import blokus.model.Piece;

/**
 * AdversaryLimitingChooser
 */
public class AdversaryLimitingChooser extends PieceChooser {
  Random r = new Random();
  PieceChooser pc;

  public AdversaryLimitingChooser(PColor color) {
    super(color);
    pc = new RandPieceChooser(color);
  }

  @Override
  public Piece pickPiece(List<Piece> availablePieces, Game game) {
    Set<Coord> advCoords = game.getBoard().getAdvAccCorners(getColor());
    List<Piece> res = new ArrayList<>();
    int max = availablePieces.stream().max((n1, n2) -> {
      return Integer.compare(n1.intersectCount(advCoords), n2.intersectCount(advCoords));
    }).get().intersectCount(advCoords);

    for (Piece m : availablePieces) {
      if (m.intersectCount(advCoords) == max) {
        res.add(m);
      }
    }
    return res.get(r.nextInt(res.size()));

  }

  @Override
  public List<Piece> selectPieces(List<Piece> availablePieces, Game game) {
    List<Piece> res = new ArrayList<>();
    Set<Coord> advCoords = game.getBoard().getAdvAccCorners(getColor());
    int max = availablePieces.stream().max((n1, n2) -> {
      return Integer.compare(n1.intersectCount(advCoords), n2.intersectCount(advCoords));
    }).get().intersectCount(advCoords);

    for (Piece m : availablePieces) {
      if (m.intersectCount(advCoords) == max) {
        res.add(m);
      }
    }
    return res;
  }

  @Override
  public Move pickMove(List<Move> moves) {
    List<Move> res = new ArrayList<>();
    int max = moves.stream().max((n1, n2) -> {
      return Integer.compare(n1.advBlockingCount(), n2.advBlockingCount());
    }).get().advBlockingCount();

    for (Move m : moves) {
      if (m.advBlockingCount() == max) {
        res.add(m);
      }
    }
    return res.get(r.nextInt(res.size()));
  }

  @Override
  public List<Move> selectMoves(List<Move> moves) {
    List<Move> res = new ArrayList<>();
    int max = moves.stream().max((n1, n2) -> {
      return Integer.compare(n1.advBlockingCount(), n2.advBlockingCount());
    }).get().advBlockingCount();

    for (Move m : moves) {
      if (m.advBlockingCount() == max) {
        res.add(m);
      }
    }
    return res;
  }

  // a quicker version of the obsutructing the other player startegy
  @Override
  public Node pickNode(List<Node> nodes) {
    List<Node> res = new ArrayList<>();
    int max = nodes.stream().max((n1, n2) -> {
      return Integer.compare(n1.getMove().advBlockingCount(), n2.getMove().advBlockingCount());
    }).get().getMove().advBlockingCount();

    for (Node n : nodes) {
      if (n.getMove().advBlockingCount() == max) {
        res.add(n);
      }
    }
    return res.get(r.nextInt(res.size()));
  }

  @Override
  public List<Node> selectNodes(List<Node> nodes) {
    List<Node> res = new ArrayList<>();
    int max = nodes.stream().max((n1, n2) -> {
      return Integer.compare(n1.getMove().advBlockingCount(), n2.getMove().advBlockingCount());
    }).get().getMove().advBlockingCount();

    for (Node n : nodes) {
      if (n.getMove().advBlockingCount() == max) {
        res.add(n);
      }
    }
    return res;

  }

  // a quicker version of the obsutructing the other player startegy
  public Node quickPickNode(List<Node> nodes) {
    Game g = nodes.get(0).getGame();
    APlayer p = g.getPlayer(getColor());
    APlayer adv = g.nextPlayer(p);
    Set<Coord> accCorners = g.getBoard().getAccCorners(adv.getColor());
    for (Node n : nodes) {
      for (Coord c : accCorners) {
        if (n.getGame().getBoard().get(c.x, c.y) == p.getColor()) {
          return n;
        }
      }
    }
    return pc.pickNode(nodes);
  }

}