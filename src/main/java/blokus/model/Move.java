package blokus.model;

/**
 * Class Move
 */
public class Move {

  //
  // Fields
  //

  public APlayer player;
  public Piece piece;
  public Board board;
  public Coord pos;

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

  //
  // Accessor methods
  //

  //
  // Other methods
  //

}
