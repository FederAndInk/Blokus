package model;

/**
 * Class Move
 */
public class Move {

  //
  // Fields
  //

  private APlayer player;

  private Piece piece;

  private Coord pos;

  //
  // Constructors
  //

  /**
   * @param player
   * @param piece
   * @param pos
   */
  public Move(APlayer player, Piece piece, Coord pos) {
    this.player = player;
    this.piece = piece;
    this.pos = pos;
  }

  //
  // Methods
  //

  /**
   * @param board
   */
  public void doMove(Board board) {
    player.play(piece, board, pos);
  }

  //
  // Accessor methods
  //

  //
  // Other methods
  //

}
