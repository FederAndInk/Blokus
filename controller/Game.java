package controller;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import model.APlayer;
import model.Board;
import model.Computer;
import model.Config;
import model.Piece;
import model.PieceReader;
import model.Player;
import view.App;

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
  private App app;

  //
  // Constructors
  //
  public Game(App app, int nbPlayers) {
    board = new Board();
    this.app = app;

    Config.i();

    PieceReader pr = new PieceReader(Config.loadRsc("pieces"));
    Piece p;
    while ((p = pr.nextPiece()) != null) {
      pieces.add(p);
    }
    Config.i().logger().info("read " + pieces.size() + " pieces");

    for (int i = 0; i < nbPlayers; i++) {
      players.add(new Player(board.colors.get(i), pieces));
    }
    curPlayer = players.get(0);
  };

  //
  // Methods
  //

  public Board getBoard() {
    return this.board;
  }

  public APlayer getAPlayer() {
    return this.curPlayer;
  }

  public App getApp() {
    return this.app;
  }

  public ArrayList<APlayer> getPlayers() {
    return this.players;
  }

  public void addPlayer(Color c, boolean computer) {
    if (computer) {
      players.add(new Computer(c, pieces));
    } else {
      players.add(new Player(c, pieces));
    }
  }

  public int getNbPlayers() {
    return players.size();
  }

  /**
  */
  public void nextPlayer() {
    curPlayer = players.get((players.indexOf(curPlayer) + 1) % players.size());
  }

  //
  // Accessor methods
  //

  //
  // Other methods
  //

}
