package blokus.model;

import java.util.ArrayList;

import blokus.controller.Game;

/**
 * Player
 */
public class Player extends APlayer {

  public Player(PColor color, ArrayList<Piece> pieces) {
    super(color, pieces);
  }

  public Player(Player p, Game g) {
    super(p, g);
  }

  public Player(APlayer aPlayer, Game g) {
    super(aPlayer, g);
  }

  @Override
  public APlayer copy(Game g) {
    return new Player(this, g);
  }
}
