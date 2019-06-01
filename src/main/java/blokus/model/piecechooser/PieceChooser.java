package blokus.model.piecechooser;

import java.io.Serializable;
import java.util.List;

import blokus.controller.Game;
import blokus.model.Move;
import blokus.model.Node;
import blokus.model.PColor;
import blokus.model.Piece;

public abstract class PieceChooser implements Serializable {
  private static final long serialVersionUID = -8389535021178179823L;
  private PColor color;

  public PieceChooser(PColor color) {
    this.color = color;
  }

  /**
   * @return the color
   */
  public PColor getColor() {
    return color;
  }

  /**
   * @param color the color to set
   */
  public void setColor(PColor color) {
    this.color = color;
  }

  abstract public Piece pickPiece(List<Piece> availablePieces, Game game);

  abstract public List<Piece> selectPieces(List<Piece> availablePieces, Game game);

  abstract public Move pickMove(List<Move> moves);

  abstract public List<Move> selectMoves(List<Move> moves);

  abstract public Node pickNode(List<Node> nodes);

  abstract public List<Node> selectNodes(List<Node> nodes);
}