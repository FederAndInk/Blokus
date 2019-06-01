package blokus.model.piecechooser;

import java.util.List;

import blokus.controller.Game;
import blokus.model.Move;
import blokus.model.Node;
import blokus.model.PColor;
import blokus.model.Piece;

/**
 * RoundPieceChooser
 */
public class RoundPieceChooser extends PieceChooser {
    PieceChooser pc;
    TwoHeuristicsPieceChooser tpc1;
    TwoHeuristicsPieceChooser tpc2;

    public RoundPieceChooser(PColor color) {
        super(color);
        pc = new RandPieceChooser(color);
        tpc1 = new TwoHeuristicsPieceChooser(color, new BigPieceChooser(color), new CenterPieceChooser(color));
        tpc2 = new TwoHeuristicsPieceChooser(color, new BigPieceChooser(color), new AdversaryLimitingChooser(color));
    }

    @Override
    public Piece pickPiece(List<Piece> availablePieces, Game game) {

        int round = (Integer) Game.getNbPieces() / game.getNbPlayers();
        if (round < 5) {
            return tpc1.pickPiece(availablePieces, game);
        } else {
            return tpc2.pickPiece(availablePieces, game);
        }

    }

    @Override
    public List<Piece> selectPieces(List<Piece> availablePieces, Game game) {
        int round = (Integer) Game.getNbPieces() / game.getNbPlayers();
        if (round < 5) {
            return tpc1.selectPieces(availablePieces, game);
        } else {
            return tpc2.selectPieces(availablePieces, game);
        }
    }

    @Override
    public Move pickMove(List<Move> moves) {
        Game g = moves.get(0).getGame();
        int round = (Integer) Game.getNbPieces() / g.getNbPlayers();
        if (round < 5) {
            return tpc1.pickMove(moves);
        } else {
            return tpc2.pickMove(moves);
        }
    }

    @Override
    public Node pickNode(List<Node> nodes) {
        Game g = nodes.get(0).getGame();
        int round = (Integer) Game.getNbPieces() / g.getNbPlayers();
        if (round < 5) {
            return tpc1.pickNode(nodes);
        } else {
            return tpc2.pickNode(nodes);
        }
    }

    @Override
    public List<Node> selectNodes(List<Node> nodes) {
        Game g = nodes.get(0).getGame();
        int round = (Integer) Game.getNbPieces() / g.getNbPlayers();
        if (round < 5) {
            return tpc1.selectNodes(nodes);
        } else {
            return tpc2.selectNodes(nodes);
        }
    }

    @Override
    public List<Move> selectMoves(List<Move> moves) {
        Game g = moves.get(0).getGame();
        int round = (Integer) Game.getNbPieces() / g.getNbPlayers();
        if (round < 5) {
            return tpc1.selectMoves(moves);
        } else {
            return tpc2.selectMoves(moves);
        }
    }
}