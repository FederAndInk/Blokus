package blokus.model.piecechooser;

import java.util.List;

import blokus.controller.Game;
import blokus.model.Move;
import blokus.model.Node;
import blokus.model.Piece;

/**
 * RoundPieceChooser
 */
public class RoundPieceChooser implements PieceChooser {
    PieceChooser pc = new RandPieceChooser();
    TwoHeuristicsPieceChooser tpc1 = new TwoHeuristicsPieceChooser(new BigPieceChooser(), new CenterPieceChooser());
    TwoHeuristicsPieceChooser tpc2 = new TwoHeuristicsPieceChooser(new BigPieceChooser(),
            new AdversaryLimitingChooser());

    @Override
    public Piece pickPiece(List<Piece> availablePieces) {
        throw new IllegalStateException("RoundPieceChooser's pickPiece method not applicable");

    }

    @Override
    public Move pickMove(List<Move> moves) {
        throw new IllegalStateException("RoundPieceChooser's pickMove method not applicable");
    }

    @Override
    public Node pickNode(List<Node> nodes) {
        Game g = nodes.get(0).getGame();
        int round = (Integer) g.getNbPieces() / 2;
        if (round < 20) {
            return tpc1.pickNode(nodes);
        } else {
            return tpc2.pickNode(nodes);
        }
    }

    @Override
    public List<Node> selectNodes(List<Node> nodes) {
        return null;
    }

}