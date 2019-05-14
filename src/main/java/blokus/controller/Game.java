package blokus.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import blokus.model.APlayer;
import blokus.model.Board;
import blokus.model.Computer;
import blokus.model.Config;
import blokus.model.Move;
import blokus.model.Piece;
import blokus.model.PieceReader;
import blokus.model.Player;
import blokus.model.PlayerType;
import blokus.model.RandomComputer;
import javafx.scene.paint.Color;

/**
 * Class Game
 */
public class Game implements Observer {

  //
  // Fields
  //

  private ArrayList<APlayer> players = new ArrayList<>();
  private ArrayList<Piece> pieces = new ArrayList<>();

  private Board board;

  private APlayer curPlayer;
  private Observer app;

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
      players.add(new Player(c, pieces));
      break;
    case AI:
      players.add(new Computer(c, pieces));
      break;
    case RANDOM:
      players.add(new RandomComputer(c, pieces));
      break;
    }
    players.get(players.size() - 1).addObserver(this);
    if (players.size() == 1) {
      curPlayer = players.get(0);
      System.out.println(curPlayer + " turn");
    }
  }

  /**
  */
  public void nextPlayer() {
    if (!isEndOfGame()) {
      curPlayer = players.get((players.indexOf(curPlayer) + 1) % players.size());
      System.out.println(getCurPlayer() + " turn");
      if (getCurPlayer().hasToPass(board)) {
        System.out.println(getCurPlayer() + " passed");
        nextPlayer();
      }
    } else {
      System.out.println("Game is over");
    }
  }

  public void play(Move m) {
    m.doMove();
    nextPlayer();
    // SEE: save the move
  }

  public boolean isEndOfGame() {
    for (APlayer p : players) {
      if (!p.hasToPass(board)) {
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
  public void setApp(Observer app) {
    this.app = app;
    board.addObserver(app);
  }

  /**
   * function to call in main loop</br>
   * to update the model</br>
   * </br>
   * - AI computation when AI turn
   */
  public void refresh() {
    getCurPlayer().completeMove(board);
  }

  @Override
  public void update(Observable o, Object arg) {
    if (o instanceof APlayer) {
      APlayer player = (APlayer) o;
      // player has made a move
      if (arg instanceof Move) {
        Move m = (Move) arg;
        play(m);
      }
    }
  }

  //
  // Other methods
  //

}
