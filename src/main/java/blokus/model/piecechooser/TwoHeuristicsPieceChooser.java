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

    public TwoHeuristicsPieceChooser(PieceChooser pc1, PieceChooser pc2) {
        this.pc1 = pc1;
        this.pc2 = pc2;
    }

    @Override
    public Piece pickPiece(List<Piece> availablePieces) {
        throw new IllegalStateException("TwoHeuristicsPieceChooser's pickPiece method not applicable");
    }

    @Override
    public Move pickMove(List<Move> moves) {
        return pc2.pickMove(pc1.selectMoves(moves));
    }

    @Override
    public Node pickNode(List<Node> nodes) {
        return pc2.pickNode(pc1.selectNodes(nodes));
    }

    @Override
    public List<Node> selectNodes(List<Node> nodes) {
        throw new IllegalStateException("TwoHeuristicsPieceChooser's selectNodes method not applicable");
    }

    @Override
    public List<Move> selectMoves(List<Move> moves) {
        throw new IllegalStateException("TwoHeuristicsPieceChooser's selectMoves method not applicable");
    }

}