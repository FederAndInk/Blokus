package blokus.model;

import java.util.ArrayList;

/**
 * Player
 */
public class Player extends APlayer {

  public Player(PColor color, ArrayList<Piece> pieces) {
    super(color, pieces);
  }

  public Player(Player p) {
    super(p);
  }

  public Player(APlayer aPlayer) {
    super(aPlayer);
  }

  @Override
  public APlayer copy() {
    return new Player(this);
  }

  @Override
  public String info() {
    return "User";
  }
}
