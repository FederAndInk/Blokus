package blokus.model.piecechooser;

import java.util.List;

import blokus.controller.Game;
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
    public Piece pickPiece(List<Piece> availablePieces, Game game) {
        return pc2.pickPiece(pc1.selectPieces(availablePieces, game), game);
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
        return pc2.selectNodes(pc1.selectNodes(nodes));
    }

    @Override
    public List<Move> selectMoves(List<Move> moves) {
        return pc2.selectMoves(pc1.selectMoves(moves));
    }

    @Override
    public List<Piece> selectPieces(List<Piece> availablePieces, Game game) {
        return pc2.selectPieces(pc1.selectPieces(availablePieces, game), game);
    }

    /**
     * @return the pc1
     */
    public PieceChooser getPc1() {
        return pc1;
    }

    /**
     * @return the pc2
     */
    public PieceChooser getPc2() {
        return pc2;
    }
}