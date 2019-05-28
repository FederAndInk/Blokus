package blokus.model.piecechooser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import blokus.controller.Game;
import blokus.model.Move;
import blokus.model.Node;
import blokus.model.Piece;

public class RandPieceChooser implements PieceChooser {
  int num = 7;
  Random r = new Random();

  @Override
  public Piece pickPiece(List<Piece> availablePieces, Game game) {
    return availablePieces.get(r.nextInt(availablePieces.size()));
  }

  @Override
  public Move pickMove(List<Move> moves) {
    return moves.get(r.nextInt(moves.size()));
  }

  @Override
  public Node pickNode(List<Node> nodes) {
    return nodes.get(r.nextInt(nodes.size()));
  }

  @Override
  public List<Node> selectNodes(List<Node> nodes) {
    ArrayList<Node> res = new ArrayList<>();
    for (int i = 0; i < this.num; i++) {
      res.add(nodes.get(r.nextInt(nodes.size())));
    }
    return res;
  }

  @Override
  public List<Move> selectMoves(List<Move> moves) {
    ArrayList<Move> res = new ArrayList<>();
    for (int i = 0; i < this.num; i++) {
      res.add(moves.get(r.nextInt(moves.size())));
    }
    return res;
  }

}