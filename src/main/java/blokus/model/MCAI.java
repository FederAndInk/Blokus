package blokus.model;

import java.util.ArrayList;
import java.util.List;

import blokus.controller.Game;
import blokus.model.piecechooser.PieceChooser;
import blokus.model.piecechooser.RandPieceChooser;

/**
 * MCAI
 */
public class MCAI extends APlayer {
  private Game game;
  PieceChooser pc;
  PieceChooser randPc = new RandPieceChooser();

  public MCAI(PColor color, ArrayList<Piece> pieces, PieceChooser pc) {
    super(color, pieces);
    this.pc = pc;
  }

  public MCAI(MCAI mcai) {
    this(mcai.getColor(), mcai.getPieces(), mcai.pc);
  }

  @Override
  public APlayer copy() {
    return new MCAI(this);
  }

  public Move completeMove(Game game) {
    this.game = game;
    long msec = 10000;
    Node n = monteCarlo(msec);
    System.out.println("MCTS most visited visits: " + n.getVisits());
    return n.getMove();
  }

  public Node monteCarlo(long msec) {
    long ms = System.currentTimeMillis() + msec;
    Node rootNode = new Node(null, game, null);
    while (ms > System.currentTimeMillis()) {
      // Selection
      Node selectedNode = selection(rootNode);

      Node expandedNode = selectedNode;
      if (!selectedNode.getGame().isEndOfGame()) {
        // Expansion
        expandedNode = fullExpansionRandomSelection(selectedNode);
      }

      // Simulation
      boolean gameResult = simulation(expandedNode.getGame());

      // Backpropagation
      Backpropagation(expandedNode, gameResult);
    }
    System.out.println("MC nb visits: " + rootNode.getVisits());
    Node node = rootNode.getMostVisitedNode();
    node.getMove().changeGame(game);
    return node;
  }

  private void Backpropagation(Node node, boolean gameResult) {
    while (node != null) {
      node.update(gameResult);
      node = node.getParent();
    }
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
  public Node fullExpansionRandomSelection(Node node) {
    Game g = node.getGame();
    APlayer p = g.getCurPlayer();
    ArrayList<Move> posPl = p.whereToPlayAll(g);
    for (Move pl : posPl) {
      Game gCpy = g.copy();
      gCpy.setOutput(false);
      gCpy.inputPlay(pl);
      Node childNode = new Node(pl, gCpy, node);
      node.addChild(childNode);
    }

    return pc.pickNode(node.getChildren());
  }

  public Node heuristicExpansionAndSelection(Node node) {
    Game g = node.getGame();
    APlayer p = g.getCurPlayer();
    List<Move> posPl = pc.selectMoves(p.whereToPlayAll(g));
    for (Move pl : posPl) {
      Game gCpy = g.copy();
      gCpy.setOutput(false);
      gCpy.inputPlay(pl);
      Node childNode = new Node(pl, gCpy, node);
      node.addChild(childNode);
    }
    return pc.pickNode(node.getChildren());
  }

  /**
   * add all possible placements to the node in the tree and select the child with
   * the highest UCT value
   */
  public Node fullExpansionUCTSelection(Node node) {
    Game g = node.getGame();
    APlayer p = g.getCurPlayer();
    ArrayList<Move> posPl = p.whereToPlayAll(g);
    double uct = 0;
    Node highestUCTNode = null;
    for (Move m : posPl) {
      Game game = g.copy();
      game.setOutput(false);
      game.inputPlay(m);
      Node childNode = new Node(m, game, node);
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
  public Node randomExpansion(Node node) {
    Game g = node.getGame().copy();
    g.setOutput(false);
    APlayer p = g.getCurPlayer();
    ArrayList<Move> posPl = p.whereToPlayAll(g);
    Move m = pc.pickMove(posPl);
    Node childNode = new Node(m, g, node);
    node.addChild(childNode);
    return childNode;
  }

  public boolean simulation(Game g) {
    Game gTmp = g.copy();
    // System.out.println("Sim:\n" + g.getBoard());
    gTmp.setOutput(false);
    while (!gTmp.isEndOfGame()) {
      APlayer p = gTmp.getCurPlayer();
      ArrayList<Move> posPl = p.whereToPlayAll(gTmp);
      if (!posPl.isEmpty()) {
        Move pl = randPc.pickMove(posPl);
        gTmp.inputPlay(pl);
      } else {
        System.out.println(p + " can't play");
        System.out.println(":\n" + gTmp.getBoard());
      }
    }
    boolean res = gTmp.getWinner().contains(this);
    return res;
  }
}