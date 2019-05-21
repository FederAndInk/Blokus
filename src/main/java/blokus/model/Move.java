package blokus.model;

import java.util.ArrayList;
import java.util.Random;

import blokus.controller.Game;

/**
 * Class Move
 */
public class Move {

  //
  // Fields
  //
  private static Random rand = new Random();
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

  public static Move makeRandomPlayMove(Game game, APlayer player, PieceChooser pChooser) {
    Move m = null;
    Board board = game.getBoard();
    ArrayList<Placement> res = player.whereToPlayAll(board);
    if (!res.isEmpty()) {
      Placement pl = pChooser.pickPlacement(res);
      m = new Move(player, pl, game, 0);
    } else {
      System.out.println(player + " can't play");
    }
    return m;
  }

  public static Move makeRandomPieceMove(Game game, APlayer player, PieceChooser pChooser) {
    Move m = null;
    Board board = game.getBoard();
    Piece piece = null;
    ArrayList<Placement> possiblePlacements;
    ArrayList<Piece> piecesTmp = new ArrayList<>(player.getPieces());
    if (!piecesTmp.isEmpty()) {
      do {
        piece = pChooser.pickPiece(piecesTmp);
        piecesTmp.remove(piece);

        possiblePlacements = player.whereToPlay(piece, board);
      } while (possiblePlacements.isEmpty() && !piecesTmp.isEmpty());

      if (!possiblePlacements.isEmpty()) {
        Placement placement = possiblePlacements.get(rand.nextInt(possiblePlacements.size()));
        m = new Move(player, placement, game, 0);
      } else {
        System.out.println(player + " can't play");
      }
    }

    return m;
  }

  @Override
  public String toString() {
    return "Player: " + player + "\nPlacement: " + placement + "\nValue: " + value;
  }
}
