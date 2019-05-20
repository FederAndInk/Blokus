package blokus.model;

import blokus.controller.Game;

/**
 * Class Move
 */
public class Move {

  //
  // Fields
  //

  private APlayer player;
  private Game game;
  private Placement placement;
  private int value;

  //
  // Constructors
  //

  public Move(APlayer player, Piece piece, Game game, Coord pos, PieceTransform pt, int vl) {
    this.player = player;
    this.placement = new Placement(piece, pt, pos);
    this.game = game;
    this.value = vl;
  }

  public Move(APlayer player, Placement pl, Game game, int value) {
    this.player = player;
    this.game = game;
    this.placement = pl;
    this.value = value;
  }

  //
  // Methods
  //

  /**
   * @param board
   */
  public void doMove() {
    placement.piece.apply(placement.trans);
    player.play(placement.piece, game.getBoard(), placement.pos);
  }

  public void undoMove() {
    player.undo(placement.piece, game.getBoard());
    game.undoDone();
    for (APlayer p : game.getPlayers()) {
      p.undoDone();
    }
  }

  public boolean isValid() {
    return placement != null && player != null && game != null && placement.piece != null && placement.pos != null
        && placement.trans != null;
  }

  //
  // Accessor methods
  //
  /**
   * @param value the value to set
   */
  public void setValue(int value) {
    this.value = value;
  }

  /**
   * @param placement the placement to set
   */
  public void setPlacement(Placement placement) {
    this.placement = placement;
  }

  /**
   * @return the value
   */
  public int getValue() {
    return value;
  }

  /**
   * @return the placement
   */
  public Placement getPlacement() {
    return placement;
  }

  /**
   * @return the player
   */
  public APlayer getPlayer() {
    return player;
  }

  //
  // Other methods
  //
  @Override
  public String toString() {
    return "Player: " + player + "Placement: " + placement + "Value: " + value;
  }
}
