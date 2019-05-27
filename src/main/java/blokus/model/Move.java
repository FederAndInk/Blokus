package blokus.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import blokus.controller.Game;
import blokus.model.piecechooser.PieceChooser;

/**
 * Class Move
 */
public class Move implements Serializable {

  private static final long serialVersionUID = -1275634038759924769L;

  //
  // Fields
  //
  private static Random rand = new Random();

  private PColor playerColor;
  private transient Game game;
  private int noPiece;
  private PieceTransform trans;
  private Coord pos;
  private int value;

  //
  // Constructors
  //

  public Move(APlayer player, Piece piece, Game game, Coord pos, PieceTransform pt, int vl) {
    if (player != null) {
      this.playerColor = player.getColor();
    } else {
      playerColor = null;
    }
    this.game = game;
    if (piece != null) {
      this.noPiece = piece.no;
    } else {
      noPiece = -1;
    }
    this.trans = pt;
    this.pos = pos;
    this.value = vl;
  }

  public Move(APlayer player, Piece piece, Game game, Coord pos, PieceTransform pt) {
    this(player, piece, game, pos, pt, 0);
  }

  public Move(int vl) {
    this(null, null, null, null, null, vl);
  }

  public Move(Move m) {
    this.playerColor = m.playerColor;
    this.game = m.game;
    this.noPiece = m.noPiece;
    this.trans = m.trans;
    this.pos = m.pos;
    this.value = m.value;
  }

  //
  // Methods
  //

  /**
   * @param board
   */
  public void doMove() {
    Piece p = getPiece();
    p.apply(getTrans());
    getPlayer().play(p, game.getBoard(), pos);
  }

  public void undoMove() {
    getPlayer().undo(getPiece(), game.getBoard());
    game.undoDone();
    for (APlayer p : game.getPlayers()) {
      p.undoDone();
    }
  }

  public boolean isValid() {
    return noPiece >= 0 && playerColor != null && game != null && pos != null && trans != null;
  }

  public Move changeGame(Game game) {
    this.game = game;
    return this;
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
   * @return the value
   */
  public int getValue() {
    return value;
  }

  /**
   * @return the player
   */
  public APlayer getPlayer() {
    return game.getPlayer(playerColor);
  }

  public Piece getPiece() {
    Piece p = getPlayer().getPiece(noPiece);
    if (p == null) {
      p = game.getBoard().getPiece(playerColor, noPiece);
    }
    return p;
  }

  /**
   * @return the noPiece
   */
  public int getNoPiece() {
    return noPiece;
  }

  /**
   * @return the pos
   */
  public Coord getPos() {
    return pos;
  }

  /**
   * @return the trans
   */
  public PieceTransform getTrans() {
    return trans;
  }

  /**
   * @return the game
   */
  public Game getGame() {
    return game;
  }

  /**
   * @return the playerColor
   */
  public PColor getPlayerColor() {
    return playerColor;
  }

  //
  // Other methods
  //

  public static Move makeRandomPlayMove(Game game, APlayer player, PieceChooser pChooser) {
    Move m = null;
    ArrayList<Move> res = player.whereToPlayAll(game);
    if (!res.isEmpty()) {
      m = pChooser.pickMove(res);
    } else {
      System.out.println(player + " can't play");
    }
    return m;
  }

  public static Move makeRandomPieceMove(Game game, APlayer player, PieceChooser pChooser) {
    Move m = null;
    Piece piece = null;
    ArrayList<Move> possiblePlacements;
    ArrayList<Piece> piecesTmp = new ArrayList<>(player.getPieces());
    if (!piecesTmp.isEmpty()) {
      do {
        piece = pChooser.pickPiece(piecesTmp);
        piecesTmp.remove(piece);

        possiblePlacements = player.whereToPlay(piece, game);
      } while (possiblePlacements.isEmpty() && !piecesTmp.isEmpty());

      if (!possiblePlacements.isEmpty()) {
        m = possiblePlacements.get(rand.nextInt(possiblePlacements.size()));
      } else {
        System.out.println(player + " can't play");
      }
    }

    return m;
  }

  @Override
  public String toString() {
    return "Player: " + getPlayer() + "\nPlacement: \npiece:" + getPiece() + "\ntrans:" + getTrans() + "\npos:" + pos
        + "\nValue: " + value;
  }
}
