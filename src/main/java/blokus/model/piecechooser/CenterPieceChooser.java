package blokus.model.piecechooser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import blokus.model.Coord;
import blokus.model.Move;
import blokus.model.Node;
import blokus.model.Piece;

/**
 * CenterPieceChooser
 */
public class CenterPieceChooser implements PieceChooser {
  Random r = new Random();

  @Override
  public Piece pickPiece(List<Piece> availablePieces) {
    return null;
  }

  @Override
  public Move pickMove(List<Move> moves) {
    return null;
  }

  // using move's attribute to choose piece performance better than the other
  // function
  @Override

  public Node pickNode(List<Node> nodes) {
    ArrayList<Node> res = new ArrayList<>();
    double min = Double.POSITIVE_INFINITY;
    int size = nodes.get(0).getGame().getBoard().getSize() / 2;
    Coord center = new Coord(size, size);
    for (Node n : nodes) {
      double distance = euclideanDistance(n.getMove().getPos(), center);
      if (distance == min) {
        res.add(n);
        min = distance;
      } else if (distance < min) {
        res.clear();
        res.add(n);
        min = distance;
      }

    }
    return res.get(r.nextInt(res.size()));
  }

  // // computes the distance between the corners of each piece and the center
  // // preformance worse than the other function
  // @Override
  // public Node pickNode(List<Node> nodes) {
  // ArrayList<Node> res = new ArrayList<>();
  // double min = Double.POSITIVE_INFINITY;
  // int size = nodes.get(0).getGame().getBoard().getSize() / 2;
  // Coord center = new Coord(size, size);
  // for (Node n : nodes) {
  // for (Coord c : n.getMove().getPiece().getCorners()) {
  // double distance = euclideanDistance(c, center);
  // if (distance == min) {
  // res.add(n);
  // min = distance;
  // } else if (distance < min) {
  // res.clear();
  // res.add(n);
  // min = distance;
  // }
  // }

  // }
  // return res.get(r.nextInt(res.size()));
  // }

  public double euclideanDistance(Coord c1, Coord c2) {
    return Math.sqrt(Math.pow(c2.x - c1.x, 2) + Math.pow(c2.y - c1.y, 2));
  }

}