package blokus.controller;

import java.util.ArrayList;
import java.util.HashMap;

import blokus.model.APlayer;
import blokus.model.Board;
import blokus.model.Computer;
import blokus.model.Config;
import blokus.model.Coord;
import blokus.model.Move;
import blokus.model.Piece;
import blokus.model.PieceChooser;
import blokus.model.PieceReader;
import blokus.model.Player;
import blokus.model.PlayerType;
import blokus.model.RandPieceChooser;
import blokus.model.RandomPieceAI;
import blokus.view.IApp;
import javafx.scene.paint.Color;

/**
 * Class Game
 */
public class Game {

  //
  // Fields
  //

  private ArrayList<APlayer> players = new ArrayList<>();
  private ArrayList<Piece> pieces = new ArrayList<>();

  private Board board;

  private APlayer curPlayer;
  private IApp app;

  //
  // Constructors
  //
  public Game() {
    Config.i();
    board = new Board();

    PieceReader pr = new PieceReader(Config.loadRsc("pieces"));
    Piece p;
    while ((p = pr.nextPiece()) != null) {
      pieces.add(p);
    }
    Config.i().logger().info("read " + pieces.size() + " pieces");
  };

  //
  // Methods
  //

  public void addPlayer(PlayerType pt) {
    Color c = Board.colors.get(players.size());
    switch (pt) {
    case USER:
    case AI:
      addPlayer(pt, null);
      break;
    case RANDOM_PIECE:
    case RANDOM_PLAY:
      addPlayer(pt, new RandPieceChooser());
      break;
    }
  }

  public void addPlayer(PlayerType pt, PieceChooser pieceChooser) {
    Color c = Board.colors.get(players.size());
    switch (pt) {
    case USER:
      players.add(new Player(c, pieces));
      break;
    case AI:
      players.add(new Computer(c, pieces));
      break;
    case RANDOM_PIECE:
      players.add(new RandomPieceAI(c, pieces, pieceChooser));
      break;
    case RANDOM_PLAY:

    }

    if (players.size() == 1) {
      curPlayer = players.get(0);
      System.out.println(curPlayer + " turn");
    }
  }

  /**
  */
  private void nextPlayer() {
    if (!isEndOfGame()) {
      curPlayer = players.get((players.indexOf(curPlayer) + 1) % players.size());
      System.out.println(getCurPlayer() + " turn");
      if (getCurPlayer().whereToPlayAll(board).isEmpty()) {
        System.out.println(getCurPlayer() + " passed");
        nextPlayer();
      }
    } else {
      System.out.println("Game is over");
    }
  }

  private void play(Move m) {
    m.doMove();
    nextPlayer();
    app.update();
    // SEE: save the move
  }

  /**
   * the player (user) input a move
   */
  public void inputPlay(Piece p, Coord pos) {
    play(new Move(getCurPlayer(), p, getBoard(), pos));
  }

  public boolean isEndOfGame() {
    for (APlayer p : players) {
      if (!p.whereToPlayAll(board).isEmpty()) {
        return false;
      }
    }
    return true;
  }

  public HashMap<Color, Integer> getScore() {
    // case where one of the players get either +20 points or +15 points
    HashMap<Color, Integer> score = board.numOfEachColor();
    for (APlayer p : players) {
      if (p.getPieces().isEmpty()) {
        ArrayList<Piece> pieces = board.getPlayed(p.getColor());
        Piece lastPiece = pieces.get(pieces.size() - 1);
        Integer res = score.get(p.getColor());
        if (lastPiece.getShape().size() == 1) {
          score.put(p.getColor(), res + 20);
        } else {
          score.put(p.getColor(), res + 15);
        }
      }
    }
    return score;
  }

  /**
   * function to call in main loop</br>
   * to update the model</br>
   * </br>
   * - AI computation when AI turn
   */
  public void refresh() {
    Move m = getCurPlayer().completeMove(board);
    if (m != null) {
      play(m);
    }
  }

  //
  // Accessor methods
  //
  /**
   * @return the board
   */
  public Board getBoard() {
    return board;
  }

  /**
   * @return the curPlayer
   */
  public APlayer getCurPlayer() {
    return curPlayer;
  }

  /**
   * @param app the app to set
   */
  public void setApp(IApp app) {
    this.app = app;
  }

  public ArrayList<APlayer> getPlayers() {
    return this.players;
  }

  public int getNbPlayers() {
    return players.size();
  }

  public int getCurPlayerNo() {
    return Board.getColorId(getCurPlayer().getColor());
  }
}
