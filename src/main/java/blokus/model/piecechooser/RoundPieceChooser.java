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
    public Piece pickPiece(List<Piece> availablePieces, Game game) {

        int round = (Integer) game.getNbPieces() / game.getNbPlayers();
        if (round < 10) {
            return tpc1.pickPiece(availablePieces, game);
        } else {
            return tpc2.pickPiece(availablePieces,game);
        }

    }

    @Override
    public Move pickMove(List<Move> moves) {
        Game g = moves.get(0).getGame();
        int round = (Integer) g.getNbPieces() / g.getNbPlayers();
        if (round < 10) {
            return tpc1.pickMove(moves);
        } else {
            return tpc2.pickMove(moves);
        }
    }

    @Override
    public Node pickNode(List<Node> nodes) {
        Game g = nodes.get(0).getGame();
        int round = (Integer) g.getNbPieces() / g.getNbPlayers();
        if (round < 20) {
            return tpc1.pickNode(nodes);
        } else {
            return tpc2.pickNode(nodes);
        }
    }

    @Override
    public List<Node> selectNodes(List<Node> nodes) {
        Game g = nodes.get(0).getGame();
        int round = (Integer) g.getNbPieces() / g.getNbPlayers();
        if (round < 20) {
            return tpc1.selectNodes(nodes);
        } else {
            return tpc2.selectNodes(nodes);
        }
    }

    @Override
    public List<Move> selectMoves(List<Move> moves) {
        Game g = moves.get(0).getGame();
        int round = (Integer) g.getNbPieces() / g.getNbPlayers();
        if (round < 10) {
            return tpc1.selectMoves(moves);
        } else {
            return tpc2.selectMoves(moves);
        }
    }

}