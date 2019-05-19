package blokus.model;

/**
 * Class Move
 */
public class Move {

  //
  // Fields
  //

  private APlayer player;
  private Board board;
  private Placement placement;
  private int value;

  //
  // Constructors
  //

  /**
   * @param player
   * @param board
   * @param pos
   */
  public Move(APlayer player, Piece piece, Board board, Coord pos, PieceTransform pt, int vl) {
    this.player = player;
    this.placement = new Placement(piece, pt, pos);
    this.board = board;
    this.value = vl;
  }

  public Move(APlayer player, Placement pl, Board board, int value) {
    this.player = player;
    this.board = board;
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
    player.play(placement.piece, board, placement.pos);
  }

  public void undoMove() {
    player.undo(placement.piece, board);
  }

  public boolean isValid() {
    return placement != null && player != null && board != null && placement.piece != null && placement.pos != null
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

  //
  // Other methods
  //
  @Override
  public String toString() {
    return "Player: " + player + "Placement: " + placement + "Value: " + value;
  }
}
