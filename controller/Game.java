package controller;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import model.APlayer;
import model.Board;
import model.Computer;
import model.Player;
import view.App;

/**
 * Class Game
 */
public class Game {
  ArrayList<Color> colors = new ArrayList<>();
  //
  // Fields
  //

  private ArrayList<APlayer> players = new ArrayList<>();

  private Board board;

  private APlayer curPlayer;
  private App app;

  //
  // Constructors
  //
  public Game(App app, int nbPlayers) {
    board = new Board();
    colors.add(Color.BLUE);
    colors.add(Color.YELLOW);
    colors.add(Color.RED);
    colors.add(Color.GREEN);
    for (int i = 0; i < nbPlayers; i++) {
      players.add(new Player(colors.get(i)));
    }
    curPlayer = players.get(0);
    this.app = app;
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
      players.add(new Computer(c));
    } else {
      players.add(new Player(c));
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
