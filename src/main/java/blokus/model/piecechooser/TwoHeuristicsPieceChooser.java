package blokus.model.piecechooser;

import java.util.List;

import blokus.model.Move;
import blokus.model.Node;
import blokus.model.Piece;

/**
 * TwoHeuristicsPieceChooser
 */
public class TwoHeuristicsPieceChooser implements PieceChooser {
    PieceChooser pc1;
    PieceChooser pc2;

    @Override
    public Piece pickPiece(List<Piece> availablePieces) {
        return null;
    }

    @Override
    public Move pickMove(List<Move> moves) {
        return null;
    }

    @Override
    public Node pickNode(List<Node> nodes) {
        return pc2.pickNode(pc1.selectNodes(nodes));
    }

    @Override
    public List<Node> selectNodes(List<Node> nodes) {
        return null;
    }

}