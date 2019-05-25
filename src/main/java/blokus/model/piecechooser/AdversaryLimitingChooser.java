package blokus.model.piecechooser;

import java.util.List;
import java.util.Random;
import java.util.Set;

import blokus.controller.Game;
import blokus.model.APlayer;
import blokus.model.Coord;
import blokus.model.Move;
import blokus.model.Node;
import blokus.model.Piece;

/**
 * AdversaryLimitingChooser
 */
public class AdversaryLimitingChooser implements PieceChooser {
    Random r = new Random();
    PieceChooser pc;

    @Override
    public Piece pickPiece(List<Piece> availablePieces) {
        return null;
    }

    @Override
    public Move pickMove(List<Move> moves) {
        return null;
    }

    // a quicker version of the obsutructing the other player startegy
    @Override
    public Node pickNode(List<Node> nodes) {
        Game g = nodes.get(0).getGame();
        APlayer p = g.getCurPlayer();
        APlayer adv = g.nextPlayer(p);
        int max = Integer.MIN_VALUE;
        Node res = null;
        Set<Coord> accCorners = g.getBoard().getAccCorners(adv.getColor());
        for (Node n : nodes) {
            int count = 0;
            for (Coord c : accCorners) {
                if (n.getGame().getBoard().get(c.x, c.y) == p.getColor()) {
                    count++;
                }
            }
            if (count > max) {
                max = count;
                res = n;
            }
        }
        if (res == null) {
            return pc.pickNode(nodes);
        } else {
            return res;
        }
    }

    // a quicker version of the obsutructing the other player startegy
    public Node quickPickNode(List<Node> nodes) {
        Game g = nodes.get(0).getGame();
        APlayer p = g.getCurPlayer();
        APlayer adv = g.nextPlayer(p);
        Set<Coord> accCorners = g.getBoard().getAccCorners(adv.getColor());
        for (Node n : nodes) {
            for (Coord c : accCorners) {
                if (n.getGame().getBoard().get(c.x, c.y) == p.getColor()) {
                    return n;
                }
            }
        }
        return pc.pickNode(nodes);
    }
}