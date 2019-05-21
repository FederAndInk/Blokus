package blokus.model;

import java.util.ArrayList;

import blokus.controller.Game;
import javafx.scene.paint.Color;

/**
 * MCAI
 */
public class MCAI extends APlayer {
    private Game game;
    PieceChooser pc;

    public MCAI(Color color, ArrayList<Piece> pieces, Game g, PieceChooser pc) {
        super(color, pieces);
        this.pc = pc;
    }

    public Move completeMove(Game game) {
        this.game = game;
        long msec = 10;
        monteCarlo(msec);
        return null;
    }

    public Move monteCarlo(long msec) {
        long ms = System.currentTimeMillis() + msec;
        Node rootNode = new Node(null, game, null);
        while (ms > System.currentTimeMillis()) {
            Node node = rootNode;
            Game g = node.getGame().copy();

            // Selection
            Node selectedNode = selection(node);

            // Expansion
            if (g.isEndOfGame()) {
                Node expandedNode = fullExpansionRandomSelection(node, g);
            }

            // Simulation
            boolean gameResult = simulation(node, g);
        }
        return null;
    }

    public Node selection(Node node) {
        while (!node.isTerminal()) {
            node = node.selectChild();
        }
        return node;
    }

    /**
     * add all possible placements to the node in the tree and select one of the
     * children randomly
     */
    public Node fullExpansionRandomSelection(Node node, Game g) {
        APlayer p = g.getCurPlayer();
        ArrayList<Placement> posPl = p.whereToPlayAll(g.getBoard());
        for (Placement pl : posPl) {
            Move m = new Move(p, pl, g, 0);
            m.doMove();
            Node childNode = new Node(m, g, node);
            // SEE: undo move afterwards??????
            node.addChild(childNode);
        }

        return node.randomChildSelection();
    }

    /**
     * add all possible placements to the node in the tree and select the child with
     * the highest UCT value
     */
    public Node fullExpansionUCTSelection(Node node, Game g) {
        APlayer p = g.getCurPlayer();
        ArrayList<Placement> posPl = p.whereToPlayAll(g.getBoard());
        double uct = 0;
        Node highestUCTNode = null;
        for (Placement pl : posPl) {
            Move m = new Move(p, pl, g, 0);
            m.doMove();
            Node childNode = new Node(m, g, node);
            // SEE: undo move afterwards??????
            childNode.addChild(childNode);
            if (uct < childNode.computeUCT()) {
                uct = childNode.computeUCT();
                highestUCTNode = childNode;
            }
        }

        return highestUCTNode;
    }

    /**
     * Randomly generate a node using piece chooser class and adding it to the tree
     */
    public Node randomExpansion(Node node, Game g) {
        APlayer p = g.getCurPlayer();
        ArrayList<Placement> posPl = p.whereToPlayAll(g.getBoard());
        Placement pl = pc.pickPlacement(posPl);
        Move m = new Move(p, pl, g, 0);
        m.doMove();
        Node childNode = new Node(m, g, node);
        // SEE: undo move afterwards??????
        node.addChild(childNode);
        return childNode;
    }

    public boolean simulation(Node node, Game g) {
        return false;
    }

}