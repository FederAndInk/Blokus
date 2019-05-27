package blokus.model.piecechooser;

import java.io.Serializable;
import java.util.List;

import blokus.model.Move;
import blokus.model.Node;
import blokus.model.Piece;

public interface PieceChooser extends Serializable {
  int max = 5;

  Piece pickPiece(List<Piece> availablePieces);

  Move pickMove(List<Move> moves);

  List<Move> selectMoves(List<Move> moves);

  Node pickNode(List<Node> nodes);

  List<Node> selectNodes(List<Node> nodes);

}