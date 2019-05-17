package blokus.model;

/**
 * Class Move
 */
public class Move {

  //
  // Fields
  //

  private APlayer player;
  private Piece piece;
  private Board board;
  private Coord pos;

  //
  // Constructors
  //

  /**
   * @param player
   * @param piece
   * @param pos
   */
  public Move(APlayer player, Piece piece, Board board, Coord pos) {
    this.player = player;
    this.piece = piece;
    this.board = board;
    this.pos = pos;
  }

  //
  // Methods
  //

  /**
   * @param board
   */
  public void doMove() {
    player.play(piece, board, pos);
  }

  public void undoMove() {
    player.undo(piece, board);
  }

  //
  // Accessor methods
  //

  //
  // Other methods
  //

}
