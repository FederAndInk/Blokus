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
      addPlayer(pt, null);
      break;
    case AI:
      addPlayer(pt, new RandPieceChooser());
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
      players.add(new Computer(c, pieces, pieceChooser));
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
      curPlayer = nextPlayer(curPlayer);
      System.out.println(getCurPlayer() + " turn");
      while (getCurPlayer().whereToPlayAll(board).isEmpty()) {
        System.out.println(getCurPlayer() + " passed");
        if (getCurPlayer().getPieces().isEmpty()) {
          System.out.println("no more pieces");
        } else {
          System.out.println("no more space to play");
          System.out.println(getCurPlayer().getPieces().size() + " pieces remaining");
        }

        curPlayer = nextPlayer(curPlayer);
      }
    } else {
      System.out.println("Game is over");
    }
  }

  public APlayer nextPlayer(APlayer p) {
    return players.get((players.indexOf(p) + 1) % players.size());
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
    play(new Move(getCurPlayer(), p, getBoard(), pos, p.getState(), 0));
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
      score.putIfAbsent(p.getColor(), 0);
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
    Move m = getCurPlayer().completeMove(this);
    if (m != null) {
      play(m);
    } else {
      System.out.println("null move");
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
