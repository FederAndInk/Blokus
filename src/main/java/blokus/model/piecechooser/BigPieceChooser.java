package blokus.model.piecechooser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import blokus.model.Move;
import blokus.model.Node;
import blokus.model.Piece;

public class BigPieceChooser implements PieceChooser {
  Random r = new Random();

  @Override
  public Piece pickPiece(List<Piece> availablePieces) {
    int max = availablePieces.stream().max((p1, p2) -> {
      return Integer.compare(p1.size(), p2.size());
    }).get().size();

    Piece[] ps = availablePieces.stream().filter((p) -> {
      return p.size() == max;
    }).toArray(Piece[]::new);
    return ps[r.nextInt(ps.length)];
  }

  @Override
  public Move pickMove(List<Move> moves) {
    int max = moves.stream().max((p1, p2) -> {
      return Integer.compare(p1.getPiece().size(), p2.getPiece().size());
    }).get().getPiece().size();

    Move[] ps = moves.stream().filter((p) -> {
      return p.getPiece().size() == max;
    }).toArray(Move[]::new);
    return ps[r.nextInt(ps.length)];
  }

  public List<Move> selectMoves(List<Move> moves) {
    int max = moves.stream().max((n1, n2) -> {
      return Integer.compare(n1.getPiece().size(), n2.getPiece().size());
    }).get().getPiece().size();
    int addedMoves = 0;
    ArrayList<Move> res = new ArrayList<>();
    for (Move n : moves) {
      if (n.getPiece().size() == max) {
        res.add(n);
        addedMoves++;
      }
      if (addedMoves > this.max) {
        return res;
      }
    }
    return res;
  }

  @Override
  public Node pickNode(List<Node> nodes) {
    int max = nodes.stream().max((n1, n2) -> {
      return Integer.compare(n1.getMove().getPiece().size(), n2.getMove().getPiece().size());
    }).get().getMove().getPiece().size();

    Node[] res = nodes.stream().filter((p) -> {
      return p.getMove().getPiece().size() == max;
    }).toArray(Node[]::new);
    return res[r.nextInt(res.length)];
  }

  @Override
  public List<Node> selectNodes(List<Node> nodes) {
    int max = nodes.stream().max((n1, n2) -> {
      return Integer.compare(n1.getMove().getPiece().size(), n2.getMove().getPiece().size());
    }).get().getMove().getPiece().size();
    int addedMoves = 0;
    ArrayList<Node> res = new ArrayList<>();
    for (Node n : nodes) {
      if (n.getMove().getPiece().size() == max) {
        res.add(n);
        addedMoves++;
      }
      if (addedMoves > this.max) {
        return res;
      }
    }
    return res;
  }

}