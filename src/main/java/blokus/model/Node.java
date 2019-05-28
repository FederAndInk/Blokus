package blokus.model;

import java.util.ArrayList;
import java.util.Random;

import blokus.controller.Game;

/**
 * Node
 */
public class Node {
  private static final double UCT_CONST = 0.5;
  private int visits;
  private int wins;
  private Move move;
  Game game;
  private Node parent;
  private ArrayList<Node> children;
  Random r = new Random();

  // Constructor
  Node(Move m, Game g, Node p) {
    visits = 0;
    wins = 0;
    move = m;
    game = g;
    parent = p;
    children = new ArrayList<>();
    if (parent != null) {
      parent.addChild(this);
    }
  }

  // methods
  public boolean isTerminal() {
    return this.children.isEmpty();
  }

  public double computeUCT() {
    if (this.visits == 0) {
      return Integer.MAX_VALUE;
    }
    return (this.wins / (this.visits))
        + (UCT_CONST * Math.sqrt(Math.log(parent.getVisits() + 0.00001) / (visits + 0.00001)));
  }

  public Node selectChild() {
    children.sort((c1, c2) -> {
      return Double.compare(c1.computeUCT(), c2.computeUCT());
    });
    int first = children.size() - 1;
    double max = children.get(first).computeUCT();
    for (; first >= 0 && children.get(first).computeUCT() == max; first--) {
    }
    first++;
    return children.get(r.nextInt(children.size() - first) + first);
  }

  public Node getMostVisitedNode() {
    children.sort((c1, c2) -> {
      return Double.compare(c1.visits, c2.visits);
    });
    return children.get(children.size() - 1);
  }

  public Node getMostProfitableNode() {
    children.sort((c1, c2) -> {
      return Double.compare(c1.wins, c2.wins);
    });
    return children.get(children.size() - 1);
  }

  // setters and getters
  /**
   * @return the parent
   */
  public Node getParent() {
    return parent;
  }

  /**
   * @return the game
   */
  public Game getGame() {
    return game;
  }

  /**
   * @param game the game to set
   */
  public void setGame(Game game) {
    this.game = game;
  }

  /**
   * @return the visits
   */
  public int getVisits() {
    return visits;
  }

  /**
   * @param visits the visits to set
   */
  public void setVisits(int visits) {
    this.visits = visits;
  }

  /**
   * @return the children
   */
  public ArrayList<Node> getChildren() {
    return children;
  }

  /**
   * @param children the children to set
   */
  public void setChildren(ArrayList<Node> children) {
    this.children = children;
  }

  public void addChild(Node n) {
    children.add(n);
  }

  /**
   * @return the wins
   */
  public int getWins() {
    return wins;
  }

  /**
   * @param wins the wins to set
   */
  public void setWins(int wins) {
    this.wins = wins;
  }

  /**
   * @return the move
   */
  public Move getMove() {
    return move;
  }

  /**
   * @param move the move to set
   */
  public void setMove(Move move) {
    this.move = move;
  }

  public void update(boolean wins) {
    visits++;
    if (wins) {
      this.wins++;
    }
  }

  @Override
  public String toString() {
    if (!isTerminal()) {
      return getMove().toString();
    } else {
      String ret = "";
      for (Node n : children) {
        ret += n.toString();
      }
      return ret;
    }
  }

}