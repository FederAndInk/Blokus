package blokus.model.piecechooser;

import java.util.List;
import java.util.Random;

import blokus.model.Move;
import blokus.model.Node;
import blokus.model.Piece;

public class RandBigPieceChooser implements PieceChooser {
  Random r = new Random();

  @Override
  public Piece pickPiece(List<Piece> availablePieces) {
    int completeWeight = 0;
    for (Piece p : availablePieces) {
      completeWeight += p.size();
    }
    int rand = r.nextInt(completeWeight);
    int countWeight = 0;
    for (Piece p : availablePieces) {
      countWeight += p.size();
      if (countWeight >= rand) {
        return p;
      }
    }
    throw new RuntimeException("should never be shown");
  }

  @Override
  public Move pickMove(List<Move> moves) {
    int completeWeight = 0;
    for (Move m : moves) {
      completeWeight += m.getPiece().size();
    }
    int rand = r.nextInt(completeWeight);
    int countWeight = 0;
    for (Move m : moves) {
      countWeight += m.getPiece().size();
      if (countWeight >= rand) {
        return m;
      }
    }
    throw new RuntimeException("should never be shown");
  }

  @Override
  public Node pickNode(List<Node> nodes) {
    int completeWeight = 0;
    for (Node n : nodes) {
      completeWeight += n.getMove().getPiece().size();
    }
    int rand = r.nextInt(completeWeight);
    int countWeight = 0;
    for (Node n : nodes) {
      countWeight += n.getMove().getPiece().size();
      if (countWeight >= rand) {
        return n;
      }
    }
    throw new RuntimeException("should never be shown");

  }

  @Override
  public List<Node> selectNodes(List<Node> nodes) {
    return null;
  }

  @Override
  public List<Move> selectMoves(List<Move> moves) {
    return null;
  }

}